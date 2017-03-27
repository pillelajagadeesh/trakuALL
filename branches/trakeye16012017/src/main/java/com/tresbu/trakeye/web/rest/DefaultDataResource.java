package com.tresbu.trakeye.web.rest;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tresbu.trakeye.domain.CaseTypeAttribute;
import com.tresbu.trakeye.domain.ServiceTypeAttribute;
import com.tresbu.trakeye.domain.TrakeyeTypeAttribute;
import com.tresbu.trakeye.repository.UserRepository;
import com.tresbu.trakeye.service.CaseTypeService;
import com.tresbu.trakeye.service.GeofenceService;
import com.tresbu.trakeye.service.ServiceTypeService;
import com.tresbu.trakeye.service.TrakeyeTypeService;
import com.tresbu.trakeye.service.UserService;
import com.tresbu.trakeye.service.dto.CaseTypeCreateDTO;
import com.tresbu.trakeye.service.dto.GeofenceCreateDTO;
import com.tresbu.trakeye.service.dto.ServiceTypeCreateDTO;
import com.tresbu.trakeye.service.dto.TrakeyeTypeCreateDTO;

public class DefaultDataResource {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultDataResource.class);


	
	
	
		@Inject
		private  UserRepository userRepository;
		
		@Inject
		private  UserService userService;
		
		@Inject
		private  ServiceTypeService serviceTypeService;
		
		@Inject
	    private  GeofenceService geofenceService;
		
		@Inject
		private  TrakeyeTypeService trakeyeTypeService;
		 
		@Inject
		private  CaseTypeService caseTypeService;
		
			 
			 
			 
	
}		
