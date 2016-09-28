package com.richrelevance.userProfile;

import com.richrelevance.internal.json.JSONArrayParserDelegate;
import com.richrelevance.internal.json.JSONHelper;
import com.richrelevance.utils.ParsingUtils;
import com.richrelevance.utils.ValueMap;

import org.json.JSONObject;

import java.util.List;

public abstract class UserProfileElement {

    private String sessionId;
    private long timestamp;

    public String getSessionId() {
        return sessionId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    void parse(JSONObject jsonObject) {
        sessionId = jsonObject.optString("sessionId");
        timestamp = jsonObject.optLong("timestamp", 0);
        onParse(jsonObject);
    }

    protected abstract void onParse(JSONObject jsonObject);

    public static class ReferrerUrl extends UserProfileElement {
        private String url;

        public String getUrl() {
            return url;
        }

        @Override
        protected void onParse(JSONObject jsonObject) {
            url = jsonObject.optString("url");
        }
    }

    public static class UserSegment extends UserProfileElement {
        private String channel;
        private List<String> segments;

        public List<String> getSegments() {
            return segments;
        }

        public String getChannel() {
            return channel;
        }

        @Override
        protected void onParse(JSONObject jsonObject) {
            channel = jsonObject.optString("channel");
            segments = JSONHelper.parseStrings(jsonObject, "segments");
        }
    }

    public static class UserAttribute extends UserProfileElement {
        private String channel;
        private ValueMap<String> values;

        @Override
        protected void onParse(JSONObject jsonObject) {
            channel = jsonObject.optString("channel");
            values = ParsingUtils.optValueMap(jsonObject, "values");
        }

        public String getChannel() {
            return channel;
        }

        public ValueMap<String> getValues() {
            return values;
        }
    }

    public static class VerbNoun extends UserProfileElement {
        private String verb;
        private String noun;

        public String getVerb() {
            return verb;
        }

        public String getNoun() {
            return noun;
        }

        @Override
        protected void onParse(JSONObject jsonObject) {
            verb = jsonObject.optString("verb");
            noun = jsonObject.optString("noun");
        }
    }

    public static class ViewedItem extends UserProfileElement {
        private String channel;
        private String itemId;

        public String getChannel() {
            return channel;
        }

        public String getItemId() {
            return itemId;
        }

        @Override
        protected void onParse(JSONObject jsonObject) {
            channel = jsonObject.optString("channel");
            itemId = jsonObject.optString("itemId");
        }
    }

    public static class ViewedCategory extends UserProfileElement {
        private String categoryId;

        public String getCategoryId() {
            return categoryId;
        }

        @Override
        protected void onParse(JSONObject jsonObject) {
            categoryId = jsonObject.optString("categoryId");
        }
    }

    public static class ViewedBrand extends UserProfileElement {
        private String brand;

        public String getBrand() {
            return brand;
        }

        @Override
        protected void onParse(JSONObject jsonObject) {
            brand = jsonObject.optString("brand");
        }
    }

    public static class ClickedItem extends UserProfileElement {
        private String itemId;

        public String getItemId() {
            return itemId;
        }

        @Override
        protected void onParse(JSONObject jsonObject) {
            itemId = jsonObject.optString("itemId");
        }
    }

    public static class SearchedTerm extends UserProfileElement {
        private String channel;
        private String searchTerm;

        public String getChannel() {
            return channel;
        }

        public String getSearchTerm() {
            return searchTerm;
        }

        @Override
        protected void onParse(JSONObject jsonObject) {
            channel = jsonObject.optString("channel");
            searchTerm = jsonObject.optString("searchTerm");
        }
    }

    public static class AddedToCartItem extends UserProfileElement {
        private String channel;
        private List<Item> items;

        public String getChannel() {
            return channel;
        }

        public List<Item> getItems() {
            return items;
        }

        @Override
        protected void onParse(JSONObject jsonObject) {
            channel = jsonObject.optString("channel");
            items = JSONHelper.parseJSONArray(jsonObject, "items", Item.PARSER_DELEGATE);
        }
    }

    public static class Order extends UserProfileElement {
        private String channel;
        private String orderId;
        private List<Item> items;

        public String getChannel() {
            return channel;
        }

        public String getOrderId() {
            return orderId;
        }

        public List<Item> getItems() {
            return items;
        }

        @Override
        protected void onParse(JSONObject jsonObject) {
            channel = jsonObject.optString("channel");
            orderId = jsonObject.optString("orderId");
            items = JSONHelper.parseJSONArray(jsonObject, "items", Item.PARSER_DELEGATE);
        }
    }

    public static class CountedEvent extends UserProfileElement {
        private String value;
        private int count;
        private long mostRecentTime;

        public String getValue() {
            return value;
        }

        public int getCount() {
            return count;
        }

        public long getMostRecentTime() {
            return mostRecentTime;
        }

        @Override
        protected void onParse(JSONObject jsonObject) {
            value = jsonObject.optString("value");
            count = jsonObject.optInt("count");
            mostRecentTime = jsonObject.optLong("mostRecentTime");
        }
    }

    public static class Item {
        private String itemId;
        private int quantity;
        private int priceInCents;

        public String getItemId() {
            return itemId;
        }

        public int getQuantity() {
            return quantity;
        }

        public int getPriceInCents() {
            return priceInCents;
        }

        Item parse(JSONObject json) {
            itemId = json.optString("itemId");
            quantity = json.optInt("quantity");
            priceInCents = json.optInt("priceInCents");
            return this;
        }

        static final JSONArrayParserDelegate<Item> PARSER_DELEGATE = new JSONArrayParserDelegate<Item>() {
            @Override
            public Item parseObject(JSONObject json) {
                if (json != null) {
                    return new Item().parse(json);
                }
                return null;
            }
        };
    }
}