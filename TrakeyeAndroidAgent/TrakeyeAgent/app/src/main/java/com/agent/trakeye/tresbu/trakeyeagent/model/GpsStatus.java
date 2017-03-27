package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;
import java.io.StringReader;

/**
 * Created by Tresbu on 05-Dec-16.
 */

public class GpsStatus implements Serializable {
    String login;
    boolean gpsStatus;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(boolean gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    @Override
    public String toString() {
        return "GpsStatus{" +
                "login='" + login + '\'' +
                ", gpsStatus=" + gpsStatus +
                '}';
    }
}
