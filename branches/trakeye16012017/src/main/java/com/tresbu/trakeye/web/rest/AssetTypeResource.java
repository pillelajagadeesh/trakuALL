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
import com.tresbu.trakeye.domain.AssetType;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.domain.enumeration.ColorCode;
import com.tresbu.trakeye.domain.enumeration.Layout;
import com.tresbu.trakeye.repository.UserRepository;
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.service.AssetTypeService;
import com.tresbu.trakeye.service.dto.AssetTypeCreateDTO;
import com.tresbu.trakeye.service.dto.AssetTypeDTO;
import com.tresbu.trakeye.service.dto.AssetTypeUpdateDTO;
import com.tresbu.trakeye.web.rest.util.HeaderUtil;
import com.tresbu.trakeye.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiOperation;

/**
 * REST controller for managing AssetType.
 */
@RestController
@RequestMapping("/api")
public class AssetTypeResource extends BaseResource {

    private final Logger log = LoggerFactory.getLogger(AssetTypeResource.class);
        
    @Inject
    private AssetTypeService assetTypeService;
    
    @Inject
    private static UserRepository userRepository;
    
   
    
    private static final String CATALINA_BASE = "catalina.base";
    private static final String WEBAPPS_CUSTOM = "/webapps/custom/";
    private static final String IMAGES = "/images/";
    
    private static final String CUSTOM_ICON = "/custom/icon/";

