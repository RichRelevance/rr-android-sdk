package com.richrelevance.recommendations;

import com.richrelevance.BaseAndroidTestCase;
import com.richrelevance.Error;
import com.richrelevance.RequestBuilderAccessor;
import com.richrelevance.internal.TestResultCallback;
import com.richrelevance.mocking.MockWebResponse;
import com.richrelevance.mocking.ResponseBuilder;

public class RecommendationsParsingTests extends BaseAndroidTestCase {

    public void testParseRecsForPlacements() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ResponseBuilder responseBuilder = new ResponseBuilder()
                .setResponseCode(200)
                .setContentAssetPath("recsForPlacements.json");
        MockWebResponse response = new MockWebResponse(responseBuilder, getContext());

        TestResultCallback<?> callback = new TestResultCallback<PlacementResponseInfo>() {
            @Override
            protected void testResponse(PlacementResponseInfo response, Error error) {
                assertNull(error);
                assertNotNull(response);
                assertEquals("ok", response.getStatus());
                assertEquals("f3a16fcb-1193-460e-4a36-255366b5cf38", response.getViewGuid());
                assertEquals(1, response.getPlacements().size());

                PlacementResponse placement = response.getPlacements().get(0);

                assertEquals("add_to_cart_page_0", placement.getHtmlElementId());
                assertEquals(Placement.PlacementType.ADD_TO_CART, placement.getPlacement().getPageType());
                assertEquals("prod1", placement.getPlacement().getName());
                assertEquals("Best Sellers", placement.getStrategyMessage());
                assertEquals(4, placement.getRecommendedProducts().size());

                RecommendedProduct product = placement.getRecommendedProducts().get(0);
                assertEquals(
                        "http://recs.richrelevance.com/rrserver/apiclick?a=showcaseparent&cak=615389034415e91d&ct=http%3A%2F%2Flabs.richrelevance.com%2Fstorre%2Fcatalog%2Fproduct%2Fview%2Fsku%2F24100292&vg=f3a16fcb-1193-460e-4a36-255366b5cf38&stid=13&pti=13&pa=4892&pos=0&p=24100292&channelId=615389034415e91d&s=13DF9FE0-20D2-4951-AF45-4DDB105E7406&u=RZTestUser",
                        product.getClickUrl());
                assertEmpty(product.getRegionPriceDescription());
                assertEquals(4.238999843597412, product.getRating());
                assertEquals(0, product.getNumReviews());
                assertEquals(2900, product.getPriceRangeCents().getMin());
                assertEquals(2900, product.getPriceRangeCents().getMax());
                assertEquals("Electronics", product.getCategoryIds().get(0));
                assertEquals("24100292", product.getRegionalProductSku());
                assertEquals(
                        "http://labs.richrelevance.com/storre/media/catalog/product/c/a/canon-pixma-mg2220-all-in-one-inkjet-multifunction-printerscannercopier-a3c09644838bab3c901601a1603534b1.jpg",
                        product.getImageUrl());
                assertEquals("Canon PIXMA MG2220 All-in-One Inkjet Multifunction Printer/Scanner/Copier", product.getName());
                assertEquals("Electronics", product.getGenre());
                assertTrue(product.isRecommendable());
                assertEquals(2900, product.getPriceCents());
                assertNotNull(product.getAttributes());
                assertNonEmpty(product.getAttributes().get("MktplcInd").get(0));
                assertEquals("24100292", product.getId());
                assertEquals("Canon", product.getBrand());
                assertEquals(4, product.getCategories().size());

                Category category = product.getCategories().get(0);
                assertEquals("Electronics", category.getName());
                assertEquals("Electronics", category.getId());
                assertFalse(category.hasChildren());
            }
        };

