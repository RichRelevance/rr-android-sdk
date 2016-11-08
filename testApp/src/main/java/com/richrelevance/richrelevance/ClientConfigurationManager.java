package com.richrelevance.richrelevance;


import android.content.Context;

import com.richrelevance.ClientConfiguration;
import com.richrelevance.Endpoint;
import com.richrelevance.RRLog;
import com.richrelevance.RichRelevance;

import java.util.UUID;

public class ClientConfigurationManager {

    public static ClientConfigurationManager INSTANCE = new ClientConfigurationManager();

    public static ClientConfigurationManager getInstance() {
        return INSTANCE;
    }

    public static final String API_KEY = "199c81c05e473265";

    public static final String DEFAULT_CLIENT_API_KEY = "ff7665ca55280538";

    public static final String API_CLIENT_SECRET = "r5j50mlag06593401nd4kt734i";

    public static final String DEFAULT_CLIENT_NAME = "Rich Relevance";

    public static final String DEFAULT_USER_ID = "RZTestUserTest";

    private String apiKey;

    private String clientApiKey;

    private String clientName = DEFAULT_CLIENT_NAME;

    private User user;

    public void setConfig(String clientApiKey, String clientName, User user, String endpoint) {
        this.clientApiKey = clientApiKey;
        setClientName(clientName);
        this.user = user;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setClientApiKey(String clientApiKey) {
        this.clientApiKey = clientApiKey;
    }

    public void setClientApiKey(String clientApiKey, String endpoint) {
        this.clientApiKey = clientApiKey;
    }

    public void setClientName(String clientName) {
        this.clientName = (clientName == null || clientName.isEmpty()) ? DEFAULT_CLIENT_NAME : clientName;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getClientName() {
        return clientName;
    }

    public User getSelectedUser() {
        return user;
    }

    public void createConfiguration(Context context) {
        ClientConfiguration config = new ClientConfiguration(
                apiKey == null ? ClientConfigurationManager.API_KEY : apiKey,
                ((clientApiKey == null || clientApiKey.isEmpty()) ? DEFAULT_CLIENT_API_KEY : clientApiKey));
        config.setApiClientSecret(ClientConfigurationManager.API_CLIENT_SECRET);
        config.setUserId((user == null) ? ClientConfigurationManager.DEFAULT_USER_ID : user.getUserID());
        config.setSessionId(UUID.randomUUID().toString());
        config.setEndpoint(Endpoint.PRODUCTION, true);

        RichRelevance.init(context.getApplicationContext(), config);

        // Enable all logging
        RichRelevance.setLoggingLevel(RRLog.VERBOSE);
    }
}
