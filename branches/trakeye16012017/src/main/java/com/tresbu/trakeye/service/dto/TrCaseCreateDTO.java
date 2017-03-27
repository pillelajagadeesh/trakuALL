package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.tresbu.trakeye.domain.CaseTypeAttributeValue;
import com.tresbu.trakeye.domain.enumeration.CasePriority;
import com.tresbu.trakeye.domain.enumeration.CaseStatus;

import io.swagger.annotations.ApiModelProperty;

public class TrCaseCreateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@ApiModelProperty(required = true, value = "Mandatory")
	private String description;

	private Double pinLat;

	private Double pinLong;

	@NotNull
	@ApiModelProperty(required = true, value = "Mandatory")
	private String address;

	private Boolean escalated;

	private CaseStatus status;

	@NotNull
	@ApiModelProperty(required = true, value = "Mandatory")
	private CasePriority priority;

	private Long assignedTo;

	private Long assignedBy;

	private long reportedBy;

	private String reportedByUser;

	private String assignedToUser;

	@NotNull
	@ApiModelProperty(required = true, value = "Mandatory")
	private CaseTypeDTO caseType;

	private Set<CaseTypeAttributeValue> caseTypeAttributeValues;

	private Set<CaseImageDTO> caseImages = new HashSet<>();

	@ApiModelProperty(notes = "NOt required to give reported by value")
	public String getReportedByUser() {
		return reportedByUser;
	}

	public void setReportedByUser(String reportedByUser) {
		this.reportedByUser = reportedByUser;
	}

	@ApiModelProperty(notes = "Case assigned to is mandatory for user with role user admin and not mandatory for user with role user and should be  long value.This is id of the user to whom the case has to be assigned ex: 1001")
	public String getAssignedToUser() {
		return assignedToUser;
	}

	public void setAssignedToUser(String assignedToUser) {
		this.assignedToUser = assignedToUser;
	}

	@ApiModelProperty(required = true, notes = "Case type is mandatory and Case type DTO should be given")
	public CaseTypeDTO getCaseType() {
		return caseType;
	}

	public void setCaseType(CaseTypeDTO caseType) {
		this.caseType = caseType;
	}

	@ApiModelProperty(notes = "Case type attributes is not mandatory and set of case type attributes should be given")
	public Set<CaseTypeAttributeValue> getCaseTypeAttributeValues() {
		return caseTypeAttributeValues;
	}

	public void setCaseTypeAttributeValues(Set<CaseTypeAttributeValue> caseTypeAttributeValues) {
		this.caseTypeAttributeValues = caseTypeAttributeValues;
	}

	@ApiModelProperty(required = true, notes = "Case description is mandatory and should be String value. ex: Aerial Wire Problem")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ApiModelProperty(notes = "PinLat to is not mandatory for user with role user admin and mandatory for user with role user and should be  long value. ex: PinLat:12.399999")
	public Double getPinLat() {
		return pinLat;
	}

	public void setPinLat(Double pinLat) {
		this.pinLat = pinLat;
	}

	@ApiModelProperty(notes = "PinLong to is not mandatory for user with role user admin and mandatory for user with role user and should be  long value. ex: PinLong:13.386868")
	public Double getPinLong() {
		return pinLong;
	}

	public void setPinLong(Double pinLong) {
		this.pinLong = pinLong;
	}

	@ApiModelProperty(required = true, notes = "Case address is mandatory and should be string value. ex: 10, 11th A Cross Road,India,IN,Karnataka,Bengaluru")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@ApiModelProperty(notes = "Case escalated is not mandatory and should be boolean value. ex: 1 or 0")
	public Boolean getEscalated() {
		return escalated;
	}

	public void setEscalated(Boolean escalated) {
		this.escalated = escalated;
	}

	@ApiModelProperty(notes = "Not required to give case status value")
	public CaseStatus getStatus() {
		return status;
	}

	public void setStatus(CaseStatus status) {
		this.status = status;
	}

	@ApiModelProperty(required = true, notes = "Case priority is mandatory and should be  string value. ex: HIGH or MEDIUM or LOW or CRITICAL")
	public CasePriority getPriority() {
		return priority;
	}

	public void setPriority(CasePriority priority) {
		this.priority = priority;
	}

	@ApiModelProperty(notes = "AssignedTo to is not mandatory for user with role user admin and mandatory for user with role user and should be  long value. ex: 1001")
	public Long getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(Long assignedTo) {
		this.assignedTo = assignedTo;
	}

	@ApiModelProperty(notes = "Not required to give asigned  by value")
	public Long getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(Long assignedBy) {
		this.assignedBy = assignedBy;
	}
	@ApiModelProperty(notes = "ReportedBy to is not mandatory for user with role user admin and mandatory for user with role user and should be  long value. ex: 1001")
	public long getReportedBy() {
		return reportedBy;
	}

	public void setReportedBy(long reportedBy) {
		this.reportedBy = reportedBy;
	}

	@ApiModelProperty(notes = "Case image is not mandatory and should be image file")
	public Set<CaseImageDTO> getCaseImages() {
		return caseImages;
	}

	public void setCaseImages(Set<CaseImageDTO> caseImages) {
		this.caseImages = caseImages;
	}

}
