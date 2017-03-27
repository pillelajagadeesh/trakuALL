package com.tresbu.trakeye.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.tresbu.trakeye.domain.CaseType;
import com.tresbu.trakeye.service.dto.CaseTypeCreateDTO;
import com.tresbu.trakeye.service.dto.CaseTypeDTO;
import com.tresbu.trakeye.service.dto.CaseTypeUpdateDTO;

/**
 * Mapper for the entity CaseType and its DTO CaseTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, },unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CaseTypeMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "createdBy")
    CaseTypeDTO caseTypeToCaseTypeDTO(CaseType caseType);

    List<CaseTypeDTO> caseTypesToCaseTypeDTOs(List<CaseType> caseTypes);

    @Mapping(source = "userId", target = "user")
    CaseType caseTypeDTOToCaseType(CaseTypeDTO caseTypeDTO);
    
    @Mapping(source = "userId", target = "user")
    CaseType caseTypeCreateDTOToCaseType(CaseTypeCreateDTO caseTypeCreateDTO);

    List<CaseType> caseTypeDTOsToCaseTypes(List<CaseTypeDTO> caseTypeDTOs);
    
    void updatecaseTypeFromCaseTypeUpdateDTO(CaseTypeUpdateDTO caseTypeUpdateDTO,@MappingTarget CaseType caseType);
}
