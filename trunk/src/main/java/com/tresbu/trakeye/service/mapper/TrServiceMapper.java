package com.tresbu.trakeye.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.tresbu.trakeye.domain.TrCase;
import com.tresbu.trakeye.domain.TrService;
import com.tresbu.trakeye.service.dto.TrServiceCreateDTO;
import com.tresbu.trakeye.service.dto.TrServiceDTO;
import com.tresbu.trakeye.service.dto.TrServiceUpdateDTO;

/**
 * Mapper for the entity TrService and its DTO TrServiceDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, TrCaseMapper.class,ServiceTypeMapper.class,ServiceImageMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrServiceMapper {

	@Mapping(source = "reportedBy.id", target = "reportedBy")
    @Mapping(source = "user.id", target = "user")
    @Mapping(source = "updatedBy.id", target = "updatedBy")
 	@Mapping(target = "serviceImages",ignore = true)
    TrServiceDTO trServiceToTrServiceDTO(TrService trService);

    List<TrServiceDTO> trServicesToTrServiceDTOs(List<TrService> trServices);

   
    TrService trServiceDTOToTrService(TrServiceDTO trServiceDTO);

    List<TrService> trServiceDTOsToTrServices(List<TrServiceDTO> trServiceDTOs);

    TrService trServiceCreateDTOToTrService(TrServiceCreateDTO trServiceCreateDTO);
    
    void updateTrServiceFromTrServiceUpdateDTO(TrServiceUpdateDTO trServiceUpdateDTO,@MappingTarget TrService trService);
    
    default TrCase trCaseFromId(Long id) {
        if (id == null) {
            return null;
        }
        TrCase trCase = new TrCase();
        trCase.setId(id);
        return trCase;
    }
}
