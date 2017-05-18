package com.richrelevance.userProfile;

import com.richrelevance.internal.json.JSONArrayParserDelegate;
import com.richrelevance.internal.json.JSONHelper;

import org.json.JSONObject;

class UserProfileParser {

    static void parseUserProfileResponseInfo(JSONObject json, UserProfileResponseInfo responseInfo) {
        if (json == null || responseInfo == null) {
            return;
        }

        responseInfo.setUserId(json.optString("userId"));
        responseInfo.setMostRecentRRUserGuid(json.optString("mostRecentRRUserGuid"));
        responseInfo.setTimeOfFirstEvent(json.optLong("timeOfFirstEvent"));

        responseInfo.setReferrerUrls(JSONHelper.parseJSONArray(json, "referrerUrls", REFFERER_PARSER));
        responseInfo.setUserSegments(JSONHelper.parseJSONArray(json, "userSegments", USER_SEGMENT_PARSER));
        responseInfo.setUserAttributes(JSONHelper.parseJSONArray(json, "userAttributes", USER_ATTRIBUTE_PARSER));
        responseInfo.setVerbNouns(JSONHelper.parseJSONArray(json, "verbNouns", VERB_NOUN_PARSER));

        responseInfo.setViewedItems(JSONHelper.parseJSONArray(json, "viewedItems", VIEWED_ITEM_PARSER));
        responseInfo.setViewedCategories(JSONHelper.parseJSONArray(json, "viewedCategories", VIEWED_CATEGORY_PARSER));
        responseInfo.setViewedBrands(JSONHelper.parseJSONArray(json, "viewedBrands", VIEWED_BRAND_PARSER));

        responseInfo.setClickedItems(JSONHelper.parseJSONArray(json, "clickedItems", CLICKED_ITEM_PARSER));
        responseInfo.setSearchedTerms(JSONHelper.parseJSONArray(json, "searchedTerms", SEARCHED_TERMS_PARSER));
        responseInfo.setAddedToCartItems(JSONHelper.parseJSONArray(json, "addedToCartItems", ADDED_TO_CART_ITEM_PARSER));
        responseInfo.setOrders(JSONHelper.parseJSONArray(json, "orders", ORDER_PARSER));
        responseInfo.setCountedEvents(JSONHelper.parseJSONArray(json, "countedEvents", COUNTED_EVENT_PARSER));

        responseInfo.setBatchAttributes(json.optJSONObject("batchAttributes"));
    }

    private static <T extends UserProfileElement> T parse(T element, JSONObject json) {
        element.parse(json);
        return element;
    }

    private static final JSONArrayParserDelegate<UserProfileElement.ReferrerUrl> REFFERER_PARSER =
            new JSONArrayParserDelegate<UserProfileElement.ReferrerUrl>() {
                @Override
                public UserProfileElement.ReferrerUrl parseObject(JSONObject json) {
                    return parse(new UserProfileElement.ReferrerUrl(), json);
                }
            };

    private static final JSONArrayParserDelegate<UserProfileElement.UserSegment> USER_SEGMENT_PARSER =
            new JSONArrayParserDelegate<UserProfileElement.UserSegment>() {
                @Override
                public UserProfileElement.UserSegment parseObject(JSONObject json) {
                    return parse(new UserProfileElement.UserSegment(), json);
                }
            };

    private static final JSONArrayParserDelegate<UserProfileElement.UserAttribute> USER_ATTRIBUTE_PARSER =
            new JSONArrayParserDelegate<UserProfileElement.UserAttribute>() {
                @Override
                public UserProfileElement.UserAttribute parseObject(JSONObject json) {
                    return parse(new UserProfileElement.UserAttribute(), json);
                }
            };

    private static final JSONArrayParserDelegate<UserProfileElement.VerbNoun> VERB_NOUN_PARSER =
            new JSONArrayParserDelegate<UserProfileElement.VerbNoun>() {
                @Override
                public UserProfileElement.VerbNoun parseObject(JSONObject json) {
                    return parse(new UserProfileElement.VerbNoun(), json);
                }
            };

    private static final JSONArrayParserDelegate<UserProfileElement.ViewedItem> VIEWED_ITEM_PARSER =
            new JSONArrayParserDelegate<UserProfileElement.ViewedItem>() {
                @Override
                public UserProfileElement.ViewedItem parseObject(JSONObject json) {
                    return parse(new UserProfileElement.ViewedItem(), json);
                }
            };

    private static final JSONArrayParserDelegate<UserProfileElement.ViewedCategory> VIEWED_CATEGORY_PARSER =
            new JSONArrayParserDelegate<UserProfileElement.ViewedCategory>() {
                @Override
                public UserProfileElement.ViewedCategory parseObject(JSONObject json) {
                    return parse(new UserProfileElement.ViewedCategory(), json);
                }
            };

    private static final JSONArrayParserDelegate<UserProfileElement.ViewedBrand> VIEWED_BRAND_PARSER =
            new JSONArrayParserDelegate<UserProfileElement.ViewedBrand>() {
                @Override
                public UserProfileElement.ViewedBrand parseObject(JSONObject json) {
                    return parse(new UserProfileElement.ViewedBrand(), json);
                }
            };

    private static final JSONArrayParserDelegate<UserProfileElement.ClickedItem> CLICKED_ITEM_PARSER =
            new JSONArrayParserDelegate<UserProfileElement.ClickedItem>() {
                @Override
                public UserProfileElement.ClickedItem parseObject(JSONObject json) {
                    return parse(new UserProfileElement.ClickedItem(), json);
                }
            };

    private static final JSONArrayParserDelegate<UserProfileElement.SearchedTerm> SEARCHED_TERMS_PARSER =
            new JSONArrayParserDelegate<UserProfileElement.SearchedTerm>() {
                @Override
                public UserProfileElement.SearchedTerm parseObject(JSONObject json) {
                    return parse(new UserProfileElement.SearchedTerm(), json);
                }
            };

    private static final JSONArrayParserDelegate<UserProfileElement.AddedToCartItem> ADDED_TO_CART_ITEM_PARSER =
            new JSONArrayParserDelegate<UserProfileElement.AddedToCartItem>() {
                @Override
                public UserProfileElement.AddedToCartItem parseObject(JSONObject json) {
                    return parse(new UserProfileElement.AddedToCartItem(), json);
                }
            };

    private static final JSONArrayParserDelegate<UserProfileElement.Order> ORDER_PARSER =
            new JSONArrayParserDelegate<UserProfileElement.Order>() {
                @Override
                public UserProfileElement.Order parseObject(JSONObject json) {
                    return parse(new UserProfileElement.Order(), json);
                }
            };

    private static final JSONArrayParserDelegate<UserProfileElement.CountedEvent> COUNTED_EVENT_PARSER =
            new JSONArrayParserDelegate<UserProfileElement.CountedEvent>() {
                @Override
                public UserProfileElement.CountedEvent parseObject(JSONObject json) {
                    return parse(new UserProfileElement.CountedEvent(), json);
                }
            };
}
