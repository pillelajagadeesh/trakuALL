package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;

/**
 * Created by Tresbu on 24-Oct-16.
 */
public class ServiceTypeAttributeValues implements Serializable {
    long id;
    ServiceTypeAttribute serviceTypeAttribute;
    String attributeValue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ServiceTypeAttribute getServiceTypeAttribute() {
        return serviceTypeAttribute;
    }

    public void setServiceTypeAttribute(ServiceTypeAttribute serviceTypeAttribute) {
        this.serviceTypeAttribute = serviceTypeAttribute;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
}
