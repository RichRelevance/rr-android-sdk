package com.richrelevance.userPreference;

import com.richrelevance.internal.json.JSONHelper;

import org.json.JSONObject;

class UserPreferenceParser {

    static void parseUserPreferenceResponseInfo(JSONObject json, UserPreferenceResponseInfo responseInfo) {
        if (json == null || responseInfo == null) {
            return;
        }

        responseInfo.setUserId(json.optString("userId"));

        responseInfo.setProducts(parsePreference(json, FieldType.PRODUCT));
        responseInfo.setBrands(parsePreference(json, FieldType.BRAND));
        responseInfo.setCategories(parsePreference(json, FieldType.CATEGORY));
        responseInfo.setStores(parsePreference(json, FieldType.STORE));
    }

    private static Preference parsePreference(JSONObject json, FieldType fieldType) {
        Preference preference = new Preference(fieldType);

        if (json != null) {
            JSONObject preferenceJson = json.optJSONObject(fieldType.getResultKey());
            if (preferenceJson != null) {
                preference.setLikes(JSONHelper.parseStrings(preferenceJson, ActionType.LIKE.getKey()));
                preference.setDislikes(JSONHelper.parseStrings(preferenceJson, ActionType.DISLIKE.getKey()));
                preference.setNeutrals(JSONHelper.parseStrings(preferenceJson, ActionType.NEUTRAL.getKey()));
                preference.setNotForRecommendations(JSONHelper.parseStrings(preferenceJson, ActionType.NOT_FOR_RECS.getKey()));
            }
        }

        return preference;
    }
}
