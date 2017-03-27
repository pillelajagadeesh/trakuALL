package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.tresbu.trakeye.domain.TrakeyeTypeAttribute;

import io.swagger.annotations.ApiModelProperty;

public class TrakeyeTypeCreateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private Long id;

	@NotNull
	@ApiModelProperty(required = true, value = "Mandatory")
	private String name;

	private String description;

	@NotNull
	@ApiModelProperty(required = true, value = "Mandatory")
	private Set<TrakeyeTypeAttribute> trakeyeTypeAttribute;

	private Long userId;

	/*public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}*/

	@ApiModelProperty(required = true, notes="Trakeye type name is mandatory and should be string value. ex: Cable reconnection")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(notes="Trakeye type description is not mandatory and should be string value. ex: Cable reconnection near bus stop")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ApiModelProperty(required = true, notes="Service type attributes is mandatory and should be set of service type attributes value. ex: Cable joining")
	public Set<TrakeyeTypeAttribute> getTrakeyeTypeAttribute() {
		return trakeyeTypeAttribute;
	}

	public void setTrakeyeTypeAttribute(Set<TrakeyeTypeAttribute> trakeyeTypeAttribute) {
		this.trakeyeTypeAttribute = trakeyeTypeAttribute;
	}

	@ApiModelProperty(notes="Not required to give user id")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "TrakeyeTypeCreateDTO [name=" + name + ", description=" + description
				+ ", trakeyeTypeAttribute=" + trakeyeTypeAttribute + ", userId=" + userId + "]";
	}

}
