package com.tresbu.trakeye.repository;

import com.tresbu.trakeye.domain.AssetType;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the AssetType entity.
 */
@SuppressWarnings("unused")
public interface AssetTypeRepository extends JpaRepository<AssetType,Long> {

    @Query("select assetType from AssetType assetType where assetType.user.login = ?#{principal.username}")
    List<AssetType> findByUserIsCurrentUser();
    
    
  /*  @Query(value="select assettype from AssetType assettype where assettype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username})"
    		+ "or assettype.user.id = (select id from User user where user.login = (select createdBy from User user where user.login =?#{principal.username}))  order by assettype.id desc",
    		countQuery = "select count(assettype) from AssetType assettype where assettype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username})"
    				+ "or assettype.user.id = (select id from User user where user.login = (select createdBy from User user where user.login =?#{principal.username}))")*/
    @Query(value="select assettype.* from trakeye_assettype assettype where assettype.tenant_id = ?#{principal.tenant.id}  ORDER BY ?#{#pageable}",
    		nativeQuery=true)
     Page<AssetType> findAssetTypeForLoggedInUser(Pageable pageable);
    
   /* @Query(value="select assettype from AssetType assettype where ((assettype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}))"
    		+ "or (assettype.user.id = (select id from User user where user.login = (select createdBy from User user where user.login =?#{principal.username}))) and (assettype.name like :searchText% or "
    		+ " assettype.description like :searchText%  or "
    		+ " assettype.id like :searchText% or assettype.layout like :searchText% or assettype.user.login like :searchText%)) order by assettype.id desc",
    		countQuery = "select count(assettype) from AssetType assettype where ((assettype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}))"
    		+ "or (assettype.user.id = (select id from User user where user.login = (select createdBy from User user where user.login =?#{principal.username}))) and (assettype.name like :searchText% or "
    		+ " assettype.description like :searchText%  or "
    		+ " assettype.id like :searchText% or assettype.layout like :searchText% or assettype.user.login like :searchText%))")*/
    @Query(value="select assettype.* from trakeye_assettype assettype where assettype.tenant_id = ?#{principal.tenant.id} "
    		+ " and (assettype.name ilike %:searchText% or assettype.description ilike %:searchText% or CAST(assetType.id AS TEXT) ILIKE %:searchText% "
    		+ " or assettype.user_id in (select id from trakeye_user u where  u.login ilike %:searchText%) "
            + " or assettype.layout ilike %:searchText%)  ORDER BY ?#{#pageable}",
    		nativeQuery=true)
     Page<AssetType> findAssetTypeForLoggedInUserBySearchValue(@Param("searchText")String searchText,Pageable pageable);
    
    @Query(value="select assettype.* from trakeye_assettype assettype where assettype.name =:name and assettype.tenant_id =?#{principal.tenant.id}",
       		nativeQuery=true) 
       Optional<AssetType> findAssetTypeByName(@Param("name") String name);
    
    
   /* @Query(value="select assettype.* from trakeye_assettype assettype where assetType.user_id = :createdByAdmin ",
    		nativeQuery=true)
     List<AssetType> findAllAssetTypeCreatedByAgentOrAdmin(@Param("createdByAdmin")long createdByAdmin);*/

    
    @Query(value="select assettype.* from trakeye_assettype assettype where assettype.id =:assetTypeId and assettype.tenant_id =?#{principal.tenant.id}",
       		nativeQuery=true) 
      AssetType findAssetTypeById(@Param("assetTypeId") Long assetTypeId);
}
