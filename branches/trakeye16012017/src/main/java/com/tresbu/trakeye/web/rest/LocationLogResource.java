package com.tresbu.trakeye.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.tresbu.trakeye.domain.LiveLogs;
import com.tresbu.trakeye.domain.LocationLog;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.domain.enumeration.LogSource;
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.security.SecurityUtils;
import com.tresbu.trakeye.service.LocationLogService;
import com.tresbu.trakeye.service.dto.LocationLogCreateDTO;
import com.tresbu.trakeye.service.dto.LocationLogDTO;
import com.tresbu.trakeye.service.dto.LocationLogUpdateDTO;
import com.tresbu.trakeye.service.dto.UserPathDTO;
import com.tresbu.trakeye.service.util.RandomUtil;
import com.tresbu.trakeye.web.rest.util.HeaderUtil;
import com.tresbu.trakeye.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiOperation;

/**
 * REST controller for managing LocationLog.
 */
@RestController
@RequestMapping("/api")
public class LocationLogResource extends BaseResource {

    private final Logger log = LoggerFactory.getLogger(LocationLogResource.class);
        
    @Inject
    private LocationLogService locationLogService;
    
    
    

    /**
     * POST  /location-logs : Create a new locationLog.
     *
     * @param locationLogDTO the locationLogDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new locationLogDTO, or with status 400 (Bad Request) if the locationLog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws ParseException 
     */
    @RequestMapping(value = "/location-logs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER})
    @ApiOperation(value = "Create new location log", notes = "User can create a new location log. User with role User is allowed.", response = LocationLogDTO.class)
    public ResponseEntity<LocationLogDTO> createLocationLog(@Valid @RequestBody LocationLogCreateDTO locationLogDTO) throws URISyntaxException, ParseException {
    	
        log.debug("REST request to save LocationLog : {}", locationLogDTO);
       
        log.info("Log source : {}", locationLogDTO.getLogSource());
         User loggedInuser=getCurrentUser();
        if(loggedInuser == null){
     	   return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("locationLog", "loggedinusernull", "Logged in user is null")).body(null);
        }
        if(locationLogDTO.getLatitude().isNaN() || locationLogDTO.getLongitude().isNaN()){
        	log.debug("NaN values in latitude and longitude {} {}",locationLogDTO.getLatitude(),locationLogDTO.getLongitude());
        	 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("locationLog", "nanvalue", "Latitide or longitude values are NaN")).body(null);
        }
        locationLogDTO.setUserId(loggedInuser.getId());
        log.info("LocationLog create for user : {}", loggedInuser.getLogin());
	    if( RandomUtil.checkFromAndToTime(loggedInuser.getFromTime(), loggedInuser.getToTime(),locationLogDTO.getLogSource(), locationLogDTO.getCreatedDateTime())){
//    		locationLogDTO.setUserId(getCurrentUserLogin());
    		
    		if(locationLogDTO.getLogSource().toString().equals(LogSource.GPS.toString())){
    			log.info("Setting createdDateTime when log source is GPS");
    			locationLogDTO.setCreatedDateTime(Instant.now().toEpochMilli());
    		
    		}else  if(locationLogDTO.getLogSource().toString().equals(LogSource.NP.toString()) || locationLogDTO.getLogSource().toString().equals(LogSource.NETWORK.toString()) ){
    			// If logSource is NP then take time from createdTime
    			
                locationLogDTO.setCreatedDateTime(locationLogDTO.getCreatedDateTime());
                
    		}
  	        LocationLogDTO result = locationLogService.save(locationLogDTO);
  	        log.info("A new locationLog is created successfully");
  	        return ResponseEntity.created(new URI("/api/location-logs/" + result.getId()))
  	            .headers(HeaderUtil.createEntityCreationAlert("locationLog", result.getId().toString()))
  	            .body(result);
  	      } else {
  	    	log.info("A new locationLog cannot be created, as log time is not between user's from time and to time");
  	    	return ResponseEntity.ok().headers(HeaderUtil
  	    		.createAlert("locationLog",  "A new locationLog cannot be created, as log time is not between user's from time and to time")).body(null);
  	      }
    }
    
    
    
    /**
     * POST  /location-logs : Create bulk locationLogs.
     *
     * @param locationLogDTO the list of locationLogDTO to create
     * @return the ResponseEntity with status 201 (Created) 
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws ParseException 
     */
    @RequestMapping(value = "/location-bulklogs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER})
    @ApiOperation(value = "Create list of location logs", notes = "User can list of new location logs. User with role User is allowed.", response = LocationLogDTO.class)
    public ResponseEntity<?> createBulkLocationLogs(@Valid @RequestBody List<LocationLogCreateDTO> locationLogDTO) throws URISyntaxException, ParseException {
    	
        log.debug("REST request to save LocationLog ");
        if(locationLogDTO.size() <= 0){
        	return ResponseEntity.badRequest().headers(HeaderUtil
	    	.createFailureAlert("locationLog", "locationloglistisnull", "LocationLog list is null")).body(null);
        }
        User loggedInuser=getCurrentUser();
        if(loggedInuser == null){
     	   return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("locationLog", "loggedinusernull", "Logged in user is null")).body(null);
        }
        locationLogDTO.sort(Comparator.comparing(LocationLogCreateDTO::getCreatedDateTime));
        LocationLogDTO result=null;
        for(LocationLogCreateDTO locationlog: locationLogDTO){
	        log.debug("REST request to save LocationLog : {}", locationlog);
	        log.debug("Log source : {}", locationlog.getLogSource());
	        if(locationlog.getLatitude().isNaN() || locationlog.getLongitude().isNaN()){
	        	log.debug("NaN values in latitude and longitude {} {}",locationlog.getLatitude(),locationlog.getLongitude());
	        	continue;
	        }
	        
	        locationlog.setUserId(loggedInuser.getId());
	        log.debug("LocationLog create for user : {}", loggedInuser.getLogin());
		    if( RandomUtil.checkFromAndToTime(loggedInuser.getFromTime(), loggedInuser.getToTime(),locationlog.getLogSource(), locationlog.getCreatedDateTime())){
//		    	locationlog.setUserId(getCurrentUserLogin());
	    		
	    		if(locationlog.getLogSource().toString().equals(LogSource.GPS.toString())){
	    			log.debug("Setting createdDateTime when log source is GPS");
	    			locationlog.setCreatedDateTime(Instant.now().toEpochMilli());
	    		
	    		}else  if(locationlog.getLogSource().toString().equals(LogSource.NP.toString()) || locationlog.getLogSource().toString().equals(LogSource.NETWORK.toString()) ){
	    			// If logSource is NP then take time from createdTime
	    			
	    			locationlog.setCreatedDateTime(locationlog.getCreatedDateTime());
	                
	    		}
	    		result = locationLogService.save(locationlog);
	  	        log.debug("A new locationLog is created successfully");
	  	      } else {
	  	    	log.debug("A new locationLog cannot be created, as log time is not between user's from time and to time");
	  	      }
        }
        
        if(result != null){
        	return ResponseEntity.created(new URI("/api/location-logs/" + result.getId()))
      	            .headers(HeaderUtil.createEntityCreationAlert("locationLog", result.getId().toString()))
      	            .body(result);
        }
        return ResponseEntity.ok()
	            .headers(HeaderUtil.createEntityCreationAlert("locationLog", "locationlog created successfully"))
	            .body("");
    }
    
   

    /**
     * PUT  /location-logs : Updates an existing locationLog.
     *
     * @param locationLogDTO the locationLogDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated locationLogDTO,
     * or with status 400 (Bad Request) if the locationLogDTO is not valid,
     * or with status 500 (Internal Server Error) if the locationLogDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws ParseException 
     */
	@RequestMapping(value = "/location-logs", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	@Secured({ AuthoritiesConstants.USER })
	@ApiOperation(value = "Update existing location log", notes = "User can update existing new location log, only battery percentage and updated time will be updated. User with role User is allowed.", response = LocationLogDTO.class)
	public ResponseEntity<LocationLogDTO> updateLocationLog(@Valid @RequestBody LocationLogUpdateDTO locationLogDTO)
			throws URISyntaxException, ParseException {
		log.debug("REST request to update LocationLog : {}", locationLogDTO);
		
			 User loggedInuser=getCurrentUser();
		if (loggedInuser == null) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("locationLog", "loggedinusernull", "Logged in user is null"))
					.body(null);
		}
		
		 log.info("LocationLog update for user : {}", loggedInuser.getLogin());
		if (RandomUtil.checkFromAndToTime(loggedInuser.getFromTime(), loggedInuser.getToTime(), locationLogDTO.getLogSource(), locationLogDTO.getCreatedDateTime()) && locationLogDTO.getLogSource().toString().equals(LogSource.GPS.toString()))  {
			
				// If logSource is GPS then convert current time to UTC and set
				// to CreatedDateTime
				log.info("Setting updatedDateTime when log source is GPS");
				LocationLogDTO result = locationLogService.update(locationLogDTO.getId(),locationLogDTO.getBatteryPercentage());
				log.info("LocationLog is updated successfully");
				return ResponseEntity.ok()
				            .headers(HeaderUtil.createEntityUpdateAlert("locationLog", result.getId().toString()))
				            .body(result);
			
		} else {
			log.info("LocationLog cannot be updated, as log time is not between user's from time and to time");
			return ResponseEntity.ok()
					.headers(HeaderUtil.createAlert("locationLog", 
							"LocationLog cannot be updated, as log time is not between user's from time and to time"))
					.body(null);
		}

	}

    /**
     * GET  /location-logs : get all the locationLogs.
     * fromDate and toDate should be in the format 'yyyy-MM-dd'. By default both fromdate and todate should be current date
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of locationLogs in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     * @throws ParseException 
     */
   /* @RequestMapping(value = "/location-logs/{fromDate}/{toDate}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER,AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Get list of location logs", notes = "User can get list of location log those have been created between given from date and to date. User with role User Admin and User is allowed."
    		+ "User with role User will get list of location logs created by him. User with role User Admin will get list of location logs created by his agents", response = LocationLogDTO.class)
    public ResponseEntity<List<LocationLogDTO>> getAllLocationLogs(@PathVariable long fromDate,@PathVariable long toDate,Pageable pageable)
            throws URISyntaxException, ParseException {
            log.debug("REST request to get a page of LocationLogs");
       
            Page<LocationLogDTO> page = locationLogService.findAll(fromDate,toDate+TimeUnit.DAYS.toMillis(1),pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/location-logs");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }*/
    
    
    
    
    /**
     * GET  /location-logs : get all the locationLogs.
     * fromDate and toDate should be in the format 'yyyy-MM-dd'. By default both fromdate and todate should be current date
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of locationLogs in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     * @throws ParseException 
     */
    @RequestMapping(value = "/location-logs/userpath/{userlogin}/{fromDate}/{toDate}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER,AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Get list of location logs based on login", notes = "User can get list of location log those have been created by given login between given from date and to date. User with role User Admin and User is allowed."
    		+ "User with role User will get list of location logs created by him. User with role User Admin will get list of location logs created by his agents", response = UserPathDTO.class)
    public ResponseEntity<UserPathDTO> getAllLocationLogs(@PathVariable String userlogin,@PathVariable long fromDate,@PathVariable long toDate)
        throws URISyntaxException, ParseException {
    	// fromDate and toDate should be in the format 'yyyy-MM-dd'. By default both fromdate and todate should be current date
        log.debug("REST request to get a page of LocationLogs");
        
       
        long userid=Integer.parseInt(userlogin);
        UserPathDTO paths = locationLogService.listLocationPath(userid,fromDate,toDate+TimeUnit.DAYS.toMillis(1));
      
        return new ResponseEntity<>(paths,  HttpStatus.OK);
    }
    /**
     * GET  /location-logs : get all the locationLogs.
     * fromDate and toDate should be in the format 'yyyy-MM-dd'. By default both fromdate and todate should be current date
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of locationLogs in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     * @throws ParseException 
     */
    @RequestMapping(value = "/location-logs/latest",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER,AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Get latest location log", notes = "User can get latest location log for that user. User with role User Admin and User is allowed."
    		+ "User with role User will get latest location log created by him. User with role User Admin will list of latest location logs created by his agents", response = LiveLogs.class)
    public ResponseEntity<List<LiveLogs>> getlatestLocationLogs()
        throws URISyntaxException, ParseException {
    	    	log.debug("REST request to get a page of LocationLogs");
    	List<LiveLogs> liveLogs = locationLogService.listLiveLogs(getCurrentUser().getLogin(),Instant.now().minusMillis(TimeUnit.MINUTES.toMillis(5)).toEpochMilli(),ZonedDateTime.now(ZoneOffset.UTC).getHour(),SecurityUtils.getCurrentUserTenantID());
    	for (Iterator<LiveLogs> iterator = liveLogs.iterator(); iterator.hasNext();) {
			LiveLogs liveLogs2 = (LiveLogs) iterator.next();
			
			log.debug("User login["+liveLogs2.getLogin()+"] status ["+liveLogs2.getStatus()+"]");
			
		}
    	
    	 return new ResponseEntity<>(liveLogs,  HttpStatus.OK);
    }
    
    

    /**
     * GET  /location-logs/:id : get the "id" locationLog.
     *
     * @param id the id of the locationLogDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the locationLogDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/location-logs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER,AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Get location log based on id", notes = "User can get location log based on log id. User with role User Admin and User is allowed."
    		+ "User with role User will get location log created by him. User with role User Admin will location logs created by his agents", response = LocationLogDTO.class)
    public ResponseEntity<LocationLogDTO> getLocationLog(@PathVariable Long id) {
        log.debug("REST request to get LocationLog : {}", id);
        LocationLogDTO locationLogDTO = locationLogService.findOne(id);
        return Optional.ofNullable(locationLogDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /location-logs/:id : delete the "id" locationLog.
     *
     * @param id the id of the locationLogDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/location-logs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Delete location log", notes = "User can delete location log based on log id. User with role User Admin is allowed.", response = Void.class)
    public ResponseEntity<Void> deleteLocationLog(@PathVariable Long id) {
        log.debug("REST request to delete LocationLog : {}", id);
        locationLogService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("locationLog", id.toString())).build();
    }
    
    
  
}
