package com.richrelevance;

import android.support.annotation.IntDef;

/**
 * Class which mirrors the default Android {@link android.util.Log}, but allows enabling and disabling. All Rich
 * Relevance logging will go through here such that logging may be globally disabled in the SDK.
 */
public class RRLog {

    //@formatter:off
    @IntDef({
            VERBOSE,
            DEBUG,
            INFO,
            WARNING,
            ERROR,
            NONE
    })
    public @interface LogLevel { }

    public static final int VERBOSE = 2; // 0x10
    public static final int DEBUG = 4; // 0x100
    public static final int INFO = 8; // 0x1000
    public static final int WARNING = 16; // 0x10000
    public static final int ERROR = 32; // 0x100000
    public static final int NONE = 63; // 0x111111

    private static @LogLevel int logLevel;
    //@formatter:on

    /**
     * Sets the level of logs which will be logged.
     *
     * @param logLevel The lowest log level to log.
     */
    static void setLogLevel(@LogLevel int logLevel) {
        RRLog.logLevel = logLevel;
    }

    public static void v(String tag, String message) {
        if (isLoggable(tag, VERBOSE)) {
            android.util.Log.v(tag, message);
        }
    }

    public static void v(String tag, String message, Throwable throwable) {
        if (isLoggable(tag, VERBOSE)) {
            android.util.Log.v(tag, message, throwable);
        }
    }

    public static void d(String tag, String message) {
        if (isLoggable(tag, DEBUG)) {
            android.util.Log.d(tag, message);
        }
    }

    public static void d(String tag, String message, Throwable throwable) {
        if (isLoggable(tag, DEBUG)) {
            android.util.Log.d(tag, message, throwable);
        }
    }

    public static void i(String tag, String message) {
        if (isLoggable(tag, INFO)) {
            android.util.Log.i(tag, message);
        }
    }

    public static void i(String tag, String message, Throwable throwable) {
        if (isLoggable(tag, INFO)) {
            android.util.Log.i(tag, message, throwable);
        }
    }

    public static void w(String tag, String message) {
        if (isLoggable(tag, WARNING)) {
            android.util.Log.w(tag, message);
        }
    }

    public static void w(String tag, String message, Throwable throwable) {
        if (isLoggable(tag, WARNING)) {
            android.util.Log.w(tag, message, throwable);
        }
    }

    public static void e(String tag, String message) {
        if (isLoggable(tag, ERROR)) {
            android.util.Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Throwable throwable) {
        if (isLoggable(tag, ERROR)) {
            android.util.Log.e(tag, message, throwable);
        }
    }

    static boolean isLoggable(String tag, @LogLevel int level) {
        return level >= logLevel;
    }
}
