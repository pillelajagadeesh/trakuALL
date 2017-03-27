package com.tresbu.trakeye.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tresbu.trakeye.domain.TrService;

/**
 * Spring Data JPA repository for the TrService entity.
 */
public interface DashboardRepository extends JpaRepository<TrService, Long> {

	@Query(value = "select trservice.status as status , count(distinct trservice.id) as count from TrService trservice where trservice.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}) group by trservice.status")
	List<Object[]> getServiceCountByStatus();

	/*@Query(value = "select geofence.name ,count(distinct user.id) from User user , IN (user.geofences) geofence where  geofence.id in (select geofence.id from Geofence geofence where geofence.user.id=(select id from user where login=?#{principal.username}))"
			+ "and user.id in (select distinct locationlog.user.id from LocationLog locationlog where locationlog.user.id in (select id from user where createdBy=?#{principal.username}) and locationlog.createdDateTime>=:dateTime ) and user.activated=true group by geofence.name")
	List<Object[]> getCurrentlyActiveUsersBygeofence(@Param(value = "dateTime") long dateTime);

	@Query(value = "select geofence.name ,count(distinct user.id) from User user , IN (user.geofences) geofence where  geofence.id in (select geofence.id from Geofence geofence where geofence.user.id=(select id from user where login=?#{principal.username}))"
			+ "and user.id in (select id from user where createdBy=?#{principal.username}) and user.activated=true group by geofence.name")
	List<Object[]> getActiveUsersBygeofence();*/
	
	   
    @Query("select trNotification.status as status, count(distinct trNotification.id) as count from TrNotification trNotification where   "
    		+ " trNotification.fromUser.login = ?#{principal.username} or trNotification.toUser.login = ?#{principal.username} "
    		+ "or trNotification.fromUser.login in (select login from User where createdBy = ?#{principal.username}) "
    		+ "or trNotification.toUser.login in (select login from User where createdBy = ?#{principal.username}) group by trNotification.status")
    List<Object[]> getNotificationbyStatus();
    
    
    @Query("select count(trNotification.id) as count from TrNotification trNotification where  trNotification.toUser.login = ?#{principal.username}" )
    long getRecievedNotifictionsCount();
    
    @Query("select count(trNotification.id) as count from TrNotification trNotification where  trNotification.fromUser.login = ?#{principal.username}" )
    long getSentNotifictionsCount();
    
    @Query(value="select trCase.priority as priority ,count(distinct trCase.id) as count from TrCase "
    		+ "trCase where trCase.reportedBy.id in (select id from User user where user.createdBy = ?#{principal.username}"
    		+ " or user.login = ?#{principal.username}) OR trCase.assignedTo.id in (select id from User user where user.createdBy = ?#{principal.username} "
    		+ "or user.login = ?#{principal.username}) group by trCase.priority")
	List<Object[]> getCaseCountByPriority();
    
    @Query(value="select trCase.status as status ,count(distinct trCase.id) as count from TrCase "
    		+ "trCase where trCase.reportedBy.id in (select id from User user where user.createdBy = ?#{principal.username}"
    		+ " or user.login = ?#{principal.username}) OR trCase.assignedTo.id in (select id from User user where user.createdBy = ?#{principal.username} "
    		+ "or user.login = ?#{principal.username}) group by trCase.status")
	List<Object[]> getCaseCountByStatus();
	
	/*@Query(value = "select count(distinct user.id)  from User user where user.createdBy = ?#{principal.username} and user.activated=false")
	long inActiveUsersCount();

	@Query(value = "select count(distinct user.id)  from User user,LocationLog locationLog where user.createdBy = ?#{principal.username}"
			+ " and user.activated=true and locationLog.user.id=user.id and locationLog.createdDateTime >= :dateTime")
	long getCurrentlyActiveUsersCount(@Param("dateTime") long dateTime);

	@Query(value = "select count(distinct user.id)  from User user where user.createdBy = ?#{principal.username} and user.activated=true")
	long getActiveUsersCount();*/
	
