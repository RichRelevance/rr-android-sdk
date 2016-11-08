package com.richrelevance.internal.net;

import com.richrelevance.Error;
import com.richrelevance.RRLog;
import com.richrelevance.internal.net.oauth.OAuthConfig;
import com.richrelevance.internal.net.oauth.signpost.OAuthConsumer;
import com.richrelevance.internal.net.oauth.signpost.exception.OAuthCommunicationException;
import com.richrelevance.internal.net.oauth.signpost.exception.OAuthExpectationFailedException;
import com.richrelevance.internal.net.oauth.signpost.exception.OAuthMessageSignerException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Implementation of a {@link WebRequestExecutor} which runs the request over an
 * {@link HttpURLConnection}.
 *
 * @param <Result> The type of the result that will be returned.
 */
class HttpUrlConnectionExecutor<Result> implements WebRequestExecutor<Result> {

    private WebRequest<Result> request;
    private int connectionTimeout;
    private int readTimeout;

    public HttpUrlConnectionExecutor(WebRequest<Result> request, int connectionTimeout, int readTimeout) {
        this.request = request;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
    }

    @Override
    public WebResultInfo<Result> execute() {
        WebRequestBuilder builder = request.getRequestBuilder();
        HttpURLConnection connection = getConnection(builder);

        if (connection != null) {
            OAuthConfig oAuthConfig = builder.getOAuthConfig();
            if (oAuthConfig != null) {
                try {
                    new OAuthConsumer(oAuthConfig.getConsumerKey(), oAuthConfig.getConsumerSecret()).sign(connection);
                } catch (OAuthMessageSignerException | OAuthExpectationFailedException | OAuthCommunicationException e) {
                    RRLog.e(getClass().getSimpleName(), "Error signing OAuth", e);
                }
            }

            connection.setConnectTimeout(connectionTimeout);
            connection.setReadTimeout(readTimeout);
            try {
                connection.connect();
                onConnected(builder, connection);

                WebResponse response = new HttpURLConnectionResponse(connection);
                SimpleResultCallback<Result> callback = new SimpleResultCallback<>();

                request.translate(response, callback);

                return new BasicWebResultInfo<>(
                        callback.getResult(),
                        callback.getError(),
                        System.currentTimeMillis(),
                        response.getResponseCode(),
                        response.getResponseMessage());
            } catch (IOException e) {
                RRLog.i(getClass().getSimpleName(), "Error opening connection. Connection failed.", e);
            } finally {
                connection.disconnect();
            }
        }

        return new FailedResultInfo<>(System.currentTimeMillis(), new Error(Error.ErrorType.HttpError, "Connection failure"));
    }

    /**
     * Gets a {@link HttpURLConnection} which can be used to execute this request.
     *
     * @return The connection to use to execute the request.
     */
    private HttpURLConnection getConnection(WebRequestBuilder builder) {
        try {
            // Get our current URL
            URL url = new URL(builder.getFullUrl());
            // "Open" the connection, which really just gives us the object, doesn't
            // actually connect
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Set the request method appropriately
            connection.setRequestMethod(builder.getMethod().getMethodName());

            // Add all headers
            for (Map.Entry<String, String> entry : builder.getHeaders().entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            return connection;

        } catch (IOException e) {
            RRLog.e(getClass().getName(), e.getMessage(), e);
        }

        return null;
    }


    /**
     * Call after the connection has been established to allow adding the rest of the data.
     *
     * @param connection The opened connection.
     */
    private void onConnected(WebRequestBuilder builder, HttpURLConnection connection) {
    }

    public static final WebRequestExecutorFactory FACTORY = new WebRequestExecutorFactory() {
        @Override
        public <T> WebRequestExecutor<T> create(WebRequest<T> request, int connectTimeout, int readTimeout) {
            return new HttpUrlConnectionExecutor<T>(request, connectTimeout, readTimeout);
        }
    };
}
