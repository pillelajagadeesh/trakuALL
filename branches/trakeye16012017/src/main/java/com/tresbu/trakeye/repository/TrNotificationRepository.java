package com.tresbu.trakeye.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.tresbu.trakeye.domain.TrNotification;
import com.tresbu.trakeye.domain.enumeration.NotificationStatus;
import com.tresbu.trakeye.service.dto.NameCountDTO;

/**
 * Spring Data JPA repository for the TrNotification entity.
 */
@SuppressWarnings("unused")
public interface TrNotificationRepository extends JpaRepository<TrNotification,Long> {

    /*@Query("select trNotification from TrNotification trNotification where trNotification.fromUser.login = ?#{principal.username}")
    List<TrNotification> findByFromUserIsCurrentUser();*/

    /*@Query("select trNotification from TrNotification trNotification where trNotification.toUser.login = ?#{principal.username}")
    List<TrNotification> findByToUserIsCurrentUser();*/

    /*@Query("select trNotification from TrNotification trNotification where trNotification.status != 'DELIVERED'")
    List<TrNotification> findByStatus();*/
    
    /*@Query("update TrNotification trNotification set trNotification.status = :status where trNotification.id = :id ")
    @Modifying
    @Transactional
    int updateStatus(@Param("id") Long id, @Param("status") NotificationStatus status);*/
    
/* @Query(value = "select distinct trNotification from TrNotification trNotification where (((trNotification.toUser.login = ?#{principal.username}) "
    		+ " or (trNotification.fromUser.login = ?#{principal.username}))  and (trNotification.subject like :searchText% or trNotification.status like :searchText% "
    		+ " or trNotification.alertType like :searchText% or trNotification.toUser.login like :searchText%  ))", 
    		countQuery = "select count(trNotification) from TrNotification trNotification where (((trNotification.toUser.login = ?#{principal.username}) "
    		+ " or (trNotification.fromUser.login = ?#{principal.username}))  and (trNotification.subject like :searchText% or trNotification.status like :searchText% "
    		+ " or trNotification.alertType like :searchText% or trNotification.toUser.login like :searchText%  ))")*/
	@Query(value = "select trNotification.* from trakeye_notification trNotification , trakeye_user tuser where (trNotification.to_user_id = tuser.id) "
	    		+ " and tuser.id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) "
	    		+ " and (trNotification.subject ilike %:searchText% or trNotification.status ilike %:searchText% "
	    		+ " or trNotification.alert_type ilike %:searchText% ) ORDER BY ?#{#pageable}", nativeQuery=true)
    Page<TrNotification> findAll(@Param("searchText") String searchText,Pageable pageable) ;
	
	
	@Query(value = "select trNotification.status, count(trNotification.*) from trakeye_notification trNotification , trakeye_user tuser where (trNotification.to_user_id = tuser.id) "
    		+ " and tuser.id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) "
    		+ " group by trNotification.status ", nativeQuery=true)
	List<Object[]> findNotificationCount() ;
	
	
	@Query(value = "select trNotification.* from trakeye_notification trNotification , trakeye_user tuser where (trNotification.to_user_id = tuser.id) "
    		+ " and tuser.id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) "
    		+ " and trNotification.id = :notificationId and trNotification.tenant_id = ?#{principal.tenant.id}  ORDER BY ?#{#pageable}", nativeQuery=true)
 TrNotification findNotificationById(@Param("notificationId") Long notificationId) ;
    
  
}
