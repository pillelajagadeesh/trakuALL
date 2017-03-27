package com.tresbu.trakeye.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tresbu.trakeye.domain.TrService;

/**
 * Spring Data JPA repository for the TrService entity.
 */
public interface DashboardRepository extends JpaRepository<TrService, Long> {

	@Query(value = "select trservice.status as status , count(distinct trservice.id) as count from trakeye_trservice as "
			+ " trservice, trakeye_trcase trcase where trcase.id = trservice.tr_case_id and trcase.assigned_to_id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) group by trservice.status",nativeQuery=true)
	List<Object[]> getServiceCountByStatus();

		
	   
    @Query("select trNotification.status as status, count(distinct trNotification.id) as count from TrNotification trNotification where   "
    		+ " trNotification.fromUser.login = ?#{principal.username} or trNotification.toUser.login = ?#{principal.username} "
    		+ "or trNotification.fromUser.login in (select login from User where createdBy = ?#{principal.username}) "
    		+ "or trNotification.toUser.login in (select login from User where createdBy = ?#{principal.username}) group by trNotification.status")
    List<Object[]> getNotificationbyStatus();
    
    
    @Query("select count(trNotification.id) as count from TrNotification trNotification where  trNotification.toUser.login = ?#{principal.username}" )
    BigInteger getRecievedNotifictionsCount();
    
    @Query("select count(trNotification.id) as count from TrNotification trNotification where  trNotification.fromUser.login = ?#{principal.username}" )
    BigInteger getSentNotifictionsCount();
    
    @Query(value="select trCase.priority as priority ,count(distinct trCase.id) as count from trakeye_trcase as "
    		+ " trCase where  trCase.assigned_to_id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) group by trCase.priority",nativeQuery=true)
    
	List<Object[]> getCaseCountByPriority();
    
	@Query(value="select trCase.status as status ,count(distinct trCase.id) as count from trakeye_trcase as "
    		+ " trCase where  trCase.assigned_to_id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) group by trCase.status",nativeQuery=true)
    List<Object[]> getCaseCountByStatus();
	
		
	@Query(value="select distinct(users.login) as login,"+
			" (select COALESCE(sum(log.distance_travelled) , 0) from trakeye_location_log log where log.created_date_time "
			+ " between :fromTime and :toTime and user_id=users.id) as distance" +
			" from trakeye_user users where id in( select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) and users.activated=true",nativeQuery=true)
	List<Object[]> getUsersListWithDistance(@Param(value = "fromTime") long fromTime,@Param(value = "toTime") long toTime);
	 
	@Query(value="select distinct(users.login) as login,"+
			" (select COALESCE(sum(log.distance_travelled) , 0) from trakeye_location_log log where log.created_date_time "
			+ " between :fromTime and :toTime and user_id=users.id) as distance" +
			" from trakeye_user users where id in( select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) and users.activated=true and users.login ilike %:searchText%",nativeQuery=true)
	List<Object[]> getUsersListWithDistanceBySearch(@Param(value = "fromTime") long fromTime,@Param(value = "toTime") long toTime,@Param("searchText") String searchText);
	
	@Query(value="select  "
			+"case "
			+" when log.created_date_time >=:dateTime and log.updated_date_time = 0 then 'ACTIVE'"
			+" when  log.updated_date_time >=:dateTime then 'IDLE' "
			+" else  'INACTIVE' "
			+" END as status ,count(distinct usr.id) as userid"
			+" from trakeye_location_log log , trakeye_user usr ,"
			+" (select log1.user_id uid,max(GREATEST(log1.updated_date_time,log1.created_date_time)) mtime from trakeye_location_log log1, trakeye_user usr1"
			+" where log1.user_id = usr1.id and usr1.id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) and "
			+ " ( (:hour >= from_time and :hour <= to_time and from_time<=to_time) or (NOT (:hour < from_time and :hour > to_time) and from_time>=to_time) )  group by log1.user_id ) res "
			+" where log.user_id = usr.id and res.mtime=GREATEST(log.updated_date_time,log.created_date_time) and uid=log.user_id and usr.activated=true group by status" ,nativeQuery=true)
	List<Object[]> getUsersCountByStatus(@Param(value = "dateTime") long dateTime,@Param(value = "hour") int hour);
	
	@Query(value="select  "
			+"case "
			+" when log.created_date_time >=:dateTime and log.updated_date_time = 0 then 'ACTIVE'"
			+" when  log.updated_date_time >=:dateTime then 'IDLE' "
			+" else  'INACTIVE' "
			+" END as status ,count(distinct usr.id) as userid"
			+" from trakeye_location_log log , trakeye_user usr ,"
			+" (select log1.user_id uid,max(GREATEST(log1.updated_date_time,log1.created_date_time)) mtime from trakeye_location_log log1, trakeye_user usr1"
			+" where log1.user_id = usr1.id and usr1.id in (select id from  trakeye_user where ((login = ?#{principal.username}  or created_by= ?#{principal.username} and activated=true) and "
			+ " ( (:hour >= from_time and :hour <= to_time and from_time<=to_time) or (NOT (:hour < from_time and :hour > to_time) and from_time>=to_time) ) "
			+ " and id in (select distinct user_id from trakeye_user_geofence where geofence_id=:geofenceId) ) ) group by log1.user_id ) res "
			+" where log.user_id = usr.id and res.mtime=GREATEST(log.updated_date_time,log.created_date_time) and uid=log.user_id group by status" ,nativeQuery=true)
	List<Object[]> getUsersCountByStatusForGeofence(@Param(value = "dateTime") long dateTime,@Param(value = "hour") int hour,@Param(value = "geofenceId") Long geofenceId);

}
