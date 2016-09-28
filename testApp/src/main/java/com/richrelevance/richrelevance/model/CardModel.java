package com.richrelevance.richrelevance.model;

import android.graphics.drawable.Drawable;

import java.text.NumberFormat;
import java.util.Locale;

public class CardModel {

    public interface OnCardDismissedListener {
        void onLike();

        void onDislike();

        void onNeutral();
    }

    public interface OnClickListener {
        void OnClickListener();
    }

    private String title;

    private String brand;

    private String imgUrl;

    private long price;

    private Drawable cardLikeImageDrawable;

    private Drawable cardDislikeImageDrawable;

    private OnCardDismissedListener mOnCardDismissedListener = null;

    private OnClickListener mOnClickListener = null;

    public CardModel() {
        this(null, null, null, 0);
    }

    public CardModel(String title, String brand, String imgUrl, int price) {
        this.title = title;
        this.brand = brand;
        this.imgUrl = imgUrl;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String description) {
        this.brand = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPrice() {
        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
        return n.format(price / 100.0);
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public OnCardDismissedListener getOnCardDismissedListener() {
        return this.mOnCardDismissedListener;
    }

    public void setOnCardDismissedListener(OnCardDismissedListener listener) {
        this.mOnCardDismissedListener = listener;
    }
}
