package com.richrelevance.userPreferences;

import com.richrelevance.BaseTestCase;
import com.richrelevance.ClientConfiguration;
import com.richrelevance.RequestBuilder;
import com.richrelevance.RequestBuilderAccessor;
import com.richrelevance.RichRelevanceClient;
import com.richrelevance.TestClient;
import com.richrelevance.userPreference.ActionType;
import com.richrelevance.userPreference.FieldType;
import com.richrelevance.userPreference.UserPreferenceBuilder;

public class UserPreferencesBuilderTests extends BaseTestCase {

    private RichRelevanceClient client;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        ClientConfiguration config = new ClientConfiguration("apiKey", "apiClientKey");
        config.setUserId("RZTestUser");
        config.setSessionId("session");

        client = new TestClient(config);
    }

    public void testActionTypes() {
        assertEquals("dislike", ActionType.DISLIKE.getKey());
        assertEquals("like", ActionType.LIKE.getKey());
        assertEquals("neutral", ActionType.NEUTRAL.getKey());
        assertEquals("notForRecs", ActionType.NOT_FOR_RECS.getKey());
    }

    public void testTargetTypes() {
        assertEquals("brand", FieldType.BRAND.getRequestKey());
        assertEquals("pref_brand", FieldType.BRAND.getResultKey());
        assertEquals("category", FieldType.CATEGORY.getRequestKey());
        assertEquals("pref_category", FieldType.CATEGORY.getResultKey());
        assertEquals("product", FieldType.PRODUCT.getRequestKey());
        assertEquals("pref_product", FieldType.PRODUCT.getResultKey());
        assertEquals("store", FieldType.STORE.getRequestKey());
        assertEquals("pref_store", FieldType.STORE.getResultKey());
    }

    public void testPath() {
        RequestBuilder<?> getBuilder = new UserPreferenceBuilder(FieldType.BRAND);
        getBuilder.setClient(client);
        RequestBuilderAccessor getAccessor = new RequestBuilderAccessor(getBuilder);
        assertTrue(getAccessor.getUrl().startsWith("https://recs.richrelevance.com/rrserver/api/user/preference/RZTestUser"));

        RequestBuilder<?> setBuilder = new UserPreferenceBuilder(FieldType.BRAND, ActionType.DISLIKE, "item");
        setBuilder.setClient(client);
        RequestBuilderAccessor setAccessor = new RequestBuilderAccessor(setBuilder);
        assertTrue(setAccessor.getUrl().startsWith("https://recs.richrelevance.com/rrserver/api/user/preference"));
    }

    public void testConstruction() {
        RequestBuilder<?> builder = new UserPreferenceBuilder(FieldType.BRAND, ActionType.LIKE, "");
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);
        assertEquals("brand", accessor.getParamValue(UserPreferenceBuilder.Keys.TARGET_TYPE));
        assertEquals("like", accessor.getParamValue(UserPreferenceBuilder.Keys.ACTION_TYPE));
    }

    public void testSetViewGuid() {
        RequestBuilder<?> builder = new UserPreferenceBuilder(FieldType.BRAND)
                .setViewGuid("ab");
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEquals("ab", accessor.getParamValue(UserPreferenceBuilder.Keys.VIEW_GUID));
    }

    public void testSetPreferences() {
        RequestBuilder<?> builder = new UserPreferenceBuilder(FieldType.BRAND)
                .setPreferences("a", "b");
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);
        assertEquals("a|b", accessor.getParamValue(UserPreferenceBuilder.Keys.PREFERENCES));
    }
}