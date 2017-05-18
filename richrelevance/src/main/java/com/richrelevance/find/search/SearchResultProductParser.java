package com.richrelevance.find.search;

import android.util.Log;

import com.richrelevance.internal.json.JSONArrayParserDelegate;
import com.richrelevance.internal.json.JSONHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultProductParser {

    static void parseSearchResponseInfo(JSONObject json, SearchResponseInfo responseInfo) {
        if (json == null || responseInfo == null) {
            return;
        }

        try {
            JSONArray resultPlacements = json.getJSONArray("placements");
            if (resultPlacements != null) {

                // The SDK only currently supports a single placement specification for Search request and single placement handling from the response.
                JSONObject supportedPlacement = resultPlacements.getJSONObject(0);
                if (supportedPlacement != null) {
                    List<Facet> facets = JSONHelper.parseJSONArray(supportedPlacement.getJSONArray("facets"), facetResponseParserDelegate);
                    responseInfo.setFacets(facets);

                    responseInfo.setAddToCartParams(supportedPlacement.optString("addtoCartParams"));

                    JSONArray productsArray = supportedPlacement.getJSONArray("docs");
                    List<SearchResultProduct> products = new ArrayList<>();

                    for (int i = 0; i < productsArray.length(); i++) {
                        SearchResultProduct product = parseSearchResultProduct(facets, productsArray.getJSONObject(i));
                        products.add(product);
                    }
                    responseInfo.setProducts(products);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static Facet parseFacet(JSONObject json) {
        if (json == null) {
            return null;
        }

        final Facet facet = new Facet();
        facet.setType(json.optString(Facet.Keys.TYPE));
        facet.setFilters(JSONHelper.parseJSONArray(json, Facet.Keys.FITERS, new JSONArrayParserDelegate<Filter>() {
                    @Override
                    public Filter parseObject(JSONObject json) {
                        return parseFilter(facet, json);
                    }
                })
        );

        return facet;
    }

    static Filter parseFilter(Facet facet, JSONObject json) {
        Filter filter = null;
        if (json != null) {
            try {
                filter = new Filter(facet);
                filter.setCount(json.getInt(Filter.Keys.COUNT));
                filter.setFilter(json.getString(Filter.Keys.FILTER));
                filter.setValue(json.getString(Filter.Keys.VALUE));

            } catch (JSONException e) {
                Log.e(SearchResultProductParser.class.getSimpleName(), "Unable to parse Filter object due to missing expected json response fields");
            }
        }
        return filter;
    }

    static SearchResultProduct parseSearchResultProduct(List<Facet> facets, JSONObject json) {
        SearchResultProduct product = null;
        if (json != null) {

            try {
                product = new SearchResultProduct();
                product.setId(json.getString(SearchResultProduct.Keys.ID));
                product.setName(json.getString(SearchResultProduct.Keys.NAME));
                product.setClickUrl(json.optString(SearchResultProduct.Keys.CLICK_URL));
                product.setImageId(json.optString(SearchResultProduct.Keys.IMAGE_ID));
                product.setLinkId(json.optString(SearchResultProduct.Keys.LINK_ID));
                product.setNumReviews(json.optInt(SearchResultProduct.Keys.NUM_REVIEWS));
                product.setDescription(json.optString(SearchResultProduct.Keys.DESCRIPTION));
                product.setScore(json.optDouble(SearchResultProduct.Keys.SCORE));
                product.setPriceCents(json.optInt(SearchResultProduct.Keys.PRICE_CENTS));
                product.setSalesPriceCents(json.optInt(SearchResultProduct.Keys.SALES_PRICE_CENTS));
                product.setBrand(json.optString(SearchResultProduct.Keys.BRAND));

                product.setCategoryNames(listify(json.optJSONArray(SearchResultProduct.Keys.CATEGORY_NAMES)));
                product.setCategoryIds(listify(json.optJSONArray(SearchResultProduct.Keys.CATEGORY_IDS)));

                for (Facet facet : facets) {
                    if (json.has(facet.getType())) {
                        product.addFilter(facet, json.get(facet.getType()));
                    }
                }

            } catch (JSONException e) {
                Log.e(SearchResultProductParser.class.getSimpleName(), "Unable to parse SearchResultProduct object due to missing expected json response fields");
            }
        }

        return product;
    }

    private static final JSONArrayParserDelegate<Facet> facetResponseParserDelegate =
            new JSONArrayParserDelegate<Facet>() {
                @Override
                public Facet parseObject(JSONObject json) {
                    return parseFacet(json);
                }
            };

    private static List<String> listify(JSONArray jsonArray) throws JSONException {
        if (jsonArray == null) {
            return null;
        }

        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }

        return list;
    }
}
