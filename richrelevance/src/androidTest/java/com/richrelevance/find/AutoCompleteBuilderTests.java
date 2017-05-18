package com.richrelevance.find;


import com.richrelevance.BaseTestCase;
import com.richrelevance.ClientConfiguration;
import com.richrelevance.RichRelevanceClient;
import com.richrelevance.TestClient;

public class AutoCompleteBuilderTests extends BaseTestCase {
    private RichRelevanceClient client;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        ClientConfiguration config = new ClientConfiguration("apiKey", "apiClientKey");
        config.setUserId("RZTestUser");
        config.setSessionId("session");

        client = new TestClient(config);
    }
}
