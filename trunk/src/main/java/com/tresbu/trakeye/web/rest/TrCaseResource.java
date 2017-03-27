package com.tresbu.trakeye.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.domain.enumeration.CasePriority;
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.service.TrCaseService;
import com.tresbu.trakeye.service.dto.TrCaseCreateDTO;
import com.tresbu.trakeye.service.dto.TrCaseDTO;
import com.tresbu.trakeye.service.dto.TrCaseUpdateDTO;
import com.tresbu.trakeye.service.dto.TrCaseUserDTO;
import com.tresbu.trakeye.web.rest.util.HeaderUtil;
import com.tresbu.trakeye.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiOperation;

/**
 * REST controller for managing TrCase.
 */
@RestController
@RequestMapping("/api")
public class TrCaseResource extends BaseResource{

    private final Logger log = LoggerFactory.getLogger(TrCaseResource.class);
        
    @Inject
    private TrCaseService trCaseService;
    
  
    
    /**
     * POST  /tr-cases : Create a new trCase.
     *
     * @param trCaseDTO the trCaseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trCaseDTO, or with status 400 (Bad Request) if the trCase has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tr-cases",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN,AuthoritiesConstants.USER})
    @ApiOperation(value = "Create new case", notes = "User can create a new case. User with role User Admin and User is allowed.", response = TrCaseDTO.class)
    public ResponseEntity<TrCaseDTO> createTrCase(@Valid @RequestBody TrCaseCreateDTO trCaseCreateDTO) throws URISyntaxException {
        log.debug("REST request to save TrCase : {}", trCaseCreateDTO);
                
        Long user=getCurrentUserLogin();
        if(user == null){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("geofence", "usernotexist", "login user not present")).body(null);
        }
        trCaseCreateDTO.setReportedBy(user);
        TrCaseDTO result = trCaseService.save(trCaseCreateDTO);
        return ResponseEntity.created(new URI("/api/tr-cases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("trCase", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tr-cases : Updates an existing trCase.
     *
     * @param trCaseDTO the trCaseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trCaseDTO,
     * or with status 400 (Bad Request) if the trCaseDTO is not valid,
     * or with status 500 (Internal Server Error) if the trCaseDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tr-cases",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN, AuthoritiesConstants.USER})
    @ApiOperation(value = "Update case", notes = "User can update existing case details. User with role User Admin is allowed.", response = TrCaseDTO.class)
    public ResponseEntity<TrCaseDTO> updateTrCase(@Valid @RequestBody TrCaseUpdateDTO trCaseDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to update TrCase : {}", trCaseDTO);
        if (trCaseDTO.getId() == null) {
        	 return ResponseEntity.badRequest()
        	            .headers(HeaderUtil.createFailureAlert("trCase", "idNotExists", "Id should not be empty"))
        	            .body(null);
        }
        Long user=getCurrentUserLogin();
        trCaseDTO.setUpdatedBy(user);
        TrCaseDTO result = trCaseService.update(trCaseDTO);
        trCaseService.sendNotification(result);
   
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("trCase", trCaseDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tr-cases : get all the trCases.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of trCases in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = {"/tr-cases","/tr-cases/priority/{prority}","/tr-cases/searchvalue/{searchText}"},
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER,AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Get list of all cases", notes = "User can get list of all cases created by him. User with role User Admin and User is allowed.", response = TrCaseDTO.class)
    public ResponseEntity<List<TrCaseDTO>> getAllTrCases(Pageable pageable,@PathVariable("prority") Optional<String> priority,@PathVariable("searchText") Optional<String> searchText)
        throws URISyntaxException {
        log.debug("REST request to get a page of TrCases");
        
        if(searchText.isPresent()){
        	return searchAllTrCases(searchText.get(),pageable);
        }
        else if(priority.isPresent()){
        	return getAllTrCasesByPriority(priority.get(),pageable);
        }
        Page<TrCaseDTO> page = trCaseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tr-cases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /tr-cases/prioritysearch/{prority}/{searchtext} : get all the trCases based on priority and searchtext
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of trCases in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/tr-cases/prioritysearch/{priority}/{searchtext}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER,AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Get list of all cases", notes = "User can get list of all cases created by him. User with role User Admin and User is allowed.", response = TrCaseDTO.class)
    public ResponseEntity<List<TrCaseDTO>> getAllTrCasesByPrioritySearch(@PathVariable("priority") String priority,@PathVariable("searchtext") String searchtext,Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TrCases by priority {} and searchtext {}", priority,searchtext);
        
        Page<TrCaseDTO> page = trCaseService.getAllTrCasesByPriorityAndSearchValue(priority,searchtext,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tr-cases/prioritysearch/{prority}/{searchtext}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    private ResponseEntity<List<TrCaseDTO>> getAllTrCasesByPriority(String priority,Pageable pageable)
            throws URISyntaxException {
            log.debug("REST request to get a page of TrCases based on priority {}", priority);
            Page<TrCaseDTO> page = trCaseService.getAllTrCasesByPriority(priority,pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tr-cases/priority/{prority}");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }

    
    
    private ResponseEntity<List<TrCaseDTO>> searchAllTrCases( String searchText,Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TrCases based on search {}", searchText);
        Page<TrCaseDTO> page = trCaseService.searchAll(searchText,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/tr-cases/search/{searchtext}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
      @RequestMapping(value = "/tr-cases/live-logs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        @Secured({AuthoritiesConstants.USER,AuthoritiesConstants.USER_ADMIN})
      @ApiOperation(value = "Get lits of cases", notes = "User can get list of cases for given user User with role User Admin and User is allowed.", response = TrCaseUserDTO.class)
        public ResponseEntity<List<TrCaseUserDTO>> getAllUserTrCases(Pageable pageable,HttpServletRequest request)
            throws URISyntaxException {
            log.debug("REST request to get a page of TrCases");
            List<TrCaseUserDTO> trCaseUserDTOs = trCaseService.findUserAndTrCases(getCurrentUser().getLogin(),pageable);
            return new ResponseEntity<>(trCaseUserDTOs, HttpStatus.OK);
        }
    
      @RequestMapping(value = "/tr-cases/live-logs/{id}",
              method = RequestMethod.GET,
              produces = MediaType.APPLICATION_JSON_VALUE)
          @Timed
          @Secured({AuthoritiesConstants.USER,AuthoritiesConstants.USER_ADMIN})
      @ApiOperation(value = "Get lits of cases", notes = "User can get list of cases for logged in user. User with role User Admin and User is allowed.", response = TrCaseUserDTO.class)
          public ResponseEntity<List<TrCaseUserDTO>> getUserTrCasesById(@PathVariable String id,HttpServletRequest request)
              throws URISyntaxException {
              log.debug("REST request to get a page of TrCases");
              List<TrCaseUserDTO> trCaseUserDTOs = trCaseService.findCasesForLoggedInCaseSearch(getCurrentUser().getLogin(),id);
              return new ResponseEntity<>(trCaseUserDTOs, HttpStatus.OK);
          }
    
         @RequestMapping(value = "/tr-cases/search/{id}",
              method = RequestMethod.GET,
              produces = MediaType.APPLICATION_JSON_VALUE)
          @Timed
          @Secured({AuthoritiesConstants.USER,AuthoritiesConstants.USER_ADMIN})
         @ApiOperation(value = "Get lits of cases", notes = "Search a Tr Cases using priority,assignedBy,ReportedBy,Geofences . User with role User Admin and User is allowed.", response = TrCaseUserDTO.class)
          public ResponseEntity<List<TrCaseDTO>> searchTrCasesByLogin(@PathVariable String id,HttpServletRequest request)
              throws URISyntaxException {
              log.debug("REST request to get a page of TrCases");
              List<TrCaseDTO> trCaseUserDTOs = trCaseService.searchAllTrCases(id);
              return new ResponseEntity<>(trCaseUserDTOs, HttpStatus.OK);
          }
    /**
     * GET  /tr-cases/:id : get the "id" trCase.
     *
     * @param id the id of the trCaseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trCaseDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/tr-cases/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER,AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Get case by id", notes = "User can find a case by id. User with role User Admin and User is allowed.", response = TrCaseDTO.class)
    public ResponseEntity<TrCaseDTO> getTrCase(@PathVariable Long id) {
        log.debug("REST request to get TrCase : {}", id);
        TrCaseDTO trCaseDTO = trCaseService.findOne(id);
        return Optional.ofNullable(trCaseDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tr-cases/:id : delete the "id" trCase.
     *
     * @param id the id of the trCaseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/tr-cases/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Delete", notes = "User can delete. User with role User Admin is allowed.", response = Void.class)
    public ResponseEntity<Void> deleteTrCase(@PathVariable Long id) {
        log.debug("REST request to delete TrCase : {}", id);
        trCaseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("trCase", id.toString())).build();
    }
    
   /* @RequestMapping(value = "/tr-cases/priority/{prority}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        @Secured({AuthoritiesConstants.USER,AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Get lits of cases based on Priority", notes = "User can get list of cases based on Priority for given user User with role User Admin and User is allowed.", response = TrCaseUserDTO.class)
    public ResponseEntity<List<TrCaseDTO>> getAllTrCasesBySta(@PathVariable("prority") String priority,Pageable pageable,HttpServletRequest request)
        throws URISyntaxException {
        log.debug("REST request to get a page of TrCases");
        Page<TrCaseDTO> page = trCaseService.getAllTrCasesByPriority(priority,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tr-cases/priority/{prority}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }*/
    
