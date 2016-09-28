package com.richrelevance.internal.net;

import com.richrelevance.Error;

/**
 * Interface which provides information about the result of a web request.
 *
 * @param <Result> The type of the result which will be returned.
 */
public interface WebResultInfo<Result> {

    /**
     * Response code which represents a failure.
     */
    public static final int RESPONSE_CODE_FAILED = -1;

    /**
     * @return The time that the response was received in milliseconds.
     */
    public long getResponseTimestamp();

    /**
     * @return The result of the request, or null if it failed.
     */
    public Result getResult();

    /**
     * @return Any resulting error from the request.
     */
    public Error getError();

    /**
     * @return The response code from the request, or -1 if it failed.
     */
    public int getResponseCode();

    /**
     * @return The response message from the request, or null if it failed.
     */
    public String getResponseMessage();
}
