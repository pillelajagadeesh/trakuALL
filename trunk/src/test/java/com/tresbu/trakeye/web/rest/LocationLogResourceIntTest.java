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
import com.tresbu.trakeye.domain.LocationLog;
import com.tresbu.trakeye.domain.enumeration.LogSource;
import com.tresbu.trakeye.repository.LocationLogRepository;
import com.tresbu.trakeye.service.LocationLogService;
import com.tresbu.trakeye.service.dto.LocationLogDTO;
import com.tresbu.trakeye.service.mapper.LocationLogMapper;
/**
 * Test class for the LocationLogResource REST controller.
 *
 * @see LocationLogResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrakeyeApp.class)
public class LocationLogResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final Double DEFAULT_LATITUDE = 1d;
    private static final Double UPDATED_LATITUDE = 2d;

    private static final Double DEFAULT_LONGITUDE = 1d;
    private static final Double UPDATED_LONGITUDE = 2d;
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";

    private static final LogSource DEFAULT_LOG_SOURCE = LogSource.NP;
    private static final LogSource UPDATED_LOG_SOURCE = LogSource.GPS;

    private static final long DEFAULT_CREATED_DATE_TIME = Instant.now().toEpochMilli();
    private static final long UPDATED_CREATED_DATE_TIME = Instant.now().toEpochMilli();
    private static final String DEFAULT_CREATED_DATE_TIME_STR =Instant.now().toEpochMilli()+"";

    @Inject
    private LocationLogRepository locationLogRepository;

    @Inject
    private LocationLogMapper locationLogMapper;

    @Inject
    private LocationLogService locationLogService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLocationLogMockMvc;

    private LocationLog locationLog;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LocationLogResource locationLogResource = new LocationLogResource();
        ReflectionTestUtils.setField(locationLogResource, "locationLogService", locationLogService);
        this.restLocationLogMockMvc = MockMvcBuilders.standaloneSetup(locationLogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationLog createEntity(EntityManager em) {
        LocationLog locationLog = new LocationLog();
        locationLog = new LocationLog();
        locationLog .setLatitude(DEFAULT_LATITUDE);
        locationLog  .setLongitude(DEFAULT_LONGITUDE);
        locationLog  .address(DEFAULT_ADDRESS);
        locationLog .logSource(DEFAULT_LOG_SOURCE);
        locationLog .setCreatedDateTime(DEFAULT_CREATED_DATE_TIME);
        locationLog .setUpdatedDateTime(DEFAULT_CREATED_DATE_TIME);
        return locationLog;
    }

    @Before
    public void initTest() {
        locationLog = createEntity(em);
    }

    @Test
    @Transactional
    public void createLocationLog() throws Exception {
        int databaseSizeBeforeCreate = locationLogRepository.findAll().size();

        // Create the LocationLog
        LocationLogDTO locationLogDTO = locationLogMapper.locationLogToLocationLogDTO(locationLog);

        restLocationLogMockMvc.perform(post("/api/location-logs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(locationLogDTO)))
                .andExpect(status().isCreated());

        // Validate the LocationLog in the database
        List<LocationLog> locationLogs = locationLogRepository.findAll();
        assertThat(locationLogs).hasSize(databaseSizeBeforeCreate + 1);
        LocationLog testLocationLog = locationLogs.get(locationLogs.size() - 1);
        assertThat(testLocationLog.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testLocationLog.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testLocationLog.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testLocationLog.getLogSource()).isEqualTo(DEFAULT_LOG_SOURCE);
        assertThat(testLocationLog.getCreatedDateTime()).isEqualTo(DEFAULT_CREATED_DATE_TIME);
        assertThat(testLocationLog.getUpdatedDateTime()).isEqualTo(DEFAULT_CREATED_DATE_TIME);
    }

    @Test
    @Transactional
    public void checkLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationLogRepository.findAll().size();
        // set the field null
        locationLog.setLatitude(null);

        // Create the LocationLog, which fails.
        LocationLogDTO locationLogDTO = locationLogMapper.locationLogToLocationLogDTO(locationLog);

        restLocationLogMockMvc.perform(post("/api/location-logs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(locationLogDTO)))
                .andExpect(status().isBadRequest());

        List<LocationLog> locationLogs = locationLogRepository.findAll();
        assertThat(locationLogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationLogRepository.findAll().size();
        // set the field null
        locationLog.setLongitude(null);

        // Create the LocationLog, which fails.
        LocationLogDTO locationLogDTO = locationLogMapper.locationLogToLocationLogDTO(locationLog);

        restLocationLogMockMvc.perform(post("/api/location-logs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(locationLogDTO)))
                .andExpect(status().isBadRequest());

        List<LocationLog> locationLogs = locationLogRepository.findAll();
        assertThat(locationLogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLogSourceIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationLogRepository.findAll().size();
        // set the field null
        locationLog.setLogSource(null);

        // Create the LocationLog, which fails.
        LocationLogDTO locationLogDTO = locationLogMapper.locationLogToLocationLogDTO(locationLog);

        restLocationLogMockMvc.perform(post("/api/location-logs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(locationLogDTO)))
                .andExpect(status().isBadRequest());

        List<LocationLog> locationLogs = locationLogRepository.findAll();
        assertThat(locationLogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationLogRepository.findAll().size();
        // set the field null
        locationLog.setCreatedDateTime(0);

        // Create the LocationLog, which fails.
        LocationLogDTO locationLogDTO = locationLogMapper.locationLogToLocationLogDTO(locationLog);

        restLocationLogMockMvc.perform(post("/api/location-logs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(locationLogDTO)))
                .andExpect(status().isBadRequest());

        List<LocationLog> locationLogs = locationLogRepository.findAll();
        assertThat(locationLogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLocationLogs() throws Exception {
        // Initialize the database
        locationLogRepository.saveAndFlush(locationLog);

        // Get all the locationLogs
        restLocationLogMockMvc.perform(get("/api/location-logs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(locationLog.getId().intValue())))
                .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].logSource").value(hasItem(DEFAULT_LOG_SOURCE.toString())))
                .andExpect(jsonPath("$.[*].createdDateTime").value(hasItem(DEFAULT_CREATED_DATE_TIME_STR)))
                .andExpect(jsonPath("$.[*].updatedDateTime").value(hasItem(DEFAULT_CREATED_DATE_TIME_STR)));
    }

    @Test
    @Transactional
    public void getLocationLog() throws Exception {
        // Initialize the database
        locationLogRepository.saveAndFlush(locationLog);

        // Get the locationLog
        restLocationLogMockMvc.perform(get("/api/location-logs/{id}", locationLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(locationLog.getId().intValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.logSource").value(DEFAULT_LOG_SOURCE.toString()))
            .andExpect(jsonPath("$.createdDateTime").value(DEFAULT_CREATED_DATE_TIME_STR))
            .andExpect(jsonPath("$.updatedDateTime").value(DEFAULT_CREATED_DATE_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingLocationLog() throws Exception {
        // Get the locationLog
        restLocationLogMockMvc.perform(get("/api/location-logs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocationLog() throws Exception {
        // Initialize the database
        locationLogRepository.saveAndFlush(locationLog);
        int databaseSizeBeforeUpdate = locationLogRepository.findAll().size();

        // Update the locationLog
        LocationLog updatedLocationLog = locationLogRepository.findOne(locationLog.getId());
      
        updatedLocationLog.setLatitude(DEFAULT_LATITUDE);
        updatedLocationLog  .setLongitude(DEFAULT_LONGITUDE);
        updatedLocationLog  .address(DEFAULT_ADDRESS);
        updatedLocationLog .logSource(DEFAULT_LOG_SOURCE);
        LocationLogDTO locationLogDTO = locationLogMapper.locationLogToLocationLogDTO(updatedLocationLog);

        restLocationLogMockMvc.perform(put("/api/location-logs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(locationLogDTO)))
                .andExpect(status().isOk());

        // Validate the LocationLog in the database
        List<LocationLog> locationLogs = locationLogRepository.findAll();
        assertThat(locationLogs).hasSize(databaseSizeBeforeUpdate);
        LocationLog testLocationLog = locationLogs.get(locationLogs.size() - 1);
        assertThat(testLocationLog.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testLocationLog.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testLocationLog.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testLocationLog.getLogSource()).isEqualTo(UPDATED_LOG_SOURCE);
        assertThat(testLocationLog.getCreatedDateTime()).isEqualTo(UPDATED_CREATED_DATE_TIME);
        assertThat(testLocationLog.getUpdatedDateTime()).isEqualTo(UPDATED_CREATED_DATE_TIME);
    }

    @Test
    @Transactional
    public void deleteLocationLog() throws Exception {
        // Initialize the database
        locationLogRepository.saveAndFlush(locationLog);
        int databaseSizeBeforeDelete = locationLogRepository.findAll().size();

        // Get the locationLog
        restLocationLogMockMvc.perform(delete("/api/location-logs/{id}", locationLog.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<LocationLog> locationLogs = locationLogRepository.findAll();
        assertThat(locationLogs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
