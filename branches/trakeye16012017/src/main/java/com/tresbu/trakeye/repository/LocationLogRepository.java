package com.tresbu.trakeye.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.tresbu.trakeye.domain.LiveLogs;
import com.tresbu.trakeye.domain.LocationLog;
import com.tresbu.trakeye.service.dto.BatteryReportDTO;
import com.tresbu.trakeye.service.dto.DistanceReportDTO;

/**
 * Spring Data JPA repository for the LocationLog entity.
 */
public interface LocationLogRepository extends JpaRepository<LocationLog,Long> {


	Optional<LocationLog> findOneById(Long id);

   /* @Query("select locationLog from LocationLog locationLog where locationLog.user.login = ?#{principal.username}")
    List<LocationLog> findByUserIsCurrentUser();
    
   /* @Query(value="select locationLog from LocationLog locationLog where locationLog.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}) and locationLog.createdDateTime between :fromDate and :toDate",
    		countQuery = "select count(locationLog) from LocationLog locationLog where locationLog.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}) and locationLog.createdDateTime between :fromDate and :toDate")
    				
    Page<LocationLog> findLogsForLoggedInUser(@Param("fromDate") long fromDate,@Param("toDate") long toDate,Pageable pageable);
    */
    
    @Query("select locationLog from LocationLog locationLog where locationLog.user.id =:id and locationLog.createdDateTime between :fromDate and :toDate order by locationLog.createdDateTime ")
    LinkedList<LocationLog> listLocationPath(@Param("id") Long id ,@Param("fromDate") long fromDate,@Param("toDate") long toDate);
    
    
    //@Query("select locationLog from LocationLog locationLog  where locationLog.id in (select max(id) from LocationLog  where user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}) group by user.id )  ")
    @Query(value="select loctionLog.*  from trakeye_locationlog loctionLog where locationLog.id in (select max(id) from trakeye_locationlog where user_id in (select id from get_allusers()) group by user.id)",
    nativeQuery=true)
    List<LocationLog> getlatestLocations();
    
    
    @Query(nativeQuery = true)
    List<LiveLogs> getLiveLogs( @Param("userName") String userName,@Param("dateTime") long dateTime,@Param("hour") int hour, @Param("tenantID") long tenantID );
    
    @Query("select locationLog from LocationLog locationLog where locationLog.user.login = ?#{principal.username} order by createdDateTime desc")
    List<LocationLog> latestLogForGPS(@Param("pageable") Pageable pageable);
    
    @Query("select locationLog from LocationLog locationLog where locationLog.user.login = ?#{principal.username} and locationLog.createdDateTime <= :createdDateTime  order by createdDateTime desc")
    List<LocationLog> latestLogForNP(@Param("pageable") Pageable pageable,@Param("createdDateTime") long createdDateTime);
    
    @Query("select locationLog from LocationLog locationLog where locationLog.id in (select max(id) from LocationLog log where log.user.login =:login)")
    LocationLog  getlatestLocation(@Param("login") String login);
    
    
    
    
    @Query(nativeQuery = true)
    List<BatteryReportDTO> findBatteryDetails(@Param("userid") Long userid ,@Param("fromDate") long fromDate,@Param("toDate") long toDate);
    
    @Query(nativeQuery = true)
    List<DistanceReportDTO> getDistanceReport(@Param("userid") Long userid ,@Param("fromDate") long fromDate,@Param("toDate") long toDate);
       
}
