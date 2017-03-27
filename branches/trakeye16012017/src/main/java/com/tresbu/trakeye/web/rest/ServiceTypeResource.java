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
import com.tresbu.trakeye.service.ServiceTypeService;
import com.tresbu.trakeye.service.dto.ServiceTypeCreateDTO;
import com.tresbu.trakeye.service.dto.ServiceTypeDTO;
import com.tresbu.trakeye.service.dto.ServiceTypeUpdateDTO;
import com.tresbu.trakeye.web.rest.util.HeaderUtil;
import com.tresbu.trakeye.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiOperation;

/**
 * REST controller for managing ServiceType.
 */
@RestController
@RequestMapping("/api")
public class ServiceTypeResource extends BaseResource {

    private final Logger log = LoggerFactory.getLogger(ServiceTypeResource.class);
        
    @Inject
    private ServiceTypeService serviceTypeService;

    /**
     * POST  /service-types : Create a new serviceType.
     *
     * @param serviceTypeDTO the serviceTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceTypeDTO, or with status 400 (Bad Request) if the serviceType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/service-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Create new service type", notes = "User can create a new service type. User with role User Admin is allowed.", response = ServiceTypeDTO.class)
    public ResponseEntity<ServiceTypeDTO> createServiceType(@Valid @RequestBody ServiceTypeCreateDTO serviceTypeCreateDTO) throws URISyntaxException {
        log.debug("REST request to save ServiceType : {}", serviceTypeCreateDTO);
//        if (serviceTypeDTO.getId() != null) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("serviceType", "idexists", "A new serviceType cannot already have an ID")).body(null);
//        }
        Long userId=getCurrentUserLogin();
        if(userId == null){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("geofence", "usernotexist", "login user not present")).body(null);
        }
        ServiceTypeDTO existingserviceType= serviceTypeService.findServiceTypeByName(serviceTypeCreateDTO.getName());
        if(existingserviceType != null){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("servicetype", "servicetypealreadyexists", "Service type with this name already exists")).body(null);
        }
        serviceTypeCreateDTO.setUserId(userId);
        ServiceTypeDTO result = serviceTypeService.save(serviceTypeCreateDTO);
        return ResponseEntity.created(new URI("/api/service-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("serviceType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /service-types : Updates an existing serviceType.
     *
     * @param serviceTypeDTO the serviceTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceTypeDTO,
     * or with status 400 (Bad Request) if the serviceTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the serviceTypeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/service-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Update service type", notes = "User can update existing service type. User with role User Admin is allowed.", response = ServiceTypeDTO.class)
    public ResponseEntity<ServiceTypeDTO> updateServiceType(@Valid @RequestBody ServiceTypeUpdateDTO serviceTypeUpdateDTO) throws URISyntaxException {
        log.debug("REST request to update ServiceType : {}", serviceTypeUpdateDTO);
//        if (serviceTypeDTO.getId() == null) {
//            return createServiceType(serviceTypeDTO);
//        }
        ServiceTypeDTO existingserviceType= serviceTypeService.findServiceTypeByName(serviceTypeUpdateDTO.getName());
        if(existingserviceType != null){
        	if(!existingserviceType.getId().equals(serviceTypeUpdateDTO.getId())){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("servicetype", "servicetypealreadyexists", "Service type with this name already exists")).body(null);
        	}
        }
        ServiceTypeDTO result = serviceTypeService.update(serviceTypeUpdateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("serviceType", serviceTypeUpdateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /service-types : get all the serviceTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of serviceTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = {"/service-types","/service-types/searchvalue/{searchtext}"},
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Get List of service types", notes = "User can get list of all service types. User with role User Admin is allowed.", response = ServiceTypeDTO.class)
    public ResponseEntity<List<ServiceTypeDTO>> getAllServiceTypes(@PathVariable("searchtext") Optional<String> searchtext,Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ServiceTypes");
        
        if(searchtext.isPresent()){
        	return searchAllServiceTypes(searchtext.get(), pageable);
        }
        Page<ServiceTypeDTO> page = serviceTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/service-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    
    private ResponseEntity<List<ServiceTypeDTO>> searchAllServiceTypes(String searchText,Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ServiceTypes based on search value {}",searchText);
        Page<ServiceTypeDTO> page = serviceTypeService.findAllBySearchValue(searchText,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/service-types/searchvalue/{searchtext");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /service-types/:id : get the "id" serviceType.
     *
     * @param id the id of the serviceTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceTypeDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/service-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Get service type by id", notes = "User can find service type by id. User with role User Admin is allowed.", response = ServiceTypeDTO.class)
    public ResponseEntity<ServiceTypeDTO> getServiceType(@PathVariable Long id) {
        log.debug("REST request to get ServiceType : {}", id);
        ServiceTypeDTO serviceTypeDTO = serviceTypeService.findOne(id);
        return Optional.ofNullable(serviceTypeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /service-types/:id : delete the "id" serviceType.
     *
     * @param id the id of the serviceTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/service-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Delete service typoe", notes = "User can delete service type by id. User with role User Admin is allowed.", response = Void.class)
    public ResponseEntity<Void> deleteServiceType(@PathVariable Long id) {
        log.debug("REST request to delete ServiceType : {}", id);
        serviceTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("serviceType", id.toString())).build();
    }

}
