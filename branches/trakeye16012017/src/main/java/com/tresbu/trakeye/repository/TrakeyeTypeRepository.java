package com.tresbu.trakeye.repository;

import com.tresbu.trakeye.domain.LocationLog;
import com.tresbu.trakeye.domain.TrakeyeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the TrakeyeType entity.
 */
@SuppressWarnings("unused")
public interface TrakeyeTypeRepository extends JpaRepository<TrakeyeType,Long> {

    @Query("select trakeyeType from TrakeyeType trakeyeType where trakeyeType.user.login = ?#{principal.username}")
    List<TrakeyeType> findByUserIsCurrentUser();
    
   /* @Query(value="select trakeyetype from TrakeyeType trakeyetype where trakeyetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}) order by trakeyetype.id desc",
    		countQuery = "select count(trakeyetype) from TrakeyeType trakeyetype where trakeyetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}) ")*/
    
  /* @Query( value="WITH RECURSIVE usertree  AS (  SELECT ut.*   FROM trakeye_user ut   WHERE (login = ?#{principal.username} and tenant_id = ?#{principal.tenant.id} )   UNION ALL "   
    				+" SELECT c.*   FROM trakeye_user c   JOIN usertree p ON c.created_by = p.login ) "
    				+" select tt.* from usertree ut , trakeye_type tt where ut.id = tt.user_id ORDER BY ?#{#pageable}",
    		countQuery="WITH RECURSIVE usertree  AS (  SELECT ut.*   FROM trakeye_user ut   WHERE (login = ?#{principal.username} and tenant_id = ?#{principal.tenant.id} )   UNION ALL "   
    	    		+" SELECT c.*   FROM trakeye_user c   JOIN usertree p ON c.created_by = p.login ) "
    	    		+" select tt.* from usertree ut , trakeye_type tt where ut.id = tt.user_id ",
    		nativeQuery=true)*/
    @Query(value="select trakEyeType.* from trakeye_type trakEyeType where trakEyeType.tenant_id = ?#{principal.tenant.id} ORDER BY ?#{#pageable}",
    		nativeQuery=true)
   Page<TrakeyeType> findTrakeyeTypesForLoggedInUser(Pageable pageable);
    
    
   /* @Query(value="select trakeyetype from TrakeyeType trakeyetype where ((trakeyetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username})) and "
    		+ "(trakeyetype.name like :searchText% or trakeyetype.description like :searchText% or trakeyetype.id like :searchText%)) order by trakeyetype.id desc",
    		countQuery = "select count(trakeyetype) from TrakeyeType trakeyetype where ((trakeyetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username})) and "
    				+ "(trakeyetype.name like :searchText% or trakeyetype.description like :searchText% or trakeyetype.id like :searchText%))")*/
   /* @Query( value="WITH RECURSIVE usertree  AS (  SELECT ut.*   FROM trakeye_user ut   WHERE (login = ?#{principal.username} and tenant_id = ?#{principal.tenant.id} )   UNION ALL "   
    				+" SELECT c.*   FROM trakeye_user c   JOIN usertree p ON c.created_by = p.login ) "
    				+" select tt.* from usertree ut , trakeye_type tt where ut.id = tt.user_id and (tt.name like :searchText% or tt.description like :searchText% ) ORDER BY ?#{#pageable}",
    		countQuery="WITH RECURSIVE usertree  AS (  SELECT ut.*   FROM trakeye_user ut   WHERE (login = ?#{principal.username} and tenant_id = ?#{principal.tenant.id} )   UNION ALL "   
    	    			+" SELECT c.*   FROM trakeye_user c   JOIN usertree p ON c.created_by = p.login ) "
    	    			+" select tt.* from usertree ut , trakeye_type tt where ut.id = tt.user_id and (tt.name like :searchText% or tt.description like :searchText% )",
    	    		nativeQuery=true)*/
    @Query(value="select trakEyeType.* from trakeye_type trakEyeType where trakEyeType.tenant_id = ?#{principal.tenant.id} "
    		+ " and (trakEyeType.name ilike %:searchText% or trakEyeType.description ilike %:searchText%  or CAST(trakEyeType.id AS TEXT) ILIKE %:searchText% ) ORDER BY ?#{#pageable}",
    		nativeQuery=true)
    Page<TrakeyeType> findTrakeyeTypesForLoggedInUserBySearch(@Param("searchText") String searchText,Pageable pageable);
    
    @Query(value="select trakeyetype.* from trakeye_type trakeyetype where trakeyetype.name =:name and trakeyetype.tenant_id =?#{principal.tenant.id}",
       		nativeQuery=true) 
       Optional<TrakeyeType> findTrakeyeTypeByName(@Param("name") String name);
    
    @Query(value="select trakEyeType.* from trakeye_type trakEyeType where trakEyeType.tenant_id = ?#{principal.tenant.id} "
    		+ " and (trakEyeType.id =:trakeyetypeId ) ORDER BY ?#{#pageable}",
       		nativeQuery=true) 
       TrakeyeType findTrakeyeTypeById(@Param("trakeyetypeId") Long trakeyetypeId);

}
