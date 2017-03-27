package com.tresbu.trakeye.web.rest;

import java.net.URISyntaxException;
import java.util.List;

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
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.service.DashboardService;
import com.tresbu.trakeye.service.dto.DashboardDTO;
import com.tresbu.trakeye.service.dto.DashboardUsersDTO;
import com.tresbu.trakeye.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/dashboard")
public class DashBoardResource extends BaseResource{

	 private final Logger log = LoggerFactory.getLogger(UserResource.class);

		   	@Inject
		   	private DashboardService dashboardService;

	    
			@RequestMapping(value = "/notifications", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
			@Timed
			@Secured({  AuthoritiesConstants.USER_ADMIN })
			@ApiOperation(value = "Get list of notifications", notes = "User can get list of notification. User with role User Admin is allowed.", response = DashboardDTO.class)
			public ResponseEntity<?> notifications(HttpServletRequest request) throws URISyntaxException {
				return ResponseEntity.ok(dashboardService.getDashbaordNotificationsCount());
			}

			@RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
			@Timed
			@Secured({  AuthoritiesConstants.USER_ADMIN })
			@ApiOperation(value = "Get list of users", notes = "User can get list of users to present on the dashboard. User with role User Admin is allowed.", response = DashboardDTO.class)
			public ResponseEntity<?> users(HttpServletRequest request) throws URISyntaxException {           
				return ResponseEntity.ok(dashboardService.getDashboardUsersCount());
			}
			
			@RequestMapping(value = "/services", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
			@Timed
			@Secured({  AuthoritiesConstants.USER_ADMIN })
			@ApiOperation(value = "Get list of servies based on status", notes = "User can get list of services based on the status to present on the dashboard. User with role User Admin is allowed.", response = DashboardDTO.class)
			public ResponseEntity<?> services(HttpServletRequest request) throws URISyntaxException {
				return ResponseEntity.ok(dashboardService.getServiceCountByStatus());
			}
			
			@RequestMapping(value = {"/userlist","/userlist/{searchtext}"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
			@Timed
			@Secured({  AuthoritiesConstants.USER_ADMIN })
			@ApiOperation(value = "Get list of users with distance coveres", notes = "User can get list of users with distance covered on the current day to present on the dashboard. User with role User Admin is allowed.", response = DashboardUsersDTO.class)
			public ResponseEntity<List<DashboardUsersDTO>> usersList(@PathVariable("searchtext")Optional<String> searchtext ,Pageable pageable,HttpServletRequest request) throws URISyntaxException {
				if(searchtext.isPresent()){
					return getUserListAndDistance(searchtext.get(),pageable);
				}
				log.debug("Rest Api to get userList");
				 Page<DashboardUsersDTO> page=dashboardService.getUsersListWithDistance(pageable);
				 HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userlist");
				return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
			}

			private ResponseEntity<List<DashboardUsersDTO>> getUserListAndDistance(String searchText,Pageable pageable) throws URISyntaxException {
				log.debug("Rest Api to get userList based on search {}",searchText);
				Page<DashboardUsersDTO> page=dashboardService.getUsersListWithDistanceBySearch(searchText,pageable);
				 HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userlist/{searchtext}");
				return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
			}
			
			@RequestMapping(value = "/casepriority", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
			@Timed
			@Secured({  AuthoritiesConstants.USER_ADMIN })
			@ApiOperation(value = "Get list of cases based on priority", notes = "User can get list of cases based on priority to present on the dashboard. User with role User Admin is allowed.", response = DashboardDTO.class)
			public ResponseEntity<?> casePriority(HttpServletRequest request) throws URISyntaxException {
				return ResponseEntity.ok(dashboardService.getCaseCountsByPriority());
			}
			
			@RequestMapping(value = "/cases", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
			@Timed
			@Secured({  AuthoritiesConstants.USER_ADMIN })
			@ApiOperation(value = "Get list of cases based on status", notes = "User can get list of cases based on status to present on the dashboard. User with role User Admin is allowed.", response = DashboardDTO.class)
			public ResponseEntity<?> cases(HttpServletRequest request) throws URISyntaxException {
				return ResponseEntity.ok(dashboardService.getCaseCountsByStatus());
			}
			
			@RequestMapping(value = "/geofences", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
			@Timed
			@Secured({  AuthoritiesConstants.USER_ADMIN })
			@ApiOperation(value = "Get list of geofences", notes = "User can get list of geofences created by to present on the dashboard. User with role User Admin is allowed.", response = DashboardDTO.class)
			public ResponseEntity<?> geofences(HttpServletRequest request) throws URISyntaxException {
				return ResponseEntity.ok(dashboardService.getGeofenceChartData());
			}
		}

