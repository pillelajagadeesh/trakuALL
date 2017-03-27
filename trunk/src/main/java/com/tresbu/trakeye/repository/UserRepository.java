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

	@Query(value = "select user from User user where user.login like :name% and user.createdBy = ?#{principal.username} and user.activated=1")
	List<User> findAllUsersByName(@Param("name")String name);
	
	Optional<User> findOneByResetKey(String resetKey);
		
	@Query(value = "select user from User user where  user.createdBy = ?#{principal.username}")
	List<User> findAllByCreatedBy();

	Optional<User> findOneByEmail(String email);

	Optional<User> findOneByLogin(String login);

	Optional<User> findOneById(Long userId);
	
	Optional<User> findOneByImei(String imei);
		
	@Query(value = "select distinct user from User user join fetch user.authorities", countQuery = "select count(user) from User user where user.createdBy = ?#{principal.username}")
	Page<User> findAllWithAuthorities(Pageable pageable);

	@Query(value = "select distinct user from User user join fetch user.authorities where user.createdBy = ?#{principal.username} order by user.id desc", 
			countQuery = "select count(user) from User user where user.createdBy = ?#{principal.username} ")
	Page<User> findAllUsersWithAuthorities(Pageable pageable);

	/*	@Query(value = "select distinct user from User user join fetch user.authorities where user.authorities = 'admin'", 
	countQuery = "select count(user) from User user where user.createdBy = ?#{principal.username} ")*/
    @Query(value = "select distinct user from User user join fetch user.authorities as authority where authority.name = :authority")
    User findUserWithAuthoritySuperAdmin(@Param("authority") String authority);
    
	@Query(value = "select distinct user from User user join fetch user.authorities where ((user.createdBy = ?#{principal.username}) and (user.login like :searchText% or user.email like :searchText% "
			+ " or user.phone like :searchText% or user.imei like :searchText% or user.operatingSystem like :searchText%))  order by user.id desc", countQuery = "select count(user) from User user where ((user.createdBy = ?#{principal.username}) and (user.login like :searchText% or user.email like :searchText% "
			+ " or user.phone like :searchText% or user.imei like :searchText% or user.operatingSystem like :searchText%))")
	Page<User> findAllUsersBySearchText(@Param("searchText")String searchText,Pageable pageable);

	@Override
	void delete(User t);

	@Query(value = "select user from User user where user.createdBy = ?#{principal.username} order by user.id desc", countQuery = "select count(user) from User user where user.createdBy = ?#{principal.username}")
    List<User> findUsersByLogin();
	
	@Query(value = "select user from User user where user.createdBy = ?#{principal.username} and user.activated=1")
    List<User> findActivatedUsersByLogin();

	@Query(value=" select * from ( select @rownum /*'*/:=/*'*/@rownum + 1 as RN,usr.*, "
			+" case "
			+" when log.created_date_time >=:dateTime and log.updated_date_time = 0 then 'ACTIVE'"
			+" when  log.updated_date_time >=:dateTime then 'IDLE' "
			+" else  'INACTIVE' "
			+" END as status "
			+" from trakeye_location_log log , trakeye_user usr ,"
			+" (select log1.user_id as uid,max(GREATEST(log1.updated_date_time,log1.created_date_time)) mtime from trakeye_location_log log1, trakeye_user usr1"
			+" where log1.user_id = usr1.id and usr1.id in (select id from  trakeye_user where ((login = ?#{principal.username}  or created_by= ?#{principal.username}) and "
			+ " ( (:hour >= from_time and :hour <= to_time and from_time<=to_time) or (NOT (:hour < from_time and :hour > to_time) and from_time>=to_time) ) ) ) group by log1.user_id ) res,(SELECT @rownum /*'*/:=/*'*/ 0) r  "
			+ " where log.user_id = usr.id and res.mtime=GREATEST(log.updated_date_time,log.created_date_time) and uid=log.user_id) as tmp where tmp.status= :status and tmp.activated=1"
            + " and RN between ?#{ #pageable.offset -1} and ?#{#pageable.offset + #pageable.pageSize}" 
			,countQuery = "select count(*) from ( select @rownum /*'*/:=/*'*/@rownum + 1 as RN,usr.*, "
			+" case "
			+" when log.created_date_time >=:dateTime and log.updated_date_time = 0 then 'ACTIVE'"
			+" when  log.updated_date_time >=:dateTime then 'IDLE' "
			+" else  'INACTIVE' "
			+" END as status "
			+" from trakeye_location_log log , trakeye_user usr ,"
			+" (select log1.user_id as uid,max(GREATEST(log1.updated_date_time,log1.created_date_time)) mtime from trakeye_location_log log1, trakeye_user usr1"
			+" where log1.user_id = usr1.id and usr1.id in (select id from  trakeye_user where ((login = ?#{principal.username}  or created_by= ?#{principal.username}) and "
			+ " ( (:hour >= from_time and :hour <= to_time and from_time<=to_time) or (NOT (:hour < from_time and :hour > to_time) and from_time>=to_time) ) ) ) group by log1.user_id ) res ,(SELECT @rownum /*'*/:=/*'*/ 0) r "
			+ " where log.user_id = usr.id and res.mtime=GREATEST(log.updated_date_time,log.created_date_time) and uid=log.user_id ) as tmp where tmp.status= :status and tmp.activated=1",nativeQuery=true)
	Page<User> getUsersByStatus(@Param(value = "dateTime") long dateTime, @Param(value = "hour") int hour, @Param(value="status")String status,Pageable pageable);
	
	@Query(nativeQuery = true)
	ReportDTO userAgentReport(@Param("fromTime") long fromTime,@Param("toTime")  long toTime,@Param("userId")  long userId);
	
	@Query(nativeQuery = true)
	ReportDTO userAgentDetailedReport(@Param("fromTime") long fromTime,@Param("toTime")  long toTime,@Param("userId")  long userId);
	
	@Query(nativeQuery = true)
	List<ReportDTO> userAgentsReport(@Param("fromTime") long fromTime,@Param("toTime")  long toTime,@Param("createdBy")  String createdBy);
	
	
	@Query(value=" select * from ( select @rownum /*'*/:=/*'*/@rownum + 1 as RN,usr.*, "
			+" case "
			+" when log.created_date_time >=:dateTime and log.updated_date_time = 0 then 'ACTIVE'"
			+" when  log.updated_date_time >=:dateTime then 'IDLE' "
			+" else  'INACTIVE' "
			+" END as status "
			+" from trakeye_location_log log , trakeye_user usr ,"
			+" (select log1.user_id as uid,max(GREATEST(log1.updated_date_time,log1.created_date_time)) mtime from trakeye_location_log log1, trakeye_user usr1"
			+" where log1.user_id = usr1.id and usr1.id in (select id from  trakeye_user where ((login = ?#{principal.username}  or created_by= ?#{principal.username}) and "
			+ " ( (:hour >= from_time and :hour <= to_time and from_time<=to_time) or (NOT (:hour < from_time and :hour > to_time) and from_time>=to_time) ) ) ) group by log1.user_id ) res,(SELECT @rownum /*'*/:=/*'*/ 0) r  "
			+ " where log.user_id = usr.id and res.mtime=GREATEST(log.updated_date_time,log.created_date_time) and uid=log.user_id) as tmp where ((tmp.status= :status) and (tmp.activated=1) and (tmp.login like :searchText% "
			+ "or tmp.email like :searchText% or tmp.phone like :searchText% or tmp.imei like :searchText% ))"
			+ " and RN between ?#{ #pageable.offset -1} and ?#{#pageable.offset + #pageable.pageSize}" 
			,countQuery = "select count(*) from ( select @rownum /*'*/:=/*'*/@rownum + 1 as RN,usr.*, "
			+" case "
			+" when log.created_date_time >=:dateTime and log.updated_date_time = 0 then 'ACTIVE'"
			+" when  log.updated_date_time >=:dateTime then 'IDLE' "
			+" else  'INACTIVE' "
			+" END as status "
			+" from trakeye_location_log log , trakeye_user usr ,"
			+" (select log1.user_id as uid,max(GREATEST(log1.updated_date_time,log1.created_date_time)) mtime from trakeye_location_log log1, trakeye_user usr1"
			+" where log1.user_id = usr1.id and usr1.id in (select id from  trakeye_user where ((login = ?#{principal.username}  or created_by= ?#{principal.username}) and "
			+ " ( (:hour >= from_time and :hour <= to_time and from_time<=to_time) or (NOT (:hour < from_time and :hour > to_time) and from_time>=to_time) ) ) ) group by log1.user_id ) res ,(SELECT @rownum /*'*/:=/*'*/ 0) r "
			+ " where log.user_id = usr.id and res.mtime=GREATEST(log.updated_date_time,log.created_date_time) and uid=log.user_id ) as tmp where ((tmp.status= :status) and (tmp.activated=1) and (tmp.login like :searchText% "
			+ "or tmp.email like :searchText% or tmp.phone like :searchText% or tmp.imei like :searchText% ))",nativeQuery=true)
	Page<User> getUsersForStausSearch(@Param(value = "dateTime") long dateTime, @Param(value = "hour") int hour, @Param(value="status")String status, @Param(value="searchText")String searchText,Pageable pageable);
	
	@Query(value = "select user from User user where user.trakeyeType.name = :trakeyeType")
	List<User> getUsersByTrakeyeType(@Param("trakeyeType") String trakeyeType);
	
}
