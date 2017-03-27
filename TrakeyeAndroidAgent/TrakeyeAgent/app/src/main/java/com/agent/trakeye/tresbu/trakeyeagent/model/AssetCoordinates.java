package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;

/**
 * Created by Tresbu on 18-Oct-16.
 */

public class AssetCoordinates implements Serializable {

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    double latitude;
    double longitude;

    @Override
    public String toString() {
        return "assetCoordinates{" +
                "latitude='" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
