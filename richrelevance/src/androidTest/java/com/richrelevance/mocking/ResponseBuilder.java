package com.richrelevance.mocking;

import com.richrelevance.internal.net.WebResultInfo;

import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder {

    private Map<String, String> headers = new HashMap<>();

    private int responseCode = WebResultInfo.RESPONSE_CODE_FAILED;
    private String responseMessage;

    private String contentAssetPath;
    private String contentEncoding;

    public ResponseBuilder() {

    }

    public ResponseBuilder addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public ResponseBuilder setResponseCode(int responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public ResponseBuilder setResponseMessage(String message) {
        this.responseMessage = message;
        return this;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public ResponseBuilder setContentAssetPath(String path) {
        this.contentAssetPath = path;
        return this;
    }

    public String getContentAssetPath() {
        return contentAssetPath;
    }

    public ResponseBuilder setContentEncoding(String encoding) {
        this.contentEncoding = encoding;
        return this;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }
}