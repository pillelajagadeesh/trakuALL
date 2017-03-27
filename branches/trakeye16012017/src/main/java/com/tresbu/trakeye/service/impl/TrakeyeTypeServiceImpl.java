package com.tresbu.trakeye.service.impl;

import java.time.Instant;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tresbu.trakeye.domain.TrakeyeType;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.repository.TrakeyeTypeRepository;
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.security.SecurityUtils;
import com.tresbu.trakeye.service.TrakeyeTypeService;
import com.tresbu.trakeye.service.UserService;
import com.tresbu.trakeye.service.dto.TrakeyeTypeCreateDTO;
import com.tresbu.trakeye.service.dto.TrakeyeTypeDTO;
import com.tresbu.trakeye.service.dto.TrakeyeTypeUpdateDTO;
import com.tresbu.trakeye.service.mapper.TrakeyeTypeMapper;

/**
 * Service Implementation for managing TrakeyeType.
 */
@Service
@Transactional
public class TrakeyeTypeServiceImpl implements TrakeyeTypeService{

    private final Logger log = LoggerFactory.getLogger(TrakeyeTypeServiceImpl.class);
    
    @Inject
    private TrakeyeTypeRepository trakeyeTypeRepository;

    @Inject
    private TrakeyeTypeMapper trakeyeTypeMapper;
    
    @Inject
    private UserService userService;

    /**
     * Save a trakeyeType.
     *
     * @param trakeyeTypeDTO the entity to save
     * @return the persisted entity
     */
    public TrakeyeTypeDTO save(TrakeyeTypeCreateDTO trakeyeTypeCreateDTO) {
        log.debug("Request to save TrakeyeType : {}", trakeyeTypeCreateDTO);
        
        TrakeyeType trakeyeType = trakeyeTypeMapper.trakeyeTypeCreateDTOToTrakeyeType(trakeyeTypeCreateDTO);
        trakeyeType.setCreatedDate(Instant.now().toEpochMilli());
        trakeyeType.setUpdatedDate(Instant.now().toEpochMilli());
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SUPER_ADMIN.toString())){
        	User user = userService.findOne(trakeyeTypeCreateDTO.getUserId());
        	trakeyeType.setTenant(user.getTenant());
        }else{
        	trakeyeType.setTenant(SecurityUtils.getCurrentUserTenant());
        }
        
        trakeyeType = trakeyeTypeRepository.save(trakeyeType);
        TrakeyeTypeDTO result = trakeyeTypeMapper.trakeyeTypeToTrakeyeTypeDTO(trakeyeType);
        return result;
    }

    /**
     *  Get all the trakeyeTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TrakeyeTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TrakeyeTypes");
        Page<TrakeyeType> result = trakeyeTypeRepository.findTrakeyeTypesForLoggedInUser(pageable);
        return result.map(trakeyeType -> trakeyeTypeMapper.trakeyeTypeToTrakeyeTypeDTO(trakeyeType));
    }
    
    @Transactional(readOnly = true) 
    public Page<TrakeyeTypeDTO> findAllBySearchValue(String searchText,Pageable pageable) {
        log.debug("Request to get all TrakeyeTypes by search value {}",searchText);
        Page<TrakeyeType> result = trakeyeTypeRepository.findTrakeyeTypesForLoggedInUserBySearch(searchText,pageable);
        return result.map(trakeyeType -> trakeyeTypeMapper.trakeyeTypeToTrakeyeTypeDTO(trakeyeType));
    }

    /**
     *  Get one trakeyeType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TrakeyeTypeDTO findOne(Long id) {
        log.debug("Request to get TrakeyeType : {}", id);
        TrakeyeType trakeyeType = trakeyeTypeRepository.findTrakeyeTypeById(id);
        if(trakeyeType == null){
        	return null;
        }
        TrakeyeTypeDTO trakeyeTypeDTO = trakeyeTypeMapper.trakeyeTypeToTrakeyeTypeDTO(trakeyeType);
        return trakeyeTypeDTO;
    }

    /**
     *  Delete the  trakeyeType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TrakeyeType : {}", id);
        trakeyeTypeRepository.delete(id);
    }

	@Override
	public TrakeyeTypeDTO update(TrakeyeTypeUpdateDTO trakeyeTypeUpdateDTO) {
		TrakeyeType trakeyeType=trakeyeTypeRepository.findTrakeyeTypeById(trakeyeTypeUpdateDTO.getId());
		if(trakeyeType == null){
			return null;
		}
		trakeyeTypeMapper.updatetrakeyeTypeFromTrakeyeTypeUpdateDTO(trakeyeTypeUpdateDTO, trakeyeType);
		trakeyeType.setUpdatedDate(Instant.now().toEpochMilli());
		trakeyeType=trakeyeTypeRepository.save(trakeyeType);
		TrakeyeTypeDTO result = trakeyeTypeMapper.trakeyeTypeToTrakeyeTypeDTO(trakeyeType);
		return result;
	}
	
	@Transactional(readOnly = true) 
    public TrakeyeTypeDTO findTrakeyeTypeByName(String name) {
        log.debug("Request to get trakeye type : {}", name);
        Optional<TrakeyeType> trakeyeType = trakeyeTypeRepository.findTrakeyeTypeByName(name);
        TrakeyeTypeDTO trakeyeTypeDTO = null;
        if(trakeyeType.isPresent()){
        	trakeyeTypeDTO = trakeyeTypeMapper.trakeyeTypeToTrakeyeTypeDTO(trakeyeType.get());
        }
        return trakeyeTypeDTO;
    }
}