    /**
     * POST  /asset-types : Create a new assetType.
     *
     * @param assetTypeDTO the assetTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new assetTypeDTO, or with status 400 (Bad Request) if the assetType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/asset-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Create new AssetType ", notes = "User can create AssetType, User with role User Admin is allowed. ", response = Void.class)
    public ResponseEntity<?> createAssetType(@Valid @RequestBody AssetTypeCreateDTO assetTypeDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save AssetType : {}", assetTypeDTO);
//        if (assetTypeDTO.getId() != null) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("assetType", "idexists", "A new assetType cannot already have an ID")).body(null);
//        }
       Long loggedInuser= getCurrentUserLogin();
        if(loggedInuser == null){
      	   return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "loggedinusernull", "Logged in user is null")).body(null);
         }
        AssetTypeDTO existingassetType= assetTypeService.findAssetTypeByName(assetTypeDTO.getName());
        if(existingassetType != null){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("assettype", "assettypealreadyexists", "Asset type with this name already exists")).body(null);
        }
        
        if(assetTypeDTO.getLayout().equals(Layout.FIXED) && assetTypeDTO.getColorcode() !=null){
        	 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "fixedtypeerror", "Fixed asset type can not have color code")).body(null);
        }
        if(assetTypeDTO.getLayout().equals(Layout.SPREAD) && assetTypeDTO.getImage() !=null){
       	 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "spreadtypeerror", "Spread asset type can not have image")).body(null);
       }
        if(assetTypeDTO.getLayout().equals(Layout.FIXED) && assetTypeDTO.getImage() ==null){
        	 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "imagenull", "Fixed asset type should have image")).body(null);
        }
        if(assetTypeDTO.getLayout().equals(Layout.SPREAD) && assetTypeDTO.getColorcode() ==null){
       	 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "colorcodenull", "Spread asset type should have color code")).body(null);
       }
        if(assetTypeDTO.getColorcode() == null && assetTypeDTO.getImage() == null){
       	 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "colorandimagenull", "Asset type color code and asset type image both cannot be null")).body(null);
       	 
       }
       
        assetTypeDTO.setUserId(getCurrentUserLogin());
        AssetTypeDTO result = assetTypeService.save(assetTypeDTO);
        return ResponseEntity.created(new URI("/api/asset-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("assetType", result.getId().toString()))
            .body(null);
    }

    /**
     * PUT  /asset-types : Updates an existing assetType.
     *
     * @param assetTypeDTO the assetTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated assetTypeDTO,
     * or with status 400 (Bad Request) if the assetTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the assetTypeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/asset-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Update AssetType ", notes = "User can update AssetType, User with role User Admin is allowed. ", response = Void.class)
    public ResponseEntity<?> updateAssetType(@Valid @RequestBody AssetTypeUpdateDTO assetTypeDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to update AssetType : {}", assetTypeDTO);
//        if (assetTypeDTO.getId() == null) {
//            return createAssetType(assetTypeDTO);
//        }
        AssetTypeDTO existingassetTypeByName= assetTypeService.findAssetTypeByName(assetTypeDTO.getName());
        if(existingassetTypeByName != null){
        	if(!existingassetTypeByName.getId().equals(assetTypeDTO.getId())){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("assettype", "assettypealreadyexists", "Asset type with this name already exists")).body(null);
        	}
        }
        AssetTypeDTO existingAssetType=assetTypeService.findOne(assetTypeDTO.getId());
        if(existingAssetType != null){
        if(!existingAssetType.getLayout().toString().equalsIgnoreCase(assetTypeDTO.getLayout().toString())){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "cannotupdatelayout", "Asset type layout can not be changed")).body(null);
        }
        }
        
        
        if(assetTypeDTO.getLayout().equals(Layout.FIXED) && assetTypeDTO.getColorcode() !=null){
       	 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "fixedtypeerror", "Fixed asset type can not have color code")).body(null);
       }
       if(assetTypeDTO.getLayout().equals(Layout.SPREAD) && assetTypeDTO.getImage() !=null){
      	 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "spreadtypeerror", "Spread asset type can not have image")).body(null);
      }
       if(assetTypeDTO.getLayout().equals(Layout.FIXED) && assetTypeDTO.getImage() ==null){
      	 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "imagenull", "Fixed asset type should have image")).body(null);
      }
      if(assetTypeDTO.getLayout().equals(Layout.SPREAD) && assetTypeDTO.getColorcode() ==null){
     	 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "colorcodenull", "Spread asset type should have color code")).body(null);
     }
      if(assetTypeDTO.getColorcode() == null && assetTypeDTO.getImage() == null){
     	 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "colorandimagenull", "Asset type color code and asset type image both cannot be null")).body(null);
     	 
     }
      
        AssetTypeDTO result = assetTypeService.update(assetTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("assetType", assetTypeDTO.getId().toString()))
            .body(null);
    }

    /**
     * GET  /asset-types : get all the assetTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of assetTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = {"/asset-types","/asset-types/searchvalue/{searchtext}"},
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN,AuthoritiesConstants.USER})
    @ApiOperation(value = "Get List of all AssetType ", notes = "User can get List AssetType, User with role UserAdmin and User is allowed. ", response = AssetTypeDTO.class)
    public ResponseEntity<List<AssetTypeDTO>> getAllAssetTypes(@PathVariable("searchtext") Optional<String> searchtext, Pageable pageable)
        throws URISyntaxException {
    	
    	if(searchtext.isPresent()){
    		 log.debug("REST request to get a page of AssetTypes based on search value {}",searchtext.get());
        	return searchAllAssetTypes(searchtext.get(), pageable);
        }
        log.debug("REST request to get a page of AssetTypes");
        Page<AssetTypeDTO> page = assetTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/asset-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    private ResponseEntity<List<AssetTypeDTO>> searchAllAssetTypes(String searchText,Pageable pageable)
            throws URISyntaxException {
            log.debug("REST request to get a page of asset types based on search value");
            Page<AssetTypeDTO> page = assetTypeService.findAllBySearch(searchText, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/asset-types/searchvalue/{searchtext}");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }

    /**
     * GET  /asset-types/:id : get the "id" assetType.
     *
     * @param id the id of the assetTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the assetTypeDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/asset-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN,AuthoritiesConstants.USER})
    @ApiOperation(value = "Get AssetType by id ", notes = "User can get AssetType by Id, User with role UserAdmin and User is allowed. ", response = AssetTypeDTO.class)
    public ResponseEntity<AssetTypeDTO> getAssetType(@PathVariable Long id) {
        log.debug("REST request to get AssetType : {}", id);
        AssetTypeDTO assetTypeDTO = assetTypeService.findOne(id);
        return Optional.ofNullable(assetTypeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /asset-types/:id : delete the "id" assetType.
     *
     * @param id the id of the assetTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/asset-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Delete AssetType by id ", notes = "User can delete AssetType by Id, User with role UserAdmin is allowed. ", response = Void.class)
    public ResponseEntity<Void> deleteAssetType(@PathVariable Long id) {
        log.debug("REST request to delete AssetType : {}", id);
        assetTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("assetType", id.toString())).build();
    }
    
   /* @RequestMapping(value = "/asset-typelist",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        @Secured({AuthoritiesConstants.USER_ADMIN,AuthoritiesConstants.USER})
        @ApiOperation(value = "Get List of all AssetType  created by admin or agent's admin", notes = "User can get List AssetType created by admin or agent's admin, User with role UserAdmin and User is allowed. ", response = AssetTypeDTO.class)
        public ResponseEntity<?> getAllAssetTypeCreatedByAgentAdmin()
            throws URISyntaxException {
        	log.debug("REST request to get list  of AssetTypes created by admin or agent's admin");
        	 User loggedInuser=getCurrentUser();
             if(loggedInuser == null){
            	 log.debug("Loggedin user is null");
          	   return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "loggedinusernull", "Logged in user is null")).body(null);
             }
            List<AssetTypeDTO> result = assetTypeService.findAllAssetTypeCreatedByAgentOrAdmin(loggedInuser);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }*/
  


}
