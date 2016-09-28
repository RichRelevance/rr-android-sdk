package com.richrelevance.recommendations;

import com.richrelevance.internal.json.JSONArrayParserDelegate;
import com.richrelevance.internal.json.JSONHelper;

import org.json.JSONObject;

public class PersonalizeParser {

    private static final JSONArrayParserDelegate<Creative> creativeParserDelegate = new JSONArrayParserDelegate<Creative>() {
        @Override
        public Creative parseObject(JSONObject json) {
            Creative creative = new Creative();
            creative.setCreativeMap(JSONHelper.parseJSONToMap(json));
            creative.setTrackingUrl(creative.getCreativeMap().remove("trackingUrl"));
            if(creative.getTrackingUrl() == null || creative.getTrackingUrl().equals("") || creative.getTrackingUrl().equals("N/A")){
                creative.setTrackingUrl(json.optString("CLICK_THROUGH_URL"));
            }
            creative.setCampaign(creative.getCreativeMap().remove("campaign"));
            return creative;
        }
    };

    private static final JSONArrayParserDelegate<PlacementPersonalizeResponse> placementResponseParserDelegate = new JSONArrayParserDelegate<PlacementPersonalizeResponse>() {
        @Override
        public PlacementPersonalizeResponse parseObject(JSONObject json) {
            return parsePlacementResponse(json);
        }
    };

    public static void parsePlacementResponseInfo(JSONObject json, PlacementPersonalizeResponseInfo responseInfo) {
        if(json == null || responseInfo == null) {
            return;
        }

        responseInfo.setPlacements(JSONHelper.parseJSONArray(json, "placements", placementResponseParserDelegate));
        responseInfo.setRequestMap(JSONHelper.parseJSONToMap(json.optJSONObject("request")));
    }

    private static PlacementPersonalizeResponse parsePlacementResponse(JSONObject json) {
        if(json == null) {
            return null;
        }

        PlacementPersonalizeResponse response = new PlacementPersonalizeResponse();
        response.setPlacement(new Placement(json.optString("placement")));
        response.setHtml(json.optString("html"));
        response.setCreatives(JSONHelper.parseJSONArray(json, "creatives", creativeParserDelegate));

        return response;
    }
}
