package com.richrelevance.userProfile;

import com.richrelevance.BaseTestCase;
import com.richrelevance.RequestBuilderAccessor;

public class UserProfileBuilderTests extends BaseTestCase {

    public void testFieldConstants() {
        assertEquals("viewedItems", UserProfileField.VIEWED_ITEMS.getKey());
        assertEquals("clickedItems", UserProfileField.CLICKED_ITEMS.getKey());
        assertEquals("referrerUrls", UserProfileField.REFERRER_URLS.getKey());
        assertEquals("orders", UserProfileField.ORDERS.getKey());
        assertEquals("viewedCategories", UserProfileField.VIEWED_CATEGORIES.getKey());
        assertEquals("viewedBrands", UserProfileField.VIEWED_BRANDS.getKey());
        assertEquals("addedToCartItems", UserProfileField.ADDED_TO_CART_ITEMS.getKey());
        assertEquals("searchedTerms", UserProfileField.SEARCHED_TERMS.getKey());
        assertEquals("userAttributes", UserProfileField.USER_ATTRIBUTES.getKey());
        assertEquals("userSegments", UserProfileField.USER_SEGMENTS.getKey());
        assertEquals("verbNouns", UserProfileField.VERB_NOUNS.getKey());
        assertEquals("countedEvents", UserProfileField.COUNTED_EVENTS.getKey());
    }

    public void testAddField() {
        UserProfileBuilder builder = new UserProfileBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.addFields(UserProfileField.ORDERS, UserProfileField.VERB_NOUNS, UserProfileField.CLICKED_ITEMS);
        assertEquals(accessor.getParamValue(UserProfileBuilder.Keys.FIELDS), "orders,verbNouns,clickedItems");
    }
}
