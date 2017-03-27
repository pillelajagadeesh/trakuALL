package com.tresbu.trakeye.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.service.dto.TrNotificationDTO;
import com.tresbu.trakeye.service.dto.TrNotificationUpdateDTO;

/**
 * Service Interface for managing TrNotification.
 */
public interface TrNotificationService {

    /**
     * Save a trNotification.
     *
     * @param trNotificationDTO the entity to save
     * @return the persisted entity
     */
    TrNotificationDTO save(TrNotificationDTO trNotificationDTO);

    
    /**
     *  Get all the trNotifications.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TrNotificationDTO> findAll(String searchText,Pageable pageable);

    /**
     *  Get the "id" trNotification.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TrNotificationDTO findOne(Long id);

    /**
     *  Delete the "id" trNotification.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    void sendNotifications();
    
    void sendNotification(User user,String fileName,String iosFileName,HttpServletRequest request);
    
    TrNotificationDTO update(TrNotificationUpdateDTO trNotificationUpdateDTO);
}
