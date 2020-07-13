package com.thresholdsoft.praanadhara.ui.surveytrack.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class MarkerTag implements Serializable {
    private LatLng latLng;
    private int position;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
