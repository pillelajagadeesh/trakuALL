package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;


/**
 * Created by Tresbu on 25-Oct-16.
 */

public class LatestUserInfo implements Serializable {
    long user;
    String login;
    double latitude;
    double longitude;
    String address;
    UserStatus status;

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

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

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
