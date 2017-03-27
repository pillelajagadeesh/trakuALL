package com.tresbu.trakeye.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tresbu.trakeye.service.dto.ServiceTypeCreateDTO;
import com.tresbu.trakeye.service.dto.ServiceTypeDTO;
import com.tresbu.trakeye.service.dto.ServiceTypeUpdateDTO;

/**
 * Service Interface for managing ServiceType.
 */
public interface ServiceTypeService {

    /**
     * Save a serviceType.
     *
     * @param serviceTypeDTO the entity to save
     * @return the persisted entity
     */
    ServiceTypeDTO save(ServiceTypeCreateDTO serviceTypeCreateDTO);

    /**
     *  Get all the serviceTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ServiceTypeDTO> findAll(Pageable pageable);
    
    Page<ServiceTypeDTO> findAllBySearchValue(String searchText,Pageable pageable);

    /**
     *  Get the "id" serviceType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ServiceTypeDTO findOne(Long id);

    /**
     *  Delete the "id" serviceType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    ServiceTypeDTO update(ServiceTypeUpdateDTO serviceTypeUpdateDTO);
    
    public ServiceTypeDTO findServiceTypeByName(String name);
}
