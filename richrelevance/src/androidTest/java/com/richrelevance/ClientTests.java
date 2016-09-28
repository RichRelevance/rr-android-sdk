package com.richrelevance;

import com.richrelevance.internal.Constants;
import com.richrelevance.internal.SimpleJsonRequest;
import com.richrelevance.internal.net.WebRequest;
import com.richrelevance.internal.net.WebRequestBuilder;
import com.richrelevance.internal.net.WebRequestManager;
import com.richrelevance.internal.net.WebResponse;
import com.richrelevance.internal.net.WebResultInfo;
import com.richrelevance.internal.BusyLock;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientTests extends BaseTestCase {

    private static final String VALUE_CONFIG1 = "config1";
    private static final String VALUE_CONFIG2 = "config2";
    
    private ClientConfiguration config1, config2;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        config1 = new ClientConfiguration(VALUE_CONFIG1, VALUE_CONFIG1);
        config1.setEndpoint(VALUE_CONFIG1, false);
        config1.setSessionId(VALUE_CONFIG1);
        config1.setUserId(VALUE_CONFIG1);

        config2 = new ClientConfiguration(VALUE_CONFIG2, VALUE_CONFIG2);
        config2.setEndpoint(VALUE_CONFIG2, true);
        config2.setSessionId(VALUE_CONFIG2);
        config2.setUserId(VALUE_CONFIG2);
    }

    public void testConfiguration() {
        TestRequestBuilder builder = new TestRequestBuilder();
        RichRelevance.getDefaultClient().setConfiguration(config1);

        WebRequestBuilder webRequestBuilder = builder.build();

        assertTrue(webRequestBuilder.getFullUrl().contains(VALUE_CONFIG1));
        assertTrue(webRequestBuilder.getFullUrl().startsWith("http:"));
        assertConfigurationValues(webRequestBuilder, VALUE_CONFIG1);

        RichRelevance.getDefaultClient().setConfiguration(config2);
        webRequestBuilder = builder.build();

        assertTrue(webRequestBuilder.getFullUrl().contains(VALUE_CONFIG2));
        assertTrue(webRequestBuilder.getFullUrl().startsWith("https:"));
        assertConfigurationValues(webRequestBuilder, VALUE_CONFIG2);
    }

    public void testEndToEnd() {
        String url = "http://recs.richrelevance.com/rrserver/api/rrPlatform/recsUsingStrategy"
                + "?apiKey=" + Constants.TestApiKeys.API_KEY
                + "&apiClientKey=" + Constants.TestApiKeys.API_CLIENT_KEY
                + "&strategyName=SiteWideBestSellers";
        SimpleJsonRequest request = new SimpleJsonRequest(url);

        WebRequestManager webRequestManager = new WebRequestManager();
        WebResultInfo<JSONObject> result = webRequestManager.execute(request);
        assertNotNull(result);
        assertEquals(200, result.getResponseCode());

        JSONObject json = result.getResult();
        assertNotNull(json);
        assertTrue(json.has("recommendedProducts"));

    }

    public void testClickTrackingSuccess() {
        PassFailWebManager testManager = new PassFailWebManager();
        WebRequestManager oldManager = flipWebManager(ClickTrackingManager.getInstance(), testManager);

        ClickTrackingManager.getInstance().trackClick(Endpoints.PRODUCTION + "/click");
        assertClickTrackingEmpties();

        flipWebManager(ClickTrackingManager.getInstance(), oldManager);
    }

    public void testClickTrackingRetry() {
        final AtomicInteger failCount = new AtomicInteger(0);
        final AtomicInteger successCount = new AtomicInteger(0);

        PassFailWebManager testManager = new PassFailWebManager() {
            @Override
            public <Result> WebResultInfo<Result> execute(WebRequest<Result> request) {
                if (fail) {
                    failCount.incrementAndGet();
                } else {
                    successCount.incrementAndGet();
                }

                return super.execute(request);
            }
        };

        WebRequestManager oldManager = flipWebManager(ClickTrackingManager.getInstance(), testManager);

        testManager.setFail(true);

        String url = Endpoints.PRODUCTION + "/click";
        ClickTrackingManager.getInstance().trackClick(url);
        ClickTrackingManager.getInstance().trackClick(url);

        assertTrue(BusyLock.wait(50, 3000, new BusyLock.Evaluator() {
            @Override
            public boolean isUnlocked() {
                return (failCount.get() == 2);
            }
        }));

        assertEquals(0, successCount.get());

        failCount.set(0);
        successCount.set(0);

        testManager.setFail(false);
        ClickTrackingManager.getInstance().flush();

        assertClickTrackingEmpties();
        assertEquals(0, failCount.get());
        assertEquals(2, successCount.get());
    }

    private void assertClickTrackingEmpties() {
        assertTrue("Failed to catch a queued click track", ClickTrackingManager.getInstance().getQueuedCount() > 0);

        boolean sentClick = BusyLock.wait(50, 10000, new BusyLock.Evaluator() {
            @Override
            public boolean isUnlocked() {
                return (ClickTrackingManager.getInstance().getQueuedCount() == 0);
            }
        });
        assertTrue(sentClick);
    }

    private WebRequestManager flipWebManager(ClickTrackingManager clickManager, WebRequestManager webManager) {
        try {
            Field webManagerField = ClickTrackingManager.class.getDeclaredField("webManager");
            webManagerField.setAccessible(true);

            WebRequestManager old = (WebRequestManager) webManagerField.get(clickManager);
            webManagerField.set(clickManager, webManager);

            return old;
        } catch (NoSuchFieldException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    private void assertConfigurationValues(WebRequestBuilder builder, String value) {
        assertEquals(value, builder.getParam("apiKey"));
        assertEquals(value, builder.getParam("apiClientKey"));
        assertEquals(value, builder.getParam("userId"));
        assertEquals(value, builder.getParam("sessionId"));
    }

    private static class TestRequestBuilder extends RequestBuilder<ResponseInfo> {

        @Override
        protected ResponseInfo createNewResult() {
            return new ResponseInfo() {};
        }

        @Override
        protected String getEndpointPath(ClientConfiguration configuration) {
            return "test";
        }

        @Override
        protected void populateResponse(WebResponse response, JSONObject json, ResponseInfo responseInfo) {

        }
    }

    private static class PassFailWebManager extends WebRequestManager {
        protected boolean fail = false;

        public void setFail(boolean fail) {
            this.fail = fail;
        }

        @Override
        public <Result> WebResultInfo<Result> execute(WebRequest<Result> request) {
            final boolean wasSuccess = !fail;

            return new WebResultInfo<Result>() {
                @Override
                public long getResponseTimestamp() {
                    return 0;
                }

                @Override
                public Result getResult() {
                    return null;
                }

                @Override
                public Error getError() {
                    return null;
                }

                @Override
                public int getResponseCode() {
                    if (wasSuccess) {
                        return 200;
                    } else {
                        return -1;
                    }
                }

                @Override
                public String getResponseMessage() {
                    return null;
                }
            };
        }
    }
}
