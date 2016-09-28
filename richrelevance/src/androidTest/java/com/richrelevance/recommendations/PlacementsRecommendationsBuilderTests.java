package com.richrelevance.recommendations;

import com.richrelevance.BaseTestCase;
import com.richrelevance.Range;
import com.richrelevance.RequestBuilderAccessor;
import com.richrelevance.utils.ValueMap;

public class PlacementsRecommendationsBuilderTests extends BaseTestCase {

    /*
     *  Inherited methods
     */
    public void testPlacementTypeConstants() {
        assertEquals(Placement.PlacementType.HOME.getKey(), "home_page");
        assertEquals(Placement.PlacementType.ITEM.getKey(), "item_page");
        assertEquals(Placement.PlacementType.ADD_TO_CART.getKey(), "add_to_cart_page");
        assertEquals(Placement.PlacementType.SEARCH.getKey(), "search_page");
        assertEquals(Placement.PlacementType.PURCHASE_COMPLETE.getKey(), "purchase_complete_page");
        assertEquals(Placement.PlacementType.CATEGORY.getKey(), "category_page");
        assertEquals(Placement.PlacementType.CART.getKey(), "cart_page");
    }

    public void testPlacementFormatting() {
        Placement placement = new Placement(Placement.PlacementType.CART, "horizontal");
        assertEquals("cart_page.horizontal", placement.getApiValue());
    }

    public void testAddPlacement() {
        Placement placement1 = new Placement(Placement.PlacementType.CART, "horizontal");
        Placement placement2 = new Placement(Placement.PlacementType.HOME, "vertical");
        Placement placement3 = new Placement(Placement.PlacementType.HOME, "full");

        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.addPlacements(placement1, placement2);

        String placementString = accessor.getParamValue(PlacementsBuilder.Keys.PLACEMENTS);
        assertEquals("cart_page.horizontal|home_page.vertical", placementString);

        builder.addPlacements(placement3);
        placementString = accessor.getParamValue(PlacementsBuilder.Keys.PLACEMENTS);
        assertEquals("cart_page.horizontal|home_page.vertical|home_page.full", placementString);
    }

