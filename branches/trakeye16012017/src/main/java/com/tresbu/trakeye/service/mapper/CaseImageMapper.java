package com.tresbu.trakeye.service.mapper;

import java.util.Set;

import org.mapstruct.Mapper;

import com.tresbu.trakeye.domain.CaseImage;
import com.tresbu.trakeye.service.dto.CaseImageDTO;

/**
 * Mapper for the entity CaseImage and its DTO CaseImageDTO.
 */
@Mapper(componentModel = "spring", uses = { TrCaseMapper.class })
public interface CaseImageMapper {

	CaseImageDTO caseImageToCaseImageDTO(CaseImage caseImage);
 
	CaseImage caseImageDTOToCaseImage(CaseImageDTO caseImage);
	
	Set<CaseImageDTO> caseImagesToCaseImageDTOs(Set<CaseImage> caseImages);
	Set<CaseImage> caseImageDTOsToCaseImages(Set<CaseImageDTO> caseImageDTOs);
    
   
}
