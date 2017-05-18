package com.richrelevance.utils;

import android.text.TextUtils;

import com.richrelevance.internal.json.JSONHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ParsingUtils {
    public static String getStatus(JSONObject json) {
        return json.optString("status");
    }

    public static String getErrorMessage(JSONObject json) {
        return json.optString("errormessage");
    }

    public static boolean isStatusOk(String status) {
        return TextUtils.isEmpty(status) || "ok".equalsIgnoreCase(status);
    }

    public static ValueMap<String> optValueMap(JSONObject json, String key) {
        JSONObject mapJson = json.optJSONObject(key);
        if (mapJson != null) {
            final ValueMap<String> map = new ValueMap<>();
            JSONHelper.runOverKeys(mapJson, new JSONHelper.JSONKeyDelegate() {
                @Override
                public void execute(JSONObject json, String key) {
                    List<String> values = JSONHelper.parseStrings(json, key);
                    map.add(key, values);
                }
            });

            return map;
        }

        return null;
    }
}
