package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;

/**
 * Created by Tresbu on 20-Oct-16.
 */

public class LocationRequest extends FieldPersonResponse implements Serializable {

    long id;
    String userName;
    long userId;
    long updatedDateTime;
    double distanceTravelled;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(long updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public double getDistanceTravelled() {
        return distanceTravelled;
    }

    public void setDistanceTravelled(double distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    @Override
    public String toString() {
        return "LocationRequest{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userId=" + userId +
                ", updatedDateTime=" + updatedDateTime +
                ", distanceTravelled=" + distanceTravelled +
                '}';
    }
}
