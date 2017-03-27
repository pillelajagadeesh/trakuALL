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
		
			 
			 
			 
	public  void createGeofence(Long userId)  {
		log.debug("REST request to save Geofence : ");
		GeofenceCreateDTO geofenceDTO= new GeofenceCreateDTO();
		//geofenceDTO.setId();
		geofenceDTO.setName("Bangalore");
		geofenceDTO.setCoordinates("[{\"lat\":12.774745014054888,\"lng\":77.7744640270248},{\"lat\":12.71045055421516,\"lng\":77.27183951530606},{\"lat\":13.213648806191749,\"lng\":77.24986685905606},{\"lat\":13.325925219804384,\"lng\":78.22215689811856}]");
		//geofenceDTO.setCreatedDate(Instant.now().toEpochMilli());
		geofenceDTO.setDescription("Default Geofence");
      //  geofenceDTO.setModifiedDate(Instant.now().toEpochMilli());
        geofenceDTO.setUserId(userId);
        geofenceService.save(geofenceDTO);
		
	}
	
	public  void createCaseType(Long userId)
	{
		log.debug("REST request to save CaseType: ");
		CaseTypeCreateDTO casetypeDTO= new CaseTypeCreateDTO();
		//casetypeDTO.setId("1");
		//casetypeDTO.setCreatedDate(Instant.now().toEpochMilli());
		casetypeDTO.setDescription("Default CaseType Data");
		casetypeDTO.setName("Default CaseType");
		//casetypeDTO.setUpdateDate(Instant.now().toEpochMilli());
		casetypeDTO.setUserId(userId);
		Set<CaseTypeAttribute> caseTypeAttributes=new HashSet<>();
		CaseTypeAttribute caseTypeAttribute=new CaseTypeAttribute();
		caseTypeAttribute.setName("Digging near cable route");
		caseTypeAttributes.add(caseTypeAttribute);
		caseTypeAttribute=new CaseTypeAttribute();
		caseTypeAttribute.setName("Trenching work");
		caseTypeAttributes.add(caseTypeAttribute);
		caseTypeAttribute=new CaseTypeAttribute();
		caseTypeAttribute.setName("Water flooding/Pipeline issues");
		caseTypeAttributes.add(caseTypeAttribute);
		caseTypeAttribute=new CaseTypeAttribute();
		caseTypeAttribute.setName("Fire");
		caseTypeAttributes.add(caseTypeAttribute);
		caseTypeAttribute=new CaseTypeAttribute();
		caseTypeAttribute.setName("Road extension on cable route");
		caseTypeAttributes.add(caseTypeAttribute);
		caseTypeAttribute=new CaseTypeAttribute();
		caseTypeAttribute.setName("Other Issues");
		caseTypeAttributes.add(caseTypeAttribute);
		casetypeDTO.setCaseTypeAttribute(caseTypeAttributes);
		caseTypeService.save(casetypeDTO);
		
		
		
		
	}
	
	public  void createTrakeyeType(Long userId)
	{
		log.debug("REST request to save TrakeyeType: ");
		TrakeyeTypeCreateDTO trakeyeTypeDTO= new TrakeyeTypeCreateDTO();
		//trakeyeTypeDTO.setId("");
		//trakeyeTypeDTO.setCreatedDate(Instant.now().toEpochMilli());
		trakeyeTypeDTO.setDescription("Default TrakeyeType Data");
		trakeyeTypeDTO.setName("Default TrakeyeType");
		//trakeyeTypeDTO.setUpdatedDate(Instant.now().toEpochMilli());
		trakeyeTypeDTO.setUserId(userId);
		Set<TrakeyeTypeAttribute> trakeyeTypeAttributes=new HashSet<>();
		TrakeyeTypeAttribute trakeyeTypeAttribute=new TrakeyeTypeAttribute();
		trakeyeTypeAttribute.setName("Vehicle");
		trakeyeTypeAttributes.add(trakeyeTypeAttribute);
		trakeyeTypeAttribute=new TrakeyeTypeAttribute();
		trakeyeTypeAttribute.setName("Mobile");
		trakeyeTypeAttributes.add(trakeyeTypeAttribute);
		trakeyeTypeDTO.setTrakeyeTypeAttribute(trakeyeTypeAttributes);
		trakeyeTypeService.save(trakeyeTypeDTO);
	}
	
		
		public  void createServiceType(Long userId)	
		{
	    log.debug("REST request to save ServiceType: ");
	    ServiceTypeCreateDTO serviceTypeDTO= new ServiceTypeCreateDTO();
	    //serviceTypeDTO.setId("");
	  //  serviceTypeDTO.setCreatedDate(Instant.now().toEpochMilli());
	    serviceTypeDTO.setDescription("Default ServiceType Data");
	    serviceTypeDTO.setName("Default ServiceType");
	  //  serviceTypeDTO.setUpdatedDate(Instant.now().toEpochMilli());
	    serviceTypeDTO.setUserId(userId);
	    Set<ServiceTypeAttribute> serviceTypeAttributes=new HashSet<>();
		ServiceTypeAttribute serviceTypeAttribute=new ServiceTypeAttribute();
		serviceTypeAttribute.setName("New");
		serviceTypeAttributes.add(serviceTypeAttribute);
		serviceTypeAttribute=new ServiceTypeAttribute();
		serviceTypeAttribute.setName("Repair");
		serviceTypeAttributes.add(serviceTypeAttribute);
		serviceTypeAttribute=new ServiceTypeAttribute();
		serviceTypeAttribute.setName("Picture");
		serviceTypeAttributes.add(serviceTypeAttribute);	
		serviceTypeDTO.setServiceTypeAttribute(serviceTypeAttributes);	
	    serviceTypeService.save(serviceTypeDTO);
			
		}
}		
