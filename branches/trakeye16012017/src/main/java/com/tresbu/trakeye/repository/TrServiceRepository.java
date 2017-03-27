package com.tresbu.trakeye.repository;

import com.tresbu.trakeye.domain.TrCase;
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
    
   
    @Query(value="select trService.* from trakeye_trservice trService, trakeye_trcase trCase, trakeye_user tuser where trCase.id = trService.tr_case_id and tuser.id = trCase.assigned_to_id and  tuser.id in "
    		+ " (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) ORDER BY ?#{#pageable}",
    		nativeQuery=true
    		) 
    Page<TrService> findUserServices(Pageable pageable);
    
    
      
    @Query(value="select trService.* from trakeye_trservice trService, trakeye_trcase trCase, trakeye_user tuser where trCase.id = trService.tr_case_id and tuser.id = trCase.assigned_to_id and  tuser.id in "
    				+ " (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) and (trService.description ilike %:searchText% or trService.status ilike %:searchText% "
            		+" or  CAST(trService.id AS TEXT) ILIKE %:searchText% "
            		+ " or trService.tr_case_id in (select id from trakeye_trcase tc where tc.description ilike %:searchText%) "
            		+ " or trService.tr_case_id in (select id from trakeye_trcase tc where tc.assigned_to_id in "
                    +"(select id from trakeye_user where login ilike %:searchText%))) ORDER BY ?#{#pageable}",
    		nativeQuery=true
    		) 
    Page<TrService> findUserServicesBySearch(@Param("searchText")String searchText, Pageable pageable);
    
    @Query(value="select trService.* from trakeye_trservice trService, trakeye_trcase trCase, trakeye_user tuser where trCase.id = trService.tr_case_id and tuser.id = trCase.assigned_to_id and  tuser.id in "
			+ " (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) and ("
    		+"  CAST(trService.id AS TEXT) ILIKE %:serviceId% )",
	nativeQuery=true) 
     List<TrService> findServicesById(@Param("serviceId") String serviceId);
    
    
    @Query(value="select trService.* from trakeye_trservice trService, trakeye_trcase trCase, trakeye_user tuser where trCase.id = trService.tr_case_id and tuser.id = trCase.assigned_to_id and  tuser.id in "
			+ " (select id from get_allusers(?#{principal.username},?#{principal.tenant.id})) and ("
    		+"  trService.id = :serviceId) and trService.tenant_id = ?#{principal.tenant.id} ",
	nativeQuery=true) 
     TrService findServiceById(@Param("serviceId")Long serviceId);

    
   
    

}
