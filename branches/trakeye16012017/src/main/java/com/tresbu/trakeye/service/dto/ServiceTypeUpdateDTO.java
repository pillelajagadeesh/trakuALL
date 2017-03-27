package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.tresbu.trakeye.domain.ServiceTypeAttribute;

import io.swagger.annotations.ApiModelProperty;

public class ServiceTypeUpdateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@ApiModelProperty(required = true, value = "Mandatory")
	private Long id;

	@NotNull
	@ApiModelProperty(required = true, value = "Mandatory")
	private String name;

	private String description;

	@NotNull
	@ApiModelProperty(required = true, value = "Mandatory")
	private Set<ServiceTypeAttribute> serviceTypeAttribute;

	@ApiModelProperty(required = true, notes="Service type id is mandatory and should be long value. ex: 1010")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@ApiModelProperty(required = true, notes="Service type attributes is mandatory and should be set of service type attributes value. ex: Cable joining")
	public Set<ServiceTypeAttribute> getServiceTypeAttribute() {
		return serviceTypeAttribute;
	}

	public void setServiceTypeAttribute(Set<ServiceTypeAttribute> serviceTypeAttribute) {
		this.serviceTypeAttribute = serviceTypeAttribute;
	}

	@Override
	public String toString() {
		return "ServiceTypeUpdateDTO [id=" + id + ", name=" + name + ", description=" + description
				+ ", serviceTypeAttribute=" + serviceTypeAttribute + "]";
	}

}
