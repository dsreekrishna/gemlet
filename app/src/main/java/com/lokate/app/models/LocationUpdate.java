package com.lokate.app.models;

import com.google.gson.annotations.SerializedName;

public class LocationUpdate {
    @SerializedName("latitude")
    private double latitude;
    
    @SerializedName("longitude")
    private double longitude;

    public LocationUpdate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
