package com.tresbu.trakeye.repository;

import com.tresbu.trakeye.domain.CaseType;
import com.tresbu.trakeye.domain.TrCase;
import com.tresbu.trakeye.domain.enumeration.CasePriority;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the TrCase entity.
 */
@SuppressWarnings("unused")
public interface TrCaseRepository extends JpaRepository<TrCase,Long> {

    @Query("select trCase from TrCase trCase where trCase.reportedBy.login = ?#{principal.username}")
    List<TrCase> findByReportedByIsCurrentUser();

    @Query("select trCase from TrCase trCase where trCase.updatedBy.login = ?#{principal.username}")
    List<TrCase> findByUpdatedByIsCurrentUser();

    @Query("select trCase from TrCase trCase where trCase.assignedBy.login = ?#{principal.username}")
    List<TrCase> findByAssignedByIsCurrentUser();
    
    
     @Query(value="select trCase from TrCase trCase where ((trCase.reportedBy.id in (select id from User user where user.createdBy = ?#{principal.username} or "
     		+ " user.login = ?#{principal.username})) OR "
     		+ " (trCase.assignedTo.id in (select id from User user where user.createdBy = ?#{principal.username} or "
     		+ " user.login = ?#{principal.username})))  order by trCase.id desc",
    		countQuery = "select count(trCase) from TrCase trCase where ((trCase.reportedBy.id in (select id from User user where user.createdBy = ?#{principal.username} or"
    				+ " user.login = ?#{principal.username})) OR (trCase.assignedTo.id in (select id from User user where user.createdBy = ?#{principal.username} or"
    				+ " user.login = ?#{principal.username})))")
     Page<TrCase> findCasesForLoggedInUser(Pageable pageable);
     
     
     @Query(value="select trCase from TrCase trCase where ((trCase.reportedBy.id in (select id from User user where user.createdBy = ?#{principal.username} or "
      		+ " user.login = ?#{principal.username})) OR "
      		+ " (trCase.assignedTo.id in (select id from User user where user.createdBy = ?#{principal.username} or "
      		+ " user.login = ?#{principal.username}))) and (trCase.assignedTo.login like :searchText%  or "
      		+ " trCase.reportedBy.login like :searchText%  or trCase.address like :searchText% or "
      		+ " trCase.status like :searchText% or trCase.priority like :searchText% or trCase.geofence.name like :searchText%  or "
      		+ " trCase.caseType.name like :searchText% or trCase.id like :searchText% or trCase.escalated like :searchText%)  order by trCase.id desc",
     		countQuery = "select count(trCase) from TrCase trCase where ((trCase.reportedBy.id in (select id from User user where user.createdBy = ?#{principal.username} or"
     				+ " user.login = ?#{principal.username})) OR (trCase.assignedTo.id in (select id from User user where user.createdBy = ?#{principal.username} or"
     				+ " user.login = ?#{principal.username}))) and (trCase.assignedTo.login like :searchText%  or "
      		+ " trCase.reportedBy.login like :searchText% or trCase.address like :searchText% or "
      		+ " trCase.status like :searchText% or trCase.priority like :searchText% or trCase.geofence.name like :searchText%  or "
      		+ " trCase.caseType.name like :searchText% or trCase.id like :searchText% or trCase.escalated like :searchText%)")
      Page<TrCase> findCasesForLoggedInUser(@Param("searchText") String searchText,Pageable pageable);
     
