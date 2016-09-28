package com.richrelevance.userProfile;

public enum UserProfileField {

    VIEWED_ITEMS {
        @Override
        public String getKey() {
            return "viewedItems";
        }
    },
    CLICKED_ITEMS {
        @Override
        public String getKey() {
            return "clickedItems";
        }
    },
    REFERRER_URLS {
        @Override
        public String getKey() {
            return "referrerUrls";
        }
    },
    ORDERS {
        @Override
        public String getKey() {
            return "orders";
        }
    },
    VIEWED_CATEGORIES{
        @Override
        public String getKey() {
            return "viewedCategories";
        }
    },
    VIEWED_BRANDS {
        @Override
        public String getKey() {
            return "viewedBrands";
        }
    },
    ADDED_TO_CART_ITEMS {
        @Override
        public String getKey() {
            return "addedToCartItems";
        }
    },
    SEARCHED_TERMS{
        @Override
        public String getKey() {
            return "searchedTerms";
        }
    },
    USER_ATTRIBUTES {
        @Override
        public String getKey() {
            return "userAttributes";
        }
    },
    USER_SEGMENTS {
        @Override
        public String getKey() {
            return "userSegments";
        }
    },
    VERB_NOUNS {
        @Override
        public String getKey() {
            return "verbNouns";
        }
    },
    COUNTED_EVENTS {
        @Override
        public String getKey() {
            return "countedEvents";
        }
    },
    ALL {
        @Override
        public String getKey() {
            return "all";
        }
    },;

    public abstract String getKey();
}
