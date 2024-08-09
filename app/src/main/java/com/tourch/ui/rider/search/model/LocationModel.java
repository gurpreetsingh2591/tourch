package com.tourch.ui.rider.search.model;

public class LocationModel {
    Float bearing;
    Double latitude;
    Double longitude;
    String status;

    public LocationModel() {
    }

    public LocationModel(Float bearing, Double latitude, Double longitude, String status) {
        this.bearing = bearing;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    public Float getBearing() {
        return bearing;
    }

    public void setBearing(Float bearing) {
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
