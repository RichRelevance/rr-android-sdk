package com.richrelevance.recommendations;

import com.richrelevance.ClientConfiguration;
import com.richrelevance.RRLog;
import com.richrelevance.Range;
import com.richrelevance.internal.net.WebRequestBuilder;
import com.richrelevance.internal.net.WebResponse;
import com.richrelevance.utils.Utils;
import com.richrelevance.utils.ValueMap;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A builder which requests product recommendations using placements.
 */
public class PlacementsRecommendationsBuilder extends PlacementsBuilder<PlacementResponseInfo, PlacementsRecommendationsBuilder> {

    public static class Keys {

        public static final String TIMESTAMP = "ts";

        public static final String BRAND_FILTER = "filbr";
        public static final String BRAND_INCLUDE_FILTERED = "includeBrandFilteredProducts";

        public static final String PRICE_FILTER_MIN = "minPriceFilter";
        public static final String PRICE_FILTER_MAX = "maxPriceFilter";
        public static final String PRICE_FILTER_INCLUDE = "includePriceFilteredProducts";

        public static final String EXCLUDE_ITEM_ATTRIBUTES = "excludeItemAttributes";
        public static final String EXCLUDE_RECOMMENDED_ITEMS = "excludeRecItems";
        public static final String MINIMAL_RECOMMENDED_ITEM_DATA = "returnMinimalRecItemData";
        public static final String EXCLUDE_PRODUCT_IDS = "bi";

        public static final String REFINEMENTS = "rfm";

        public static final String PRODUCT_ID = "productId";
        public static final String PRODUCT_QUANTITIES = "q";
        public static final String PRODUCT_PRICES_DOLLARS = "pp";
        public static final String PRODUCT_PRICES_CENTS = "ppc";

        public static final String CATEGORY_ID = "categoryId";
        public static final String SEARCH_TERM = "searchTerm";
        public static final String ORDER_ID = "o";
        public static final String REGISTRY_ID = "rg";
        public static final String REGISTRY_TYPE_ID = "rgt";
        public static final String ALREADY_ADDED_REGISTRY_ITEMS = "aari";
        public static final String STRATEGY_SET = "strategySet";

        public static final String COUNT = "count";
        public static final String START = "st";
        public static final String PRICE_RANGES = "priceRanges";
        public static final String FILTER_ATTRIBUTES = "filterAtr";
    }

    private boolean addTimestampEnabled = true;

    public PlacementsRecommendationsBuilder() {
        excludeHtml(true);
    }

    /**
     * Sets whether to add a timestamp for cache busting. Highly recommended. If excluded, you may see cached responses. Default is true.
     *
     * @param enabled True to enable automatic timestamping, false to disable it.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setAddTimestampEnabled(boolean enabled) {
        this.addTimestampEnabled = enabled;
        return this;
    }

    /**
     * For cache busting. Highly recommended. If excluded, you may see cached responses.
     *
     * @param timestamp The timestamp to set.
     * @return This builder for chaining method calls.
     */
    protected PlacementsRecommendationsBuilder setTimestamp(long timestamp) {
        setParameter(Keys.TIMESTAMP, timestamp);
        return this;
    }

    /**
     * Filter by brand name. Excludes all products with the specified brands.
     * * <p>
     * <b>Note:</b> This is mutually exclusive with {@link #setBrandFilterInclude(Collection)} - setting one will
     * clear the other.
     *
     * @param brands The brands to exclude.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setBrandFilterExclude(String... brands) {
        return setFilteredBrands(Utils.safeAsList(brands), false);
    }

    /**
     * Filter by brand name. Excludes all products with the specified brands.
     * * <p>
     * <b>Note:</b> This is mutually exclusive with {@link #setBrandFilterInclude(Collection)} - setting one will
     * clear the other.
     *
     * @param brands The brands to exclude.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setBrandFilterExclude(Collection<String> brands) {
        return setFilteredBrands(brands, false);
    }

    /**
     * Filter by brand name. Excludes all products except the specified brands.
     * * <p>
     * <b>Note:</b> This is mutually exclusive with {@link #setBrandFilterExclude(Collection)} - setting one will
     * clear the other.
     *
     * @param brands The brands to include.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setBrandFilterInclude(String... brands) {
        return setFilteredBrands(Utils.safeAsList(brands), true);
    }

    /**
     * Filter by brand name. Excludes all products except the specified brands.
     * * <p>
     * <b>Note:</b> This is mutually exclusive with {@link #setBrandFilterExclude(Collection)} - setting one will
     * clear the other.
     *
     * @param brands The brands to include.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setBrandFilterInclude(Collection<String> brands) {
        return setFilteredBrands(brands, true);
    }

    /**
     * Sets the filtered brands and whether to include or exclude the given brands.
     *
     * @param brands    The brands to filter.
     * @param inclusive True to include only items of the given brands, false to exclude items of the given brands.
     * @return This builder for chaining method calls.
     */
    private PlacementsRecommendationsBuilder setFilteredBrands(Collection<String> brands, boolean inclusive) {
        setListParameter(Keys.BRAND_FILTER, brands);
        setParameter(Keys.BRAND_INCLUDE_FILTERED, inclusive);
        return this;
    }

