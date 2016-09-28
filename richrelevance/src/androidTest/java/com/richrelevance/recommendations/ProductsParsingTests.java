package com.richrelevance.recommendations;

import com.richrelevance.BaseAndroidTestCase;
import com.richrelevance.Error;
import com.richrelevance.RequestBuilderAccessor;
import com.richrelevance.internal.TestResultCallback;
import com.richrelevance.mocking.MockWebResponse;
import com.richrelevance.mocking.ResponseBuilder;

import java.util.List;

public class ProductsParsingTests extends BaseAndroidTestCase {

    public void testParseProducts() {
        ProductBuilder builder = new ProductBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ResponseBuilder responseBuilder = new ResponseBuilder().setResponseCode(200).setContentAssetPath("products.json");
        MockWebResponse response = new MockWebResponse(responseBuilder, getContext());

        TestResultCallback<?> callback = new TestResultCallback<ProductResponseInfo>() {
            @Override
            protected void testResponse(ProductResponseInfo response, com.richrelevance.Error error) {
                assertNull(error);
                assertNotNull(response);

                assertEmpty(response.getRequestId());

                List<CompleteProduct> products = response.getProducts();
                assertNotNull(products);
                assertEquals(1, products.size());

                CompleteProduct product = products.get(0);
                assertEmpty(product.getRegionPriceDescription());
                assertEquals(1247, product.getSalesPriceRangeCents().getMin());
                assertEquals(1247, product.getSalesPriceRangeCents().getMax());
                assertEquals(4.77400016784668, product.getRating());
                assertEquals(0, product.getNumReviews());
                assertEquals(1097, product.getPriceRangeCents().getMin());
                assertEquals(1097, product.getPriceRangeCents().getMax());
                assertEquals(8, product.getCategoryIds().size());
                assertEquals("Grocery", product.getCategoryIds().get(0));
                assertEquals(1247, product.getSalesPriceCents());
                assertEquals("17177141", product.getRegionalProductSku());
                assertEquals("http://labs.richrelevance.com/storre/media/catalog/product/p/a/pampers-sensitive-baby-wipes-448ct-993f0e036eb313f85235fb68dc8148dd.jpg", product.getImageUrl());
                assertEquals("Pampers Sensitive Baby Wipes, 448ct", product.getName());
                assertEquals("Baby", product.getGenre());
                assertTrue(product.isRecommendable());
                assertEquals(1097, product.getPriceCents());
                assertEquals("17177141", product.getId());
                assertEquals("Pampers", product.getBrand());
                assertEquals(8, product.getCategories().size());

                Category category = product.getCategories().get(0);
                assertEquals("Grocery", category.getName());
                assertEquals("Grocery", category.getId());
                assertFalse(category.hasChildren());
            }
        };

        accessor.parseResponse(response, callback);
        callback.assertSuccess(this);
    }

    public void testProductsApiError() {
        ProductBuilder builder = new ProductBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ResponseBuilder responseBuilder = new ResponseBuilder().setResponseCode(200).setContentAssetPath("productsError.json");
        MockWebResponse response = new MockWebResponse(responseBuilder, getContext());

        TestResultCallback<?> callback = new TestResultCallback<ProductResponseInfo>() {
            @Override
            protected void testResponse(ProductResponseInfo response, com.richrelevance.Error error) {
                assertNotNull(error);
                assertEquals(Error.ErrorType.ApiError, error.getType());
                assertEquals("Something went horribly wrong!", error.getMessage());
            }
        };

        accessor.parseResponse(response, callback);
        callback.assertSuccess(this);
    }

    public void testProductsBadResponse() {
        ProductBuilder builder = new ProductBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ResponseBuilder responseBuilder = new ResponseBuilder().setResponseCode(200);
        MockWebResponse response = new MockWebResponse(responseBuilder, getContext());

        TestResultCallback<?> callback = new TestResultCallback<ProductResponseInfo>() {
            @Override
            protected void testResponse(ProductResponseInfo response, Error error) {
                assertNotNull(error);
                assertEquals(Error.ErrorType.CannotParseResponse, error.getType());
            }
        };

        accessor.parseResponse(response, callback);
        callback.assertSuccess(this);
    }
}
