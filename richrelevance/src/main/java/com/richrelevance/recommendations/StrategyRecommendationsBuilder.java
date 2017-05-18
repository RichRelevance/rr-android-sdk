package com.richrelevance.recommendations;

import com.richrelevance.ClientConfiguration;
import com.richrelevance.RequestBuilder;
import com.richrelevance.internal.net.WebResponse;
import com.richrelevance.utils.ValueMap;

import org.json.JSONObject;

import java.util.Collection;

/**
 * A builder which requests product recommendations using a specified strategy.
 */
public class StrategyRecommendationsBuilder extends RequestBuilder<StrategyResponseInfo> {

    public static class Keys {
        public static final String STRATEGY_NAME = "strategyName";
        public static final String SEED = "seed";
        public static final String RESULT_COUNT = "resultCount";
        public static final String ATTRIBUTES = "attribute";
        public static final String REQUEST_ID = "requestId";

        public static final String EMAIL_CAMPAIGN_ID = "emailCampaignId";
        public static final String INCLUDE_CATEGORY_DATA = "categoryData";
        public static final String EXCLUDED_PRODUCT_IDS = "blockedProductIds";
        public static final String USER_ATTRIBUTES = "userAttribute";
        public static final String REGION_ID = "region";
    }

    /**
     * Sets the strategy to use to obtain recommendations.
     *
     * @param strategy The strategy to use.
     * @return This builder for chaining method calls.
     */
    public StrategyRecommendationsBuilder setStrategy(StrategyType strategy) {
        setParameter(Keys.STRATEGY_NAME, strategy.getKey());
        return this;
    }

    /**
     * A product, a category, or a search string depending on the type of strategy chosen for strategyName.
     * <ul>
     * <li><b>PurchaseCP</b>,<b> ClickCP</b>: Use a <b>product ID</b></li>
     * <li><b>CategoryCP2</b>, <b>CategoryTopSellers</b>, <b>NewArrivalsIncategory</b>: Use a <b>category ID</b></li>
     * <li>SolrSearchToProduct: Use a search term</li>
     * <li><b>BrandTopSellers</b>: Use a <b>brand</b></li>
     * <li><b>TopSellers</b>, <b>TopRatedProducts</b>, <b>movers_and_shakers_1</b>, <b>PersonalizedClickCP</b>: <b>No seed</b> needed</li>
     * <li>Others: Use a product ID</li>
     * </ul>
     * recsUsingStrategy does not infer category information from product seeds.
     *
     * @param seed The seed to set.
     * @return This builder for chaining method calls.
     */
    public StrategyRecommendationsBuilder setSeed(String seed) {
        setParameter(Keys.SEED, seed);
        return this;
    }

    /**
     * How many categories to return.
     *
     * @param count The number of categories to return.
     * @return This builder for chaining method calls.
     */
    public StrategyRecommendationsBuilder setResultCount(int count) {
        setParameter(Keys.RESULT_COUNT, count);
        return this;
    }

    /**
     * Retrieves custom attributes provided in the catalog feed. Use * as the parameter value to request all attributes.
     *
     * @param attributes The attributes to retrieve.
     * @return This builder for chaining method calls
     */
    public StrategyRecommendationsBuilder setCatalogFeedAttributes(String... attributes) {
        setListParameterFlat(Keys.ATTRIBUTES, attributes);
        return this;
    }

    /**
     * Retrieves custom attributes provided in the catalog feed. Use * as the parameter value to request all attributes.
     *
     * @param attributes The attributes to retrieve.
     * @return This builder for chaining method calls
     */
    public StrategyRecommendationsBuilder setCatalogFeedAttributes(Collection<String> attributes) {
        setListParameterFlat(Keys.ATTRIBUTES, attributes);
        return this;
    }

    /**
     * Sets the email campaign ID. Used only if the request is part of an email campaign.
     * @param campaignId The campaign ID.
     * @return This builder for chaining method calls.
     */
    public StrategyRecommendationsBuilder setEmailCampaignId(String campaignId) {
        setParameter(Keys.EMAIL_CAMPAIGN_ID, campaignId);
        return this;
    }

    /**
     * recsUsingStrategy simply returns this ID in the response object untouched. If you have multiple outstanding
     * requests that were issued in parallel, the request ID lets you figure out which response matches which request.
     * This parameter is purely for identifying responses if more than one requests were made asynchronously.
     * @param requestId The ID to use as the request ID.
     * @return This builder for chaining method calls.
     */
    public StrategyRecommendationsBuilder setRequestId(String requestId) {
        setParameter(Keys.REQUEST_ID, requestId);
        return this;
    }

    /**
     * Includes category data if set to true, omits it if set to false. Default = true.
     * @param include True to include category data.
     * @return This builder for chaining method calls.
     */
    public StrategyRecommendationsBuilder setIncludeCategoryData(boolean include) {
        setParameter(Keys.INCLUDE_CATEGORY_DATA, include);
        return this;
    }

    /**
     * List of product IDs that should not be recommended in this response.
     *
     * @param productIds The product IDs not to recommend.
     * @return This builder for chaining method calls.
     */
    public StrategyRecommendationsBuilder setExcludedProducts(String... productIds) {
        setListParameterFlat(Keys.EXCLUDED_PRODUCT_IDS, productIds);
        return this;
    }

    /**
     * List of product IDs that should not be recommended in this response.
     *
     * @param productIds The product IDs not to recommend.
     * @return This builder for chaining method calls.
     */
    public StrategyRecommendationsBuilder setExcludedProducts(Collection<String> productIds) {
        setListParameterFlat(Keys.EXCLUDED_PRODUCT_IDS, productIds);
        return this;
    }

    /**
     * Sets the key/value pairs describing the attribute context of the current of the user.
     *
     * @param attributes The map of attributes to set.
     * @return This builder for chaining method calls.
     */
    public StrategyRecommendationsBuilder setUserAttributes(ValueMap<String> attributes) {
        setValueMapParameter(Keys.USER_ATTRIBUTES, attributes);
        return this;
    }

    /**
     * Region ID. Must be consistent with the ID used in the product region feed.
     * @param regionId The region ID to set.
     * @return This builder for chaining method calls.
     */
    public StrategyRecommendationsBuilder setRegionId(String regionId) {
        setParameter(Keys.REGION_ID, regionId);
        return this;
    }

    @Override
    protected String getEndpointPath(ClientConfiguration configuration) {
        return "rrserver/api/rrPlatform/recsUsingStrategy";
    }

    @Override
    protected StrategyResponseInfo createNewResult() {
        return new StrategyResponseInfo();
    }

    @Override
    protected void populateResponse(WebResponse response, JSONObject json, StrategyResponseInfo responseInfo) {
        RecommendationsParser.parseStrategyResponseInfo(json, responseInfo);
    }
}
