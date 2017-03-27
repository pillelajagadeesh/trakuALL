package com.tresbu.trakeye.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tresbu.trakeye.domain.enumeration.CasePriority;
import com.tresbu.trakeye.domain.enumeration.CaseStatus;

/**
 * A TrCase.
 */
@Entity
@Table(name = "trakeye_trcase")
public class TrCase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2712943478121372573L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(name = "description", nullable = false)
	private String description;

	@NotNull
	@Column(name = "create_date", nullable = false)
	private long createDate;

	@NotNull
	@Column(name = "update_date", nullable = false)
	private long updateDate;

	@Column(name = "pin_lat", precision = 15, scale = 3)
	private Double pinLat;

	@Column(name = "pin_long", precision = 15, scale = 3)
	private Double pinLong;

	@Column(name = "address")
	private String address;

	@Column(name = "escalated")
	private Boolean escalated;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private CaseStatus status;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "priority", nullable = false)
	private CasePriority priority;

	@ManyToOne
	private Geofence geofence;

	@ManyToOne
	private User reportedBy;

	@ManyToOne
	private User assignedTo;

	@ManyToOne
	private User updatedBy;

	@ManyToOne
	private User assignedBy;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	private CaseType caseType;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "trakeye_case_trakeye_attributevalue", joinColumns = {
			@JoinColumn(name = "trakeye_case_id") }, inverseJoinColumns = {
					@JoinColumn(name = "trakeye_casetype_attribute_value_id") })
	private Set<CaseTypeAttributeValue> caseTypeAttributeValues = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "trCase")
	private Set<CaseImage> caseImages = new HashSet<>();
	
	@JsonIgnore
	@ManyToOne
	private Tenant tenant;

	public CasePriority getPriority() {
		return priority;
	}

	public void setPriority(CasePriority priority) {
		this.priority = priority;
	}

	public Long getId() {
		return id;
	}

	public Geofence getGeofence() {
		return geofence;
	}

	public void setGeofence(Geofence geofence) {
		this.geofence = geofence;
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

	public CaseStatus getStatus() {
		return status;
	}

	public void setStatus(CaseStatus status) {
		this.status = status;
	}

	public User getReportedBy() {
		return reportedBy;
	}

	public void setReportedBy(User reportedBy) {
		this.reportedBy = reportedBy;
	}

	public User getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(User assignedTo) {
		this.assignedTo = assignedTo;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

	public User getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(User assignedBy) {
		this.assignedBy = assignedBy;
	}

	public CaseType getCaseType() {
		return caseType;
	}

	public void setCaseType(CaseType caseType) {
		this.caseType = caseType;
	}

	public Set<CaseTypeAttributeValue> getCaseTypeAttributeValues() {
		return caseTypeAttributeValues;
	}

	public void setCaseTypeAttributeValues(Set<CaseTypeAttributeValue> caseTypeAttributeValues) {
		this.caseTypeAttributeValues = caseTypeAttributeValues;
	}

	public Set<CaseImage> getCaseImages() {
		return caseImages;
	}

	public void setCaseImages(Set<CaseImage> caseImages) {
		this.caseImages = caseImages;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}
	
	
	
}
