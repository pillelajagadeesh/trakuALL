package com.tresbu.trakeye.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.tresbu.trakeye.domain.TrakeyeType;
import com.tresbu.trakeye.service.dto.TrakeyeTypeCreateDTO;
import com.tresbu.trakeye.service.dto.TrakeyeTypeDTO;
import com.tresbu.trakeye.service.dto.TrakeyeTypeUpdateDTO;

/**
 * Mapper for the entity TrakeyeType and its DTO TrakeyeTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrakeyeTypeMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "createdBy")
    TrakeyeTypeDTO trakeyeTypeToTrakeyeTypeDTO(TrakeyeType trakeyeType);

    List<TrakeyeTypeDTO> trakeyeTypesToTrakeyeTypeDTOs(List<TrakeyeType> trakeyeTypes);

    @Mapping(source = "userId", target = "user")
    TrakeyeType trakeyeTypeDTOToTrakeyeType(TrakeyeTypeDTO trakeyeTypeDTO);

    List<TrakeyeType> trakeyeTypeDTOsToTrakeyeTypes(List<TrakeyeTypeDTO> trakeyeTypeDTOs);

    @Mapping(source = "userId", target = "user")
    TrakeyeType trakeyeTypeCreateDTOToTrakeyeType(TrakeyeTypeCreateDTO trakeyeTypeCreateDTO);
    
    void updatetrakeyeTypeFromTrakeyeTypeUpdateDTO(TrakeyeTypeUpdateDTO trakeyeTypeUpdateDTO,@MappingTarget TrakeyeType trakeyeType);
    
   /* default Set<String> stringsFromTrakeyeTypeAttributes (Set<TrakeyeTypeAttribute> attributes) {
        return attributes.stream().map(TrakeyeTypeAttribute::getName)
            .collect(Collectors.toSet());
    }

    default Set<TrakeyeTypeAttribute> trakeyeTypeAttributesFromStrings(Set<String> attributes) {
        return attributes.stream().map(string -> {
        	TrakeyeTypeAttribute attributesValues = new TrakeyeTypeAttribute();
        	attributesValues.setName(string);
            return attributesValues;
        }).collect(Collectors.toSet());
    }*/
}
