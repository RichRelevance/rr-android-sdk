package com.richrelevance.recommendations;

import java.util.List;

public class PlacementPersonalizeResponse {

    private Placement placement;

    private String html;

    private List<Creative> creatives;

    public Placement getPlacement() {
        return placement;
    }

    void setPlacement(Placement placement) {
        this.placement = placement;
    }

    public String getHtml() {
        return html;
    }

    void setHtml(String html) {
        this.html = html;
    }

    public List<Creative> getCreatives() {
        return creatives;
    }

    public void setCreatives(List<Creative> creatives) {
        this.creatives = creatives;
    }
}
