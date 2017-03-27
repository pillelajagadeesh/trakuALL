package com.tresbu.trakeye.service.impl;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tresbu.trakeye.domain.Geofence;
import com.tresbu.trakeye.domain.enumeration.CasePriority;
import com.tresbu.trakeye.domain.enumeration.CaseStatus;
import com.tresbu.trakeye.domain.enumeration.NotificationStatus;
import com.tresbu.trakeye.domain.enumeration.ServiceStatus;
import com.tresbu.trakeye.repository.DashboardRepository;
import com.tresbu.trakeye.repository.GeofenceRepository;
import com.tresbu.trakeye.service.DashboardService;
import com.tresbu.trakeye.service.dto.DashboardDTO;
import com.tresbu.trakeye.service.dto.DashboardUsersDTO;

@Service
@Transactional
public class DashboardServiceImpl implements DashboardService{

	
	@Inject
	DashboardRepository dashboardRepository;
	
	@Inject
	GeofenceRepository geofenceRepository;
	
	@Override
	public DashboardDTO getGeofenceChartData() {
		
		List<Geofence> findByUserIsCurrentUser = geofenceRepository.findByUserIsCurrentUser();
		 List<Map<String, Object>> geofencesList=new LinkedList<>();
		for (Geofence geofence : findByUserIsCurrentUser) {
			List<Object[]> usersCountByStatus = dashboardRepository.getUsersCountByStatusForGeofence(Instant.now().minusMillis(TimeUnit.MINUTES.toMillis(5)).toEpochMilli(), Instant.now().atZone(ZoneOffset.UTC).getHour(), geofence.getId());
			Map<String, Object> counts = new HashMap<>();
			for (Object[] objects : usersCountByStatus) {
				counts.put(objects[0].toString(),Long.valueOf(objects[1].toString()));
			}
	    	if(!counts.containsKey("ACTIVE")){
	    		counts.put("ACTIVE", 0L);
	    	}
	    	if(!counts.containsKey("IDLE")){
	    		counts.put("IDLE", 0L);
	    	}
	    	if(!counts.containsKey("INACTIVE")){
	    		counts.put("INACTIVE", 0L);
	    	}
	    	counts.put("LABEL", geofence.getName());
	    	geofencesList.add(counts);
		}
		 DashboardDTO dashboardDTO= new DashboardDTO();
		 dashboardDTO.setGeofences(geofencesList);
		return dashboardDTO;
		
		
	}

	  @Transactional(readOnly = true) 
	    public DashboardDTO getDashbaordNotificationsCount() {
			Map<String, Long> notifications = new HashMap<String, Long>();
			long recievedNotifictionsCount = dashboardRepository.getRecievedNotifictionsCount();
			long sentNotifictionsCount = dashboardRepository.getSentNotifictionsCount();
			notifications.put(NotificationStatus.RECIEVED.toString(), recievedNotifictionsCount);
			notifications.put(NotificationStatus.SENT.toString(), sentNotifictionsCount);
			notifications.put("TOTAL", sentNotifictionsCount+recievedNotifictionsCount);
			DashboardDTO dashboardDto = new DashboardDTO();
			dashboardDto.setNotifications(notifications);
			return dashboardDto;
		}

	  @Transactional(readOnly = true) 
		public DashboardDTO getCaseCountsByPriority() {
				List<Object[]> caseCountByPriority = dashboardRepository.getCaseCountByPriority();
				Map<String,Long> counts = new HashMap<>();
				for (Object[] objects : caseCountByPriority) {
					counts.put(objects[0].toString(), (Long)objects[1]);
				}
				for(CasePriority key : CasePriority.values()){
					if(!counts.containsKey(key.toString())){
						counts.put(key.toString(), (long) 0);
					}
				}
			DashboardDTO dashboardDTO = new DashboardDTO();
			dashboardDTO.setCasePriority(counts);
			return dashboardDTO;
		}
	  
	  @Transactional(readOnly = true) 
		public DashboardDTO getCaseCountsByStatus() {
				List<Object[]> caseCountByStatus = dashboardRepository.getCaseCountByStatus();
				Map<String,Long> counts = new HashMap<>();
				for (Object[] objects : caseCountByStatus) {
					counts.put(objects[0].toString(), (Long)objects[1]);
				}
				for(CaseStatus key : CaseStatus.values()){
					if(!counts.containsKey(key.toString())){
						counts.put(key.toString(), (long) 0);
					}
				}
			DashboardDTO dashboardDTO = new DashboardDTO();
			dashboardDTO.setCaseType(counts);
			return dashboardDTO;
		}


