package com.tresbu.trakeye.repository;

import com.tresbu.trakeye.domain.TrService;
import com.tresbu.trakeye.domain.TrakeyeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the TrService entity.
 */
@SuppressWarnings("unused")
public interface TrServiceRepository extends JpaRepository<TrService,Long> {

    @Query("select trService from TrService trService where trService.user.login = ?#{principal.username}")
    List<TrService> findByUserIsCurrentUser();
    
    @Query(value="select trservice from TrService trservice where trservice.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username})",
    		countQuery = "select count(trservice) from TrService trservice where trservice.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username})")
    Page<TrService> findServicesForLoggedInUser(Pageable pageable);
   
    @Query(
            value = "Select a.* from(SELECT @rownum /*'*/:=/*'*/@rownum + 1 as RN, t.* FROM ( SELECT distinct a.* from trakeye_trservice a, trakeye_trcase b , trakeye_user c "
            		+ "where (a.tr_case_id=b.id and b.assigned_to_id = c.id and c.login = ?#{principal.username}) or (a.user_id = c.id and c.login=?#{principal.username})) t,(SELECT @rownum /*'*/:=/*'*/ 0) r) a "
            		+ "where RN between ?#{ #pageable.offset -1} and ?#{#pageable.offset + #pageable.pageSize} order by a.id desc",
            countQuery = "Select count(*) from(SELECT @rownum/*'*/:=/*'*/@rownum + 1 as RN, t.* FROM ( SELECT distinct a.* from trakeye_trservice a, trakeye_trcase b , trakeye_user c "
            		+ "where (a.tr_case_id=b.id and b.assigned_to_id = c.id and c.login = ?#{principal.username}) or (a.user_id = c.id and c.login=?#{principal.username})) t,(SELECT @rownum /*'*/:=/*'*/ 0) r) a ", nativeQuery = true)
    Page<TrService> findUserServices(Pageable pageable);
    
    
    @Query(
            value = "Select a.* from(SELECT @rownum /*'*/:=/*'*/@rownum + 1 as RN, t.* FROM ( SELECT distinct a.* from trakeye_trservice a, trakeye_trcase b , trakeye_user c "
            		+ "where (a.tr_case_id=b.id and b.assigned_to_id = c.id and c.login = ?#{principal.username}) or (a.user_id = c.id and c.login=?#{principal.username})) t,(SELECT @rownum /*'*/:=/*'*/ 0) r) a "
            		+ "where ((RN between ?#{ #pageable.offset -1} and ?#{#pageable.offset + #pageable.pageSize}) and (a.description like :searchText% or a.status like :searchText% "
            		+"or a.id like :searchText%)) order by a.id desc",
            countQuery = "Select count(*) from(SELECT @rownum /*'*/:=/*'*/@rownum + 1 as RN, t.* FROM ( SELECT distinct a.* from trakeye_trservice a, trakeye_trcase b , trakeye_user c "
            		+ "where (a.tr_case_id=b.id and b.assigned_to_id = c.id and c.login = ?#{principal.username}) or (a.user_id = c.id and c.login=?#{principal.username})) t,(SELECT @rownum /*'*/:=/*'*/ 0) r) a "
            		+ "where ((RN between ?#{ #pageable.offset -1} and ?#{#pageable.offset + #pageable.pageSize}) and (a.description like :searchText% or a.status like :searchText% "
            		+"or a.id like :searchText%))", nativeQuery = true)
    Page<TrService> findUserServicesBySearch(@Param("searchText")String searchText, Pageable pageable);

    
   
    

}
