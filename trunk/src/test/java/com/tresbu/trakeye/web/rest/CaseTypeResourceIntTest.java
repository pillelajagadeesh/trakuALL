package com.tresbu.trakeye.web.rest;

import com.tresbu.trakeye.TrakeyeApp;
import com.tresbu.trakeye.domain.CaseType;
import com.tresbu.trakeye.repository.CaseTypeRepository;
import com.tresbu.trakeye.service.CaseTypeService;
import com.tresbu.trakeye.service.dto.CaseTypeDTO;
import com.tresbu.trakeye.service.mapper.CaseTypeMapper;

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
 * Test class for the CaseTypeResource REST controller.
 *
 * @see CaseTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrakeyeApp.class)
public class CaseTypeResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

   private static final long DEFAULT_CREATED_DATE = Instant.now().toEpochMilli();
    private static final long UPDATED_CREATED_DATE = Instant.now().toEpochMilli();
    private static final String DEFAULT_CREATED_DATE_STR = UPDATED_CREATED_DATE+"";

    private static final long DEFAULT_UPDATE_DATE =Instant.now().toEpochMilli();
    private static final long UPDATED_UPDATE_DATE =Instant.now().toEpochMilli();
    private static final String DEFAULT_UPDATE_DATE_STR = UPDATED_CREATED_DATE+"";
    
    @Inject
    private CaseTypeRepository caseTypeRepository;

    @Inject
    private CaseTypeMapper caseTypeMapper;

    @Inject
    private CaseTypeService caseTypeService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCaseTypeMockMvc;

    private CaseType caseType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CaseTypeResource caseTypeResource = new CaseTypeResource();
        ReflectionTestUtils.setField(caseTypeResource, "caseTypeService", caseTypeService);
        this.restCaseTypeMockMvc = MockMvcBuilders.standaloneSetup(caseTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CaseType createEntity(EntityManager em) {
        CaseType caseType = new CaseType();
        caseType = new CaseType()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .createdDate(DEFAULT_CREATED_DATE)
                .updateDate(DEFAULT_UPDATE_DATE);
        return caseType;
    }

    @Before
    public void initTest() {
        caseType = createEntity(em);
    }

    @Test
    @Transactional
    public void createCaseType() throws Exception {
        int databaseSizeBeforeCreate = caseTypeRepository.findAll().size();

        // Create the CaseType
        CaseTypeDTO caseTypeDTO = caseTypeMapper.caseTypeToCaseTypeDTO(caseType);

        restCaseTypeMockMvc.perform(post("/api/case-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(caseTypeDTO)))
                .andExpect(status().isCreated());

        // Validate the CaseType in the database
        List<CaseType> caseTypes = caseTypeRepository.findAll();
        assertThat(caseTypes).hasSize(databaseSizeBeforeCreate + 1);
        CaseType testCaseType = caseTypes.get(caseTypes.size() - 1);
        assertThat(testCaseType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCaseType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCaseType.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCaseType.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = caseTypeRepository.findAll().size();
        // set the field null
        caseType.setName(null);

        // Create the CaseType, which fails.
        CaseTypeDTO caseTypeDTO = caseTypeMapper.caseTypeToCaseTypeDTO(caseType);

        restCaseTypeMockMvc.perform(post("/api/case-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(caseTypeDTO)))
                .andExpect(status().isBadRequest());

        List<CaseType> caseTypes = caseTypeRepository.findAll();
        assertThat(caseTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCaseTypes() throws Exception {
        // Initialize the database
        caseTypeRepository.saveAndFlush(caseType);

        // Get all the caseTypes
        restCaseTypeMockMvc.perform(get("/api/case-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(caseType.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE_STR)));
    }

    @Test
    @Transactional
    public void getCaseType() throws Exception {
        // Initialize the database
        caseTypeRepository.saveAndFlush(caseType);

        // Get the caseType
        restCaseTypeMockMvc.perform(get("/api/case-types/{id}", caseType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(caseType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingCaseType() throws Exception {
        // Get the caseType
        restCaseTypeMockMvc.perform(get("/api/case-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCaseType() throws Exception {
        // Initialize the database
        caseTypeRepository.saveAndFlush(caseType);
        int databaseSizeBeforeUpdate = caseTypeRepository.findAll().size();

        // Update the caseType
        CaseType updatedCaseType = caseTypeRepository.findOne(caseType.getId());
        updatedCaseType
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .createdDate(UPDATED_CREATED_DATE)
                .updateDate(UPDATED_UPDATE_DATE);
        CaseTypeDTO caseTypeDTO = caseTypeMapper.caseTypeToCaseTypeDTO(updatedCaseType);

        restCaseTypeMockMvc.perform(put("/api/case-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(caseTypeDTO)))
                .andExpect(status().isOk());

        // Validate the CaseType in the database
        List<CaseType> caseTypes = caseTypeRepository.findAll();
        assertThat(caseTypes).hasSize(databaseSizeBeforeUpdate);
        CaseType testCaseType = caseTypes.get(caseTypes.size() - 1);
        assertThat(testCaseType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCaseType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCaseType.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCaseType.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void deleteCaseType() throws Exception {
        // Initialize the database
        caseTypeRepository.saveAndFlush(caseType);
        int databaseSizeBeforeDelete = caseTypeRepository.findAll().size();

        // Get the caseType
        restCaseTypeMockMvc.perform(delete("/api/case-types/{id}", caseType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CaseType> caseTypes = caseTypeRepository.findAll();
        assertThat(caseTypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
