package com.tresbu.trakeye.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.tresbu.trakeye.domain.AssetType;
import com.tresbu.trakeye.service.dto.AssetTypeCreateDTO;
import com.tresbu.trakeye.service.dto.AssetTypeDTO;
import com.tresbu.trakeye.service.dto.AssetTypeUpdateDTO;

/**
 * Mapper for the entity AssetType and its DTO AssetTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, },unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AssetTypeMapper {

    @Mapping(source = "user.id", target = "userId")
	@Mapping(source = "user.login", target = "createdBy")
	AssetTypeDTO assetTypeToAssetTypeDTO(AssetType assetType);
    
    @Mapping(source = "userId", target = "user")
    AssetType assetTypeDTOToAssetType(AssetTypeDTO assetTypeDTO);

    
	List<AssetTypeDTO> assetTypesToAssetTypeDTOs(List<AssetType> assetTypes);

    @Mapping(source = "userId", target = "user")
    @Mapping(target="colorcode",ignore=true)
    AssetType assetTypeCreateDTOToAssetType(AssetTypeCreateDTO assetTypeCreateDTO);

    List<AssetType> assetTypeDTOsToAssetTypes(List<AssetTypeCreateDTO> assetTypeDTOs);
    
    @Mapping(target="colorcode",ignore=true)
    void updateassetTypeFromAssetTypeUpdateDTO(AssetTypeUpdateDTO assetTypeUpdateDTO,@MappingTarget AssetType assetType);
}
