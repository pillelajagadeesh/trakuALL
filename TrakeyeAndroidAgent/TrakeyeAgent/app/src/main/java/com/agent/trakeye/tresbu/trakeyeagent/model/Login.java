package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;

/**
 * Created by Tresbu on 18-Oct-16.
 */

public class Login implements Serializable {
    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    String applicationVersion;
    String operatingSystem;
    String username;
    String password;
    String imei;
    String phoneNo;

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    String fcmToken;
    boolean rememberMe;
    boolean gpsStatus;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getIMEI() {
        return imei;
    }

    public void setIMEI(String IMEI) {
        this.imei = IMEI;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public boolean isGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(boolean gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    @Override
    public String toString() {
        return "Login{" +
                "applicationVersion='" + applicationVersion + '\'' +
                ", operatingSystem='" + operatingSystem + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", imei='" + imei + '\'' +
                ", fcmToken='" + fcmToken + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", rememberMe=" + rememberMe +
                ", gpsStatus=" + gpsStatus +
                '}';
    }
}
