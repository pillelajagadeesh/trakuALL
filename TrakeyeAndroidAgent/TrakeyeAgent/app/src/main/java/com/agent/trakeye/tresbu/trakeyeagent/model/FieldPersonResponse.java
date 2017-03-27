package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;

/**
 * Created by sharmaan on 13-04-2016.
 */
public class FieldPersonResponse implements Serializable {


    double latitude;
    double longitude;
    String address;
    String logSource;
    long createdDateTime;
    long logTimeAndZone;
    int batteryPercentage;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogSource() {
        return logSource;
    }

    public void setLogSource(String logSource) {
        this.logSource = logSource;
    }


    public long getLogTimeAndZone() {
        return logTimeAndZone;
    }

    public void setLogTimeAndZone(long localTimeZone) {
        this.logTimeAndZone = localTimeZone;
    }

    public long getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(long createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public int getBatteryPercentage() {
        return batteryPercentage;
    }

    public void setBatteryPercentage(int batteryPercentage) {
        this.batteryPercentage = batteryPercentage;
    }

    @Override
    public String toString() {
        return "FieldPersonResponse{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", logSource='" + logSource + '\'' +
                ", createdDateTime=" + createdDateTime +
                ", logTimeAndZone=" + logTimeAndZone +
                ", batteryPercentage=" + batteryPercentage +
                '}';
    }
}
