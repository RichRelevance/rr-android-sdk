package com.richrelevance.find.search;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Facet implements Parcelable {

    public static class Keys {
        public static final String TYPE = "facet";
        public static final String FITERS = "values";
    }

    private String type;
    private List<Filter> filters;

    public Facet() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    protected Facet(Parcel in) {
        type = in.readString();
        filters = new ArrayList<>();

        if (in.readByte() == 0x01) {
            in.readList(filters, Filter.class.getClassLoader());
        }
    }

    // Parcelable methods and classes

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        if (filters == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(filters);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Facet> CREATOR = new Parcelable.Creator<Facet>() {
        @Override
        public Facet createFromParcel(Parcel in) {
            return new Facet(in);
        }

        @Override
        public Facet[] newArray(int size) {
            return new Facet[size];
        }
    };

    // End Parcelable methods and classes
}
