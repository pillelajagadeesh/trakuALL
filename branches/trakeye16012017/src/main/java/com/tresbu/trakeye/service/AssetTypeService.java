package com.tresbu.trakeye.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.service.dto.AssetTypeCreateDTO;
import com.tresbu.trakeye.service.dto.AssetTypeDTO;
import com.tresbu.trakeye.service.dto.AssetTypeUpdateDTO;

/**
 * Service Interface for managing AssetType.
 */
public interface AssetTypeService {

    /**
     * Save a assetType.
     *
     * @param assetTypeDTO the entity to save
     * @return the persisted entity
     */
    AssetTypeDTO save(AssetTypeCreateDTO assetTypeDTO);

    
    /**
     * Update a assetType.
     *
     * @param assetTypeDTO the entity to save
     * @return the persisted entity
     */
    AssetTypeDTO update(AssetTypeUpdateDTO assetTypeUpdateDTO);
    /**
     *  Get all the assetTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AssetTypeDTO> findAll(Pageable pageable);
    
    Page<AssetTypeDTO> findAllBySearch(String searchText,Pageable pageable);
    
   // List<AssetTypeDTO> findAllAssetTypeCreatedByAgentOrAdmin(User loggedInuser);

    /**
     *  Get the "id" assetType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AssetTypeDTO findOne(Long id);

    /**
     *  Delete the "id" assetType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    public AssetTypeDTO findAssetTypeByName(String name);
}
