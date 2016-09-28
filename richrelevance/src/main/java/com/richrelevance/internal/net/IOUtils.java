package com.richrelevance.internal.net;


import com.richrelevance.RRLog;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class of helper utilities for dealing with IO
 */
class IOUtils {

    /**
     * Utility method for pulling plain text from an InputStream object
     *
     * @param in InputStream object retrieved from an HttpResponse
     * @return String contents of stream
     */
    static String readStream(InputStream in) {
        StringBuilder sb = new StringBuilder();

        byte[] buffer = new byte[8192];
        int count;
        try {
            while ((count = in.read(buffer)) > 0) {
                sb.append(new String(buffer, 0, count));
            }
        } catch (Exception ex) {
            RRLog.w(IOUtils.class.getSimpleName(), "Error reading stream", ex);
        } finally {
            safeClose(in);
        }

        return sb.toString();
    }

    /**
     * Safely closes the given {@link Closeable} without throwing
     * any exceptions due to null pointers or {@link IOException}s.
     *
     * @param closeable The {@link Closeable} to close.
     */
    static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

}
