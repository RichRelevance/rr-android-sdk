package com.richrelevance.internal.net;

import android.text.TextUtils;

import com.richrelevance.RRLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Abstract class which does some of the generic work for a response.
 */
abstract class BaseResponse implements WebResponse {

    @Override
    public String getContentAsString() {
        InputStream content = null;
        try {
            content = getContentStream();
            if (content != null) {
                return IOUtils.readStream(content);
            }
        } catch (IOException e) {
            RRLog.w(getClass().getName(), "IOException in getContentAsString: " + e.getMessage());
        } finally {
            IOUtils.safeClose(content);
        }
        return null;
    }

    @Override
    public JSONArray getContentAsJSONArray() {
        String content = getContentAsString();
        if (!TextUtils.isEmpty(content)) {
            try {
                return new JSONArray(content);
            } catch (JSONException e) {
                RRLog.w(getClass().getName(), "JSONException in getContentAsJSONArray: " + e.getMessage());
            }
        }

        return null;
    }

    @Override
    public JSONObject getContentAsJSON() {
        String content = getContentAsString();
        if (!TextUtils.isEmpty(content)) {
            try {
                return new JSONObject(content);
            } catch (JSONException e) {
                RRLog.w(getClass().getName(), "JSONException in getContentAsJSON: " + e.getMessage());
            }
        }

        return null;
    }
}
