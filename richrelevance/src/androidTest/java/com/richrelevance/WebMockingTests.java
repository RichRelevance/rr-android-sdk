package com.richrelevance;

import android.test.AndroidTestCase;

import com.richrelevance.internal.SimpleJsonRequest;
import com.richrelevance.internal.net.HttpMethod;
import com.richrelevance.internal.net.WebRequest;
import com.richrelevance.internal.net.WebRequestBuilder;
import com.richrelevance.internal.net.WebRequestManager;
import com.richrelevance.internal.net.WebResultInfo;
import com.richrelevance.mocking.MockResponseManager;
import com.richrelevance.mocking.MockWebRequestExecutor;
import com.richrelevance.mocking.ResponseBuilder;

import org.json.JSONObject;

public class WebMockingTests extends AndroidTestCase {

    private WebRequestManager webManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        MockResponseManager.getInstance().init(getContext());
        MockResponseManager.getInstance().putResponse(
                new WebRequestBuilder(HttpMethod.Get, "http://jsonplaceholder.typicode.com/posts/1"),
                new ResponseBuilder()
                        .setResponseCode(200)
                        .setContentAssetPath("postTest.json"));

        webManager = new WebRequestManager();
        webManager.setExecutorFactory(MockWebRequestExecutor.FACTORY);
    }

    public void testMocking() {
        WebRequest<JSONObject> request = new SimpleJsonRequest("http://jsonplaceholder.typicode.com/posts/1");
        WebResultInfo<JSONObject> result = webManager.execute(request);

        assertNotNull(result);
        assertNotNull(result.getResult());
        assertEquals("THIS IS MOCKED DATA", result.getResult().optString("body"));
    }
}
