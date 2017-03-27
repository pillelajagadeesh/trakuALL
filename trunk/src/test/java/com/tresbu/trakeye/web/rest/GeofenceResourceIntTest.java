package com.tresbu.trakeye.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.tresbu.trakeye.TrakeyeApp;
import com.tresbu.trakeye.domain.Geofence;
import com.tresbu.trakeye.repository.GeofenceRepository;
import com.tresbu.trakeye.service.GeofenceService;
import com.tresbu.trakeye.service.dto.GeofenceDTO;
import com.tresbu.trakeye.service.mapper.GeofenceMapper;

/**
 * Test class for the GeofenceResource REST controller.
 *
 * @see GeofenceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrakeyeApp.class)
public class GeofenceResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_COORDINATES = "AAAAA";
    private static final String UPDATED_COORDINATES = "BBBBB";

    private static final long DEFAULT_CREATED_DATE = Instant.now().toEpochMilli();
    private static final long UPDATED_CREATED_DATE = Instant.now().toEpochMilli();
    private static final String DEFAULT_CREATED_DATE_STR =UPDATED_CREATED_DATE+"";

    private static final long DEFAULT_MODIFIED_DATE = Instant.now().toEpochMilli();
    private static final long UPDATED_MODIFIED_DATE = Instant.now().toEpochMilli();
    private static final String DEFAULT_MODIFIED_DATE_STR = DEFAULT_MODIFIED_DATE+"";

    @Inject
    private GeofenceRepository geofenceRepository;

    @Inject
    private GeofenceMapper geofenceMapper;

    @Inject
    private GeofenceService geofenceService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restGeofenceMockMvc;

    private Geofence geofence;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GeofenceResource geofenceResource = new GeofenceResource();
        ReflectionTestUtils.setField(geofenceResource, "geofenceService", geofenceService);
        this.restGeofenceMockMvc = MockMvcBuilders.standaloneSetup(geofenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Geofence createEntity(EntityManager em) {
        Geofence geofence = new Geofence();
        geofence = new Geofence()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .coordinates(DEFAULT_COORDINATES)
                .createdDate(DEFAULT_CREATED_DATE)
                .modifiedDate(DEFAULT_MODIFIED_DATE);
        return geofence;
    }

    @Before
    public void initTest() {
        geofence = createEntity(em);
    }

    @Test
    @Transactional
    public void createGeofence() throws Exception {
        int databaseSizeBeforeCreate = geofenceRepository.findAll().size();

        // Create the Geofence
        GeofenceDTO geofenceDTO = geofenceMapper.geofenceToGeofenceDTO(geofence);

        restGeofenceMockMvc.perform(post("/api/geofences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(geofenceDTO)))
                .andExpect(status().isCreated());

        // Validate the Geofence in the database
        List<Geofence> geofences = geofenceRepository.findAll();
        assertThat(geofences).hasSize(databaseSizeBeforeCreate + 1);
        Geofence testGeofence = geofences.get(geofences.size() - 1);
        assertThat(testGeofence.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGeofence.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testGeofence.getCoordinates()).isEqualTo(DEFAULT_COORDINATES);
        assertThat(testGeofence.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testGeofence.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = geofenceRepository.findAll().size();
        // set the field null
        geofence.setName(null);

        // Create the Geofence, which fails.
        GeofenceDTO geofenceDTO = geofenceMapper.geofenceToGeofenceDTO(geofence);

        restGeofenceMockMvc.perform(post("/api/geofences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(geofenceDTO)))
                .andExpect(status().isBadRequest());

        List<Geofence> geofences = geofenceRepository.findAll();
        assertThat(geofences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCoordinatesIsRequired() throws Exception {
        int databaseSizeBeforeTest = geofenceRepository.findAll().size();
        // set the field null
        geofence.setCoordinates(null);

        // Create the Geofence, which fails.
        GeofenceDTO geofenceDTO = geofenceMapper.geofenceToGeofenceDTO(geofence);

        restGeofenceMockMvc.perform(post("/api/geofences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(geofenceDTO)))
                .andExpect(status().isBadRequest());

        List<Geofence> geofences = geofenceRepository.findAll();
        assertThat(geofences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGeofences() throws Exception {
        // Initialize the database
        geofenceRepository.saveAndFlush(geofence);

        // Get all the geofences
        restGeofenceMockMvc.perform(get("/api/geofences?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(geofence.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].coordinates").value(hasItem(DEFAULT_COORDINATES.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE_STR)));
    }

    @Test
    @Transactional
    public void getGeofence() throws Exception {
        // Initialize the database
        geofenceRepository.saveAndFlush(geofence);

        // Get the geofence
        restGeofenceMockMvc.perform(get("/api/geofences/{id}", geofence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(geofence.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.coordinates").value(DEFAULT_COORDINATES.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingGeofence() throws Exception {
        // Get the geofence
        restGeofenceMockMvc.perform(get("/api/geofences/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGeofence() throws Exception {
        // Initialize the database
        geofenceRepository.saveAndFlush(geofence);
        int databaseSizeBeforeUpdate = geofenceRepository.findAll().size();

        // Update the geofence
        Geofence updatedGeofence = geofenceRepository.findOne(geofence.getId());
        updatedGeofence
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .coordinates(UPDATED_COORDINATES)
                .createdDate(UPDATED_CREATED_DATE)
                .modifiedDate(UPDATED_MODIFIED_DATE);
        GeofenceDTO geofenceDTO = geofenceMapper.geofenceToGeofenceDTO(updatedGeofence);

        restGeofenceMockMvc.perform(put("/api/geofences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(geofenceDTO)))
                .andExpect(status().isOk());

        // Validate the Geofence in the database
        List<Geofence> geofences = geofenceRepository.findAll();
        assertThat(geofences).hasSize(databaseSizeBeforeUpdate);
        Geofence testGeofence = geofences.get(geofences.size() - 1);
        assertThat(testGeofence.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGeofence.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGeofence.getCoordinates()).isEqualTo(UPDATED_COORDINATES);
        assertThat(testGeofence.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testGeofence.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void deleteGeofence() throws Exception {
        // Initialize the database
        geofenceRepository.saveAndFlush(geofence);
        int databaseSizeBeforeDelete = geofenceRepository.findAll().size();

        // Get the geofence
        restGeofenceMockMvc.perform(delete("/api/geofences/{id}", geofence.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Geofence> geofences = geofenceRepository.findAll();
        assertThat(geofences).hasSize(databaseSizeBeforeDelete - 1);
    }
}
