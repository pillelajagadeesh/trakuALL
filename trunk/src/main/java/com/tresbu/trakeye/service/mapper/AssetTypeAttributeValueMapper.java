package com.tresbu.trakeye.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tresbu.trakeye.domain.AssetTypeAttributeValue;
import com.tresbu.trakeye.service.dto.AssetTypeAttributeValueDTO;

@Mapper(componentModel = "spring", uses = { UserMapper.class,AssetMapper.class,AssetTypeAttributeMapper.class})
public interface AssetTypeAttributeValueMapper {

	@Mapping(source = "user.id", target = "userId")
	AssetTypeAttributeValueDTO assetTypeAttributeValueToAssetTypeAttributeValueDTO(AssetTypeAttributeValue assetTypeAttributeValue);

	List<AssetTypeAttributeValueDTO> assetTypeAttributeValuesToAssetTypeAttributeValueDTOs(List<AssetTypeAttributeValue> assetTypeAttributeValues);

	@Mapping(source = "userId", target = "user")
	AssetTypeAttributeValue assetTypeAttributeValueDTOToAssetTypeAttributeValue(AssetTypeAttributeValueDTO assetTypeAttributeValueDTO);

	List<AssetTypeAttributeValue> assetTypeAttributeValueDTOsToAssetTypeAttributeValues(List<AssetTypeAttributeValueDTO> assetTypeAttributeValueDTOs);
	
}
