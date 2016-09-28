package com.richrelevance.recommendations;

import com.richrelevance.BaseTestCase;
import com.richrelevance.RequestBuilderAccessor;
import com.richrelevance.utils.ValueMap;

import java.util.Arrays;

public class PlacementsPersonalizeBuilderTests extends BaseTestCase {


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

        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.addPlacements(placement1, placement2);

        String placementString = accessor.getParamValue(PlacementsBuilder.Keys.PLACEMENTS);
        assertEquals("cart_page.horizontal|home_page.vertical", placementString);

        builder.addPlacements(placement3);
        placementString = accessor.getParamValue(PlacementsBuilder.Keys.PLACEMENTS);
        assertEquals("cart_page.horizontal|home_page.vertical|home_page.full", placementString);
    }

    public void testSetPageFeaturedBrand() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setPageFeaturedBrand("Sony");
        assertEquals("Sony", accessor.getParamValue(PlacementsBuilder.Keys.PAGE_FEATURED_BRAND));
    }

    public void testExcludeHtml() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEquals("true", accessor.getParamValue(PlacementsBuilder.Keys.EXCLUDE_HTML));

        builder.excludeHtml(false);
        assertEquals("false", accessor.getParamValue(PlacementsBuilder.Keys.EXCLUDE_HTML));
    }

    public void testSetIncludeCategoryData() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsBuilder.Keys.INCLUDE_CATEGORY_DATA));

        builder.setReturnCategoryData(false);
        assertEquals("false", accessor.getParamValue(PlacementsBuilder.Keys.INCLUDE_CATEGORY_DATA));
    }

    public void testSetUserAttributes() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
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
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsBuilder.Keys.REFERRER));

        builder.setReferrer("myRef");
        assertEquals("myRef", accessor.getParamValue(PlacementsBuilder.Keys.REFERRER));
    }


    public void testSetCategoryHintIds() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsBuilder.Keys.CATEGORY_HINT_IDS));

        String[] hints = new String[]{"1", "2", "3"};
        builder.setCategoryHintIds(hints);
        assertJoined(accessor.getParamValue(PlacementsBuilder.Keys.CATEGORY_HINT_IDS), hints);

    }

    public void testSetUserSegments() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ValueMap<String> segments = new ValueMap<String>()
                .add("101", "NewUser")
                .add("2", "Test");
        builder.setUserSegments(segments);
        assertEquals("101:NewUser|2:Test", accessor.getParamValue(PlacementsBuilder.Keys.USER_SEGMENTS));
    }

    public void testSetRegionId() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setRegionId("123");
        assertEquals("123", accessor.getParamValue(PlacementsBuilder.Keys.REGION_ID));
    }

    public void testViewedProducts() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ValueMap<Long> viewedProducts = new ValueMap<Long>()
                .add("1", 190000L)
                .add("2", 190010L, 190200L);

        builder.setViewedProducts(viewedProducts);
        assertEquals("1:190000|2:190010;190200", accessor.getParamValue(PlacementsBuilder.Keys.VIEWED_PRODUCTS));
    }

    public void testSetPurchasedProducts() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ValueMap<Long> purchasedProducts = new ValueMap<Long>()
                .add("1", 190000L)
                .add("2", 190010L, 190200L);

        builder.setPurchasedProducts(purchasedProducts);
        assertEquals("1:190000|2:190010;190200", accessor.getParamValue(PlacementsBuilder.Keys.PURCHASED_PRODUCTS));
    }


    /*
     * PlacementsPersonalizeBuilder unique methods
     */
    public void testCustomAttributes() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        String[] attributes = new String[] { "key1", "key2", "key3" };
        builder.setCustomAttributes(attributes);
        assertEquals(Arrays.asList(attributes), accessor.getAllParamValues(PlacementsPersonalizeBuilder.Keys.ATTRIBUTES));
    }

    public void testSetCartValue() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsPersonalizeBuilder.Keys.CART_VALUE_CENTS));

        builder.setCartValue("1000");
        assertEquals("1000", accessor.getParamValue(PlacementsPersonalizeBuilder.Keys.CART_VALUE_CENTS));
    }

    public void testSetExternalCategoryIds() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsPersonalizeBuilder.Keys.EXTERNAL_CATEGORY_IDS));

        String[] categoryIds = new String[]{"00000", "11111", "22222"};
        builder.setExternalCategoryIds(categoryIds);
        assertJoined(accessor.getParamValue(PlacementsPersonalizeBuilder.Keys.EXTERNAL_CATEGORY_IDS), categoryIds);
    }

    public void testSetCategoryName() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsPersonalizeBuilder.Keys.CATEGORY_NAME));

        builder.setCategoryName("myCategory");
        assertEquals("myCategory", accessor.getParamValue(PlacementsPersonalizeBuilder.Keys.CATEGORY_NAME));
    }

    public void testSetSpoof() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsPersonalizeBuilder.Keys.SPOOF));

        builder.setSpoof("mySpoof");
        assertEquals("mySpoof", accessor.getParamValue(PlacementsPersonalizeBuilder.Keys.SPOOF));
    }

    public void testSetEmailCampaignId() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsPersonalizeBuilder.Keys.EMAIL_CAMPAIGN_ID));

        builder.setEmailCampaignId("myCampaign2015");
        assertEquals("myCampaign2015", accessor.getParamValue(PlacementsPersonalizeBuilder.Keys.EMAIL_CAMPAIGN_ID));
    }

    public void testSetNumRecommendationsLimit() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsPersonalizeBuilder.Keys.NUM_RECOMMENDATIONS_LIMIT));

        builder.setNumRecommendationsLimit(7);
        assertEquals("7", accessor.getParamValue(PlacementsPersonalizeBuilder.Keys.NUM_RECOMMENDATIONS_LIMIT));
    }

    public void testSetUseCookie() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEmpty(accessor.getParamValue(PlacementsPersonalizeBuilder.Keys.USE_COOKIE));

        builder.setUseCookie(Boolean.TRUE);
        assertEquals("true", accessor.getParamValue(PlacementsPersonalizeBuilder.Keys.USE_COOKIE));
    }
}