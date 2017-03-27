package com.tresbu.trakeye.service.dto;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.tresbu.trakeye.domain.Authority;
import com.tresbu.trakeye.domain.Geofence;
import com.tresbu.trakeye.domain.TrakeyeType;
import com.tresbu.trakeye.domain.TrakeyeTypeAttributeValue;
import com.tresbu.trakeye.domain.User;

import io.swagger.annotations.ApiModelProperty;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

	@NotNull
	// @Pattern(regexp = Constants.LOGIN_REGEX)
	@Size(min = 1, max = 50)
	private String login;
	
	private Long id;

	@Size(max = 50)
	private String firstName;

	@Size(max = 50)
	private String lastName;

	@Email
	@Size(min = 5, max = 100)
	private String email;
	
	 @Size(min = 10, max = 100)
	    private String phone;

	 @Size(min = 15, max = 100)
	    private String imei;
	 
	 @Size(max = 100)
	    private String operatingSystem;
	 
	 @Size(max = 100)
	    private String applicationVersion;
	 
	private boolean activated = false;

	@Size(min = 2, max = 5)
	private String langKey;

	private Set<String> authorities;

	@NotNull
	private Set<String> geofences;

	@NotNull
	private TrakeyeType trakeyeType;
	
	private byte[] userImage;
	
	private Set<TrakeyeTypeAttributeValue> trakeyeTypeAttributeValues;

	public TrakeyeType getTrakeyeType() {
		return trakeyeType;
	}

	public void setTrakeyeType(TrakeyeType trakeyeType) {
		this.trakeyeType = trakeyeType;
	}

	public void setGeofences(Set<String> geofences) {
		this.geofences = geofences;
	}

	private int fromTime;

	private int toTime;
	
	
	@ApiModelProperty(notes="User image is not mandatory. It should be byte array.")
	public byte[] getUserImage() {
		return userImage;
	}

	public void setUserImage(byte[] userImage) {
		this.userImage = userImage;
	}

	public UserDTO() {
	}

	public UserDTO(User user) {
		this(user.getLogin(), user.getFirstName(), user.getLastName(), user.getEmail(),
				user.getActivated(), user.getLangKey(),
				user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()),
				user.getGeofences().stream().map(Geofence::getName).collect(Collectors.toSet()), user.getTrakeyeType(),
				user.getTrakeyeTypeAttributeValues(), user.getFromTime(), user.getToTime(), user.getPhone(),user.getImei(), user.getOperatingSystem(), user.getApplicationVersion(), 
				user.getUserImage());
	}

	public UserDTO(String login, String firstName, String lastName, String email,
			boolean activated, String langKey, Set<String> authorities, Set<String> geofences, TrakeyeType trakeyeType,
			Set<TrakeyeTypeAttributeValue> trakeyeTypeAttributeValues, int fromTime, int toTime, String phone, String imei, String operatingSystem, String applicationVersion, 
			byte[] userImage) {
		super();
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.activated = activated;
		this.langKey = langKey;
		this.authorities = authorities;
		this.geofences = geofences;
		this.trakeyeType = trakeyeType;
		this.fromTime = fromTime;
		this.trakeyeTypeAttributeValues = trakeyeTypeAttributeValues;
		this.toTime = toTime;
		this.phone = phone;
		this.imei= imei;
		this.operatingSystem = operatingSystem;
		this.applicationVersion = applicationVersion;
		this.userImage = userImage;
				
	}

	@ApiModelProperty(required=true, notes="User name is mandatory while creating customer/agent and should be string value. ex: johnwilliams")
	public String getLogin() {
		return login;
	}

	@ApiModelProperty(required=true, notes="First name is mandatory while creating customer/agent and should be string value. ex: john")
	public String getFirstName() {
		return firstName;
	}

	@ApiModelProperty(required=true, notes="Last name is mandatory while creating customer/agent and should be string value. ex: williams")
	public String getLastName() {
		return lastName;
	}

	@ApiModelProperty(required=true, notes="EMail is mandatory while creating customer/agent and should be string value. ex: johnwilliams@gmail.com")
	public String getEmail() {
		return email;
	}

	@ApiModelProperty(notes="User activated is not mandatory while creating customer/agent . But required while updating agent and should be boolean value.")
	public boolean isActivated() {
		return activated;
	}

	@ApiModelProperty(notes="Language key is not mandatory while creating customer/agent and should be string value.")
	public String getLangKey() {
		return langKey;
	}

	@ApiModelProperty(notes="User authorities is not mandatory while creating customer/agent.")
	public Set<String> getAuthorities() {
		return authorities;
	}

	
	@ApiModelProperty(notes="User geofence is mandatory while creating agent. But not required while creating customer")
	public Set<String> getGeofences() {
		return geofences;
	}

	@ApiModelProperty(notes="User duty start time is mandatory while creating agent. But not required while creating customer. It should be ineteger value from 0 to 23")
	public int getFromTime() {
		return fromTime;
	}

	public void setFromTime(int fromTime) {
		this.fromTime = fromTime;
	}

	@ApiModelProperty(notes="User duty end time is mandatory while creating agent. But not required while creating customer. It should be ineteger value from 0 to 23")
	public int getToTime() {
		return toTime;
	}

	public void setToTime(int toTime) {
		this.toTime = toTime;
	}

	@ApiModelProperty(notes="User trakeye type attribues is not mandatory while creating agent. But not required while creating customer.")
	public Set<TrakeyeTypeAttributeValue> getTrakeyeTypeAttributeValues() {
		return trakeyeTypeAttributeValues;
	}

	public void setTrakeyeTypeAttributeValues(Set<TrakeyeTypeAttributeValue> trakeyeTypeAttributeValues) {
		this.trakeyeTypeAttributeValues = trakeyeTypeAttributeValues;
	}
	
	
	@ApiModelProperty(required= true,notes="Phone number is mandatory while creating customer/agent")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@ApiModelProperty(notes="User imei is not mandatory while creating agent. But not required while creating customer.")
	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}
	
	
	@ApiModelProperty(notes="Not required while creating customer/agent.")
	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	@ApiModelProperty(notes="Not required while creating customer/agent.")
	public String getApplicationVersion() {
		return applicationVersion;
	}

	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	@Override
	public String toString() {
		return "UserDTO [login=" + login + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+", activated=" + activated + ", langKey=" + langKey
				+ ", authorities=" + authorities + ", geofences=" + geofences + ", trakeyeType=" + trakeyeType
				+ ", fromTime=" + fromTime + ", toTime=" + toTime + ", phone=" + phone + ", imei=" + imei 
				+ ", operatingSystem=" + operatingSystem
				+ ", applicationVersion=" + applicationVersion +"]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
