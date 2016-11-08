package com.richrelevance.recommendations;

import com.richrelevance.ResponseInfo;

import java.util.List;
import java.util.Map;

public class PlacementPersonalizeResponseInfo extends ResponseInfo {
    private List<PlacementPersonalizeResponse> placements;
    private Map<String, String> requestMap;

    public List<PlacementPersonalizeResponse> getPlacements() {
        return placements;
    }

    void setPlacements(List<PlacementPersonalizeResponse> placements) {
        this.placements = placements;
    }

    public Map<String, String> getRequestMap() {
        return requestMap;
    }

    public void setRequestMap(Map<String, String> requestMap) {
        this.requestMap = requestMap;
    }
}
