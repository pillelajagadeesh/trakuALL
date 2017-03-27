package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;



public class DashboardDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String,Long> notifications;
	private Map<String,Long> users;
	private Map<String,Long> serviceType;
	private Map<String,Long> caseType;
	private Map<String,Long> casePriority;
	private List<Map<String, Object>> geofences;
	public Map<String, Long> getNotifications() {
		return notifications;
	}
	public void setNotifications(Map<String, Long> notifications) {
		this.notifications = notifications;
	}
	public Map<String, Long> getUsers() {
		return users;
	}
	public void setUsers(Map<String, Long> users) {
		this.users = users;
	}
	public Map<String, Long> getServiceType() {
		return serviceType;
	}
	public void setServiceType(Map<String, Long> servicesType) {
		this.serviceType = servicesType;
	}
	public Map<String, Long> getCaseType() {
		return caseType;
	}
	public void setCaseType(Map<String, Long> caseType) {
		this.caseType = caseType;
	}
	public List<Map<String, Object>> getGeofences() {
		return geofences;
	}
	public void setGeofences(List<Map<String, Object>> geofencesList) {
		this.geofences = geofencesList;
	}
	public Map<String, Long> getCasePriority() {
		return casePriority;
	}
	public void setCasePriority(Map<String, Long> casePriority) {
		this.casePriority = casePriority;
	}
	
	
	
	

	
	
}
