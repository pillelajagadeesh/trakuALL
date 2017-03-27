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
public class AssetUpdateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3748561453423587748L;

	private Long id;

	@NotNull
	@Size(min = 3, max = 50)
	private String name;

	private String description;
	private List<AssetCoordinateDTO> assetCoordinates = new LinkedList<>();

	private Set<AssetTypeAttributeValueDTO> assetTypeAttributeValues = new HashSet<>();

	public List<AssetCoordinateDTO> getAssetCoordinates() {
		return assetCoordinates;
	}

	public void setAssetCoordinates(List<AssetCoordinateDTO> assetCoordinates) {
		this.assetCoordinates = assetCoordinates;
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

	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		AssetUpdateDTO assetDTO = (AssetUpdateDTO) o;

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
				 + '}';
	}
}
