package com.tresbu.trakeye.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tresbu.trakeye.service.dto.TrakeyeTypeCreateDTO;
import com.tresbu.trakeye.service.dto.TrakeyeTypeDTO;
import com.tresbu.trakeye.service.dto.TrakeyeTypeUpdateDTO;

/**
 * Service Interface for managing TrakeyeType.
 */
public interface TrakeyeTypeService {

    /**
     * Save a trakeyeType.
     *
     * @param trakeyeTypeDTO the entity to save
     * @return the persisted entity
     */
    TrakeyeTypeDTO save(TrakeyeTypeCreateDTO trakeyeTypeCreateDTO);

    /**
     *  Get all the trakeyeTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TrakeyeTypeDTO> findAll(Pageable pageable);
    
    Page<TrakeyeTypeDTO> findAllBySearchValue(String searchText,Pageable pageable);

    /**
     *  Get the "id" trakeyeType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TrakeyeTypeDTO findOne(Long id);

    /**
     *  Delete the "id" trakeyeType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    TrakeyeTypeDTO update(TrakeyeTypeUpdateDTO trakeyeTypeUpdateDTO);
    
    public TrakeyeTypeDTO findTrakeyeTypeByName(String name);
}
