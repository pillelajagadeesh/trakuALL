package com.tresbu.trakeye.service.dto;

public class BatteryReportDTO {
	
	private static final long serialVersionUID = 677752000145846804L;
	public BatteryReportDTO(){
		
	}
	
	public BatteryReportDTO( int batteryValue, Long createTime) {
		super();
		
		this.batteryValue = batteryValue;
		this.createTime = createTime;
		
		
	}
	private Long createTime;
	private int batteryValue;
	

	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public int getBatteryValue() {
		return batteryValue;
	}

	public void setBatteryValue(int batteryValue) {
		this.batteryValue = batteryValue;
	}

	
	
	
	
	
	
	

}
