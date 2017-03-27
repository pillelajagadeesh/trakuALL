package com.tresbu.trakeye.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tresbu.trakeye.config.TrakEyeProperties;
import com.tresbu.trakeye.domain.Tenant;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.repository.UserRepository;
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.security.SecurityUtils;
import com.tresbu.trakeye.service.MailService;
import com.tresbu.trakeye.service.TenantService;
import com.tresbu.trakeye.web.rest.util.HeaderUtil;
import com.tresbu.trakeye.web.rest.util.PaginationUtil;
import com.tresbu.trakeye.web.rest.vm.LoginVM;
import com.tresbu.trakeye.web.rest.vm.ManagedUserVM;
import com.tresbu.trakeye.service.dto.CollabTenantDTO;
import com.tresbu.trakeye.service.dto.CollabUserDTO;
import com.tresbu.trakeye.service.dto.ResponseAuthunticationDTO;
import com.tresbu.trakeye.service.dto.TenantDTO;
import com.tresbu.trakeye.service.util.CollabUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Tenant.
 */
@RestController
@RequestMapping("/api")
public class TenantResource {

    private final Logger log = LoggerFactory.getLogger(TenantResource.class);
        
    @Inject
    private TenantService tenantService;
    
    @Inject
    private MailService mailService;
    
    @Inject 
    private UserRepository userRepository;
    
    @Inject
    private TrakEyeProperties jHipsterProperties;

    /**
     * POST  /tenants : Create a new tenant.
     *
     * @param tenantDTO the tenantDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tenantDTO, or with status 400 (Bad Request) if the tenant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tenants",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.SUPER_ADMIN})
    public ResponseEntity<TenantDTO> createTenant(@Valid @RequestBody TenantDTO tenantDTO) throws URISyntaxException {
        log.debug("REST request to save Tenant : {}", tenantDTO);
        if (tenantDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tenant", "idexists", "A new tenant cannot already have an ID")).body(null);
        }
        
        
        if (userRepository.findOneByLogin(tenantDTO.getLoginName()).isPresent()) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "Login already in use"))
					.body(null);
		}
        TenantDTO result = tenantService.save(tenantDTO);
     // call collab api's only if collabEnabled is true
        if(jHipsterProperties.getCollab().isCollabEnabled()){
        	//collab admin login
            String collabAdminJwtToken = CollabUtil.collabAdminAuthentication(jHipsterProperties.getCollab().getCollabBaseUrl(), jHipsterProperties.getCollab().getCollabSuperAdminLogin(), jHipsterProperties.getCollab().getCollabSuperAdminPassword());
        	if(collabAdminJwtToken != null){
        		 ResponseEntity<CollabTenantDTO> collabTenant= CollabUtil.collabTenantCreate(jHipsterProperties.getCollab().getCollabBaseUrl(),collabAdminJwtToken, result);
        	}
        }
        
        Optional<User> optionalUser = userRepository.findOneByLogin(tenantDTO.getLoginName());
        if (optionalUser.isPresent()){
        	mailService.sendCreationEmail(optionalUser.get());
        }
        
        
        
        return ResponseEntity.created(new URI("/api/tenants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tenant", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tenants : Updates an existing tenant.
     *
     * @param tenantDTO the tenantDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tenantDTO,
     * or with status 400 (Bad Request) if the tenantDTO is not valid,
     * or with status 500 (Internal Server Error) if the tenantDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tenants",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.SUPER_ADMIN})
    public ResponseEntity<TenantDTO> updateTenant(@Valid @RequestBody TenantDTO tenantDTO) throws URISyntaxException {
        log.debug("REST request to update Tenant : {}", tenantDTO);
        if (tenantDTO.getId() == null) {
            return createTenant(tenantDTO);
        }
        TenantDTO result = tenantService.update(tenantDTO);
     
     // call collab api's only if collabEnabled is true
        if(jHipsterProperties.getCollab().isCollabEnabled()){
        	String collabAdminJwtToken = CollabUtil.collabAdminAuthentication(jHipsterProperties.getCollab().getCollabBaseUrl(), jHipsterProperties.getCollab().getCollabSuperAdminLogin(), jHipsterProperties.getCollab().getCollabSuperAdminPassword());
            if(collabAdminJwtToken != null){
        		// find collab tenant by login name
                ResponseEntity<CollabTenantDTO> collabTenant= CollabUtil.findCollabTenantByName(jHipsterProperties.getCollab().getCollabBaseUrl(), collabAdminJwtToken, result.getLoginName());
                if(collabTenant != null){
                	if(collabTenant.getBody() != null){
                	// update collab tenant
                    ResponseEntity<CollabTenantDTO> collabUpdatedTenant= CollabUtil.collabTenantUpdate(jHipsterProperties.getCollab().getCollabBaseUrl(), collabAdminJwtToken, result, collabTenant.getBody());
                	}
                }
        	}
        }
    
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tenant", tenantDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tenants : get all the tenants.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tenants in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = {"/tenants","/tenants/searchvalue/{searchtext}"},
    	method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.SUPER_ADMIN})
    public ResponseEntity<List<TenantDTO>> getAllTenants(@PathVariable("searchtext") Optional<String> searchtext, Pageable pageable)
        throws URISyntaxException {
    	
    	if(searchtext.isPresent()){
   		 log.debug("REST request to get a page of tenanta based on search value {}",searchtext.get());
       	return searchAllTenants(searchtext.get(), pageable);
       }
        log.debug("REST request to get a page of Tenants");
       
      
        Page<TenantDTO> page = tenantService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tenants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    private ResponseEntity<List<TenantDTO>> searchAllTenants(String searchText,Pageable pageable)
            throws URISyntaxException {
            log.debug("REST request to get a page of tenants  based on search value");
            Page<TenantDTO> page = tenantService.findAllBySearch(searchText, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tenants/searchvalue/{searchtext}");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }

    /**
     * GET  /tenants/:id : get the "id" tenant.
     *
     * @param id the id of the tenantDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tenantDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/tenants/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.SUPER_ADMIN})
    public ResponseEntity<TenantDTO> getTenant(@PathVariable Long id) {
        log.debug("REST request to get Tenant : {}", id);
        TenantDTO tenantDTO = tenantService.findOne(id);
        return Optional.ofNullable(tenantDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tenants/:id : delete the "id" tenant.
     *
     * @param id the id of the tenantDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/tenants/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.SUPER_ADMIN})
    public ResponseEntity<Void> deleteTenant(@PathVariable Long id) {
        log.debug("REST request to delete Tenant : {}", id);
        tenantService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tenant", id.toString())).build();
    }
    
   

}
