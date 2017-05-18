package com.richrelevance.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Structure which maps keys to multiple values. Null values are ignored.
 * @param <T> The type of values being stored.
 */
public class ValueMap<T> {

    private Map<String, List<T>> map;

    public ValueMap() {
        map = new LinkedHashMap<>();
    }

    /**
     * Adds the given values to the given key. This will append to any values that already exist under the given key.
     * @param key The key to add the values to.
     * @param values The values to add.
     * @return This map for chaining method calls.
     */
    public ValueMap<T> add(String key, T...values) {
        return add(key, Utils.safeAsList(values));
    }

    /**
     * Adds the given values to the given key. This will append to any values that already exist under the given key.
     * @param key The key to add the values to.
     * @param values The values to add.
     * @return This map for chaining method calls.
     */
    public ValueMap<T> add(String key, List<T> values) {
        List<T> valueList = get(key);
        if (valueList == null) {
            valueList = new LinkedList<>();
            set(key, valueList);
        }

        for (T attribute : values) {
            if (attribute != null) {
                valueList.add(attribute);
            }
        }

        return this;
    }

    /**
     * Gets the values associated with the given key.
     * @param key The key to get the values of.
     * @return The list of values associated with the given key.
     */
    public List<T> get(String key) {
        return map.get(key);
    }

    public Iterable<String> getKeys() {
        return map.keySet();
    }

    public int size() {
        return map.size();
    }

    private void set(String key, List<T> attributes) {
        map.put(key, attributes);
    }
}
