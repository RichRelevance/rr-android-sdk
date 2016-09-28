package com.richrelevance;

import android.util.Log;

import com.richrelevance.internal.BusyLock;
import com.richrelevance.internal.Constants;
import com.richrelevance.internal.OneShotLock;
import com.richrelevance.internal.net.WebResponse;
import com.richrelevance.recommendations.Creative;
import com.richrelevance.recommendations.CompleteProduct;
import com.richrelevance.recommendations.Placement;
import com.richrelevance.recommendations.PlacementPersonalizeResponse;
import com.richrelevance.recommendations.PlacementPersonalizeResponseInfo;
import com.richrelevance.recommendations.PlacementResponse;
import com.richrelevance.recommendations.PlacementResponseInfo;
import com.richrelevance.recommendations.PlacementsPersonalizeBuilder;
import com.richrelevance.recommendations.PlacementsRecommendationsBuilder;
import com.richrelevance.recommendations.ProductBuilder;
import com.richrelevance.recommendations.ProductResponseInfo;
import com.richrelevance.recommendations.RecommendedProduct;
import com.richrelevance.recommendations.StrategyRecommendationsBuilder;
import com.richrelevance.recommendations.StrategyResponseInfo;
import com.richrelevance.recommendations.StrategyType;
import com.richrelevance.userPreference.ActionType;
import com.richrelevance.userPreference.FieldType;
import com.richrelevance.userPreference.UserPreferenceBuilder;
import com.richrelevance.userPreference.UserPreferenceResponseInfo;
import com.richrelevance.userProfile.UserProfileBuilder;
import com.richrelevance.userProfile.UserProfileField;
import com.richrelevance.userProfile.UserProfileResponseInfo;
import com.richrelevance.utils.ParsingUtils;
import com.richrelevance.utils.Wrapper;

