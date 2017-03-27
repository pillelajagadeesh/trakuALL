package com.tresbu.trakeye.web.rest;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.FileItem;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.codahale.metrics.annotation.Timed;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.domain.enumeration.Layout;
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.service.AssetService;
import com.tresbu.trakeye.service.AssetTypeService;
import com.tresbu.trakeye.service.dto.AssetCoordinateDTO;
import com.tresbu.trakeye.service.dto.AssetDTO;
import com.tresbu.trakeye.service.dto.AssetTypeAttributeValueDTO;
import com.tresbu.trakeye.service.dto.AssetTypeDTO;
import com.tresbu.trakeye.service.dto.AssetUpdateDTO;
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
    
    @Inject
    private AssetTypeService assetTypeService;

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
        
        AssetDTO existingAsset= assetService.findAssetByName(assetDTO.getName());
        if(existingAsset != null){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "assetalreadyexists", "Asset with this name already exists")).body(null);
        }
        
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
     
     AssetDTO existingAsset= assetService.findAssetByName(assetDTO.getName());
     if(existingAsset != null){
    	 if(!existingAsset.getId().equals(assetDTO.getId())){
     	   return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "assetalreadyexists", "Asset with this name already exists")).body(null);
         }
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
    
    
  
    
    @RequestMapping(value = "/assetsimport", method = RequestMethod.POST,consumes="multipart/form-data",
	   produces = {"application/json","application/xml"})
        @Timed
        @Secured({AuthoritiesConstants.USER_ADMIN})
        @ApiOperation(value = "Import asset", notes = "User can create assets by importing excel file. User with role User Admin is allowed.", response = Void.class)
        public ResponseEntity<?> importAsset(@RequestParam(value="file", required=true) MultipartFile file, 
    			@RequestParam(value="assetname", required=true) String assetname, HttpServletRequest request) throws URISyntaxException, IOException {
            log.debug("REST request to import assets ");
            if(file.isEmpty()){
            	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "assetfileempty", "Asset file is empty")).body(null);
            }
            
            if(!(getFileExtension(file.getOriginalFilename()).equals("xlsx")||getFileExtension(file.getOriginalFilename()).equals("xls"))){
				return ResponseEntity.badRequest()
		                .headers(HeaderUtil.createFailureAlert("upload", "fileisnotxls", "Only xlsx or xls file allowed"))
		                .body(null);
			}
           
         
          
           AssetTypeDTO existingassetType= assetTypeService.findAssetTypeByName(assetname);
            if(existingassetType == null || !existingassetType.getLayout().toString().equals(Layout.SPREAD.toString())){
            	log.debug("assetType is null or existingassetType layout is not spread ");
            	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("assettype", "assettypeisnotpresent", "Asset type with this name not present. Create spread asset  type before creating assets")).body(null);
            }
            
            AssetDTO existingSpreadAsset= assetService.findAssetByName(assetname);
            if(existingSpreadAsset != null){
            	log.debug("Asset with this name already exists ");
            	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "assetalreadyexists", "Asset with this name already exists")).body(null);
            }

           
            //String FILE_NAME = "C:/Users/Admin/Desktop/asset.xlsx";
            List<AssetDTO> assetDTOList = new ArrayList<AssetDTO>();
            List<AssetCoordinateDTO> spreadAssetCoordinates  = new ArrayList<AssetCoordinateDTO>();
            InputStream inputStream =  new BufferedInputStream(file.getInputStream());
       	 try {
       		
               // FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
                Workbook workbook = new XSSFWorkbook(inputStream);
                Sheet datatypeSheet = workbook.getSheetAt(0);
                Iterator<Row> iterator = datatypeSheet.iterator();
                

                while (iterator.hasNext()) {

                    Row currentRow = iterator.next();
                    Iterator<Cell> cellIterator = currentRow.iterator();
                    
                    if (currentRow.getRowNum() == 0) {
                        continue;// skip first row, as it contains column names
                    }
                    
                   
                   AssetDTO assetDTO = new AssetDTO();
                   assetDTO.setId(null);
                   assetDTO.setCreateDate(null);
                   assetDTO.setUpdateDate(null);
                   assetDTO.setUserId(null);
                   AssetCoordinateDTO assetCoordinateDTO = new AssetCoordinateDTO();
                   List<AssetCoordinateDTO> assetCoordinates  = new ArrayList<AssetCoordinateDTO>();
                   Double latitude = null;
                   Double longitude = null;
                   String latlong = null;
                   String assetTypeName  = null;
                    while (cellIterator.hasNext()) {
                        Cell currentCell = cellIterator.next();
                        if (currentCell.getCellTypeEnum() == CellType.BLANK){
                        	 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "wrongformat", "xlsx file is in wromng format")).body(null);
                        }
                        if (currentCell.getCellTypeEnum() == CellType.STRING) {
                        	
                            int columnindex= currentCell.getColumnIndex();
                            switch(columnindex){
                            case 0:
                            	 if(currentCell.getStringCellValue() == null ){
                            		 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "wrongformat", "xlsx file is in wromng format")).body(null);
                            	 }
                            	   assetDTO.setName(currentCell.getStringCellValue());
                                   break;
                            
                            case 2:  if(currentCell.getStringCellValue() == null ){
			                       		 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "wrongformat", "xlsx file is in wromng format")).body(null);
			                       	 }
                            	assetTypeName= currentCell.getStringCellValue();
                                    break;  
                            case 1:     
                            	if(currentCell.getStringCellValue() == null ){
		                       		 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "wrongformat", "xlsx file is in wromng format")).body(null);
		                       	 }
			                        	latlong = currentCell.getStringCellValue();
			                        	String[] arr = latlong.split("/");
			                        	if(arr.length <= 0){
			                        		 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "wrongformat", "xlsx file is in wromng format")).body(null);
			                        	}
			                        	latitude = Double.parseDouble(arr[0]);
			                        	longitude = Double.parseDouble(arr[1]);
			                        	if(latitude.isNaN() || longitude.isNaN() || latitude == null || longitude == null){
			                        		 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "wrongformat", "xlsx file is in wromng format")).body(null);
			                        	}
					                     assetCoordinateDTO.setLatitude(latitude);
					                     assetCoordinateDTO.setLongitude(longitude);
					                     break;   
                            }
                        } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            
                            
                            
                        }
                    }
                    
                    assetCoordinates.add(assetCoordinateDTO);
                    spreadAssetCoordinates.add(assetCoordinateDTO);
                    assetDTO.setAssetCoordinates(assetCoordinates);
                    
                    AssetTypeDTO assetTye = assetTypeService.findAssetTypeByName(assetTypeName);
                    assetDTO.setAssetType(assetTye);
                    
                    Set<AssetTypeAttributeValueDTO> assetTypeAttributeValueDTO = new HashSet<AssetTypeAttributeValueDTO>();
                    assetDTO.setAssetTypeAttributeValues(assetTypeAttributeValueDTO);
                    log.info("imported asset dto {}", assetDTO);
               
                    assetDTOList.add(assetDTO);
                    
                    

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
       	for(AssetDTO asset :assetDTOList ){
          	
          	 AssetDTO existingAsset= assetService.findAssetByName(asset.getName());
             if(existingAsset != null){
             	log.info("Asset with name "+asset.getName()+" can not be created as it already exists");
             } else{
          	 if (asset.getAssetCoordinates().size() <=0) {
          		continue;
                }
                User loggedInuser=getCurrentUser();
                if(loggedInuser == null){
                	continue;
                }
                asset.setUserId(loggedInuser.getId());
                log.info("Creating asset with  asset dto {}", asset);
               
                AssetDTO result = assetService.save(asset);
                
           }
       	}
       	AssetDTO spreadAssetDTO = new AssetDTO();
       	spreadAssetDTO.setId(null);
       	spreadAssetDTO.setCreateDate(null);
       	spreadAssetDTO.setUpdateDate(null);
       	spreadAssetDTO.setUserId(null);
       	spreadAssetDTO.setName(assetname);
       	spreadAssetDTO.setDescription(assetname);
       	spreadAssetDTO.setAssetCoordinates(spreadAssetCoordinates);
        AssetTypeDTO spreadAssetTye = assetTypeService.findAssetTypeByName(assetname);
        spreadAssetDTO.setAssetType(spreadAssetTye);
        Set<AssetTypeAttributeValueDTO> assetTypeAttributeValueDTO = new HashSet<AssetTypeAttributeValueDTO>();
        spreadAssetDTO.setAssetTypeAttributeValues(assetTypeAttributeValueDTO);
        
        if (spreadAssetDTO.getAssetCoordinates().size() <=0) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "coordinatesnull", "Asset coordinates should not be null")).body(null);
           }
           User loggedInuser=getCurrentUser();
           if(loggedInuser == null){
        	   return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asset", "loggedinusernull", "Logged in user is null")).body(null);
           }
           spreadAssetDTO.setUserId(loggedInuser.getId());
           AssetDTO result = assetService.save(spreadAssetDTO);
            
       	return ResponseEntity.ok()
	            .headers(HeaderUtil.createEntityCreationAlert("asset", "Assets imported successfully"))
	            .body("");
          
           
        }
    
    private static String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
    
    
    @RequestMapping(value = {"/assetsformap"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        @Secured({AuthoritiesConstants.USER_ADMIN,AuthoritiesConstants.USER})
        @ApiOperation(value = "Get list of assets", notes = "User can get list of assets. User with role User Admin and User is allowed.", response = AssetDTO.class)
        public ResponseEntity<List<AssetDTO>> getAllAssetsForMap()
            throws URISyntaxException {
        	
        	
            log.debug("REST request to get all Assets to display on graph");
            List<AssetDTO> assets = assetService.findAllAssetsformap();
            log.debug("NO of asssets displaying on map {}",assets.size());
            return new ResponseEntity<>(assets, HttpStatus.OK);
        }

    
}
