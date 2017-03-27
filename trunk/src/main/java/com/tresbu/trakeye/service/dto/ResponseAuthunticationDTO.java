package com.tresbu.trakeye.service.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseAuthunticationDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @JsonProperty("id_token")
	private String idToken;

	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}
		
}
