package com.tresbu.trakeye.service.dto;

import java.io.Serializable;

public class CollabAccountDTO implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6354143637693137469L;

	
    private String firstName;

   
    private String lastName;

    private String email;
    
    private String login;

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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
    
    
    
    
    

}
