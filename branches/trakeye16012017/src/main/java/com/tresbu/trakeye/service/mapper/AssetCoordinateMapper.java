package com.tresbu.trakeye.service.mapper;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tresbu.trakeye.domain.AssetCoordinate;
import com.tresbu.trakeye.service.dto.AssetCoordinateDTO;

@Mapper(componentModel = "spring", uses = {AssetMapper.class })
public interface AssetCoordinateMapper {

	AssetCoordinateDTO assetCoordinateToAssetCoordinateDTO(AssetCoordinate assetCoordinate);
	
	List<AssetCoordinateDTO> assetCoordinatesToAssetCoordinateDTOs(List<AssetCoordinate> assetCoordinates);
	@Mapping(target = "asset",ignore = true)
	AssetCoordinate assetCoordinateDTOToAssetCoordinate(AssetCoordinateDTO assetCoordinateDTO);

	
	List<AssetCoordinate> assetCoordinateDTOsToAssetCoordinates(List<AssetCoordinateDTO> assetCoordinateDTOs);
}
