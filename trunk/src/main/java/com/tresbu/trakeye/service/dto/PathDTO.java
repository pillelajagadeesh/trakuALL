package com.tresbu.trakeye.service.dto;

public class PathDTO {
	
	public PathDTO(Double latitude, Double longitude) {
		this.lat=latitude;
		this.lng=longitude;
	}
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
