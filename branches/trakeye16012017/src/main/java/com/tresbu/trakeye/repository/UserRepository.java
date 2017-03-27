

package com.tresbu.trakeye.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.service.dto.ReportDTO;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findOneByActivationKey(String activationKey);

	List<User> findAllByActivatedIsFalseAndCreatedDateBefore(long dateTime);

		@Query(value="select * from trakeye_user as a where id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) and login ilike %:name% ORDER BY ?#{#pageable}",
			nativeQuery=true)
	List<User> findAllUsersByName(@Param("name")String name);
	
	Optional<User> findOneByResetKey(String resetKey);
		
		@Query(value="select * from trakeye_user as a where id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id}))  ORDER BY ?#{#pageable}",
			nativeQuery=true)
	List<User> findAllByCreatedBy();

	Optional<User> findOneByEmail(String email);

	Optional<User> findOneByLogin(String login);

	Optional<User> findOneById(Long userId);
	
	
	@Query(value="select * from trakeye_user as a where  imei = :imei and activated = true",
			nativeQuery=true)
	Optional<User> findOneByImei(@Param("imei")String imei);
		
		@Query(value="select * from trakeye_user as a where id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id}))  ORDER BY ?#{#pageable}",
			nativeQuery=true)
	Page<User> findAllUsersWithAuthorities(Pageable pageable);

	@Query(value = "select distinct user from User user join fetch user.authorities as authority where authority.name = :authority")
    User findUserWithAuthoritySuperAdmin(@Param("authority") String authority);
    
		@Query(value="select * from trakeye_user as a where id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) AND (login ilike %:searchText% or email ilike %:searchText% "
			        + " or phone ilike %:searchText% or imei ilike %:searchText% or operating_system ilike %:searchText% )  ORDER BY ?#{#pageable}",
			nativeQuery=true)
	Page<User> findAllUsersBySearchText(@Param("searchText")String searchText,Pageable pageable);

	@Override
	void delete(User t);

	@Query(value="select * from trakeye_user as a where id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id}))  ORDER BY ?#{#pageable}",
			nativeQuery=true)
    List<User> findUsersByLogin();
	
	@Query(value="select * from trakeye_user as a where id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) and activated = true  ORDER BY ?#{#pageable}",
			nativeQuery=true)
	List<User> findActivatedUsersByLogin();

	
	@Query(value=
			" select * from (select usr.*,case  when log.created_date_time >=:dateAndTime and log.updated_date_time = 0 then 'ACTIVE' "
			+" when  log.updated_date_time >=:dateAndTime then 'IDLE'  else  'INACTIVE'  END as status "
			+" from trakeye_location_log log , trakeye_user usr , "
			+" (select log1.user_id uid,max(GREATEST(log1.updated_date_time,log1.created_date_time)) mtime from trakeye_location_log log1, trakeye_user usr1 where "
			+"  log1.user_id = usr1.id and usr1.id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) and  ( (:hour >= from_time and :hour <= to_time and from_time<=to_time) or "
			+"   (NOT (:hour < from_time and :hour > to_time) and from_time>=to_time) )  "
			+"  group by log1.user_id ) res  where log.user_id = usr.id and res.mtime=GREATEST(log.updated_date_time,log.created_date_time) "
			+"  and uid=log.user_id and usr.activated=true ) as log where log.status =:status ORDER BY ?#{#pageable}",
			countQuery=" select count(*) from(select usr.*,case  when log.created_date_time >=:dateAndTime and log.updated_date_time = 0 then 'ACTIVE' "
					+" when  log.updated_date_time >=:dateAndTime then 'IDLE'  else  'INACTIVE'  END as status "
					+" from trakeye_location_log log , trakeye_user usr , "
					+" (select log1.user_id uid,max(GREATEST(log1.updated_date_time,log1.created_date_time)) mtime from trakeye_location_log log1, trakeye_user usr1 where "
					+"  log1.user_id = usr1.id and usr1.id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) and  ( (:hour >= from_time and :hour <= to_time and from_time<=to_time) or "
					+"   (NOT (:hour < from_time and :hour > to_time) and from_time>=to_time) )  "
					+"  group by log1.user_id ) res  where log.user_id = usr.id and res.mtime=GREATEST(log.updated_date_time,log.created_date_time) "
					+"  and uid=log.user_id and usr.activated=true ) as log where log.status =:status ORDER BY ?#{#pageable}"
			,nativeQuery=true)
	Page<User> getUsersByStatus(@Param("dateAndTime") long dateAndTime, @Param("hour") int hour, @Param("status")String status,Pageable pageable);
	
	@Query(nativeQuery = true)
	ReportDTO userAgentReport(@Param("fromTime") long fromTime,@Param("toTime")  long toTime,@Param("userId")  long userId);
	
	@Query(nativeQuery = true)
	ReportDTO userAgentDetailedReport(@Param("fromTime") long fromTime,@Param("toTime")  long toTime,@Param("userId")  long userId);
	
	@Query(nativeQuery = true)
	List<ReportDTO> userAgentsReport(@Param("fromTime") long fromTime,@Param("toTime")  long toTime,@Param("createdBy")  String createdBy);
	
	@Query(value=" select * from("
			+" select usr.*,case  when log.created_date_time >=:dateTime and log.updated_date_time = 0 then 'ACTIVE' "
			+" when  log.updated_date_time >=:dateTime then 'IDLE'  else  'INACTIVE'  END as status "
			+" from trakeye_location_log log , trakeye_user usr , "
			+" (select log1.user_id uid,max(GREATEST(log1.updated_date_time,log1.created_date_time)) mtime from trakeye_location_log log1, trakeye_user usr1 where "
			+"  log1.user_id = usr1.id and usr1.id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) and  ( (:hour >= from_time and :hour <= to_time and from_time<=to_time) or "
			+"   (NOT (:hour < from_time and :hour > to_time) and from_time>=to_time) )  "
			+"  group by log1.user_id ) res  where log.user_id = usr.id and res.mtime=GREATEST(log.updated_date_time,log.created_date_time) "
			+"  and uid=log.user_id and usr.activated=true ) as log where log.status =:status and (log.login ilike %:searchText% "
            + "or log.email ilike %:searchText% or log.phone ilike %:searchText% or log.imei ilike %:searchText% ) ORDER BY ?#{#pageable}",
            countQuery=" select count(*) from("
        			+" select usr.*,case  when log.created_date_time >=:dateTime and log.updated_date_time = 0 then 'ACTIVE' "
        			+" when  log.updated_date_time >=:dateTime then 'IDLE'  else  'INACTIVE'  END as status "
        			+" from trakeye_location_log log , trakeye_user usr , "
        			+" (select log1.user_id uid,max(GREATEST(log1.updated_date_time,log1.created_date_time)) mtime from trakeye_location_log log1, trakeye_user usr1 where "
        			+"  log1.user_id = usr1.id and usr1.id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) and  ( (:hour >= from_time and :hour <= to_time and from_time<=to_time) or "
        			+"   (NOT (:hour < from_time and :hour > to_time) and from_time>=to_time) )  "
        			+"  group by log1.user_id ) res  where log.user_id = usr.id and res.mtime=GREATEST(log.updated_date_time,log.created_date_time) "
        			+"  and uid=log.user_id and usr.activated=true ) as log where log.status =:status and (log.login ilike %:searchText% "
                    + "or log.email ilike %:searchText% or log.phone ilike %:searchText% or log.imei ilike %:searchText% ) "
			,nativeQuery=true)
	Page<User> getUsersForStausSearch(@Param("dateTime") long dateTime, @Param( "hour") int hour, @Param("status")String status, @Param("searchText")String searchText,Pageable pageable);
	
	@Query(value = "select user from User user where user.trakeyeType.name = :trakeyeType")
	List<User> getUsersByTrakeyeType(@Param("trakeyeType") String trakeyeType);
	
	@Query(value="select * from trakeye_user as a where id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) "
			+ "and activated = true AND (login ilike %:searchText% "
	        + " )  ORDER BY ?#{#pageable}",
	nativeQuery=true)
Page<User> searchactivatedusers(@Param("searchText")String searchText,Pageable pageable);
	
	@Query(value="select * from trakeye_user as tuser  where id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) "
			+ "and login = :login and tuser.tenant_id = ?#{principal.tenant.id}",
			nativeQuery = true)
	Optional<User> findUserByLogin(@Param ("login") String login);
	
}
