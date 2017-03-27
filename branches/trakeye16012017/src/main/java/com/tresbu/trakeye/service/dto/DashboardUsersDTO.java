package com.tresbu.trakeye.service.dto;

import java.io.Serializable;

public class DashboardUsersDTO  implements  Serializable{
	
	private static final long serialVersionUID = 677752000145846804L;
	
	
	public DashboardUsersDTO(String login,Double distance) {
		super();
		this.login = login;
		this.distance = distance;
	}
	
	private String login;
	
	private Double distance;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}
	
	

}
