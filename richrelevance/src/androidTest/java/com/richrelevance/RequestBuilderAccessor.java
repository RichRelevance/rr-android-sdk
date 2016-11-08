package com.richrelevance;

import com.richrelevance.internal.net.WebRequest;
import com.richrelevance.internal.net.WebResponse;

import java.util.List;

public class RequestBuilderAccessor {
    private RequestBuilder<?> builder;

    public RequestBuilderAccessor(RequestBuilder<?> builder) {
        this.builder = builder;
    }

    public String getUrl() {
        return builder.getWebRequest().getRequestBuilder().getFullUrl();
    }

    public String getParamValue(String key) {
        return builder.getWebRequest().getRequestBuilder().getParam(key);
    }

    public List<String> getAllParamValues(String key) {
        return builder.getWebRequest().getRequestBuilder().getAllParamValues(key);
    }

    public void parseResponse(WebResponse response, WebRequest.ResultCallback<ResponseInfo> callback) {
        builder.parseResponse(response, callback);
    }
}