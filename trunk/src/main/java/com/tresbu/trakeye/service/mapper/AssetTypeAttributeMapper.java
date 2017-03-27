package com.tresbu.trakeye.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.tresbu.trakeye.domain.AssetTypeAttribute;
import com.tresbu.trakeye.service.dto.AssetTypeAttributeDTO;

@Mapper(componentModel = "spring", uses = { UserMapper.class,AssetMapper.class,AssetTypeAttributeValueMapper.class  })
public interface AssetTypeAttributeMapper {

	AssetTypeAttributeDTO assetTypeAttributeToAssetTypeAttributeDTO(AssetTypeAttribute assetTypeAttribute);

	List<AssetTypeAttributeDTO> assetTypeAttributesToAssetTypeAttributeDTOs(List<AssetTypeAttribute> assetTypeAttributes);

	AssetTypeAttribute assetTypeAttributeDTOToAssetTypeAttribute(AssetTypeAttributeDTO assetTypeAttributeDTO);

	List<AssetTypeAttribute> assetTypeAttributeDTOsToAssetTypeAttributes(List<AssetTypeAttributeDTO> assetTypeAttributeDTOs);
}
