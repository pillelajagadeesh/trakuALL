package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;

public class AssetCreateDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3176894623422436105L;

	private Long id;
	
	@NotNull
    @Size(min = 3, max = 50)
    private String name;

    private String description;

    private Long userId;
    
    @NotNull
    private Long assetTypeId;
	
	private Long createDate;

	private Long updateDate;

	private AssetTypeDTO assetType;

	private Set<AssetTypeAttributeValueDTO> assetTypeAttributeValues;

	private Set<AssetCoordinateDTO> assetCoordinates;
    
    

    @ApiModelProperty( notes="Asset id is not mandatory while creating user and mandatory while updating asset , should be long value")
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ApiModelProperty(required = true, notes="Asset name is mandatory and should be string value")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(notes="Asset description is not mandatory and should be string value")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ApiModelProperty(notes="Not required to give user id")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@ApiModelProperty(required = true,notes="Asset type id is mandatory and should be long value")
	public Long getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Long assetTypeId) {
		this.assetTypeId = assetTypeId;
	}


	public Set<AssetCoordinateDTO> getAssetCoordinates() {
		return assetCoordinates;
	}

	public void setAssetCoordinates(Set<AssetCoordinateDTO> assetCoordinates) {
		this.assetCoordinates = assetCoordinates;
	}

	public AssetTypeDTO getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetTypeDTO assetType) {
		this.assetType = assetType;
	}

	public Set<AssetTypeAttributeValueDTO> getAssetTypeAttributeValues() {
		return assetTypeAttributeValues;
	}

	public void setAssetTypeAttributeValues(Set<AssetTypeAttributeValueDTO> assetTypeAttributeValues) {
		this.assetTypeAttributeValues = assetTypeAttributeValues;
	}


	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public Long getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Long updateDate) {
		this.updateDate = updateDate;
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
	public int hashCode() {
		return Objects.hashCode(id);
	}

	
    

}