    public void testSetPageFeaturedBrand() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setPageFeaturedBrand("Sony");
        assertEquals("Sony", accessor.getParamValue(PlacementsBuilder.Keys.PAGE_FEATURED_BRAND));
    }

    public void testExcludeHtml() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEquals("true", accessor.getParamValue(PlacementsBuilder.Keys.EXCLUDE_HTML));

        builder.excludeHtml(false);
        assertEquals("false", accessor.getParamValue(PlacementsBuilder.Keys.EXCLUDE_HTML));
    }

    public void testSetIncludeCategoryData() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsBuilder.Keys.INCLUDE_CATEGORY_DATA));

        builder.setReturnCategoryData(false);
        assertEquals("false", accessor.getParamValue(PlacementsBuilder.Keys.INCLUDE_CATEGORY_DATA));
    }

    public void testSetUserAttributes() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ValueMap<String> attributes = new ValueMap<String>()
                .add("age", "30")
                .add("gender", "female")
                .add("hair_color", "red", "blonde");

        builder.setUserAttributes(attributes);
        assertEquals(
                "age:30|gender:female|hair_color:red;blonde",
                accessor.getParamValue(PlacementsBuilder.Keys.USER_ATTRIBUTES));
    }

    public void testSetReferrer() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsBuilder.Keys.REFERRER));

        builder.setReferrer("myRef");
        assertEquals("myRef", accessor.getParamValue(PlacementsBuilder.Keys.REFERRER));
    }


    public void testSetCategoryHintIds() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsBuilder.Keys.CATEGORY_HINT_IDS));

        String[] hints = new String[]{"1", "2", "3"};
        builder.setCategoryHintIds(hints);
        assertJoined(accessor.getParamValue(PlacementsBuilder.Keys.CATEGORY_HINT_IDS), hints);

    }

    public void testSetUserSegments() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ValueMap<String> segments = new ValueMap<String>()
                .add("101", "NewUser")
                .add("2", "Test");
        builder.setUserSegments(segments);
        assertEquals("101:NewUser|2:Test", accessor.getParamValue(PlacementsBuilder.Keys.USER_SEGMENTS));
    }

    public void testSetRegionId() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setRegionId("123");
        assertEquals("123", accessor.getParamValue(PlacementsBuilder.Keys.REGION_ID));
    }

    public void testViewedProducts() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ValueMap<Long> viewedProducts = new ValueMap<Long>()
                .add("1", 190000L)
                .add("2", 190010L, 190200L);

        builder.setViewedProducts(viewedProducts);
        assertEquals("1:190000|2:190010;190200", accessor.getParamValue(PlacementsBuilder.Keys.VIEWED_PRODUCTS));
    }

    public void testSetPurchasedProducts() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ValueMap<Long> purchasedProducts = new ValueMap<Long>()
                .add("1", 190000L)
                .add("2", 190010L, 190200L);

        builder.setPurchasedProducts(purchasedProducts);
        assertEquals("1:190000|2:190010;190200", accessor.getParamValue(PlacementsBuilder.Keys.PURCHASED_PRODUCTS));
    }


    /*
     * PlacementsRecommendationsBuilder unique methods
     */
    public void testTimestamp() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertNonEmpty(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.TIMESTAMP));

        // Timestamps get set at web request generation and can't be unset. Rebuild the request.
        builder = new PlacementsRecommendationsBuilder();
        accessor = new RequestBuilderAccessor(builder);

        builder.setAddTimestampEnabled(false);
        assertEmpty(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.TIMESTAMP));
    }

    public void testIncludedBrands() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.BRAND_FILTER));
        assertEmpty(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.BRAND_INCLUDE_FILTERED));

        String[] brands = new String[]{"Sony", "EA", "Apple"};
        builder.setBrandFilterInclude(brands);
        assertJoined(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.BRAND_FILTER), brands);
        assertTrue(Boolean.valueOf(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.BRAND_INCLUDE_FILTERED)));
    }

    public void testExcludedBrands() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.BRAND_FILTER));
        assertEmpty(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.BRAND_INCLUDE_FILTERED));

        String[] brands = new String[]{"Sony", "EA", "Apple"};
        builder.setBrandFilterExclude(brands);
        assertJoined(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.BRAND_FILTER), brands);
        assertFalse(Boolean.valueOf(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.BRAND_INCLUDE_FILTERED)));
    }


    public void setFilterPriceRangeIncludeCents() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setPriceFilterIncludeRange(new Range(10, 100));
        assertEquals("10", accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.PRICE_FILTER_MIN));
        assertEquals("100", accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.PRICE_FILTER_MAX));
        assertEquals("true", accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.PRICE_FILTER_INCLUDE));
    }

    public void setFilterPriceRangeExcludeCents() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setPriceFilterExcludeRange(new Range(10, 100));
        assertEquals("10", accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.PRICE_FILTER_MIN));
        assertEquals("100", accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.PRICE_FILTER_MAX));
        assertEquals("false", accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.PRICE_FILTER_INCLUDE));
    }


    public void testExcludeRecommendedItems() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.EXCLUDE_RECOMMENDED_ITEMS));

        builder.excludeRecommendedItems(true);
        assertEquals("true", accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.EXCLUDE_RECOMMENDED_ITEMS));
    }

    public void testMinimalRecommendedItemData() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.MINIMAL_RECOMMENDED_ITEM_DATA));

        builder.setReturnMinimalRecommendedItemData(true);
        assertEquals("true", accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.MINIMAL_RECOMMENDED_ITEM_DATA));
    }

    public void testSetExcludeProductsFromRecommendations() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        String[] products = new String[]{"1", "2", "3"};
        builder.setExcludedProducts(products);
        assertJoined(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.EXCLUDE_PRODUCT_IDS), products);
    }

    public void testSetRefinements() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ValueMap<String> attributes = new ValueMap<String>()
                .add("age", "30")
                .add("gender", "female")
                .add("hair_color", "red", "blonde");

        builder.setRefinements(attributes);
        assertEquals(
                "age:30|gender:female|hair_color:red|hair_color:blonde",
                accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.REFINEMENTS));
    }

    public void testSetCategoryId() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.CATEGORY_ID));

        builder.setCategoryId("1234");
        assertEquals("1234", accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.CATEGORY_ID));
    }

    public void testSetSearchTerm() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.SEARCH_TERM));

        builder.setSearchTerm("Shoes");
        assertEquals("Shoes", accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.SEARCH_TERM));
    }

    public void testSetOrderId() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.ORDER_ID));

        builder.setOrderId("123");
        assertEquals("123", accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.ORDER_ID));
    }

    public void testPurchasedProductsCents() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.addPurchasedProducts(new Product("1", 10, 100));
        builder.addPurchasedProducts(new Product("2", 5, 75));

        assertJoined(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.PRODUCT_ID), "1", "2");
        assertJoined(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.PRODUCT_QUANTITIES), "10", "5");
        assertJoined(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.PRODUCT_PRICES_CENTS), "100", "75");
    }

    public void testPurchasedProductsDollars() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.addPurchasedProducts(new Product("1", 10, 100.5));
        builder.addPurchasedProducts(new Product("2", 5, 75.5));

        assertJoined(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.PRODUCT_ID), "1", "2");
        assertJoined(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.PRODUCT_QUANTITIES), "10", "5");
        assertJoined(accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.PRODUCT_PRICES_DOLLARS), "100.5", "75.5");
    }

    public void testSetRegistryId() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setRegistryId("123");
        assertEquals("123", accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.REGISTRY_ID));
    }

    public void testSetRegistryTypeId() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setRegistryTypeId("123");
        assertEquals("123", accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.REGISTRY_TYPE_ID));
    }

    public void testSetAlreadyAddedRegistryProductIds() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        String[] items = new String[] { "1", "2", "3"};
        builder.setAlreadyAddedRegistryItems(items);
        assertJoined(
                accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.ALREADY_ADDED_REGISTRY_ITEMS),
                items);
    }

    public void testAddStrategy() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setStrategySet(StrategyType.NEW_ARRIVALS, StrategyType.PERSONALIZED);
        assertEquals("NewArrivals|Personalized", accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.STRATEGY_SET));
    }

    public void testSetCount() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setCount(50);
        assertEquals("50", accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.COUNT));
    }

    public void testSetStart() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setStart(50);
        assertEquals("50", accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.START));
    }

    public void testSetPriceRanges() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        Range[] ranges = new Range[] {
                new Range(1, 5),
                new Range(Range.NONE, 5),
                new Range(1, Range.NONE)
        };

        builder.setPriceRanges(ranges);
        assertEquals("1;5", accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.PRICE_RANGES));
    }

    public void testSetFilterAttributes() {
        PlacementsRecommendationsBuilder builder = new PlacementsRecommendationsBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ValueMap<String> attributes = new ValueMap<String>()
                .add("age", "30")
                .add("gender", "female")
                .add("hair_color", "red", "blonde");

        builder.setFilterAttributes(attributes);

        assertEquals(
                "age:30|gender:female|hair_color:red;blonde",
                accessor.getParamValue(PlacementsRecommendationsBuilder.Keys.FILTER_ATTRIBUTES));
    }
}