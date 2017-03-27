package com.tresbu.trakeye.service.impl;

import java.time.Instant;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tresbu.trakeye.domain.CaseType;
import com.tresbu.trakeye.repository.CaseTypeRepository;
import com.tresbu.trakeye.service.CaseTypeService;
import com.tresbu.trakeye.service.dto.CaseTypeCreateDTO;
import com.tresbu.trakeye.service.dto.CaseTypeDTO;
import com.tresbu.trakeye.service.dto.CaseTypeUpdateDTO;
import com.tresbu.trakeye.service.mapper.CaseTypeMapper;

/**
 * Service Implementation for managing CaseType.
 */
@Service
@Transactional
public class CaseTypeServiceImpl implements CaseTypeService {

	private final Logger log = LoggerFactory.getLogger(CaseTypeServiceImpl.class);

	@Inject
	private CaseTypeRepository caseTypeRepository;

	@Inject
	private CaseTypeMapper caseTypeMapper;

	/**
	 * Save a caseType.
	 *
	 * @param caseTypeDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	public CaseTypeDTO save(CaseTypeCreateDTO caseTypeCreateDTO) {
		log.debug("Request to save CaseType : {}", caseTypeCreateDTO);
		CaseType caseType = caseTypeMapper.caseTypeCreateDTOToCaseType(caseTypeCreateDTO);
		caseType.setCreatedDate(Instant.now().toEpochMilli());
		caseType.setUpdateDate(Instant.now().toEpochMilli());
		caseType = caseTypeRepository.save(caseType);
		CaseTypeDTO result = caseTypeMapper.caseTypeToCaseTypeDTO(caseType);
		return result;
	}

	/**
	 * Get all the caseTypes.
	 * 
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<CaseTypeDTO> findAll(Pageable pageable) {
		log.debug("Request to get all CaseTypes");
		Page<CaseType> result = caseTypeRepository.findCaseTypeForLoggedInUser(pageable);
		return result.map(caseType -> caseTypeMapper.caseTypeToCaseTypeDTO(caseType));
	}
	
	@Transactional(readOnly = true)
	public Page<CaseTypeDTO> findAllBySearch(String searchText,Pageable pageable) {
		log.debug("Request to get all CaseTypes by search value and the search value is {}",searchText);
		Page<CaseType> result = caseTypeRepository.findCaseTypeForLoggedInUserBySearchValue(searchText,pageable);
		return result.map(caseType -> caseTypeMapper.caseTypeToCaseTypeDTO(caseType));
	}

	/**
	 * Get one caseType by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public CaseTypeDTO findOne(Long id) {
		log.debug("Request to get CaseType : {}", id);
		CaseType caseType = caseTypeRepository.findOne(id);
		CaseTypeDTO caseTypeDTO = caseTypeMapper.caseTypeToCaseTypeDTO(caseType);
		return caseTypeDTO;
	}

	/**
	 * Delete the caseType by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete CaseType : {}", id);
		caseTypeRepository.delete(id);
	}

	@Override
	public List<CaseTypeDTO> findCaseTypeDtos(String name) {
		log.debug("Request to get CaseType : {}", name);
		List<CaseType> caseTypes = caseTypeRepository.findCaseTypeByCase(name);
		List<CaseTypeDTO> caseTypeDTOs = caseTypeMapper.caseTypesToCaseTypeDTOs(caseTypes);
		return caseTypeDTOs;
	}

	@Override
	public CaseTypeDTO update(CaseTypeUpdateDTO caseTypeUpdateDTO) {
		CaseType caseType = caseTypeRepository.findOne(caseTypeUpdateDTO.getId());
		caseTypeMapper.updatecaseTypeFromCaseTypeUpdateDTO(caseTypeUpdateDTO, caseType);
		caseType.setUpdateDate(Instant.now().toEpochMilli());
		caseType = caseTypeRepository.save(caseType);
		CaseTypeDTO result = caseTypeMapper.caseTypeToCaseTypeDTO(caseType);
		return result;
	}
}
