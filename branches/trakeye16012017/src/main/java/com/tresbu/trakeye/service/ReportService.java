package com.tresbu.trakeye.service;

import java.util.List;

import com.tresbu.trakeye.service.dto.ReportDTO;

/**
 * Service Interface for managing Report.
 */
public interface ReportService {
    
    public ReportDTO getGeofenceReportforGeofence(long fromTime, long toTime,long geofenceId);
    
    public List<ReportDTO> getGeofenceReportforAll(long fromTime, long toTime,long userId);
    
	public ReportDTO getUserAgentReportforUser(long fromTime, long toTime,long userId);
	
	public List<ReportDTO> getUserAgentReportforAll(long fromTime, long toTime,String createdBy);
	
	public ReportDTO getUserAgentDetailedReportforUser(long fromTime, long toTime,long userId);

	public ReportDTO getGeofenceDetailedReportforGeofence(long fromTime, long toTime, long geofenceId);


	
}
