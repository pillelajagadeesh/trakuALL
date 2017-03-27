package com.tresbu.trakeye.repository;


import com.tresbu.trakeye.domain.ServiceType;
import com.tresbu.trakeye.domain.TrService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the ServiceType entity.
 */
@SuppressWarnings("unused")
public interface ServiceTypeRepository extends JpaRepository<ServiceType,Long> {

    @Query("select serviceType from ServiceType serviceType where serviceType.user.login = ?#{principal.username}")
    List<ServiceType> findByUserIsCurrentUser();
    
    
    /*@Query(value="select servicetype from ServiceType servicetype where servicetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}) order by servicetype.id desc",
    		countQuery = "select count(servicetype) from ServiceType servicetype where servicetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}) ")
    
    @Query( value="WITH RECURSIVE usertree  AS (  SELECT ut.*   FROM trakeye_user ut   WHERE (login = ?#{principal.username} and tenant_id = ?#{principal.tenant.id} )   UNION ALL "   
    				+" SELECT c.*   FROM trakeye_user c   JOIN usertree p ON c.created_by = p.login ) "
    				+" select tst.* from usertree ut , trakeye_service_type tst where ut.id = tst.user_id ORDER BY ?#{#pageable}",
    		countQuery="WITH RECURSIVE usertree  AS (  SELECT ut.*   FROM trakeye_user ut   WHERE (login = ?#{principal.username} and tenant_id = ?#{principal.tenant.id} )   UNION ALL "   
    	    		+" SELECT c.*   FROM trakeye_user c   JOIN usertree p ON c.created_by = p.login ) "
    	    		+" select tst.* from usertree ut , trakeye_service_type tst where ut.id = tst.user_id ",
    		nativeQuery=true)*/
    @Query(value="select serviceType.* from trakeye_service_type serviceType where serviceType.tenant_id = ?#{principal.tenant.id} ORDER BY ?#{#pageable}",
    		nativeQuery=true)
    Page<ServiceType> findServiceTypeForLoggedInUser(Pageable pageable);
    
    
   /* @Query(value="select servicetype from ServiceType servicetype where ((servicetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}))"
    		+ "and (servicetype.name like :searchText% or servicetype.description like :searchText% or servicetype.id like :searchText%)) order by servicetype.id desc",
    		countQuery = "select count(servicetype) from ServiceType servicetype where ((servicetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username})) and "
    				+ "(servicetype.name like :searchText% or servicetype.description like :searchText% or servicetype.id like :searchText%))")*/
  /*  @Query( value="WITH RECURSIVE usertree  AS (  SELECT ut.*   FROM trakeye_user ut   WHERE (login = ?#{principal.username} and tenant_id = ?#{principal.tenant.id} )   UNION ALL "   
			+" SELECT c.*   FROM trakeye_user c   JOIN usertree p ON c.created_by = p.login ) "
			+" select tst.* from usertree ut , trakeye_service_type tst where ut.id = tst.user_id and (tst.name like :searchText% or tst.description like :searchText% ) ORDER BY ?#{#pageable}",
	countQuery="WITH RECURSIVE usertree  AS (  SELECT ut.*   FROM trakeye_user ut   WHERE (login = ?#{principal.username} and tenant_id = ?#{principal.tenant.id} )   UNION ALL "   
    		+" SELECT c.*   FROM trakeye_user c   JOIN usertree p ON c.created_by = p.login ) "
    		+" select tst.* from usertree ut , trakeye_service_type tst where ut.id = tst.user_id and (tst.name like :searchText% or tst.description like :searchText% ) ",
    		nativeQuery=true)*/
    
    @Query(value="select serviceType.* from trakeye_service_type serviceType where serviceType.tenant_id = ?#{principal.tenant.id} "
    		+ " and (serviceType.name ilike %:searchText% or serviceType.description ilike %:searchText%  or CAST(serviceType.id AS TEXT) ILIKE %:searchText% ) ORDER BY ?#{#pageable}",
    		nativeQuery=true)
    Page<ServiceType> findServiceTypeForLoggedInUserBySearch(@Param("searchText") String searchText,Pageable pageable);
    
    @Query(value="select servicetype.* from trakeye_service_type servicetype where servicetype.name =:name and servicetype.tenant_id =?#{principal.tenant.id}",
       		nativeQuery=true) 
       Optional<ServiceType> findServiceTypeByName(@Param("name") String name);
    
    @Query(value="select servicetype.* from trakeye_service_type servicetype where servicetype.id =:serviceTypeId and servicetype.tenant_id =?#{principal.tenant.id}",
       		nativeQuery=true) 
      ServiceType findServiceTypeById(@Param("serviceTypeId") Long serviceTypeId);

}
