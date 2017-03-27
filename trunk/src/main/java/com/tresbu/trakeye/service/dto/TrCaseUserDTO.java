package com.tresbu.trakeye.service.dto;

import java.io.Serializable;

import com.tresbu.trakeye.domain.Geofence;
import com.tresbu.trakeye.domain.enumeration.CasePriority;

public class TrCaseUserDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Double pinLat;
	private Double pinLong;
	private CasePriority priority;
	private Geofence geofence;
	private String address;
	private String userCase;
	private String status;
	private Long userid;
	private String login;
	private String reportedByUser;
	private String assignedToUser;
	private Long id;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public CasePriority getPriority() {
		return priority;
	}

	public void setPriority(CasePriority priority) {
		this.priority = priority;
	}

	public Geofence getGeofence() {
		return geofence;
	}

	public void setGeofence(Geofence geofence) {
		this.geofence = geofence;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUserCase() {
		return userCase;
	}

	public void setUserCase(String userCase) {
		this.userCase = userCase;
	}

}
