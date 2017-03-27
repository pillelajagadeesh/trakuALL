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
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.service.TrakeyeTypeService;
import com.tresbu.trakeye.service.dto.CaseTypeDTO;
import com.tresbu.trakeye.service.dto.TrakeyeTypeCreateDTO;
import com.tresbu.trakeye.service.dto.TrakeyeTypeDTO;
import com.tresbu.trakeye.service.dto.TrakeyeTypeUpdateDTO;
import com.tresbu.trakeye.web.rest.util.HeaderUtil;
import com.tresbu.trakeye.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiOperation;

/**
 * REST controller for managing TrakeyeType.
 */
@RestController
@RequestMapping("/api")
public class TrakeyeTypeResource extends BaseResource{

    private final Logger log = LoggerFactory.getLogger(TrakeyeTypeResource.class);
        
    @Inject
    private TrakeyeTypeService trakeyeTypeService;

    /**
     * POST  /trakeye-types : Create a new trakeyeType.
     *
     * @param trakeyeTypeDTO the trakeyeTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trakeyeTypeDTO, or with status 400 (Bad Request) if the trakeyeType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/trakeye-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.USER_ADMIN)
    @ApiOperation(value = "Create new trakeye type", notes = "User can create a new trakeye type. User with role User Admin is allowed.", response = TrakeyeTypeDTO.class)
    public ResponseEntity<TrakeyeTypeDTO> createTrakeyeType(@Valid @RequestBody TrakeyeTypeCreateDTO trakeyeTypeCreateDTO) throws URISyntaxException {
        log.debug("REST request to save TrakeyeType : {}", trakeyeTypeCreateDTO);
        
        Long userId=getCurrentUserLogin();
        if(userId == null){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("geofence", "usernotexist", "login user not present")).body(null);
        }
        TrakeyeTypeDTO existingtrakeyeType= trakeyeTypeService.findTrakeyeTypeByName(trakeyeTypeCreateDTO.getName());
        if(existingtrakeyeType != null){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("trakeyetype", "trakeyetypealreadyexists", "Trakeye type with this name already exists")).body(null);
        }
        trakeyeTypeCreateDTO.setUserId(userId);
        TrakeyeTypeDTO result = trakeyeTypeService.save(trakeyeTypeCreateDTO);
        return ResponseEntity.created(new URI("/api/trakeye-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("trakeyeType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trakeye-types : Updates an existing trakeyeType.
     *
     * @param trakeyeTypeDTO the trakeyeTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trakeyeTypeDTO,
     * or with status 400 (Bad Request) if the trakeyeTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the trakeyeTypeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/trakeye-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN, AuthoritiesConstants.USER})
    @ApiOperation(value = "Update trakeye type", notes = "User can update existing trakeye type. User with role User Admin is allowed.", response = TrakeyeTypeDTO.class)
    public ResponseEntity<TrakeyeTypeDTO> updateTrakeyeType(@Valid @RequestBody TrakeyeTypeUpdateDTO trakeyeTypeUpdateDTO) throws URISyntaxException {
        log.debug("REST request to update TrakeyeType : {}", trakeyeTypeUpdateDTO);
//        if (trakeyeTypeDTO.getId() == null) {
//            return createTrakeyeType(trakeyeTypeDTO);
//        }
        TrakeyeTypeDTO existingtrakeyeType= trakeyeTypeService.findTrakeyeTypeByName(trakeyeTypeUpdateDTO.getName());
        if(existingtrakeyeType != null){
        	if(!existingtrakeyeType.getId().equals(trakeyeTypeUpdateDTO.getId())){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("trakeyetype", "trakeyetypealreadyexists", "Trakeye type with this name already exists")).body(null);
        }
        }
        TrakeyeTypeDTO result = trakeyeTypeService.update(trakeyeTypeUpdateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("trakeyeType", trakeyeTypeUpdateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trakeye-types : get all the trakeyeTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of trakeyeTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = {"/trakeye-types","/trakeye-types/searchvalue/{searchtext}"},
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Get list of all trakeye trakeye types", notes = "User can get list of all trakeye types. User with role UserAdmin is allowed.", response = TrakeyeTypeDTO.class)
    public ResponseEntity<List<TrakeyeTypeDTO>> getAllTrakeyeTypes(@PathVariable("searchtext") Optional<String> searchtext,Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TrakeyeTypes");
        
        if(searchtext.isPresent()){
        	return searchAllTrakeyeTypes(searchtext.get(), pageable);
        }
        
        Page<TrakeyeTypeDTO> page = trakeyeTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trakeye-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
   
    private ResponseEntity<List<TrakeyeTypeDTO>> searchAllTrakeyeTypes(String searchText,Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TrakeyeTypes for serahc value {}",searchText);
        Page<TrakeyeTypeDTO> page = trakeyeTypeService.findAllBySearchValue(searchText, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trakeye-types/searchvalue/{searchtext}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /trakeye-types/:id : get the "id" trakeyeType.
     *
     * @param id the id of the trakeyeTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trakeyeTypeDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/trakeye-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN,AuthoritiesConstants.USER})
    @ApiOperation(value = "Get trakeye type by id", notes = "User can find trakeye type by id. User with role User Admin is allowed.", response = TrakeyeTypeDTO.class)
    public ResponseEntity<TrakeyeTypeDTO> getTrakeyeType(@PathVariable Long id) {
        log.debug("REST request to get TrakeyeType : {}", id);
        TrakeyeTypeDTO trakeyeTypeDTO = trakeyeTypeService.findOne(id);
        return Optional.ofNullable(trakeyeTypeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /trakeye-types/:id : delete the "id" trakeyeType.
     *
     * @param id the id of the trakeyeTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/trakeye-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.USER_ADMIN)
    @ApiOperation(value = "Delete trakeye type", notes = "User can delete trakeye type by id. User with role User Admin is allowed.", response = Void.class)
    public ResponseEntity<Void> deleteTrakeyeType(@PathVariable Long id) {
        log.debug("REST request to delete TrakeyeType : {}", id);
        trakeyeTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("trakeyeType", id.toString())).build();
    }

}
