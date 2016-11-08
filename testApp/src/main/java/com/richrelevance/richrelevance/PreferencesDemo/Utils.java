package com.richrelevance.richrelevance.PreferencesDemo;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

public class Utils {

    public static float functionNormalize(int max, int min, int value) {
        int intermediateValue = max - min;
        value -= intermediateValue;
        float var = Math.abs((float) value / (float) intermediateValue);
        return Math.abs((float) value / (float) intermediateValue);
    }

    public static void changeDrawableColor(Drawable drawable, int color) {
        if(drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }
}
