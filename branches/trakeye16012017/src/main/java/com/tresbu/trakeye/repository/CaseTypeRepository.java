package com.tresbu.trakeye.repository;

import com.tresbu.trakeye.domain.CaseType;
import com.tresbu.trakeye.domain.Geofence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
/**
 * Spring Data JPA repository for the CaseType entity.
 */
@SuppressWarnings("unused")
public interface CaseTypeRepository extends JpaRepository<CaseType,Long> {

    @Query("select caseType from CaseType caseType where caseType.user.login = ?#{principal.username}")
    List<CaseType> findByUserIsCurrentUser();
    
    @Query("select caseType from CaseType caseType where caseType.name=:name ")
    List<CaseType> findCaseTypeByCase(@Param("name") String name);
    
   @Query(value="select casetype.* from trakeye_case_type casetype where casetype.tenant_id = ?#{principal.tenant.id} ORDER BY ?#{#pageable}",
    		nativeQuery=true)
     Page<CaseType> findCaseTypeForLoggedInUser(Pageable pageable);
    
   @Query(value="select casetype.* from trakeye_case_type casetype where casetype.tenant_id = ?#{principal.tenant.id} "
    		+ " and (casetype.name ilike %:searchText% or casetype.description ilike %:searchText%  or CAST(casetype.id AS TEXT) ILIKE %:searchText% ) ORDER BY ?#{#pageable}",
    		nativeQuery=true)
    
    Page<CaseType> findCaseTypeForLoggedInUserBySearchValue(@Param("searchText")String searchText,Pageable pageable);
   
   @Query(value="select casetype.* from trakeye_case_type casetype where casetype.name =:name and casetype.tenant_id =?#{principal.tenant.id}",
   		nativeQuery=true) 
   Optional<CaseType> findCaseTypeByName(@Param("name") String name);
   
  /* @Query(value="select casetype.* from trakeye_case_type casetype where casetype.user_id = :createdByAdmin ",
   		nativeQuery=true)
    List<CaseType> findAllCaseTypeCreatedByAgentOrAdmin(@Param("createdByAdmin")long createdByAdmin);*/
   
   @Query(value="select casetype.* from trakeye_case_type casetype where casetype.id =:caseTypeId and casetype.tenant_id =?#{principal.tenant.id}",
	   		nativeQuery=true) 
	   CaseType findCaseTypeById(@Param("caseTypeId") Long caseTypeId);

}
