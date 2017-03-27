package com.tresbu.trakeye.web.rest.vm;

import com.tresbu.trakeye.config.Constants;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * View Model object for storing a user's credentials.
 */
public class LoginVM {

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @NotNull
    @Size(min = 1, max = 50)
    private String username;

    @NotNull
    @Size(min = ManagedUserVM.PASSWORD_MIN_LENGTH, max = ManagedUserVM.PASSWORD_MAX_LENGTH)
    private String password;
    
    private String imei;

    private Boolean rememberMe;
    
    private String phoneNo;
    
    private String operatingSystem;
    
    private String applicationVersion;
    
    private boolean gpsStatus= true;
    
    private String fcmToken;
    
    @ApiModelProperty(required= true, notes="FcmToken is not mandatory and should be string value")
    public String getFcmToken() {
		return fcmToken;
	}

	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

	@ApiModelProperty(required= true, notes="User name is mandatory and should be string value")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @ApiModelProperty(required= true, notes="Password is mandatory and should be string value")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @ApiModelProperty(notes="Remember me is not mandatory and should be boolean value")
    public Boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
    
    
    @ApiModelProperty(notes="IMEI is not mandatory and should be string value. If IMEI is given , then gpsStatus should also be given")
    public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}
	
	

	
	@ApiModelProperty(notes="Operating system is not mandatory and should be string value. ex: android or ios")
	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	@ApiModelProperty(notes="Application version is not mandatory and should be string value. This is version no of android or ios application installed by user")
	public String getApplicationVersion() {
		return applicationVersion;
	}

	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	@ApiModelProperty(notes="Phone number is not mandatory.")
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	
	@ApiModelProperty(notes="GPS status is not mandatory and should be booean value. If IMEI is given , then gpsStatus should also be given")
	public boolean getGpsStatus() {
		return gpsStatus;
	}

	public void setGpsStatus(boolean gpsStatus) {
		this.gpsStatus = gpsStatus;
	}

	@Override
    public String toString() {
        return "LoginVM{" +
            //"password='" + password + '\'' +
            ", username='" + username + '\'' +
            ", rememberMe=" + rememberMe +
            ", imei=" + imei +
            ", phoneNo=" + phoneNo +
            ", operatingSystem=" + operatingSystem +
            ", applicationVersion=" + applicationVersion +
            ", gpsStatus=" + gpsStatus +
            '}';
    }
}
