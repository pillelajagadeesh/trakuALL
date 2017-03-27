package com.tresbu.trakeye.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tresbu.trakeye.domain.AssetType;
import com.tresbu.trakeye.domain.Authority;
import com.tresbu.trakeye.domain.TrCase;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.domain.enumeration.Layout;
import com.tresbu.trakeye.repository.AssetTypeRepository;
import com.tresbu.trakeye.repository.UserRepository;
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.security.SecurityUtils;
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
    private UserRepository userRepository;

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
        assetType.setTenant(SecurityUtils.getCurrentUserTenant());
	    assetType = assetTypeRepository.save(assetType);
        AssetTypeDTO result = assetTypeMapper.assetTypeToAssetTypeDTO(assetType);
        return result;
    }
    
    @Override
	public AssetTypeDTO update(AssetTypeUpdateDTO assetTypeUpdateDTO) {
    	log.debug("Request to Update AssetType : {}", assetTypeUpdateDTO);
    	AssetType assetType=assetTypeRepository.findAssetTypeById(assetTypeUpdateDTO.getId());
    	if(assetType== null){
    		return null;
    	}
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
        AssetType assetType = assetTypeRepository.findAssetTypeById(id);
        if(assetType == null){
        	return null;
        }
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
    
    @Transactional(readOnly = true) 
    public AssetTypeDTO findAssetTypeByName(String name) {
        log.debug("Request to get asset type : {}", name);
        Optional<AssetType> assetType = assetTypeRepository.findAssetTypeByName(name);
        AssetTypeDTO assetTypeDTO = null;
        if(assetType.isPresent()){
        	assetTypeDTO = assetTypeMapper.assetTypeToAssetTypeDTO(assetType.get());
        }
        return assetTypeDTO;
    }
    
   /* @Override
    @Transactional(readOnly = true) 
    public List<AssetTypeDTO> findAllAssetTypeCreatedByAgentOrAdmin(User loggedInuser) {
        log.debug("Request to get all AssetTypes");
        for(Authority authority : loggedInuser.getAuthorities()){
        	if(authority.getName().toString().equals(AuthoritiesConstants.USER_ADMIN.toString())){
        		List<AssetType> result = assetTypeRepository.findAllAssetTypeCreatedByAgentOrAdmin(loggedInuser.getId());
                return assetTypeMapper.assetTypesToAssetTypeDTOs(result);
        	}
        }
        Optional<User> agentAdmin = userRepository.findOneByLogin(loggedInuser.getCreatedBy());
    	List<AssetType> result = assetTypeRepository.findAllAssetTypeCreatedByAgentOrAdmin(agentAdmin.get().getId());
    	return assetTypeMapper.assetTypesToAssetTypeDTOs(result);
        	
        if(loggedInuser.getAuthorities().contains(AuthoritiesConstants.USER_ADMIN)){
        	List<AssetType> result = assetTypeRepository.findAllAssetTypeCreatedByAgentOrAdmin(loggedInuser.getId());
            return assetTypeMapper.assetTypesToAssetTypeDTOs(result);
        }else{
        	Optional<User> agentAdmin = userRepository.findOneByLogin(loggedInuser.getCreatedBy());
        	List<AssetType> result = assetTypeRepository.findAllAssetTypeCreatedByAgentOrAdmin(agentAdmin.get().getId());
        	return assetTypeMapper.assetTypesToAssetTypeDTOs(result);
        }
        
    }*/

	
}
