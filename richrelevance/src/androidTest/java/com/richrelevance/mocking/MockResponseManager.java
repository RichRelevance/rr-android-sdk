package com.richrelevance.mocking;

import android.content.Context;

import com.richrelevance.internal.net.SimpleResultCallback;
import com.richrelevance.internal.net.WebRequest;
import com.richrelevance.internal.net.WebRequestBuilder;
import com.richrelevance.internal.net.WebResultInfo;
import com.richrelevance.Error;

import java.util.HashMap;
import java.util.Map;

/**
 * Class which maintains a set of mocked responses.
 */
public class MockResponseManager {

    private static final MockResponseManager INSTANCE = new MockResponseManager();

    public static MockResponseManager getInstance() {
        return INSTANCE;
    }

    private Context context;
    private Map<WebRequestBuilder, MockWebResponse> responseMap;

    private MockResponseManager() {
        responseMap = new HashMap<>();
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * Gets a mocked result which corresponds to the given request or a failure if none is found.
     * @param request The request to get a response for.
     * @param <T> The result type of the request.
     * @return The mocked result of the request.
     */
    public <T> WebResultInfo<T> getResult(WebRequest<T> request) {
        MockWebResponse response = responseMap.get(request.getRequestBuilder());

        if (response != null) {
            SimpleResultCallback<T> callback = new SimpleResultCallback<>();
            request.translate(response, callback);
            return new MockWebResultInfo<>(response.getResponseCode(), callback.getResult(), callback.getError());
        } else {
            Error notFoundError = new Error(Error.ErrorType.Unknown, "No mocked response found");
            return new MockWebResultInfo<>(WebResultInfo.RESPONSE_CODE_FAILED, null, notFoundError);
        }
    }

    /**
     * Stores the given response as the response to the given request. All requests with an equivalent request builder
     * will be responded to using the given response.
     * @param requestBuilder The request builder to test for equality against future requests.
     * @param response The response to return if a matching request is sent.
     */
    public void putResponse(WebRequestBuilder requestBuilder, ResponseBuilder response) {
        responseMap.put(requestBuilder, new MockWebResponse(response, context));
    }

    private static class MockWebResultInfo<Result> implements WebResultInfo<Result> {

        private int responseCode;
        private Result result;
        private long timestamp;
        private Error error;

        public MockWebResultInfo(int responseCode, Result result, Error error) {
            this.responseCode = responseCode;
            this.result = result;
            this.timestamp = System.currentTimeMillis();
            this.error = error;
        }

        @Override
        public long getResponseTimestamp() {
            return timestamp;
        }

        @Override
        public Result getResult() {
            return result;
        }

        @Override
        public Error getError() {
            return error;
        }

        public void setResult(Result result) {
            this.result = result;
        }

        @Override
        public int getResponseCode() {
            return responseCode;
        }

        @Override
        public String getResponseMessage() {
            return null;
        }
    }
}
