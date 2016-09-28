package com.richrelevance.internal.net;

import android.util.Pair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Class of helper utilities for HTTP.
 */
class HttpUtils {

    @SuppressWarnings("deprecation")
    static String getQueryString(List<Pair<String, String>> pairs) {
        StringBuilder queryBuilder = new StringBuilder();
        boolean first = true;
        for (Pair<String, String> pair : pairs) {
            // This will throw a NullPointerException if you call URLEncoder.encode(null).
            // Instead caught & thrown with description above.
            String value = pair.second;
            if (value == null) {
                // Can't be more specific without jeopardizing security.
                throw new NullPointerException("Malformed Request. Entry has null value for key: "
                        + pair.first);
            }

            if (!first) {
                queryBuilder.append("&");
            }
            queryBuilder.append(pair.first);
            queryBuilder.append("=");
            try {
                queryBuilder.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                // Fallback
                queryBuilder.append(URLEncoder.encode(value));
            }

            first = false;

        }

        return queryBuilder.toString();
    }
}
