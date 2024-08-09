package com.tourch.ui.mapCustomView.map_poliline.events;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Chirag Khandla on 9/9/18.
 */
public class EndJourneyEvent {

    public String event = "END_JOURNEY";
    private LatLng endJourneyLatLng;

    public LatLng getEndJourneyLatLng() {
        return endJourneyLatLng;
    }

    public void setEndJourneyLatLng(LatLng endJourneyLatLng) {
        this.endJourneyLatLng = endJourneyLatLng;
    }
}
