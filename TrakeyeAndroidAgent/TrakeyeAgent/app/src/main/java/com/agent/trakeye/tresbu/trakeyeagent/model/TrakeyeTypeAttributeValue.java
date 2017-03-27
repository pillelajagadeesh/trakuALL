package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;


public class TrakeyeTypeAttributeValue implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private TrakeyeTypeAttribute trakeyeTypeAttribute;
    private String attributeValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrakeyeTypeAttribute getTrakeyeTypeAttribute() {
        return trakeyeTypeAttribute;
    }

    public void setTrakeyeTypeAttribute(TrakeyeTypeAttribute trakeyeTypeAttribute) {
        this.trakeyeTypeAttribute = trakeyeTypeAttribute;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    @Override
    public String toString() {
        return "TrakeyeTypeAttributeValue{" +
                "id=" + id +
                ", trakeyeTypeAttribute=" + trakeyeTypeAttribute +
                ", attributeValue='" + attributeValue + '\'' +
                '}';
    }
}
