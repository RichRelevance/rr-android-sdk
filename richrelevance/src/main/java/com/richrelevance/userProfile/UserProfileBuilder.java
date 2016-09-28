package com.richrelevance.userProfile;

import com.richrelevance.ClientConfiguration;
import com.richrelevance.RequestBuilder;
import com.richrelevance.internal.net.WebResponse;
import com.richrelevance.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A builder which requests user profile information.
 */
public class UserProfileBuilder extends RequestBuilder<UserProfileResponseInfo> {

    private static final String DELIMITER_FIELDS = ",";

    public static class Keys {
        public static final String FIELDS = "fields";
    }

    public UserProfileBuilder() {
        setUseOAuth(true);
    }

    /**
     * Adds to the list of the subset of profile fields to return.
     * @param fields The fields to add.
     * @return This builder for chaining method calls.
     */
    public UserProfileBuilder addFields(UserProfileField... fields) {
        return addFields(Utils.safeAsList(fields));
    }

    /**
     * Adds to the list of the subset of profile fields to return.
     * @param fields The fields to add.
     * @return This builder for chaining method calls.
     */
    public UserProfileBuilder addFields(Collection<UserProfileField> fields) {
        if (fields != null) {
            List<String> stringFields = new ArrayList<>(fields.size());
            for (UserProfileField field : fields) {
                if (field != null) {
                    stringFields.add(field.getKey());
                }
            }
            addListParametersWithDelimiter(DELIMITER_FIELDS, Keys.FIELDS, stringFields);
        }

        return this;
    }

    /**
     * Sets the list of the subset of profile fields to return.
     * @param fields The fields to return.
     * @return This builder for chaining method calls.
     */
    public UserProfileBuilder setFields(UserProfileField... fields) {
        setFields(Utils.safeAsList(fields));
        return this;
    }

    /**
     * Sets the list of the subset of profile fields to return.
     * @param fields The fields to return.
     * @return This builder for chaining method calls.
     */
    public UserProfileBuilder setFields(Collection<UserProfileField> fields) {
        removeParameter(Keys.FIELDS);
        addFields(fields);
        return this;
    }

    @Override
    protected UserProfileResponseInfo createNewResult() {
        return new UserProfileResponseInfo();
    }

    @Override
    protected String getEndpointPath(ClientConfiguration configuration) {
        final String format = "userProfile/api/v1/service/userProfile/%s/%s";
        return String.format(format, configuration.getApiKey(), configuration.getUserId());
    }

    @Override
    protected void populateResponse(WebResponse response, JSONObject json, UserProfileResponseInfo responseInfo) {
        UserProfileParser.parseUserProfileResponseInfo(json, responseInfo);
    }

    @Override
    protected void applyConfigurationParams(ClientConfiguration configuration) {
        // Override to prevent adding configuration parameters - handled in path for this call.
    }
}