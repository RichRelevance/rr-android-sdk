package com.richrelevance.find.autocomplete;


import android.util.Log;

import com.richrelevance.ClientConfiguration;
import com.richrelevance.Error;
import com.richrelevance.RequestBuilder;
import com.richrelevance.internal.net.WebRequest;
import com.richrelevance.internal.net.WebResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

public class AutoCompleteBuilder extends RequestBuilder<AutoCompleteResponseInfo> {

    public static class Keys {
        public static final String QUERY = "query";
        public static final String LANGUAGE = "lang";
        public static final String START = "start";
        public static final String ROWS = "rows";
    }

    /**
     * Sets the `query` parameter used to find auto-complete suggestions
     *
     * @param query the query to auto-complete
     * @return This builder for chaining method calls.
     */
    public AutoCompleteBuilder setQuery(String query) {
        if (query != null && !query.isEmpty()) {
            setParameter(Keys.QUERY, query);
        }

        return this;
    }

    /**
     * Sets the language of the desired results
     *
     * @param locale The locale for the desired language of results.
     * @return This builder for chaining method calls.
     */
    public AutoCompleteBuilder setLocale(Locale locale) {
        if (locale != null) {
            setParameter(Keys.LANGUAGE, locale.getLanguage());
        }

        return this;
    }

    /**
     * Sets the start index of the result set.
     *
     * @param startIndex The index of the first desired response item from the auto-complete results.
     * @return This builder for chaining method calls.
     */
    public AutoCompleteBuilder setStartIndex(int startIndex) {
        setParameter(Keys.START, startIndex);

        return this;
    }

    /**
     * Sets the start index of the result set.
     *
     * @param numRows The index of the first desired response item from the auto-complete results.
     * @return This builder for chaining method calls.
     */
    public AutoCompleteBuilder setNumRows(int numRows) {
        setParameter(Keys.ROWS, numRows);

        return this;
    }

    @Override
    protected AutoCompleteResponseInfo createNewResult() {
        return new AutoCompleteResponseInfo();
    }

    @Override
    protected String getEndpointPath(ClientConfiguration configuration) {
        final String format = "rrserver/api/find/v1/autocomplete/%s";
        String string = String.format(format, configuration.getApiKey());
        return string;
    }

    @Override
    protected void populateResponse(WebResponse response, JSONObject json, AutoCompleteResponseInfo autoCompleteResponseInfo) {
        Log.e(AutoCompleteBuilder.class.getSimpleName(), "Unable to popoulated response for AutoComplete request. Please use populate response for JSONArray");
    }

    protected void populateResponse(WebResponse response, JSONArray jsonArray, AutoCompleteResponseInfo autoCompleteResponseInfo) {
        AutoCompleteParser.parseAutoCompleteResponseInfo(jsonArray, autoCompleteResponseInfo);
    }

    @Override
    protected void parseResponse(WebResponse response, WebRequest.ResultCallback<? super AutoCompleteResponseInfo> resultCallback) {
        if (response.getResponseCode() >= 400) {
            resultCallback.onError(new Error(Error.ErrorType.HttpError, response.getResponseMessage()));
        } else {
            JSONArray jsonArr = response.getContentAsJSONArray();
            if (jsonArr == null) {
                resultCallback.onError(new Error(Error.ErrorType.CannotParseResponse, "Error finding JSONArr"));
                return;
            }

            AutoCompleteResponseInfo result = createNewResult();

            if (!result.isStatusOk()) {
                resultCallback.onError(new com.richrelevance.Error(Error.ErrorType.ApiError, "Status was: " + result.getStatus()));
                return;
            }

            populateResponse(response, jsonArr, result);
            resultCallback.onSuccess(result);
        }
    }
}
