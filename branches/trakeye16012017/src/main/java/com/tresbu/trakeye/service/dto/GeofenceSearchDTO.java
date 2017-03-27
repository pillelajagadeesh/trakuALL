package com.tresbu.trakeye.service.dto;

import java.io.Serializable;

public class GeofenceSearchDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8192662894186420075L;
	private Long id;
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



}
