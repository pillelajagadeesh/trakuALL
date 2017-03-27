package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;

/**
 * Created by Tresbu on 18-Oct-16.
 */

public class AssetTypeAttributeValues implements Serializable {

    UserInfo user;
    String attributeValue;
    AssetTypeAttribute assetTypeAttribute;

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

    public AssetTypeAttribute getAssetTypeAttribute() {
        return assetTypeAttribute;
    }

    public void setAssetTypeAttribute(AssetTypeAttribute assetTypeAttribute) {
        this.assetTypeAttribute = assetTypeAttribute;
    }


    @Override
    public String toString() {
        return "assetTypeAttributeValues{" +
                ", attributeValue='" + attributeValue + '\'' +
                ", assetTypeAttribute=" + assetTypeAttribute +
                '}';
    }
}
