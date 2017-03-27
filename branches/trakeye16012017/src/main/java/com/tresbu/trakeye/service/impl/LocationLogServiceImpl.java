package com.tresbu.trakeye.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.tresbu.trakeye.config.TrakEyeProperties;

import com.tresbu.trakeye.domain.LiveLogs;
import com.tresbu.trakeye.domain.LocationLog;
import com.tresbu.trakeye.domain.TrNotification;
import com.tresbu.trakeye.domain.TrakeyeTypeAttributeValue;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.domain.enumeration.AlertType;
import com.tresbu.trakeye.domain.enumeration.GeofenceStatus;
import com.tresbu.trakeye.domain.enumeration.LogSource;
import com.tresbu.trakeye.domain.enumeration.NotificationStatus;
import com.tresbu.trakeye.domain.enumeration.NotificationType;
import com.tresbu.trakeye.repository.LocationLogRepository;
import com.tresbu.trakeye.repository.TrNotificationRepository;
import com.tresbu.trakeye.repository.UserRepository;
import com.tresbu.trakeye.security.SecurityUtils;
import com.tresbu.trakeye.service.FcmService;
import com.tresbu.trakeye.service.LocationLogService;
import com.tresbu.trakeye.service.MailService;
import com.tresbu.trakeye.service.SMSService;
import com.tresbu.trakeye.service.dto.BatteryReportDTO;
import com.tresbu.trakeye.service.dto.DistanceReportDTO;
import com.tresbu.trakeye.service.dto.LocationLogCreateDTO;
import com.tresbu.trakeye.service.dto.LocationLogDTO;
import com.tresbu.trakeye.service.dto.PathDTO;
import com.tresbu.trakeye.service.dto.StrokeDTO;
import com.tresbu.trakeye.service.dto.TrNotificationDTO;
import com.tresbu.trakeye.service.dto.UserPathDTO;
import com.tresbu.trakeye.service.dto.UserUIDTO;
import com.tresbu.trakeye.service.mapper.LocationLogMapper;
import com.tresbu.trakeye.service.mapper.TrNotificationMapper;
import com.tresbu.trakeye.service.mapper.UserMapper;
import com.tresbu.trakeye.service.util.RandomUtil;
import com.tresbu.trakeye.web.rest.util.GeofenceUtil;

/**
 * Service Implementation for managing LocationLog.
 */
@Service
@Transactional
public class LocationLogServiceImpl implements LocationLogService {

	private final Logger log = LoggerFactory.getLogger(LocationLogServiceImpl.class);
	
	@Autowired
	MessageSource messageSource;

	@Inject
	private LocationLogRepository locationLogRepository;

	@Inject
	private LocationLogMapper locationLogMapper;	

	@Inject
	private UserRepository userRepository;
	
	@Inject
	private UserMapper userMapper;

	@Inject
	private TrNotificationRepository trNotificationRepository;

	@Inject
	private TrNotificationMapper trNotificationMapper;
	
	@Inject
	private MailService mailService;
	
	@Inject
	private FcmService fcmService;
	
	@Inject
	private SMSService smsService;
	
    @Inject
    private TrakEyeProperties jHipsterProperties;

