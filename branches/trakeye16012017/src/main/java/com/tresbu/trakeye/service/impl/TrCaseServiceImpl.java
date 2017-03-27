package com.tresbu.trakeye.service.impl;

import java.math.BigInteger;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tresbu.trakeye.domain.Authority;
import com.tresbu.trakeye.domain.CaseImage;
import com.tresbu.trakeye.domain.Geofence;
import com.tresbu.trakeye.domain.LiveLogs;
import com.tresbu.trakeye.domain.TrCase;
import com.tresbu.trakeye.domain.TrNotification;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.domain.enumeration.AlertType;
import com.tresbu.trakeye.domain.enumeration.CasePriority;
import com.tresbu.trakeye.domain.enumeration.CaseStatus;
import com.tresbu.trakeye.domain.enumeration.NotificationStatus;
import com.tresbu.trakeye.domain.enumeration.NotificationType;
import com.tresbu.trakeye.domain.enumeration.UserCase;
import com.tresbu.trakeye.repository.GeofenceRepository;
import com.tresbu.trakeye.repository.TrCaseRepository;
import com.tresbu.trakeye.repository.TrNotificationRepository;
import com.tresbu.trakeye.repository.UserRepository;
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.security.SecurityUtils;
import com.tresbu.trakeye.service.FcmService;
import com.tresbu.trakeye.service.LocationLogService;
import com.tresbu.trakeye.service.MailService;
import com.tresbu.trakeye.service.SMSService;
import com.tresbu.trakeye.service.TrCaseService;
import com.tresbu.trakeye.service.dto.LocationLogCreateDTO;
import com.tresbu.trakeye.service.dto.TrCaseCreateDTO;
import com.tresbu.trakeye.service.dto.TrCaseDTO;
import com.tresbu.trakeye.service.dto.TrCaseUpdateDTO;
import com.tresbu.trakeye.service.dto.TrCaseUserDTO;
import com.tresbu.trakeye.service.dto.TrNotificationDTO;
import com.tresbu.trakeye.service.dto.UserUIDTO;
import com.tresbu.trakeye.service.mapper.CaseImageMapper;
import com.tresbu.trakeye.service.mapper.TrCaseMapper;
import com.tresbu.trakeye.service.mapper.TrNotificationMapper;
import com.tresbu.trakeye.service.mapper.UserMapper;
import com.tresbu.trakeye.web.rest.util.GeofenceUtil;

/**
 * Service Implementation for managing TrCase.
 */
@Service
@Transactional
public class TrCaseServiceImpl implements TrCaseService {

	private final Logger log = LoggerFactory.getLogger(TrCaseServiceImpl.class);

	@Autowired
	MessageSource messageSource;

	@Inject
	private TrCaseRepository trCaseRepository;

	@Inject
	private TrCaseMapper trCaseMapper;
	@Inject
	private UserRepository userRepository;

	@Inject
	private UserMapper userMapper;

	@Inject
	private TrNotificationRepository trNotificationRepository;
	@Inject
	private MailService mailService;

	@Inject
	private TrNotificationMapper trNotificationMapper;

	@Inject
	private CaseImageMapper caseImageMapper;

	@Inject
	private GeofenceRepository geofenceRepository;

	@Inject
	private SMSService smsService;
	
	@Inject
	private FcmService fcmService;

	@Inject
	private LocationLogService locationLogService;

