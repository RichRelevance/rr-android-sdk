package com.richrelevance.userPreference;

import android.text.TextUtils;

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
 * A builder which gets or sets user preferences.
 */
public class UserPreferenceBuilder extends RequestBuilder<UserPreferenceResponseInfo> {

    public static class Keys {
        public static final String FIELDS = "fields";
        public static final String VIEW_GUID = "vg";
        public static final String PREFERENCES = "p";
        public static final String TARGET_TYPE = "targetType";
        public static final String ACTION_TYPE = "actionType";
    }

    /**
     * Constructs a builder which retrieves user preferences for the given fields.
     * @param fields The fields to retrieve.
     */
    public UserPreferenceBuilder(FieldType... fields) {
        setFields(fields);
    }

    /**
     * Constructs a builder which retrieves user preferences for the given fields.
     * @param fields The fields to retrieve.
     */
    public UserPreferenceBuilder(Collection<FieldType> fields) {
        setFields(fields);
    }

    /**
     * Constructs a builder which sets the given preferences.
     * @param target What kind of value is being passed as a preference.
     * @param action What has the shopper indicated about the brands/categories/products/stores.
     * @param ids A list of brand IDs, category IDs, product IDs, or store IDs to perform the action on.
     */
    public UserPreferenceBuilder(FieldType target, ActionType action, String... ids) {
        setTargetType(target);
        setActionType(action);
        setPreferences(ids);
    }

    /**
     * Constructs a builder which sets the given preferences.
     * @param target What kind of value is being passed as a preference.
     * @param action What has the shopper indicated about the brands/categories/products/stores.
     * @param ids A list of brand IDs, category IDs, product IDs, or store IDs to perform the action on.
     */
    public UserPreferenceBuilder(FieldType target, ActionType action, Collection<String> ids) {
        setTargetType(target);
        setActionType(action);
        setPreferences(ids);
    }

    /**
     * Which preference values should be returned?
     * @param fields The fields to return.
     * @return This builder for chaining method calls.
     */
    public UserPreferenceBuilder setFields(FieldType... fields) {
        setFields(Utils.safeAsList(fields));
        return this;
    }

    /**
     * Which preference values should be returned?
     * @param fields The fields to return.
     * @return This builder for chaining method calls.
     */
    public UserPreferenceBuilder setFields(Collection<FieldType> fields) {
        if (fields != null) {
            List<String> fieldValues = new ArrayList<>(fields.size());
            for (FieldType field : fields) {
                if (field != null) {
                    fieldValues.add(field.getResultKey());
                }
            }

            setListParameter(Keys.FIELDS, fieldValues);
        } else {
            removeParameter(Keys.FIELDS);
        }

        return this;
    }

    /**
     * Sets the view GUID. Unique string to identify a set of recommendations. Returned as part of the RichRelevance
     * response.
     *
     * @param guid The view GUID to set.
     * @return This builder for chaining method calls.
     */
    public UserPreferenceBuilder setViewGuid(String guid) {
        setParameter(Keys.VIEW_GUID, guid);
        return this;
    }

    /**
     * Sets the preferences. A list of brand IDs, category IDs, product IDs, or store IDs (depending on the value
     * of targetType).
     *
     * @param preferences The list of preferences to modify.
     * @return This builder for chaining method calls.
     */
    public UserPreferenceBuilder setPreferences(String... preferences) {
        setListParameter(Keys.PREFERENCES, preferences);
        return this;
    }

    /**
     * Sets the preferences. A list of brand IDs, category IDs, product IDs, or store IDs (depending on the value
     * of targetType).
     *
     * @param preferences The list of preferences to modify.
     * @return This builder for chaining method calls.
     */
    public UserPreferenceBuilder setPreferences(Collection<String> preferences) {
        setListParameter(Keys.PREFERENCES, preferences);
        return this;
    }

    /**
     * Sets what kind of value is being passed as a preference.
     *
     * @param target The kind of value.
     * @return This builder for chaining method calls.
     */
    protected UserPreferenceBuilder setTargetType(FieldType target) {
        if (target != null) {
            setParameter(Keys.TARGET_TYPE, target.getRequestKey());
        } else {
            removeParameter(Keys.TARGET_TYPE);
        }
        return this;
    }

    /**
     * What has the shopper indicated about the brands/categories/products/stores.
     *
     * @param action The action the shopper indicated.
     * @return This builder for chaining method calls.
     */
    protected UserPreferenceBuilder setActionType(ActionType action) {
        if (action != null) {
            setParameter(Keys.ACTION_TYPE, action.getKey());
        } else {
            removeParameter(Keys.ACTION_TYPE);
        }
        return this;
    }

    @Override
    protected RequestBuilder<UserPreferenceResponseInfo> setApiClientKey(String apiClientKey) {
        return this;
    }

    @Override
    protected RequestBuilder<UserPreferenceResponseInfo> setSessionId(String sessionId) {
        setParameter("s", sessionId);
        return this;
    }

    @Override
    protected RequestBuilder<UserPreferenceResponseInfo> setUserId(String userId) {
        if (isSet()) {
            setParameter("u", userId);
        }
        return this;
    }

    @Override
    protected void applyConfigurationParams(ClientConfiguration configuration) {
        if (!isSet()) {
            setApiKey(configuration.getApiKey());
        } else {
            super.applyConfigurationParams(configuration);
        }
    }

    @Override
    protected UserPreferenceResponseInfo createNewResult() {
        return new UserPreferenceResponseInfo();
    }

    @Override
    protected String getEndpointPath(ClientConfiguration configuration) {
        if (!isSet()) {
            return "rrserver/api/user/preference/" + configuration.getUserId();
        } else {
            return "rrserver/api/user/preference";
        }
    }

    @Override
    protected void populateResponse(WebResponse response, JSONObject json, UserPreferenceResponseInfo responseInfo) {
        UserPreferenceParser.parseUserPreferenceResponseInfo(json, responseInfo);
    }

    private boolean isSet() {
        return !TextUtils.isEmpty(getParam(Keys.TARGET_TYPE));
    }
}
