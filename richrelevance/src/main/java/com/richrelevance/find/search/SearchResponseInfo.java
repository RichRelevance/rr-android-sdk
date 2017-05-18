package com.richrelevance.find.search;

import com.richrelevance.ResponseInfo;

import java.util.List;


public class SearchResponseInfo extends ResponseInfo {

    private List<Facet> facets;

    private List<SearchResultProduct> products;

    private String addToCartParams;

    public List<Facet> getFacets() {
        return facets;
    }

    public void setFacets(List<Facet> facets) {
        this.facets = facets;
    }

    public List<SearchResultProduct> getProducts() {
        return products;
    }

    public void setProducts(List<SearchResultProduct> products) {
        this.products = products;
    }

    public String getAddToCartParams() {
        return addToCartParams;
    }

    public void setAddToCartParams(String addToCartParams) {
        this.addToCartParams = addToCartParams;
    }
}
