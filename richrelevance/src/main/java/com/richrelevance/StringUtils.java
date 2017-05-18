package com.richrelevance;

import com.richrelevance.utils.ValueMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class StringUtils {

    public static String join(String delimiter, Object... items) {
        if (items == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            if (i > 0) {
                builder.append(delimiter);
            }

            builder.append(items[i]);
        }

        return builder.toString();
    }

    public static String join(String delimiter, Collection<?> items) {
        if (items == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Object item : items) {
            if (!isFirst) {
                builder.append(delimiter);
            }

            builder.append(item);
            isFirst = false;
        }

        return builder.toString();
    }

    public static String join(ValueMap<?> map, String keyDelimiter, String valueDelimiter, String valueAssignment) {
        if (map == null) {
            return null;
        }

        // Maintain a list of the strings for each individual key
        List<String> keyMappings = new ArrayList<>(map.size());
        for (String key : map.getKeys()) {
            List<?> values = map.get(key);
            if (values != null && !values.isEmpty()) {
                String valueString = join(valueDelimiter, values);
                keyMappings.add(key + valueAssignment + valueString);
            }
        }

        // Join all of the mapped strings together on the outer delimiter
        return join(keyDelimiter, keyMappings);
    }

    public static String joinFlat(ValueMap<?> map, String delimiter, String valueAssignment) {
        if (map == null) {
            return null;
        }

        // Maintain a list of strings for each key to value pair
        List<String> valueStrings = new ArrayList<>(map.size());
        for (String key : map.getKeys()) {
            List<?> values = map.get(key);
            if (values != null) {
                for (Object value : values) {
                    valueStrings.add(key + valueAssignment + value.toString());
                }
            }
        }

        // Join all of the key value pair strings together on the delimiter
        return join(delimiter, valueStrings);
    }
}
