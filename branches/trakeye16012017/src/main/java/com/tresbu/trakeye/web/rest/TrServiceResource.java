package com.tresbu.trakeye.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

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
import com.tresbu.trakeye.domain.TrCase;
import com.tresbu.trakeye.domain.enumeration.ServiceStatus;
import com.tresbu.trakeye.repository.TrCaseRepository;
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.service.TrCaseService;
import com.tresbu.trakeye.service.TrServiceService;
import com.tresbu.trakeye.service.dto.TrCaseDTO;
import com.tresbu.trakeye.service.dto.TrCaseUserDTO;
import com.tresbu.trakeye.service.dto.TrNotificationDTO;
import com.tresbu.trakeye.service.dto.TrServiceCreateDTO;
import com.tresbu.trakeye.service.dto.TrServiceDTO;
import com.tresbu.trakeye.service.dto.TrServiceUpdateDTO;
import com.tresbu.trakeye.web.rest.util.HeaderUtil;
import com.tresbu.trakeye.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiOperation;

/**
 * REST controller for managing TrService.
 */
@RestController
@RequestMapping("/api")
public class TrServiceResource extends BaseResource{

    private final Logger log = LoggerFactory.getLogger(TrServiceResource.class);
        
    @Inject
    private TrServiceService trServiceService;
    
    @Inject
    private TrCaseService trCaseService;
    
    @Inject
	private TrCaseRepository trCaseRepository;

    /**
     * POST  /tr-services : Create a new trService.
     *
     * @param trServiceDTO the trServiceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trServiceDTO, or with status 400 (Bad Request) if the trService has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tr-services",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Create new service", notes = "User can create a new service. User with role User Admin is allowed.", response = TrServiceDTO.class)
    public ResponseEntity<TrServiceDTO> createTrService(@RequestBody TrServiceCreateDTO trServiceCreateDTO) throws URISyntaxException {
        log.debug("REST request to save TrService : {}", trServiceCreateDTO);
//        if (trServiceDTO.getId() != null) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("trService", "idexists", "A new trService cannot already have an ID")).body(null);
//        }
        TrCaseDTO trCase = trCaseService.findOne(trServiceCreateDTO.getTrCase().getId());
		if(trCase == null){
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("trService", "trservicecasenull", "Case is null"))
					.body(null);
		}
      Long userDTO = getCurrentUserLogin();
      trServiceCreateDTO.setReportedBy(userDTO);
      trServiceCreateDTO.setUpdatedBy(userDTO);
      trServiceCreateDTO.setUser(userDTO);
        TrServiceDTO result = trServiceService.save(trServiceCreateDTO);
        return ResponseEntity.created(new URI("/api/tr-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("trService", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tr-services : Updates an existing trService.
     *
     * @param trServiceDTO the trServiceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trServiceDTO,
     * or with status 400 (Bad Request) if the trServiceDTO is not valid,
     * or with status 500 (Internal Server Error) if the trServiceDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tr-services",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN,AuthoritiesConstants.USER})
    @ApiOperation(value = "Update service", notes = "User can update existing service. User with role User Admin and User is allowed.", response = TrServiceDTO.class)
    public ResponseEntity<TrServiceDTO> updateTrService(@RequestBody TrServiceUpdateDTO trServiceUpdateDTO) throws URISyntaxException {
        log.debug("REST request to update TrService : {}", trServiceUpdateDTO);
//        if (trServiceDTO.getId() == null) {
//            return createTrService(trServiceDTO);
//        }
        TrCaseDTO trCase = trCaseService.findOne(trServiceUpdateDTO.getTrCase().getId());
		if(trCase == null){
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("trService", "trservicecasenull", "Case is null"))
					.body(null);
		}
        Long userDTO = getCurrentUserLogin();
        trServiceUpdateDTO.setUpdatedBy(userDTO);
        TrServiceDTO result = trServiceService.update(trServiceUpdateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("trService", trServiceUpdateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tr-services : get all the trServices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of trServices in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = {"/tr-services", "/tr-services/searchvalue/{searchtext}"},
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER,AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "GEt list of services", notes = "User can cget list of all services. User with role User Admin and User is allowed.", response = TrServiceDTO.class)
    public ResponseEntity<List<TrServiceDTO>> getAllTrServices(@PathVariable("searchtext") Optional<String> searchtext, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TrServices");
       
        if(searchtext.isPresent()){
        	return searchAllTrServices(searchtext.get(), pageable);
        }
        Page<TrServiceDTO> page = trServiceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tr-services");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    private ResponseEntity<List<TrServiceDTO>> searchAllTrServices( String searchText,Pageable pageable)
            throws URISyntaxException {
            log.debug("REST request to get a page of TrServices based on search {}", searchText);
            Page<TrServiceDTO> page = trServiceService.findAllBySearch(searchText,pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/tr-services/search/{searchtext}");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tr-services/:id : get the "id" trService.
     *
     * @param id the id of the trServiceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trServiceDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/tr-services/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER,AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Get service by id", notes = "User can find service by id. User with role User Admin and User is allowed.", response = TrServiceDTO.class)
    public ResponseEntity<TrServiceDTO> getTrService(@PathVariable Long id) {
        log.debug("REST request to get TrService : {}", id);
        TrServiceDTO trServiceDTO = trServiceService.findOne(id);
        return Optional.ofNullable(trServiceDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tr-services/:id : delete the "id" trService.
     *
     * @param id the id of the trServiceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/tr-services/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Delete service", notes = "User can delete service by id. User with role User Admin is allowed.", response = Void.class)
    public ResponseEntity<Void> deleteTrService(@PathVariable Long id) {
        log.debug("REST request to delete TrService : {}", id);
        trServiceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("trService", id.toString())).build();
    }
    
    
    @RequestMapping(value = "/tr-services/searchinmap/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	@Secured({ AuthoritiesConstants.USER, AuthoritiesConstants.USER_ADMIN })
	@ApiOperation(value = "Get lits of services", notes = "Search a Tr servicesby service id . User with role User Admin and User is allowed.", response = TrServiceDTO.class)
	public ResponseEntity<List<TrServiceDTO>> searchTrServicesById(@PathVariable String id, HttpServletRequest request)
			throws URISyntaxException {
		log.debug("REST request to search tr service by id in map {}", id);
		List<TrServiceDTO> trServiceDTO = trServiceService.searchTrSevicesById(id);
		return new ResponseEntity<>(trServiceDTO, HttpStatus.OK);
	}

}
