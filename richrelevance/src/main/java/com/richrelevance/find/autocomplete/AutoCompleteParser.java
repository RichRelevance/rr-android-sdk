package com.richrelevance.find.autocomplete;

import com.richrelevance.internal.json.JSONArrayParserDelegate;
import com.richrelevance.internal.json.JSONHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AutoCompleteParser {

    static void parseAutoCompleteResponseInfo(JSONArray json, AutoCompleteResponseInfo responseInfo) {
        if (json == null || responseInfo == null) {
            return;
        }

        responseInfo.setSuggestions(JSONHelper.parseJSONArray(json, AutoCompleteResponseParserDelegate));
    }

    static AutoCompleteSuggestion parseAutoCompleteSuggestion(JSONObject json) {
        if (json == null) {
            return null;
        }

        AutoCompleteSuggestion suggestion = null;
        try {
            suggestion = new AutoCompleteSuggestion(
                    json.getString("id"),
                    json.getString("terms"),
                    json.getString("type"),
                    json.getInt("value"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return suggestion;
    }

    private static final JSONArrayParserDelegate<AutoCompleteSuggestion> AutoCompleteResponseParserDelegate =
            new JSONArrayParserDelegate<AutoCompleteSuggestion>() {
                @Override
                public AutoCompleteSuggestion parseObject(JSONObject json) {
                    return parseAutoCompleteSuggestion(json);
                }
            };


}
