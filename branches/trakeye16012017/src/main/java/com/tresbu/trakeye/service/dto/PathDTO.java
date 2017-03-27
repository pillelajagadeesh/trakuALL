package com.tresbu.trakeye.service.dto;

public class PathDTO {
	
	
	public PathDTO(Double lat, Double lng, String address, long createdDateTime, long updatedDateTime,
			int batteryPercentage) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.address = address;
		this.createdDateTime = createdDateTime;
		this.updatedDateTime = updatedDateTime;
		this.batteryPercentage = batteryPercentage;
	}

	private Double lat;
	
	private Double lng;
	
	private String address;
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(long createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public long getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(long updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	public int getBatteryPercentage() {
		return batteryPercentage;
	}

	public void setBatteryPercentage(int batteryPercentage) {
		this.batteryPercentage = batteryPercentage;
	}

	private long createdDateTime;
	
	private long updatedDateTime;
	
	private int batteryPercentage;

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}
	
	
	

}
