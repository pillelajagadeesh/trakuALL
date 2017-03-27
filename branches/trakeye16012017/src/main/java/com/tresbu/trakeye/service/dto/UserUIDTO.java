package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

import com.tresbu.trakeye.domain.Authority;
import com.tresbu.trakeye.domain.Geofence;
import com.tresbu.trakeye.domain.TrakeyeTypeAttributeValue;
import com.tresbu.trakeye.domain.User;
public class UserUIDTO implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 7442359188227402786L;
	
	public UserUIDTO() {
	
	}
	
	public UserUIDTO(User user) {
		this.id = user.getId();
		this.login = user.getLogin();
		this.langKey = user.getLangKey();
		this.authorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
		this.activated=user.getActivated();
		this.geofences = user.getGeofences().stream().map(Geofence ::getName).collect(Collectors.toSet());
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email =user.getEmail();
		this.fromTime =user.getFromTime();
		this.toTime =user.getToTime();
		this.phone = user.getPhone();
		this.imei = user.getImei();
		this.operatingSystem = user.getOperatingSystem();
		this.applicationVersion = user.getApplicationVersion();
		this.resetKey=user.getResetKey();
		this.gpsStatus=user.getGpsStatus();
		this.createdBy=user.getCreatedBy();
		this.userImage = user.getUserImage();
		if(user.getTrakeyeType()!=null){
			TrakeyeTypeDTO trakeyeTypeDTO = new TrakeyeTypeDTO();
			trakeyeTypeDTO.setId(user.getTrakeyeType().getId());
			trakeyeTypeDTO.setName(user.getTrakeyeType().getName());
			
			this.trakeyeType = trakeyeTypeDTO;
		}
	
	}
	public UserUIDTO(User user,TrakeyeTypeDTO trakeyeTypeDTO) {
		
		this(user);
		this.trakeyeType=trakeyeTypeDTO;
		this.trakeyeTypeAttributeValues = user.getTrakeyeTypeAttributeValues();
		this.fromTime =user.getFromTime();
		this.toTime =user.getToTime();
		this.phone = user.getPhone();
		this.imei = user.getImei();
		this.operatingSystem = user.getOperatingSystem();
		this.applicationVersion = user.getApplicationVersion();
		this.gpsStatus = user.getGpsStatus();
		this.resetKey=user.getResetKey();
		this.createdBy=user.getCreatedBy();
		this.userImage = user.getUserImage();
	}
	
	
	private String login;
	
	private Long id;

	private String firstName;

	private String lastName;

	private String email;

	private boolean activated = false;

	private String langKey;

	private Set<String> authorities;

	private Set<String> geofences;
	
	private TrakeyeTypeDTO trakeyeType;
	
	private int fromTime;

	private int toTime;
	
	private String phone;
	
	private String imei;
	
	private String operatingSystem;
	
	private String applicationVersion;
	
	private String resetKey;
	
	private String createdBy;
	
	private boolean gpsStatus = true;
	
	private byte[] userImage;
	
	private Set<TrakeyeTypeAttributeValue> trakeyeTypeAttributeValues;

	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getResetKey() {
		return resetKey;
	}

	public void setResetKey(String resetKey) {
		this.resetKey = resetKey;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public String getLangKey() {
		return langKey;
	}

	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}

	public Set<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<String> authorities) {
		this.authorities = authorities;
	}

	public Set<String> getGeofences() {
		return geofences;
	}

	public void setGeofences(Set<String> geofences) {
		this.geofences = geofences;
	}

	public Set<TrakeyeTypeAttributeValue> getTrakeyeTypeAttributeValues() {
		return trakeyeTypeAttributeValues;
	}

	public void setTrakeyeTypeAttributeValues(Set<TrakeyeTypeAttributeValue> trakeyeTypeAttributeValues) {
		this.trakeyeTypeAttributeValues = trakeyeTypeAttributeValues;
	}

	public TrakeyeTypeDTO getTrakeyeType() {
		return trakeyeType;
	}

	public void setTrakeyeType(TrakeyeTypeDTO trakeyeType) {
		this.trakeyeType = trakeyeType;
	}

	public int getFromTime() {
		return fromTime;
	}

	public void setFromTime(int fromTime) {
		this.fromTime = fromTime;
	}

	public int getToTime() {
		return toTime;
	}

	public void setToTime(int toTime) {
		this.toTime = toTime;
	}
	
	

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	

	
	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}
	
	

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getApplicationVersion() {
		return applicationVersion;
	}

	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}
	
	

	public boolean isGpsStatus() {
		return gpsStatus;
	}

	public void setGpsStatus(boolean gpsStatus) {
		this.gpsStatus = gpsStatus;
	}
	
	

	public byte[] getUserImage() {
		return userImage;
	}

	public void setUserImage(byte[] userImage) {
		this.userImage = userImage;
	}

	@Override
	public String toString() {
		return "UserUIDTO [login=" + login + ", id=" + id + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", activated=" + activated + ", langKey=" + langKey + ", authorities="
				+ authorities + ", geofences=" + geofences + ", trakeyeType=" + trakeyeType + ", fromTime=" + fromTime
				+ ", toTime=" + toTime + ", trakeyeTypeAttributeValues=" + trakeyeTypeAttributeValues + ", phone=" + phone + ", imei=" + imei
				+ ", operatingSystem=" + operatingSystem 
				+ ", applicationVersion=" + applicationVersion 
				+ ", gpsStatus=" + gpsStatus +"]";
	}

	
	
	
}