    /**
     * <p>Sets the range of prices to include, filtering out anything outside the given range. The filter will match the
     * sale price or the list price of a product if no sale price is provided. The price is given in cents meaning if
     * you only want to exclude products that are greater than $5.79, the value provided should be '579'.
     * </p>
     * <b>Note:</b> This is mutually exclusive with {@link #setPriceFilterExcludeRange(Range)} - setting one will
     * clear the other.
     *
     * @param range The range of prices in cents.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setPriceFilterIncludeRange(Range range) {
        return setPriceFilterRange(range, true);
    }

    /**
     * <p>Sets the range of prices to exclude, filtering out anything inside the given range. The filter will match the
     * sale price or the list price of a product if no sale price is provided. The price is given in cents meaning if
     * you only want to exclude products that are greater than $5.79, the value provided should be '579'.
     * </p>
     * <b>Note:</b> This is mutually exclusive with {@link #setPriceFilterIncludeRange(Range)} - setting one will
     * clear the other.
     *
     * @param range The range of prices in cents.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setPriceFilterExcludeRange(Range range) {
        return setPriceFilterRange(range, false);
    }

    /**
     * Sets the range of prices to filter and whether to include or exclude said prices.
     *
     * @param range     The range of prices in cents.
     * @param inclusive True to include only items within the given range, false to exclude the items in the given range.
     * @return This builder for chaining method calls.
     */
    private PlacementsRecommendationsBuilder setPriceFilterRange(Range range, boolean inclusive) {
        if (range != null) {
            if (range.hasMax()) {
                setParameter(Keys.PRICE_FILTER_MIN, range.getMin());
            } else {
                removeParameter(Keys.PRICE_FILTER_MIN);
            }

            if (range.hasMax()) {
                setParameter(Keys.PRICE_FILTER_MAX, range.getMax());
            } else {
                removeParameter(Keys.PRICE_FILTER_MAX);
            }

            setParameter(Keys.PRICE_FILTER_INCLUDE, inclusive);
        } else {
            removeParameter(Keys.PRICE_FILTER_MIN);
            removeParameter(Keys.PRICE_FILTER_MAX);
            removeParameter(Keys.PRICE_FILTER_INCLUDE);
        }


        return this;
    }

