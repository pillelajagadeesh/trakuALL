package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.tresbu.trakeye.domain.ServiceTypeAttributeValue;
import com.tresbu.trakeye.domain.enumeration.ServiceStatus;

import io.swagger.annotations.ApiModelProperty;

public class TrServiceUpdateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@ApiModelProperty(required = true, value = "Mandatory")
	private Long id;

	private String description;

	@NotNull
	@ApiModelProperty(required = true, value = "Mandatory")
	private long serviceDate;

	@NotNull
	@ApiModelProperty(required = true, value = "Mandatory")
	private ServiceStatus status;

	private String notes;

	private Long updatedBy;

	@NotNull
	@ApiModelProperty(required = true, value = "Mandatory")
	private TrCaseDTO trCase;

	private Set<ServiceImageDTO> serviceImages = new HashSet<>();

	private Set<ServiceTypeAttributeValue> serviceTypeAttributeValues;

	@NotNull
	@ApiModelProperty(required = true, value = "Mandatory")
	private ServiceTypeDTO serviceType;

	@ApiModelProperty(required=true, notes="Service id is mandatory and should be long value. ex: 2001")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ApiModelProperty( notes="Service description is not mandatory and should be string value.")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ApiModelProperty(required=true,notes="Service date is mandatory and should be long value. ex: 1479277800000")
	public long getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(long serviceDate) {
		this.serviceDate = serviceDate;
	}

	@ApiModelProperty(required=true,notes="Service staus is mandatory and should be string value. ex: INPROGRESS or PENDING or CLOSED or CANCELLED")
	public ServiceStatus getStatus() {
		return status;
	}

	public void setStatus(ServiceStatus status) {
		this.status = status;
	}

	@ApiModelProperty(notes="Service notes is not mandatory and should be string value. ")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@ApiModelProperty(notes="Not required to give this value")
	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	@ApiModelProperty(required=true,notes="Case is mandatory and should be TrCaseDTO value. This is the case to which service is being provided")
	public TrCaseDTO getTrCase() {
		return trCase;
	}

	public void setTrCase(TrCaseDTO trCase) {
		this.trCase = trCase;
	}

	@ApiModelProperty(notes="Service image is not mandatory and should be set of ServiceImageDTO value. This should be image file")
	public Set<ServiceImageDTO> getServiceImages() {
		return serviceImages;
	}

	public void setServiceImages(Set<ServiceImageDTO> serviceImages) {
		this.serviceImages = serviceImages;
	}
	
	@ApiModelProperty(notes="Service type attributes is not mandatory and should be set of service type attributes")
	public Set<ServiceTypeAttributeValue> getServiceTypeAttributeValues() {
		return serviceTypeAttributeValues;
	}

	public void setServiceTypeAttributeValues(Set<ServiceTypeAttributeValue> serviceTypeAttributeValues) {
		this.serviceTypeAttributeValues = serviceTypeAttributeValues;
	}

	@ApiModelProperty(required=true, notes="Service type is mandatory and should be ServiceTypeDTO value. ")
	public ServiceTypeDTO getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceTypeDTO serviceType) {
		this.serviceType = serviceType;
	}

	@Override
	public String toString() {
		return "TrServiceUpdateDTO [id=" + id + ", description=" + description + ", serviceDate=" + serviceDate
				+ ", status=" + status + ", notes=" + notes + ", updatedBy=" + updatedBy + ", trCase=" + trCase
				+ ", serviceImages=" + serviceImages + ", serviceTypeAttributeValues=" + serviceTypeAttributeValues
				+ ", serviceType=" + serviceType + "]";
	}

}
