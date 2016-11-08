package com.richrelevance.internal.net;

import android.util.Pair;

import com.richrelevance.internal.net.oauth.OAuthConfig;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Builder class which allows for the construction of a request. This class abstracts the
 * implementation and allows definition of all the properties of HTTP requests.
 */
public class WebRequestBuilder {

    private String url;
    private HttpMethod method;
    private List<Pair<String, String>> params;
    private LinkedHashMap<String, String> headers;
    private List<String> appended;

    private OAuthConfig oAuthConfig;

    /**
     * Constructs a {@link WebRequestBuilder} using the given {@link HttpMethod}
     * and pointing to the given url.
     *
     * @param method The {@link HttpMethod} to use for the request.
     * @param url    The url the request targets.
     */
    public WebRequestBuilder(HttpMethod method, String url) {
        setUrl(url);
        this.method = method;
        this.params = new LinkedList<>();
        this.headers = new LinkedHashMap<>();
        this.appended = new ArrayList<>();
    }

    /**
     * Constructs a {@link WebRequestBuilder} using the given {@link HttpMethod}
     * and pointing to the given {@link URI}.
     *
     * @param method The {@link HttpMethod} to use for the request.
     * @param url    The url the request targets.
     */
    public WebRequestBuilder(HttpMethod method, URI url) {
        this(method, url.toString());
    }


