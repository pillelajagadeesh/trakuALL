package com.tresbu.trakeye.repository;

import com.tresbu.trakeye.domain.LocationLog;
import com.tresbu.trakeye.domain.TrakeyeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the TrakeyeType entity.
 */
@SuppressWarnings("unused")
public interface TrakeyeTypeRepository extends JpaRepository<TrakeyeType,Long> {

    @Query("select trakeyeType from TrakeyeType trakeyeType where trakeyeType.user.login = ?#{principal.username}")
    List<TrakeyeType> findByUserIsCurrentUser();
    
    @Query(value="select trakeyetype from TrakeyeType trakeyetype where trakeyetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}) order by trakeyetype.id desc",
    		countQuery = "select count(trakeyetype) from TrakeyeType trakeyetype where trakeyetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}) ")
    				
    Page<TrakeyeType> findTrakeyeTypesForLoggedInUser(Pageable pageable);
    
    
    @Query(value="select trakeyetype from TrakeyeType trakeyetype where ((trakeyetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username})) and "
    		+ "(trakeyetype.name like :searchText% or trakeyetype.description like :searchText% or trakeyetype.id like :searchText%)) order by trakeyetype.id desc",
    		countQuery = "select count(trakeyetype) from TrakeyeType trakeyetype where ((trakeyetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username})) and "
    				+ "(trakeyetype.name like :searchText% or trakeyetype.description like :searchText% or trakeyetype.id like :searchText%))")
    				
    Page<TrakeyeType> findTrakeyeTypesForLoggedInUserBySearch(@Param("searchText") String searchText,Pageable pageable);

}
