package com.richrelevance.recommendations;

import java.util.List;

public class PlacementResponse {

    private Placement placement;

    private String strategyMessage;

    private String htmlElementId;
    private String html;

    private List<RecommendedProduct> recommendedProducts;

    public Placement getPlacement() {
        return placement;
    }

    void setPlacement(Placement placement) {
        this.placement = placement;
    }

    public String getStrategyMessage() {
        return strategyMessage;
    }

    void setStrategyMessage(String strategyMessage) {
        this.strategyMessage = strategyMessage;
    }

    public String getHtmlElementId() {
        return htmlElementId;
    }

    void setHtmlElementId(String htmlElementId) {
        this.htmlElementId = htmlElementId;
    }

    public String getHtml() {
        return html;
    }

    void setHtml(String html) {
        this.html = html;
    }

    public List<RecommendedProduct> getRecommendedProducts() {
        return recommendedProducts;
    }

    void setRecommendedProducts(List<RecommendedProduct> recommendedProducts) {
        this.recommendedProducts = recommendedProducts;
    }
}
