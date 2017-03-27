package com.tresbu.trakeye.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.tresbu.trakeye.domain.Geofence;
import com.tresbu.trakeye.service.dto.GeofenceCreateDTO;
import com.tresbu.trakeye.service.dto.GeofenceDTO;
import com.tresbu.trakeye.service.dto.GeofenceSearchDTO;
import com.tresbu.trakeye.service.dto.GeofenceUpdateDTO;

/**
 * Mapper for the entity Geofence and its DTO GeofenceDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, },unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GeofenceMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "createdBy")
    GeofenceDTO geofenceToGeofenceDTO(Geofence geofence);

    List<GeofenceDTO> geofencesToGeofenceDTOs(List<Geofence> geofences);

    @Mapping(source = "userId", target = "user")
    Geofence geofenceDTOToGeofence(GeofenceDTO geofenceDTO);

    
    @Mapping(source = "userId", target = "user")
    Geofence geofenceCreateDTOToGeofence(GeofenceCreateDTO geofenceCreateDTO);
    
    
    List<Geofence> geofenceDTOsToGeofences(List<GeofenceDTO> geofenceDTOs);

    GeofenceSearchDTO  geofenceToGeofenceSearchDTO(Geofence geofence);    
	List<GeofenceSearchDTO> geofencesToGeofenceSearchDTOs(List<Geofence> geofence);
	
	void updategeofenceFromGeofenceUpdateDTO(GeofenceUpdateDTO geofenceUpdateDTO,@MappingTarget Geofence geofence);
}
