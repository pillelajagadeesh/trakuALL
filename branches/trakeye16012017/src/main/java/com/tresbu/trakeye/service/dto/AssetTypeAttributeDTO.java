package com.tresbu.trakeye.service.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class AssetTypeAttributeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	@NotNull
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

	@Override
	public String toString() {
		return "AssetTypeAttributeDTO [id=" + id + ", name=" + name + "]";
	}

}
