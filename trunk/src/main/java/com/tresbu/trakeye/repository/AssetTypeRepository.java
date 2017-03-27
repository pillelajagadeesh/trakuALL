package com.tresbu.trakeye.repository;

import com.tresbu.trakeye.domain.AssetType;
import com.tresbu.trakeye.domain.CaseType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the AssetType entity.
 */
@SuppressWarnings("unused")
public interface AssetTypeRepository extends JpaRepository<AssetType,Long> {

    @Query("select assetType from AssetType assetType where assetType.user.login = ?#{principal.username}")
    List<AssetType> findByUserIsCurrentUser();
    
    
    @Query(value="select assettype from AssetType assettype where assettype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username})"
    		+ "or assettype.user.id = (select id from User user where user.login = (select createdBy from User user where user.login =?#{principal.username}))  order by assettype.id desc",
    		countQuery = "select count(assettype) from AssetType assettype where assettype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username})"
    				+ "or assettype.user.id = (select id from User user where user.login = (select createdBy from User user where user.login =?#{principal.username}))")
     Page<AssetType> findAssetTypeForLoggedInUser(Pageable pageable);
    
    @Query(value="select assettype from AssetType assettype where ((assettype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}))"
    		+ "or (assettype.user.id = (select id from User user where user.login = (select createdBy from User user where user.login =?#{principal.username}))) and (assettype.name like :searchText% or "
    		+ " assettype.description like :searchText%  or "
    		+ " assettype.id like :searchText% or assettype.layout like :searchText% or assettype.user.login like :searchText%)) order by assettype.id desc",
    		countQuery = "select count(assettype) from AssetType assettype where ((assettype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}))"
    		+ "or (assettype.user.id = (select id from User user where user.login = (select createdBy from User user where user.login =?#{principal.username}))) and (assettype.name like :searchText% or "
    		+ " assettype.description like :searchText%  or "
    		+ " assettype.id like :searchText% or assettype.layout like :searchText% or assettype.user.login like :searchText%))")
     Page<AssetType> findAssetTypeForLoggedInUserBySearchValue(@Param("searchText")String searchText,Pageable pageable);

}
