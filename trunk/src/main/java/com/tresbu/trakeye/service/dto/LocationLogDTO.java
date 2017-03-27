package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.tresbu.trakeye.domain.enumeration.LogSource;

/**
 * A DTO for the LocationLog entity.
 */
public class LocationLogDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5292114071982649014L;

	private Long id;

	@NotNull
	private Double latitude;

	@NotNull
	private Double longitude;
	
	
	private Double distanceTravelled;
	
	private int batteryPercentage;

	private String address;

	@NotNull
	private LogSource logSource;
	
	private long createdDateTime;
	
	private long updatedDateTime;

	
	private String userName;

	private Long userId;

	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
	
	

	public Double getDistanceTravelled() {
		return distanceTravelled;
	}

	public void setDistanceTravelled(Double distanceTravelled) {
		this.distanceTravelled = distanceTravelled;
	}
	
	

	public int getBatteryPercentage() {
		return batteryPercentage;
	}

	public void setBatteryPercentage(int batteryPercentage) {
		this.batteryPercentage = batteryPercentage;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LogSource getLogSource() {
		return logSource;
	}

	public void setLogSource(LogSource logSource) {
		this.logSource = logSource;
	}



	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		LocationLogDTO locationLogDTO = (LocationLogDTO) o;

		if (!Objects.equals(id, locationLogDTO.id))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "LocationLogDTO [id=" + id + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", distanceTravelled=" + distanceTravelled + ", batteryPercentage=" + batteryPercentage + ", address="
				+ address + ", logSource=" + logSource + ", createdDateTime=" + createdDateTime + ", updatedDateTime="
				+ updatedDateTime + ", userName=" + userName + ", userId=" + userId + "]";
	}
	
	

	
}
