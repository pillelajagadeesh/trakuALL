package com.tresbu.trakeye.repository;

import com.tresbu.trakeye.domain.Geofence;
import com.tresbu.trakeye.domain.LocationLog;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.service.dto.ReportDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Geofence entity.
 */
@SuppressWarnings("unused")
public interface GeofenceRepository extends JpaRepository<Geofence,Long> {

    @Query("select geofence from Geofence geofence ,User user where geofence.user.id =user.id and  user.login = ?#{principal.username})")
    List<Geofence> findByUserIsCurrentUser();
    
    @Query("select geofence from Geofence geofence where geofence.user.login = ?#{principal.username} and geofence.name =:name") 
    Optional<Geofence> findOneByName(@Param("name") String name);
    
    
    @Query(value="select geofence from Geofence geofence where geofence.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}) order by geofence.id desc",
    		countQuery = "select count(geofence) from Geofence geofence where geofence.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}) ")
     Page<Geofence> findGeofenceForLoggedInUser(Pageable pageable);
    
    
    @Query(value="select geofence from Geofence geofence where ((geofence.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username})) and"
    		+ "(geofence.name like :searchText% or geofence.description like :searchText% or geofence.id like :searchText%)) order by geofence.id desc",
    		countQuery = "select count(geofence) from Geofence geofence where ((geofence.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username})) "
    				+ "and (geofence.name like :searchText% or geofence.description like :searchText% or geofence.id like :searchText%))")
     Page<Geofence> findGeofenceForLoggedInUserBySearch(@Param("searchText")String searchText,Pageable pageable);
    
    @Query("select geofence from Geofence geofence where geofence.user.login = :login")
    List<Geofence> findGeofencesCreatedBy(@Param("login") String login);
    
  
    
    @Query(nativeQuery = true)
	ReportDTO geofenceReport(@Param("fromTime") long fromTime,@Param("toTime")  long toTime,@Param("geofenceId")  long geofenceId);
	
	@Query(nativeQuery = true)
	List<ReportDTO> geofencesReport(@Param("fromTime") long fromTime,@Param("toTime")  long toTime,@Param("userId")  long userId);
	
	ReportDTO geofenceDetailedReport(@Param("fromTime") long fromTime,@Param("toTime")  long toTime,@Param("geofenceId")  long geofenceId);
    
}
