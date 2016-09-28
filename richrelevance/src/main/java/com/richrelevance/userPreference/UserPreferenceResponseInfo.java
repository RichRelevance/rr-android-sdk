package com.richrelevance.userPreference;

import com.richrelevance.ResponseInfo;

public class UserPreferenceResponseInfo extends ResponseInfo {
    private String userId;

    private Preference products;
    private Preference brands;
    private Preference categories;
    private Preference stores;

    public String getUserId() {
        return userId;
    }

    void setUserId(String userId) {
        this.userId = userId;
    }

    public Preference getProducts() {
        return products;
    }

    void setProducts(Preference products) {
        this.products = products;
    }

    public Preference getBrands() {
        return brands;
    }

    void setBrands(Preference brands) {
        this.brands = brands;
    }

    public Preference getCategories() {
        return categories;
    }

    void setCategories(Preference categories) {
        this.categories = categories;
    }

    public Preference getStores() {
        return stores;
    }

    void setStores(Preference stores) {
        this.stores = stores;
    }
}
