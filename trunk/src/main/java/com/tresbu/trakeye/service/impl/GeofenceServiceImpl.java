package com.tresbu.trakeye.service.impl;

import java.time.Instant;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tresbu.trakeye.domain.Geofence;
import com.tresbu.trakeye.repository.GeofenceRepository;
import com.tresbu.trakeye.service.GeofenceService;
import com.tresbu.trakeye.service.dto.GeofenceCreateDTO;
import com.tresbu.trakeye.service.dto.GeofenceDTO;
import com.tresbu.trakeye.service.dto.GeofenceSearchDTO;
import com.tresbu.trakeye.service.dto.GeofenceUpdateDTO;
import com.tresbu.trakeye.service.mapper.GeofenceMapper;

/**
 * Service Implementation for managing Geofence.
 */
@Service
@Transactional
public class GeofenceServiceImpl implements GeofenceService{

    private final Logger log = LoggerFactory.getLogger(GeofenceServiceImpl.class);
    
    @Inject
    private GeofenceRepository geofenceRepository;

    @Inject
    private GeofenceMapper geofenceMapper;

    /**
     * Save a geofence.
     *
     * @param geofenceDTO the entity to save
     * @return the persisted entity
     */
    public GeofenceDTO save(GeofenceCreateDTO geofenceCreateDTO) {
        log.debug("Request to save Geofence : {}", geofenceCreateDTO);
        Geofence geofence = geofenceMapper.geofenceCreateDTOToGeofence(geofenceCreateDTO);
        geofence.setCreatedDate(Instant.now().toEpochMilli());
        geofence.setModifiedDate(Instant.now().toEpochMilli());
        geofence = geofenceRepository.save(geofence);
        GeofenceDTO result = geofenceMapper.geofenceToGeofenceDTO(geofence);
        return result;
    }

    /**
     *  Get all the geofences.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<GeofenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Geofences");
        Page<Geofence> result = geofenceRepository.findGeofenceForLoggedInUser(pageable);
        return result.map(geofence -> geofenceMapper.geofenceToGeofenceDTO(geofence));
    }
    
    @Transactional(readOnly = true) 
    public Page<GeofenceDTO> findAllBySearchValue(String searchText,Pageable pageable) {
        log.debug("Request to get all Geofences by search value {}",searchText);
       Page<Geofence> result = geofenceRepository.findGeofenceForLoggedInUserBySearch(searchText,pageable);
        return result.map(geofence -> geofenceMapper.geofenceToGeofenceDTO(geofence));
    }

    /**
     *  Get one geofence by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public GeofenceDTO findOne(Long id) {
        log.debug("Request to get Geofence : {}", id);
        Geofence geofence = geofenceRepository.findOne(id);
        GeofenceDTO geofenceDTO = geofenceMapper.geofenceToGeofenceDTO(geofence);
        return geofenceDTO;
    }

    /**
     *  Delete the  geofence by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
    	geofenceRepository.findAll();
        log.debug("Request to delete Geofence : {}", id);
        geofenceRepository.delete(id);
    }

	@Override
	public List<GeofenceSearchDTO> searchGeofencesByName() {
		List<Geofence> searchGeofencesbyName = geofenceRepository.findByUserIsCurrentUser();
		return geofenceMapper.geofencesToGeofenceSearchDTOs(searchGeofencesbyName);
	}

	@Override
	public GeofenceDTO update(GeofenceUpdateDTO geofenceUpdateDTO) {
		Geofence geofence=geofenceRepository.findOne(geofenceUpdateDTO.getId());
		geofenceMapper.updategeofenceFromGeofenceUpdateDTO(geofenceUpdateDTO, geofence);
		geofence.setModifiedDate(Instant.now().toEpochMilli());
		geofenceRepository.save(geofence);
		GeofenceDTO result = geofenceMapper.geofenceToGeofenceDTO(geofence);
		return result;
	}

	
}
