package com.richrelevance.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Utils {

    public static <T> void addIfNonNull(Collection<T> collection, T item) {
        if (item != null) {
            collection.add(item);
        }
    }

    public static <T> List<T> safeAsList(T... array) {
        if (array == null) {
            return null;
        }

        return Arrays.asList(array);
    }
}
