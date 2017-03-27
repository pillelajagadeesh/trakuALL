package com.tresbu.trakeye.repository;

import com.tresbu.trakeye.domain.Tenant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Tenant entity.
 */
@SuppressWarnings("unused")
public interface TenantRepository extends JpaRepository<Tenant,Long> {
	
	 @Query(value="select tenant.* from trakeye_tenant tenant where "
	    		+ "tenant.login_name ilike %:searchText% or tenant.email ilike %:searchText% or tenant.organization ilike %:searchText% or tenant.phone ilike %:searchText% or tenant.address ilike %:searchText% or tenant.city ilike %:searchText% or CAST(tenant.id AS TEXT) ILIKE %:searchText% ORDER BY ?#{#pageable}",
	    		nativeQuery=true)
	     Page<Tenant> findTenantsBySearchValue(@Param("searchText")String searchText,Pageable pageable);
	

}
