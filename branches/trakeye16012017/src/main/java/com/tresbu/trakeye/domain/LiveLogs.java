package com.tresbu.trakeye.domain;

import java.io.Serializable;


public class LiveLogs implements  Serializable{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 677752000145846804L;
	
	public LiveLogs( Double latitude, Double longitude, String address,String login, String status,Long userid, byte[] userImage,String phone, int batteryPercentage ) {
		super();
		this.userid = userid;
		this.login = login;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.status = status;
		this.userImage = userImage;
		this.phone = phone;
		this.batteryPercentage= batteryPercentage;
	}
	Long userid;
	
	
	
	
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	String login;
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	private Double latitude;
	 public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private Double longitude;
	 private String address;
	 private String status;
	 
	 
	 private String phone;
	 private int batteryPercentage;
	 
	 private byte[] userImage;

	public byte[] getUserImage() {
		return userImage;
	}
	public void setUserImage(byte[] userImage) {
		this.userImage = userImage;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getBatteryPercentage() {
		return batteryPercentage;
	}
	public void setBatteryPercentage(int batteryPercentage) {
		this.batteryPercentage = batteryPercentage;
	}
	
	 
	 
	 
	 
	 
}





