package com.tresbu.trakeye.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tresbu.trakeye.domain.CaseImage;

/**
 * Spring Data JPA repository for the CaseImage entity.
 */
@SuppressWarnings("unused")
public interface CaseImageRepository extends JpaRepository<CaseImage,Long> {

    
}
