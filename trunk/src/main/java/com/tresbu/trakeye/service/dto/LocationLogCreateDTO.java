package com.tresbu.trakeye.service.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.tresbu.trakeye.domain.enumeration.LogSource;

import io.swagger.annotations.ApiModelProperty;

public class LocationLogCreateDTO {
	
	private static final long serialVersionUID = 5292114071982649014L;

	//private Long id;

	@NotNull
	@ApiModelProperty(required = true, value="Mandatory")
	private Double latitude;

	@NotNull
	@ApiModelProperty(required = true, value="Mandatory")
	private Double longitude;
	
	private int batteryPercentage;

	@NotNull
	@ApiModelProperty(required = true, value="Mandatory")
	private String address;

	@NotNull
	@ApiModelProperty(required = true, value="Mandatory")
	private LogSource logSource;
	
	@NotNull
	@ApiModelProperty(required = true, value="Mandatory")
	private long createdDateTime;
	
	
	
	private Long userId;
	
	
	@ApiModelProperty(required = true, notes="Latitude is mandatory and should be a double value. ex:  12.928174")
	public Double getLatitude() {
		return latitude;
	}

	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@ApiModelProperty(required = true,notes="Longitude is mandatory and should be a double value. ex:  77.584349")
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@ApiModelProperty(required = true, notes="Battery percentage is mandatory and should be a numeric value. ex:  20")
	public int getBatteryPercentage() {
		return batteryPercentage;
	}

	public void setBatteryPercentage(int batteryPercentage) {
		this.batteryPercentage = batteryPercentage;
	}

	@ApiModelProperty(required = true, notes="Address is mandatory and should be a string value. ex:  4th cross, jayanagar 4th block Bangalore")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@ApiModelProperty(required = true, notes="Log source is mandatory and should be a String value. ex:  GPS or NP or NETWORK")
	public LogSource getLogSource() {
		return logSource;
	}

	public void setLogSource(LogSource logSource) {
		this.logSource = logSource;
	}

	@ApiModelProperty(required = true, notes="Created date time is mandatory and should be a long utc value. ex:  1480341024660")
	public long getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(long createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	
	
	@ApiModelProperty(notes="Not required to give this value")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	

	@Override
	public String toString() {
		return "LocationLogCreateDTO [latitude=" + latitude + ", longitude=" + longitude + ", batteryPercentage="
				+ batteryPercentage + ", address=" + address + ", logSource=" + logSource + ", createdDateTime="
				+ createdDateTime + ", userId=" + userId + "]";
	}

	

}
