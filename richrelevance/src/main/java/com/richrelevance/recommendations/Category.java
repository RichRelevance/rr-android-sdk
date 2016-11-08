package com.richrelevance.recommendations;

public class Category {
    private String id;
    private String name;
    private boolean hasChildren;

    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public boolean hasChildren() {
        return hasChildren;
    }

    void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
}
