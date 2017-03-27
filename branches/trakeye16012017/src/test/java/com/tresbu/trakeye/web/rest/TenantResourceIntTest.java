package com.tresbu.trakeye.web.rest;

import com.tresbu.trakeye.TrakeyeApp;
import com.tresbu.trakeye.domain.Tenant;
import com.tresbu.trakeye.repository.TenantRepository;
import com.tresbu.trakeye.service.TenantService;
import com.tresbu.trakeye.service.dto.TenantDTO;
import com.tresbu.trakeye.service.mapper.TenantMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TenantResource REST controller.
 *
 * @see TenantResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrakeyeApp.class)
public class TenantResourceIntTest {
    private static final String DEFAULT_ORGANIZATION = "AAA";
    private static final String UPDATED_ORGANIZATION = "BBB";
    private static final String DEFAULT_LOGIN_NAME = "AAA";
    private static final String UPDATED_LOGIN_NAME = "BBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_PHONE = "AAAAA";
    private static final String UPDATED_PHONE = "BBBBB";
    private static final String DEFAULT_CITY = "AAAAA";
    private static final String UPDATED_CITY = "BBBBB";
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";

    private static final Long DEFAULT_CREATED_DATE = 1L;
    private static final Long UPDATED_CREATED_DATE = 2L;

    private static final Long DEFAULT_UPDATED_DATE = 1L;
    private static final Long UPDATED_UPDATED_DATE = 2L;

    @Inject
    private TenantRepository tenantRepository;

    @Inject
    private TenantMapper tenantMapper;

    @Inject
    private TenantService tenantService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTenantMockMvc;

    private Tenant tenant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TenantResource tenantResource = new TenantResource();
        ReflectionTestUtils.setField(tenantResource, "tenantService", tenantService);
        this.restTenantMockMvc = MockMvcBuilders.standaloneSetup(tenantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tenant createEntity(EntityManager em) {
        Tenant tenant = new Tenant();
        tenant = new Tenant()
                .organization(DEFAULT_ORGANIZATION)
                .loginName(DEFAULT_LOGIN_NAME)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .city(DEFAULT_CITY)
                .address(DEFAULT_ADDRESS)
                .createdDate(DEFAULT_CREATED_DATE)
                .updatedDate(DEFAULT_UPDATED_DATE);
        return tenant;
    }

    @Before
    public void initTest() {
        tenant = createEntity(em);
    }

    @Test
    @Transactional
    public void createTenant() throws Exception {
        int databaseSizeBeforeCreate = tenantRepository.findAll().size();

        // Create the Tenant
        TenantDTO tenantDTO = tenantMapper.tenantToTenantDTO(tenant);

        restTenantMockMvc.perform(post("/api/tenants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
                .andExpect(status().isCreated());

        // Validate the Tenant in the database
        List<Tenant> tenants = tenantRepository.findAll();
        assertThat(tenants).hasSize(databaseSizeBeforeCreate + 1);
        Tenant testTenant = tenants.get(tenants.size() - 1);
        assertThat(testTenant.getOrganization()).isEqualTo(DEFAULT_ORGANIZATION);
        assertThat(testTenant.getLoginName()).isEqualTo(DEFAULT_LOGIN_NAME);
        assertThat(testTenant.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTenant.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testTenant.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testTenant.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testTenant.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTenant.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void checkOrganizationIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantRepository.findAll().size();
        // set the field null
        tenant.setOrganization(null);

        // Create the Tenant, which fails.
        TenantDTO tenantDTO = tenantMapper.tenantToTenantDTO(tenant);

        restTenantMockMvc.perform(post("/api/tenants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
                .andExpect(status().isBadRequest());

        List<Tenant> tenants = tenantRepository.findAll();
        assertThat(tenants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLoginNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantRepository.findAll().size();
        // set the field null
        tenant.setLoginName(null);

        // Create the Tenant, which fails.
        TenantDTO tenantDTO = tenantMapper.tenantToTenantDTO(tenant);

        restTenantMockMvc.perform(post("/api/tenants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
                .andExpect(status().isBadRequest());

        List<Tenant> tenants = tenantRepository.findAll();
        assertThat(tenants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTenants() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenants
        restTenantMockMvc.perform(get("/api/tenants?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tenant.getId().intValue())))
                .andExpect(jsonPath("$.[*].organization").value(hasItem(DEFAULT_ORGANIZATION.toString())))
                .andExpect(jsonPath("$.[*].loginName").value(hasItem(DEFAULT_LOGIN_NAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.intValue())))
                .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.intValue())));
    }

    @Test
    @Transactional
    public void getTenant() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get the tenant
        restTenantMockMvc.perform(get("/api/tenants/{id}", tenant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tenant.getId().intValue()))
            .andExpect(jsonPath("$.organization").value(DEFAULT_ORGANIZATION.toString()))
            .andExpect(jsonPath("$.loginName").value(DEFAULT_LOGIN_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.intValue()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTenant() throws Exception {
        // Get the tenant
        restTenantMockMvc.perform(get("/api/tenants/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTenant() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);
        int databaseSizeBeforeUpdate = tenantRepository.findAll().size();

        // Update the tenant
        Tenant updatedTenant = tenantRepository.findOne(tenant.getId());
        updatedTenant
                .organization(UPDATED_ORGANIZATION)
                .loginName(UPDATED_LOGIN_NAME)
                .email(UPDATED_EMAIL)
                .phone(UPDATED_PHONE)
                .city(UPDATED_CITY)
                .address(UPDATED_ADDRESS)
                .createdDate(UPDATED_CREATED_DATE)
                .updatedDate(UPDATED_UPDATED_DATE);
        TenantDTO tenantDTO = tenantMapper.tenantToTenantDTO(updatedTenant);

        restTenantMockMvc.perform(put("/api/tenants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
                .andExpect(status().isOk());

        // Validate the Tenant in the database
        List<Tenant> tenants = tenantRepository.findAll();
        assertThat(tenants).hasSize(databaseSizeBeforeUpdate);
        Tenant testTenant = tenants.get(tenants.size() - 1);
        assertThat(testTenant.getOrganization()).isEqualTo(UPDATED_ORGANIZATION);
        assertThat(testTenant.getLoginName()).isEqualTo(UPDATED_LOGIN_NAME);
        assertThat(testTenant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTenant.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testTenant.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testTenant.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testTenant.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTenant.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteTenant() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);
        int databaseSizeBeforeDelete = tenantRepository.findAll().size();

        // Get the tenant
        restTenantMockMvc.perform(delete("/api/tenants/{id}", tenant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Tenant> tenants = tenantRepository.findAll();
        assertThat(tenants).hasSize(databaseSizeBeforeDelete - 1);
    }
}
