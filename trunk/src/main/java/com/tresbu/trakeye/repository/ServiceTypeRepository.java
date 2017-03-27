package com.tresbu.trakeye.repository;

import com.tresbu.trakeye.domain.ServiceType;
import com.tresbu.trakeye.domain.TrService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the ServiceType entity.
 */
@SuppressWarnings("unused")
public interface ServiceTypeRepository extends JpaRepository<ServiceType,Long> {

    @Query("select serviceType from ServiceType serviceType where serviceType.user.login = ?#{principal.username}")
    List<ServiceType> findByUserIsCurrentUser();
    
    
    @Query(value="select servicetype from ServiceType servicetype where servicetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}) order by servicetype.id desc",
    		countQuery = "select count(servicetype) from ServiceType servicetype where servicetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}) ")
    Page<ServiceType> findServiceTypeForLoggedInUser(Pageable pageable);
    
    
    @Query(value="select servicetype from ServiceType servicetype where ((servicetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username}))"
    		+ "and (servicetype.name like :searchText% or servicetype.description like :searchText% or servicetype.id like :searchText%)) order by servicetype.id desc",
    		countQuery = "select count(servicetype) from ServiceType servicetype where ((servicetype.user.id in (select id from User user where user.createdBy = ?#{principal.username} or user.login = ?#{principal.username})) and "
    				+ "(servicetype.name like :searchText% or servicetype.description like :searchText% or servicetype.id like :searchText%))")
    Page<ServiceType> findServiceTypeForLoggedInUserBySearch(@Param("searchText") String searchText,Pageable pageable);

}
