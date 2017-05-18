package com.richrelevance.recommendations;

import com.richrelevance.RichRelevance;

import java.util.Map;

public class Creative {
    private String trackingUrl;
    private String campaign;
    private Map<String, String> creativeMap;

    public String getTrackingUrl() {
        return trackingUrl;
    }

    void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    public String getCampaign() {
        return campaign;
    }

    void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public Map<String, String> getCreativeMap() {
        return creativeMap;
    }

    public void setCreativeMap(Map<String, String> creativeMap) {
        this.creativeMap = creativeMap;
    }

    /**
     * Tracks a click on this creative.
     */
    public void trackClick() {
        RichRelevance.trackClick(this);
    }
}