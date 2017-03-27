package com.tresbu.trakeye.service.dto;

import java.io.Serializable;

public class UserIdDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2267211056758639604L;
	private Long id;
	private String login;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

}
