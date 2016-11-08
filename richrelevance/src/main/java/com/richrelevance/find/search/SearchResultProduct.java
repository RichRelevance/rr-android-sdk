package com.richrelevance.find.search;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResultProduct implements Parcelable {

    public static class Keys {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String CLICK_URL = "clickUrl";
        public static final String IMAGE_ID = "imageId";
        public static final String LINK_ID = "linkId";
        public static final String NUM_REVIEWS = "numReviews";
        public static final String DESCRIPTION = "description";
        public static final String CATEGORY_NAMES = "categoryName";
        public static final String SCORE = "score";
        public static final String PRICE_CENTS = "priceCents";
        public static final String SALES_PRICE_CENTS = "salePriceCents";
        public static final String BRAND = "brand";
        public static final String CATEGORY_IDS = "salePriceCents";
    }

    public enum Field {
        ID(Keys.ID.toLowerCase()),
        NAME(Keys.NAME.toLowerCase()),
        BRAND(Keys.BRAND.toLowerCase()),
        CATEGORY_ID(Keys.CATEGORY_IDS.toLowerCase()),
        CATEGORY_NAME(Keys.CATEGORY_NAMES.toLowerCase()),
        DESCRIPTION(Keys.DESCRIPTION.toLowerCase()),
        RATING("rating"),
        NUM_REVIEWS(Keys.NUM_REVIEWS.toLowerCase()),
        PRICE_CENTS(Keys.PRICE_CENTS.toLowerCase()),
        RELEASE_DATE("release_date");

        private String requestKey;

        Field(String requestKey) {
            this.requestKey = requestKey;
        }

        public String getRequestKey() {
            return requestKey;
        }
    }

    private String id;
    private String name;
    private String clickUrl;
    private String imageId;
    private String linkId;
    private int numReviews;
    private String description;
    private List<String> categoryNames;
    private List<String> categoryIds;
    private double score;
    private int priceCents;
    private int salesPriceCents;
    private String brand;
    private Map<Facet, Object> filtersMap = new HashMap<>();

    public SearchResultProduct() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public int getNumReviews() {
        return numReviews;
    }

    public void setNumReviews(int numReviews) {
        this.numReviews = numReviews;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getPriceCents() {
        return priceCents;
    }

    public void setPriceCents(int priceCents) {
        this.priceCents = priceCents;
    }

    public int getSalesPriceCents() {
        return salesPriceCents;
    }

    public void setSalesPriceCents(int salesPriceCents) {
        this.salesPriceCents = salesPriceCents;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Map<Facet, Object> getFiltersMap() {
        return filtersMap;
    }

    public void addFilter(Facet facet, Object filter) {
        filtersMap.put(facet, filter);
    }

    // Parcelable methods and classes

    protected SearchResultProduct(Parcel in) {
        id = in.readString();
        name = in.readString();
        clickUrl = in.readString();
        imageId = in.readString();
        linkId = in.readString();
        numReviews = in.readInt();
        description = in.readString();
        if (in.readByte() == 0x01) {
            categoryNames = new ArrayList<>();
            in.readList(categoryNames, String.class.getClassLoader());
        } else {
            categoryNames = null;
        }
        if (in.readByte() == 0x01) {
            categoryIds = new ArrayList<>();
            in.readList(categoryIds, String.class.getClassLoader());
        } else {
            categoryIds = null;
        }
        score = in.readDouble();
        priceCents = in.readInt();
        salesPriceCents = in.readInt();
        brand = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(clickUrl);
        dest.writeString(imageId);
        dest.writeString(linkId);
        dest.writeInt(numReviews);
        dest.writeString(description);
        if (categoryNames == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(categoryNames);
        }
        if (categoryIds == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(categoryIds);
        }
        dest.writeDouble(score);
        dest.writeInt(priceCents);
        dest.writeInt(salesPriceCents);
        dest.writeString(brand);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SearchResultProduct> CREATOR = new Parcelable.Creator<SearchResultProduct>() {
        @Override
        public SearchResultProduct createFromParcel(Parcel in) {
            return new SearchResultProduct(in);
        }

        @Override
        public SearchResultProduct[] newArray(int size) {
            return new SearchResultProduct[size];
        }
    };

    // End parcelable methods and classes
}
