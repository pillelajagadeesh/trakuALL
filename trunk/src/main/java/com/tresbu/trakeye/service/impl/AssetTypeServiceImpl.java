package com.tresbu.trakeye.service.impl;

import java.time.Instant;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tresbu.trakeye.domain.AssetType;
import com.tresbu.trakeye.domain.enumeration.Layout;
import com.tresbu.trakeye.repository.AssetTypeRepository;
import com.tresbu.trakeye.service.AssetTypeService;
import com.tresbu.trakeye.service.dto.AssetTypeCreateDTO;
import com.tresbu.trakeye.service.dto.AssetTypeDTO;
import com.tresbu.trakeye.service.dto.AssetTypeUpdateDTO;
import com.tresbu.trakeye.service.mapper.AssetTypeMapper;


/**
 * Service Implementation for managing AssetType.
 */
@Service
@Transactional
public class AssetTypeServiceImpl implements AssetTypeService{

    private final Logger log = LoggerFactory.getLogger(AssetTypeServiceImpl.class);
    
    @Inject
    private AssetTypeRepository assetTypeRepository;

    @Inject
    private AssetTypeMapper assetTypeMapper;

    /**
     * Save a assetType.
     *
     * @param assetTypeDTO the entity to save
     * @return the persisted entity
     */
    public AssetTypeDTO save(AssetTypeCreateDTO assetTypeDTO) {
        log.debug("Request to save AssetType : {}", assetTypeDTO);
        AssetType assetType = assetTypeMapper.assetTypeCreateDTOToAssetType(assetTypeDTO);
        if(assetTypeDTO.getLayout().toString().equals(Layout.FIXED.toString())){
        	 assetType.setImage(assetTypeDTO.getImage());
        }else if (assetTypeDTO.getLayout().toString().equals(Layout.SPREAD.toString())){
        	assetType.setColorcode(assetTypeDTO.getColorcode().toString());
        }
        assetType.setUpdateDate(Instant.now().toEpochMilli());
        assetType.setCreateDate(Instant.now().toEpochMilli());
	    assetType = assetTypeRepository.save(assetType);
        AssetTypeDTO result = assetTypeMapper.assetTypeToAssetTypeDTO(assetType);
        return result;
    }
    
    @Override
	public AssetTypeDTO update(AssetTypeUpdateDTO assetTypeUpdateDTO) {
    	log.debug("Request to Update AssetType : {}", assetTypeUpdateDTO);
    	AssetType assetType=assetTypeRepository.findOne(assetTypeUpdateDTO.getId());
    	assetTypeMapper.updateassetTypeFromAssetTypeUpdateDTO(assetTypeUpdateDTO,assetType);
    	if(assetTypeUpdateDTO.getLayout().toString().equals(Layout.FIXED.toString())){
    		assetType.setColorcode(null);
    		assetType.setImage(assetTypeUpdateDTO.getImage());
    	}else if (assetTypeUpdateDTO.getLayout().toString().equals(Layout.SPREAD.toString())){
    		assetType.setImage(null);
    	   assetType.setColorcode(assetTypeUpdateDTO.getColorcode().toString());
    	}
    	assetType.setUpdateDate(Instant.now().toEpochMilli());
    	assetType = assetTypeRepository.save(assetType);
    	AssetTypeDTO result = assetTypeMapper.assetTypeToAssetTypeDTO(assetType);
		return result;
	}

    /**
     *  Get all the assetTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true) 
    public Page<AssetTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetTypes");
        Page<AssetType> result = assetTypeRepository.findAssetTypeForLoggedInUser(pageable);
        return result.map(assetType -> assetTypeMapper.assetTypeToAssetTypeDTO(assetType));
    }
    
    @Override
    @Transactional(readOnly = true)
	public Page<AssetTypeDTO> findAllBySearch(String searchText,Pageable pageable) {
		log.debug("Request to get all Asset Types by search value and the search value is {}",searchText);
		Page<AssetType> result = assetTypeRepository.findAssetTypeForLoggedInUserBySearchValue(searchText,pageable);
		return result.map(assetType -> assetTypeMapper.assetTypeToAssetTypeDTO(assetType));
	}

    /**
     *  Get one assetType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public AssetTypeDTO findOne(Long id) {
        log.debug("Request to get AssetType : {}", id);
        AssetType assetType = assetTypeRepository.findOne(id);
        AssetTypeDTO assetTypeDTO = assetTypeMapper.assetTypeToAssetTypeDTO(assetType);
        return assetTypeDTO;
    }

    /**
     *  Delete the  assetType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AssetType : {}", id);
        assetTypeRepository.delete(id);
    }

	
}
