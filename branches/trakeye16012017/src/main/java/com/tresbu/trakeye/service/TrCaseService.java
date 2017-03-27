package com.tresbu.trakeye.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tresbu.trakeye.domain.TrCase;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.domain.enumeration.CasePriority;
import com.tresbu.trakeye.service.dto.TrCaseCreateDTO;
import com.tresbu.trakeye.service.dto.TrCaseDTO;
import com.tresbu.trakeye.service.dto.TrCaseUpdateDTO;
import com.tresbu.trakeye.service.dto.TrCaseUserDTO;

/**
 * Service Interface for managing TrCase.
 */
public interface TrCaseService {

    /**
     * Save a trCase.
     *
     * @param trCaseDTO the entity to save
     * @return the persisted entity
     */
    TrCaseDTO save(TrCaseCreateDTO trCaseDTO);
    /**
     * update a trCase.
     *
     * @param trCaseDTO the entity to update
     * @return the persisted entity
     */
     TrCaseDTO update(TrCaseUpdateDTO trCaseDTO);

    /**
     *  Get all the trCases.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TrCaseDTO> findAll(Pageable pageable);
    
    Page<TrCaseDTO> searchAll(String searchText,Pageable pageable);
    
    Page<TrCaseDTO> getAllTrCasesByPriorityAndSearchValue(String priority, String searchText,Pageable pageable);

    /**
     *  Get the "id" trCase.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TrCaseDTO findOne(Long id);

    /**
     *  Delete the "id" trCase.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<TrCaseUserDTO> findUserAndTrCases(String login,Pageable pageable);
    
    List<TrCaseUserDTO> findCasesForLoggedInCaseSearch(String login,String id);
   
    List<TrCaseDTO> searchAllTrCases(String id);
    
    Page<TrCaseDTO> getAllTrCasesByPriority(String priority,Pageable pageable);
    
    void sendNotification(String OldAssignedUser, TrCaseDTO result);
    
    List<TrCaseUserDTO> findUserAndTrCasesByPriority(String login,String priority,Pageable pageable);
    
    List<TrCaseDTO> findAllCases();
    
    TrCaseDTO editAssignedToUser(Long userId, Long caseId, User loggedInuser);
    
    Map<String, String> caseCount();
    
    List<TrCaseDTO> casesAssignedToUserToday(Long assigneToId);
}
