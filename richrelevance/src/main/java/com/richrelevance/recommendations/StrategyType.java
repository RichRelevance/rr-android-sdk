package com.richrelevance.recommendations;

public enum StrategyType {
    SITE_WIDE_BEST_SELLERS {
        @Override
        public String getKey() {
            return "SiteWideBestSellers";
        }
    },
    PRODUCT_BOUGHT_BOUGHT {
        @Override
        public String getKey() {
            return "ProductBoughtBought";
        }
    },
    CATEGORY_BEST_SELLERS {
        @Override
        public String getKey() {
            return "CategoryBestSellers";
        }
    },
    PRODUCT_VIEWED_VIEWED {
        @Override
        public String getKey() {
            return "ProductViewedViewed";
        }
    },
    SEARCH_BOUGHT {
        @Override
        public String getKey() {
            return "SearchBought";
        }
    },
    RATINGS_REVIEWS {
        @Override
        public String getKey() {
            return "RatingsReviews";
        }
    },
    NEW_ARRIVALS {
        @Override
        public String getKey() {
            return "NewArrivals";
        }
    },
    CATEGORY_BOUGHT_BOUGHT {
        @Override
        public String getKey() {
            return "CategoryBoughtBought";
        }
    },
    MOVERS_AND_SHAKERS {
        @Override
        public String getKey() {
            return "MoversAndShakers";
        }
    },
    PERSONALIZED {
        @Override
        public String getKey() {
            return "Personalized";
        }
    },
    BRAND_TOP_SELLERS {
        @Override
        public String getKey() {
            return "BrandTopSellers";
        }
    },;

    public abstract String getKey();

    public static StrategyType fromKey(String key) {
        for (StrategyType type : StrategyType.values()) {
            if (type.getKey().equals(key)) {
                return type;
            }
        }

        return null;
    }
}
