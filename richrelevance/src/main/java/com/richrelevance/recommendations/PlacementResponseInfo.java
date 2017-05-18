package com.richrelevance.recommendations;

import com.richrelevance.ResponseInfo;

import java.util.List;

public class PlacementResponseInfo extends ResponseInfo {
    private String viewGuid;
    private List<PlacementResponse> placements;

    public String getViewGuid() {
        return viewGuid;
    }

    void setViewGuid(String viewGuid) {
        this.viewGuid = viewGuid;
    }

    public List<PlacementResponse> getPlacements() {
        return placements;
    }

    void setPlacements(List<PlacementResponse> placements) {
        this.placements = placements;
    }
}
