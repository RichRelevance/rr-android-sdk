package com.richrelevance.recommendations;

import android.text.TextUtils;

import java.util.Locale;

public class Placement {

    public enum PlacementType {

        HOME {
            @Override
            public String getKey() {
                return "home_page";
            }
        },
        ITEM {
            @Override
            public String getKey() {
                return "item_page";
            }
        },
        ADD_TO_CART {
            @Override
            public String getKey() {
                return "add_to_cart_page";
            }
        },
        SEARCH {
            @Override
            public String getKey() {
                return "search_page";
            }
        },
        PURCHASE_COMPLETE {
            @Override
            public String getKey() {
                return "purchase_complete_page";
            }
        },
        CATEGORY {
            @Override
            public String getKey() {
                return "category_page";
            }
        },
        CART {
            @Override
            public String getKey() {
                return "cart_page";
            }
        };


        public abstract String getKey();

        public static PlacementType fromKey(String key) {
            for (PlacementType type : PlacementType.values()) {
                if (type.getKey().equals(key)) {
                    return type;
                }
            }

            return null;
        }
    }

    private PlacementType pageType;
    private String name;

    public Placement(PlacementType pageType, String name) {
        this.pageType = pageType;
        this.name = name;
    }

    public Placement(String apiValue) {
        if (!TextUtils.isEmpty(apiValue)) {
            String[] values = apiValue.split("\\.");
            if (values.length == 2) {
                this.pageType = PlacementType.fromKey(values[0]);
                this.name = values[1];
            }
        }
    }

    public String getApiValue() {
        return String.format(Locale.US, "%s.%s", pageType.getKey(), name);
    }

    public PlacementType getPageType() {
        return pageType;
    }

    public String getName() {
        return name;
    }
}
