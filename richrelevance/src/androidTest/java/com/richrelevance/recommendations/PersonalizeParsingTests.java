package com.richrelevance.recommendations;

import com.richrelevance.*;
import com.richrelevance.Error;
import com.richrelevance.internal.TestResultCallback;
import com.richrelevance.mocking.MockWebResponse;
import com.richrelevance.mocking.ResponseBuilder;

public class PersonalizeParsingTests extends BaseAndroidTestCase {

    public void testParsePersonalize() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ResponseBuilder responseBuilder = new ResponseBuilder().setResponseCode(200).setContentAssetPath("personalizeForPlacements.json");
        MockWebResponse response = new MockWebResponse(responseBuilder, getContext());

        TestResultCallback<?> callback = new TestResultCallback<PlacementPersonalizeResponseInfo>() {
            @Override
            protected void testResponse(PlacementPersonalizeResponseInfo response, com.richrelevance.Error error) {
                assertNull(error);
                assertNotNull(response);
                assertEquals("OK", response.getStatus());
                assertEquals(5, response.getRequestMap().size());
                assertEquals("null", response.getRequestMap().get("sessionId"));
                assertEquals(1, response.getPlacements().size());

                PlacementPersonalizeResponse placement = response.getPlacements().get(0);
                assertEquals("", placement.getHtml());
                assertEquals(Placement.PlacementType.HOME, placement.getPlacement().getPageType());
                assertEquals("omnichannel", placement.getPlacement().getName());
                assertEquals(1, placement.getCreatives().size());

                Creative creative = placement.getCreatives().get(0);
                assertEquals("http://qa.richrelevance.com/rrserver/emailTracking?a=showcaseparent&vg=12b3ecd4-aeb3-4630-003d-3c53be534219&cpi=NONE&pgt=9&pa=omnichannel&pt=home_page.omnichannel&pcam=766609&pcre=12365", creative.getTrackingUrl());
                assertEquals("mobile: electronics", creative.getCampaign());
                assertEquals(2, creative.getCreativeMap().size());
                assertEquals("Electronics", creative.getCreativeMap().get("ALERT_TEXT"));
            }
        };

        accessor.parseResponse(response, callback);
        callback.assertSuccess(this);
    }

    public void testParsePersonalizeApiError() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ResponseBuilder responseBuilder = new ResponseBuilder().setResponseCode(200).setContentAssetPath("personalizeForPlacementsError.json");
        MockWebResponse response = new MockWebResponse(responseBuilder, getContext());

        TestResultCallback<?> callback = new TestResultCallback<PlacementPersonalizeResponseInfo>() {
            @Override
            protected void testResponse(PlacementPersonalizeResponseInfo response, com.richrelevance.Error error) {
                assertNotNull(error);
                assertEquals(Error.ErrorType.ApiError, error.getType());
                assertEquals("Something went horribly wrong!", error.getMessage());
            }
        };

        accessor.parseResponse(response, callback);
        callback.assertSuccess(this);
    }

    public void testParseRecsForPlacementsBadResponse() {
        PlacementsPersonalizeBuilder builder = new PlacementsPersonalizeBuilder();
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ResponseBuilder responseBuilder = new ResponseBuilder()
                .setResponseCode(200);
        MockWebResponse response = new MockWebResponse(responseBuilder, getContext());

        TestResultCallback<?> callback = new TestResultCallback<PlacementPersonalizeResponseInfo>() {
            @Override
            protected void testResponse(PlacementPersonalizeResponseInfo response, Error error) {
                assertNotNull(error);
                assertEquals(Error.ErrorType.CannotParseResponse, error.getType());
            }
        };

        accessor.parseResponse(response, callback);
        callback.assertSuccess(this);
    }


}