	    @Transactional(readOnly = true) 
		public DashboardDTO getServiceCountByStatus() {
			List<Object[]> caseCountByStatus = dashboardRepository.getServiceCountByStatus();
			Map<String, Long> counts = new HashMap<>();
			for (Object[] objects : caseCountByStatus) {
				counts.put(objects[0].toString(), (Long) objects[1]);
			}
			for (ServiceStatus key : ServiceStatus.values()) {
				if (!counts.containsKey(key.toString())) {
					counts.put(key.toString(), (long) 0);
				}
			}
			DashboardDTO dashboardDTO = new DashboardDTO();
			dashboardDTO.setServiceType(counts);
			return dashboardDTO;

		}
	    
	    @Transactional(readOnly = true)
	    public Page<DashboardUsersDTO> getUsersListWithDistance(Pageable pageable){
		     Calendar date = new GregorianCalendar();
		     date.set(Calendar.HOUR_OF_DAY, 0);
		     date.set(Calendar.MINUTE, 0);
		     date.set(Calendar.SECOND, 0);
		     date.set(Calendar.MILLISECOND, 0);
		     Long fromtime= date.getTimeInMillis();
		     date.set(Calendar.HOUR_OF_DAY, 23);
		     date.set(Calendar.MINUTE, 59);
		     date.set(Calendar.SECOND, 59);
		     date.set(Calendar.MILLISECOND, 1000);
		     Long totime= date.getTimeInMillis();
	     	
	     	
	    	List<Object[]> usersListWithDistance = dashboardRepository.getUsersListWithDistance(fromtime,totime);
	    	List<DashboardUsersDTO> userDtoList = new ArrayList<DashboardUsersDTO>();
			for (Object[] object : usersListWithDistance) {
				DashboardUsersDTO dashboardUsersDTO=new DashboardUsersDTO(object[0].toString(), (Double) object[1]);
				userDtoList.add(dashboardUsersDTO);
			}
			Page<DashboardUsersDTO> usersPage = new PageImpl<DashboardUsersDTO>(userDtoList,pageable, userDtoList.size());
			
			return usersPage;
	    	 
	    	 
	    }
	    
	    @Transactional(readOnly = true)
	    public Page<DashboardUsersDTO> getUsersListWithDistanceBySearch(String searchText,Pageable pageable){
		     Calendar date = new GregorianCalendar();
		     date.set(Calendar.HOUR_OF_DAY, 0);
		     date.set(Calendar.MINUTE, 0);
		     date.set(Calendar.SECOND, 0);
		     date.set(Calendar.MILLISECOND, 0);
		     Long fromtime= date.getTimeInMillis();
		     date.set(Calendar.HOUR_OF_DAY, 23);
		     date.set(Calendar.MINUTE, 59);
		     date.set(Calendar.SECOND, 59);
		     date.set(Calendar.MILLISECOND, 1000);
		     Long totime= date.getTimeInMillis();
	     	
	     	
	    	List<Object[]> usersListWithDistance = dashboardRepository.getUsersListWithDistanceBySearch(fromtime,totime,searchText);
	    	List<DashboardUsersDTO> userDtoList = new ArrayList<DashboardUsersDTO>();
			for (Object[] object : usersListWithDistance) {
				DashboardUsersDTO dashboardUsersDTO=new DashboardUsersDTO(object[0].toString(), (Double) object[1]);
				userDtoList.add(dashboardUsersDTO);
			}
			Page<DashboardUsersDTO> usersPage = new PageImpl<DashboardUsersDTO>(userDtoList,pageable, userDtoList.size());
			
			return usersPage;
	    	 
	    	 
	    }

	    @Transactional(readOnly = true)
	    public DashboardDTO getDashboardUsersCount(){
	    	List<Object[]> usersCountByStatus = dashboardRepository.getUsersCountByStatus(Instant.now().minusMillis(TimeUnit.MINUTES.toMillis(5)).toEpochMilli(),Instant.now().atZone(ZoneOffset.UTC).getHour());
	    	Map<String, Long> counts = new HashMap<>();
			for (Object[] objects : usersCountByStatus) {
				counts.put(objects[0].toString(),Long.valueOf(objects[1].toString()));
			}
	    	if(!counts.containsKey("ACTIVE")){
	    		counts.put("ACTIVE", 0L);
	    	}
	    	if(!counts.containsKey("IDLE")){
	    		counts.put("IDLE", 0L);
	    	}
	    	if(!counts.containsKey("INACTIVE")){
	    		counts.put("INACTIVE", 0L);
	    	}
	    	
	    	 DashboardDTO dashboardDTO = new DashboardDTO();
	    	
	    	 dashboardDTO.setUsers(counts);
			return dashboardDTO;
	    	 
	    	 
	    }

}