	/**
	 * Save a locationLog.
	 *
	 * @param locationLogDTO
	 *            the entity to save
	 * @return the persisted entity
	 * @throws ParseException 
	 */
	public LocationLogDTO save(LocationLogCreateDTO locationLogDTO) throws ParseException {
		log.debug("Request to save LocationLog : {}", locationLogDTO);
		double distance=0;
		Pageable topOne= new PageRequest(0, 1);// This is to get only one record at a time
		// Below list will always have only one record due to the pagination object passed to the query
		List<LocationLog> latestDBLog = new ArrayList<LocationLog>();
		
		if(locationLogDTO.getLogSource().toString().equals(LogSource.GPS.toString())){
			latestDBLog= locationLogRepository.latestLogForGPS(topOne);
			log.debug("Fetching latest log in case of GPS: {}", latestDBLog);
		} else if(locationLogDTO.getLogSource().toString().equals(LogSource.NP.toString()) || locationLogDTO.getLogSource().toString().equals(LogSource.NETWORK.toString())){
			latestDBLog= locationLogRepository.latestLogForNP(topOne,locationLogDTO.getCreatedDateTime());
			log.debug("Fetching latest log in case of NP or network: {}", latestDBLog);
			
		}
		if(latestDBLog.size() >0){
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date(locationLogDTO.getCreatedDateTime());
		    String logDTODate=simpleDateFormat.format(date);
		    
		    Date date1=new Date(latestDBLog.get(0).getCreatedDateTime());
		    String dbLogDate=simpleDateFormat.format(date1);
		    if(logDTODate.equals(dbLogDate)){
		    	distance = RandomUtil.distance(latestDBLog.get(0).getLatitude(), latestDBLog.get(0).getLongitude(), locationLogDTO.getLatitude(), locationLogDTO.getLongitude(), "K");
		    	log.debug("distance calculated is {}", distance);
		    			    	
		    }
		}
		
		LocationLog locationLog = locationLogMapper.createLocationLogDTOToLocationLog(locationLogDTO);
		locationLog.setDistanceTravelled(distance);
		locationLog.setTenant(SecurityUtils.getCurrentUserTenant());
		locationLog = locationLogRepository.save(locationLog);
		LocationLogDTO result = locationLogMapper.locationLogToLocationLogDTO(locationLog);
		
		this.validateLatLong(locationLogDTO);
		return result;
	}
	@Transactional
	public LocationLogDTO update(long id, int batteryPercentage) {
	LocationLog findOne = locationLogRepository.findOne(id);
	findOne.setUpdatedDateTime(Instant.now().toEpochMilli());
	findOne.setBatteryPercentage(batteryPercentage);
	return locationLogMapper.locationLogToLocationLogDTO(findOne);
	}

	private void validateLatLong(LocationLogCreateDTO locationLogDTO) {
		if (locationLogDTO.getUserId() != 0) {
			User user = userRepository.findOne(locationLogDTO.getUserId());

			boolean isValidLocation = GeofenceUtil.validateGeofence(user.getGeofences(),
					locationLogDTO);

			String status = GeofenceStatus.IN.toString();

			if (isValidLocation == false) {
				status = GeofenceStatus.OUT.toString();
			} else {
				status = GeofenceStatus.IN.toString();
			}

			Optional<User> adminopt = userRepository.findOneByLogin(user.getCreatedBy());
			User admin = adminopt.get();
			UserUIDTO userUIDTO= userMapper.userToUserUIDTO(admin);

			if ((user.getIsValidLocation() == null && status.equals(GeofenceStatus.OUT.toString()))
					|| (user.getIsValidLocation()!=null && !user.getIsValidLocation().equals(status))) {
				sendNotification(user,status, userUIDTO,admin,locationLogDTO);
			}

			user.setIsValidLocation(status);
			user = userRepository.save(user);
		}
	}

