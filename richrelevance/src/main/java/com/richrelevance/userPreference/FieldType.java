package com.richrelevance.userPreference;

public enum FieldType {

    BRAND {
        @Override
        public String getRequestKey() {
            return "brand";
        }
    },
    CATEGORY {
        @Override
        public String getRequestKey() {
            return "category";
        }
    },
    PRODUCT {
        @Override
        public String getRequestKey() {
            return "product";
        }
    },
    STORE {
        @Override
        public String getRequestKey() {
            return "store";
        }
    };

    public abstract String getRequestKey();

    public String getResultKey() {
        return "pref_" + getRequestKey();
    }
}
