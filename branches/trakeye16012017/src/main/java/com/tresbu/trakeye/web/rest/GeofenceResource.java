package com.tresbu.trakeye.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.tresbu.trakeye.domain.Geofence;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.service.GeofenceService;
import com.tresbu.trakeye.service.dto.GeofenceCreateDTO;
import com.tresbu.trakeye.service.dto.GeofenceDTO;
import com.tresbu.trakeye.service.dto.GeofenceSearchDTO;
import com.tresbu.trakeye.service.dto.GeofenceUpdateDTO;
import com.tresbu.trakeye.web.rest.util.HeaderUtil;
import com.tresbu.trakeye.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiOperation;

/**
 * REST controller for managing Geofence.
 */
@RestController
@RequestMapping("/api")
public class GeofenceResource extends BaseResource{

    private final Logger log = LoggerFactory.getLogger(GeofenceResource.class);
        
    @Inject
    private GeofenceService geofenceService;
    
   
    
    

    /**
     * POST  /geofences : Create a new geofence.
     *
     * @param geofenceDTO the geofenceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new geofenceDTO, or with status 400 (Bad Request) if the geofence has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/geofences",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Create new geofence", notes = "User can create a new geofence. User with role User Admin is allowed.", response = GeofenceDTO.class)
    public ResponseEntity<GeofenceDTO> createGeofence(@Valid @RequestBody GeofenceCreateDTO geofenceDTO) throws URISyntaxException {
        log.debug("REST request to save Geofence : {}", geofenceDTO);
//        if (geofenceDTO.getId() != null) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("geofence", "idexists", "A new geofence cannot already have an ID")).body(null);
//        }
        User loggedInuser=getCurrentUser();
        if(loggedInuser == null){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("geofence", "usernotexist", "login user not present")).body(null);
        }
        
        GeofenceDTO geo= geofenceService.findGeofenceByName(geofenceDTO.getName());
        if(geo != null){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("geofence", "geofencealreadyexists", "Geofence with this name already exists")).body(null);
        }
        Long userId=getCurrentUserLogin();
        if(userId == null){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("geofence", "usernotexist", "login user not present")).body(null);
        }
        geofenceDTO.setUserId(userId);
        GeofenceDTO result = geofenceService.save(geofenceDTO);
        return ResponseEntity.created(new URI("/api/geofences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("geofence", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /geofences : Updates an existing geofence.
     *
     * @param geofenceDTO the geofenceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated geofenceDTO,
     * or with status 400 (Bad Request) if the geofenceDTO is not valid,
     * or with status 500 (Internal Server Error) if the geofenceDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/geofences",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Update geofence", notes = "User can update existing geofence. User with role User Admin is allowed.", response = GeofenceDTO.class)
    public ResponseEntity<GeofenceDTO> updateGeofence(@Valid @RequestBody GeofenceUpdateDTO geofenceUpdateDTO) throws URISyntaxException {
        log.debug("REST request to update Geofence : {}", geofenceUpdateDTO);
//        if (geofenceDTO.getId() == null) {
//            return createGeofence(geofenceDTO);
//        }
        GeofenceDTO existingGeofence= geofenceService.findGeofenceByName(geofenceUpdateDTO.getName());
        if(existingGeofence != null){
        	if(!existingGeofence.getId().equals(geofenceUpdateDTO.getId())){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("geofence", "geofencealreadyexists", "Geofence with this name already exists")).body(null);
        	}
        }
        GeofenceDTO result = geofenceService.update(geofenceUpdateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("geofence", geofenceUpdateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /geofences : get all the geofences.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of geofences in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = {"/geofences","/geofences/searchvalue/{searchtext}"},
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Get list of all geofences", notes = "User can createlist of geofences cretaed by him. User with role User Admin is allowed.", response = GeofenceDTO.class)
    public ResponseEntity<List<GeofenceDTO>> getAllGeofences(@PathVariable("searchtext") Optional<String> searchtext,Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Geofences");
        
        if(searchtext.isPresent()){
        	return searchAllGeofences(searchtext.get(), pageable);
        }
        
        Page<GeofenceDTO> page = geofenceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/geofences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
   
    private ResponseEntity<List<GeofenceDTO>> searchAllGeofences(String searchtext,Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Geofences by search value {}",searchtext);
        Page<GeofenceDTO> page = geofenceService.findAllBySearchValue(searchtext,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/geofences/searchvalue/{searchtext}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /geofences/:id : get the "id" geofence.
     *
     * @param id the id of the geofenceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the geofenceDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/geofences/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Get geofence by id", notes = "User can find a geofence by geofence id. User with role User Admin is allowed.", response = GeofenceDTO.class)
    public ResponseEntity<GeofenceDTO> getGeofence(@PathVariable Long id) {
        log.debug("REST request to get Geofence : {}", id);
        GeofenceDTO geofenceDTO = geofenceService.findOne(id);
        return Optional.ofNullable(geofenceDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /geofences/:id : delete the "id" geofence.
     *
     * @param id the id of the geofenceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/geofences/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Delete geofence", notes = "User can delete geofence created by him. User with role User Admin is allowed.", response = Void.class)
    public ResponseEntity<Void> deleteGeofence(@PathVariable Long id) {
        log.debug("REST request to delete Geofence : {}", id);
        geofenceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("geofence", id.toString())).build();
    }
    
    /**
     * DELETE  /geofences/:id : delete the "id" geofence.
     *
     * @param id the id of the geofenceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/geofenceslist",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Get geofence list by name", notes = "User can search geofence by name. User with role User Admin is allowed.", response = GeofenceDTO.class)
    public ResponseEntity<?> listGeofencesbyName() {
        log.debug("REST request to search Geofences ");
       List<GeofenceSearchDTO> searchGeofencesByName = geofenceService.searchGeofencesByName();  
       return  new ResponseEntity<>(searchGeofencesByName, HttpStatus.OK);
    }

}
