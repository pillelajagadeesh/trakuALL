package com.tresbu.trakeye.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tresbu.trakeye.service.dto.GeofenceCreateDTO;
import com.tresbu.trakeye.service.dto.GeofenceDTO;
import com.tresbu.trakeye.service.dto.GeofenceSearchDTO;
import com.tresbu.trakeye.service.dto.GeofenceUpdateDTO;

/**
 * Service Interface for managing Geofence.
 */
public interface GeofenceService {

    /**
     * Save a geofence.
     *
     * @param geofenceDTO the entity to save
     * @return the persisted entity
     */
    GeofenceDTO save(GeofenceCreateDTO geofenceCreateDTO);

    /**
     *  Get all the geofences.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<GeofenceDTO> findAll(Pageable pageable);
    
    Page<GeofenceDTO> findAllBySearchValue(String searchText,Pageable pageable);

    /**
     *  Get the "id" geofence.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    GeofenceDTO findOne(Long id);
    
    public GeofenceDTO findGeofenceByName(String name);

    /**
     *  Delete the "id" geofence.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

	List<GeofenceSearchDTO> searchGeofencesByName();
	
	GeofenceDTO update(GeofenceUpdateDTO geofenceUpdateDTO);
}
