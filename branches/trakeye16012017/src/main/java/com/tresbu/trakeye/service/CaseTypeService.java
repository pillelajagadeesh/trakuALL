package com.tresbu.trakeye.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.service.dto.CaseTypeCreateDTO;
import com.tresbu.trakeye.service.dto.CaseTypeDTO;
import com.tresbu.trakeye.service.dto.CaseTypeUpdateDTO;


/**
 * Service Interface for managing CaseType.
 */
public interface CaseTypeService {

    /**
     * Save a caseType.
     *
     * @param caseTypeDTO the entity to save
     * @return the persisted entity
     */
    CaseTypeDTO save(CaseTypeCreateDTO caseTypeCreateDTO);
    
    List<CaseTypeDTO> findCaseTypeDtos(String name);

    /**
     *  Get all the caseTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CaseTypeDTO> findAll(Pageable pageable);
    
    Page<CaseTypeDTO> findAllBySearch(String searchText,Pageable pageable);

    /**
     *  Get the "id" caseType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CaseTypeDTO findOne(Long id);

    /**
     *  Delete the "id" caseType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    CaseTypeDTO update(CaseTypeUpdateDTO caseTypeUpdateDTO);
    
    public CaseTypeDTO findCaseTypeByName(String name);
    
   // List<CaseTypeDTO> findAllCaseTypeCreatedByAgentOrAdmin(User loggedInuser);
}
