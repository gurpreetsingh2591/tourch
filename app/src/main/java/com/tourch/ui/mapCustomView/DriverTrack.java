package com.tourch.ui.mapCustomView;

public class DriverTrack {

    Double latitude;
    Double longitude;
    float bearing;
    String status;


    public DriverTrack(Double latitude, Double longitude, String status, float bearing) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.bearing = bearing;
    }

    public DriverTrack() {

    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