	private void sendNotification(User user, String status, UserUIDTO userUIDTO,User admin, LocationLogCreateDTO locationLogDTO) {
		String message= null;
		String description=null;
		String title=null;
		String subject="Location Details";
		if(status!=null&&status.equals(GeofenceStatus.OUT.toString())==true){
			description="Your user : "+user.getLogin()+" presently outside the assigned geofence " +locationLogDTO.getAddress();
			message=messageSource.getMessage("sms.locationlogimpl.text1", new String[]{""+user.getLogin(),""+locationLogDTO.getAddress()}, new Locale("en", "US"));
			
			title = messageSource.getMessage("sms.caseimpl.text8", new String[] { "" + user.getLogin(),""+locationLogDTO.getAddress() }, new Locale("en", "US"));
			fcmService.sendAndroidNotification(admin.getFcmToken(), message, title);
		}else{
			description="Your user :"+user.getLogin() +"entered inside the assigned geofence "+locationLogDTO.getAddress();
			message=messageSource.getMessage("sms.locationlogimpl.text2", new String[]{""+user.getLogin(),""+locationLogDTO.getAddress()}, new Locale("en", "US"));
			
			title = messageSource.getMessage("sms.caseimpl.text8", new String[] { ""+user.getLogin(),"" + locationLogDTO.getAddress() }, new Locale("en", "US"));
			fcmService.sendAndroidNotification(admin.getFcmToken(), message, title);
		}
		
		TrNotification trNotification=new TrNotification(Instant.now().toEpochMilli(), admin,
				admin, null, NotificationStatus.SENT, NotificationType.A, subject, description, AlertType.EMAIL, admin.getTenant());
		
//		TrNotification trNotification = new TrNotification();
//		trNotification.setFromUser(admin);
//		trNotification.setToUser(admin);
//		trNotification.setCreatedDate(Instant.now().toEpochMilli());
//		// trNotification.setTrCase(trCase);
//		trNotification.setStatus(NotificationStatus.DELIVERED);
//		trNotification.setNotificationType(NotificationType.A);
//		trNotification.setSubject("TrNotification Template");
//		trNotification.setAlertType(AlertType.EMAIL);
		
		
		TrNotification notification = trNotificationRepository.save(trNotification);
		TrNotificationDTO trNotificationDTO=trNotificationMapper.trNotificationToTrNotificationDTO(notification);
		trNotificationDTO.setFromUser(userUIDTO);
		trNotificationDTO.setToUser(userUIDTO);
		mailService.sendAdminNotificationMail(trNotificationDTO);
		smsService.sendSMS(admin.getPhone(), message);

		String agntDescription=null;
		if (status != null && status.equals(GeofenceStatus.OUT.toString()) == true) {
			agntDescription="This mail is intended to inform that you are outside the assigned geofence "
					+ locationLogDTO.getAddress();
			message = messageSource.getMessage("sms.locationlogimpl.text3",
					new String[] { "" + locationLogDTO.getAddress() }, new Locale("en", "US"));
			
		    title = messageSource.getMessage("sms.caseimpl.text8", new String[] { "" + locationLogDTO.getAddress() }, new Locale("en", "US"));
			fcmService.sendAndroidNotification(admin.getFcmToken(), message, title);
		} else {
			agntDescription="This mail is intended to inform that you are entered inside the assigned geofence"
							+ locationLogDTO.getAddress();
			message = messageSource.getMessage("sms.locationlogimpl.text4",
					new String[] { "" + locationLogDTO.getAddress() }, new Locale("en", "US"));
			
			title = messageSource.getMessage("sms.caseimpl.text8", new String[] { "" + locationLogDTO.getAddress() }, new Locale("en", "US"));
			fcmService.sendAndroidNotification(admin.getFcmToken(), message, title);
		}
		trNotification=new TrNotification(Instant.now().toEpochMilli(), admin,
				 user, null, NotificationStatus.SENT, NotificationType.A, subject, agntDescription, AlertType.EMAIL, admin.getTenant());
		 
//		trNotification = new TrNotification();
//		trNotification.setFromUser(admin);
//		trNotification.setToUser(user);
//		trNotification.setCreatedDate(Instant.now().toEpochMilli());
//		// trNotification.setTrCase(trCase);
//		trNotification.setStatus(NotificationStatus.DELIVERED);
//		trNotification.setNotificationType(NotificationType.A);
//		trNotification.setSubject("TrNotification Template");
//		trNotification.setAlertType(AlertType.EMAIL);
		
		TrNotification notificationAgent = trNotificationRepository.save(trNotification);
		TrNotificationDTO trNotificationDTOAgent=trNotificationMapper.trNotificationToTrNotificationDTO(notificationAgent);
		trNotificationDTOAgent.setFromUser(userUIDTO);
		trNotificationDTOAgent.setToUser(userMapper.userToUserUIDTO(user));
		mailService.sendNotificationMail(trNotificationDTOAgent);
		smsService.sendSMS(user.getPhone(), message);
		
		title = messageSource.getMessage("sms.caseimpl.text8", new String[] { "" + locationLogDTO.getAddress() }, new Locale("en", "US"));
		fcmService.sendAndroidNotification(admin.getFcmToken(), message, title);
	}

	/**
	 * Get all the locationLogs.
	 * 
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
/*	@Transactional(readOnly = true)
	public Page<LocationLogDTO> findAll(long fromDate, long toDate, Pageable pageable) {
		log.debug("Request to get all LocationLogs");
		Page<LocationLog> result = locationLogRepository.findLogsForLoggedInUser(fromDate, toDate, pageable);

		return result.map(locationLog -> locationLogMapper.locationLogToLocationLogDTO(locationLog));
	}*/

