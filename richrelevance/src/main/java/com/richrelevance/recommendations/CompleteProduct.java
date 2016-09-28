package com.richrelevance.recommendations;

import com.richrelevance.Range;

public class CompleteProduct extends QualifiedProduct {
    private int salesPriceCents;

    private Range salesPriceRangeCents;

    public int getSalesPriceCents() {
        return salesPriceCents;
    }

    void setSalesPriceCents(int salesPriceCents) {
        this.salesPriceCents = salesPriceCents;
    }

    public Range getSalesPriceRangeCents() {
        return salesPriceRangeCents;
    }

    void setSalesPriceRangeCents(Range salesPriceRangeCents) {
        this.salesPriceRangeCents = salesPriceRangeCents;
    }
}
