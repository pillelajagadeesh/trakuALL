package com.tresbu.trakeye.web.rest;

import com.tresbu.trakeye.TrakeyeApp;
import com.tresbu.trakeye.domain.TrNotification;
import com.tresbu.trakeye.repository.TrNotificationRepository;
import com.tresbu.trakeye.service.TrNotificationService;
import com.tresbu.trakeye.service.dto.TrNotificationDTO;
import com.tresbu.trakeye.service.mapper.TrNotificationMapper;

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

import com.tresbu.trakeye.domain.enumeration.NotificationStatus;
import com.tresbu.trakeye.domain.enumeration.AlertType;
/**
 * Test class for the TrNotificationResource REST controller.
 *
 * @see TrNotificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrakeyeApp.class)
public class TrNotificationResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    
    private static final long DEFAULT_CREATED_DATE = Instant.now().toEpochMilli();
    private static final long UPDATED_CREATED_DATE = Instant.now().toEpochMilli();
    private static final String DEFAULT_CREATED_DATE_STR = DEFAULT_CREATED_DATE+"";

    private static final long DEFAULT_UPDATED_DATE = Instant.now().toEpochMilli();
    private static final long UPDATED_UPDATED_DATE = Instant.now().toEpochMilli();
    private static final String DEFAULT_UPDATED_DATE_STR = DEFAULT_UPDATED_DATE+"";
    
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final NotificationStatus DEFAULT_STATUS = NotificationStatus.SENT;
    private static final NotificationStatus UPDATED_STATUS = NotificationStatus.RECIEVED;
    private static final String DEFAULT_SUBJECT = "AAAAA";
    private static final String UPDATED_SUBJECT = "BBBBB";

    private static final AlertType DEFAULT_ALERT_TYPE = AlertType.EMAIL;
    private static final AlertType UPDATED_ALERT_TYPE = AlertType.SMS;

    @Inject
    private TrNotificationRepository trNotificationRepository;

    @Inject
    private TrNotificationMapper trNotificationMapper;

    @Inject
    private TrNotificationService trNotificationService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTrNotificationMockMvc;

    private TrNotification trNotification;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TrNotificationResource trNotificationResource = new TrNotificationResource();
        ReflectionTestUtils.setField(trNotificationResource, "trNotificationService", trNotificationService);
        this.restTrNotificationMockMvc = MockMvcBuilders.standaloneSetup(trNotificationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrNotification createEntity(EntityManager em) {
        TrNotification trNotification = new TrNotification();
        trNotification = new TrNotification()
                .createdDate(DEFAULT_CREATED_DATE)
                .description(DEFAULT_DESCRIPTION)
                .status(DEFAULT_STATUS)
                .subject(DEFAULT_SUBJECT)
                .alertType(DEFAULT_ALERT_TYPE);
        return trNotification;
    }

    @Before
    public void initTest() {
        trNotification = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrNotification() throws Exception {
        int databaseSizeBeforeCreate = trNotificationRepository.findAll().size();

        // Create the TrNotification
        TrNotificationDTO trNotificationDTO = trNotificationMapper.trNotificationToTrNotificationDTO(trNotification);

        restTrNotificationMockMvc.perform(post("/api/tr-notifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trNotificationDTO)))
                .andExpect(status().isCreated());

        // Validate the TrNotification in the database
        List<TrNotification> trNotifications = trNotificationRepository.findAll();
        assertThat(trNotifications).hasSize(databaseSizeBeforeCreate + 1);
        TrNotification testTrNotification = trNotifications.get(trNotifications.size() - 1);
        assertThat(testTrNotification.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTrNotification.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTrNotification.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTrNotification.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testTrNotification.getAlertType()).isEqualTo(DEFAULT_ALERT_TYPE);
    }

    @Test
    @Transactional
    public void checkSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = trNotificationRepository.findAll().size();
        // set the field null
        trNotification.setSubject(null);

        // Create the TrNotification, which fails.
        TrNotificationDTO trNotificationDTO = trNotificationMapper.trNotificationToTrNotificationDTO(trNotification);

        restTrNotificationMockMvc.perform(post("/api/tr-notifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trNotificationDTO)))
                .andExpect(status().isBadRequest());

        List<TrNotification> trNotifications = trNotificationRepository.findAll();
        assertThat(trNotifications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrNotifications() throws Exception {
        // Initialize the database
        trNotificationRepository.saveAndFlush(trNotification);

        // Get all the trNotifications
        restTrNotificationMockMvc.perform(get("/api/tr-notifications?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(trNotification.getId().intValue())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
                .andExpect(jsonPath("$.[*].alertType").value(hasItem(DEFAULT_ALERT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getTrNotification() throws Exception {
        // Initialize the database
        trNotificationRepository.saveAndFlush(trNotification);

        // Get the trNotification
        restTrNotificationMockMvc.perform(get("/api/tr-notifications/{id}", trNotification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trNotification.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.alertType").value(DEFAULT_ALERT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTrNotification() throws Exception {
        // Get the trNotification
        restTrNotificationMockMvc.perform(get("/api/tr-notifications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrNotification() throws Exception {
        // Initialize the database
        trNotificationRepository.saveAndFlush(trNotification);
        int databaseSizeBeforeUpdate = trNotificationRepository.findAll().size();

        // Update the trNotification
        TrNotification updatedTrNotification = trNotificationRepository.findOne(trNotification.getId());
        updatedTrNotification
                .createdDate(UPDATED_CREATED_DATE)
                .description(UPDATED_DESCRIPTION)
                .status(UPDATED_STATUS)
                .subject(UPDATED_SUBJECT)
                .alertType(UPDATED_ALERT_TYPE);
        TrNotificationDTO trNotificationDTO = trNotificationMapper.trNotificationToTrNotificationDTO(updatedTrNotification);

        restTrNotificationMockMvc.perform(put("/api/tr-notifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trNotificationDTO)))
                .andExpect(status().isOk());

        // Validate the TrNotification in the database
        List<TrNotification> trNotifications = trNotificationRepository.findAll();
        assertThat(trNotifications).hasSize(databaseSizeBeforeUpdate);
        TrNotification testTrNotification = trNotifications.get(trNotifications.size() - 1);
        assertThat(testTrNotification.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTrNotification.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTrNotification.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTrNotification.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testTrNotification.getAlertType()).isEqualTo(UPDATED_ALERT_TYPE);
    }

    @Test
    @Transactional
    public void deleteTrNotification() throws Exception {
        // Initialize the database
        trNotificationRepository.saveAndFlush(trNotification);
        int databaseSizeBeforeDelete = trNotificationRepository.findAll().size();

        // Get the trNotification
        restTrNotificationMockMvc.perform(delete("/api/tr-notifications/{id}", trNotification.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TrNotification> trNotifications = trNotificationRepository.findAll();
        assertThat(trNotifications).hasSize(databaseSizeBeforeDelete - 1);
    }
}