     @Query(value="select trCase from TrCase trCase where ((trCase.reportedBy.id in (select id from User user where user.createdBy = ?#{principal.username} "
     		+ " or user.login = ?#{principal.username})) OR (trCase.assignedTo.id in (select id from User user where user.createdBy = ?#{principal.username} "
     		+ " or user.login = ?#{principal.username}))) and (trCase.assignedTo.login like :login%  or trCase.reportedBy.login like :login% "
     		+ " or trCase.description like :login% or trCase.address like :login% or trCase.status like :login% or trCase.priority like :login% "
     		+ " or trCase.geofence.name like :login% or trCase.id like :login%) ",
     		countQuery = "select trCase from TrCase trCase where ((trCase.reportedBy.id in (select id from User user where user.createdBy = ?#{principal.username} "
     				+ " or user.login = ?#{principal.username})) OR (trCase.assignedTo.id in (select id from User user where user.createdBy = ?#{principal.username} "
     				+ " or user.login = ?#{principal.username}))) and (trCase.assignedTo.login like :login%  or trCase.reportedBy.login like :login% or "
     				+ " trCase.description like :login% or trCase.address like :login% "
     				+ " or trCase.status like :login% or trCase.priority like :login% or trCase.geofence.name like :login% or trCase.id like :login%) ")
     List<TrCase> findCasesForLoggedInCaseSearch(@Param("login") String login);

     @Query(value="select trCase from TrCase trCase where ((trCase.reportedBy.id in (select id from User user where user.createdBy = ?#{principal.username} "
     		+ " or user.login = ?#{principal.username})) OR (trCase.assignedTo.id in (select id from User user where user.createdBy = ?#{principal.username} "
     		+ " or user.login = ?#{principal.username}))) and trCase.priority=:priority",
     		countQuery = "select count(trCase) from TrCase trCase where ((trCase.reportedBy.id in (select id from User user where user.createdBy = ?#{principal.username} "
     				+ " or user.login = ?#{principal.username})) OR (trCase.assignedTo.id in (select id from User user where user.createdBy = ?#{principal.username}"
     				+ " or user.login = ?#{principal.username}))) and trCase.priority=:priority")
     Page<TrCase> findCasesForLoggedInUserByPriority(Pageable pageable,@Param("priority") CasePriority priority);
     
     
     
     @Query(value="select trCase from TrCase trCase where ((trCase.reportedBy.id in (select id from User user where user.createdBy = ?#{principal.username} "
      		+ " or user.login = ?#{principal.username})) OR (trCase.assignedTo.id in (select id from User user where user.createdBy = ?#{principal.username} "
      		+ " or user.login = ?#{principal.username})) and (trCase.priority=:priority) and (trCase.assignedTo.login like :searchText%  or "
      		+ " trCase.reportedBy.login like :searchText% or trCase.description like :searchText% or trCase.address like :searchText% or "
      		+ " trCase.status like :searchText% or trCase.priority like :searchText% or trCase.geofence.name like :searchText%  or "
      		+ " trCase.caseType.name like :searchText% or trCase.id like :searchText% or trCase.escalated like :searchText%))",
      		countQuery = "select count(trCase) from TrCase trCase where ((trCase.reportedBy.id in (select id from User user where user.createdBy = ?#{principal.username} "
      				+ " or user.login = ?#{principal.username})) OR (trCase.assignedTo.id in (select id from User user where user.createdBy = ?#{principal.username}"
      				+ " or user.login = ?#{principal.username})) and (trCase.priority=:priority) and (trCase.assignedTo.login like :searchText%  or "
      		+ " trCase.reportedBy.login like :searchText% or trCase.description like :searchText% or trCase.address like :searchText% or "
      		+ " trCase.status like :searchText% or trCase.priority like :searchText% or trCase.geofence.name like :searchText%  or "
      		+ " trCase.caseType.name like :searchText% or trCase.id like :searchText% or trCase.escalated like :searchText%))")
      Page<TrCase> findCasesByPriorityAndSearch(@Param("priority") CasePriority priority, @Param("searchText") String searchText,Pageable pageable);
   
    
     @Query(value="select trCase from TrCase trCase where ((trCase.reportedBy.id in (select id from User user where user.createdBy = ?#{principal.username} or "
      		+ " user.login = ?#{principal.username})) OR "
      		+ " (trCase.assignedTo.id in (select id from User user where user.createdBy = ?#{principal.username} or "
      		+ " user.login = ?#{principal.username})))  order by trCase.id desc")
      List<TrCase> findAllCasesForLoggedInUser();
    

}
