package com.richrelevance.recommendations;

import com.richrelevance.ResponseInfo;

import java.util.List;

public class ProductResponseInfo extends ResponseInfo {

    private String requestId;

    private List<CompleteProduct> products;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public List<CompleteProduct> getProducts() {
        return products;
    }

    public void setProducts(List<CompleteProduct> products) {
        this.products = products;
    }
}
