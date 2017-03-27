package com.tresbu.trakeye.service.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.tresbu.trakeye.domain.enumeration.CustomerStatus;

import io.swagger.annotations.ApiModelProperty;

public class CustomerDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	@Size(max = 50)
	private String firstName;
    
	@Size(max = 50)
	private String lastName;

	@Email
	@Size(min = 5, max = 100)
	private String email;
    
	@Size(max = 50)
	private String organization;

	@Size(min = 10, max = 100)
	//@Number
	private String mobilePhone;
    
	@Size(min = 2, max = 5)
	private String langKey;
	
	@Size(min = 3, max = 50)
	private String city;
	
	@Size(min = 3, max = 50)
	private String state;
	
	@Size(min = 3, max = 50)
	private String country;
	
    private long createdDate;

    private CustomerStatus status;

    @ApiModelProperty(notes="Not requiredto give id while creating customer. And id is mandatory while updating and deleting customer")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

    @ApiModelProperty(notes="Not requiredto give created date time")
	public long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

	@ApiModelProperty(required = true, notes="Not required to give language key")
	public String getLangKey() {
		return langKey;
	}

	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}

	@ApiModelProperty(required = true, notes="Not required to give status")
	public CustomerStatus getStatus() {
		return status;
	}

	public void setStatus(CustomerStatus status) {
		this.status = status;
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
