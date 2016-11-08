package com.richrelevance.recommendations;

import com.richrelevance.Range;
import com.richrelevance.internal.json.JSONArrayParserDelegate;
import com.richrelevance.internal.json.JSONHelper;
import com.richrelevance.utils.ParsingUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class RecommendationsParser {

    private static final String KEY_RECOMMENDED_PRODUCTS = "recommendedProducts";

    static void parsePlacementResponseInfo(JSONObject json, PlacementResponseInfo responseInfo) {
        if (json == null || responseInfo == null) {
            return;
        }

        responseInfo.setViewGuid(json.optString("viewGuid"));

        responseInfo.setPlacements(JSONHelper.parseJSONArray(json, "placements", placementResponseParserDelegate));
    }

    static void parseStrategyResponseInfo(JSONObject json, StrategyResponseInfo responseInfo) {
        if (json == null || responseInfo == null) {
            return;
        }

        responseInfo.setRequestId(json.optString("requestId"));
        responseInfo.setStrategyType(StrategyType.fromKey(json.optString("strategyName")));
        responseInfo.setMessage(json.optString("message"));

        responseInfo.setRecommendedProducts(JSONHelper.parseJSONArray(json, KEY_RECOMMENDED_PRODUCTS, productRecommendationParserDelegate));
    }

    static PlacementResponse parsePlacementResponse(JSONObject json) {
        if (json == null) {
            return null;
        }

        PlacementResponse response = new PlacementResponse();
        response.setPlacement(new Placement(json.optString("placement")));
        response.setStrategyMessage(json.optString("strategyMessage"));

        response.setHtmlElementId(json.optString("htmlElementId"));
        response.setHtml(json.optString("html"));

        response.setRecommendedProducts(
                JSONHelper.parseJSONArray(json, KEY_RECOMMENDED_PRODUCTS, productRecommendationParserDelegate));

        return response;
    }

    static RecommendedProduct parseProductRecommendation(JSONObject json) {
        if (json == null) {
            return null;
        }

        RecommendedProduct product = new RecommendedProduct();
        product.setId(json.optString(RecommendedProduct.Keys.ID));
        product.setName(json.optString(RecommendedProduct.Keys.NAME));
        product.setBrand(json.optString(RecommendedProduct.Keys.BRAND));
        product.setGenre(json.optString(RecommendedProduct.Keys.GENRE));
        product.setRating(json.optDouble(RecommendedProduct.Keys.RATING));
        product.setNumReviews(json.optLong(RecommendedProduct.Keys.NUM_REVIEWS));
        product.setRegionalProductSku(json.optString(RecommendedProduct.Keys.REGIONAL_PRODUCTS_SKU));
        product.setCategoryIds(JSONHelper.parseStrings(json, RecommendedProduct.Keys.CATEGORY_IDS));
        product.setImageUrl(json.optString(RecommendedProduct.Keys.IMAGE_URL));
        product.setIsRecommendable(json.optBoolean(RecommendedProduct.Keys.IS_RECCOMENDABLE));

        product.setPriceCents(json.optInt(RecommendedProduct.Keys.PRICE_CENTS));
        product.setRegionPriceDescription(json.optString(RecommendedProduct.Keys.REGION_PRICE_DESCRIPTION));
        JSONArray priceRangeCentsJson = json.optJSONArray(RecommendedProduct.Keys.PRICE_RANGE_CENTS);
        if (priceRangeCentsJson != null && priceRangeCentsJson.length() == 2) {
            try {
                int min = priceRangeCentsJson.getInt(0);
                int max = priceRangeCentsJson.getInt(1);
                product.setPriceRangeCents(new Range(min, max));
            } catch (JSONException e) {
                // Don't care, just drop it
            }
        }

        product.setClickUrl(json.optString(RecommendedProduct.Keys.CLICK_URL));

        product.setAttributes(ParsingUtils.optValueMap(json, RecommendedProduct.Keys.ATTRS));
        product.setCategories(JSONHelper.parseJSONArray(json, RecommendedProduct.Keys.CATEGORIES, categoryParserDelegate));

        return product;
    }

    static Category parseCategory(JSONObject json) {
        if (json == null) {
            return null;
        }

        Category category = new Category();
        category.setId(json.optString("id"));
        category.setName(json.optString("name"));
        category.setHasChildren(json.optBoolean("hasChildren"));

        return category;
    }

    private static final JSONArrayParserDelegate<PlacementResponse> placementResponseParserDelegate =
            new JSONArrayParserDelegate<PlacementResponse>() {
                @Override
                public PlacementResponse parseObject(JSONObject json) {
                    return parsePlacementResponse(json);
                }
            };

    private static final JSONArrayParserDelegate<RecommendedProduct> productRecommendationParserDelegate =
            new JSONArrayParserDelegate<RecommendedProduct>() {
                @Override
                public RecommendedProduct parseObject(JSONObject json) {
                    return parseProductRecommendation(json);
                }
            };

    public static final JSONArrayParserDelegate<Category> categoryParserDelegate =
            new JSONArrayParserDelegate<Category>() {
                @Override
                public Category parseObject(JSONObject json) {
                    return parseCategory(json);
                }
            };
}
