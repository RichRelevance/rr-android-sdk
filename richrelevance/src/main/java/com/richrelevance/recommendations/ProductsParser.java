package com.richrelevance.recommendations;

import com.richrelevance.Range;
import com.richrelevance.internal.json.JSONArrayParserDelegate;
import com.richrelevance.internal.json.JSONHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductsParser {
    private static final JSONArrayParserDelegate<CompleteProduct> productParserDelegate = new JSONArrayParserDelegate<CompleteProduct>() {
        @Override
        public CompleteProduct parseObject(JSONObject json) {
            return parseCompleteProduct(json);
        }
    };

    static void parseProductResponseInfo(JSONObject json, ProductResponseInfo responseInfo) {
        if(json == null || responseInfo == null) {
            return;
        }

        responseInfo.setRequestId(json.optString("requestId"));
        responseInfo.setProducts(JSONHelper.parseJSONArray(json, "products", productParserDelegate));
    }

    private static CompleteProduct parseCompleteProduct(JSONObject json) {
        if(json == null) {
            return null;
        }

        CompleteProduct product = new CompleteProduct();
        product.setId(json.optString("id"));
        product.setName(json.optString("name"));
        product.setBrand(json.optString("brand"));
        product.setGenre(json.optString("genre"));
        product.setRating(json.optDouble("rating"));
        product.setNumReviews(json.optLong("numReviews"));
        product.setRegionalProductSku(json.optString("regionalProductSku"));
        product.setCategoryIds(JSONHelper.parseStrings(json, "categoryIds"));
        product.setImageUrl(json.optString("imageURL"));
        product.setIsRecommendable(json.optBoolean("isRecommendable"));
        product.setPriceCents(json.optInt("priceCents"));
        product.setSalesPriceCents(json.optInt("salePriceCents"));
        product.setRegionPriceDescription(json.optString("regionPriceDescription"));

        JSONArray priceRangeCentsJson = json.optJSONArray("priceRangeCents");
        if(priceRangeCentsJson != null && priceRangeCentsJson.length() == 2) {
            try {
                int min = priceRangeCentsJson.getInt(0);
                int max = priceRangeCentsJson.getInt(1);
                product.setPriceRangeCents(new Range(min, max));
            } catch(JSONException e) {
                // Don't care, just drop it
            }
        }

        JSONArray salePriceRangeCentsJson = json.optJSONArray("salePriceRangeCents");
        if(salePriceRangeCentsJson != null && salePriceRangeCentsJson.length() == 2) {
            try {
                int min = salePriceRangeCentsJson.getInt(0);
                int max = salePriceRangeCentsJson.getInt(1);
                product.setSalesPriceRangeCents(new Range(min, max));
            } catch(JSONException e) {
                // Don't care, just drop it
            }
        }

        product.setCategories(JSONHelper.parseJSONArray(json, "categories", RecommendationsParser.categoryParserDelegate));

        return product;
    }
}
