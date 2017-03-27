package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.tresbu.trakeye.domain.CaseTypeAttributeValue;

/**
 * A DTO for the TrCase entity.
 */
public class TrCaseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4113139484543925967L;

	private Long id;

	private String description;

	private Double pinLat;

	private Double pinLong;

	private long createDate;

	private long updateDate;

	private String address;

	private Boolean escalated;

	private String status;

	private String priority;

	private String geofenceName;

	private Set<CaseImageDTO> caseImages = new HashSet<>();

	private String reportedByUser;

	private String assignedToUser;

	private String updatedByUser;

	private String assignedByUser;

	private CaseTypeDTO caseType;

	private Set<CaseTypeAttributeValue> caseTypeAttributeValues;

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public long getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(long updateDate) {
		this.updateDate = updateDate;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Double getPinLat() {
		return pinLat;
	}

	public void setPinLat(Double pinLat) {
		this.pinLat = pinLat;
	}

	public Double getPinLong() {
		return pinLong;
	}

	public void setPinLong(Double pinLong) {
		this.pinLong = pinLong;
	}

	public String getGeofenceName() {
		return geofenceName;
	}

	public void setGeofenceName(String geofenceName) {
		this.geofenceName = geofenceName;
	}

	public String getReportedByUser() {
		return reportedByUser;
	}

	public void setReportedByUser(String reportedByUser) {
		this.reportedByUser = reportedByUser;
	}

	public String getAssignedToUser() {
		return assignedToUser;
	}

	public void setAssignedToUser(String assignedToUser) {
		this.assignedToUser = assignedToUser;
	}

	public String getUpdatedByUser() {
		return updatedByUser;
	}

	public void setUpdatedByUser(String updatedByUser) {
		this.updatedByUser = updatedByUser;
	}

	public String getAssignedByUser() {
		return assignedByUser;
	}

	public void setAssignedByUser(String assignedByUser) {
		this.assignedByUser = assignedByUser;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getEscalated() {
		return escalated;
	}

	public void setEscalated(Boolean escalated) {
		this.escalated = escalated;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public CaseTypeDTO getCaseType() {
		return caseType;
	}

	public void setCaseType(CaseTypeDTO caseType) {
		this.caseType = caseType;
	}

	public Set<CaseTypeAttributeValue> getCaseTypeAttributeValues() {
		return caseTypeAttributeValues;
	}

	public void setCaseTypeAttributeValues(Set<CaseTypeAttributeValue> caseTypeAttributeValues) {
		this.caseTypeAttributeValues = caseTypeAttributeValues;
	}

	public Set<CaseImageDTO> getCaseImages() {
		return caseImages;
	}

	public void setCaseImages(Set<CaseImageDTO> caseImages) {
		this.caseImages = caseImages;
	}

}
