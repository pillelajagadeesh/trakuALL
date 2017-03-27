package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;

/**
 * Created by Tresbu on 18-Oct-16.
 */

public class CaseTypeAttributeValues implements Serializable {

    UserInfo user;
    String attributeValue;
    CaseTypeAttribute caseTypeAttribute;

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public CaseTypeAttribute getCaseTypeAttribute() {
        return caseTypeAttribute;
    }

    public void setCaseTypeAttribute(CaseTypeAttribute caseTypeAttribute) {
        this.caseTypeAttribute = caseTypeAttribute;
    }


    @Override
    public String toString() {
        return "CaseTypeAttributeValues{" +

                ", attributeValue='" + attributeValue + '\'' +
                ", caseTypeAttribute=" + caseTypeAttribute +
                '}';
    }
}
