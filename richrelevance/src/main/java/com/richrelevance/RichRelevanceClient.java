package com.richrelevance;

/**
 * Interface which is used to submit Rich Relevance requests. A {@link ClientConfiguration} is used to apply shared
 * properties to requests when they are sent.
 */
public interface RichRelevanceClient {

    /**
     * @return The configuration being used on requests.
     */
    public ClientConfiguration getConfiguration();

    /**
     * Sets the configuration to use on requests.
     * @param configuration The configuration to use on requests.
     */
    public void setConfiguration(ClientConfiguration configuration);

    /**
     * Executes the given request using the current configuration.
     * @param request The request to execute.
     * @param <T> The result type of the request.
     */
    public <T extends ResponseInfo> void executeRequest(RequestBuilder<T> request);
}
