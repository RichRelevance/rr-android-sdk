package com.richrelevance.find.search;


import com.richrelevance.BaseTestCase;
import com.richrelevance.RequestBuilderAccessor;
import com.richrelevance.recommendations.Placement;

import java.util.Locale;

public class SearchRequestBuildTests extends BaseTestCase {

    public void testQuery() {
        SearchRequestBuilder.RCSSearchTokenListener rcsSearchTokenListener = new SearchRequestBuilder.RCSSearchTokenListener();

        SearchRequestBuilder builder = new SearchRequestBuilder(rcsSearchTokenListener);
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setQuery("shoe");
        assertEquals("shoe", accessor.getParamValue(SearchRequestBuilder.Keys.QUERY));
    }

    public void testRCSNotSetOnFirstQuery() {
        SearchRequestBuilder.RCSSearchTokenListener rcsSearchTokenListener = new SearchRequestBuilder.RCSSearchTokenListener();

        SearchRequestBuilder builder = new SearchRequestBuilder(rcsSearchTokenListener);
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertNull(accessor.getParamValue(SearchRequestBuilder.Keys.RCS));
    }

    public void testSetLanguage() {
        SearchRequestBuilder.RCSSearchTokenListener rcsSearchTokenListener = new SearchRequestBuilder.RCSSearchTokenListener();

        SearchRequestBuilder builder = new SearchRequestBuilder(rcsSearchTokenListener);
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setLanguage(Locale.CANADA_FRENCH);
        assertEquals(Locale.CANADA_FRENCH.getLanguage(), accessor.getParamValue(SearchRequestBuilder.Keys.LANGUAGE));
        builder.setLanguage(Locale.ENGLISH);
        assertEquals(Locale.ENGLISH.getLanguage(), accessor.getParamValue(SearchRequestBuilder.Keys.LANGUAGE));
    }

    public void testSetStart() {
        SearchRequestBuilder.RCSSearchTokenListener rcsSearchTokenListener = new SearchRequestBuilder.RCSSearchTokenListener();

        SearchRequestBuilder builder = new SearchRequestBuilder(rcsSearchTokenListener);
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setStart(999);
        assertEquals("999", accessor.getParamValue(SearchRequestBuilder.Keys.START));
    }

    public void testSetRows() {
        SearchRequestBuilder.RCSSearchTokenListener rcsSearchTokenListener = new SearchRequestBuilder.RCSSearchTokenListener();

        SearchRequestBuilder builder = new SearchRequestBuilder(rcsSearchTokenListener);
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setRows(555);
        assertEquals("555", accessor.getParamValue(SearchRequestBuilder.Keys.ROWS));
    }

    public void testSetPlacement() {
        SearchRequestBuilder.RCSSearchTokenListener rcsSearchTokenListener = new SearchRequestBuilder.RCSSearchTokenListener();

        SearchRequestBuilder builder = new SearchRequestBuilder(rcsSearchTokenListener);
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        Placement placement = new Placement(Placement.PlacementType.SEARCH, "find");

        builder.setPlacement(placement);
        assertEquals(placement.getApiValue(), accessor.getParamValue(SearchRequestBuilder.Keys.PLACEMENT));
    }


    public void testSetSessionId() {
        SearchRequestBuilder.RCSSearchTokenListener rcsSearchTokenListener = new SearchRequestBuilder.RCSSearchTokenListener();

        SearchRequestBuilder builder = new SearchRequestBuilder(rcsSearchTokenListener);
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEquals("config2", accessor.getParamValue(SearchRequestBuilder.Keys.SESSION_ID));
    }

    public void testSetUserId() {
        SearchRequestBuilder.RCSSearchTokenListener rcsSearchTokenListener = new SearchRequestBuilder.RCSSearchTokenListener();

        SearchRequestBuilder builder = new SearchRequestBuilder(rcsSearchTokenListener);
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        assertEquals("config2", accessor.getParamValue(SearchRequestBuilder.Keys.USER_ID));
    }

    public void testSetSort() {
        SearchRequestBuilder.RCSSearchTokenListener rcsSearchTokenListener = new SearchRequestBuilder.RCSSearchTokenListener();

        SearchRequestBuilder builder = new SearchRequestBuilder(rcsSearchTokenListener);
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        SearchResultProduct.Field field = SearchResultProduct.Field.BRAND;
        SearchRequestBuilder.SortOrder sortOrder = SearchRequestBuilder.SortOrder.ASCENDING;

        builder.setSort(field, sortOrder);
        assertEquals(sortOrder.createAPIValueForField(field), accessor.getParamValue(SearchRequestBuilder.Keys.SORT));
    }

    public void testSetChannelId() {
        SearchRequestBuilder.RCSSearchTokenListener rcsSearchTokenListener = new SearchRequestBuilder.RCSSearchTokenListener();

        SearchRequestBuilder builder = new SearchRequestBuilder(rcsSearchTokenListener);
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setChannelId("channelId1345");
        assertEquals("channelId1345", accessor.getParamValue(SearchRequestBuilder.Keys.CHANNEL_ID));
    }

    public void testSetLog() {
        SearchRequestBuilder.RCSSearchTokenListener rcsSearchTokenListener = new SearchRequestBuilder.RCSSearchTokenListener();

        SearchRequestBuilder builder = new SearchRequestBuilder(rcsSearchTokenListener);
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setLog(true);
        assertEquals("true", accessor.getParamValue(SearchRequestBuilder.Keys.LOG));
    }

    public void testSetRegion() {
        SearchRequestBuilder.RCSSearchTokenListener rcsSearchTokenListener = new SearchRequestBuilder.RCSSearchTokenListener();

        SearchRequestBuilder builder = new SearchRequestBuilder(rcsSearchTokenListener);
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setRegion("Northern California");
        assertEquals("Northern California", accessor.getParamValue(SearchRequestBuilder.Keys.REGION));
    }

    public void testSetSSL() {
        SearchRequestBuilder.RCSSearchTokenListener rcsSearchTokenListener = new SearchRequestBuilder.RCSSearchTokenListener();

        SearchRequestBuilder builder = new SearchRequestBuilder(rcsSearchTokenListener);
        RequestBuilderAccessor accessor = new RequestBuilderAccessor(builder);

        builder.setSSL(true);
        assertEquals("true", accessor.getParamValue(SearchRequestBuilder.Keys.SSL));
    }
}
