package com.richrelevance.mocking;

import android.content.Context;
import android.text.TextUtils;

import com.richrelevance.RRLog;
import com.richrelevance.internal.net.HttpMethod;
import com.richrelevance.internal.net.IOUtilities;
import com.richrelevance.internal.net.WebRequestBuilder;
import com.richrelevance.internal.net.WebResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link WebResponse} which returns the contents of a mocked response.
 */
public class MockWebResponse implements WebResponse {

    private Context context;
    private ResponseBuilder response;

    public MockWebResponse(ResponseBuilder responseBuilder, Context context) {
        this.context = context;
        this.response = responseBuilder;
    }

    @Override
    public boolean containsHeader(String name) {
        return response.getHeaders().containsKey(name);
    }

    @Override
    public String getHeaderValue(String name) {
        return response.getHeaders().get(name);
    }

    @Override
    public int getResponseCode() {
        return response.getResponseCode();
    }

    @Override
    public String getResponseMessage() {
        return response.getResponseMessage();
    }

    @Override
    public String getContentEncoding() {
        return response.getContentEncoding();
    }

    @Override
    public long getContentLength() {
        String assetPath = response.getContentAssetPath();
        if (!TextUtils.isEmpty(assetPath)) {
            try {
                return context.getAssets().openFd(assetPath).getLength();
            } catch (IOException e) {
                // Don't care, just fail
            }
        }

        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public InputStream getContentStream() throws IOException {
        String assetPath = response.getContentAssetPath();
        if (!TextUtils.isEmpty(assetPath)) {
            return context.getAssets().open(assetPath);
        }
        return null;
    }

    @Override
    public String getContentAsString() {
        try {
            InputStream stream = getContentStream();
            if (stream != null) {
                return IOUtilities.readStream(stream);
            }
        } catch (IOException e) {
            // Don't care, just fail
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