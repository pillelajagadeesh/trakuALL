package com.tresbu.trakeye.repository;

import com.tresbu.trakeye.domain.CaseType;
import com.tresbu.trakeye.domain.Geofence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.data.repository.query.Param;
/**
 * Spring Data JPA repository for the CaseType entity.
 */
@SuppressWarnings("unused")
public interface CaseTypeRepository extends JpaRepository<CaseType,Long> {

    @Query("select caseType from CaseType caseType where caseType.user.login = ?#{principal.username}")
    List<CaseType> findByUserIsCurrentUser();
    
    @Query("select caseType from CaseType caseType where caseType.name=:name and caseType.user.login = ?#{principal.username}")
    List<CaseType> findCaseTypeByCase(@Param("name") String name);
    
    @Query(value="select casetype from CaseType casetype where casetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username})"
    		+ "or casetype.user.id = (select id from User user where user.login = (select createdBy from User user where user.login =?#{principal.username}))  order by casetype.id desc",
    		countQuery = "select count(casetype) from CaseType casetype where casetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username})"
    				+ "or casetype.user.id = (select id from User user where user.login = (select createdBy from User user where user.login =?#{principal.username}))")
     Page<CaseType> findCaseTypeForLoggedInUser(Pageable pageable);
    
    @Query(value="select casetype from CaseType casetype where ((casetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}))"
    		+ "or (casetype.user.id = (select id from User user where user.login = (select createdBy from User user where user.login =?#{principal.username}))) and (casetype.name like :searchText% or "
    		+ " casetype.description like :searchText%  or "
    		+ " casetype.id like :searchText%)) order by casetype.id desc",
    		countQuery = "select count(casetype) from CaseType casetype where ((casetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}))"
    				+ "or (casetype.user.id = (select id from User user where user.login = (select createdBy from User user where user.login =?#{principal.username}))) and (casetype.name like :searchText% or "
    		+ " casetype.description like :searchText%  or "
    		+ " casetype.id like :searchText%))")
     Page<CaseType> findCaseTypeForLoggedInUserBySearchValue(@Param("searchText")String searchText,Pageable pageable);

}
