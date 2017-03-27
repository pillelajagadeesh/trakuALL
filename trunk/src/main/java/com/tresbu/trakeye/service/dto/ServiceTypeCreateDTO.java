package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.tresbu.trakeye.domain.ServiceTypeAttribute;

import io.swagger.annotations.ApiModelProperty;

public class ServiceTypeCreateDTO implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@NotNull
	@ApiModelProperty(required = true, value = "Mandatory")
	private String name;

	private String description;

	@NotNull
	@ApiModelProperty(required = true, value = "Mandatory")
	private Set<ServiceTypeAttribute> serviceTypeAttribute;
	
	private Long userId;

	@ApiModelProperty(required = true, notes="Service type attributes is mandatory and should be set of service type attributes value. ex: Cable joining")
	public Set<ServiceTypeAttribute> getServiceTypeAttribute() {
		return serviceTypeAttribute;
	}

	public void setServiceTypeAttribute(Set<ServiceTypeAttribute> serviceTypeAttribute) {
		this.serviceTypeAttribute = serviceTypeAttribute;
	}

	@ApiModelProperty(notes="Not required to give user id")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@ApiModelProperty(required = true, notes="Service type name is mandatory and should be string value. ex: Cable reconnection")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(notes="Service type description is not mandatory and should be string value. ex: Cable reconnection near bus stop")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ServiceTypeCreateDTO [name=" + name + ", description=" + description + ", serviceTypeAttribute="
				+ serviceTypeAttribute + ", userId=" + userId + "]";
	}

}
