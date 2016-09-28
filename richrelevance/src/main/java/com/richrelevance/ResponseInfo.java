package com.richrelevance;

import android.text.TextUtils;

import com.richrelevance.utils.ParsingUtils;

import org.json.JSONObject;

/**
 * Class which represents a set of response information for a particular request.
 */
public abstract class ResponseInfo {
    private String status;
    private String errorMessage;
    private JSONObject rawJson;

    /**
     * @return True if the status appears to be successful.
     */
    public boolean isStatusOk() {
        return ParsingUtils.isStatusOk(getStatus());
    }

    /**
     * @return The status returned by the API.
     */
    public String getStatus() {
        return status;
    }

    void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return True if there is an error message.
     */
    public boolean hasErrorMessage() {
        return !TextUtils.isEmpty(errorMessage);
    }

    /**
     * @return An error message returned by the API.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return The raw JSON returned as a response.
     */
    public JSONObject getRawJson() {
        return rawJson;
    }

    public void setRawJson(JSONObject rawJson) {
        this.rawJson = rawJson;
    }
}
