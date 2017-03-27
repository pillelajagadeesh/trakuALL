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

   
    
    @Query(value="select trCase.*  from trakeye_trcase as trCase, trakeye_user tuser where  "
    		+ "( trCase.assigned_to_id = tuser.id) and tuser.id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) ORDER BY ?#{#pageable}",
    		nativeQuery=true)
     Page<TrCase> findCasesForLoggedInUser(Pageable pageable);
     
     @Query(value="select trCase.*  from trakeye_trcase as trCase, trakeye_user tuser where "
    		+ " ( trCase.assigned_to_id = tuser.id) and tuser.id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) "
    		+ "and (trCase.address ilike %:searchText% or trCase.description ilike %:searchText% or CAST(trCase.id AS TEXT) ilike %:searchText% "
    		+ "or trCase.reported_by_id in (select id from trakeye_user u where  u.login ilike %:searchText%) "
    		+ "or trCase.assigned_to_id in (select id from trakeye_user u where  u.login ilike %:searchText%) "
    		+ "or trCase.priority  ilike %:searchText% "
    		+ "or trCase.case_type_id in (select id from trakeye_case_type c where  c.name ilike %:searchText%) "
    		+ ") ORDER BY ?#{#pageable}",
    		nativeQuery=true)
    
      Page<TrCase> findCasesForLoggedInUser(@Param("searchText") String searchText,Pageable pageable);
     
    /* @Query(value="select trCase from TrCase trCase where ((trCase.reportedBy.id in (select id from User user where user.createdBy = ?#{principal.username} "
     		+ " or user.login = ?#{principal.username})) OR (trCase.assignedTo.id in (select id from User user where user.createdBy = ?#{principal.username} "
     		+ " or user.login = ?#{principal.username}))) and (trCase.assignedTo.login like :login%  or trCase.reportedBy.login like :login% "
     		+ " or trCase.description like :login% or trCase.address like :login% or trCase.status like :login% or trCase.priority like :login% "
     		+ " or trCase.geofence.name like :login% or trCase.id like :login%) ",
     		countQuery = "select trCase from TrCase trCase where ((trCase.reportedBy.id in (select id from User user where user.createdBy = ?#{principal.username} "
     				+ " or user.login = ?#{principal.username})) OR (trCase.assignedTo.id in (select id from User user where user.createdBy = ?#{principal.username} "
     				+ " or user.login = ?#{principal.username}))) and (trCase.assignedTo.login like :login%  or trCase.reportedBy.login like :login% or "
     				+ " trCase.description like :login% or trCase.address like :login% "
     				+ " or trCase.status like :login% or trCase.priority like :login% or trCase.geofence.name like :login% or trCase.id like :login%) ")*/
     @Query(value="select trCase.*  from trakeye_trcase as trCase, trakeye_user tuser where "
     		+ " ( trCase.assigned_to_id = tuser.id) and tuser.id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id}))"
     		+ "and (CAST(trCase.id AS TEXT) ilike :caseId% ) ",
     		nativeQuery=true)
     List<TrCase> findCasesForLoggedInCaseSearch(@Param("caseId") String caseId);

    /* @Query(value="select trCase from TrCase trCase where ((trCase.reportedBy.id in (select id from User user where user.createdBy = ?#{principal.username} "
     		+ " or user.login = ?#{principal.username})) OR (trCase.assignedTo.id in (select id from User user where user.createdBy = ?#{principal.username} "
     		+ " or user.login = ?#{principal.username}))) and trCase.priority=:priority",
     		countQuery = "select count(trCase) from TrCase trCase where ((trCase.reportedBy.id in (select id from User user where user.createdBy = ?#{principal.username} "
     				+ " or user.login = ?#{principal.username})) OR (trCase.assignedTo.id in (select id from User user where user.createdBy = ?#{principal.username}"
     				+ " or user.login = ?#{principal.username}))) and trCase.priority=:priority")*/
     @Query(value="select trCase.*  from trakeye_trcase as trCase, trakeye_user tuser where "
      		+ " ( trCase.assigned_to_id = tuser.id) and tuser.id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id}))"
      		+ "and (trCase.priority =:priority )  ORDER BY ?#{#pageable}",
      		nativeQuery=true)
     Page<TrCase> findCasesForLoggedInUserByPriority(Pageable pageable,@Param("priority") String priority);
     
     
     
    /* @Query(value="select trCase from TrCase trCase where ((trCase.reportedBy.id in (select id from User user where user.createdBy = ?#{principal.username} "
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
      		+ " trCase.caseType.name like :searchText% or trCase.id like :searchText% or trCase.escalated like :searchText%))")*/
     @Query(value="select trCase.*  from trakeye_trcase as trCase, trakeye_user tuser where "
       		+ " ( trCase.assigned_to_id = tuser.id) and tuser.id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id}))"
       		+ "and (trCase.priority ilike %:searchText%  OR trCase.address ilike  %:searchText% or CAST(trCase.id AS TEXT) ILIKE %:searchText%) and trCase.priority =:priority  ORDER BY ?#{#pageable}",
       		nativeQuery=true)
      Page<TrCase> findCasesByPriorityAndSearch(@Param("priority") String priority, @Param("searchText") String searchText,Pageable pageable);
   
    
    /* @Query(value="select trCase from TrCase trCase where ((trCase.reportedBy.id in (select id from User user where user.createdBy = ?#{principal.username} or "
      		+ " user.login = ?#{principal.username})) OR "
      		+ " (trCase.assignedTo.id in (select id from User user where user.createdBy = ?#{principal.username} or "
      		+ " user.login = ?#{principal.username})))  order by trCase.id desc")*/
     @Query(value="select trCase.*  from trakeye_trcase as trCase, trakeye_user tuser where  "
     		+ "( trCase.assigned_to_id = tuser.id) and tuser.id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) ",
     		nativeQuery=true)
      List<TrCase> findAllCasesForLoggedInUser();
     
     
     @Query(value="select trCase.status, count(trCase.*)  from trakeye_trcase as trCase, trakeye_user tuser where "
     		+ " ( trCase.assigned_to_id = tuser.id) and tuser.id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) "
     		+ "   group by trCase.status  ",
     		nativeQuery=true)
     
     List<Object[]> findCaseCount();
     
     
     @Query(value="select trCase.*  from trakeye_trcase as trCase, trakeye_user tuser where "+ 
   "( trCase.assigned_to_id = tuser.id) and "+
   "(trCase.assigned_to_id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) or "+
   "(trCase.assigned_by_id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id}))) or "+
   "(trCase.reported_by_id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id}))) or  "+
   "(trCase.updated_by_id in (select id from get_allusers(?#{principal.username},?#{principal.tenant.id}))) ) "+
   "and (trCase.id = :caseId) and trCase.tenant_id = ?#{principal.tenant.id} ",
      		nativeQuery=true)
      TrCase findCaseById(@Param("caseId") Long caseId);
     
     @Query(value="select trCase.*  from trakeye_trcase as trCase where "
      		+ "trCase.assigned_to_id =:userid and trCase.create_date between :fromtime and :totime ",
      		nativeQuery=true)
      
      List<TrCase> casesAssignedToUserToday(@Param("fromtime") Long fromtime, @Param("totime") Long totime, @Param("userid") Long userid);
    

}
