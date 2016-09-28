package com.richrelevance.userProfile;


import com.richrelevance.BaseAndroidTestCase;
import com.richrelevance.RequestBuilderAccessor;
import com.richrelevance.internal.TestResultCallback;
import com.richrelevance.mocking.MockWebResponse;
import com.richrelevance.mocking.ResponseBuilder;

public class UserProfileParsingTests extends BaseAndroidTestCase {

    public void testParseUserProfile() {
        UserProfileBuilder builder = new UserProfileBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ResponseBuilder responseBuilder = new ResponseBuilder()
                .setResponseCode(200)
                .setContentAssetPath("userProfile.json");
        MockWebResponse response = new MockWebResponse(responseBuilder, getContext());

        TestResultCallback<?> callback = new TestResultCallback<UserProfileResponseInfo>() {
            @Override
            protected void testResponse(UserProfileResponseInfo response, com.richrelevance.Error error) {
                assertNull(error);
                assertNotNull(response);

                assertEquals("RZTestUser", response.getUserId());
                assertEquals("e886b297-8ef4-43bc-2313-52536fc162c6", response.getMostRecentRRUserGuid());
                assertEquals(1432757395552L, response.getTimeOfFirstEvent());


                // Viewed Items
                assertEquals(1, response.getViewedItems().size());
                UserProfileElement.ViewedItem viewedItem = response.getViewedItems().get(0);
                assertEquals("10291452", viewedItem.getItemId());

                // Clicked Items
                assertEquals(2, response.getClickedItems().size());
                UserProfileElement.ClickedItem clickedItem = response.getClickedItems().get(0);
                assertEquals("click2", clickedItem.getItemId());

                // Orders
                assertEquals(1, response.getOrders().size());
                UserProfileElement.Order order = response.getOrders().get(0);
                assertEquals("8C9F19A4-7E7D-DC12-1AD6-63BC99DE4633", order.getSessionId());
                assertEquals("bccfa17d092268c0", order.getChannel());
                assertEquals("10001", order.getOrderId());
                assertEquals(1433791582850L, order.getTimestamp());

                assertEquals(1, order.getItems().size());
                UserProfileElement.Item item = order.getItems().get(0);
                assertEquals("10291452", item.getItemId());
                assertEquals(1, item.getQuantity());
                assertEquals(5999, item.getPriceInCents());


                // Viewed Categories
                assertEquals(2, response.getViewedCategories().size());
                UserProfileElement.ViewedCategory viewedCategory = response.getViewedCategories().get(0);
                assertEquals("viewCategory2", viewedCategory.getCategoryId());

                // Viewed Brands
                assertEquals(2, response.getViewedBrands().size());
                UserProfileElement.ViewedBrand viewedBrand = response.getViewedBrands().get(0);
                assertEquals("viewBrand2", viewedBrand.getBrand());

                // Added To Cart Items
                assertEquals(15, response.getAddedToCartItems().size());
                UserProfileElement.AddedToCartItem cartItem = response.getAddedToCartItems().get(0);
                assertEquals(2, cartItem.getItems().size());

                // Searched Terms
                assertEquals(3, response.getSearchedTerms().size());
                UserProfileElement.SearchedTerm searchedTerm = response.getSearchedTerms().get(0);
                assertEquals("nuts", searchedTerm.getSearchTerm());

                // User Attributes
                assertEquals(4, response.getUserAttributes().size());
                UserProfileElement.UserAttribute userAttribute = response.getUserAttributes().get(0);
                assertEquals("WEB", userAttribute.getChannel());
                 assertNotNull(userAttribute.getValues());
                assertEquals("facebook", userAttribute.getValues().get("Social").get(0));

                // Referrer
                assertEquals(2, response.getReferrerUrls().size());
                UserProfileElement.ReferrerUrl referrerUrl = response.getReferrerUrls().get(0);
                assertEquals("ref2", referrerUrl.getUrl());

                // Segments
                assertEquals(2, response.getUserSegments().size());
                UserProfileElement.UserSegment segment = response.getUserSegments().get(0);
                assertEquals("WEB", segment.getChannel());
                assertTrue(segment.getSegments().contains("1"));

                // Verb Nouns
                assertEquals(5, response.getVerbNouns().size());
                UserProfileElement.VerbNoun verbNoun = response.getVerbNouns().get(0);
                assertNonEmpty(verbNoun.getVerb());
                assertEquals("st1", verbNoun.getNoun());

                // Counted Events
                assertEquals(2, response.getCountedEvents().size());
                UserProfileElement.CountedEvent event = response.getCountedEvents().get(0);
                assertEquals("e1", event.getValue());
                assertEquals(3, event.getCount());
                assertEquals(1419150628318L, event.getMostRecentTime());

                // Batch Attributes
                assertNotNull(response.getBatchAttributes());
                assertEquals("someValue", response.getBatchAttributes().optString("someField"));
            }
        };

        accessor.parseResponse(response, callback);
        callback.assertSuccess(this);
    }
}