	/**
	 * Get one locationLog by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public LocationLogDTO findOne(Long id) {
		log.debug("Request to get LocationLog : {}", id);
		LocationLog locationLog = locationLogRepository.findOne(id);
		LocationLogDTO locationLogDTO = locationLogMapper.locationLogToLocationLogDTO(locationLog);
		return locationLogDTO;
	}

	/**
	 * Delete the locationLog by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete LocationLog : {}", id);
		locationLogRepository.delete(id);
	}

	/**
	 * Get all the locationLogs.
	 * 
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public UserPathDTO listLocationPath(Long login, long fromDate, long toDate) {
		log.debug("Request to get all LocationLogs");
		log.debug("fromDate and toDate values in listLocationPath in serviceimpl {} {}",fromDate,toDate);
		LinkedList<LocationLog> result = locationLogRepository.listLocationPath(login, fromDate, toDate);
		UserPathDTO pathlist;
		LinkedList<PathDTO> list = new LinkedList<PathDTO>();
		String userName = "";
		for (LocationLog locationLog : result) {
			list.add(new PathDTO(locationLog.getLatitude(), locationLog.getLongitude(),locationLog.getAddress(),locationLog.getCreatedDateTime(),locationLog.getUpdatedDateTime(),locationLog.getBatteryPercentage()));
		}
		/*
		 * if(result!=null && result.size()>0){ LocationLog locationLog =
		 * result.get(0); User user =locationLog.getUser(); userName =
		 * user.getFirstName()+user.getLastName(); }
		 */
		if (userRepository.findOneById(login).isPresent()) {
			Optional<User> user = userRepository.findOneById(login);
			userName = user.get().getFirstName() + user.get().getLastName();
		}
		pathlist = new UserPathDTO(login, userName, list, new StrokeDTO(getColor()));

		return pathlist;
	}

/*	@Transactional(readOnly = true)
	public List<LocationLogDTO> listLatestLocations() {
		log.debug("Request to get all LocationLogs");
		List<LocationLog> result = locationLogRepository.getlatestLocations();
		List<LocationLogDTO> locationLogDTOs = locationLogMapper.locationLogsToLocationLogDTOs(result);
		return locationLogDTOs;
	}*/

	@Transactional(readOnly = true)
	public List<LiveLogs> listLiveLogs(String login, long dateTime, int hour, long tenantID) {
		log.debug("Request to get all LocationLogs login{}, dateTime{}", login, dateTime, hour);
		List<LiveLogs> result = locationLogRepository.getLiveLogs(login, dateTime, hour,tenantID);
		log.debug("Number of Live logs are [" + result.size() + "]");
		// List<LocationLogDTO> locationLogDTOs =
		// locationLogMapper.locationLogsToLocationLogDTOs(result);
		return result;
	}

	private String getColor() {
		/*
		 * String[] letters = "0123456789ABCDEF".split(""); String color = "#";
		 * for (int i = 0; i < 6; i++ ) { color += letters[(int)
		 * Math.round(Math.random() * 15)]; } return color;
		 */

		Random randomService = new Random();
		StringBuilder sb = new StringBuilder();
		while (sb.length() < 6) {
			sb.append(Integer.toHexString(randomService.nextInt()));
		}
		sb.setLength(6);
		return "#" + sb.toString();
	}
	
	@Override
	@Scheduled(fixedDelayString = "${trakeye.gpsTracker.delayTime}")
	public void getUserAndInsertGpsLocationLogs() {
		boolean isEnabled = jHipsterProperties.getGpsTracker().isEnabled();
		if (!isEnabled) {
			log.debug("GPS traker is not enabled");
			return;
		} else {
			log.debug("GPS traker is enabled updating location logs for the below device logins ");
		}

		String trakEyeType = jHipsterProperties.getGpsTracker().getTrakEyeType();
		String gpsUrl = jHipsterProperties.getGpsTracker().getGpsServerUrl();
		String param1 = jHipsterProperties.getGpsTracker().getParam1();
		String param2 = jHipsterProperties.getGpsTracker().getParam2();
		// TODO Auto-generated method stub
		List<User> users = userRepository.getUsersByTrakeyeType(trakEyeType);

		log.debug("Request to update GPS logs ");
		RestTemplate restTemplate = new RestTemplate();
		String trakerUrl = null;
		for (User user : users) {

			Set<TrakeyeTypeAttributeValue> trakeyeTypeAttributeValues = user.getTrakeyeTypeAttributeValues();
			String parm1Value = null;
			String parm2Value = null;
			for (TrakeyeTypeAttributeValue trakeyeTypeAttributeValue : trakeyeTypeAttributeValues) {

				if (trakeyeTypeAttributeValue.getTrakeyeTypeAttribute().getName().equals(param1)) {
					parm1Value = trakeyeTypeAttributeValue.getAttributeValue();
				} else if (trakeyeTypeAttributeValue.getTrakeyeTypeAttribute().getName().equals(param2)) {
					parm2Value = trakeyeTypeAttributeValue.getAttributeValue();
				}
			}
			if (parm1Value == null || parm2Value == null)
				continue;

			String userIMEI = parm2Value.split(",")[1];
			if (trakerUrl != null) {
				trakerUrl = trakerUrl + ";" + userIMEI;
			} else {
				trakerUrl = gpsUrl + "&" + param1 + "=" + parm1Value + "&" + param2 + "=" + parm2Value;
			}

		}
		log.debug("GPS Server Url is {}", trakerUrl);

		if(trakerUrl != null){
		String responseEntity = restTemplate.getForObject(trakerUrl, String.class);
		if (responseEntity == null) {
			log.debug("No response from given GPS Server urs {}", trakerUrl);
			
		}else {
			log.debug("Response from gps tracker : {}", responseEntity);
			try {
				JSONObject json = new JSONObject(responseEntity);
				updateDeviceLocationLog(json,users,param1,param2);
			} catch (JSONException e) {
				log.error("Response from GPS Server error {}",e.getMessage());
			}
		}
		}
				
	}
	
