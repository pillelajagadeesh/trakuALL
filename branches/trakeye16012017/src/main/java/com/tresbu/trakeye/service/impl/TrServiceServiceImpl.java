package com.tresbu.trakeye.service.impl;

import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tresbu.trakeye.domain.ServiceImage;
import com.tresbu.trakeye.domain.TrCase;
import com.tresbu.trakeye.domain.TrNotification;
import com.tresbu.trakeye.domain.TrService;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.domain.enumeration.AlertType;
import com.tresbu.trakeye.domain.enumeration.NotificationStatus;
import com.tresbu.trakeye.domain.enumeration.NotificationType;
import com.tresbu.trakeye.domain.enumeration.ServiceStatus;
import com.tresbu.trakeye.repository.TrCaseRepository;
import com.tresbu.trakeye.repository.TrNotificationRepository;
import com.tresbu.trakeye.repository.TrServiceRepository;
import com.tresbu.trakeye.repository.UserRepository;
import com.tresbu.trakeye.security.SecurityUtils;
import com.tresbu.trakeye.service.FcmService;
import com.tresbu.trakeye.service.MailService;
import com.tresbu.trakeye.service.SMSService;
import com.tresbu.trakeye.service.TrServiceService;
import com.tresbu.trakeye.service.dto.TrCaseDTO;
import com.tresbu.trakeye.service.dto.TrNotificationDTO;
import com.tresbu.trakeye.service.dto.TrServiceCreateDTO;
import com.tresbu.trakeye.service.dto.TrServiceDTO;
import com.tresbu.trakeye.service.dto.TrServiceUpdateDTO;
import com.tresbu.trakeye.service.dto.UserUIDTO;
import com.tresbu.trakeye.service.mapper.ServiceImageMapper;
import com.tresbu.trakeye.service.mapper.TrNotificationMapper;
import com.tresbu.trakeye.service.mapper.TrServiceMapper;
import com.tresbu.trakeye.service.mapper.UserMapper;

/**
 * Service Implementation for managing TrService.
 */
@Service
@Transactional
public class TrServiceServiceImpl implements TrServiceService {

	private final Logger log = LoggerFactory.getLogger(TrServiceServiceImpl.class);
	
	@Autowired
	MessageSource messageSource;

	@Inject
	private TrServiceRepository trServiceRepository;

	@Inject
	private TrServiceMapper trServiceMapper;
	
	@Inject
	private ServiceImageMapper serviceImageMapper;

	@Inject
	private TrNotificationRepository trNotificationRepository;

	@Inject
	private MailService mailService;
	
	@Inject
	private SMSService smsService;
	
	@Inject
	private FcmService fcmService;

	@Inject
	private UserRepository userRepository;
	
	@Inject
	private TrNotificationMapper trNotificationMapper;
	
	@Inject
	private UserMapper userMapper;

	@Inject
	private TrCaseRepository trCaseRepository;

