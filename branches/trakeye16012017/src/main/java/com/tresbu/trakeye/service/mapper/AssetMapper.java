package com.tresbu.trakeye.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.tresbu.trakeye.domain.Asset;
import com.tresbu.trakeye.domain.AssetType;
import com.tresbu.trakeye.domain.TrCase;
import com.tresbu.trakeye.service.dto.AssetCreateDTO;
import com.tresbu.trakeye.service.dto.AssetDTO;
import com.tresbu.trakeye.service.dto.AssetUpdateDTO;
import com.tresbu.trakeye.service.dto.TrCaseDTO;

/**
 * Mapper for the entity Asset and its DTO AssetDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class,AssetTypeAttributeValueMapper.class,AssetTypeMapper.class,AssetCoordinateMapper.class })
public interface AssetMapper {

    @Mapping(source = "user.id", target = "userId")
  
    @Mapping(source = "user", target = "userIdDTO")
    AssetDTO assetToAssetDTO(Asset asset);
    
    List<AssetDTO> assetsToAssetDTOs(List<Asset> assets);
  
    // List<AssetDTO> assetsToAssetDTOs(List<Asset> assets);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "tenant", ignore = true)
      Asset assetDTOToAsset(AssetDTO assetDTO);
    
    @Mapping(source = "userId", target = "user")
     @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
   // @Mapping(target = "assetType", ignore = true)
    @Mapping(target = "tenant", ignore = true)
    Asset assetCreateDTOToAsset(AssetCreateDTO assetDTO);

    List<Asset> assetDTOsToAssets(List<AssetDTO> assetDTOs);
    
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "assetType", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tenant", ignore = true)
    void updateAssetFromAssetDTO(AssetUpdateDTO assetUpdateDTO,@MappingTarget Asset asset);

    default AssetType assetTypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        AssetType assetType = new AssetType();
        assetType.setId(id);
        return assetType;
    }
}
