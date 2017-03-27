package com.tresbu.trakeye.service.dto;

import java.io.Serializable;

public class AssetTypeAttributeValueDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1178799546464755387L;


	private String attributeValue;

	private Long id;

	private Long userId;

	private AssetTypeAttributeDTO assetTypeAttribute;

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public AssetTypeAttributeDTO getAssetTypeAttribute() {
		return assetTypeAttribute;
	}

	public void setAssetTypeAttribute(AssetTypeAttributeDTO assetTypeAttribute) {
		this.assetTypeAttribute = assetTypeAttribute;
	}

	@Override
	public String toString() {
		return "AssetTypeAttributeValueDTO [attributeValue=" + attributeValue + ", id=" + id + ", user=" + userId
				+ ", assetTypeAttribute=" + assetTypeAttribute + "]";
	}

}
