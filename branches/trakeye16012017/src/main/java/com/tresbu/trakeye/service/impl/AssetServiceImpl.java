package com.tresbu.trakeye.service.impl;

import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tresbu.trakeye.domain.Asset;
import com.tresbu.trakeye.domain.AssetCoordinate;
import com.tresbu.trakeye.domain.CaseType;
import com.tresbu.trakeye.domain.TrCase;
import com.tresbu.trakeye.repository.AssetRepository;
import com.tresbu.trakeye.security.SecurityUtils;
import com.tresbu.trakeye.service.AssetService;
import com.tresbu.trakeye.service.dto.AssetDTO;
import com.tresbu.trakeye.service.dto.AssetUpdateDTO;
import com.tresbu.trakeye.service.dto.CaseTypeDTO;
import com.tresbu.trakeye.service.mapper.AssetCoordinateMapper;
import com.tresbu.trakeye.service.mapper.AssetMapper;

/**
 * Service Implementation for managing Asset.
 */
@Service
@Transactional
public class AssetServiceImpl implements AssetService{

    private final Logger log = LoggerFactory.getLogger(AssetServiceImpl.class);
    
    @Inject
    private AssetRepository assetRepository;

    @Inject
    private AssetMapper assetMapper;

    @Inject
    private AssetCoordinateMapper assetCoordinateMapper;
    /**
     * Save a asset.
     *
     * @param assetDTO the entity to save
     * @return the persisted entity
     */
    public AssetDTO save(AssetDTO assetDTO) {
        log.debug("Request to save Asset : {}", assetDTO);
        Asset asset = assetMapper.assetDTOToAsset(assetDTO);
        asset.setCreateDate(Instant.now().toEpochMilli());
        asset.setUpdateDate(Instant.now().toEpochMilli());
        asset.setTenant(SecurityUtils.getCurrentUserTenant());
        
        List<AssetCoordinate> assetCoordinates = asset.getAssetCoordinates();
		for (Iterator<AssetCoordinate> iterator = assetCoordinates.iterator(); iterator.hasNext();) {
			AssetCoordinate assetCoordinate = (AssetCoordinate) iterator.next();
			assetCoordinate.setAsset(asset);
		}
		
		asset = assetRepository.save(asset);
        AssetDTO result = assetMapper.assetToAssetDTO(asset);
        return result;
    }
    
    /**
	 * update a trCase.
	 *
	 * @param trCaseUpdateDTO
	 *            the entity to update
	 * @return the persisted entity
	 */
	public AssetDTO update(AssetUpdateDTO assetUpdateDTO) {
		log.debug("Request to update Asset : {}", assetUpdateDTO);
		Asset asset = assetRepository.findAssetById(assetUpdateDTO.getId());
		if(asset == null){
			return null;
		}
		assetMapper.updateAssetFromAssetDTO(assetUpdateDTO, asset);
		asset.setUpdateDate(Instant.now().toEpochMilli());
		
		List<AssetCoordinate> assetCoordinates = asset.getAssetCoordinates();
		 for (Iterator<AssetCoordinate> iterator = assetCoordinates.iterator(); iterator.hasNext();) {
				AssetCoordinate assetCoordinate = (AssetCoordinate) iterator.next();
				assetCoordinate.setAsset(asset);
		 }
		 asset = assetRepository.save(asset);
		AssetDTO result = assetMapper.assetToAssetDTO(asset);
		return result;
	}

    /**
     *  Get all the assets.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<AssetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Assets");
        Page<Asset> result = assetRepository.findAssetsForLoggedInUser(pageable);
        return result.map(asset -> assetMapper.assetToAssetDTO(asset));
    }
    
    @Transactional(readOnly = true) 
	public Page<AssetDTO> searchAllAssets(String searchText, Pageable pageable) {
		log.debug("In put to the search query {}",searchText);
		Page<Asset> result = assetRepository.findAssetsForLoggedInUser(searchText,pageable);
		return result.map(asset -> assetMapper.assetToAssetDTO(asset));
	}
    
    @Transactional(readOnly = true) 
   	public List<AssetDTO> searchAssetsForMap(String searchText) {
   		log.debug("In put to the searchAssetsForMap query {}",searchText);
   		List<Asset> result = assetRepository.findAssetsForMap(searchText);
   		return assetMapper.assetsToAssetDTOs(result);
   		
   	}
    

    /**
     *  Get one asset by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public AssetDTO findOne(Long id) {
        log.debug("Request to get Asset : {}", id);
        Asset asset = assetRepository.findAssetById(id);
        if(asset == null){
        	return null;
        }
        AssetDTO assetDTO = assetMapper.assetToAssetDTO(asset);
        assetDTO.setAssetCoordinates(assetCoordinateMapper.assetCoordinatesToAssetCoordinateDTOs(asset.getAssetCoordinates()));
        return assetDTO;
    }

    /**
     *  Delete the  asset by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Asset : {}", id);
        assetRepository.delete(id);
    }
    
    
    @Transactional(readOnly = true) 
    public AssetDTO findAssetByName(String name) {
        log.debug("Request to get asset by name : {}", name);
        Optional<Asset> assetType = assetRepository.findAssetByName(name);
        AssetDTO assetDTO = null;
        if(assetType.isPresent()){
        	assetDTO = assetMapper.assetToAssetDTO(assetType.get());
        }
        return assetDTO;
    }
    
    @Transactional(readOnly = true) 
    public List<AssetDTO> findAllAssetsformap() {
        log.debug("Request to get all Assets for map");
        List<Asset> result = assetRepository.findAssetsForMap();
        List<AssetDTO> result1 = assetMapper.assetsToAssetDTOs(result);
        return result1;
    }
}
