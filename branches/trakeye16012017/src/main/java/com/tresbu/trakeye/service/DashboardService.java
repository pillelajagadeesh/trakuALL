package com.tresbu.trakeye.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tresbu.trakeye.service.dto.DashboardDTO;
import com.tresbu.trakeye.service.dto.DashboardUsersDTO;

/**
 * Service Interface for managing Dashboard.
 */
public interface DashboardService {
    
  //  public DashboardDTO getGeofenceChartData();
    

	//public DashboardDTO getDashbaordNotificationsCount();
	
	//public DashboardDTO getCaseCountsByPriority();


	//public DashboardDTO getCaseCountsByStatus();


	//public DashboardDTO getServiceCountByStatus();


	//public DashboardDTO getDashboardUsersCount();
	
	public DashboardDTO getDashboardData();
	
	public Page<DashboardUsersDTO> getUsersListWithDistance(Pageable pageable);
	
	public Page<DashboardUsersDTO> getUsersListWithDistanceBySearch(String searchText,Pageable pageable);
}
