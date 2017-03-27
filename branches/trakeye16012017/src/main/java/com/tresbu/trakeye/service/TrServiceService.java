package com.tresbu.trakeye.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tresbu.trakeye.service.dto.TrCaseDTO;
import com.tresbu.trakeye.service.dto.TrServiceCreateDTO;
import com.tresbu.trakeye.service.dto.TrServiceDTO;
import com.tresbu.trakeye.service.dto.TrServiceUpdateDTO;

/**
 * Service Interface for managing TrService.
 */
public interface TrServiceService {

    /**
     * Save a trService.
     *
     * @param trServiceDTO the entity to save
     * @return the persisted entity
     */
    TrServiceDTO save(TrServiceCreateDTO trServiceCreateDTO);

    /**
     *  Get all the trServices.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TrServiceDTO> findAll(Pageable pageable);
    
    Page<TrServiceDTO> findAllBySearch(String searchText, Pageable pageable);

    /**
     *  Get the "id" trService.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TrServiceDTO findOne(Long id);

    /**
     *  Delete the "id" trService.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    TrServiceDTO update(TrServiceUpdateDTO trServiceUpdateDTO);
    
    List<TrServiceDTO> searchTrSevicesById(String id);
}
