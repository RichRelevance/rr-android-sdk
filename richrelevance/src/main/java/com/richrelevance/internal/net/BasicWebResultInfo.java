package com.richrelevance.internal.net;

import com.richrelevance.Error;

import java.io.IOException;

/**
 * Class which implements {@link WebResultInfo} with basic functionality.
 *
 * @param <Result> The type of the result which will be returned
 */
class BasicWebResultInfo<Result> implements WebResultInfo<Result> {
    private Result result;
    private Error error;

    private long timestamp;
    private int responseCode = RESPONSE_CODE_FAILED;
    private String responseMessage;

    /**
     * Creates a {@link BasicWebResultInfo} by wrapping the given result.
     *
     * @param result           The result of the request.
     * @param error            Any resulting error from the request.
     * @param requestTimestamp The time the request was completed.
     * @param responseCode     The response code of the request.
     * @param responseMessage  The response message of the request.
     * @throws IOException If there was an exception with the connection.
     */
    public BasicWebResultInfo(Result result, Error error, long requestTimestamp, int responseCode, String responseMessage) throws IOException {
        this.result = result;
        this.error = error;
        this.timestamp = requestTimestamp;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public Error getError() {
        return error;
    }

    @Override
    public long getResponseTimestamp() {
        return timestamp;
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public String getResponseMessage() {
        return responseMessage;
    }
}
