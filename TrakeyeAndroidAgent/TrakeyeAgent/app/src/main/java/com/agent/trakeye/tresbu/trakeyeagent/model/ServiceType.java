package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Tresbu on 24-Oct-16.
 */

public class ServiceType implements Serializable {
    long id;
    String name;
    String description;
    String createdDate;
    String updatedDate;
    ArrayList<ServiceTypeAttribute> serviceTypeAttribute;
    long userId;
    UserInfo user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public ArrayList<ServiceTypeAttribute> getServiceTypeAttribute() {
        return serviceTypeAttribute;
    }

    public void setServiceTypeAttribute(ArrayList<ServiceTypeAttribute> serviceTypeAttribute) {
        this.serviceTypeAttribute = serviceTypeAttribute;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
