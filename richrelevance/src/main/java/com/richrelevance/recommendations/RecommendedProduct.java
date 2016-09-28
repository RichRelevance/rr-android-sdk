package com.richrelevance.recommendations;

import com.richrelevance.RichRelevance;
import com.richrelevance.utils.ValueMap;

public class RecommendedProduct extends QualifiedProduct {
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
