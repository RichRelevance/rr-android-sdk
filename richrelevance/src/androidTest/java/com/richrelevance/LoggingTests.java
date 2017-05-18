package com.richrelevance;

import junit.framework.TestCase;

public class LoggingTests extends TestCase {

    public void testLoggingSettings() {
        RichRelevance.setLoggingLevel(RRLog.NONE);
        assertFalse(RRLog.isLoggable("", RRLog.ERROR));
        assertFalse(RRLog.isLoggable("", RRLog.WARNING));
        assertFalse(RRLog.isLoggable("", RRLog.INFO));
        assertFalse(RRLog.isLoggable("", RRLog.DEBUG));
        assertFalse(RRLog.isLoggable("", RRLog.VERBOSE));

        RichRelevance.setLoggingLevel(RRLog.ERROR);
        assertTrue(RRLog.isLoggable("", RRLog.ERROR));
        assertFalse(RRLog.isLoggable("", RRLog.WARNING));
        assertFalse(RRLog.isLoggable("", RRLog.INFO));
        assertFalse(RRLog.isLoggable("", RRLog.DEBUG));
        assertFalse(RRLog.isLoggable("", RRLog.VERBOSE));

        RichRelevance.setLoggingLevel(RRLog.WARNING);
        assertTrue(RRLog.isLoggable("", RRLog.ERROR));
        assertTrue(RRLog.isLoggable("", RRLog.WARNING));
        assertFalse(RRLog.isLoggable("", RRLog.INFO));
        assertFalse(RRLog.isLoggable("", RRLog.DEBUG));
        assertFalse(RRLog.isLoggable("", RRLog.VERBOSE));

        RichRelevance.setLoggingLevel(RRLog.INFO);
        assertTrue(RRLog.isLoggable("", RRLog.ERROR));
        assertTrue(RRLog.isLoggable("", RRLog.WARNING));
        assertTrue(RRLog.isLoggable("", RRLog.INFO));
        assertFalse(RRLog.isLoggable("", RRLog.DEBUG));
        assertFalse(RRLog.isLoggable("", RRLog.VERBOSE));

        RichRelevance.setLoggingLevel(RRLog.DEBUG);
        assertTrue(RRLog.isLoggable("", RRLog.ERROR));
        assertTrue(RRLog.isLoggable("", RRLog.WARNING));
        assertTrue(RRLog.isLoggable("", RRLog.INFO));
        assertTrue(RRLog.isLoggable("", RRLog.DEBUG));
        assertFalse(RRLog.isLoggable("", RRLog.VERBOSE));

        RichRelevance.setLoggingLevel(RRLog.VERBOSE);
        assertTrue(RRLog.isLoggable("", RRLog.ERROR));
        assertTrue(RRLog.isLoggable("", RRLog.WARNING));
        assertTrue(RRLog.isLoggable("", RRLog.INFO));
        assertTrue(RRLog.isLoggable("", RRLog.DEBUG));
        assertTrue(RRLog.isLoggable("", RRLog.VERBOSE));
    }
}
