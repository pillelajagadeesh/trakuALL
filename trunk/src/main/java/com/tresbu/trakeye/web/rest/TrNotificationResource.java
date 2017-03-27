package com.tresbu.trakeye.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

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
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.service.TrNotificationService;
import com.tresbu.trakeye.service.dto.TrNotificationDTO;
import com.tresbu.trakeye.service.dto.TrNotificationUpdateDTO;
import com.tresbu.trakeye.web.rest.util.HeaderUtil;
import com.tresbu.trakeye.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiOperation;

/**
 * REST controller for managing TrNotification.
 */
@RestController
@RequestMapping("/api")
public class TrNotificationResource extends BaseResource {

    private final Logger log = LoggerFactory.getLogger(TrNotificationResource.class);
    
    
    @Inject
    private TrNotificationService trNotificationService;

    /**
     * POST  /tr-notifications : Create a new trNotification.
     *
     * @param trNotificationDTO the trNotificationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trNotificationDTO, or with status 400 (Bad Request) if the trNotification has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tr-notifications",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Create new notification", notes = "User can create a new notification. User with role User Admin is allowed.", response = TrNotificationDTO.class)
    public ResponseEntity<TrNotificationDTO> createTrNotification(@Valid @RequestBody TrNotificationDTO trNotificationDTO) throws URISyntaxException {
        log.debug("REST request to save TrNotification : {}", trNotificationDTO);
        if (trNotificationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("trNotification", "idexists", "A new trNotification cannot already have an ID")).body(null);
        }
        
        Long userId=getCurrentUserLogin();
        if(userId == null){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("geofence", "usernotexist", "login user not present")).body(null);
        }
        trNotificationDTO.setFromUserId(userId);
        
        trNotificationDTO.setCreatedDate(Instant.now().toEpochMilli());
        TrNotificationDTO result = trNotificationService.save(trNotificationDTO);
       
        return ResponseEntity.created(new URI("/api/tr-notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("trNotification", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tr-notifications : Updates an existing trNotification.
     *
     * @param trNotificationDTO the trNotificationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trNotificationDTO,
     * or with status 400 (Bad Request) if the trNotificationDTO is not valid,
     * or with status 500 (Internal Server Error) if the trNotificationDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tr-notifications",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Update notification", notes = "User can updayte existing notification. User with role User Admin is allowed.", response = TrNotificationDTO.class)
    public ResponseEntity<TrNotificationDTO> updateTrNotification(@Valid @RequestBody TrNotificationUpdateDTO trNotificationDTO) throws URISyntaxException {
        log.debug("REST request to update TrNotification : {}", trNotificationDTO);
//        if (trNotificationDTO.getId() == null) {
//            return createTrNotification(trNotificationDTO);
//        }
       
        TrNotificationDTO result = trNotificationService.update(trNotificationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("trNotification", trNotificationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tr-notifications : get all the trNotifications.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of trNotifications in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/tr-notifications",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN,AuthoritiesConstants.USER})
    @ApiOperation(value = "Get list of notifications", notes = "User can get list of all notifications . User with role User Admin is allowed.", response = TrNotificationDTO.class)
    public ResponseEntity<List<TrNotificationDTO>> getAllTrNotifications(String searchText,Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TrNotifications");
        Page<TrNotificationDTO> page = trNotificationService.findAll(searchText,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tr-notifications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    
    /**
     * GET  /tr-notifications : get all the trNotifications.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of trNotifications in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/tr-notifications/searchvalue/{searchtext}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN,AuthoritiesConstants.USER})
    @ApiOperation(value = "Search list of notifications", notes = "Method returns list of services created by the caller and services created by users under the caller. Role UserAdmin,User is allowed.", response = TrNotificationDTO.class)
    public ResponseEntity<List<TrNotificationDTO>> searchAllTrNotifications(@PathVariable("searchtext")String searchText,Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TrNotifications");
        Page<TrNotificationDTO> page = trNotificationService.findAll(searchText,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tr-notifications/searchvalue/{searchtext}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /tr-notifications/:id : get the "id" trNotification.
     *
     * @param id the id of the trNotificationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trNotificationDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/tr-notifications/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN, AuthoritiesConstants.USER})
    @ApiOperation(value = "Get notification by id", notes = "User can find notification by id. User with role User Admin is allowed.", response = TrNotificationDTO.class)
    public ResponseEntity<TrNotificationDTO> getTrNotification(@PathVariable Long id) {
        log.debug("REST request to get TrNotification : {}", id);
        TrNotificationDTO trNotificationDTO = trNotificationService.findOne(id);
        
        return Optional.ofNullable(trNotificationDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tr-notifications/:id : delete the "id" trNotification.
     *
     * @param id the id of the trNotificationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/tr-notifications/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Delete notification", notes = "User can delete notification by id. User with role User Admin is allowed.", response = Void.class)
    public ResponseEntity<Void> deleteTrNotification(@PathVariable Long id) {
        log.debug("REST request to delete TrNotification : {}", id);
        trNotificationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("trNotification", id.toString())).build();
    }

}
