package com.tresbu.trakeye.service.mapper;

import com.tresbu.trakeye.domain.*;
import com.tresbu.trakeye.service.dto.LatLongDTO;
import com.tresbu.trakeye.service.dto.LocationLogCreateDTO;
import com.tresbu.trakeye.service.dto.LocationLogDTO;
import com.tresbu.trakeye.service.dto.LocationLogUpdateDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity LocationLog and its DTO LocationLogDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface LocationLogMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userName")
    LocationLogDTO locationLogToLocationLogDTO(LocationLog locationLog);
   
    List<LocationLogDTO> locationLogsToLocationLogDTOs(List<LocationLog> locationLogs);

    @Mapping(source = "latitude", target = "lat")
    @Mapping(source = "longitude", target = "lng")
    LatLongDTO locationLogToLatLongDTO(LocationLog locationLog);
    
    List<LatLongDTO> locationLogsToLatLongDTOs(List<LocationLog> locationLog);
    
    @Mapping(source = "userId", target = "user")
    @Mapping(target = "tenant", ignore = true)
    LocationLog locationLogDTOToLocationLog(LocationLogDTO locationLogDTO);

    List<LocationLog> locationLogDTOsToLocationLogs(List<LocationLogDTO> locationLogDTOs);
    
    @Mapping(source = "userId", target = "user")
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "distanceTravelled",ignore = true)
    @Mapping(target = "updatedDateTime",ignore = true)
    @Mapping(target = "tenant", ignore = true)
    LocationLog createLocationLogDTOToLocationLog(LocationLogCreateDTO createLocationLogDTO);
    
    @Mapping(target = "distanceTravelled",ignore = true)
    @Mapping(target = "updatedDateTime",ignore = true)
    @Mapping(target = "longitude",ignore = true)
    @Mapping(target = "latitude",ignore = true)
    @Mapping(target = "address",ignore = true)
    @Mapping(target = "user",ignore = true)
    @Mapping(target = "tenant", ignore = true)
    void updateLocationLogFromLocationLogDTO(LocationLogUpdateDTO updateLocationLogDTO,@MappingTarget LocationLog locationLog);
}
