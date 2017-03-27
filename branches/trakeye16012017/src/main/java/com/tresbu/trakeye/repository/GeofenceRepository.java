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
    
   // @Query("select geofence from Geofence geofence where  geofence.name =:name") 
   // Optional<Geofence> findOneByName(@Param("name") String name);
    
    @Query(value="select geofence.* from trakeye_geofence geofence, trakeye_user tuser where tuser.id=geofence.user_id and  geofence.name =:name and geofence.tenant_id =?#{principal.tenant.id} and tuser.login = ?#{principal.username}",
    		nativeQuery=true) 
    Optional<Geofence> findGeofenceByName(@Param("name") String name);
    
    
      @Query(value="select geofence.* from trakeye_geofence geofence,trakeye_user tuser where geofence.user_id = tuser.id and tuser.login = ?#{principal.username} ORDER BY ?#{#pageable}",
	nativeQuery=true)
     Page<Geofence> findGeofenceForLoggedInUser(Pageable pageable);
    
    
     @Query(value="select geofence.* from trakeye_geofence geofence,trakeye_user tuser where geofence.user_id = tuser.id and tuser.login = ?#{principal.username} "
    		+ " and (geofence.name ilike %:searchText% or geofence.description ilike %:searchText%  or CAST(geofence.id AS TEXT) ILIKE %:searchText% ) ORDER BY ?#{#pageable}",
    		nativeQuery=true)
      Page<Geofence> findGeofenceForLoggedInUserBySearch(@Param("searchText")String searchText,Pageable pageable);
    
    @Query("select geofence from Geofence geofence where geofence.user.login = :login")
    List<Geofence> findGeofencesCreatedBy(@Param("login") String login);
    
  
    
    @Query(nativeQuery = true)
	ReportDTO geofenceReport(@Param("fromTime") long fromTime,@Param("toTime")  long toTime,@Param("geofenceId")  long geofenceId);
	
	@Query(nativeQuery = true)
	List<ReportDTO> geofencesReport(@Param("fromTime") long fromTime,@Param("toTime")  long toTime,@Param("userId")  long userId);
	
	ReportDTO geofenceDetailedReport(@Param("fromTime") long fromTime,@Param("toTime")  long toTime,@Param("geofenceId")  long geofenceId);
	
	 @Query(value="select geofence.* from trakeye_geofence geofence, trakeye_user tuser where tuser.id=geofence.user_id and  "
	 		+ "geofence.id =:geofenceId and geofence.tenant_id =?#{principal.tenant.id} and tuser.login = ?#{principal.username}",
	    		nativeQuery=true) 
	    Geofence findGeofenceById(@Param("geofenceId") Long geofenceId);
    
}
