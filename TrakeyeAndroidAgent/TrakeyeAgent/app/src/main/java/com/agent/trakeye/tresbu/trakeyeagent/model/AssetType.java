package com.agent.trakeye.tresbu.trakeyeagent.model;

/**
 * Created by Pradeep on 12/9/2016.
 */

import java.io.Serializable;
import java.util.List;

public class AssetType implements Serializable {
    long id;
    String name;
    String description;
    String createDate;
    String updateDate;
    long userId;
    String layout;
    UserInfo user;
    String image;
    List<AssetTypeAttribute> assetTypeAttributes;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public List<AssetTypeAttribute> getAssetTypeAttributes() {
        return assetTypeAttributes;
    }

    public void setAssetTypeAttributes(List<AssetTypeAttribute> assetTypeAttributes) {
        this.assetTypeAttributes = assetTypeAttributes;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getColorcode() {
        return colorcode;
    }

    public void setColorcode(String colorcode) {
        this.colorcode = colorcode;
    }

    private String colorcode;

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


    @Override
    public String toString() {
        return "AssetType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createDate='" + createDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", userId=" + userId +
                ", colorcode=" + colorcode +
                ", layout=" + layout +
                ", assetTypeAttributes=" + assetTypeAttributes +
                '}';
    }
}
