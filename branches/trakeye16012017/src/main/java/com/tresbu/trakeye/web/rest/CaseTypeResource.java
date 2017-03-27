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
import com.tresbu.trakeye.domain.CaseType;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.service.CaseTypeService;
import com.tresbu.trakeye.service.dto.AssetTypeDTO;
import com.tresbu.trakeye.service.dto.CaseTypeCreateDTO;
import com.tresbu.trakeye.service.dto.CaseTypeDTO;
import com.tresbu.trakeye.service.dto.CaseTypeUpdateDTO;
import com.tresbu.trakeye.web.rest.util.HeaderUtil;
import com.tresbu.trakeye.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiOperation;

/**
 * REST controller for managing CaseType.
 */
@RestController
@RequestMapping("/api")
public class CaseTypeResource extends BaseResource {

    private final Logger log = LoggerFactory.getLogger(CaseTypeResource.class);
        
    @Inject
    private CaseTypeService caseTypeService;

    /**
     * POST  /case-types : Create a new caseType.
     *
     * @param caseTypeDTO the caseTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new caseTypeDTO, or with status 400 (Bad Request) if the caseType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/case-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Create a case type", notes = "Create a new case type. User with role User Admin is allowed. ", response = CaseTypeDTO.class)
    public ResponseEntity<CaseTypeDTO> createCaseType(@Valid @RequestBody CaseTypeCreateDTO caseTypeDTO) throws URISyntaxException {
        log.debug("REST request to save CaseType : {}", caseTypeDTO);
//        if (caseTypeDTO.getId() != null) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("caseType", "idexists", "A new caseType cannot already have an ID")).body(null);
//        }
        Long userId=getCurrentUserLogin();
        if(userId == null){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("geofence", "usernotexist", "login user not present")).body(null);
        }
        CaseTypeDTO existingcaseType= caseTypeService.findCaseTypeByName(caseTypeDTO.getName());
        if(existingcaseType != null){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("casetype", "casetypealreadyexists", "Case type with this name already exists")).body(null);
        }
        caseTypeDTO.setUserId(userId);
        CaseTypeDTO result = caseTypeService.save(caseTypeDTO);
        return ResponseEntity.created(new URI("/api/case-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("caseType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /case-types : Updates an existing caseType.
     *
     * @param caseTypeDTO the caseTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated caseTypeDTO,
     * or with status 400 (Bad Request) if the caseTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the caseTypeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/case-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Update a case type", notes = "User can update existing case type. User with role User Admin is allowed.", response = CaseTypeDTO.class)
    public ResponseEntity<CaseTypeDTO> updateCaseType(@Valid @RequestBody CaseTypeUpdateDTO caseTypeUpdateDTO) throws URISyntaxException {
        log.debug("REST request to update CaseType : {}", caseTypeUpdateDTO);
//        if (caseTypeDTO.getId() == null) {
//            return createCaseType(caseTypeDTO);
//        }
        CaseTypeDTO existingcaseType= caseTypeService.findCaseTypeByName(caseTypeUpdateDTO.getName());
        if(existingcaseType != null){
        	if(!existingcaseType.getId().equals(caseTypeUpdateDTO.getId())){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("casetype", "casetypealreadyexists", "Case type with this name already exists")).body(null);
        }
        }
        CaseTypeDTO result = caseTypeService.update(caseTypeUpdateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("caseType", caseTypeUpdateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /case-types : get all the caseTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of caseTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = {"/case-types","/case-types/searchvalue/{searchtext}"},
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN, AuthoritiesConstants.USER})
    @ApiOperation(value = "Get list of case types", notes = "User can get list of case types. User with role User Admin and User is allowed.", response = CaseTypeDTO.class)
    public ResponseEntity<List<CaseTypeDTO>> getAllCaseTypes(@PathVariable("searchtext") Optional<String> searchtext,Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CaseTypes");
        
        if(searchtext.isPresent()){
        	return searchAllCaseTypes(searchtext.get(), pageable);
        }
        Page<CaseTypeDTO> page = caseTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/case-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    private ResponseEntity<List<CaseTypeDTO>> searchAllCaseTypes(String searchText,Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CaseTypes based on search value");
        Page<CaseTypeDTO> page = caseTypeService.findAllBySearch(searchText, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/case-types/searchvalue/{searchtext}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /case-types/:id : get the "id" caseType.
     *
     * @param id the id of the caseTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the caseTypeDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/case-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN}) 
    @ApiOperation(value = "Get case type by id", notes = "User can find a case type by its id. User with role User Admin is allowed.", response = CaseTypeDTO.class)
    public ResponseEntity<CaseTypeDTO> getCaseType(@PathVariable Long id) {
        log.debug("REST request to get CaseType : {}", id);
        CaseTypeDTO caseTypeDTO = caseTypeService.findOne(id);
        return Optional.ofNullable(caseTypeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * GET  /case-types/:name : get the "name" caseType.
     *
     * @param name the name of the caseTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the caseTypeDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/case-types-details/{name}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN}) 
    @ApiOperation(value = "Get list of case type by name", notes = "User can get list of  case type by its name. User with role User Admin is allowed.", response = CaseTypeDTO.class)
    public ResponseEntity<List<CaseTypeDTO>> getCaseType(@PathVariable String name) {
        log.debug("REST request to get CaseType : {}", name);
        List<CaseTypeDTO> managedUserVM = caseTypeService.findCaseTypeDtos(name);
        if(managedUserVM!=null){
    	    return  new ResponseEntity<>(managedUserVM, HttpStatus.OK);
          }
       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * DELETE  /case-types/:id : delete the "id" caseType.
     *
     * @param id the id of the caseTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/case-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Delete case type", notes = "User can delete case type by its id. User with role User Admin is allowed.", response = Void.class)
    public ResponseEntity<Void> deleteCaseType(@PathVariable Long id) {
        log.debug("REST request to delete CaseType : {}", id);
        caseTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("caseType", id.toString())).build();
    }
    
   /* @RequestMapping(value = "/case-typelist",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        @Secured({AuthoritiesConstants.USER_ADMIN,AuthoritiesConstants.USER})
        @ApiOperation(value = "Get List of all caseType  created by admin or agent's admin", notes = "User can get List CaseType created by admin or agent's admin, User with role UserAdmin and User is allowed. ", response = CaseTypeDTO.class)
        public ResponseEntity<?> getAllCaseTypeCreatedByAgentAdmin()
            throws URISyntaxException {
        	log.debug("REST request to get list  of CaseTypes created by admin or agent's admin");
        	 User loggedInuser=getCurrentUser();
             if(loggedInuser == null){
            	 log.debug("Loggedin user is null");
          	   return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("caseType", "loggedinusernull", "Logged in user is null")).body(null);
             }
            List<CaseTypeDTO> result = caseTypeService.findAllCaseTypeCreatedByAgentOrAdmin(loggedInuser);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }*/

}
