package com.tresbu.trakeye.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tresbu.trakeye.domain.LocationLog;
import com.tresbu.trakeye.repository.GeofenceRepository;
import com.tresbu.trakeye.repository.LocationLogRepository;
import com.tresbu.trakeye.repository.TrCaseRepository;
import com.tresbu.trakeye.repository.TrServiceRepository;
import com.tresbu.trakeye.repository.UserRepository;
import com.tresbu.trakeye.service.ReportService;
import com.tresbu.trakeye.service.dto.ReportDTO;
import com.tresbu.trakeye.service.mapper.LocationLogMapper;
import com.tresbu.trakeye.service.util.RandomUtil;

@Service
@Transactional
public class ReportServiceImpl implements ReportService{

	@Inject
	UserRepository userRepository;
	
	@Inject
	TrServiceRepository trServiceRepository;
	
	@Inject
	TrCaseRepository trCaseRepository;
	
	@Inject
	LocationLogRepository locationLogRepository;
	
	@Inject
	GeofenceRepository geofenceRepository;
	
	@Inject
	LocationLogMapper locationLogMapper;
	
	@Override
	public ReportDTO getGeofenceReportforGeofence(long fromTime, long toTime, long geofenceId) {
		return geofenceRepository.geofenceReport(fromTime, toTime, geofenceId);
	}

	@Override
	public List<ReportDTO> getGeofenceReportforAll(long fromTime, long toTime, long userId) {
		return geofenceRepository.geofencesReport(fromTime, toTime, userId);
	}

	@Override
	public ReportDTO getUserAgentReportforUser(long fromTime, long toTime, long userId) {
		ReportDTO agentReportDTO = userRepository.userAgentReport(fromTime, toTime, userId);
		return agentReportDTO;
	}

	@Override
	public List<ReportDTO> getUserAgentReportforAll(long fromTime, long toTime,String createdBy) {
		List<ReportDTO> userAgentsReport = userRepository.userAgentsReport(fromTime, toTime, createdBy);
		return userAgentsReport;
	}
	
	
	@Override
	public ReportDTO getUserAgentDetailedReportforUser(long fromTime, long toTime, long userId) {
		ReportDTO agentReportDTO = userRepository.userAgentDetailedReport(fromTime, toTime, userId);
		LinkedList<LocationLog> listLocationPath = locationLogRepository.listLocationPath(userId, fromTime, toTime);
		agentReportDTO.setLocations(locationLogMapper.locationLogsToLatLongDTOs(listLocationPath));
		return agentReportDTO;
	}
	
	@Override
	public ReportDTO getGeofenceDetailedReportforGeofence(long fromTime, long toTime, long geofenceId) {
		ReportDTO geofenceReport = geofenceRepository.geofenceDetailedReport(fromTime, toTime, geofenceId);
		return geofenceReport;
	}
	

}
