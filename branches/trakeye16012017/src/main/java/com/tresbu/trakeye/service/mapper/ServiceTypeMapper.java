package com.tresbu.trakeye.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.tresbu.trakeye.domain.ServiceType;
import com.tresbu.trakeye.service.dto.ServiceTypeCreateDTO;
import com.tresbu.trakeye.service.dto.ServiceTypeDTO;
import com.tresbu.trakeye.service.dto.ServiceTypeUpdateDTO;

/**
 * Mapper for the entity ServiceType and its DTO ServiceTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, },unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceTypeMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "createdBy")
    ServiceTypeDTO serviceTypeToServiceTypeDTO(ServiceType serviceType);

    List<ServiceTypeDTO> serviceTypesToServiceTypeDTOs(List<ServiceType> serviceTypes);

    @Mapping(source = "userId", target = "user")
    ServiceType serviceTypeDTOToServiceType(ServiceTypeDTO serviceTypeDTO);

    List<ServiceType> serviceTypeDTOsToServiceTypes(List<ServiceTypeDTO> serviceTypeDTOs);
    
    @Mapping(source = "userId", target = "user")
    ServiceType serviceTypeCreateDTOToServiceType(ServiceTypeCreateDTO serviceTypeCreateDTO);
    
    void updateserviceTypeFromServiceTypeUpdateDTO(ServiceTypeUpdateDTO serviceTypeUpdateDTO,@MappingTarget ServiceType serviceType);
}
