package com.tresbu.trakeye.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tresbu.trakeye.service.dto.AssetDTO;
import com.tresbu.trakeye.service.dto.AssetUpdateDTO;

/**
 * Service Interface for managing Asset.
 */
public interface AssetService {

    /**
     * Save a asset.
     *
     * @param assetDTO the entity to save
     * @return the persisted entity
     */
    AssetDTO save(AssetDTO assetDTO);
    
    
    AssetDTO update(AssetUpdateDTO assetDTO);

    /**
     *  Get all the assets.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AssetDTO> findAll(Pageable pageable);
    
    Page<AssetDTO> searchAllAssets(String searchText,Pageable pageable);
    
    List<AssetDTO> searchAssetsForMap(String searchText);

    /**
     *  Get the "id" asset.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AssetDTO findOne(Long id);

    /**
     *  Delete the "id" asset.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    public AssetDTO findAssetByName(String name);
    
    List<AssetDTO> findAllAssetsformap();
}
