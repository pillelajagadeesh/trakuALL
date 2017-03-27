package com.tresbu.trakeye.domain;


import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.tresbu.trakeye.domain.enumeration.CustomerStatus;


@Entity
@Table(name = "trakeye_customer")
public class Customer {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
    @NotNull
    @Column(name = "first_name", nullable = false)
    @Size(max=50)
	private String firstName;
    
    @NotNull
    @Column(name = "last_name", nullable = false)
    @Size(max=50)
	private String lastName;
    
    @NotNull
	@Email
    @Column(name = "email", nullable = false)
	private String email;
	
    @NotNull
    @Column(name = "organization", nullable = false)
    @Size(max=50)
	private String organization;
    
    @NotNull
    @Column(name = "mobile_phone", nullable = false)
    //@Number
	private String mobilePhone;
    
/*    @NotNull
    @Column(name = "gender", nullable = false)
	private String gender;*/

	@Size(min = 2, max = 5)
	@Column(name = "lang_key", length = 5)
	private String langKey;
	
    @NotNull
    @Column(name = "created_date", nullable = false)
    private long createdDate;    

    @NotNull
    private CustomerStatus status;

    @NotNull
    @Column(name = "city", nullable = false)
    @Size(max=50)
	private String city;

    @NotNull
    @Column(name = "state", nullable = false)
    @Size(max=50)
	private String state;
    
    @NotNull
    @Column(name = "country", nullable = false)
    @Size(max=50)
	private String country;
    
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

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

/*	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}*/

	public long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

	public String getLangKey() {
		return langKey;
	}

	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}

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
