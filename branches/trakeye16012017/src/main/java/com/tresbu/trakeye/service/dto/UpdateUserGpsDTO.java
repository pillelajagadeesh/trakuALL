package com.tresbu.trakeye.service.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

public class UpdateUserGpsDTO {
	
	private static final long serialVersionUID = 5292114071982649014L;
	
	
	@NotNull
	@ApiModelProperty(required = true, value="Mandatory")
	private String login;
	
	private boolean gpsStatus;

	
	@ApiModelProperty(required= true, notes="User login name is mandatory and should be string value")
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	
	@ApiModelProperty(notes="GPS status is not  mandatory and should be boolean value")
	public boolean getGpsStatus() {
		return gpsStatus;
	}

	public void setGpsStatus(boolean gpsStatus) {
		this.gpsStatus = gpsStatus;
	}

	@Override
	public String toString() {
		return "UpdateUserGPSDTO [login=" + login + ", gpsStatus=" + gpsStatus + "]";
	}
	
	
	
	
	
	

}
