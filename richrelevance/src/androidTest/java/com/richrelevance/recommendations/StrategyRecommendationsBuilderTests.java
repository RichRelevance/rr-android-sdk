package com.richrelevance.recommendations;

import com.richrelevance.BaseTestCase;
import com.richrelevance.RequestBuilderAccessor;
import com.richrelevance.utils.ValueMap;

import java.util.Arrays;

public class StrategyRecommendationsBuilderTests extends BaseTestCase {

    public void testSetStrategy() {
        StrategyRecommendationsBuilder builder = new StrategyRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setStrategy(StrategyType.NEW_ARRIVALS);
        assertEquals("NewArrivals", accessor.getParamValue(StrategyRecommendationsBuilder.Keys.STRATEGY_NAME));
    }

    public void testSetSeed() {
        StrategyRecommendationsBuilder builder = new StrategyRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setSeed("1234");
        assertEquals("1234", accessor.getParamValue(StrategyRecommendationsBuilder.Keys.SEED));
    }

    public void testSetResultCount() {
        StrategyRecommendationsBuilder builder = new StrategyRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setResultCount(5);
        assertEquals("5", accessor.getParamValue(StrategyRecommendationsBuilder.Keys.RESULT_COUNT));
    }

    public void testSetCatalogFeedAttributes() {
        StrategyRecommendationsBuilder builder = new StrategyRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        String[] attributes = new String[] { "A", "B", "3" };
        builder.setCatalogFeedAttributes(attributes);
        assertEquals(Arrays.asList(attributes), accessor.getAllParamValues(StrategyRecommendationsBuilder.Keys.ATTRIBUTES));
    }

    public void testSetEmailCampaignId() {
        StrategyRecommendationsBuilder builder = new StrategyRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setEmailCampaignId("1234");
        assertEquals("1234", accessor.getParamValue(StrategyRecommendationsBuilder.Keys.EMAIL_CAMPAIGN_ID));
    }

    public void testSetRequestId() {
        StrategyRecommendationsBuilder builder = new StrategyRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setRequestId("ABC");
        assertEquals("ABC", accessor.getParamValue(StrategyRecommendationsBuilder.Keys.REQUEST_ID));
    }

    public void testSetIncludeCategoryData() {
        StrategyRecommendationsBuilder builder = new StrategyRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(StrategyRecommendationsBuilder.Keys.INCLUDE_CATEGORY_DATA));
        builder.setIncludeCategoryData(false);
        assertEquals("false", accessor.getParamValue(StrategyRecommendationsBuilder.Keys.INCLUDE_CATEGORY_DATA));
        builder.setIncludeCategoryData(true);
        assertEquals("true", accessor.getParamValue(StrategyRecommendationsBuilder.Keys.INCLUDE_CATEGORY_DATA));
    }

    public void testSetUserAttributes() {
        StrategyRecommendationsBuilder builder = new StrategyRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ValueMap<String> attributes = new ValueMap<String>()
                .add("age", "30")
                .add("gender", "female")
                .add("hair_color", "red", "blonde");

        builder.setUserAttributes(attributes);
        assertEquals("age:30|gender:female|hair_color:red;blonde", accessor.getParamValue(StrategyRecommendationsBuilder.Keys.USER_ATTRIBUTES));
    }

    public void testSetExcludedProducts() {
        StrategyRecommendationsBuilder builder = new StrategyRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        String[] products = new String[] { "A", "B", "3" };
        builder.setExcludedProducts(products);
        assertEquals(Arrays.asList(products), accessor.getAllParamValues(StrategyRecommendationsBuilder.Keys.EXCLUDED_PRODUCT_IDS));
    }

    public void testSetRegionId() {
        StrategyRecommendationsBuilder builder = new StrategyRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setRegionId("ABC");
        assertEquals("ABC", accessor.getParamValue(StrategyRecommendationsBuilder.Keys.REGION_ID));
    }
}
