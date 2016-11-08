package com.richrelevance;

import com.richrelevance.internal.net.WebRequestBuilder;
import com.richrelevance.internal.net.WebResponse;

import junit.framework.TestCase;

import org.json.JSONObject;

public class RequestBuilderTests extends TestCase {

    private static final String TEST_KEY = "TestKey";
    private static final String TEST_VALUE = "TestValue";
    private static final String TEST_ENDPOINT_PATH = "some/url/path";

    public void testRequestBuilder() {

        TestBuilder builder = new TestBuilder();
        builder.setParameter(TEST_KEY, TEST_VALUE);

        WebRequestBuilder webRequest = builder.build();
        assertEquals(TEST_VALUE, webRequest.getParam(TEST_KEY));
        assertTrue(webRequest.getFullUrl().contains(TEST_ENDPOINT_PATH));
    }

    private static class TestBuilder extends RequestBuilder<ResponseInfo> {

        @Override
        protected ResponseInfo createNewResult() {
            return new ResponseInfo() { };
        }

        @Override
        protected String getEndpointPath(ClientConfiguration configuration) {
            return TEST_ENDPOINT_PATH;
        }

        @Override
        protected void populateResponse(WebResponse response, JSONObject json, ResponseInfo responseInfo) {

        }

    }
}
