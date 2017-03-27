package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;



public class DashboardDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String,BigInteger> notifications;
	private Map<String,BigInteger> users;
	private Map<String,BigInteger> serviceType;
	private Map<String,BigInteger> caseType;
	private Map<String,BigInteger> casePriority;
	private List<Map<String, Object>> geofences;
	public Map<String, BigInteger> getNotifications() {
		return notifications;
	}
	public void setNotifications(Map<String, BigInteger> notifications) {
		this.notifications = notifications;
	}
	public Map<String, BigInteger> getUsers() {
		return users;
	}
	public void setUsers(Map<String, BigInteger> users) {
		this.users = users;
	}
	public Map<String, BigInteger> getServiceType() {
		return serviceType;
	}
	public void setServiceType(Map<String, BigInteger> servicesType) {
		this.serviceType = servicesType;
	}
	public Map<String, BigInteger> getCaseType() {
		return caseType;
	}
	public void setCaseType(Map<String, BigInteger> caseType) {
		this.caseType = caseType;
	}
	public List<Map<String, Object>> getGeofences() {
		return geofences;
	}
	public void setGeofences(List<Map<String, Object>> geofencesList) {
		this.geofences = geofencesList;
	}
	public Map<String, BigInteger> getCasePriority() {
		return casePriority;
	}
	public void setCasePriority(Map<String, BigInteger> casePriority) {
		this.casePriority = casePriority;
	}
	
	
	
	

	
	
}