	@Query(value="select distinct(user.login) as login,"+
			" (select IFNULL(sum(log.distance_travelled) , 0) from trakeye_location_log log where log.created_date_time "
			+ " between :fromTime and :toTime and user_id=user.id) as distance" +
			" from trakeye_user user where user.created_by=?#{principal.username} and user.activated=1",nativeQuery=true)
	List<Object[]> getUsersListWithDistance(@Param(value = "fromTime") long fromTime,@Param(value = "toTime") long toTime);
	 
	@Query(value="select distinct(user.login) as login,"+
			" (select IFNULL(sum(log.distance_travelled) , 0) from trakeye_location_log log where log.created_date_time "
			+ " between :fromTime and :toTime and user_id=user.id) as distance" +
			" from trakeye_user user where user.created_by=?#{principal.username} and user.activated=1 and user.login like :searchText%",nativeQuery=true)
	List<Object[]> getUsersListWithDistanceBySearch(@Param(value = "fromTime") long fromTime,@Param(value = "toTime") long toTime,@Param("searchText") String searchText);
	
	@Query(value="select  "
			+"case "
			+" when log.created_date_time >=:dateTime and log.updated_date_time = 0 then 'ACTIVE'"
			+" when  log.updated_date_time >=:dateTime then 'IDLE' "
			+" else  'INACTIVE' "
			+" END as status ,count(distinct usr.id) as userid"
			+" from trakeye_location_log log , trakeye_user usr ,"
			+" (select log1.user_id uid,max(GREATEST(log1.updated_date_time,log1.created_date_time)) mtime from trakeye_location_log log1, trakeye_user usr1"
			+" where log1.user_id = usr1.id and usr1.id in (select id from  trakeye_user where ((login = ?#{principal.username}  or created_by= ?#{principal.username}) and "
			+ " ( (:hour >= from_time and :hour <= to_time and from_time<=to_time) or (NOT (:hour < from_time and :hour > to_time) and from_time>=to_time) ) ) ) group by log1.user_id ) res "
			+" where log.user_id = usr.id and res.mtime=GREATEST(log.updated_date_time,log.created_date_time) and uid=log.user_id and usr.activated=1 group by status" ,nativeQuery=true)
	List<Object[]> getUsersCountByStatus(@Param(value = "dateTime") long dateTime,@Param(value = "hour") int hour);
	
	@Query(value="select  "
			+"case "
			+" when log.created_date_time >=:dateTime and log.updated_date_time = 0 then 'ACTIVE'"
			+" when  log.updated_date_time >=:dateTime then 'IDLE' "
			+" else  'INACTIVE' "
			+" END as status ,count(distinct usr.id) as userid"
			+" from trakeye_location_log log , trakeye_user usr ,"
			+" (select log1.user_id uid,max(GREATEST(log1.updated_date_time,log1.created_date_time)) mtime from trakeye_location_log log1, trakeye_user usr1"
			+" where log1.user_id = usr1.id and usr1.id in (select id from  trakeye_user where ((login = ?#{principal.username}  or created_by= ?#{principal.username} and activated=1) and "
			+ " ( (:hour >= from_time and :hour <= to_time and from_time<=to_time) or (NOT (:hour < from_time and :hour > to_time) and from_time>=to_time) ) and id in (select distinct user_id from trakeye_user_geofence where geofence_id=:geofenceId) ) ) group by log1.user_id ) res "
			+" where log.user_id = usr.id and res.mtime=GREATEST(log.updated_date_time,log.created_date_time) and uid=log.user_id group by status" ,nativeQuery=true)
	List<Object[]> getUsersCountByStatusForGeofence(@Param(value = "dateTime") long dateTime,@Param(value = "hour") int hour,@Param(value = "geofenceId") Long geofenceId);

}
