package com.richrelevance.find.search;

import android.support.annotation.NonNull;

import com.richrelevance.ClientConfiguration;
import com.richrelevance.RequestBuilder;
import com.richrelevance.internal.net.WebResponse;
import com.richrelevance.recommendations.Placement;
import com.richrelevance.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class SearchRequestBuilder extends RequestBuilder<SearchResponseInfo> {

    public static class Keys {
        public static final String QUERY = "query";
        public static final String LANGUAGE = "lang";
        public static final String START = "start";
        public static final String ROWS = "rows";
        public static final String PLACEMENT = "placement";
        public static final String SESSION_ID = "sessionId";
        public static final String USER_ID = "userId";
        public static final String FILTER = "filter";
        public static final String FACET = "facet";
        public static final String SORT = "sort";
        public static final String CHANNEL_ID = "channelId";
        public static final String LOG = "log";
        public static final String REGION = "region";
        public static final String PREF = "pref";
        public static final String RCS = "rcs";
        public static final String SSL = "ssl";
    }

    public enum SortOrder {
        ASCENDING("ASC"),
        DESCENDING("DESC");

        private String key;

        SortOrder(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public String createAPIValueForField(SearchResultProduct.Field field) {
            return "product_" + field.getRequestKey() + " " + key;
        }

        public String createAPIValueForCustomField(String resuestKey) {
            return "product_" + resuestKey + " " + key;
        }
    }

    public static final String CHANNEL_DEFAULT = "Android";

    public static class RCSSearchTokenListener {
        private String rcsSearchToken;

        void setToken(String rcsSearchToken) {
            this.rcsSearchToken = rcsSearchToken;
        }

        String getRCSToken() {
            return rcsSearchToken;
        }
    }

    private RCSSearchTokenListener rcsSearchTokenListener;

    /**
     * The SearchRequestBuilder must be initialized with a RCSSearchTokenListener to allow token
     * altercation for future search requests
     *
     * @param rcsSearchTokenListener The listener used to persist rcs tokens from search results
     *                               from one request to the next
     */
    public SearchRequestBuilder(@NonNull RCSSearchTokenListener rcsSearchTokenListener) {
        this.rcsSearchTokenListener = rcsSearchTokenListener;
        setRCS(rcsSearchTokenListener.getRCSToken());
        setSSL(true);
    }

    /**
     * Sets the query string to result and return results for that search term
     *
     * @param query the query to search
     * @return This builder for chaining method calls.
     */
    public SearchRequestBuilder setQuery(String query) {
        setParameter(Keys.QUERY, query);
        return this;
    }

    /**
     * Sets the language of the search results of the provided Locale
     *
     * @param locale the Android Locale which provides an accepted language
     * @return This builder for chaining method calls.
     */
    public SearchRequestBuilder setLanguage(Locale locale) {
        setParameter(Keys.LANGUAGE, locale.getLanguage());
        return this;
    }

    /**
     * Sets the starting position of the search results.  It's a zero indexed system. This can be
     * used for pagination along with the Rows attribute
     *
     * @param start the
     * @return This builder for chaining method calls.
     */
    public SearchRequestBuilder setStart(int start) {
        setParameter(Keys.START, start);
        return this;
    }

    /**
     * Sets the rows which describe how many results to return back. If Rows exceeds the number of results,
     * the maximum number of results, which is less than Rows will be returned. This can be used for pagination
     * along with the Start attribute.
     *
     * @param rows the number of
     * @return This builder for chaining method calls.
     */
    public SearchRequestBuilder setRows(int rows) {
        setParameter(Keys.ROWS, rows);
        return this;
    }

    /**
     * Sets the placement identifier. The identifier consists of a page type and a placement name.
     *
     * @param placement The placement to use.
     * @return This builder for chaining method calls.
     */
    public SearchRequestBuilder setPlacement(Placement placement) {
        setListParameter(Keys.PLACEMENT, placement.getApiValue());
        return this;
    }

    /**
     * Sets the SessionId which identifies a single visit by a shopper. Sessions are used by
     * behavioral models (to scope user behaviour in a shopping session) and reporting metrics.
     *
     * @param sessionId the uniques session identifier
     * @return This builder for chaining method calls.
     */
    public SearchRequestBuilder setSessionId(String sessionId) {
        setParameter(Keys.SESSION_ID, sessionId);
        return this;
    }

    /**
     * Sets the Rich Relevance userId for Search result personalization and User Profile personalization.
     * A unique string to identify each shopper (user). All shopper behavior is stored using this key.
     * It is case-sensitive, and should be the same user ID sent to RichRelevance in other applications.
     * If no userId is given, recommendations are based on view and purchase history as well as
     * unpersonalized strategies such as CategoryBestSellers.
     *
     * @param userId the Rich Relevance UserId for the User Profile.
     * @return This builder for chaining method calls.
     */
    public SearchRequestBuilder setUserId(String userId) {
        setParameter(Keys.USER_ID, userId);
        return this;
    }

    /**
     * Set the filters which to apply to the search term. You can apply multiple filters.
     *
     * @param filters The filters to apply.
     * @return This builder for chaining method calls.
     */
    public SearchRequestBuilder setFilters(Filter... filters) {
        setListParameter(Keys.FILTER, getFiltersStrings(Utils.safeAsList(filters)));
        return this;
    }

    /**
     * Set the filters which to apply to the search term. You can apply multiple filters.
     *
     * @param filters The filters to apply.
     * @return This builder for chaining method calls.
     */
    public SearchRequestBuilder setFilters(Collection<Filter> filters) {
        setListParameter(Keys.FILTER, getFiltersStrings(filters));
        return this;
    }

    /**
     * Specify how you want to sort the products.  The default is a personalized sort.
     * You can instead sort by any 'Field' that you specified in your catalog.
     *
     * @param sortedByField the field by which to apply the sort
     * @param sortOrder either ASCENDING or DESCENDING
     * @return This builder for chaining method calls.
     */
    public SearchRequestBuilder setSort(SearchResultProduct.Field sortedByField, SortOrder sortOrder) {
        setParameter(Keys.SORT, sortOrder.createAPIValueForField(sortedByField));
        return this;
    }

    /**
     * Specify with a custom field request key how you want to sort the products.
     * The default is a personalized sort. You can instead sort by any 'Field' that you specified in your catalog.
     *
     * @param customFieldRequestKey the custom requestKey by which to apply the sort
     * @param sortOrder either ASCENDING or DESCENDING
     * @return This builder for chaining method calls.
     */
    public SearchRequestBuilder setSort(String customFieldRequestKey, SortOrder sortOrder) {
        setParameter(Keys.SORT, sortOrder.createAPIValueForCustomField(customFieldRequestKey));
        return this;
    }

    /**
     * Sets the ChannelId of this Search request. The default channel for this SDK is 'Android'
     * A channel is a description of the caller of this API.  It's mostly used to describe whether
     * this API is being used in an Android app or a Call center app.
     *
     * @param channelId the unique if of this request channel
     * @return This builder for chaining method calls.
     */
    public SearchRequestBuilder setChannelId(String channelId) {
        setParameter(Keys.CHANNEL_ID, channelId);
        return this;
    }

    /**
     * Sets the Log parameter which enables Search request event logging for provided profiling personalization.
     * If set to true this will log the search request event.  The default behavior is for this to be true.
     * Only set it to false if you are testing the API.
     *
     * @param log True to log this search request event. False to not log this search request event.
     * @return This builder for chaining method calls.
     */
    public SearchRequestBuilder setLog(boolean log) {
        setParameter(Keys.LOG, log);
        return this;
    }

    /**
     * Set Region identifier for the Search request
     *
     * @param region the identifier of the region of this Search request
     * @return This builder for chaining method calls.
     */
    public SearchRequestBuilder setRegion(String region) {
        setParameter(Keys.REGION, region);
        return this;
    }

    /**
     * Sets the first party cookie string. This is a encrypted value of Rich Relevance cookies.
     * It should be passed as it was received in the earlier api response.
     *
     * @param rcs the rcsSearchToken returned from the previous Search response if any
     * @return This builder for chaining method calls.
     */
    public SearchRequestBuilder setRCS(String rcs) {
        if(rcs != null) {
            setParameter(Keys.RCS, rcs);
        }
        return this;
    }

    /**
     * Sets on or off https protocol. This is only exposed to search/find classes of the SDK,
     * and cannot be set by the SDK client.
     *
     * @param ssl If set to true than clickUrl and searchTrackingUrl are returned with https protocol.
     * @return This builder for chaining method calls.
     */
    protected SearchRequestBuilder setSSL(boolean ssl) {
        setParameter(Keys.SSL, ssl);
        return this;
    }

    @Override
    protected SearchResponseInfo createNewResult() {
        return new SearchResponseInfo();
    }

    @Override
    protected String getEndpointPath(ClientConfiguration configuration) {
        return String.format("rrserver/api/find/v1/%s", configuration.getApiKey());
    }

    @Override
    protected void populateResponse(WebResponse response, JSONObject json, SearchResponseInfo searchResponseInfo) {
        String rcsSearchToken = json.optString(Keys.RCS);
        if(rcsSearchToken != null) {
            rcsSearchTokenListener.setToken(rcsSearchToken);
        }

        SearchResultProductParser.parseSearchResponseInfo(json, searchResponseInfo);
    }

    public static Collection<String> getFiltersStrings(Collection<Filter> filters) {
        if(filters != null) {
            List<String> stringFacets = new ArrayList<>(filters.size());

            for(Filter filter : filters) {
                if(filter != null) {
                    stringFacets.add(filter.getApiValue());
                }
            }

            return stringFacets;
        }

        return null;
    }
}
