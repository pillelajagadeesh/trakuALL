package com.tresbu.trakeye.web.rest;

import com.tresbu.trakeye.TrakeyeApp;
import com.tresbu.trakeye.domain.TrakeyeType;
import com.tresbu.trakeye.domain.TrakeyeTypeAttribute;
import com.tresbu.trakeye.repository.TrakeyeTypeRepository;
import com.tresbu.trakeye.service.TrakeyeTypeService;
import com.tresbu.trakeye.service.dto.TrakeyeTypeDTO;
import com.tresbu.trakeye.service.mapper.TrakeyeTypeMapper;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TrakeyeTypeResource REST controller.
 *
 * @see TrakeyeTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrakeyeApp.class)
public class TrakeyeTypeResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_ATTRIBUTE_NAME="attribute1";

        
    private static final long DEFAULT_CREATED_DATE = Instant.now().toEpochMilli();
    private static final long UPDATED_CREATED_DATE = Instant.now().toEpochMilli();
    private static final String DEFAULT_CREATED_DATE_STR = DEFAULT_CREATED_DATE+"";

    private static final long DEFAULT_UPDATED_DATE = Instant.now().toEpochMilli();
    private static final long UPDATED_UPDATED_DATE = Instant.now().toEpochMilli();
    private static final String DEFAULT_UPDATED_DATE_STR = DEFAULT_UPDATED_DATE+"";

    @Inject
    private TrakeyeTypeRepository trakeyeTypeRepository;

    @Inject
    private TrakeyeTypeMapper trakeyeTypeMapper;

    @Inject
    private TrakeyeTypeService trakeyeTypeService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTrakeyeTypeMockMvc;

    private TrakeyeType trakeyeType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TrakeyeTypeResource trakeyeTypeResource = new TrakeyeTypeResource();
        ReflectionTestUtils.setField(trakeyeTypeResource, "trakeyeTypeService", trakeyeTypeService);
        this.restTrakeyeTypeMockMvc = MockMvcBuilders.standaloneSetup(trakeyeTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrakeyeType createEntity(EntityManager em) {
    	Set<TrakeyeTypeAttribute> trakeyeTypeAttributes=new HashSet<>();
    	TrakeyeTypeAttribute trakeyeTypeAttributes2=new TrakeyeTypeAttribute();
    	trakeyeTypeAttributes2.setName(DEFAULT_ATTRIBUTE_NAME);
    	trakeyeTypeAttributes.add(trakeyeTypeAttributes2);
    	
        TrakeyeType trakeyeType = new TrakeyeType();
        trakeyeType = new TrakeyeType()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .createdDate(DEFAULT_CREATED_DATE)
                .updatedDate(DEFAULT_UPDATED_DATE).trakeyeTypeAttribute(trakeyeTypeAttributes);
        return trakeyeType;
    }

    @Before
    public void initTest() {
        trakeyeType = createEntity(em);
    }

    @Test
    //@Transactional
    public void createTrakeyeType() throws Exception {
        int databaseSizeBeforeCreate = trakeyeTypeRepository.findAll().size();

        // Create the TrakeyeType
        TrakeyeTypeDTO trakeyeTypeDTO = trakeyeTypeMapper.trakeyeTypeToTrakeyeTypeDTO(trakeyeType);

        restTrakeyeTypeMockMvc.perform(post("/api/trakeye-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trakeyeTypeDTO)));
                //.andExpect(status().isCreated());

        // Validate the TrakeyeType in the database
        List<TrakeyeType> trakeyeTypes = trakeyeTypeRepository.findAll();
        assertThat(trakeyeTypes).hasSize(databaseSizeBeforeCreate + 1);
        TrakeyeType testTrakeyeType = trakeyeTypes.get(trakeyeTypes.size() - 1);
        assertThat(testTrakeyeType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrakeyeType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTrakeyeType.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTrakeyeType.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = trakeyeTypeRepository.findAll().size();
        // set the field null
        trakeyeType.setName(null);

        // Create the TrakeyeType, which fails.
        TrakeyeTypeDTO trakeyeTypeDTO = trakeyeTypeMapper.trakeyeTypeToTrakeyeTypeDTO(trakeyeType);

        restTrakeyeTypeMockMvc.perform(post("/api/trakeye-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trakeyeTypeDTO)))
                .andExpect(status().isBadRequest());

        List<TrakeyeType> trakeyeTypes = trakeyeTypeRepository.findAll();
        assertThat(trakeyeTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrakeyeTypes() throws Exception {
        // Initialize the database
        trakeyeTypeRepository.saveAndFlush(trakeyeType);

        // Get all the trakeyeTypes
        restTrakeyeTypeMockMvc.perform(get("/api/trakeye-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(trakeyeType.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE_STR)));
    }

    @Test
    @Transactional
    public void getTrakeyeType() throws Exception {
        // Initialize the database
        trakeyeTypeRepository.saveAndFlush(trakeyeType);

        // Get the trakeyeType
        restTrakeyeTypeMockMvc.perform(get("/api/trakeye-types/{id}", trakeyeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trakeyeType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingTrakeyeType() throws Exception {
        // Get the trakeyeType
        restTrakeyeTypeMockMvc.perform(get("/api/trakeye-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrakeyeType() throws Exception {
        // Initialize the database
        trakeyeTypeRepository.saveAndFlush(trakeyeType);
        int databaseSizeBeforeUpdate = trakeyeTypeRepository.findAll().size();

        // Update the trakeyeType
        TrakeyeType updatedTrakeyeType = trakeyeTypeRepository.findOne(trakeyeType.getId());
        updatedTrakeyeType
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .createdDate(UPDATED_CREATED_DATE)
                .updatedDate(UPDATED_UPDATED_DATE);
        TrakeyeTypeDTO trakeyeTypeDTO = trakeyeTypeMapper.trakeyeTypeToTrakeyeTypeDTO(updatedTrakeyeType);

        restTrakeyeTypeMockMvc.perform(put("/api/trakeye-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trakeyeTypeDTO)))
                .andExpect(status().isOk());

        // Validate the TrakeyeType in the database
        List<TrakeyeType> trakeyeTypes = trakeyeTypeRepository.findAll();
        assertThat(trakeyeTypes).hasSize(databaseSizeBeforeUpdate);
        TrakeyeType testTrakeyeType = trakeyeTypes.get(trakeyeTypes.size() - 1);
        assertThat(testTrakeyeType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrakeyeType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTrakeyeType.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTrakeyeType.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteTrakeyeType() throws Exception {
        // Initialize the database
        trakeyeTypeRepository.saveAndFlush(trakeyeType);
        int databaseSizeBeforeDelete = trakeyeTypeRepository.findAll().size();

        // Get the trakeyeType
        restTrakeyeTypeMockMvc.perform(delete("/api/trakeye-types/{id}", trakeyeType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TrakeyeType> trakeyeTypes = trakeyeTypeRepository.findAll();
        assertThat(trakeyeTypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