    /**
     * If set to true, removes the item attributes from the recommended products data. Default = false.
     *
     * @param exclude True to remove the item attributes.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder excludeItemAttributes(boolean exclude) {
        setParameter(Keys.EXCLUDE_ITEM_ATTRIBUTES, exclude);
        return this;
    }

    /**
     * If set to true, removes the recommended items structure completely. This is useful when having HTML is enough
     * in the response. Default = false.
     *
     * @param exclude True to remove the recommended items.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder excludeRecommendedItems(boolean exclude) {
        setParameter(Keys.EXCLUDE_RECOMMENDED_ITEMS, exclude);
        return this;
    }

    /**
     * If set to true, reduces the information about the recommended items down to external ID and click URL.
     * Default = false.
     *
     * @param minimal True to return minimal recommended item data.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setReturnMinimalRecommendedItemData(boolean minimal) {
        setParameter(Keys.MINIMAL_RECOMMENDED_ITEM_DATA, minimal);
        return this;
    }

    /**
     * List of product IDs that should not be recommended in this response.
     *
     * @param productIds The product IDs not to recommend.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setExcludedProducts(String... productIds) {
        setListParameter(Keys.EXCLUDE_PRODUCT_IDS, productIds);
        return this;
    }

    /**
     * List of product IDs that should not be recommended in this response.
     *
     * @param productIds The product IDs not to recommend.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setExcludedProducts(Collection<String> productIds) {
        setListParameter(Keys.EXCLUDE_PRODUCT_IDS, productIds);
        return this;
    }

    /**
     * Refinement. Triggers a refinement filter rule configured in the {rr} dashboard. Rules eliminate a set of
     * products from recommendations based on product attributes. For more information see: Supplement: Adding
     * Refinements (RRO-2SIGSAR)
     *
     * @param refinements The map of refinements to add.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setRefinements(ValueMap<String> refinements) {
        setValueMapParameterFlat(Keys.REFINEMENTS, refinements);
        return this;
    }

    /**
     * Sets the ID of the category currently being viewed.
     *
     * @param categoryId The ID of the category being viewed.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setCategoryId(String categoryId) {
        setParameter(Keys.CATEGORY_ID, categoryId);
        return this;
    }

    /**
     * Sets the search term the user typed in. You can also use the productId parameter to provide the product IDs of
     * the products in the search results.
     *
     * @param searchTerm The search term the user typed in.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setSearchTerm(String searchTerm) {
        setParameter(Keys.SEARCH_TERM, searchTerm);
        return this;
    }

    /**
     * A single, or list of, product IDs. Part of an order definition on the purchase complete page.
     *
     * @param productIds The product IDs to set.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setProductIds(String... productIds) {
        setListParameter(Keys.PRODUCT_ID, productIds);
        return this;
    }

    /**
     * A single, or list of, product IDs. Part of an order definition on the purchase complete page.
     *
     * @param productIds The product IDs to set.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setProductIds(Collection<String> productIds) {
        setListParameter(Keys.PRODUCT_ID, productIds);
        return this;
    }

    /**
     * Sets the Order ID. Part of the order definition.
     *
     * @param orderId The order ID.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setOrderId(String orderId) {
        setParameter(Keys.ORDER_ID, orderId);
        return this;
    }

    /**
     * Adds {@link Product}s for purchase tracking.
     *
     * @param products The products to track.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder addPurchasedProducts(Product... products) {
        return addPurchasedProducts(Utils.safeAsList(products));
    }

    /**
     * Adds {@link Product}s for purchase tracking.
     *
     * @param products The products to track.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder addPurchasedProducts(Collection<Product> products) {
        if (products != null) {
            Collection<String> productIds = new ArrayList<>(products.size());
            Collection<Integer> productQuantities = new ArrayList<>(products.size());
            Collection<Integer> productPriceCents = new ArrayList<>(products.size());
            Collection<Double> productPriceDollars = new ArrayList<>(products.size());

            for (Product product : products) {
                if (product != null) {
                    Utils.addIfNonNull(productIds, product.getId());
                    Utils.addIfNonNull(productQuantities, product.getQuantity());
                    Utils.addIfNonNull(productPriceCents, product.getPriceCents());
                    Utils.addIfNonNull(productPriceDollars, product.getPriceDollars());
                }
            }

            addListParameters(Keys.PRODUCT_ID, productIds);
            addListParameters(Keys.PRODUCT_QUANTITIES, productQuantities);
            addListParameters(Keys.PRODUCT_PRICES_CENTS, productPriceCents);
            addListParameters(Keys.PRODUCT_PRICES_DOLLARS, productPriceDollars);
        }

        return this;
    }

    /**
     * Sets a registry ID string, used to identify a particular registry.
     *
     * @param registryId The registry ID to set.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setRegistryId(String registryId) {
        setParameter(Keys.REGISTRY_ID, registryId);
        return this;
    }

    public PlacementsRecommendationsBuilder setRegistryTypeId(String registryTypeId) {
        setParameter(Keys.REGISTRY_TYPE_ID, registryTypeId);
        return this;
    }

    /**
     * Sets a single, or list of, product IDs.
     *
     * @param productIds IDs of the products.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setAlreadyAddedRegistryItems(String... productIds) {
        setListParameter(Keys.ALREADY_ADDED_REGISTRY_ITEMS, productIds);
        return this;
    }

    /**
     * Sets a single, or list of, product IDs.
     *
     * @param productIds IDs of the products.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setAlreadyAddedRegistryItems(Collection<String> productIds) {
        setListParameter(Keys.ALREADY_ADDED_REGISTRY_ITEMS, productIds);
        return this;
    }

    /**
     * A prioritized list of strategy sets that you would want to be returned based on the campaign use case. Please
     * see additional details under Strategy Families listing below. If this is not provided, our recommendation
     * engine will run King of the Hill (KOTH) to provide best recommendations given the information provided.
     *
     * @param strategies The strategies to be returned.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setStrategySet(StrategyType... strategies) {
        setStrategySet(Utils.safeAsList(strategies));
        return this;
    }

    /**
     * A prioritized list of strategy sets that you would want to be returned based on the campaign use case. Please
     * see additional details under Strategy Families listing below. If this is not provided, our recommendation
     * engine will run King of the Hill (KOTH) to provide best recommendations given the information provided.
     *
     * @param strategies The strategies to be returned.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setStrategySet(Collection<StrategyType> strategies) {
        if (strategies != null) {
            List<String> stringStrategies = new ArrayList<>(strategies.size());
            for (StrategyType strategy : strategies) {
                if (strategy != null) {
                    stringStrategies.add(strategy.getKey());
                }
            }
            setListParameter(Keys.STRATEGY_SET, stringStrategies);
        } else {
            removeParameter(Keys.STRATEGY_SET);
        }

        return this;
    }

    /**
     * Search and Browse only. The total number of products to return in the response. This value overrides the return
     * count set in the placement or in the Search and Browse configuration. (For example, if you want to return products
     * 30-60, this value would be 30.) Max value = 1000.
     *
     * @param count The total number of products to return.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setCount(int count) {
        setParameter(Keys.COUNT, count);
        return this;
    }

    /**
     * Search and Browse only. The starting number for the products that you want to return. (For example, if you want to
     * return products 30-60, this value would be 30.)
     *
     * @param start The starting product.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setStart(int start) {
        setParameter(Keys.START, start);
        return this;
    }

    /**
     * Search and Browse only. Filter based on price ranges that the products should belong to in cents. N/A for clients
     * with localized product prices.
     *
     * @param ranges The ranges that the products should belong to in cents.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setPriceRanges(Range... ranges) {
        return setPriceRanges(Utils.safeAsList(ranges));
    }

    /**
     * Search and Browse only. Filter based on price ranges that the products should belong to in cents. N/A for clients
     * with localized product prices.
     *
     * @param ranges The ranges that the products should belong to in cents.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setPriceRanges(Collection<Range> ranges) {
        if (ranges != null) {
            List<String> stringRanges = new ArrayList<>(ranges.size());
            for (Range range : ranges) {
                if (range != null) {
                    if (range.hasMax() && range.hasMin()) {
                        stringRanges.add(range.getMin() + ";" + range.getMax());
                    } else {
                        RRLog.w(getClass().getSimpleName(), "Skipping price range - range must have a min and max value");
                    }
                }
            }

            setListParameter(Keys.PRICE_RANGES, stringRanges);
        } else {
            removeParameter(Keys.PRICE_RANGES);
        }

        return this;
    }

    /**
     * Search and Browse only. Filter types and values selected by the shopper. Needs configuration by the RichRelevance
     * team before turned on.
     *
     * @param attributes The attributes to set.
     * @return This builder for chaining method calls.
     */
    public PlacementsRecommendationsBuilder setFilterAttributes(ValueMap<String> attributes) {
        setValueMapParameter(Keys.FILTER_ATTRIBUTES, attributes);
        return this;
    }

    @Override
    protected void onBuild(WebRequestBuilder builder) {
        super.onBuild(builder);
        if (addTimestampEnabled) {
            builder.setParam(Keys.TIMESTAMP, System.currentTimeMillis());
        }
    }

    @Override
    protected String getEndpointPath(ClientConfiguration configuration) {
        return "rrserver/api/rrPlatform/recsForPlacements";
    }

    @Override
    protected PlacementResponseInfo createNewResult() {
        return new PlacementResponseInfo();
    }

    @Override
    protected void populateResponse(WebResponse response, JSONObject json, PlacementResponseInfo responseInfo) {
        RecommendationsParser.parsePlacementResponseInfo(json, responseInfo);
    }
}
