package com.tresbu.trakeye.service.dto;

import java.io.Serializable;

public class DistanceReportDTO  implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3775975113797895452L;
	public DistanceReportDTO(){
		
	}
	
	public DistanceReportDTO(Double distance, String date) {
		super();
		this.distance = distance;
		this.date = date;
	}

	private Double distance;
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	private String date;
	
	
	

}
