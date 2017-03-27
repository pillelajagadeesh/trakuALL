package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.Set;

import com.tresbu.trakeye.domain.AssetTypeAttribute;
import com.tresbu.trakeye.domain.enumeration.Layout;

/**
 * A DTO for the AssetType entity.
 */
public class AssetTypeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9146341611372334154L;

	private Long id;

	private String name;

	private String description;

	private Layout layout;

	private String colorcode;

	private Long userId;

	private long createDate;

	private long updateDate;

	private String createdBy;

	private Set<AssetTypeAttribute> assetTypeAttributes;
	
	private byte[] image;

	public Set<AssetTypeAttribute> getAssetTypeAttributes() {
		return assetTypeAttributes;
	}

	public void setAssetTypeAttributes(Set<AssetTypeAttribute> assetTypeAttributes) {
		this.assetTypeAttributes = assetTypeAttributes;
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

	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	public String getColorcode() {
		return colorcode;
	}

	public void setColorcode(String colorcode) {
		this.colorcode = colorcode;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public long getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(long updateDate) {
		this.updateDate = updateDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	

	

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "AssetTypeDTO [id=" + id + ", name=" + name + ", description=" + description + ", layout=" + layout
				+ ", colorcode=" + colorcode + ", userId=" + userId + ", createDate=" + createDate + ", updateDate="
				+ updateDate + ", createdBy=" + createdBy + ", assetTypeAttribute=" + assetTypeAttributes + ", image=" + image + "]";
	}

}
