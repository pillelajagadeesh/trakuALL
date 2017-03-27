package com.tresbu.trakeye.service;

import java.util.List;

import com.tresbu.trakeye.service.dto.CustomerCreateDTO;
import com.tresbu.trakeye.service.dto.CustomerDTO;
import com.tresbu.trakeye.service.dto.UserUIDTO;

public interface CustomerService {

    /**
     * Save a serviceType.
     *
     * @param serviceTypeDTO the entity to save
     * @return the persisted entity
     */
    CustomerDTO save(CustomerCreateDTO customerCreateDTO);

    /**
     *  Get all the serviceTypes.
     *  
     *  @param List the pagination information
     *  @return the list of entities
     */
    List<CustomerDTO> findAll();

    /**
     *  Get the "id" serviceType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CustomerDTO findOne(Long id);

    /**
     *  Delete the "id" serviceType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    /**
     *  Get the "admin email" serviceType.
     *
     */
    UserUIDTO getUserWithAuthoritySuperAdmin();
    
    CustomerDTO update(CustomerDTO customerDTO);
}
