package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.tresbu.trakeye.domain.AssetTypeAttribute;
import com.tresbu.trakeye.domain.enumeration.ColorCode;
import com.tresbu.trakeye.domain.enumeration.Layout;

import io.swagger.annotations.ApiModelProperty;

/**
 * A DTO for the AssetType entity.
 */
public class AssetTypeUpdateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9146341611372334154L;

	@NotNull
	private Long id;

	@NotNull
	@Size(min = 3, max = 50)
	private String name;

	private String description;

	@NotNull
	private Layout layout;

	
	private ColorCode colorcode;

	@NotNull
	private Set<AssetTypeAttribute> assetTypeAttributes;
	
	
	private byte[] image;

	@ApiModelProperty(required = true,notes="Asset type name is mandatory and should be string value")
	public String getName() {
		return name;
	}

	@ApiModelProperty(required = true,notes="Asset type attributes is mandatory and should be set of AssetTypeAttribute")
	public Set<AssetTypeAttribute> getAssetTypeAttributes() {
		return assetTypeAttributes;
	}

	public void setAssetTypeAttributes(Set<AssetTypeAttribute> assetTypeAttributes) {
		this.assetTypeAttributes = assetTypeAttributes;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(notes="Asset type description is not mandatory and should be string value")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ApiModelProperty(required = true,notes="Asset type layout is mandatory and should be layout enumeration [FIXED,SPREAD] value")
	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	
	
	@ApiModelProperty(notes="If layout is FIXED, asset type color code is mandatory and should be ColorCode enumeration value [CYAN, BLACK, BLUE, BLUEVIOLET, BROWN, CHARTREUSE, CRIMSON, YELLOW, MAGENTA, DEEPPINK,LIGHTCORAL]")
	public ColorCode getColorcode() {
		return colorcode;
	}

	public void setColorcode(ColorCode colorcode) {
		this.colorcode = colorcode;
	}

	@ApiModelProperty(required = true, notes="Asset type id is mandatory and should long value")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		return "AssetTypeUpdateDTO [id=" + id + ", name=" + name + ", description=" + description + ", layout=" + layout
				+ ", colorcode=" + colorcode + ", assetTypeAttribute=" + assetTypeAttributes
				+  ", image=" + image+ "]";
	}

}
