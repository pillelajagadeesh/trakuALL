package com.tresbu.trakeye.web.rest;

import com.tresbu.trakeye.TrakeyeApp;
import com.tresbu.trakeye.domain.TrService;
import com.tresbu.trakeye.repository.TrServiceRepository;
import com.tresbu.trakeye.service.TrServiceService;
import com.tresbu.trakeye.service.dto.TrServiceDTO;
import com.tresbu.trakeye.service.mapper.TrServiceMapper;

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

import com.tresbu.trakeye.domain.enumeration.ServiceStatus;
/**
 * Test class for the TrServiceResource REST controller.
 *
 * @see TrServiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrakeyeApp.class)
public class TrServiceResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final long DEFAULT_CREATED_DATE = Instant.now().toEpochMilli();
    private static final long UPDATED_CREATED_DATE = Instant.now().toEpochMilli();
    private static final String DEFAULT_CREATED_DATE_STR = DEFAULT_CREATED_DATE+"";

    private static final long DEFAULT_MODIFIED_DATE = Instant.now().toEpochMilli();
    private static final long UPDATED_MODIFIED_DATE = Instant.now().toEpochMilli();
    private static final String DEFAULT_MODIFIED_DATE_STR = DEFAULT_MODIFIED_DATE+"";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final long DEFAULT_SERVICE_DATE = Instant.now().toEpochMilli();
    private static final long UPDATED_SERVICE_DATE = Instant.now().toEpochMilli();
    private static final String DEFAULT_SERVICE_DATE_STR = DEFAULT_MODIFIED_DATE+"";

    private static final ServiceStatus DEFAULT_STATUS = ServiceStatus.INPROGRESS;
    private static final ServiceStatus UPDATED_STATUS = ServiceStatus.PENDING;
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";

    @Inject
    private TrServiceRepository trServiceRepository;

    @Inject
    private TrServiceMapper trServiceMapper;

    @Inject
    private TrServiceService trServiceService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTrServiceMockMvc;

    private TrService trService;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TrServiceResource trServiceResource = new TrServiceResource();
        ReflectionTestUtils.setField(trServiceResource, "trServiceService", trServiceService);
        this.restTrServiceMockMvc = MockMvcBuilders.standaloneSetup(trServiceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrService createEntity(EntityManager em) {
        TrService trService = new TrService();
        trService = new TrService()
                .createdDate(DEFAULT_CREATED_DATE)
                .modifiedDate(DEFAULT_MODIFIED_DATE)
                .description(DEFAULT_DESCRIPTION)
                .serviceDate(DEFAULT_SERVICE_DATE)
                .status(DEFAULT_STATUS)
                .notes(DEFAULT_NOTES);
        return trService;
    }

    @Before
    public void initTest() {
        trService = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrService() throws Exception {
        int databaseSizeBeforeCreate = trServiceRepository.findAll().size();

        // Create the TrService
        TrServiceDTO trServiceDTO = trServiceMapper.trServiceToTrServiceDTO(trService);

        restTrServiceMockMvc.perform(post("/api/tr-services")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trServiceDTO)))
                .andExpect(status().isCreated());

        // Validate the TrService in the database
        List<TrService> trServices = trServiceRepository.findAll();
        assertThat(trServices).hasSize(databaseSizeBeforeCreate + 1);
        TrService testTrService = trServices.get(trServices.size() - 1);
        assertThat(testTrService.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTrService.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
        assertThat(testTrService.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTrService.getServiceDate()).isEqualTo(DEFAULT_SERVICE_DATE);
        assertThat(testTrService.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTrService.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void getAllTrServices() throws Exception {
        // Initialize the database
        trServiceRepository.saveAndFlush(trService);

        // Get all the trServices
        restTrServiceMockMvc.perform(get("/api/tr-services?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(trService.getId().intValue())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE_STR)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].serviceDate").value(hasItem(DEFAULT_SERVICE_DATE_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void getTrService() throws Exception {
        // Initialize the database
        trServiceRepository.saveAndFlush(trService);

        // Get the trService
        restTrServiceMockMvc.perform(get("/api/tr-services/{id}", trService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trService.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE_STR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.serviceDate").value(DEFAULT_SERVICE_DATE_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTrService() throws Exception {
        // Get the trService
        restTrServiceMockMvc.perform(get("/api/tr-services/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrService() throws Exception {
        // Initialize the database
        trServiceRepository.saveAndFlush(trService);
        int databaseSizeBeforeUpdate = trServiceRepository.findAll().size();

        // Update the trService
        TrService updatedTrService = trServiceRepository.findOne(trService.getId());
        updatedTrService
                .createdDate(UPDATED_CREATED_DATE)
                .modifiedDate(UPDATED_MODIFIED_DATE)
                .description(UPDATED_DESCRIPTION)
                .serviceDate(UPDATED_SERVICE_DATE)
                .status(UPDATED_STATUS)
                .notes(UPDATED_NOTES);
        TrServiceDTO trServiceDTO = trServiceMapper.trServiceToTrServiceDTO(updatedTrService);

        restTrServiceMockMvc.perform(put("/api/tr-services")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trServiceDTO)))
                .andExpect(status().isOk());

        // Validate the TrService in the database
        List<TrService> trServices = trServiceRepository.findAll();
        assertThat(trServices).hasSize(databaseSizeBeforeUpdate);
        TrService testTrService = trServices.get(trServices.size() - 1);
        assertThat(testTrService.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTrService.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testTrService.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTrService.getServiceDate()).isEqualTo(UPDATED_SERVICE_DATE);
        assertThat(testTrService.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTrService.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void deleteTrService() throws Exception {
        // Initialize the database
        trServiceRepository.saveAndFlush(trService);
        int databaseSizeBeforeDelete = trServiceRepository.findAll().size();

        // Get the trService
        restTrServiceMockMvc.perform(delete("/api/tr-services/{id}", trService.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TrService> trServices = trServiceRepository.findAll();
        assertThat(trServices).hasSize(databaseSizeBeforeDelete - 1);
    }
}
