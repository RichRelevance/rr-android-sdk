package com.richrelevance.internal.net.oauth;

public class OAuthConfig {

    private String consumerKey;
    private String consumerSecret;

    public OAuthConfig(String consumerKey, String consumerSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }
}
