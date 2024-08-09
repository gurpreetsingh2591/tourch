package com.tourch.ui.mapCustomView.map_poliline.events;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Chirag Khandla on 9/9/18.
 */
public class CurrentJourneyEvent {

    private String event = "BEGIN_JOURNEY";
    private LatLng currentLatLng;

    public LatLng getCurrentLatLng() {
        return currentLatLng;
    }

    public void setCurrentLatLng(LatLng currentLatLng) {
        this.currentLatLng = currentLatLng;
    }
}
