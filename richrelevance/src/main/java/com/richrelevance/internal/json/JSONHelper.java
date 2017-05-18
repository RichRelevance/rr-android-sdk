package com.richrelevance.internal.json;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class with common utilities for JSON parsing
 */
public class JSONHelper {

    /**
     * Delegate which is executed on individual keys of a {@link JSONObject}.
     */
    public interface JSONKeyDelegate {
        /**
         * Called to execute this delegate on the given key.
         *
         * @param json The JSONObject the key belongs to.
         * @param key  The key to handle.
         */
        public void execute(JSONObject json, String key);
    }

    /**
     * Runs the given delegate on all of the keys in the given JSON object.
     *
     * @param json     The JSON whose keys to run over.
     * @param delegate The delegate to run on each key.
     */
    public static void runOverKeys(JSONObject json, JSONKeyDelegate delegate) {
        if (json != null) {
            // JSON uses a set of Strings and returns an Iterator<String>
            // but doesn't clarify in the method definition.
            // Scary, but always works under the current implementation.
            @SuppressWarnings("unchecked")
            Iterator<String> iterator = json.keys();
            while (iterator.hasNext()) {
                delegate.execute(json, iterator.next());
            }
        }
    }

    /**
     * Iterates over the {@link JSONArray} defined at the given key in the
     * given JSON object, if one exists, and parses each element with the given
     * {@link JSONArrayParserDelegate}. If the delegate returns an item, it
     * will be added to the result list. If it does not, that index will be
     * skipped - no null will be added to the list.
     *
     * @param <T>      The type of object to parse from the array.
     * @param json     The {@link JSONObject} to get the array from
     * @param key      The key to look for the array under
     * @param delegate The {@link JSONArrayParserDelegate} to call to parse
     *                 each object
     * @return A {@link List} containing all parsed objects or null if the key
     * didn't map to a {@link JSONArray}
     */
    public static <T> List<T> parseJSONArray(JSONObject json, String key,
                                             JSONArrayParserDelegate<T> delegate) {
        JSONArray array = json.optJSONArray(key);
        if (array == null) return null;

        return parseJSONArray(array, delegate);
    }

    /**
     * Iterates over the given {@link JSONArray} and parses each element with
     * the given {@link JSONArrayParserDelegate}. If the delegate returns an
     * item, it will be added to the result list. If it does not, that index
     * will be skipped - no null will be added to the list.
     *
     * @param <T>      The type of object to parse from the array.
     * @param array    The {@link JSONArray} to parse
     * @param delegate The {@link JSONArrayParserDelegate} to call to parse
     *                 each object
     * @return A {@link List} containing all parsed objects
     */
    public static <T> List<T> parseJSONArray(JSONArray array,
                                             JSONArrayParserDelegate<T> delegate) {

        final int numItems = (array == null) ? 0 : array.length();

        if (numItems == 0) {
            return new ArrayList<T>(0);
        } else {
            List<T> items = new ArrayList<T>(numItems);

            for (int i = 0; i < numItems; i++) {
                try {
                    JSONObject objectJSON = array.getJSONObject(i);

                    T obj = delegate.parseObject(objectJSON);
                    if (obj != null) items.add(obj);
                } catch (JSONException e) {
                    // This shouldn't really ever happen since we are checking
                    // the length
                    Log.d(JSONHelper.class.getName(), "Error parsing object", e);
                }
            }

            return items;
        }
    }

    /**
     * Parses all strings out of the data at the given key inside the given
     * {@link JSONObject} if it exists
     *
     * @param json The {@link JSONObject} to look for the key in
     * @param key  The key to look for the strings under
     * @return The list of strings, or null if they couldn't be parsed or the
     * key wasn't defined
     */
    public static List<String> parseStrings(JSONObject json, String key) {
        // Attempt to grab the messages as an array of messages
        List<String> strings = parseStrings(json.optJSONArray(key));
        if (strings == null) {
            // If there is only one string, the field will be a simple string
            // mapping instead of an array of size 1 :(
            String string = json.optString(key);
            if (!TextUtils.isEmpty(string)) {
                strings = new ArrayList<String>(1);
                strings.add(string);
            }
        }

        return strings;
    }

    /**
     * Parses all strings out of the given {@link JSONArray} into a list.
     *
     * @param json The array to parse.
     * @return A list containing all the strings or null if the input array
     * was null.
     */
    public static List<String> parseStrings(JSONArray json) {
        if (json != null) {

            final int numStrings = json.length();

            List<String> strings = new ArrayList<String>(numStrings);

            for (int i = 0; i < numStrings; i++) {
                String string = json.optString(i);
                if (!TextUtils.isEmpty(string)) strings.add(string);
            }

            return strings;
        }
        return null;
    }

    /**
     * Parses all the keys and their values out of the given {@link JSONObject} into a map.
     *
     * @param json The {@link JSONObject} to get the keys/values from
     * @return A map containing all the keys and their corresponding values.
     */
    public static Map<String, String> parseJSONToMap(JSONObject json) {
        if(json == null) {
            return null;
        }

        final Map<String, String> map = new HashMap<>();
        JSONHelper.runOverKeys(json, new JSONHelper.JSONKeyDelegate() {
            @Override
            public void execute(JSONObject json, String key) {
                map.put(key, json.optString(key));
            }
        });
        return map;
    }
}