package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tresbu on 13-Oct-16.
 */

public class CaseType implements Serializable {
    long id;
    String name;
    String description;
    String createdDate;
    String updateDate;
    long userId;
    UserInfo user;
    List<CaseTypeAttribute> caseTypeAttribute;

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

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
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

    public List<CaseTypeAttribute> getCaseTypeAttribute() {
        return caseTypeAttribute;
    }

    public void setCaseTypeAttribute(List<CaseTypeAttribute> caseTypeAttribute) {
        this.caseTypeAttribute = caseTypeAttribute;
    }

    @Override
    public String toString() {
        return "CaseType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", userId=" + userId +
                ", user=" + user +
                ", caseTypeAttribute=" + caseTypeAttribute +
                '}';
    }
}
