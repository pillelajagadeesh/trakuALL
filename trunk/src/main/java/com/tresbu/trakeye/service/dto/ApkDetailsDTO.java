package com.tresbu.trakeye.service.dto;

import java.io.Serializable;

public class ApkDetailsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private String fileName;
	private String version;
	private UserIdDTO user;

	public UserIdDTO getUser() {
		return user;
	}

	public void setUser(UserIdDTO user) {
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
