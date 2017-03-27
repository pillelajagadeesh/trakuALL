package com.tresbu.trakeye.service.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import io.swagger.annotations.ApiModelProperty;

public class CustomerCreateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Size(max = 50)
	private String firstName;
    
	@Size(max = 50)
	private String lastName;

	@Email
	@Size(min = 5, max = 100)
	private String email;
    
	@Size(max = 50)
	private String organization;

	@NotNull
	@Size(min = 10, max = 100)
	private String mobilePhone;

	@Size(min = 3, max = 50)
	private String city;
	
	@Size(min = 3, max = 50)
	private String state;
	
	@Size(min = 3, max = 50)	
	private String country;
	
	@Size(min = 2, max = 5)
	private String langKey;

	@ApiModelProperty(required = true, notes="Customer first name is mandatory and should be string value, ex: Steve")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@ApiModelProperty(required = true, notes="Customer last name is mandatory and should be string value, ex: Gates")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@ApiModelProperty(required = true, notes="Custome email is mandatory and should be string value, ex: xyz@gmail.com")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ApiModelProperty(required = true, notes="Organization is mandatory and should be string value, ex: Tresbu")
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@ApiModelProperty(required = true, notes="Customer mobile number is mandatory and should be string value, ex: 9999999999")
	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	@ApiModelProperty(required = true, notes="Not required to give language key")
	public String getLangKey() {
		return langKey;
	}

	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	
}
