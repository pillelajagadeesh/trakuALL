package com.tresbu.trakeye.service.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.tresbu.trakeye.domain.enumeration.LogSource;

import io.swagger.annotations.ApiModelProperty;

public class LocationLogUpdateDTO {
	
	private static final long serialVersionUID = 5292114071982649014L;

	@NotNull
	@ApiModelProperty(required = true, value="Mandatory")
	private Long id;
	
	
	private int batteryPercentage;
	
	//private UserIdDTO user;
	
	@NotNull
	@ApiModelProperty(required = true, value="Mandatory")
	private LogSource logSource;
	
	@NotNull
	@ApiModelProperty(required = true, value="Mandatory")
	private long createdDateTime;
	
	
	@ApiModelProperty(required = true, notes="Location log id is mandatory and should be a long value. ex:  1108")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ApiModelProperty(required = true, notes="Battery percentage is mandatory and should be integer value. ex: 20")
	public int getBatteryPercentage() {
		return batteryPercentage;
	}

	public void setBatteryPercentage(int batteryPercentage) {
		this.batteryPercentage = batteryPercentage;
	}
	
	
/*
	public UserIdDTO getUser() {
		return user;
	}

	public void setUser(UserIdDTO user) {
		this.user = user;
	}
*/
	
	@ApiModelProperty(required = true, notes="Log source is mandatory and should be a string value. ex:  GPS or NP or NETWORK")
	public LogSource getLogSource() {
		return logSource;
	}

	public void setLogSource(LogSource logSource) {
		this.logSource = logSource;
	}

	@ApiModelProperty(required = true, notes="Created date time is mandatory and should be a long value. ex:  1480341024660")
	public long getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(long createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	@Override
	public String toString() {
		return "LocationLogUpdateDTO [id=" + id + ", batteryPercentage=" + batteryPercentage + ", logSource="
				+ logSource + ", createdDateTime=" + createdDateTime + "]";
	}



	

}
