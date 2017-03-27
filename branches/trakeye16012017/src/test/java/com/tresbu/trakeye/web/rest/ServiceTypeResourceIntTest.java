package com.tresbu.trakeye.web.rest;

import com.tresbu.trakeye.TrakeyeApp;
import com.tresbu.trakeye.domain.ServiceType;
import com.tresbu.trakeye.repository.ServiceTypeRepository;
import com.tresbu.trakeye.service.ServiceTypeService;
import com.tresbu.trakeye.service.dto.ServiceTypeDTO;
import com.tresbu.trakeye.service.mapper.ServiceTypeMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ServiceTypeResource REST controller.
 *
 * @see ServiceTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrakeyeApp.class)
public class ServiceTypeResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final long DEFAULT_CREATED_DATE = Instant.now().toEpochMilli();
    private static final long UPDATED_CREATED_DATE = Instant.now().toEpochMilli();
    private static final String DEFAULT_CREATED_DATE_STR = DEFAULT_CREATED_DATE+"";

    private static final long DEFAULT_UPDATED_DATE = Instant.now().toEpochMilli();
    private static final long UPDATED_UPDATED_DATE = Instant.now().toEpochMilli();
    private static final String DEFAULT_UPDATED_DATE_STR = DEFAULT_UPDATED_DATE+"";

    @Inject
    private ServiceTypeRepository serviceTypeRepository;

    @Inject
    private ServiceTypeMapper serviceTypeMapper;

    @Inject
    private ServiceTypeService serviceTypeService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restServiceTypeMockMvc;

    private ServiceType serviceType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ServiceTypeResource serviceTypeResource = new ServiceTypeResource();
        ReflectionTestUtils.setField(serviceTypeResource, "serviceTypeService", serviceTypeService);
        this.restServiceTypeMockMvc = MockMvcBuilders.standaloneSetup(serviceTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceType createEntity(EntityManager em) {
        ServiceType serviceType = new ServiceType();
        serviceType = new ServiceType()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .createdDate(DEFAULT_CREATED_DATE)
                .updatedDate(DEFAULT_UPDATED_DATE);
        return serviceType;
    }

    @Before
    public void initTest() {
        serviceType = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceType() throws Exception {
        int databaseSizeBeforeCreate = serviceTypeRepository.findAll().size();

        // Create the ServiceType
        ServiceTypeDTO serviceTypeDTO = serviceTypeMapper.serviceTypeToServiceTypeDTO(serviceType);

        restServiceTypeMockMvc.perform(post("/api/service-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceTypeDTO)))
                .andExpect(status().isCreated());

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypes = serviceTypeRepository.findAll();
        assertThat(serviceTypes).hasSize(databaseSizeBeforeCreate + 1);
        ServiceType testServiceType = serviceTypes.get(serviceTypes.size() - 1);
        assertThat(testServiceType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testServiceType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testServiceType.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testServiceType.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceTypeRepository.findAll().size();
        // set the field null
        serviceType.setName(null);

        // Create the ServiceType, which fails.
        ServiceTypeDTO serviceTypeDTO = serviceTypeMapper.serviceTypeToServiceTypeDTO(serviceType);

        restServiceTypeMockMvc.perform(post("/api/service-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceTypeDTO)))
                .andExpect(status().isBadRequest());

        List<ServiceType> serviceTypes = serviceTypeRepository.findAll();
        assertThat(serviceTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServiceTypes() throws Exception {
        // Initialize the database
        serviceTypeRepository.saveAndFlush(serviceType);

        // Get all the serviceTypes
        restServiceTypeMockMvc.perform(get("/api/service-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(serviceType.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE_STR)));
    }

    @Test
    @Transactional
    public void getServiceType() throws Exception {
        // Initialize the database
        serviceTypeRepository.saveAndFlush(serviceType);

        // Get the serviceType
        restServiceTypeMockMvc.perform(get("/api/service-types/{id}", serviceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviceType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingServiceType() throws Exception {
        // Get the serviceType
        restServiceTypeMockMvc.perform(get("/api/service-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceType() throws Exception {
        // Initialize the database
        serviceTypeRepository.saveAndFlush(serviceType);
        int databaseSizeBeforeUpdate = serviceTypeRepository.findAll().size();

        // Update the serviceType
        ServiceType updatedServiceType = serviceTypeRepository.findOne(serviceType.getId());
        updatedServiceType
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .createdDate(UPDATED_CREATED_DATE)
                .updatedDate(UPDATED_UPDATED_DATE);
        ServiceTypeDTO serviceTypeDTO = serviceTypeMapper.serviceTypeToServiceTypeDTO(updatedServiceType);

        restServiceTypeMockMvc.perform(put("/api/service-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceTypeDTO)))
                .andExpect(status().isOk());

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypes = serviceTypeRepository.findAll();
        assertThat(serviceTypes).hasSize(databaseSizeBeforeUpdate);
        ServiceType testServiceType = serviceTypes.get(serviceTypes.size() - 1);
        assertThat(testServiceType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testServiceType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testServiceType.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testServiceType.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteServiceType() throws Exception {
        // Initialize the database
        serviceTypeRepository.saveAndFlush(serviceType);
        int databaseSizeBeforeDelete = serviceTypeRepository.findAll().size();

        // Get the serviceType
        restServiceTypeMockMvc.perform(delete("/api/service-types/{id}", serviceType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ServiceType> serviceTypes = serviceTypeRepository.findAll();
        assertThat(serviceTypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
