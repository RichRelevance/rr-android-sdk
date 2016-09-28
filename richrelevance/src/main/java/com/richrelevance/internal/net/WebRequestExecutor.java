package com.richrelevance.internal.net;

/**
 * Defines a mechanism for executing a web request and retrieving a result.
 *
 * @param <Result> The type of the result that will be returned.
 */
public interface WebRequestExecutor<Result> {

    /**
     * Executes this request and returns the result.
     *
     * @return A {@link WebResultInfo} containing the result.
     */
    public WebResultInfo<Result> execute();
}
