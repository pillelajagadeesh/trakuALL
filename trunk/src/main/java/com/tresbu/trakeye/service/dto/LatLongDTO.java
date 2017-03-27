package com.tresbu.trakeye.service.dto;

import java.io.Serializable;

/**
 * A DTO for the LocationLog entity.
 */
public class LatLongDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5292114071982649014L;


	private Double lat;

	private Double lng;

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	
	

}
