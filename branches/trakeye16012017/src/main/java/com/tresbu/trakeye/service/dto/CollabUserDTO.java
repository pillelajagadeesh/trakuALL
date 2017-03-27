package com.tresbu.trakeye.service.dto;

import org.hibernate.validator.constraints.Email;

import com.tresbu.trakeye.domain.Authority;
import com.tresbu.trakeye.domain.User;

import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class CollabUserDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3225653882583048539L;
	public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int PASSWORD_MAX_LENGTH = 100;

    private Long id;

    private ZonedDateTime createdDate;
    
    private String createdBy;

    private String lastModifiedBy;

    private ZonedDateTime lastModifiedDate;
    
    private String resetKey;
    private String resetDate;

    @NotNull
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;
	
	 @NotNull
	    @Pattern(regexp = "^[a-zA-Z0-9]*$")
	    @Size(min = 1, max = 50)
	    private String login;

	    @Size(max = 50)
	    private String firstName;

	    @Size(max = 50)
	    private String lastName;

	    @Email
	    @Size(min = 5, max = 100)
	    private String email;

	    private boolean activated = false;

	    @Size(min = 2, max = 5)
	    private String langKey;

	    private Set<String> authorities;

	    public CollabUserDTO() {
	    }

	    

	   

	    public Long getId() {
			return id;
		}





		public void setId(Long id) {
			this.id = id;
		}





		public ZonedDateTime getCreatedDate() {
			return createdDate;
		}





		public void setCreatedDate(ZonedDateTime createdDate) {
			this.createdDate = createdDate;
		}





		public String getCreatedBy() {
			return createdBy;
		}





		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}





		public String getLastModifiedBy() {
			return lastModifiedBy;
		}





		public void setLastModifiedBy(String lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
		}





		public ZonedDateTime getLastModifiedDate() {
			return lastModifiedDate;
		}





		public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
			this.lastModifiedDate = lastModifiedDate;
		}





		public String getResetKey() {
			return resetKey;
		}





		public void setResetKey(String resetKey) {
			this.resetKey = resetKey;
		}





		public String getResetDate() {
			return resetDate;
		}





		public void setResetDate(String resetDate) {
			this.resetDate = resetDate;
		}





		public String getPassword() {
			return password;
		}





		public void setPassword(String password) {
			this.password = password;
		}





		public String getLogin() {
			return login;
		}





		public void setLogin(String login) {
			this.login = login;
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





		@Override
	    public String toString() {
	        return "CollabUserDTO{" +
	            "login='" + login + '\'' +
	            ", firstName='" + firstName + '\'' +
	            ", lastName='" + lastName + '\'' +
	            ", email='" + email + '\'' +
	            ", activated=" + activated +
	            ", langKey='" + langKey + '\'' +
	            ", authorities=" + authorities +
	            "}";
	    }

}
