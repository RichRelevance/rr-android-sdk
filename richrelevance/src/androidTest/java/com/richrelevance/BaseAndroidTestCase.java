package com.richrelevance;

import android.test.AndroidTestCase;
import android.text.TextUtils;

public class BaseAndroidTestCase extends AndroidTestCase {

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
