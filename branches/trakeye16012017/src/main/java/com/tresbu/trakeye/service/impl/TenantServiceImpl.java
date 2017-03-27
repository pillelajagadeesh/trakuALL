package com.tresbu.trakeye.service.impl;

import com.tresbu.trakeye.service.CaseTypeService;
import com.tresbu.trakeye.service.GeofenceService;
import com.tresbu.trakeye.service.ServiceTypeService;
import com.tresbu.trakeye.service.TenantService;
import com.tresbu.trakeye.service.TrakeyeTypeService;
import com.tresbu.trakeye.service.UserService;
import com.tresbu.trakeye.domain.Asset;
import com.tresbu.trakeye.domain.AssetType;
import com.tresbu.trakeye.domain.CaseTypeAttribute;
import com.tresbu.trakeye.domain.ServiceTypeAttribute;
import com.tresbu.trakeye.domain.Tenant;
import com.tresbu.trakeye.domain.TrakeyeTypeAttribute;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.repository.TenantRepository;
import com.tresbu.trakeye.service.dto.AssetTypeDTO;
import com.tresbu.trakeye.service.dto.CaseTypeCreateDTO;
import com.tresbu.trakeye.service.dto.GeofenceCreateDTO;
import com.tresbu.trakeye.service.dto.ServiceTypeCreateDTO;
import com.tresbu.trakeye.service.dto.TenantDTO;
import com.tresbu.trakeye.service.dto.TrakeyeTypeCreateDTO;
import com.tresbu.trakeye.service.mapper.TenantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Tenant.
 */
@Service
@Transactional
public class TenantServiceImpl implements TenantService{

    private final Logger log = LoggerFactory.getLogger(TenantServiceImpl.class);
    
    @Inject
    private TenantRepository tenantRepository;

    @Inject
    private TenantMapper tenantMapper;
    
    @Inject 
    private UserService userService;
    
    
    @Inject
	private  ServiceTypeService serviceTypeService;
	
	@Inject
    private  GeofenceService geofenceService;
	
	@Inject
	private  TrakeyeTypeService trakeyeTypeService;
	 
	@Inject
	private  CaseTypeService caseTypeService;

    /**
     * Save a tenant.
     *
     * @param tenantDTO the entity to save
     * @return the persisted entity
     */
    public TenantDTO save(TenantDTO tenantDTO) {
    	log.debug("Request to save Tenant : {}", tenantDTO);
        Tenant tenant = tenantMapper.tenantDTOToTenant(tenantDTO);
        tenant.setCreatedDate(Instant.now().toEpochMilli());
        tenant = tenantRepository.save(tenant);
      //create tenant as user for login
      User user=  userService.createUser(tenant);
      //create default data  for organization  
        createGeofence(user.getId());
    	createCaseType(user.getId());
    	createTrakeyeType(user.getId());
    	createServiceType(user.getId());
        TenantDTO result = tenantMapper.tenantToTenantDTO(tenant);
        return result;
    }
    public TenantDTO update(TenantDTO tenantDTO) {
    	log.debug("Request to save Tenant : {}", tenantDTO);
    	Tenant tenant = tenantRepository.findOne(tenantDTO.getId());
        Tenant t1= tenantMapper.tenantDTOToTenant(tenantDTO);
        t1.updatedDate(Instant.now().toEpochMilli());
        tenant = tenantRepository.save(t1);
        
       userService.updateTenantUser(tenant);
      TenantDTO result = tenantMapper.tenantToTenantDTO(tenant);
        return result;
    }

    /**
     *  Get all the tenants.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TenantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tenants");
        Page<Tenant> result = tenantRepository.findAll(pageable);
        return result.map(tenant -> tenantMapper.tenantToTenantDTO(tenant));
    }
    
    @Override
    @Transactional(readOnly = true)
	public Page<TenantDTO> findAllBySearch(String searchText,Pageable pageable) {
		log.debug("Request to get all tenants by search value and the search value is {}",searchText);
		Page<Tenant> result = tenantRepository.findTenantsBySearchValue(searchText,pageable);
		return result.map(tenant -> tenantMapper.tenantToTenantDTO(tenant));
	}

    /**
     *  Get one tenant by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TenantDTO findOne(Long id) {
        log.debug("Request to get Tenant : {}", id);
        Tenant tenant = tenantRepository.findOne(id);
        TenantDTO tenantDTO = tenantMapper.tenantToTenantDTO(tenant);
        return tenantDTO;
    }

    /**
     *  Delete the  tenant by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Tenant : {}", id);
        tenantRepository.delete(id);
    }
    
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
