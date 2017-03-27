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
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.service.AssetService;
import com.tresbu.trakeye.service.dto.AssetDTO;
import com.tresbu.trakeye.service.dto.AssetUpdateDTO;
import com.tresbu.trakeye.service.dto.TrCaseDTO;
import com.tresbu.trakeye.service.dto.TrCaseUserDTO;
import com.tresbu.trakeye.web.rest.util.HeaderUtil;
import com.tresbu.trakeye.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiOperation;

/**
 * REST controller for managing Asset.
 */
@RestController
@RequestMapping("/api")
public class AssetResource extends BaseResource {

    private final Logger log = LoggerFactory.getLogger(AssetResource.class);
        
    @Inject
    private AssetService assetService;

    /**
     * POST  /assets : Create a new asset.
     *
     * @param assetDTO the assetDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new assetDTO, or with status 400 (Bad Request) if the asset has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/assets",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN,AuthoritiesConstants.USER})
    @ApiOperation(value = "Create asset", notes = "User can create a new asset. User with role User Admin and User is allowed.", response = Void.class)
    public ResponseEntity<?> createAsset(@Valid @RequestBody AssetDTO assetDTO) throws URISyntaxException {
        log.debug("REST request to save Asset : {}", assetDTO);
//        if (assetDTO.getId() != null) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "idexists", "A new asset cannot already have an ID")).body(null);
//        }
        if (assetDTO.getAssetCoordinates().size() <=0) {
         return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "coordinatesnull", "Asset coordinates should not be null")).body(null);
        }
        User loggedInuser=getCurrentUser();
        if(loggedInuser == null){
     	   return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "loggedinusernull", "Logged in user is null")).body(null);
        }
        assetDTO.setUserId(loggedInuser.getId());
        AssetDTO result = assetService.save(assetDTO);
        return ResponseEntity.created(new URI("/api/assets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("asset", result.getId().toString()))
            .body(null);
    }

    /**
     * PUT  /assets : Updates an existing asset.
     *
     * @param assetDTO the assetDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated assetDTO,
     * or with status 400 (Bad Request) if the assetDTO is not valid,
     * or with status 500 (Internal Server Error) if the assetDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/assets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN,AuthoritiesConstants.USER})
    @ApiOperation(value = "Update asset", notes = "User can update existing new asset. User with role User Admin and User is allowed.", response = Void.class)
    public ResponseEntity<?> updateAsset(@Valid @RequestBody AssetUpdateDTO assetDTO) throws URISyntaxException {
        log.debug("REST request to update Asset : {}", assetDTO);
        
     if (assetDTO.getId() == null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "notexists", "Asset with ID not exists")).body(null);
  }
     
        User loggedInuser=getCurrentUser();
        if(loggedInuser == null){
     	   return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "loggedinusernull", "Logged in user is null")).body(null);
        }
        //assetDTO.setUserId(loggedInuser.getId());
        AssetDTO result = assetService.update(assetDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("asset", result.getId().toString()))
            .body(null);
    }

    /**
     * GET  /assets : get all the assets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of assets in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = {"/assets","/assets/searchvalue/{searchText}"},
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN,AuthoritiesConstants.USER})
    @ApiOperation(value = "Get list of assets", notes = "User can get list of assets. User with role User Admin and User is allowed.", response = AssetDTO.class)
    public ResponseEntity<List<AssetDTO>> getAllAssets(@PathVariable("searchText") Optional<String> searchText, Pageable pageable)
        throws URISyntaxException {
    	if(searchText.isPresent()){
    		log.debug("REST request to get a page of Assets based on search value {}",searchText.get() );
        	return searchAllAssets(searchText.get(),pageable);
        }
        log.debug("REST request to get a page of Assets");
        Page<AssetDTO> page = assetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/assets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    private ResponseEntity<List<AssetDTO>> searchAllAssets( String searchText,Pageable pageable)
            throws URISyntaxException {
            log.debug("REST request to get a page of Assets based on search {}", searchText);
            Page<AssetDTO> page = assetService.searchAllAssets(searchText,pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/assets/search/{searchtext}");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }
    
    @RequestMapping(value = "/assets/searchformap/{searchvalue}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        @Secured({AuthoritiesConstants.USER,AuthoritiesConstants.USER_ADMIN})
       @ApiOperation(value = "Get list of assets based on search value", notes = "Search assets based on searhc value. User with role User Admin and User is allowed.", response = AssetDTO.class)
        public ResponseEntity<List<AssetDTO>> searchAssetsForMap(@PathVariable("searchvalue") String searchvalue)
            throws URISyntaxException {
            log.debug("REST request to search assets for map");
            List<AssetDTO> assetsDTOs = assetService.searchAssetsForMap(searchvalue);
            return new ResponseEntity<>(assetsDTOs, HttpStatus.OK);
        }

    /**
     * GET  /assets/:id : get the "id" asset.
     *
     * @param id the id of the assetDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the assetDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/assets/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN,AuthoritiesConstants.USER})
    @ApiOperation(value = "Get a asset by id", notes = "User can get asset by given id. User with role User Admin and User is allowed.", response = AssetDTO.class)
    public ResponseEntity<AssetDTO> getAsset(@PathVariable Long id) {
        log.debug("REST request to get Asset : {}", id);
        AssetDTO assetDTO = assetService.findOne(id);
        return Optional.ofNullable(assetDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /assets/:id : delete the "id" asset.
     *
     * @param id the id of the assetDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/assets/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Delete asset", notes = "User can delete existing asset. User with role User Admin is allowed.", response = Void.class)
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        log.debug("REST request to delete Asset : {}", id);
        assetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("asset", id.toString())).build();
    }

}
