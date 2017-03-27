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


import com.tresbu.trakeye.domain.ServiceType;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.repository.ServiceTypeRepository;
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.security.SecurityUtils;
import com.tresbu.trakeye.service.ServiceTypeService;
import com.tresbu.trakeye.service.UserService;
import com.tresbu.trakeye.service.dto.ServiceTypeCreateDTO;
import com.tresbu.trakeye.service.dto.ServiceTypeDTO;
import com.tresbu.trakeye.service.dto.ServiceTypeUpdateDTO;
import com.tresbu.trakeye.service.mapper.ServiceTypeMapper;

/**
 * Service Implementation for managing ServiceType.
 */
@Service
@Transactional
public class ServiceTypeServiceImpl implements ServiceTypeService{

    private final Logger log = LoggerFactory.getLogger(ServiceTypeServiceImpl.class);
    
    @Inject
    private ServiceTypeRepository serviceTypeRepository;

    @Inject
    private ServiceTypeMapper serviceTypeMapper;

    @Inject
    private UserService userService;

    /**
     * Save a serviceType.
     *
     * @param serviceTypeDTO the entity to save
     * @return the persisted entity
     */
    public ServiceTypeDTO save(ServiceTypeCreateDTO serviceTypeCreateDTO) {
        log.debug("Request to save ServiceType : {}", serviceTypeCreateDTO);
        ServiceType serviceType = serviceTypeMapper.serviceTypeCreateDTOToServiceType(serviceTypeCreateDTO);
        serviceType.setCreatedDate(Instant.now().toEpochMilli());
        serviceType.setUpdatedDate(Instant.now().toEpochMilli());
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SUPER_ADMIN.toString())){
        	User user = userService.findOne(serviceTypeCreateDTO.getUserId());
        	serviceType.setTenant(user.getTenant());
        }else{
        	serviceType.setTenant(SecurityUtils.getCurrentUserTenant());
        }
        
        serviceType = serviceTypeRepository.save(serviceType);
        ServiceTypeDTO result = serviceTypeMapper.serviceTypeToServiceTypeDTO(serviceType);
        return result;
    }

    /**
     *  Get all the serviceTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ServiceTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceTypes");
        Page<ServiceType> result = serviceTypeRepository.findServiceTypeForLoggedInUser(pageable);
        return result.map(serviceType -> serviceTypeMapper.serviceTypeToServiceTypeDTO(serviceType));
    }
    
    @Transactional(readOnly = true) 
    public Page<ServiceTypeDTO> findAllBySearchValue(String searchText,Pageable pageable) {
        log.debug("Request to get all ServiceTypes based on search value {}",searchText);
        Page<ServiceType> result = serviceTypeRepository.findServiceTypeForLoggedInUserBySearch(searchText,pageable);
        return result.map(serviceType -> serviceTypeMapper.serviceTypeToServiceTypeDTO(serviceType));
    }

    /**
     *  Get one serviceType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ServiceTypeDTO findOne(Long id) {
        log.debug("Request to get ServiceType : {}", id);
        ServiceType serviceType = serviceTypeRepository.findServiceTypeById(id);
        if(serviceType == null){
        	return null;
        }
        ServiceTypeDTO serviceTypeDTO = serviceTypeMapper.serviceTypeToServiceTypeDTO(serviceType);
        return serviceTypeDTO;
    }

    /**
     *  Delete the  serviceType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ServiceType : {}", id);
        serviceTypeRepository.delete(id);
    }

	@Override
	public ServiceTypeDTO update(ServiceTypeUpdateDTO serviceTypeUpdateDTO) {
		ServiceType serviceType = serviceTypeRepository.findServiceTypeById(serviceTypeUpdateDTO.getId());
		if(serviceType == null){
			return null;
		}
		serviceTypeMapper.updateserviceTypeFromServiceTypeUpdateDTO(serviceTypeUpdateDTO, serviceType);
		serviceType.setUpdatedDate(Instant.now().toEpochMilli());
		serviceType=serviceTypeRepository.save(serviceType);
		ServiceTypeDTO result = serviceTypeMapper.serviceTypeToServiceTypeDTO(serviceType);
		return result;
	}
	
	@Transactional(readOnly = true) 
    public ServiceTypeDTO findServiceTypeByName(String name) {
        log.debug("Request to get service type : {}", name);
        Optional<ServiceType> serviceType = serviceTypeRepository.findServiceTypeByName(name);
        ServiceTypeDTO serviceTypeDTO = null;
        if(serviceType.isPresent()){
        	serviceTypeDTO = serviceTypeMapper.serviceTypeToServiceTypeDTO(serviceType.get());
        }
        return serviceTypeDTO;
    }
}
