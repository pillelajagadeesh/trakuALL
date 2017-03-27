package com.tresbu.trakeye.service.dto;

import java.io.Serializable;

public class NameCountDTO implements Serializable{
public NameCountDTO(String name, long count) {
	this.name=name;
	this.count = count;
}
	private String name;
	private long count;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	
	
	
}
