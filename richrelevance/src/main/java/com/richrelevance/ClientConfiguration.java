package com.richrelevance;

/**
 * Defines a set of properties that contains general Rich Relevance configuration information used to track and obtain
 * Rich Relevance data.
 */
public class ClientConfiguration {

    private String endpoint;
    private boolean useHttps;

    private String apiKey;
    private String apiClientKey;
    private String apiClientSecret;
    private String userId;
    private String sessionId;

    /**
     * Constructs a new configuration with the given keys and default endpoints.
     * @param apiKey The API key to use for requests.
     * @param apiClientKey The API Client Key to use for requests.
     */
    public ClientConfiguration(String apiKey, String apiClientKey) {
        setApiKey(apiKey);
        setApiClientKey(apiClientKey);
        setEndpoint(Endpoint.PRODUCTION, true);
    }

    /**
     * @return The primary endpoint.
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * @return True if HTTPS should be used for requests, false if HTTP should be used.
     */
    public boolean useHttps() {
        return useHttps;
    }

    /**
     * Sets the endpoint configuration to use.
     * @param endpoint The host part of the endpoint to send requests to. This should not include the scheme (http://
     *                 or https://).
     * @param useHttps True to use HTTPS, false to use HTTP.
     */
    public void setEndpoint(String endpoint, boolean useHttps) {
        this.endpoint = endpoint;
        this.useHttps = useHttps;
    }

    /**
     * @return The API Key to use for requests.
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Sets the API Key to use for requests.
     * @param apiKey The API Key to use.
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * @return The API Client Key to use for requests.
     */
    public String getApiClientKey() {
        return apiClientKey;
    }

    /**
     * Sets the API Client Key to use for requests.
     * @param apiClientKey The API Client Key to use for requests.
     */
    public void setApiClientKey(String apiClientKey) {
        this.apiClientKey = apiClientKey;
    }

    /**
     * @return The User ID to use for requests.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @return The API Client Secret to use for requests.
     */
    public String getApiClientSecret() {
        return apiClientSecret;
    }

    /**
     * Sets the API Client Secret to use for requests.
     * @param apiClientSecret The API Client Secret to use for requests.
     */
    public void setApiClientSecret(String apiClientSecret) {
        this.apiClientSecret = apiClientSecret;
    }

    /**
     * Sets the User ID to use for requests.
     * @param userId The User ID to use for requests.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return The Session ID to use for requests.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Sets the Session ID to use for requests.
     * @param sessionId The Session ID to use for requests.
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
