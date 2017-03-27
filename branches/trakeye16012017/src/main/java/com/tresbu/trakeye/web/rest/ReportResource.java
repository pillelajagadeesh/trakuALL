package com.tresbu.trakeye.web.rest;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.service.LocationLogService;
import com.tresbu.trakeye.service.ReportService;
import com.tresbu.trakeye.service.dto.BatteryReportDTO;
import com.tresbu.trakeye.service.dto.DistanceReportDTO;
import com.tresbu.trakeye.service.dto.ReportDTO;
import com.tresbu.trakeye.service.dto.UserPathDTO;
import com.tresbu.trakeye.web.rest.vm.LoggerVM;
import com.tresbu.trakeye.web.rest.vm.ReportRequestDTO;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/reports")
public class ReportResource extends BaseResource{

	 private final Logger log = LoggerFactory.getLogger(ReportResource.class);

		   	@Inject
		   	private ReportService reportService;
		   	
			@Inject
		   	private LocationLogService locationLogService;

	    
			@RequestMapping(value = "/view", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
			@Timed
			@Secured({  AuthoritiesConstants.USER_ADMIN })
			@ApiOperation(value = "Get agent and geofence report", notes = "User can get report for his agents and geofence created by him within given dates. User with role User Admin is allowed.", response = ReportDTO.class)
			public ResponseEntity<?> notifications(HttpServletRequest request,@RequestBody ReportRequestDTO reportRequestDTO) throws URISyntaxException {
				  log.debug("REST request to view report : {}", reportRequestDTO);
				if(reportRequestDTO.getReportType().equalsIgnoreCase("Agent")){
						if( reportRequestDTO.getId()==0){
							return ResponseEntity.ok(reportService.getUserAgentReportforAll(reportRequestDTO.getFromTime(), reportRequestDTO.getToTime(),getCurrentUser().getLogin()));

						}else{
							return ResponseEntity.ok(reportService.getUserAgentReportforUser(reportRequestDTO.getFromTime(), reportRequestDTO.getToTime(), reportRequestDTO.getId()));

						}
				}else if(reportRequestDTO.getReportType().equalsIgnoreCase("Geofence")){
					if( reportRequestDTO.getId()==0){
						return ResponseEntity.ok(reportService.getGeofenceReportforAll(reportRequestDTO.getFromTime(), reportRequestDTO.getToTime(), getCurrentUser().getId()));

					}else{
						return ResponseEntity.ok(reportService.getGeofenceReportforGeofence(reportRequestDTO.getFromTime(), reportRequestDTO.getToTime(), reportRequestDTO.getId()));

					}
				}
				
					return ResponseEntity.ok(reportService.getUserAgentReportforUser(reportRequestDTO.getFromTime(), reportRequestDTO.getToTime(), reportRequestDTO.getId()));

			}
			@RequestMapping(value = "/detailed-report", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
			@Timed
			@Secured({  AuthoritiesConstants.USER_ADMIN })
			@ApiOperation(value = "Get agent and geofence detailed report", notes = "User can get detailed report for his agents and geofence created by him within givenm dates. User with role User Admin is allowed.", response = ReportDTO.class)
			public ResponseEntity<?> detailedReport(HttpServletRequest request,@RequestBody ReportRequestDTO reportRequestDTO) throws URISyntaxException {
				 log.debug("REST request to get detailed report report : {}", reportRequestDTO);
				if(reportRequestDTO.getReportType().equalsIgnoreCase("Agent")){
							return ResponseEntity.ok(reportService.getUserAgentDetailedReportforUser(reportRequestDTO.getFromTime(), reportRequestDTO.getToTime(),reportRequestDTO.getId()));

					
				}else if(reportRequestDTO.getReportType().equalsIgnoreCase("Geofence")){
					return ResponseEntity.ok(reportService.getGeofenceDetailedReportforGeofence(reportRequestDTO.getFromTime(), reportRequestDTO.getToTime(),reportRequestDTO.getId()));
				}
				return ResponseEntity.ok(reportService.getUserAgentReportforAll(reportRequestDTO.getFromTime(), reportRequestDTO.getToTime(),getCurrentUser().getLogin()));

			}
			
			@RequestMapping(value = "/batteryreport/{userlogin}/{fromDate}/{toDate}",
			        method = RequestMethod.GET,
			        produces = MediaType.APPLICATION_JSON_VALUE)
			    @Timed
			    @Secured({AuthoritiesConstants.USER_ADMIN})
			    @ApiOperation(value = "Get battery report statistics", notes = "User Get battery report statistics by given login between given from date and to date. User with role User Admin allowed."
			    		+ "", response = BatteryReportDTO.class)
			    public ResponseEntity<List<BatteryReportDTO>> getAllLocationLogs(@PathVariable String userlogin,@PathVariable long fromDate,@PathVariable long toDate)
			        throws URISyntaxException, ParseException {
			    	
			        log.debug("REST request to get a battery details");
			        
			       
			        long userid1=Integer.parseInt(userlogin);
			        List<BatteryReportDTO> batteryReportDTO = locationLogService.listBatteryDetails(userid1,fromDate,toDate+TimeUnit.DAYS.toMillis(1));
			      
			        return new ResponseEntity<>(batteryReportDTO,  HttpStatus.OK);
			    }
			
			
			
			@RequestMapping(value = "/distancereport/{userlogin}/{fromDate}/{toDate}",
			        method = RequestMethod.GET,
			        produces = MediaType.APPLICATION_JSON_VALUE)
			    @Timed
			    @Secured({AuthoritiesConstants.USER_ADMIN})
			    @ApiOperation(value = "Get distance report statistics", notes = "User Get distance report statistics by given login between given from date and to date. User with role User Admin allowed."
			    		+ "", response = BatteryReportDTO.class)
			    public ResponseEntity<List<DistanceReportDTO>> getDistanceReport(@PathVariable String userlogin,@PathVariable long fromDate,@PathVariable long toDate)
			        throws URISyntaxException, ParseException {
			    	
			        log.debug("REST request to get a distance details");
			        
			       
			        long userid1=Integer.parseInt(userlogin);
			        List<DistanceReportDTO> distanceReportDTO = locationLogService.getDistanceReport(userid1,fromDate,toDate+TimeUnit.DAYS.toMillis(1));
			      
			        return new ResponseEntity<>(distanceReportDTO,  HttpStatus.OK);
			    }

			
			
		}

