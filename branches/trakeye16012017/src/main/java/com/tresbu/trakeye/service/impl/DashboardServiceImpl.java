package com.tresbu.trakeye.service.impl;

import java.math.BigInteger;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.tresbu.trakeye.security.SecurityUtils;
import com.tresbu.trakeye.service.DashboardService;
import com.tresbu.trakeye.service.dto.DashboardDTO;
import com.tresbu.trakeye.service.dto.DashboardUsersDTO;
import com.tresbu.trakeye.web.rest.DashBoardResource;

@Service
@Transactional
public class DashboardServiceImpl implements DashboardService{
	private final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);

	
	@Inject
	DashboardRepository dashboardRepository;
	
	@Inject
	GeofenceRepository geofenceRepository;
	
	
	 @Transactional(readOnly = true)
	    public DashboardDTO getDashboardData(){
		    log.debug("Method to fetch dashboard data");
	    	// Users count by status
		    
	    	List<Object[]> usersCountByStatus = dashboardRepository.getUsersCountByStatus(Instant.now().minusMillis(TimeUnit.MINUTES.toMillis(5)).toEpochMilli(),Instant.now().atZone(ZoneOffset.UTC).getHour());
	    	Map<String, BigInteger> usercounts = new HashMap<>();
			for (Object[] objects : usersCountByStatus) {
				usercounts.put(objects[0].toString(),(BigInteger) objects[1]);
			}
	    	if(!usercounts.containsKey("ACTIVE")){
	    		usercounts.put("ACTIVE", BigInteger.ZERO);
	    	}
	    	if(!usercounts.containsKey("IDLE")){
	    		usercounts.put("IDLE", BigInteger.ZERO);
	    	}
	    	if(!usercounts.containsKey("INACTIVE")){
	    		usercounts.put("INACTIVE", BigInteger.ZERO);
	    	}
	    	DashboardDTO dashboardDTO = new DashboardDTO();
	    	dashboardDTO.setUsers(usercounts);
	    	
	    	// Cases count by priority
	    	List<Object[]> caseCountByPriority = dashboardRepository.getCaseCountByPriority();
			Map<String,BigInteger> casebyprioritycounts = new HashMap<>();
			for (Object[] objects : caseCountByPriority) {
				casebyprioritycounts.put(objects[0].toString(), (BigInteger)objects[1]);
			}
			for(CasePriority key : CasePriority.values()){
				if(!casebyprioritycounts.containsKey(key.toString())){
					casebyprioritycounts.put(key.toString(), BigInteger.ZERO);
				}
			}
		   dashboardDTO.setCasePriority(casebyprioritycounts);
		   
		   // services count by status
		   List<Object[]> serviceCountByStatus = dashboardRepository.getServiceCountByStatus();
			Map<String, BigInteger> servicecounts = new HashMap<>();
			for (Object[] objects : serviceCountByStatus) {
				servicecounts.put(objects[0].toString(), (BigInteger) objects[1]);
			}
			for (ServiceStatus key : ServiceStatus.values()) {
				if (!servicecounts.containsKey(key.toString())) {
					servicecounts.put(key.toString(), BigInteger.ZERO);
				}
			}
			dashboardDTO.setServiceType(servicecounts);
			
			// cases count by status
			List<Object[]> caseCountByStatus = dashboardRepository.getCaseCountByStatus();
			Map<String,BigInteger> caseStatuscounts = new HashMap<>();
			for (Object[] objects : caseCountByStatus) {
				caseStatuscounts.put(objects[0].toString(), (BigInteger)objects[1]);
			}
			for(CaseStatus key : CaseStatus.values()){
				if(!caseStatuscounts.containsKey(key.toString())){
					caseStatuscounts.put(key.toString(), BigInteger.ZERO);
				}
			}
		   dashboardDTO.setCaseType(caseStatuscounts);
		   
		   // geofence data
		   
		   List<Geofence> findByUserIsCurrentUser = geofenceRepository.findByUserIsCurrentUser();
			 List<Map<String, Object>> geofencesList=new LinkedList<>();
			for (Geofence geofence : findByUserIsCurrentUser) {
				List<Object[]> usersGeofenceCountByStatus = dashboardRepository.getUsersCountByStatusForGeofence(Instant.now().minusMillis(TimeUnit.MINUTES.toMillis(5)).toEpochMilli(), Instant.now().atZone(ZoneOffset.UTC).getHour(), geofence.getId());
				Map<String, Object> geofenceusercounts = new HashMap<>();
				for (Object[] objects : usersGeofenceCountByStatus) {
					geofenceusercounts.put(objects[0].toString(),Long.valueOf(objects[1].toString()));
				}
		    	if(!geofenceusercounts.containsKey("ACTIVE")){
		    		geofenceusercounts.put("ACTIVE", 0L);
		    	}
		    	if(!geofenceusercounts.containsKey("IDLE")){
		    		geofenceusercounts.put("IDLE", 0L);
		    	}
		    	if(!geofenceusercounts.containsKey("INACTIVE")){
		    		geofenceusercounts.put("INACTIVE", 0L);
		    	}
		    	geofenceusercounts.put("LABEL", geofence.getName());
		    	geofencesList.add(geofenceusercounts);
			}
			 dashboardDTO.setGeofences(geofencesList);
	    	
	    	
			return dashboardDTO;
	    	 
	    	 
	    }
	 
	 @Transactional(readOnly = true)
	    public Page<DashboardUsersDTO> getUsersListWithDistance(Pageable pageable){
		 log.debug("Method to fetch user list in dashboard");
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
	     	
	     	log.debug("Usere distance for fromtime {}, to totime {}, loginuser {}, tenantID {}",fromtime,totime,SecurityUtils.getCurrentUserLogin(),SecurityUtils.getCurrentUserTenantID());
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
	    	log.debug("Method to fetch user list in dashboard based on search value");
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
	     	
		     log.debug("Usere distance for fromtime {}, to totime {}, loginuser {}, tenantID {}",fromtime,totime,SecurityUtils.getCurrentUserLogin(),SecurityUtils.getCurrentUserTenantID());
		    	
	    	List<Object[]> usersListWithDistance = dashboardRepository.getUsersListWithDistanceBySearch(fromtime,totime,searchText);
	    	List<DashboardUsersDTO> userDtoList = new ArrayList<DashboardUsersDTO>();
			for (Object[] object : usersListWithDistance) {
				DashboardUsersDTO dashboardUsersDTO=new DashboardUsersDTO(object[0].toString(), (Double) object[1]);
				userDtoList.add(dashboardUsersDTO);
			}
			Page<DashboardUsersDTO> usersPage = new PageImpl<DashboardUsersDTO>(userDtoList,pageable, userDtoList.size());
			
			return usersPage;
	    	 
	    	 
	    }
	
	

}
