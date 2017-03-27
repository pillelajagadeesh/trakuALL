package com.tresbu.trakeye.repository;

import com.tresbu.trakeye.domain.Geofence;
import com.tresbu.trakeye.domain.LocationLog;
import com.tresbu.trakeye.domain.TrakeyeTypeAttributeValue;
import com.tresbu.trakeye.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Geofence entity.
 */
@SuppressWarnings("unused")
public interface TrakeyeTypeAttributeValueRepository extends JpaRepository<TrakeyeTypeAttributeValue,Long> {

    
}
