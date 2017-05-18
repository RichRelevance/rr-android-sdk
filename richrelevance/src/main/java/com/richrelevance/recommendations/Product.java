package com.richrelevance.recommendations;

public class Product {

    private String id;
    private int quantity;
    private Integer priceCents = null;
    private Double priceDollars = null;

    public Product(String id, int quantity, Integer priceCents) {
        setId(id);
        setQuantity(quantity);
        setPriceCents(priceCents);
    }

    public Product(String id, int quantity, Double priceDollars) {
        setId(id);
        setQuantity(quantity);
        setPriceDollars(priceDollars);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getPriceCents() {
        return priceCents;
    }

    public void setPriceCents(Integer priceCents) {
        this.priceCents = priceCents;
    }

    public Double getPriceDollars() {
        return priceDollars;
    }

    public void setPriceDollars(Double priceDollars) {
        this.priceDollars = priceDollars;
    }

}
