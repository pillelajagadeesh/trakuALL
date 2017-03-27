package com.tresbu.trakeye.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.tresbu.trakeye.config.Constants;

import io.swagger.annotations.ApiModelProperty;

public class UserAccountUpdateDTO {
	
	private static final long serialVersionUID = 5292114071982649014L;
	
	
	@NotNull
	@Size(max = 50)
	private String firstName;

	@NotNull
	@Size(max = 50)
	private String lastName;

	@NotNull
	@Email
	@Size(min = 5, max = 100)
	private String email;
	
	@NotNull
	//@Pattern(regexp = Constants.LOGIN_REGEX)
	@Size(min = 1, max = 50)
	private String login;

	@ApiModelProperty(required = true, notes="User first name is mandatory and should be string value, ex: John")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@ApiModelProperty(required = true, notes="User last name is mandatory and should be string value, ex: Williams")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@ApiModelProperty(required = true, notes="User email is mandatory and should be vaid email, ex: john.williams@gmail.com")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	@ApiModelProperty(required = true, notes="User login is mandatory and should be string value, ex: johnwilliams")
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	
	
		
	

}
