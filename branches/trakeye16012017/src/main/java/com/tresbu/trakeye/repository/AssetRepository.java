package com.tresbu.trakeye.repository;

import com.tresbu.trakeye.domain.Asset;
import com.tresbu.trakeye.domain.CaseType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Asset entity.
 */
@SuppressWarnings("unused")
public interface AssetRepository extends JpaRepository<Asset,Long> {

    @Query("select asset from Asset asset where asset.user.login = ?#{principal.username}")
    List<Asset> findByUserIsCurrentUser();
    
   @Query( value="select assets.* from trakeye_asset as assets, trakeye_user tuser  where tuser.id=assets.user_id and assets.user_id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id}) ) ORDER BY ?#{#pageable}",
    		nativeQuery=true)
    Page<Asset> findAssetsForLoggedInUser(Pageable pageable);
    
    
   @Query( value="select * from trakeye_asset as assets, trakeye_assettype assetType, trakeye_user tuser  where tuser.id=assets.user_id and assets.asset_type_id=assetType.id  and assets.user_id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id}) )"
    		+ " and (assets.name ilike %:searchText% or assets.description ilike %:searchText%  or "
    		+ "  assetType.name ilike %:searchText% or CAST(assets.id AS TEXT) ILIKE %:searchText% "
    		+ " or assets.user_id in (select id from trakeye_user u where  u.login ilike %:searchText%)) ORDER BY ?#{#pageable}",
    		nativeQuery=true)
      Page<Asset> findAssetsForLoggedInUser(@Param("searchText") String searchText,Pageable pageable);
    
   /* @Query(value="select asset from Asset asset where ((asset.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}))"
    		+ "and (asset.name like :searchText% or "
    		+ " asset.description like :searchText%  or "
    		+ " asset.id like :searchText% or asset.assetType.name like :searchText% )) order by asset.id desc",
	countQuery = "select count(asset) from Asset asset where ((asset.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}))"
			+ "and (asset.name like :searchText% or "
    		+ " asset.description like :searchText%  or "
    		+ " asset.id like :searchText% or asset.assetType.name like :searchText% ))")*/
   
   @Query( value="select * from trakeye_asset as assets, trakeye_assettype assetType, trakeye_user tuser  where tuser.id=assets.user_id and assets.asset_type_id=assetType.id  and assets.user_id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id}) )"
   		+ " and (assets.name ilike %:searchText% or assets.description ilike %:searchText%  or "
   		+ "  assetType.name ilike %:searchText%  or CAST(assets.id AS TEXT) ILIKE %:searchText% ) order by assets.id desc",
   		nativeQuery=true)
      List<Asset> findAssetsForMap(@Param("searchText") String searchText);
   
   
   @Query( value="select assets.* from trakeye_asset as assets, trakeye_user tuser  "
   		+ "where tuser.id=assets.user_id and assets.user_id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id}) ) "
   		+ " and assets.id= :assetId and assets.tenant_id = ?#{principal.tenant.id}  ORDER BY ?#{#pageable}",
   		nativeQuery=true)
   Asset findAssetById(@Param("assetId") Long assetId);
   
   
   @Query(value="select asset.* from trakeye_asset as asset where asset.name =:name and asset.tenant_id =?#{principal.tenant.id}",
	   		nativeQuery=true) 
	   Optional<Asset> findAssetByName(@Param("name") String name);
   
   @Query( value="select assets.* from trakeye_asset as assets, trakeye_user tuser  where tuser.id=assets.user_id and assets.user_id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id}) ) ORDER BY assets.id",
   		nativeQuery=true)
   List<Asset> findAssetsForMap();

}