    /**
     * Sets the target {@link URI} for this {@link WebRequestBuilder}.
     *
     * @param url the url the request targets.
     * @return This {@link WebRequestBuilder} object to allow for chaining of calls.
     */
    public WebRequestBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * Adds the given parameter. If this key is already included, it will be added a second time - this is not standard
     * behavior - see {@link #setParam(String, String)}.
     *
     * @param key   The parameter key.
     * @param value The parameter value.
     * @return This {@link WebRequestBuilder} object to allow for chaining of calls.
     */
    public WebRequestBuilder addParam(String key, String value) {
        params.add(new Pair<>(key, value));
        return this;
    }

    /**
     * Sets a parameter in this request, overriding any previous value.
     *
     * @param key   The parameter key.
     * @param value The parameter value.
     * @return This {@link WebRequestBuilder} object to allow for chaining of calls.
     */
    public WebRequestBuilder setParam(String key, String value) {
        removeParam(key);
        addParam(key, value);
        return this;
    }

    /**
     * Sets a parameter in this request, overriding any previous value.
     *
     * @param key   The parameter key.
     * @param value The parameter value.
     * @return This {@link WebRequestBuilder} object to allow for chaining of calls.
     */
    public WebRequestBuilder setParam(String key, boolean value) {
        setParam(key, Boolean.toString(value));
        return this;
    }

    /**
     * Sets a parameter in this request, overriding any previous value.
     *
     * @param key   The parameter key.
     * @param value The parameter value.
     * @return This {@link WebRequestBuilder} object to allow for chaining of calls.
     */
    public WebRequestBuilder setParam(String key, int value) {
        setParam(key, Integer.toString(value));
        return this;
    }

    /**
     * Sets a parameter in this request, overriding any previous value.
     *
     * @param key   The parameter key.
     * @param value The parameter value.
     * @return This {@link WebRequestBuilder} object to allow for chaining of calls.
     */
    public WebRequestBuilder setParam(String key, long value) {
        setParam(key, Long.toString(value));
        return this;
    }

    /**
     * Adds a parameter to the request if the value is not null.  Note that this method
     * will still add an empty string value to the request.
     *
     * @param key   The parameter key.
     * @param value The parameter value.
     * @return This {@link WebRequestBuilder} object to allow for chaining of calls.
     */
    public WebRequestBuilder addParamIfNotNull(String key, String value) {
        if (value != null) {
            setParam(key, value);
        }
        return this;
    }

    /**
     * Adds a {@link Map} of parameter key value pairs as parameters of this
     * request. Parameters are added in iteration order.
     *
     * @param params The {@link Map} of parameters.
     * @return This {@link WebRequestBuilder} object to allow for chaining of calls.
     */
    public WebRequestBuilder addParams(Map<String, String> params) {
        for (Entry<String, String> entry : params.entrySet()) {
            addParam(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public WebRequestBuilder addAppend(String append) {
        if(append != null) {
            appended.add(append);
        }
        return this;
    }

    public boolean containsParam(String key) {
        for (Pair<String, String> param : params) {
            if (key.equals(param.first)) {
                return true;
            }
        }

        return false;
    }

    public List<Pair<String, String>> getParams() {
        return Collections.unmodifiableList(params);
    }

    /**
     * Gets the first value for the given key.
     *
     * @param key The key to get the value of.
     * @return The current value of the key.
     */
    public String getParam(String key) {
        if (key != null) {
            for (Pair<String, String> param : params) {
                if (key.equals(param.first)) {
                    return param.second;
                }
            }
        }

        return null;
    }

    /**
     * Gets all the values for the given key.
     * @param key The key to get the values of.
     * @return The current values of the key.
     */
    public List<String> getAllParamValues(String key) {
        if (key != null) {
            List<String> values = new LinkedList<>();
            for (Pair<String, String> param : params) {
                if (key.equals(param.first)) {
                    values.add(param.second);
                }
            }

            return values;
        }
        return null;
    }

    /**
     * Removes a parameter from the request.
     *
     * @param key The parameter key.
     * @return This {@link WebRequestBuilder} object to allow for chaining of calls.
     */
    public WebRequestBuilder removeParam(String key) {
        if (key != null) {
            List<Pair<String, String>> toRemove = new LinkedList<>();
            for (Pair<String, String> pair : params) {
                if (key.equals(pair.first)) {
                    toRemove.add(pair);
                }
            }

            params.removeAll(toRemove);
        }
        return this;
    }


    /**
     * Adds a header to this request with the given name and value.
     *
     * @param name  The name of the header.
     * @param value The value for the header.
     * @return This {@link WebRequestBuilder} object to allow for chaining of calls.
     */
    public WebRequestBuilder addHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    /**
     * Adds a {@link Map} of key value pairs as headers to this request.
     * Headers are added in iteration order.
     *
     * @param headers The {@link Map} of header key value pairs to add.
     * @return This {@link WebRequestBuilder} object to allow for chaining of calls.
     */
    public WebRequestBuilder addHeaders(Map<String, String> headers) {
        putEntries(headers, this.headers);
        return this;
    }

    public void setOAuthConfig(OAuthConfig config) {
        this.oAuthConfig = config;
    }

    public OAuthConfig getOAuthConfig() {
        return oAuthConfig;
    }

    private void putEntries(Map<String, String> entries, Map<String, String> map) {
        for (Entry<String, String> entry : entries.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
    }


    /**
     * Gets the URL that this {@link WebRequestBuilder} points to.
     *
     * @return The URL the {@link WebRequestBuilder} is pointing to.
     */
    public String getFullUrl() {
        String fullUrl = this.url;

        // If we should set params in the url and we have params to set, do so
        if (params.size() > 0) {
            String queryString = "?" + HttpUtils.getQueryString(params);
            fullUrl = String.format("%s%s", this.url, queryString);
        }

        if (appended.size() > 0) {
            for(String append : appended) {
                fullUrl += append;
            }
        }

        return fullUrl;
    }

    /**
     * @return The headers to be used on this request.
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @return The http method to be used for this request.
     */
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public int hashCode() {
        int result = 0;

        result = mergeHashes(result, url.hashCode(), method.hashCode());
        return result;
    }

    private int mergeHashes(int current, int... addends) {
        for (int addend : addends) {
            current = 37 * current + addend;
        }

        return current;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof WebRequestBuilder) {
            WebRequestBuilder other = (WebRequestBuilder) o;

            return url.equals(other.url) &&
                    method.equals(other.method) &&
                    params.equals(other.params) &&
                    headers.equals(other.headers);
        }

        return super.equals(o);
    }
}
