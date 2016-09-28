package com.richrelevance.internal.net;

import java.io.InputStream;

/**
 * Contains handy IO utilities for testing.
 */
public class IOUtilities {

    /**
     * Exposes the internal read stream method. Reads a stream to a string.
     * @param in The stream to read.
     * @return The contents of the stream.
     */
    public static String readStream(InputStream in) {
        return IOUtils.readStream(in);
    }
}