        accessor.parseResponse(response, callback);
        callback.assertSuccess(this);
    }

    public void testParseRecsForPlacementsApiError() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ResponseBuilder responseBuilder = new ResponseBuilder()
                .setResponseCode(200)
                .setContentAssetPath("recsForPlacementsError.json");
        MockWebResponse response = new MockWebResponse(responseBuilder, getContext());

        TestResultCallback<?> callback = new TestResultCallback<PlacementResponseInfo>() {
            @Override
            protected void testResponse(PlacementResponseInfo response, Error error) {
                assertNotNull(error);
                assertEquals(Error.ErrorType.ApiError, error.getType());
                assertEquals("Something went horribly wrong!", error.getMessage());
            }
        };

        accessor.parseResponse(response, callback);
        callback.assertSuccess(this);
    }

    public void testParseRecsForPlacementsBadResponse() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ResponseBuilder responseBuilder = new ResponseBuilder()
                .setResponseCode(200);
        MockWebResponse response = new MockWebResponse(responseBuilder, getContext());

        TestResultCallback<?> callback = new TestResultCallback<PlacementResponseInfo>() {
            @Override
            protected void testResponse(PlacementResponseInfo response, Error error) {
                assertNotNull(error);
                assertEquals(Error.ErrorType.CannotParseResponse, error.getType());
            }
        };

        accessor.parseResponse(response, callback);
        callback.assertSuccess(this);
    }

    public void testParseRecsForStrategy() {
        StrategyRecommendationsBuilder builder = new StrategyRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ResponseBuilder responseBuilder = new ResponseBuilder()
                .setResponseCode(200)
                .setContentAssetPath("recsForStrategy.json");
        MockWebResponse response = new MockWebResponse(responseBuilder, getContext());

        TestResultCallback<?> callback = new TestResultCallback<StrategyResponseInfo>() {
            @Override
            protected void testResponse(StrategyResponseInfo response, Error error) {
                assertNull(error);
                assertNotNull(response);
                assertEquals("Site-wide Best Sellers", response.getMessage());
                assertEquals(StrategyType.SITE_WIDE_BEST_SELLERS, response.getStrategyType());
                assertEquals(5, response.getRecommendedProducts().size());

                RecommendedProduct product = response.getRecommendedProducts().get(0);
                assertEquals(
                        "http://recs.richrelevance.com/rrserver/apiclick?a=showcaseparent&cak=615389034415e91d&ct=http%3A%2F%2Flabs.richrelevance.com%2Fstorre%2Fcatalog%2Fproduct%2Fview%2Fsku%2F24100292&vg=f3a16fcb-1193-460e-4a36-255366b5cf38&stid=13&pti=13&pa=4892&pos=0&p=24100292&channelId=615389034415e91d&s=13DF9FE0-20D2-4951-AF45-4DDB105E7406&u=RZTestUser",
                        product.getClickUrl()
                );
                assertEquals("", product.getRegionPriceDescription());
                assertEquals(4.238999843597412, product.getRating());
                assertEquals(0, product.getNumReviews());
                assertEquals(2900, product.getPriceRangeCents().getMin());
                assertEquals(2900, product.getPriceRangeCents().getMax());
                assertEquals("Electronics", product.getCategoryIds().get(0));
                assertEquals("24100292", product.getRegionalProductSku());
                assertEquals(
                        "http://labs.richrelevance.com/storre/media/catalog/product/c/a/canon-pixma-mg2220-all-in-one-inkjet-multifunction-printerscannercopier-a3c09644838bab3c901601a1603534b1.jpg",
                        product.getImageUrl()
                );
                assertEquals("Canon PIXMA MG2220 All-in-One Inkjet Multifunction Printer/Scanner/Copier", product.getName());
                assertEquals("Electronics", product.getGenre());
                assertTrue(product.isRecommendable());
                assertEquals(2900, product.getPriceCents());
                assertNotNull(product.getAttributes());
                assertNotNull(product.getAttributes().get("MktplcInd"));
                assertEquals("24100292", product.getId());
                assertEquals("Canon", product.getBrand());
                assertEquals(4, product.getCategories().size());

                Category category = product.getCategories().get(0);
                assertEquals("Electronics", category.getName());
                assertEquals("Electronics", category.getId());
                assertFalse(category.hasChildren());
            }
        };

        accessor.parseResponse(response, callback);
        callback.assertSuccess(this);
    }
}
