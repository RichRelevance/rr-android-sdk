package com.richrelevance;

import android.text.TextUtils;

import junit.framework.TestCase;

public class BaseTestCase extends TestCase {

    protected void assertEmpty(String str) {
        assertTrue(TextUtils.isEmpty(str));
    }

    protected void assertNonEmpty(String str) {
        assertTrue(!TextUtils.isEmpty(str));
    }

    protected void assertJoined(String source, String... values) {
        String joined = StringUtils.join(RequestBuilder.LIST_DELIMITER, values);
        assertEquals(joined, source);
    }
}
