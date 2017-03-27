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

    @Query("select trNotification from TrNotification trNotification where trNotification.fromUser.login = ?#{principal.username}")
    List<TrNotification> findByFromUserIsCurrentUser();

    @Query("select trNotification from TrNotification trNotification where trNotification.toUser.login = ?#{principal.username}")
    List<TrNotification> findByToUserIsCurrentUser();

    @Query("select trNotification from TrNotification trNotification where trNotification.status != 'DELIVERED'")
    List<TrNotification> findByStatus();
    
    @Query("update TrNotification trNotification set trNotification.status = :status where trNotification.id = :id ")
    @Modifying
    @Transactional
    int updateStatus(@Param("id") Long id, @Param("status") NotificationStatus status);
    
    @Query(value = "select distinct trNotification from TrNotification trNotification where ((trNotification.toUser.login = ?#{principal.username}) "
    		+ " or (trNotification.fromUser.login = ?#{principal.username}))  and (trNotification.subject like :searchText% or trNotification.status like :searchText% "
    		+ " or trNotification.alertType like :searchText% or trNotification.toUser.login like :searchText% or trNotification.id like :searchText% "
    		+ " or trNotification.trCase.id like :searchText% )", 
    		countQuery = "select count(trNotification) from TrNotification trNotification where  trNotification.toUser.login = ?#{principal.username} "
    		+ " or trNotification.fromUser.login = ?#{principal.username} and ( trNotification.subject like :searchText% or trNotification.status like :searchText% "
    		+ " or trNotification.alertType like :searchText% or trNotification.toUser.login like :searchText% or trNotification.id like :searchText% "
    		+ " or trNotification.trCase.id like :searchText% )")
    Page<TrNotification> findAll(@Param("searchText") String searchText,Pageable pageable) ;
    
}
