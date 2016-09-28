package com.richrelevance.internal.net;

import com.richrelevance.Error;

/**
 * Class which contains the logic of how to make a web request and interpret the response.
 *
 * @param <T> The type of data that will be returned as the response to the request.
 */
public interface WebRequest<T> {

    public interface ResultCallback<T> {
        public void onSuccess(T result);

        public void onError(Error error);
    }

    /**
     * Called to obtain a {@link WebRequestBuilder} that contains all the parameters and info needed to
     * perform the request.
     *
     * @return A request builder representing the contents of the request.
     */
    public WebRequestBuilder getRequestBuilder();

    /**
     * Interprets the given response and obtains the result.
     *
     * @param response       The response to interpret.
     * @param resultCallback The callback to send results to.
     */
    public void translate(WebResponse response, ResultCallback<T> resultCallback);
}
