package com.tresbu.trakeye.service;

import com.tresbu.trakeye.service.dto.AssetTypeDTO;
import com.tresbu.trakeye.service.dto.TenantDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Tenant.
 */
public interface TenantService {

    /**
     * Save a tenant.
     *
     * @param tenantDTO the entity to save
     * @return the persisted entity
     */
    TenantDTO save(TenantDTO tenantDTO);
    TenantDTO update(TenantDTO tenantDTO);

    /**
     *  Get all the tenants.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TenantDTO> findAll(Pageable pageable);
    
    Page<TenantDTO> findAllBySearch(String searchText,Pageable pageable);

    /**
     *  Get the "id" tenant.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TenantDTO findOne(Long id);

    /**
     *  Delete the "id" tenant.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
