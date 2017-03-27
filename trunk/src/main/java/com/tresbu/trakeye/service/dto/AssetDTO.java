package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the Asset entity.
 */
public class AssetDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3748561453423587748L;

	private Long id;

	@NotNull
	@Size(min = 3, max = 50)
	private String name;

	private String description;

	private Long createDate;

	private Long updateDate;

	private Long userId;
	
	
	private UserIdDTO userIdDTO;

	private AssetTypeDTO assetType;
	
	private List<AssetCoordinateDTO> assetCoordinates = new LinkedList<>();

	private Set<AssetTypeAttributeValueDTO> assetTypeAttributeValues = new HashSet<>();

	public List<AssetCoordinateDTO> getAssetCoordinates() {
		return assetCoordinates;
	}

	public void setAssetCoordinates(List<AssetCoordinateDTO> assetCoordinates) {
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	


	public UserIdDTO getUserIdDTO() {
		return userIdDTO;
	}

	public void setUserIdDTO(UserIdDTO userIdDTO) {
		this.userIdDTO = userIdDTO;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		AssetDTO assetDTO = (AssetDTO) o;

		if (!Objects.equals(id, assetDTO.id))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "AssetDTO{" + "id=" + id + ", name='" + name + "'" + ", description='" + description + "'"
				+ ", createDate='" + createDate + "'" + ", updateDate='" + updateDate + "'" + '}';
	}
}
