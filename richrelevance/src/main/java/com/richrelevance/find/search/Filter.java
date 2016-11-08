package com.richrelevance.find.search;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public class Filter implements Parcelable {

    public class Keys {
        public static final String VALUE = "value";
        public static final String COUNT = "count";
        public static final String FILTER = "filter";
    }

    private String filter;
    private int count;
    private String value;
    private String facetType;

    public Filter(Facet facet) {
        facetType = facet.getType();
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getApiValue() {
        return String.format(Locale.US, "%s:\"%s\"", facetType, getValue());
    }

    protected Filter(Parcel in) {
        filter = in.readString();
        count = in.readInt();
        value = in.readString();
        facetType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filter);
        dest.writeInt(count);
        dest.writeString(value);
        dest.writeString(facetType);
    }

    public static final Creator<Filter> CREATOR = new Creator<Filter>() {
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };
}