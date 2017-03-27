package com.tresbu.trakeye.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.tresbu.trakeye.domain.CaseType;
import com.tresbu.trakeye.domain.TrCase;
import com.tresbu.trakeye.service.dto.TrCaseCreateDTO;
import com.tresbu.trakeye.service.dto.TrCaseDTO;
import com.tresbu.trakeye.service.dto.TrCaseUpdateDTO;

/**
 * Mapper for the entity TrCase and its DTO TrCaseDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, CaseTypeMapper.class,CaseImageMapper.class ,GeofenceMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrCaseMapper {

 	@Mapping(source = "reportedBy.login", target = "reportedByUser")
    @Mapping(source = "assignedTo.login", target = "assignedToUser")
    @Mapping(source = "updatedBy.login", target = "updatedByUser")
    @Mapping(source = "assignedBy.login", target = "assignedByUser")
 	@Mapping(target = "caseImages",ignore = true)
 	@Mapping(source = "geofence.name", target = "geofenceName")
    TrCaseDTO trCaseToTrCaseDTO(TrCase trCase);

    List<TrCaseDTO> trCasesToTrCaseDTOs(List<TrCase> trCases);

    TrCase trCaseDTOToTrCase(TrCaseDTO trCaseDTO);
    
    TrCase trCaseCreateDTOToTrCase(TrCaseCreateDTO trCaseCreateDTO);
    
    void updateTrCaseFromTrCaseDTO(TrCaseUpdateDTO trCaseUpdateDTO,@MappingTarget TrCase trCase);

    List<TrCase> trCaseDTOsToTrCases(List<TrCaseDTO> trCaseDTOs);

    default CaseType caseTypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        CaseType caseType = new CaseType();
        caseType.setId(id);
        return caseType;
    }
    
    
   
}