private void updateDeviceLocationLog(JSONObject json,List<User> users,String param1,String param2){
		
		for (User user : users) {
			Set<TrakeyeTypeAttributeValue> trakeyeTypeAttributeValues = user.getTrakeyeTypeAttributeValues();
			String parm1Value = null;
			String parm2Value = null;
			for (TrakeyeTypeAttributeValue trakeyeTypeAttributeValue : trakeyeTypeAttributeValues) {

				if (trakeyeTypeAttributeValue.getTrakeyeTypeAttribute().getName().equals(param1)) {
					parm1Value = trakeyeTypeAttributeValue.getAttributeValue();
				} else if (trakeyeTypeAttributeValue.getTrakeyeTypeAttribute().getName().equals(param2)) {
					parm2Value = trakeyeTypeAttributeValue.getAttributeValue();
				}
			}
			if (parm1Value == null || parm2Value == null)
				continue;

			String userIMEI = parm2Value.split(",")[1];
			
		try {
			 
			LocationLog locationLog = locationLogRepository.getlatestLocation(user.getLogin());

			double currentLat = Double.parseDouble(json.getJSONObject(userIMEI).getString("lat"));
			double currentLang = Double.parseDouble(json.getJSONObject(userIMEI).getString("lng"));

			double distance = 0;
			if (locationLog != null)
				distance = RandomUtil.distance(currentLat, currentLang, locationLog.getLatitude(),
						locationLog.getLongitude(), "K");

			if (distance > 0.01 || locationLog == null) {
				// create location log
				LocationLog newLoactionLog = new LocationLog();
				newLoactionLog.setAddress("");
				newLoactionLog.setLatitude(currentLat);
				newLoactionLog.setLongitude(currentLang);
				newLoactionLog.setDistanceTravelled(distance);
				newLoactionLog.setUser(user);
				newLoactionLog.logSource(LogSource.GPS);
				newLoactionLog.setCreatedDateTime(Instant.now().toEpochMilli());
				locationLogRepository.save(newLoactionLog);
			} else {
				locationLog.setUpdatedDateTime(Instant.now().toEpochMilli());
				locationLogRepository.save(locationLog);
				// update loaction log
			}

			
		} catch (Exception e) {
			log.error("Failed with response {} ", e.getMessage());
		}
		}
	}

@Transactional(readOnly = true)
public List<BatteryReportDTO> listBatteryDetails(Long login, long fromDate, long toDate) {
	log.debug("Request to get battery details");
	log.debug("fromDate and toDate values in listLocationPath in serviceimpl {} {}",fromDate,toDate);
	List<BatteryReportDTO> findBatteryReport = locationLogRepository.findBatteryDetails(login,fromDate,toDate );
	
	return findBatteryReport;
}


@Transactional(readOnly = true)
public List<DistanceReportDTO> getDistanceReport(Long login, long fromDate, long toDate) {
	log.debug("Request to get distance details");
	log.debug("fromDate and toDate values in to find distance in serviceimpl {} {}",fromDate,toDate);
	List<DistanceReportDTO> distanceReportDTO = locationLogRepository.getDistanceReport(login,fromDate,toDate );
	
	return distanceReportDTO;
}
}
