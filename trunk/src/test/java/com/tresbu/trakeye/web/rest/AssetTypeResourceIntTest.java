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
import com.tresbu.trakeye.domain.AssetType;
import com.tresbu.trakeye.domain.enumeration.Layout;
import com.tresbu.trakeye.repository.AssetTypeRepository;
import com.tresbu.trakeye.service.AssetTypeService;
import com.tresbu.trakeye.service.dto.AssetTypeDTO;
import com.tresbu.trakeye.service.mapper.AssetTypeMapper;
/**
 * Test class for the AssetTypeResource REST controller.
 *
 * @see AssetTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrakeyeApp.class)
public class AssetTypeResourceIntTest {
    private static final String DEFAULT_NAME = "AAA";
    private static final String UPDATED_NAME = "BBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Layout DEFAULT_LAYOUT = Layout.FIXED;
    private static final Layout UPDATED_LAYOUT = Layout.SPREAD;
    private static final String DEFAULT_COLORCODE = "AAAAA";
    private static final String UPDATED_COLORCODE = "BBBBB";

    private static final Long DEFAULT_CREATE_DATE = 1L;
    private static final Long UPDATED_CREATE_DATE = 2L;

    private static final Long DEFAULT_UPDATE_DATE = 1L;
    private static final Long UPDATED_UPDATE_DATE = 2L;

    @Inject
    private AssetTypeRepository assetTypeRepository;

    @Inject
    private AssetTypeMapper assetTypeMapper;

    @Inject
    private AssetTypeService assetTypeService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAssetTypeMockMvc;

    private AssetType assetType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AssetTypeResource assetTypeResource = new AssetTypeResource();
        ReflectionTestUtils.setField(assetTypeResource, "assetTypeService", assetTypeService);
        this.restAssetTypeMockMvc = MockMvcBuilders.standaloneSetup(assetTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetType createEntity(EntityManager em) {
        AssetType assetType = new AssetType();
        assetType = new AssetType()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .layout(DEFAULT_LAYOUT)
                .colorcode(DEFAULT_COLORCODE)
                .createDate(DEFAULT_CREATE_DATE)
                .updateDate(DEFAULT_UPDATE_DATE);
        return assetType;
    }

    @Before
    public void initTest() {
        assetType = createEntity(em);
    }

    @Test
    @Transactional
    public void createAssetType() throws Exception {
        int databaseSizeBeforeCreate = assetTypeRepository.findAll().size();

        // Create the AssetType
        AssetTypeDTO assetTypeDTO = assetTypeMapper.assetTypeToAssetTypeDTO(assetType);

        restAssetTypeMockMvc.perform(post("/api/asset-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetTypeDTO)))
                .andExpect(status().isCreated());

        // Validate the AssetType in the database
        List<AssetType> assetTypes = assetTypeRepository.findAll();
        assertThat(assetTypes).hasSize(databaseSizeBeforeCreate + 1);
        AssetType testAssetType = assetTypes.get(assetTypes.size() - 1);
        assertThat(testAssetType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAssetType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssetType.getLayout()).isEqualTo(DEFAULT_LAYOUT);
        assertThat(testAssetType.getColorcode()).isEqualTo(DEFAULT_COLORCODE);
        assertThat(testAssetType.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testAssetType.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetTypeRepository.findAll().size();
        // set the field null
        assetType.setName(null);

        // Create the AssetType, which fails.
        AssetTypeDTO assetTypeDTO = assetTypeMapper.assetTypeToAssetTypeDTO(assetType);

        restAssetTypeMockMvc.perform(post("/api/asset-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetTypeDTO)))
                .andExpect(status().isBadRequest());

        List<AssetType> assetTypes = assetTypeRepository.findAll();
        assertThat(assetTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAssetTypes() throws Exception {
        // Initialize the database
        assetTypeRepository.saveAndFlush(assetType);

        // Get all the assetTypes
        restAssetTypeMockMvc.perform(get("/api/asset-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(assetType.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].layout").value(hasItem(DEFAULT_LAYOUT.toString())))
                .andExpect(jsonPath("$.[*].colorcode").value(hasItem(DEFAULT_COLORCODE.toString())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.intValue())))
                .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.intValue())));
    }

    @Test
    @Transactional
    public void getAssetType() throws Exception {
        // Initialize the database
        assetTypeRepository.saveAndFlush(assetType);

        // Get the assetType
        restAssetTypeMockMvc.perform(get("/api/asset-types/{id}", assetType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(assetType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.layout").value(DEFAULT_LAYOUT.toString()))
            .andExpect(jsonPath("$.colorcode").value(DEFAULT_COLORCODE.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.intValue()))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAssetType() throws Exception {
        // Get the assetType
        restAssetTypeMockMvc.perform(get("/api/asset-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssetType() throws Exception {
        // Initialize the database
        assetTypeRepository.saveAndFlush(assetType);
        int databaseSizeBeforeUpdate = assetTypeRepository.findAll().size();

        // Update the assetType
        AssetType updatedAssetType = assetTypeRepository.findOne(assetType.getId());
        updatedAssetType
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .layout(UPDATED_LAYOUT)
                .colorcode(UPDATED_COLORCODE)
                .createDate(UPDATED_CREATE_DATE)
                .updateDate(UPDATED_UPDATE_DATE);
        AssetTypeDTO assetTypeDTO = assetTypeMapper.assetTypeToAssetTypeDTO(updatedAssetType);

        restAssetTypeMockMvc.perform(put("/api/asset-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetTypeDTO)))
                .andExpect(status().isOk());

        // Validate the AssetType in the database
        List<AssetType> assetTypes = assetTypeRepository.findAll();
        assertThat(assetTypes).hasSize(databaseSizeBeforeUpdate);
        AssetType testAssetType = assetTypes.get(assetTypes.size() - 1);
        assertThat(testAssetType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAssetType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssetType.getLayout()).isEqualTo(UPDATED_LAYOUT);
        assertThat(testAssetType.getColorcode()).isEqualTo(UPDATED_COLORCODE);
        assertThat(testAssetType.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testAssetType.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void deleteAssetType() throws Exception {
        // Initialize the database
        assetTypeRepository.saveAndFlush(assetType);
        int databaseSizeBeforeDelete = assetTypeRepository.findAll().size();

        // Get the assetType
        restAssetTypeMockMvc.perform(delete("/api/asset-types/{id}", assetType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AssetType> assetTypes = assetTypeRepository.findAll();
        assertThat(assetTypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
