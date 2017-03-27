package com.tresbu.trakeye.service.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

public class GeofenceCreateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@ApiModelProperty(required = true, value="Mandatory")
	private String name;

	private String description;

	@NotNull
	@ApiModelProperty(required = true, value="Mandatory")
	private String coordinates;

	private Long userId;

	@ApiModelProperty(required = true, notes="Geofence name is mandatory and should be string value. ex:  Bangalore")
	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(notes="Geofence description is not mandatory and should be string value. ex:  Bangalore area geofence")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ApiModelProperty(required = true, notes="Geofence cordinates is mandatory and should be list log latitude and longitude values. ex: [{lat:12.774745014054888,lng:77.7744640270248},{lat:12.71045055421516,lng:77.27183951530606}]")
	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

	@ApiModelProperty(notes="It is not requiredto give user id value")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}


	@Override
	public String toString() {
		return "GeofenceCreateDTO [name=" + name + ", description=" + description + ", coordinates=" + coordinates
				+ ", userId=" + userId + "]";
	}

}