import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class ApiIntegrationTests extends BaseTestCase {

    private RichRelevanceClient client;
    private RichRelevanceClient oAuthClient;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        ClientConfiguration config = new ClientConfiguration(Constants.TestApiKeys.API_KEY, Constants.TestApiKeys.API_CLIENT_KEY);
        config.setEndpoint(Endpoints.PRODUCTION, false);
        config.setUserId("AndroidTestUser");
        config.setSessionId(UUID.randomUUID().toString());

        client = new RichRelevanceClientImpl();
        client.setConfiguration(config);

        ClientConfiguration oAuthConfig = new ClientConfiguration(Constants.TestApiKeys.API_KEY, Constants.TestApiKeys.API_CLIENT_KEY);
        oAuthConfig.setEndpoint(Endpoints.PRODUCTION, true);
        oAuthConfig.setUserId("RZTestUser");
        oAuthConfig.setSessionId(UUID.randomUUID().toString());
        oAuthConfig.setApiClientSecret(Constants.TestApiKeys.API_CLIENT_SECRET);

        oAuthClient = new RichRelevanceClientImpl();
        oAuthClient.setConfiguration(oAuthConfig);
    }

    public void testBasic() {
        RequestBuilder<ResponseInfo> builder = new RequestBuilder<ResponseInfo>() {
            @Override
            protected ResponseInfo createNewResult() {
                return new ResponseInfo() {
                };
            }

            @Override
            protected String getEndpointPath(ClientConfiguration configuration) {
                return "rrserver/api/rrPlatform/recsForPlacements";
            }

            @Override
            protected void populateResponse(WebResponse response, JSONObject json, ResponseInfo responseInfo) {
            }
        };

        BuilderExecutorHelper<ResponseInfo> helper = new BuilderExecutorHelper<>(client, builder);
        helper.execute();
        helper.waitUntilCompleted();
        assertNotNull(helper.getResult());
    }

    public void testOAuth() {
        RequestBuilder<ResponseInfo> builder = new RequestBuilder<ResponseInfo>() {
            @Override
            protected ResponseInfo createNewResult() {
                return new ResponseInfo() {
                };
            }

            @Override
            protected String getEndpointPath(ClientConfiguration configuration) {
                String format = "userProfile/api/v1/service/userProfile/%s/%s";
                return String.format(format, configuration.getApiKey(), configuration.getUserId());
            }

            @Override
            protected void applyConfigurationParams(ClientConfiguration configuration) {

            }

            @Override
            protected void populateResponse(WebResponse response, JSONObject json, ResponseInfo responseInfo) {

            }
        }
                .setUseOAuth(true)
                .setParameter("field", "all");

        BuilderExecutorHelper<ResponseInfo> helper = new BuilderExecutorHelper<>(oAuthClient, builder);
        helper.execute();
        helper.waitUntilCompleted();
        assertNotNull(helper.getResult());
    }

    public void testRecommendationsForStrategy() {
        StrategyRecommendationsBuilder builder = RichRelevance.buildRecommendationsUsingStrategy(StrategyType.SITE_WIDE_BEST_SELLERS);
        BuilderExecutorHelper<StrategyResponseInfo> helper = new BuilderExecutorHelper<>(client, builder);
        helper.execute();
        helper.waitUntilCompleted();
        assertNotNull(helper.getResult());
    }

    public void testPersonalizedRecommendationsForPlacements() {
        Placement placement = new Placement(Placement.PlacementType.ADD_TO_CART, "prod1");
        PlacementsRecommendationsBuilder builder = RichRelevance.buildRecommendationsForPlacements(placement);
        BuilderExecutorHelper<PlacementResponseInfo> helper = new BuilderExecutorHelper<>(client, builder);
        helper.execute();
        helper.waitUntilCompleted();
        validateRecommendationsForPlacementsResponse(helper.getResult());

        PlacementResponse placementResponse = helper.getResult().getPlacements().get(0);
        RecommendedProduct product = placementResponse.getRecommendedProducts().get(0);
        product.trackClick();
        assertTrue("Failed to catch a queued click track", ClickTrackingManager.getInstance().getQueuedCount() > 0);

        boolean sentClick = BusyLock.wait(50, 5 * 3000, new BusyLock.Evaluator() {
            @Override
            public boolean isUnlocked() {
                return (ClickTrackingManager.getInstance().getQueuedCount() == 0);
            }
        });

        assertTrue(sentClick);
    }

    public void testRecommendationsForPlacementsWithSearchTerm() {
        Placement placement = new Placement(Placement.PlacementType.ADD_TO_CART, "prod1");
        PlacementsRecommendationsBuilder builder = RichRelevance.buildRecommendationsForPlacements(placement)
                .setSearchTerm("SearchTerm");
        BuilderExecutorHelper<PlacementResponseInfo> helper = new BuilderExecutorHelper<>(client, builder);
        helper.execute();
        helper.waitUntilCompleted();
        validateRecommendationsForPlacementsResponse(helper.getResult());
    }

    public void validateRecommendationsForPlacementsResponse(PlacementResponseInfo responseInfo) {
        assertNotNull(responseInfo);

        assertTrue(ParsingUtils.isStatusOk(responseInfo.getStatus()));
        assertNonEmpty(responseInfo.getViewGuid());
        assertTrue(responseInfo.getPlacements().size() > 0);

        PlacementResponse placement = responseInfo.getPlacements().get(0);
        assertTrue(placement.getRecommendedProducts().size() > 0);

        RecommendedProduct product = placement.getRecommendedProducts().get(0);
        assertTrue(product.getCategories().size() > 0);
        assertNonEmpty(product.getName());
        assertNonEmpty(product.getGenre());
        assertNonEmpty(product.getId());
        assertNonEmpty(product.getClickUrl());
        assertNonEmpty(product.getImageUrl());
    }

    public void testPersonalizePlacements() {
        Placement placement = new Placement(Placement.PlacementType.HOME, "omnichannel");
        PlacementsPersonalizeBuilder builder = RichRelevance.buildPersonalizations(placement);
        BuilderExecutorHelper<PlacementPersonalizeResponseInfo> helper = new BuilderExecutorHelper<>(client, builder);
        helper.execute();
        helper.waitUntilCompleted();
        validatePersonalizePlacementsResponse(helper.getResult());

        PlacementPersonalizeResponse placementResponse = helper.getResult().getPlacements().get(0);
        Creative creative = placementResponse.getCreatives().get(0);
        int currentClickCount = ClickTrackingManager.getInstance().getQueuedCount();
        creative.trackClick();
        assertTrue("Failed to catch a queued click track", ++currentClickCount ==  ClickTrackingManager.getInstance().getQueuedCount());

        boolean sentClick = BusyLock.wait(50, 5 * 3000, new BusyLock.Evaluator() {
            @Override
            public boolean isUnlocked() {
                return (ClickTrackingManager.getInstance().getQueuedCount() == 0);
            }
        });

        assertTrue(sentClick);
    }

    public void validatePersonalizePlacementsResponse(PlacementPersonalizeResponseInfo responseInfo) {
        assertNotNull(responseInfo);

        assertTrue(ParsingUtils.isStatusOk(responseInfo.getStatus()));
        assertTrue(responseInfo.getPlacements().size() > 0);

        assertTrue(responseInfo.getRequestMap().size() > 0);

        PlacementPersonalizeResponse placement = responseInfo.getPlacements().get(0);
        assertTrue(placement.getCreatives().size() > 0);

        Creative creative = placement.getCreatives().get(0);
        assertNonEmpty(creative.getTrackingUrl());
        assertNonEmpty(creative.getCampaign());
        assertTrue(creative.getCreativeMap().size() > 0);
    }

    public void testProducts() {
        ProductBuilder builder = RichRelevance.buildProductsRequest("17177141");
        BuilderExecutorHelper<ProductResponseInfo> helper = new BuilderExecutorHelper<>(client, builder);
        helper.execute();
        helper.waitUntilCompleted();

        ProductResponseInfo responseInfo = helper.getResult();
        assertNotNull(responseInfo);
        assertEmpty(responseInfo.getRequestId());

        List<CompleteProduct> products = responseInfo.getProducts();
        assertNotNull(products);
        assertEquals(1, products.size());
    }

    public void testUserProfile() {
        UserProfileBuilder builder = RichRelevance.buildGetUserProfile(UserProfileField.ALL);

        BuilderExecutorHelper<UserProfileResponseInfo> helper = new BuilderExecutorHelper<>(oAuthClient, builder);
        helper.execute();
        helper.waitUntilCompleted();

        UserProfileResponseInfo responseInfo = helper.getResult();
        assertNotNull(responseInfo);
        assertEquals(oAuthClient.getConfiguration().getUserId(), responseInfo.getUserId());
    }

    public void testUserPreferences() {
        UserPreferenceBuilder builder = RichRelevance.buildTrackUserPreference(FieldType.BRAND, ActionType.LIKE, "apple");

        BuilderExecutorHelper<UserPreferenceResponseInfo> helper = new BuilderExecutorHelper<>(client, builder);
        helper.execute();
        helper.waitUntilCompleted();

        UserPreferenceResponseInfo responseInfo = helper.getResult();
        assertNotNull(responseInfo);
        assertNonEmpty(responseInfo.getUserId());
    }

    private static class BuilderExecutorHelper<T extends ResponseInfo> {

        private RichRelevanceClient client;
        private RequestBuilder<T> builder;

        private OneShotLock completionLock;
        private Wrapper<T> resultWrapper;

        public BuilderExecutorHelper(RichRelevanceClient client, RequestBuilder<T> builder) {
            this.client = client;
            this.builder = builder;

            this.completionLock = new OneShotLock();
            this.resultWrapper = new Wrapper<>();
        }

        public void execute() {
            builder.setCallback(new Callback<T>() {
                @Override
                public void onResult(T result) {
                    resultWrapper.set(result);
                    completionLock.unlock();
                }

                @Override
                public void onError(Error error) {
                    Log.w(getClass().getSimpleName(), "Builder web request responded with an error");
                    resultWrapper.set(null);
                    completionLock.unlock();
                }
            });

            client.executeRequest(builder);
        }

        public void waitUntilCompleted() {
            completionLock.waitUntilUnlocked();
        }

        public T getResult() {
            return resultWrapper.get();
        }
    }
}
