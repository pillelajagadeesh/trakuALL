package com.tresbu.trakeye.service.mapper;

import java.util.Set;

import org.mapstruct.Mapper;

import com.tresbu.trakeye.domain.ServiceImage;
import com.tresbu.trakeye.service.dto.ServiceImageDTO;

/**
 * Mapper for the entity ServiceImage and its DTO ServiceImageDTO.
 */
@Mapper(componentModel = "spring", uses = { TrServiceMapper.class })
public interface ServiceImageMapper {

	ServiceImageDTO serviceImageToServiceImageDTO(ServiceImage serviceImage);
 
	ServiceImage serviceImageDTOToServiceImage(ServiceImageDTO serviceImage);
	
	Set<ServiceImageDTO> serviceImagesToServiceImageDTOs(Set<ServiceImage> serviceImages);
	Set<ServiceImage> serviceImageDTOsToServiceImages(Set<ServiceImageDTO> serviceImageDTOs);
    
   
}
