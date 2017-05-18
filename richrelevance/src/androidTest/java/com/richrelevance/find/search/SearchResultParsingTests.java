package com.richrelevance.find.search;

import android.test.AndroidTestCase;

import com.richrelevance.Error;
import com.richrelevance.RequestBuilderAccessor;
import com.richrelevance.internal.TestResultCallback;
import com.richrelevance.mocking.MockWebResponse;
import com.richrelevance.mocking.ResponseBuilder;

public class SearchResultParsingTests extends AndroidTestCase {

    public void testParseSearch() {
        SearchRequestBuilder.RCSSearchTokenListener rcsSearchTokenListener = new SearchRequestBuilder.RCSSearchTokenListener();

        SearchRequestBuilder builder = new SearchRequestBuilder(rcsSearchTokenListener);
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ResponseBuilder responseBuilder = new ResponseBuilder()
                .setResponseCode(200)
                .setContentAssetPath("findSearch.json");
        MockWebResponse response = new MockWebResponse(responseBuilder, getContext());

        TestResultCallback<?> callback = new TestResultCallback<SearchResponseInfo>() {

            @Override
            protected void testResponse(SearchResponseInfo response, Error error) {
                assertNull(error);
                assertNotNull(response);
                assertEquals("OK", response.getStatus());
                assertEquals(10, response.getProducts().size());

                SearchResultProduct product = response.getProducts().get(0);

                assertEquals("WB3AJ1R0YT5", product.getId());
                assertEquals("Mid-Rise Power Curvy Jeans in Reller Wash", product.getName());
                assertEquals("guess", product.getBrand());
                assertEquals(2.7763522, product.getScore());
                assertEquals(6230, product.getSalesPriceCents());
                assertEquals(0, product.getNumReviews());
                assertEquals(8900, product.getPriceCents());
                assertEquals(
                        "http://recs.richrelevance.com/rrserver/api/find/v1/track/click/199c81c05e473265?a=199c81c05e473265&vg=ddd227d8-2e4e-4cc9-cb8c-f655eefe2f70&pti=2&pa=find&hpi=0&stn=PersonalizedProductSearchAndBrowse&stid=184&rti=2&sgs=&u=RZTestUserTest&mvtId=-1&mvtTs=1476328054521&uguid=d8db2bd5-aff9-4865-33e6-7355ee901b90&channelId=Android&s=659c2799-04a6-4f8f-b654-3db02ddaf5bb&pg=-1&page=1&query=sh&lang=en&searchConfigId=57c4a9230db39000194e3fc7&p=WB3AJ1R0YT5&ind=0&ct=http%3A%2F%2Fshop.guess.com%2FCatalog%2FView%2FWB3AJ1R0YT5",
                        product.getClickUrl());
                assertEquals("/Catalog/View/WB3AJ1R0YT5", product.getLinkId());
                assertEquals(4, response.getFacets().size());

                Facet facet = response.getFacets().get(0);
                assertEquals("releaseDate", facet.getType());
                assertEquals(100, facet.getFilters().size());

                Filter filter = facet.getFilters().get(0);
                assertEquals("1465628400000", filter.getValue());
                assertEquals("product_release_date:1465628400000", filter.getFilter());
                assertEquals(46, filter.getCount());
            }
        };

        accessor.parseResponse(response, callback);
        callback.assertSuccess(this);
    }
}