	/**
	 * Save a trCase.
	 *
	 * @param trCaseDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	public TrCaseDTO save(TrCaseCreateDTO trCaseDTO) {
		log.debug("Request to save TrCase : {}", trCaseDTO);
		TrCase trCase = trCaseMapper.trCaseCreateDTOToTrCase(trCaseDTO);
		Optional<User> useropt = userRepository.findOneById(trCaseDTO.getReportedBy());
		User user = useropt.get();
		UserUIDTO userUIDTO = userMapper.userToUserUIDTO(user);
		trCase.setUpdatedBy(user);
		Set<Authority> authorities = user.getAuthorities();
		Set<String> collect = authorities.stream().map(Authority::getName).collect(Collectors.toSet());

		if (!collect.contains(AuthoritiesConstants.USER_ADMIN)) {
			/*Optional<User> adminopt = userRepository.findOneByLogin(user.getCreatedBy());
			User admin = adminopt.get();*/
			trCase.setAssignedBy(user);
			trCase.setAssignedTo(user);
			//assign case to himself when agent creates case
		} else {
			trCase.setAssignedBy(user);
			/*
			 * 
			 * commented when admin create the tr-case it should be assigned to
			 * user trCase.setAssignedTo(user);
			 * 
			 */
		}

		this.getGeofenceIdForLocation(trCase);

		trCase.setCreateDate(Instant.now().toEpochMilli());
		trCase.setUpdateDate(Instant.now().toEpochMilli());
		trCase.setStatus(CaseStatus.NEW);
		trCase.setTenant(SecurityUtils.getCurrentUserTenant());
		trCase = trCaseRepository.save(trCase);
		Set<CaseImage> caseImages = trCase.getCaseImages();
		for (Iterator<CaseImage> iterator = caseImages.iterator(); iterator.hasNext();) {
			CaseImage caseImage = (CaseImage) iterator.next();
			caseImage.setTrCase(trCase);
		}

		Optional<User> adminopt = userRepository.findOneByLogin(user.getCreatedBy());
		User admin = adminopt.get();
		UserUIDTO userUIDTOAdmin = userMapper.userToUserUIDTO(admin);
		

		TrCaseDTO result = trCaseMapper.trCaseToTrCaseDTO(trCase);
		// TrNotification trNotification = new TrNotification();

		String decription = "Your case Registration is completed,Your case ticket number is: " + result.getId();
		String subject = "New case created: Case No. " + result.getId();

		TrNotification trNotification = new TrNotification(Instant.now().toEpochMilli(), user, user, trCase, NotificationStatus.SENT,
				NotificationType.C, subject, decription, AlertType.EMAIL, user.getTenant());

		// trNotification.setCreatedDate(Instant.now().toEpochMilli());
		// trNotification.setFromUser(user);
		// trNotification.setToUser(user);
		// trNotification.setTrCase(trCase);
		// trNotification.setStatus(NotificationStatus.DELIVERED);
		// trNotification.setNotificationType(NotificationType.C);
		// trNotification.setSubject("New case created: Case No. " +
		// result.getId());
		// trNotification.setDescription("Your case Registration is
		// completed,Your ticket number is: " + result.getId());
		// trNotification.setAlertType(AlertType.EMAIL);

		TrNotification notification = trNotificationRepository.save(trNotification);
		TrNotificationDTO trNotificationDTO = trNotificationMapper.trNotificationToTrNotificationDTO(notification);
		trNotificationDTO.setFromUser(userUIDTO);
		trNotificationDTO.setToUser(userUIDTO);
		trNotificationDTO.setCaseIdLink(result.getId()+"");
		mailService.sendNotificationMail(trNotificationDTO);
		String message ="Your case Registration is completed,Your case ticket number is: " + result.getId();
		smsService.sendSMS(user.getPhone(), message);

		String title = "New case created: Case No. " + result.getId();
		fcmService.sendAndroidNotification(user.getFcmToken(), message, title);
		
		String decriptionForAdmin = "Case is registered,Case ticket number is: " + result.getId();
		String subjectForAdmin = "New case created: Case No. " + result.getId();

		trNotification = new TrNotification(Instant.now().toEpochMilli(), user, admin, trCase, NotificationStatus.SENT, NotificationType.C,
				subjectForAdmin, decriptionForAdmin, AlertType.EMAIL, user.getTenant());

		// trNotification.setCreatedDate(Instant.now().toEpochMilli());
		// trNotification.setFromUser(user);
		// trNotification.setToUser(admin);
		// trNotification.setTrCase(trCase);
		// trNotification.setStatus(NotificationStatus.DELIVERED);
		// trNotification.setNotificationType(NotificationType.C);
		// trNotification.setSubject("TrNotification Template");
		// trNotification.setDescription("Case is registered,Case ticket number
		// is: " + result.getId());
		// trNotification.setAlertType(AlertType.EMAIL);
		
		

		TrNotification notificationAdmin = trNotificationRepository.save(trNotification);
		TrNotificationDTO trNotificationDTOAdmin = trNotificationMapper.trNotificationToTrNotificationDTO(notificationAdmin);
		trNotificationDTOAdmin.setFromUser(userUIDTO);
		trNotificationDTOAdmin.setToUser(userUIDTOAdmin);
		trNotificationDTOAdmin.setCaseIdLink(result.getId()+"");
		mailService.sendNotificationMail(trNotificationDTOAdmin);
		message = "Case is registered,Case ticket number is: " + result.getId();
		smsService.sendSMS(admin.getPhone(), message);
		
	     title = "New case created: Case No. " + result.getId();
		fcmService.sendAndroidNotification(admin.getFcmToken(), message, title);
		
			if(trCaseDTO.getAssignedTo()!=null && trCaseDTO.getAssignedTo() > 0){
			String agentSubject = "Case Assigned Details :"+result.getId();
			String agentDescription = "Case ticket number  :" + result.getId() + " has been assigned to you";
	
			User userAssigned = userRepository.findOne(trCase.getAssignedTo().getId());
					
			trNotification = new TrNotification(Instant.now().toEpochMilli(), trCase.getAssignedBy(), trCase.getAssignedTo(), trCase,
					NotificationStatus.SENT, NotificationType.C, agentSubject, agentDescription, AlertType.EMAIL, trCase.getAssignedBy().getTenant());
	
	
			TrNotification notificationAgent = trNotificationRepository.save(trNotification);
			TrNotificationDTO trNotificationDTOAgent = trNotificationMapper.trNotificationToTrNotificationDTO(notificationAgent);
			trNotificationDTOAgent.setFromUser(userMapper.userToUserUIDTO(trCase.getAssignedBy()));
			trNotificationDTOAgent.setToUser(userMapper.userToUserUIDTO(userAssigned));
			trNotificationDTOAgent.setCaseIdLink(result.getId()+"");
			mailService.sendNotificationMail(trNotificationDTOAgent);
			message = "Case ticket number  :" + result.getId() + " has been assigned to you";
			smsService.sendSMS(userAssigned.getPhone(), message);
			
			title = "Case Assigned Details :"+result.getId();
			fcmService.sendAndroidNotification(userAssigned.getFcmToken(), message, title);
			}
		return result;
	}

	private void getGeofenceIdForLocation(TrCase trCase) {
		Optional<User> useropt = userRepository.findOneById(trCase.getReportedBy().getId());
		User user = useropt.get();
		trCase.setUpdatedBy(user);
		Set<Authority> authorities = user.getAuthorities();
		Set<String> collect = authorities.stream().map(Authority::getName).collect(Collectors.toSet());
		User admin = user;
		if (!collect.contains(AuthoritiesConstants.USER_ADMIN)) {
			Optional<User> adminopt = userRepository.findOneByLogin(user.getCreatedBy());
			admin = adminopt.get();
		}

		List<Geofence> geofences = geofenceRepository.findGeofencesCreatedBy(admin.getLogin());
		Collections.sort(geofences, (p1, p2) -> p2.getId().compareTo(p1.getId()));
		trCase.setGeofence(geofences.get(geofences.size() - 1));
		if (trCase.getPinLat() != null && trCase.getPinLong() != null) {
			LocationLogCreateDTO locationLogDTO = new LocationLogCreateDTO();
			locationLogDTO.setLatitude(trCase.getPinLat());
			locationLogDTO.setLongitude(trCase.getPinLong());
			Geofence geofence = GeofenceUtil.findValidGeofence(geofences, locationLogDTO);

			if (geofence != null) {
				trCase.setGeofence(geofence);
			}
		}
	}

	/**
	 * update a trCase.
	 *
	 * @param trCaseUpdateDTO
	 *            the entity to update
	 * @return the persisted entity
	 */
	public TrCaseDTO update(TrCaseUpdateDTO trCaseUpdateDTO) {
		log.debug("Request to save TrCase : {}", trCaseUpdateDTO);
		TrCase trCase = trCaseRepository.findCaseById(trCaseUpdateDTO.getId());
		if(trCaseUpdateDTO.getAssignedTo() == null){
			trCase.setAssignedTo(trCase.getAssignedTo());
		}else{
			User user = userRepository.getOne(trCaseUpdateDTO.getAssignedTo());
			trCase.setAssignedTo(user);
		}
/*		trCaseMapper.updateTrCaseFromTrCaseDTO(trCaseUpdateDTO, trCase);*/		
		trCase.setDescription(trCaseUpdateDTO.getDescription());
		trCase.setPriority(trCaseUpdateDTO.getPriority());
		trCase.setStatus(trCaseUpdateDTO.getStatus());
		trCase.setEscalated(trCaseUpdateDTO.getEscalated());		
		trCase.setUpdateDate(Instant.now().toEpochMilli());
		trCase = trCaseRepository.save(trCase);
		TrCaseDTO result = trCaseMapper.trCaseToTrCaseDTO(trCase);
		return result;
	}

	public void sendNotification(String oldAssignedUser, TrCaseDTO result) {
		String message;
		TrCase trCase = trCaseRepository.findOne(result.getId());
		// User user = userRepository.findOne(trCase.getReportedBy().getId());
		UserUIDTO userUIDTO = userMapper.userToUserUIDTO(trCase.getReportedBy());
		// TrNotification trNotification = new TrNotification();
		String description = null;
		String subject = null;
		String title = null;
		if(oldAssignedUser.equals(result.getAssignedToUser())){
			description= "Trakeye case no :" + result.getId() +" has been updated" ;
			subject = "Case update notification :"+result.getId();
			 title = "Case update notification :"+result.getId();
			 message= "Trakeye case no :" + result.getId() +" has been updated" ;
		}else{
			description = "Your case has been assigned to :" + trCase.getAssignedTo().getFirstName() + " " + trCase.getAssignedTo().getLastName()
					+ ", Case Ticket Number is:" + result.getId();
            subject = "Case Assigned Details :"+result.getId();
            title= "Case Assigned Details :"+result.getId();
            message = "Your case has been assigned to :" + trCase.getAssignedTo().getFirstName() + " " + trCase.getAssignedTo().getLastName()
					+ ", Case Ticket Number is:" + result.getId();
		}
		 

		TrNotification trNotification = new TrNotification(Instant.now().toEpochMilli(), trCase.getAssignedBy(), trCase.getReportedBy(), trCase,
				NotificationStatus.SENT, NotificationType.C, subject, description, AlertType.EMAIL, trCase.getAssignedBy().getTenant());

		// trNotification.setCreatedDate(Instant.now().toEpochMilli());
		// trNotification.setFromUser(trCase.getAssignedBy());
		// trNotification.setToUser(trCase.getReportedBy());
		// trNotification.setTrCase(trCase);
		// trNotification.setStatus(NotificationStatus.DELIVERED);
		// trNotification.setNotificationType(NotificationType.C);
		// trNotification.setSubject("Case Assigned Details");
		//
		// //User userAssignedTo =
		// userRepository.findOne(trCase.getAssignedTo().getId());
		//
		// // Case Created Person
		// trNotification.setDescription("Your case has been assigned to :" +
		// trCase.getAssignedTo().getFirstName() + " "
		// + trCase.getAssignedTo().getLastName() + "Case Ticket Number is:" +
		// result.getId());
		// trNotification.setAlertType(AlertType.EMAIL);

		TrNotification notification = trNotificationRepository.save(trNotification);
		TrNotificationDTO trNotificationDTO = trNotificationMapper.trNotificationToTrNotificationDTO(notification);
		trNotificationDTO.setFromUser(userMapper.userToUserUIDTO(trCase.getAssignedBy()));
		trNotificationDTO.setToUser(userUIDTO);
		trNotificationDTO.setCaseIdLink(result.getId()+"");
		mailService.sendNotificationMail(trNotificationDTO);
		/*message = messageSource.getMessage("sms.caseimpl.text3",
				new String[] { "" + trCase.getAssignedTo().getFirstName(), "" + trCase.getAssignedTo().getLastName(), "" + result.getId() },
				new Locale("en", "US"));*/
		smsService.sendSMS(trCase.getReportedBy().getPhone(), message);

		// String title = messageSource.getMessage("sms.caseimpl.text6", new String[] { "" + result.getId() }, new Locale("en", "US"));
		 fcmService.sendAndroidNotification(trCase.getReportedBy().getFcmToken(), message, title);
		 
		String agentSubject = "Case update notification : "+result.getId();
		title = "Case update notification : "+result.getId();
		String agentDescription= null;
		// if case is still assigned to the previously assigned to user itself, then give email message as case is modified. If it is assigned to some other user
		// then give email description as case has been assigned to you.
		 if(oldAssignedUser.equals(result.getAssignedToUser())){
			 agentDescription = "Trakeye case no :" + result.getId() +" has been updated" ;
             message= "Trakeye case no :" + result.getId() +" has been updated" ;
	       }else{
	    	   agentDescription = "Case Ticket Number  :" + result.getId() + " has been assigned to you";
               message = "Case Ticket Number  :" + result.getId() + " has been assigned to you";
	       }
		

		trNotification = new TrNotification(Instant.now().toEpochMilli(), trCase.getAssignedBy(), trCase.getAssignedTo(), trCase,
				NotificationStatus.SENT, NotificationType.C, agentSubject, agentDescription, AlertType.EMAIL, trCase.getAssignedBy().getTenant());

		// trNotification = new TrNotification();
		// trNotification.setCreatedDate(Instant.now().toEpochMilli());
		// trNotification.setFromUser(trCase.getAssignedBy());
		// trNotification.setToUser(trCase.getAssignedTo());
		// trNotification.setTrCase(trCase);
		// trNotification.setStatus(NotificationStatus.DELIVERED);
		// trNotification.setNotificationType(NotificationType.C);
		// trNotification.setSubject("Case Details");
		//
		// // Case Created Person
		// trNotification.setDescription("Case Ticket Number is :" +
		// result.getId() + " has been assigned to you");
		// trNotification.setAlertType(AlertType.EMAIL);

		TrNotification notificationAgent = trNotificationRepository.save(trNotification);
		TrNotificationDTO trNotificationDTOAgent = trNotificationMapper.trNotificationToTrNotificationDTO(notificationAgent);
		trNotificationDTOAgent.setFromUser(userMapper.userToUserUIDTO(trCase.getAssignedBy()));
		trNotificationDTOAgent.setToUser(userMapper.userToUserUIDTO(trCase.getAssignedTo()));
		trNotificationDTOAgent.setCaseIdLink(result.getId()+"");
		mailService.sendNotificationMail(trNotificationDTOAgent);
		
		//message = messageSource.getMessage("sms.caseimpl.text4", new String[] { "" + result.getId() }, new Locale("en", "US"));
		smsService.sendSMS(trCase.getAssignedTo().getPhone(), message);

		
		 fcmService.sendAndroidNotification(trCase.getAssignedTo().getFcmToken(), message, title);
		 
		
		Optional<User> adminopt = userRepository.findOneByLogin(trCase.getReportedBy().getCreatedBy());
		User admin = adminopt.get();
		UserUIDTO userUIDTOAdmin = userMapper.userToUserUIDTO(admin);
		trNotification = new TrNotification();

		String adminDescription=null;
		String adminSubject = null;
		 if(oldAssignedUser.equals(result.getAssignedToUser())){
			 adminDescription = "Trakeye case no :" + result.getId() +" has been updated" ;
		    adminSubject = "Case update notification :"+result.getId();
		    title =  "Case update notification :"+result.getId();
		    message = "Trakeye case no :" + result.getId() +" has been updated" ;
		 }else{
			 adminDescription = "Case assigned details " + trCase.getAssignedTo().getFirstName() + " " + trCase.getAssignedTo().getLastName()
						+ ",Case ticket number is: " + trCase.getId();
		    adminSubject = "Case Asigned Details :"+result.getId();
		    message ="Case assigned details " + trCase.getAssignedTo().getFirstName() + " " + trCase.getAssignedTo().getLastName()
						+ ",Case ticket number is: " + trCase.getId();
		    title= "Case Asigned Details :"+result.getId();
		 }
		

		trNotification = new TrNotification(Instant.now().toEpochMilli(), admin, admin, trCase, NotificationStatus.SENT, NotificationType.C,
				adminSubject, adminDescription, AlertType.EMAIL, admin.getTenant());

		// trNotification.setCreatedDate(Instant.now().toEpochMilli());
		// trNotification.setFromUser(admin);
		// trNotification.setToUser(admin);
		// trNotification.setTrCase(trCase);
		// trNotification.setStatus(NotificationStatus.DELIVERED);
		// trNotification.setNotificationType(NotificationType.C);
		// trNotification.setSubject("TrNofiifcation Template");
		// trNotification.setDescription(adminDescription);
		// trNotification.setAlertType(AlertType.EMAIL);

		TrNotification notificationAdmin = trNotificationRepository.save(trNotification);
		TrNotificationDTO trNotificationDTOAdmin = trNotificationMapper.trNotificationToTrNotificationDTO(notificationAdmin);
		trNotificationDTOAdmin.setFromUser(userUIDTOAdmin);
		trNotificationDTOAdmin.setToUser(userUIDTOAdmin);
		trNotificationDTOAdmin.setCaseIdLink(result.getId()+"");
		mailService.sendNotificationMail(trNotificationDTOAdmin);
		/*message = messageSource.getMessage("sms.caseimpl.text5",
				new String[] { "" + trCase.getAssignedTo().getFirstName(), "" + trCase.getAssignedTo().getLastName(), "" + trCase.getId() },
				new Locale("en", "US"));*/
		smsService.sendSMS(admin.getPhone(), message);
		
		//title = messageSource.getMessage("sms.caseimpl.text6", new String[] { "" + result.getId() }, new Locale("en", "US"));
		 fcmService.sendAndroidNotification(admin.getFcmToken(), message, title);
	}

	/**
	 * Get all the trCases.
	 * 
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<TrCaseDTO> findAll(Pageable pageable) {
		log.debug("Request to get all TrCases");
		Page<TrCase> result = trCaseRepository.findCasesForLoggedInUser(pageable);
		return result.map(trCase -> trCaseMapper.trCaseToTrCaseDTO(trCase));
	}

	/**
	 * Get one trCase by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public TrCaseDTO findOne(Long id) {
		log.debug("Request to get TrCase : {}", id);
		TrCase trCase = trCaseRepository.findCaseById(id);
		if(trCase == null){
			return null;
		}
		TrCaseDTO trCaseDTO = trCaseMapper.trCaseToTrCaseDTO(trCase);
		trCaseDTO.setCaseImages(caseImageMapper.caseImagesToCaseImageDTOs(trCase.getCaseImages()));
		return trCaseDTO;
	}

	/**
	 * Delete the trCase by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete TrCase : {}", id);
		trCaseRepository.delete(id);
	}

	@Override
	public List<TrCaseUserDTO> findUserAndTrCases(String login, Pageable pageable) {
		Page<TrCase> result = trCaseRepository.findCasesForLoggedInUser("%",pageable);
		List<LiveLogs> liveLogs = locationLogService.listLiveLogs(login, Instant.now().minusMillis(TimeUnit.MINUTES.toMillis(5)).toEpochMilli(),
				ZonedDateTime.now(ZoneOffset.UTC).getHour(),SecurityUtils.getCurrentUserTenantID());
		return getUserAndTrCaseList(result.getContent(), liveLogs);
	}

	@Override
	public List<TrCaseDTO> searchAllTrCases(String id) {
		List<TrCase> result = trCaseRepository.findCasesForLoggedInCaseSearch(id);
		return trCaseMapper.trCasesToTrCaseDTOs(result);
	}

	@Override
	public List<TrCaseUserDTO> findCasesForLoggedInCaseSearch(String login, String id) {
		List<TrCase> result = trCaseRepository.findCasesForLoggedInCaseSearch(id);
		List<LiveLogs> liveLogs = locationLogService.listLiveLogs(login, Instant.now().minusMillis(TimeUnit.MINUTES.toMillis(5)).toEpochMilli(),
				ZonedDateTime.now(ZoneOffset.UTC).getHour(),SecurityUtils.getCurrentUserTenantID());
		return getUserAndTrCaseList(result, liveLogs);
	}

	private List<TrCaseUserDTO> getUserAndTrCaseList(List<TrCase> result, List<LiveLogs> liveLogs) {
		List<TrCaseUserDTO> trCaseUserDTOs = new LinkedList<>();
		for (TrCase trCases : result) {
			TrCaseUserDTO trCaseUserDTO = new TrCaseUserDTO();
			trCaseUserDTO.setAddress(trCases.getAddress());
			trCaseUserDTO.setGeofence(trCases.getGeofence());
			trCaseUserDTO.setPinLat(trCases.getPinLat());
			trCaseUserDTO.setPinLong(trCases.getPinLong());
			trCaseUserDTO.setLogin(trCases.getDescription());
			trCaseUserDTO.setPriority(trCases.getPriority());
			trCaseUserDTO.setUserCase(UserCase.C.toString());
			trCaseUserDTO.setAssignedToUser(trCases.getAssignedTo()!=null?trCases.getAssignedTo().getLogin():"");
			trCaseUserDTO.setReportedByUser(trCases.getReportedBy()!=null?trCases.getReportedBy().getLogin():"");
			trCaseUserDTO.setId(trCases.getId());
			trCaseUserDTOs.add(trCaseUserDTO);
		}
		for (LiveLogs liveLogs2 : liveLogs) {
			TrCaseUserDTO trCaseUserDTO = new TrCaseUserDTO();
			trCaseUserDTO.setAddress(liveLogs2.getAddress());
			trCaseUserDTO.setPinLat(liveLogs2.getLatitude());
			trCaseUserDTO.setPinLong(liveLogs2.getLongitude());
			trCaseUserDTO.setStatus(liveLogs2.getStatus());
			trCaseUserDTO.setLogin(liveLogs2.getLogin());
			trCaseUserDTO.setUserid(liveLogs2.getUserid());
			trCaseUserDTO.setUserCase(UserCase.U.toString());
			trCaseUserDTOs.add(trCaseUserDTO);
		}
		return trCaseUserDTOs;
	}

	@Override
	public Page<TrCaseDTO> getAllTrCasesByPriority(String priority, Pageable pageable) {
		Page<TrCase> result = this.getTrCaseDTOByPriority(priority, pageable);
		return result.map(trCase -> trCaseMapper.trCaseToTrCaseDTO(trCase));
	}

	private Page<TrCase> getTrCaseDTOByPriority(String priority, Pageable pageable) {
		/*if (priority != null && priority.equals(CasePriority.HIGH.toString())) {
			return getCasePriority(pageable, CasePriority.HIGH);
		} else if (priority != null && priority.equals(CasePriority.LOW.toString())) {
			return getCasePriority(pageable, CasePriority.LOW);
		} else if (priority != null && priority.equals(CasePriority.MEDIUM.toString())) {
			return getCasePriority(pageable, CasePriority.MEDIUM);
		} else if (priority != null && priority.equals(CasePriority.CRITICAL.toString())) {
			return getCasePriority(pageable, CasePriority.CRITICAL);
		}*/
		return trCaseRepository.findCasesForLoggedInUserByPriority(pageable, priority);
	}

	/*private Page<TrCase> getCasePriority(Pageable pageable, String casePriority) {
		Page<TrCase> result = trCaseRepository.findCasesForLoggedInUserByPriority(pageable, casePriority);
		return result;
	}*/

	@Override
	public List<TrCaseUserDTO> findUserAndTrCasesByPriority(String login, String priority, Pageable pageable) {
		Page<TrCase> result = getTrCaseDTOByPriority(priority, pageable);
		List<LiveLogs> liveLogs = locationLogService.listLiveLogs(login, Instant.now().minusMillis(TimeUnit.MINUTES.toMillis(5)).toEpochMilli(),
				ZonedDateTime.now(ZoneOffset.UTC).getHour(),SecurityUtils.getCurrentUserTenantID());
		return getUserAndTrCaseList(result.getContent(), liveLogs);
	}


	@Override
	public Page<TrCaseDTO> searchAll(String searchText, Pageable pageable) {
		log.debug("In put to the search query {}",searchText);
		Page<TrCase> result = trCaseRepository.findCasesForLoggedInUser(searchText,pageable);
		return result.map(trCase -> trCaseMapper.trCaseToTrCaseDTO(trCase));
	}
	
	@Override
	public Page<TrCaseDTO> getAllTrCasesByPriorityAndSearchValue(String priority, String searchText, Pageable pageable) {
		log.debug("Api to find all trcases by priority {} and search value {}", priority, searchText);
		Page<TrCase> result = getTrCaseForPrioritySearch(priority, searchText, pageable);
		return result.map(trCase -> trCaseMapper.trCaseToTrCaseDTO(trCase));
		
	}
	
	private Page<TrCase> getTrCaseForPrioritySearch(String priority, String searchText, Pageable pageable) {
		/*if (priority != null && priority.equals(CasePriority.HIGH.toString())) {
			return getCaseForPrioritySearch(CasePriority.HIGH,searchText, pageable);
		} else if (priority != null && priority.equals(CasePriority.LOW.toString())) {
			return getCaseForPrioritySearch(CasePriority.LOW,searchText, pageable);
		} else if (priority != null && priority.equals(CasePriority.MEDIUM.toString())) {
			return getCaseForPrioritySearch(CasePriority.MEDIUM,searchText, pageable);
		} else if (priority != null && priority.equals(CasePriority.CRITICAL.toString())) {
			return getCaseForPrioritySearch(CasePriority.CRITICAL,searchText, pageable);
		}*/
		return  trCaseRepository.findCasesByPriorityAndSearch(priority, searchText,pageable);
		//return null;
	}

	private Page<TrCase> getCaseForPrioritySearch(String priority, String searchText, Pageable pageable) {
		Page<TrCase> result = trCaseRepository.findCasesByPriorityAndSearch(priority, searchText,pageable);
		return result;
	}
	
	@Transactional(readOnly = true)
	public List<TrCaseDTO> findAllCases() {
		log.debug("Request to get all TrCases");
		List<TrCase> result = trCaseRepository.findAllCasesForLoggedInUser();
		return trCaseMapper.trCasesToTrCaseDTOs(result);
	}
	
	public TrCaseDTO editAssignedToUser(Long userId, Long caseId, User loggedInuser) {
		log.debug("Request to edit assigne dto user for case with id {}: ", caseId);
		TrCase trCase = trCaseRepository.findOne(caseId);
		User assignedToUser = userRepository.findOne(userId);
		trCase.setUpdateDate(Instant.now().toEpochMilli());
		trCase.setUpdatedBy(loggedInuser);
		trCase.setAssignedBy(loggedInuser);
		trCase.setAssignedTo(assignedToUser);
		trCase = trCaseRepository.save(trCase);
		TrCaseDTO result = trCaseMapper.trCaseToTrCaseDTO(trCase);
		return result;
	}
	
	@Transactional(readOnly = true)
	public Map<String, String> caseCount() {
		log.debug("Request to get count of cases with the status NEW, PENDING, ASSIGNED, INPROGRESS, CANCELLED, ASSIGNED ");
		List<Object[]> casesCountByStatus = trCaseRepository.findCaseCount();
		Map<String, String> casecounts = new HashMap<>();
		for (Object[] objects : casesCountByStatus) {
			casecounts.put(objects[0].toString(),objects[1].toString());
		}
		return casecounts;
	}
	
	@Transactional(readOnly = true)
	public  List<TrCaseDTO> casesAssignedToUserToday(Long assigneToId) {
		log.debug("Request to get cases assigned to loggedin user for the current day");
		
		 Calendar date = new GregorianCalendar();
	     date.set(Calendar.HOUR_OF_DAY, 0);
	     date.set(Calendar.MINUTE, 0);
	     date.set(Calendar.SECOND, 0);
	     date.set(Calendar.MILLISECOND, 0);
	     Long fromtime= date.getTimeInMillis();
	     date.set(Calendar.HOUR_OF_DAY, 23);
	     date.set(Calendar.MINUTE, 59);
	     date.set(Calendar.SECOND, 59);
	     date.set(Calendar.MILLISECOND, 1000);
	     Long totime= date.getTimeInMillis();
	    
	     
		List<TrCase> result = trCaseRepository.casesAssignedToUserToday(fromtime,totime,assigneToId);
		
		return trCaseMapper.trCasesToTrCaseDTOs(result);
	}

}
