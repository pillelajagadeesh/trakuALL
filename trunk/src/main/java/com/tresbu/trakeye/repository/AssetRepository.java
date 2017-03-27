package com.tresbu.trakeye.repository;

import com.tresbu.trakeye.domain.Asset;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Asset entity.
 */
@SuppressWarnings("unused")
public interface AssetRepository extends JpaRepository<Asset,Long> {

    @Query("select asset from Asset asset where asset.user.login = ?#{principal.username}")
    List<Asset> findByUserIsCurrentUser();
    
    @Query(value="select asset from Asset asset where asset.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username})"
    		+ "order by asset.id desc",
    		countQuery = "select count(asset) from Asset asset where asset.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username})")
    Page<Asset> findAssetsForLoggedInUser(Pageable pageable);
    
    
    @Query(value="select asset from Asset asset where ((asset.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}))"
    		+ "and (asset.name like :searchText% or "
    		+ " asset.description like :searchText%  or "
    		+ " asset.id like :searchText% or asset.assetType.name like :searchText% )) order by asset.id desc",
	countQuery = "select count(asset) from Asset asset where ((asset.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}))"
			+ "and (asset.name like :searchText% or "
    		+ " asset.description like :searchText%  or "
    		+ " asset.id like :searchText% or asset.assetType.name like :searchText% ))")
      Page<Asset> findAssetsForLoggedInUser(@Param("searchText") String searchText,Pageable pageable);
    
    @Query(value="select asset from Asset asset where ((asset.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}))"
    		+ "and (asset.name like :searchText% or "
    		+ " asset.description like :searchText%  or "
    		+ " asset.id like :searchText% or asset.assetType.name like :searchText% )) order by asset.id desc",
	countQuery = "select count(asset) from Asset asset where ((asset.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}))"
			+ "and (asset.name like :searchText% or "
    		+ " asset.description like :searchText%  or "
    		+ " asset.id like :searchText% or asset.assetType.name like :searchText% ))")
      List<Asset> findAssetsForMap(@Param("searchText") String searchText);

}
