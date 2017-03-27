package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Tresbu on 21-Oct-16.
 */

public class Asset implements Serializable {
    String name;
    String id;
    String description;
    long assetTypeId;
    String createDate;
    String updateDate;
    ArrayList<AssetTypeAttributeValues> assetTypeAttributeValues;
    ArrayList<AssetCoordinates> assetCoordinates;
    AssetType assetType;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(long assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    public ArrayList<AssetTypeAttributeValues> getAssetTypeAttributeValues() {
        return assetTypeAttributeValues;
    }

    public void setAssetTypeAttributeValues(ArrayList<AssetTypeAttributeValues> assetTypeAttributeValues) {
        this.assetTypeAttributeValues = assetTypeAttributeValues;
    }

    public ArrayList<AssetCoordinates> getAssetCoordinates() {
        return assetCoordinates;
    }

    public void setAssetCoordinates(ArrayList<AssetCoordinates> assetCoordinates) {
        this.assetCoordinates = assetCoordinates;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", assetTypeId='" + assetTypeId +
                ", assetType=" + assetType +
                ", assetCoordinates=" + assetCoordinates +
                ", assetTypeAttributeValues=" + assetTypeAttributeValues +
                '}';
    }
}
