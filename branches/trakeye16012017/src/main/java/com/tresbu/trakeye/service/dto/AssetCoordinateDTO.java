package com.tresbu.trakeye.service.dto;

import java.io.Serializable;

public class AssetCoordinateDTO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7078524982093370201L;
	
	private Long id;
	private Double latitude;
	private Double longitude;
	//Asset will be generated after creating co-ordinate this property will put app in infinite loop
	//private AssetDTO asset;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/*public AssetDTO getAsset() {
		return asset;
	}

	public void setAsset(AssetDTO asset) {
		this.asset = asset;
	}*/

	
	@Override
	public String toString() {
		return "AssetCoordinateDTO [id=" + id + ", latitude=" + latitude + ", longitude=" + longitude +  "]";
	}

}