    @RequestMapping(value = "/tr-cases/live-logs/priority/{priorityId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        @Secured({AuthoritiesConstants.USER,AuthoritiesConstants.USER_ADMIN})
      @ApiOperation(value = "Get lits of cases and Users By Priority", notes = "User can get list of cases and users based on priority for given user User with role User Admin and User is allowed.", response = TrCaseUserDTO.class)
        public ResponseEntity<List<TrCaseUserDTO>> getAllUserTrCasesByPriority(@PathVariable("priorityId") String priority,Pageable pageable,HttpServletRequest request)
            throws URISyntaxException {
            log.debug("REST request to get a page of TrCases");
            List<TrCaseUserDTO> trCaseUserDTOs = trCaseService.findUserAndTrCasesByPriority(getCurrentUser().getLogin(),priority,pageable);
            return new ResponseEntity<>(trCaseUserDTOs, HttpStatus.OK);
        }
    
    /**
     * GET  /tr-cases : get all the trCases.
     *
     *@return the ResponseEntity with status 200 (OK) and the list of trCases in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = {"/tr-allcases"},
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER,AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Get list of all cases", notes = "User can get list of all cases created by him. User with role User Admin and User is allowed.", response = TrCaseDTO.class)
    public ResponseEntity<List<TrCaseDTO>> getCaseList()
        throws URISyntaxException {
        log.debug("REST request to get a all tr cases");
        
        List<TrCaseDTO> trCaseDTOList = trCaseService.findAllCases();
        return new ResponseEntity<>(trCaseDTOList, HttpStatus.OK);
    }
    
    /**
     * GET  /tr-caseedit : editassigned to user of tr case
     *
     *@return the ResponseEntity with status 200 (OK) and the list of trCases in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = {"/tr-caseedit/{userId}/{caseId}"},
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER,AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Edit tr-case assigned to user", notes = "User can  edit tr-case assigned to user. User with role User Admin and User is allowed.", response = TrCaseDTO.class)
    public ResponseEntity<?> editTrCase(@PathVariable("userId") Long userId, @PathVariable("caseId") Long caseId)
        throws URISyntaxException {
        log.debug("REST request to edit assigned to user of case with id {} and user id {}",caseId,userId);
        
        if (caseId == null) {
       	 return ResponseEntity.badRequest()
       	            .headers(HeaderUtil.createFailureAlert("trCase", "idNotExists", "Case Id should not be empty"))
       	            .body(null);
       }
       if (userId == null) {
          	 return ResponseEntity.badRequest()
          	            .headers(HeaderUtil.createFailureAlert("trCase", "idNotExists", "User Id should not be empty"))
          	            .body(null);
       }
       User loggedInuser=getCurrentUser();
       if(loggedInuser == null){
     	   return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("locationLog", "loggedinusernull", "Logged in user is null")).body(null);
        }
       TrCaseDTO result = trCaseService.editAssignedToUser(userId,caseId,loggedInuser);
       trCaseService.sendNotification(result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
}
