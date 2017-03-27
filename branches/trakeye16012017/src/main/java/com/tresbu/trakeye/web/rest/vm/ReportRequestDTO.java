package com.tresbu.trakeye.web.rest.vm;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * View Model object for Report Request params
 */
public class ReportRequestDTO implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 2430760235989682264L;

	/*
     * if id is zero will fetch all 
     */
    private long id;

    @NotNull
    private long fromTime;
    
    @NotNull
    private long toTime;
  
    @NotEmpty
    @NotNull
    private String reportType;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getFromTime() {
		return fromTime;
	}

	public void setFromTime(long fromTime) {
		this.fromTime = fromTime;
	}

	public long getToTime() {
		return toTime;
	}

	public void setToTime(long toTime) {
		this.toTime = toTime;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
    
 
    
}
