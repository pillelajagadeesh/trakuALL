package com.tresbu.trakeye.web.rest;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


import com.tresbu.trakeye.domain.LiveLogs;
import com.tresbu.trakeye.domain.TrCase;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.security.SecurityUtils;
import com.tresbu.trakeye.service.DashboardService;
import com.tresbu.trakeye.service.LocationLogService;
import com.tresbu.trakeye.service.TrCaseService;
import com.tresbu.trakeye.service.TrNotificationService;
import com.tresbu.trakeye.service.dto.AgentDashBoardDTO;
import com.tresbu.trakeye.service.dto.DashboardDTO;
import com.tresbu.trakeye.service.dto.DashboardUsersDTO;
import com.tresbu.trakeye.service.dto.TrCaseDTO;
import com.tresbu.trakeye.web.rest.util.HeaderUtil;
import com.tresbu.trakeye.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/dashboard")
public class DashBoardResource extends BaseResource{

	 private final Logger log = LoggerFactory.getLogger(DashBoardResource.class);

		   	@Inject
		   	private DashboardService dashboardService;
		   	
		   	@Inject
		   	private TrCaseService trCaseService;
			
			@Inject
		   	private TrNotificationService trNotificationService;
			
			@Inject
		   	private LocationLogService locationLogService;

		   	
		   	@RequestMapping(value = "/dashboarddata", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
			@Timed
			@Secured({  AuthoritiesConstants.USER_ADMIN })
			@ApiOperation(value = "Get list of all data for dashboard", notes = "User can get list of all cases, services, geofence to display on dashboard. User with role User Admin is allowed.", response = DashboardDTO.class)
			public ResponseEntity<?> notifications(HttpServletRequest request) throws URISyntaxException {
		   		log.debug("Rest Api to get dashboard data");
				return ResponseEntity.ok(dashboardService.getDashboardData());
			}
			
			@RequestMapping(value = {"/userlist","/userlist/{searchtext}"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
			@Timed
			@Secured({  AuthoritiesConstants.USER_ADMIN })
			@ApiOperation(value = "Get list of users with distance covered", notes = "User can get list of users with distance covered on the current day to present on the dashboard. User with role User Admin is allowed.", response = DashboardUsersDTO.class)
			public ResponseEntity<List<DashboardUsersDTO>> usersList(@PathVariable("searchtext")Optional<String> searchtext ,Pageable pageable,HttpServletRequest request) throws URISyntaxException {
				if(searchtext.isPresent()){
					log.debug("Rest Api to get userList to show in dahsboard based on search value");
					return getUserListAndDistance(searchtext.get(),pageable);
				}
				log.debug("Rest Api to get userList to show in dahsboard");
				 Page<DashboardUsersDTO> page = dashboardService.getUsersListWithDistance(pageable);
				 HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userlist");
				return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
			}

			private ResponseEntity<List<DashboardUsersDTO>> getUserListAndDistance(String searchText,Pageable pageable) throws URISyntaxException {
				log.debug("Rest Api to get userList based on search {}",searchText);
				Page<DashboardUsersDTO> page=dashboardService.getUsersListWithDistanceBySearch(searchText,pageable);
				 HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userlist/{searchtext}");
				return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
			}
			
			@RequestMapping(value = "/agentdashboard",
		            method = RequestMethod.GET,
		            produces = MediaType.APPLICATION_JSON_VALUE)
		        @Timed
		        @Secured({AuthoritiesConstants.USER,AuthoritiesConstants.USER_ADMIN})
		        @ApiOperation(value = "Get latest location log", notes = "User can get latest location log, case count and notification count for that user. User with role User Admin and User is allowed."
		        		+ "User with role User will get latest location log created by him, case count and notification count. User with role User Admin will list of latest location logs created by his agents", response = AgentDashBoardDTO.class)
		        public ResponseEntity<AgentDashBoardDTO> getlatestData()
		            throws URISyntaxException, ParseException {
		        	log.debug("REST request to get a latest location logs, case count and notification count");
		        	 User loggedInuser=getCurrentUser();
		        	 if(loggedInuser == null){
		        		 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("locationLog", "loggedinusernull", "Logged in user is null")).body(null);
		        	 }
		        	List<LiveLogs> liveLogs = locationLogService.listLiveLogs(getCurrentUser().getLogin(),Instant.now().minusMillis(TimeUnit.MINUTES.toMillis(5)).toEpochMilli(),ZonedDateTime.now(ZoneOffset.UTC).getHour(),SecurityUtils.getCurrentUserTenantID());
		        	AgentDashBoardDTO agentDashBoardData = new AgentDashBoardDTO();
		        	agentDashBoardData.setLiveLogs(liveLogs);
		        	Map<String, String> caseCount = trCaseService.caseCount();
		        	agentDashBoardData.setCaseCounts(caseCount);
		        	Map<String, String> notificationCount = trNotificationService.notificationCount();
		        	agentDashBoardData.setNotificationCounts(notificationCount);
		        	
		        	List<TrCaseDTO> trCases = trCaseService.casesAssignedToUserToday(loggedInuser.getId());
		        	agentDashBoardData.setTrCases(trCases);
		        	
		        	 return new ResponseEntity<>(agentDashBoardData,  HttpStatus.OK);
		        }
		
		}

