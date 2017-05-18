package com.richrelevance.recommendations;

import com.richrelevance.RichRelevance;
import com.richrelevance.utils.ValueMap;

public class RecommendedProduct extends QualifiedProduct {

    public static class Keys {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String BRAND = "brand";
        public static final String GENRE = "genre";
        public static final String RATING = "rating";
        public static final String NUM_REVIEWS = "numReviews";
        public static final String REGIONAL_PRODUCTS_SKU = "regionalProductSku";
        public static final String CATEGORY_IDS = "categoryIds";
        public static final String IMAGE_URL = "imageURL";
        public static final String IS_RECCOMENDABLE = "isRecommendable";
        public static final String PRICE_CENTS = "priceCents";
        public static final String REGION_PRICE_DESCRIPTION = "regionPriceDescription";
        public static final String PRICE_RANGE_CENTS = "priceRangeCents";
        public static final String CLICK_URL = "clickURL";
        public static final String ATTRS = "attributes";
        public static final String CATEGORIES = "categories";
    }

    private String clickUrl;

    private ValueMap<String> attributes;

    public String getClickUrl() {
        return clickUrl;
    }

    void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public ValueMap<String> getAttributes() {
        return attributes;
    }

    void setAttributes(ValueMap<String> attributes) {
        this.attributes = attributes;
    }

    /**
     * Tracks a click on this product.
     */
    public void trackClick() {
        RichRelevance.trackClick(this);
    }
}
