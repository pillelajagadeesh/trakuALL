package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.tresbu.trakeye.domain.CaseTypeAttributeValue;
import com.tresbu.trakeye.domain.enumeration.CasePriority;
import com.tresbu.trakeye.domain.enumeration.CaseStatus;

import io.swagger.annotations.ApiModelProperty;

/**
 * An Update DTO for the TrCase entity.
 */
public class TrCaseUpdateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4113139484543925967L;

	@NotNull
	private Long id;

	@NotNull
	private String description;


	private Double pinLat;

	private Double pinLong;

	private String address;

	private Boolean escalated;

	private CaseStatus status;

	private CasePriority priority;

	private Long assignedTo;

	private Long updatedBy;

	private CaseTypeDTO caseType;

	private Set<CaseTypeAttributeValue> caseTypeAttributeValues;

	private GeofenceDTO geofence;

	private Set<CaseImageDTO> caseImages = new HashSet<>();

	@ApiModelProperty(required=true,notes="Case id is mandatory and should be a long value. ex: 1002")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ApiModelProperty(required = true, notes="Case description is mandatory and should be String value. ex: Aerial Wire Problem")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ApiModelProperty(notes="Case pin latitude is not mandatory and should be  double value. ex: 12.909152667745944")
	public Double getPinLat() {
		return pinLat;
	}

	public void setPinLat(Double pinLat) {
		this.pinLat = pinLat;
	}

	@ApiModelProperty(notes="Case pin longitude is not mandatory and should be  double value. ex: 77.59973865783259")
	public Double getPinLong() {
		return pinLong;
	}

	public void setPinLong(Double pinLong) {
		this.pinLong = pinLong;
	}

	@ApiModelProperty(required=true, notes="Case address is mandatory and should be string value. ex: 10, 11th A Cross Road,India,IN,Karnataka,Bengaluru")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@ApiModelProperty( notes="Case escalated is not mandatory and should be boolean value. ex: 1 or 0")
	public Boolean getEscalated() {
		return escalated;
	}

	public void setEscalated(Boolean escalated) {
		this.escalated = escalated;
	}

	@ApiModelProperty(required=true, notes="Case status is mandatory and should be string value. ex: NEW or INPROGRESS or PENDING or ASSIGNED or RESOLVED or CANCELLED ")
	public CaseStatus getStatus() {
		return status;
	}

	public void setStatus(CaseStatus status) {
		this.status = status;
	}

	@ApiModelProperty(required = true, notes="Case priority is mandatory and should be  string value. ex: HIGH or MEDIUM or LOW or CRITICAL")
	public CasePriority getPriority() {
		return priority;
	}

	public void setPriority(CasePriority priority) {
		this.priority = priority;
	}

	@ApiModelProperty(notes="Case assigned to is mandatory for user with role user admin and not mandatory for user with role user and should be  long value.This is id of the user to whom the case has to be assigned ex: 1001")
	public Long getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(Long assignedTo) {
		this.assignedTo = assignedTo;
	}

	@ApiModelProperty(notes="Not reuired to give updated by value")
	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	@ApiModelProperty(notes="Not reuired to give updated by value")
	public GeofenceDTO getGeofence() {
		return geofence;
	}

	public void setGeofence(GeofenceDTO geofence) {
		this.geofence = geofence;
	}

	@ApiModelProperty(notes="Case image is not mandatory and should be image file")
	public Set<CaseImageDTO> getCaseImages() {
		return caseImages;
	}

	public void setCaseImages(Set<CaseImageDTO> caseImages) {
		this.caseImages = caseImages;
	}

	@ApiModelProperty(required=true,notes="Case type is mandatory and Case type DTO should be given")
	public CaseTypeDTO getCaseType() {
		return caseType;
	}

	public void setCaseType(CaseTypeDTO caseType) {
		this.caseType = caseType;
	}

	@ApiModelProperty(notes="Case type attributes is not mandatory and set of case type attributes should be given")
	public Set<CaseTypeAttributeValue> getCaseTypeAttributeValues() {
		return caseTypeAttributeValues;
	}

	public void setCaseTypeAttributeValues(Set<CaseTypeAttributeValue> caseTypeAttributeValues) {
		this.caseTypeAttributeValues = caseTypeAttributeValues;
	}

	

}
