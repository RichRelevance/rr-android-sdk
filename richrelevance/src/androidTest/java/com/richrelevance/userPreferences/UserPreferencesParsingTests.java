package com.richrelevance.userPreferences;


import android.test.AndroidTestCase;

import com.richrelevance.RequestBuilderAccessor;
import com.richrelevance.internal.TestResultCallback;
import com.richrelevance.mocking.MockWebResponse;
import com.richrelevance.mocking.ResponseBuilder;
import com.richrelevance.userPreference.Preference;
import com.richrelevance.userPreference.FieldType;
import com.richrelevance.userPreference.UserPreferenceBuilder;
import com.richrelevance.userPreference.UserPreferenceResponseInfo;

import java.util.LinkedList;

public class UserPreferencesParsingTests extends AndroidTestCase {

    public void testParseUserPreferences() {
        UserPreferenceBuilder builder = new UserPreferenceBuilder(new LinkedList<FieldType>());
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        ResponseBuilder responseBuilder = new ResponseBuilder()
                .setResponseCode(200)
                .setContentAssetPath("userPreferences.json");
        MockWebResponse response = new MockWebResponse(responseBuilder, getContext());

        TestResultCallback<?> callback = new TestResultCallback<UserPreferenceResponseInfo>() {
            @Override
            protected void testResponse(UserPreferenceResponseInfo response, com.richrelevance.Error error) {
                assertNull(error);
                assertNotNull(response);

                assertEquals("RZTestUser", response.getUserId());

                Preference brands = response.getBrands();
                assertNotNull(brands);
                assertEquals(1, brands.getLikes().size());
                assertEquals("apple", brands.getLikes().get(0));
            }
        };

        accessor.parseResponse(response, callback);
        callback.assertSuccess(this);
    }
}
