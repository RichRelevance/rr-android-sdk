package com.richrelevance.internal.net;

/**
 * Interface which provides {@link WebRequestExecutor}s.
 */
public interface WebRequestExecutorFactory {

    /**
     * Gets a {@link WebRequestExecutor} which executes the given request.
     *
     * @param <Result>       The type of result being returend.
     * @param request        The request to execute.
     * @param connectTimeout The connect timeout to set on the request.
     * @param readTimeout    The read timeout to set on t he request.
     * @return An executor to execute the given request.
     */
    public <Result> WebRequestExecutor<Result> create(WebRequest<Result> request, int connectTimeout, int readTimeout);
}
