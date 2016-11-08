package com.richrelevance.recommendations;

import com.richrelevance.ResponseInfo;

import java.util.List;

public class StrategyResponseInfo extends ResponseInfo {
    private List<RecommendedProduct> products;
    private StrategyType strategyType;
    private String requestId;
    private String message;

    public List<RecommendedProduct> getRecommendedProducts() {
        return products;
    }

    void setRecommendedProducts(List<RecommendedProduct> products) {
        this.products = products;
    }

    public StrategyType getStrategyType() {
        return strategyType;
    }

    void setStrategyType(StrategyType strategyType) {
        this.strategyType = strategyType;
    }

    public String getRequestId() {
        return requestId;
    }

    void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }
}
