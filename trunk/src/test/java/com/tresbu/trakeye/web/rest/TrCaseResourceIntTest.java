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
import com.tresbu.trakeye.domain.TrCase;
import com.tresbu.trakeye.domain.enumeration.CaseStatus;
import com.tresbu.trakeye.repository.TrCaseRepository;
import com.tresbu.trakeye.service.TrCaseService;
import com.tresbu.trakeye.service.dto.TrCaseDTO;
import com.tresbu.trakeye.service.mapper.TrCaseMapper;
/**
 * Test class for the TrCaseResource REST controller.
 *
 * @see TrCaseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrakeyeApp.class)
public class TrCaseResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final long DEFAULT_CREATE_DATE = Instant.now().toEpochMilli();
    private static final long UPDATED_CREATE_DATE =Instant.now().toEpochMilli();
    private static final String DEFAULT_CREATE_DATE_STR = Instant.now().toEpochMilli()+"";

    private static final long DEFAULT_UPDATE_DATE =Instant.now().toEpochMilli();
    private static final long UPDATED_UPDATE_DATE = Instant.now().toEpochMilli();
    private static final String DEFAULT_UPDATE_DATE_STR =Instant.now().toEpochMilli()+"";

    private static final Double DEFAULT_PIN_LAT = 1d;
    private static final Double UPDATED_PIN_LAT = 2d;

    private static final Double DEFAULT_PIN_LONG = 1d;
    private static final Double UPDATED_PIN_LONG = 2d;
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";

    private static final Boolean DEFAULT_ESCALATED = false;
    private static final Boolean UPDATED_ESCALATED = true;

    private static final CaseStatus DEFAULT_STATUS = CaseStatus.NEW;
    private static final CaseStatus UPDATED_STATUS = CaseStatus.INPROGRESS;

    @Inject
    private TrCaseRepository trCaseRepository;

    @Inject
    private TrCaseMapper trCaseMapper;

    @Inject
    private TrCaseService trCaseService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTrCaseMockMvc;

    private TrCase trCase;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TrCaseResource trCaseResource = new TrCaseResource();
        ReflectionTestUtils.setField(trCaseResource, "trCaseService", trCaseService);
        this.restTrCaseMockMvc = MockMvcBuilders.standaloneSetup(trCaseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrCase createEntity(EntityManager em) {
        TrCase trCase = new TrCase();
        trCase = new TrCase();
        trCase.setDescription(DEFAULT_DESCRIPTION);
        trCase.setCreateDate(DEFAULT_CREATE_DATE);
        trCase.setUpdateDate(DEFAULT_UPDATE_DATE);
        trCase.setPinLat(DEFAULT_PIN_LAT);;
        trCase.setPinLong(DEFAULT_PIN_LONG);
        trCase.setAddress(DEFAULT_ADDRESS);
        trCase.setEscalated(DEFAULT_ESCALATED);
        trCase.setStatus(DEFAULT_STATUS);
               
        return trCase;
    }

    @Before
    public void initTest() {
        trCase = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrCase() throws Exception {
        int databaseSizeBeforeCreate = trCaseRepository.findAll().size();

        // Create the TrCase
        TrCaseDTO trCaseDTO = trCaseMapper.trCaseToTrCaseDTO(trCase);

        restTrCaseMockMvc.perform(post("/api/tr-cases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trCaseDTO)))
                .andExpect(status().isCreated());

        // Validate the TrCase in the database
        List<TrCase> trCases = trCaseRepository.findAll();
        assertThat(trCases).hasSize(databaseSizeBeforeCreate + 1);
        TrCase testTrCase = trCases.get(trCases.size() - 1);
        assertThat(testTrCase.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTrCase.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testTrCase.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testTrCase.getPinLat()).isEqualTo(DEFAULT_PIN_LAT);
        assertThat(testTrCase.getPinLong()).isEqualTo(DEFAULT_PIN_LONG);
        assertThat(testTrCase.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testTrCase.getEscalated()).isEqualTo(DEFAULT_ESCALATED);
        assertThat(testTrCase.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = trCaseRepository.findAll().size();
        // set the field null
        trCase.setDescription(null);

        // Create the TrCase, which fails.
        TrCaseDTO trCaseDTO = trCaseMapper.trCaseToTrCaseDTO(trCase);

        restTrCaseMockMvc.perform(post("/api/tr-cases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trCaseDTO)))
                .andExpect(status().isBadRequest());

        List<TrCase> trCases = trCaseRepository.findAll();
        assertThat(trCases).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = trCaseRepository.findAll().size();
        // set the field null
        trCase.setCreateDate(0);

        // Create the TrCase, which fails.
        TrCaseDTO trCaseDTO = trCaseMapper.trCaseToTrCaseDTO(trCase);

        restTrCaseMockMvc.perform(post("/api/tr-cases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trCaseDTO)))
                .andExpect(status().isBadRequest());

        List<TrCase> trCases = trCaseRepository.findAll();
        assertThat(trCases).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = trCaseRepository.findAll().size();
        // set the field null
        trCase.setUpdateDate(0);

        // Create the TrCase, which fails.
        TrCaseDTO trCaseDTO = trCaseMapper.trCaseToTrCaseDTO(trCase);

        restTrCaseMockMvc.perform(post("/api/tr-cases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trCaseDTO)))
                .andExpect(status().isBadRequest());

        List<TrCase> trCases = trCaseRepository.findAll();
        assertThat(trCases).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = trCaseRepository.findAll().size();
        // set the field null
        trCase.setStatus(null);

        // Create the TrCase, which fails.
        TrCaseDTO trCaseDTO = trCaseMapper.trCaseToTrCaseDTO(trCase);

        restTrCaseMockMvc.perform(post("/api/tr-cases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trCaseDTO)))
                .andExpect(status().isBadRequest());

        List<TrCase> trCases = trCaseRepository.findAll();
        assertThat(trCases).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrCases() throws Exception {
        // Initialize the database
        trCaseRepository.saveAndFlush(trCase);

        // Get all the trCases
        restTrCaseMockMvc.perform(get("/api/tr-cases?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(trCase.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
                .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE_STR)))
                .andExpect(jsonPath("$.[*].pinLat").value(hasItem(DEFAULT_PIN_LAT.doubleValue())))
                .andExpect(jsonPath("$.[*].pinLong").value(hasItem(DEFAULT_PIN_LONG.doubleValue())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].escalated").value(hasItem(DEFAULT_ESCALATED.booleanValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getTrCase() throws Exception {
        // Initialize the database
        trCaseRepository.saveAndFlush(trCase);

        // Get the trCase
        restTrCaseMockMvc.perform(get("/api/tr-cases/{id}", trCase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trCase.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE_STR))
            .andExpect(jsonPath("$.pinLat").value(DEFAULT_PIN_LAT.doubleValue()))
            .andExpect(jsonPath("$.pinLong").value(DEFAULT_PIN_LONG.doubleValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.escalated").value(DEFAULT_ESCALATED.booleanValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTrCase() throws Exception {
        // Get the trCase
        restTrCaseMockMvc.perform(get("/api/tr-cases/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrCase() throws Exception {
        // Initialize the database
        trCaseRepository.saveAndFlush(trCase);
        int databaseSizeBeforeUpdate = trCaseRepository.findAll().size();

        // Update the trCase
        TrCase trCase1 = trCaseRepository.findOne(trCase.getId());
        
        trCase1.setDescription(DEFAULT_DESCRIPTION);
        trCase1.setCreateDate(DEFAULT_CREATE_DATE);
        trCase1.setUpdateDate(DEFAULT_UPDATE_DATE);
        trCase1.setPinLat(DEFAULT_PIN_LAT);;
        trCase1.setPinLong(DEFAULT_PIN_LONG);
        trCase1.setAddress(DEFAULT_ADDRESS);
        trCase1.setEscalated(DEFAULT_ESCALATED);
        trCase1.setStatus(DEFAULT_STATUS);
        TrCaseDTO trCaseDTO = trCaseMapper.trCaseToTrCaseDTO(trCase1);

        restTrCaseMockMvc.perform(put("/api/tr-cases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trCaseDTO)))
                .andExpect(status().isOk());

        // Validate the TrCase in the database
        List<TrCase> trCases = trCaseRepository.findAll();
        assertThat(trCases).hasSize(databaseSizeBeforeUpdate);
        TrCase testTrCase = trCases.get(trCases.size() - 1);
        assertThat(testTrCase.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTrCase.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testTrCase.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testTrCase.getPinLat()).isEqualTo(UPDATED_PIN_LAT);
        assertThat(testTrCase.getPinLong()).isEqualTo(UPDATED_PIN_LONG);
        assertThat(testTrCase.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testTrCase.getEscalated()).isEqualTo(UPDATED_ESCALATED);
        assertThat(testTrCase.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteTrCase() throws Exception {
        // Initialize the database
        trCaseRepository.saveAndFlush(trCase);
        int databaseSizeBeforeDelete = trCaseRepository.findAll().size();

        // Get the trCase
        restTrCaseMockMvc.perform(delete("/api/tr-cases/{id}", trCase.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TrCase> trCases = trCaseRepository.findAll();
        assertThat(trCases).hasSize(databaseSizeBeforeDelete - 1);
    }
}
