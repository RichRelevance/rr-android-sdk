package com.richrelevance;

/**
 * Class which defines a range of values with optional ends.
 */
public class Range {

    public static final int NONE = Integer.MIN_VALUE;

    private int min;
    private int max;

    public Range(int min, int max) {
        setMin(min);
        setMax(max);
    }

    /**
     * @return True if this range has a minimum value.
     */
    public boolean hasMin() {
        return getMin() != NONE;
    }

    /**
     * @return The minimum value or {@link #NONE} if there is no minimum.
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the minimum value of the range.
     * @param min The minimum value or {@link #NONE} for no minimum.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return True if this range has a maximum value.
     */
    public boolean hasMax() {
        return getMax() != NONE;
    }

    /**
     * @return The maximum value or {@link #NONE} if there is no maximum.
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the maximum value of the range.
     * @param max The maximum value or {@link #NONE} for no maximum.
     */
    public void setMax(int max) {
        this.max = max;
    }
}