	/**
	 * Save a trService.
	 *
	 * @param trServiceDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	public TrServiceDTO save(TrServiceCreateDTO trServiceCreateDTO) {
		log.debug("Request to save TrService : {}", trServiceCreateDTO);
		TrService trService = trServiceMapper.trServiceCreateDTOToTrService(trServiceCreateDTO);
		trService.setCreatedDate(Instant.now().toEpochMilli());
		trService.setModifiedDate(Instant.now().toEpochMilli());
		trService.setStatus(ServiceStatus.INPROGRESS);
		trService.setTenant(SecurityUtils.getCurrentUserTenant());
		trService = trServiceRepository.save(trService);
		
		Set<ServiceImage> serviceImage = trService.getServiceImages();
		for (Iterator<ServiceImage> iterator = serviceImage.iterator(); iterator.hasNext();) {
			ServiceImage caseImage = (ServiceImage) iterator.next();
			caseImage.setTrService(trService);
		}
		TrServiceDTO result = trServiceMapper.trServiceToTrServiceDTO(trService);

		TrCase trCase = trCaseRepository.findOne(result.getTrCase().getId());
		
		User assignToUser = null;
		User assignByUser = null;
		
		User caseCreated = userRepository.findOne(trCase.getReportedBy().getId());
		Optional<User> adminopt = userRepository.findOneByLogin(caseCreated.getCreatedBy());
		User admin = adminopt.get();
		if (trCase.getAssignedTo() != null && trCase.getAssignedBy() != null) {
			assignToUser = userRepository.findOne(trCase.getAssignedTo().getId());
			assignByUser = userRepository.findOne(trCase.getAssignedBy().getId());
		} else {
			assignToUser=admin;
			assignByUser=admin;
		}
		
		TrNotification trNotification = new TrNotification();
		trNotification.setFromUser(assignByUser);
		trNotification.setToUser(assignToUser);
		trNotification.setCreatedDate(Instant.now().toEpochMilli());
		trNotification.setTrCase(trCase);
		trNotification.setStatus(NotificationStatus.SENT);
		trNotification.setNotificationType(NotificationType.S);
		trNotification.setSubject("Case Service Details");
		trNotification.setDescription("Service ticket number is: " + result.getId() + " has been assign to you");
		trNotification.setAlertType(AlertType.EMAIL);
		trNotification.setTenant(assignByUser.getTenant());
		TrNotification notification = trNotificationRepository.save(trNotification);
		TrNotificationDTO trNotificationDTO=trNotificationMapper.trNotificationToTrNotificationDTO(notification);
		
		UserUIDTO assignedByUserDto=userMapper.userToUserUIDTO(assignByUser);
		trNotificationDTO.setFromUser(assignedByUserDto);
		trNotificationDTO.setToUser(userMapper.userToUserUIDTO(assignToUser));
		mailService.sendNotificationMail(trNotificationDTO);
		String message=messageSource.getMessage("sms.serviceimpl.text1", new String[]{""+result.getId()}, new Locale("en", "US"));
		smsService.sendSMS(assignToUser.getPhone(), message);

		String title = messageSource.getMessage("sms.caseimpl.text7", new String[] { "" + result.getId() }, new Locale("en", "US"));
		fcmService.sendAndroidNotification(assignToUser.getFcmToken(), message, title);
		
		trNotification = new TrNotification();
		trNotification.setCreatedDate(Instant.now().toEpochMilli());
		trNotification.setFromUser(assignByUser);
		trNotification.setToUser(caseCreated);
		trNotification.setTrCase(trCase);
		trNotification.setStatus(NotificationStatus.SENT);
		trNotification.setNotificationType(NotificationType.S);
		trNotification.setSubject("Case Service Details");
		trNotification.setDescription("Your case service has been assigned to :" + assignToUser.getFirstName() + ","
				+ assignToUser.getLastName() + ",Service ticket number is: " + result.getId());
		trNotification.setAlertType(AlertType.EMAIL);
		trNotification.setTenant(assignByUser.getTenant());
		TrNotification notification2 = trNotificationRepository.save(trNotification);
		TrNotificationDTO trNotificationDTO2=trNotificationMapper.trNotificationToTrNotificationDTO(notification2);
		trNotificationDTO2.setFromUser(assignedByUserDto);
		trNotificationDTO2.setToUser(userMapper.userToUserUIDTO(caseCreated));
		mailService.sendNotificationMail(trNotificationDTO2);
		message=messageSource.getMessage("sms.serviceimpl.text2", new String[]{""+assignToUser.getFirstName(),""+assignToUser.getLastName(),""+result.getId()}, new Locale("en", "US"));
		smsService.sendSMS(caseCreated.getPhone(), message);

		title = messageSource.getMessage("sms.caseimpl.text7", new String[] { "" + result.getId() }, new Locale("en", "US"));
		fcmService.sendAndroidNotification(caseCreated.getFcmToken(), message, title);
		
		trNotification = new TrNotification();
		trNotification.setCreatedDate(Instant.now().toEpochMilli());
		trNotification.setFromUser(assignByUser);
		trNotification.setToUser(admin);
		trNotification.setTrCase(trCase);
		trNotification.setStatus(NotificationStatus.SENT);
		trNotification.setNotificationType(NotificationType.S);
		trNotification.setSubject("Case Service Details");
		trNotification.setDescription("Service assigned details " + assignToUser.getFirstName() + ","
				+ assignToUser.getLastName() + ",Service ticket number is: " + result.getId());
		trNotification.setAlertType(AlertType.EMAIL);
		trNotification.setTenant(assignByUser.getTenant());
		TrNotification notification3 = trNotificationRepository.save(trNotification);
		TrNotificationDTO trNotificationDTO3=trNotificationMapper.trNotificationToTrNotificationDTO(notification3);
		trNotificationDTO3.setFromUser(assignedByUserDto);
		trNotificationDTO3.setToUser(userMapper.userToUserUIDTO(admin));
		mailService.sendNotificationMail(trNotificationDTO3);
		message=messageSource.getMessage("sms.serviceimpl.text3", new String[]{""+assignToUser.getFirstName(),""+assignToUser.getLastName(),""+result.getId()}, new Locale("en", "US"));
		smsService.sendSMS(admin.getPhone(), message);

		title = messageSource.getMessage("sms.caseimpl.text7", new String[] { "" + result.getId() }, new Locale("en", "US"));
		fcmService.sendAndroidNotification(admin.getFcmToken(), message, title);
		
		return result;
	}

	/**
	 * Get all the trServices.
	 * 
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<TrServiceDTO> findAll(Pageable pageable) {
		log.debug("Request to get all TrServices");
		Page<TrService> result = trServiceRepository.findUserServices(pageable);
		return result.map(trService -> trServiceMapper.trServiceToTrServiceDTO(trService));
	}
	
	
	@Transactional(readOnly = true)
	public Page<TrServiceDTO> findAllBySearch(String searchText, Pageable pageable) {
		log.debug("Request to get all TrServices based on search value {}", searchText);
		Page<TrService> result = trServiceRepository.findUserServicesBySearch(searchText,pageable);
		return result.map(trService -> trServiceMapper.trServiceToTrServiceDTO(trService));
	}

	/**
	 * Get one trService by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public TrServiceDTO findOne(Long id) {
		log.debug("Request to get TrService : {}", id);
		TrService trService = trServiceRepository.findServiceById(id);
		if(trService == null){
			return null;
		}
		TrServiceDTO trServiceDTO = trServiceMapper.trServiceToTrServiceDTO(trService);
		trServiceDTO.setServiceImages(serviceImageMapper.serviceImagesToServiceImageDTOs( trService.getServiceImages()));
		return trServiceDTO;
	}

	/**
	 * Delete the trService by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete TrService : {}", id);
		trServiceRepository.delete(id);
	}

	@Override
	public TrServiceDTO update(TrServiceUpdateDTO trServiceUpdateDTO) {
		TrService trService=trServiceRepository.findServiceById(trServiceUpdateDTO.getId());
		if(trService== null){
			return null;
		}
		trServiceMapper.updateTrServiceFromTrServiceUpdateDTO(trServiceUpdateDTO, trService);
		trService.setModifiedDate(Instant.now().toEpochMilli());
		trService=trServiceRepository.save(trService);
		Set<ServiceImage> serviceImage = trService.getServiceImages();
		for (Iterator<ServiceImage> iterator = serviceImage.iterator(); iterator.hasNext();) {
			ServiceImage caseImage = (ServiceImage) iterator.next();
			caseImage.setTrService(trService);
		}
		TrServiceDTO result = trServiceMapper.trServiceToTrServiceDTO(trService);
		return result;
	}
	
	@Override
	public List<TrServiceDTO> searchTrSevicesById(String id) {
		log.debug("Request to search service by id {}", id);
		List<TrService> result = trServiceRepository.findServicesById(id);
		return trServiceMapper.trServicesToTrServiceDTOs(result);
		
	}

}
