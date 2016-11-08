package com.richrelevance.recommendations;

import com.richrelevance.ClientConfiguration;
import com.richrelevance.RequestBuilder;
import com.richrelevance.internal.net.WebResponse;
import com.richrelevance.utils.Utils;

import org.json.JSONObject;

import java.util.Collection;

public class ProductRequestBuilder extends RequestBuilder<ProductResponseInfo> {

    public static class Keys {
        public static final String PRODUCTID = "productId";
        public static final String ATTRIBUTES = "attribute";
    }

    /**
     * Set one or more product ids. Used to retrieve a list of products based on these ids.
     *
     * @param productIds The products to return.
     * @return This builder for chaining method calls.
     */
    public ProductRequestBuilder setProducts(String... productIds) {
        setProducts(Utils.safeAsList(productIds));
        return this;
    }

    /**
     * Sets a list of product ids. Used to retrieve a list of products based on these ids.
     *
     * @param productIds The products to return.
     * @return This builder for chaining method calls.
     */
    public ProductRequestBuilder setProducts(Collection<String> productIds) {
        if(productIds != null) {
            setListParameter(Keys.PRODUCTID, productIds);
        } else {
            removeParameter(Keys.PRODUCTID);
        }
        return this;
    }

    /**
     * Retrieves custom attributes provided in the catalog feed. Use * as the parameter value to request all attributes.
     *
     * @param attributes The attributes to retrieve.
     * @return This builder for chaining method calls
     */
    public ProductRequestBuilder setCatalogFeedAttributes(String... attributes) {
        setListParameterFlat(Keys.ATTRIBUTES, attributes);
        return this;
    }

    /**
     * Retrieves custom attributes provided in the catalog feed. Use * as the parameter value to request all attributes.
     *
     * @param attributes The attributes to retrieve.
     * @return This builder for chaining method calls
     */
    public ProductRequestBuilder setCatalogFeedAttributes(Collection<String> attributes) {
        setListParameterFlat(Keys.ATTRIBUTES, attributes);
        return this;
    }

    @Override
    protected ProductResponseInfo createNewResult() {
        return new ProductResponseInfo();
    }

    @Override
    protected String getEndpointPath(ClientConfiguration configuration) {
        return "rrserver/api/rrPlatform/getProducts";
    }

    @Override
    protected void populateResponse(WebResponse response, JSONObject json, ProductResponseInfo responseInfo) {
        ProductsParser.parseProductResponseInfo(json, responseInfo);
    }
}
