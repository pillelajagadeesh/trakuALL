package com.tresbu.trakeye.service.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tresbu.trakeye.domain.TrNotification;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.domain.enumeration.AlertType;
import com.tresbu.trakeye.domain.enumeration.NotificationStatus;
import com.tresbu.trakeye.domain.enumeration.NotificationType;
import com.tresbu.trakeye.repository.TrNotificationRepository;
import com.tresbu.trakeye.repository.UserRepository;
import com.tresbu.trakeye.security.SecurityUtils;
import com.tresbu.trakeye.service.FcmService;
import com.tresbu.trakeye.service.MailService;
import com.tresbu.trakeye.service.SMSService;
import com.tresbu.trakeye.service.TrNotificationService;
import com.tresbu.trakeye.service.UserService;
import com.tresbu.trakeye.service.dto.TrNotificationDTO;
import com.tresbu.trakeye.service.dto.TrNotificationListDTO;
import com.tresbu.trakeye.service.dto.TrNotificationUpdateDTO;
import com.tresbu.trakeye.service.dto.UserUIDTO;
import com.tresbu.trakeye.service.mapper.TrNotificationMapper;
import com.tresbu.trakeye.service.mapper.UserMapper;

/**
 * Service Implementation for managing TrNotification.
 */
@Service
@Transactional
@EnableScheduling
public class TrNotificationServiceImpl implements TrNotificationService{

    private final Logger log = LoggerFactory.getLogger(TrNotificationServiceImpl.class);
    
    @Autowired
	MessageSource messageSource;
    
    @Inject
    private TrNotificationRepository trNotificationRepository;

    @Inject
    private TrNotificationMapper trNotificationMapper;

    @Inject
    private MailService mailService;
    
    @Inject
    private UserRepository userRepository;
    
    @Inject
	private FcmService fcmService;
    
    @Inject
    private UserService userService;
    
    @Inject
    private UserMapper userMapper;
    
    @Inject
    private SMSService smsService;
    /**
     * Save a trNotification.
     *
     * @param trNotificationDTO the entity to save
     * @return the persisted entity
     */
    public TrNotificationDTO save(TrNotificationDTO trNotificationDTO) {
        log.debug("Request to save TrNotification : {}", trNotificationDTO);
        TrNotification trNotification = trNotificationMapper.trNotificationDTOToTrNotification(trNotificationDTO);
        trNotification.setTenant(SecurityUtils.getCurrentUserTenant());
        trNotification = trNotificationRepository.save(trNotification);
        TrNotificationDTO result = trNotificationMapper.trNotificationToTrNotificationDTO(trNotification);
        Optional<User> fromUser= userRepository.findOneById(result.getFromUserId());
        UserUIDTO fromUserUIDTO = userMapper.userToUserUIDTO(fromUser.get());
        Optional<User> toUser= userRepository.findOneById(result.getToUserId());
        UserUIDTO toUserUIDTO = userMapper.userToUserUIDTO(toUser.get());
        
        result.setFromUser(fromUserUIDTO);
        result.setToUser(toUserUIDTO);
		mailService.sendNotificationMail(result);
		
		String message = result.getDescription();
		String title = result.getSubject();
		fcmService.sendAndroidNotification(toUser.get().getFcmToken(), message, title);

        return result;
    }

    
    /**
     *  Get all the trNotifications.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TrNotificationListDTO> findAll(String searchText,Pageable pageable) {
        log.debug("Request to get all TrNotifications");
        if(searchText==null ||searchText.isEmpty()){
    			searchText="%";
        }
        Page<TrNotification> result = trNotificationRepository.findAll(searchText,pageable);
        return result.map(trNotification -> trNotificationMapper.trNotificationToTrNotificationListDTO(trNotification));
    }

    /**
     *  Get one trNotification by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TrNotificationListDTO findOne(Long id) {
        log.debug("Request to get TrNotification : {}", id);
        TrNotification trNotification = trNotificationRepository.findNotificationById(id);
        if(trNotification == null){
        	return null;
        }
        TrNotificationListDTO trNotificationDTO = trNotificationMapper.trNotificationToTrNotificationListDTO(trNotification);
        return trNotificationDTO;
    }

    /**
     *  Delete the  trNotification by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TrNotification : {}", id);
        trNotificationRepository.delete(id);
    }
    
    /**
     * Sends all pending notifications and it's status updated to delivered.
     *
     */
  //  @Scheduled(fixedDelay=5000)
   /* public void sendNotifications(){
    	
    	log.debug("REST request to get a page of TrNotifications");
    	List<TrNotification> notifications  = trNotificationRepository.findByStatus();
    	
    	Iterator<TrNotification> iterator = notifications.listIterator();
    	String message=null;
    	while(iterator.hasNext()){
    		TrNotification notification  = iterator.next();
    		TrNotificationDTO trNotificationDTO=trNotificationMapper.trNotificationToTrNotificationDTO(notification);
    		
    		if ((notification.getAlertType().name().toString()).equals((AlertType.EMAIL).toString())){
    			mailService.sendNotificationMail(trNotificationDTO);
    			message=trNotificationDTO.getDescription();
    			smsService.sendSMS(trNotificationDTO.getToUser().getPhone(), message);
    			trNotificationRepository.updateStatus(notification.getId().longValue(), NotificationStatus.DELIVERED);
    		}
    		if ((notification.getAlertType().name().toString()).equals((AlertType.SMS).toString())){
    			message=trNotificationDTO.getDescription();
    			smsService.sendSMS(trNotificationDTO.getToUser().getPhone(), message);
    			//smsService.sendNotificationSMS(notification);
    			trNotificationRepository.updateStatus(notification.getId().longValue(), NotificationStatus.DELIVERED);
    		}
    	}		
    }*/

	@Override
	public void sendNotification(User pUser,String fileName,String iosFileName,HttpServletRequest request) {
		
		List<User> users=userService.findAdminUsersByLogin();
		for(User user:users){
			 String baseUrl = request.getScheme() + // "http"
	                    "://" +                                // "://"
	                    request.getServerName() +              // "myhost"
	                    "/custom/"+pUser.getLogin()+"/agents/"+"android"+"/"+fileName;
			 
			 String baseIosUrl = request.getScheme() + // "http"
	                    "://" +                                // "://"
	                    request.getServerName() +              // "myhost"
	                    "/custom/"+pUser.getLogin()+"/agents/"+"ios"+"/"+iosFileName;
			UserUIDTO userUIDTO=userMapper.userToUserUIDTO(user);
		    mailService.sendDownloadLinkMail(userUIDTO, baseUrl,baseIosUrl);
			String message=messageSource.getMessage("sms.apklink.text1", new String[]{""+user.getLogin(),""+baseUrl,""+baseIosUrl}, new Locale("en", "US"));
			smsService.sendSMS(user.getPhone(), message);
			
			TrNotification trNotification = new TrNotification();
			trNotification.setFromUser(user);
			trNotification.setToUser(user);
			trNotification.setCreatedDate(Instant.now().toEpochMilli());
			
			//trNotification.setTrCase(trCase);
			trNotification.setStatus(NotificationStatus.SENT);
			trNotification.setNotificationType(NotificationType.A);
			trNotification.setSubject("Sent Andriod Apk Link");
			trNotification.setDescription("Latest Andriod apk is sent for your email");
			trNotification.setAlertType(AlertType.EMAIL);
			trNotification.setDownloadLink(baseUrl);
			trNotification.setTenant(user.getTenant());
			trNotificationRepository.save(trNotification);
			
		}
		
	}  
	
	@Override
	public TrNotificationDTO update(TrNotificationDTO trNotificationUpdateDTO) {
		log.debug("Request to save TrCase : {}", trNotificationUpdateDTO);
		TrNotification trNotification = trNotificationRepository.findNotificationById(trNotificationUpdateDTO.getId());
		if(trNotification == null){
			return null;
		}
		
		trNotification.setStatus(trNotificationUpdateDTO.getStatus());
		trNotification = trNotificationRepository.save(trNotification);
		TrNotificationDTO result = trNotificationMapper.trNotificationToTrNotificationDTO(trNotification);
		
		Optional<User> fromUser= userRepository.findOneById(result.getFromUserId());
        UserUIDTO fromUserUIDTO = userMapper.userToUserUIDTO(fromUser.get());
        Optional<User> toUser= userRepository.findOneById(result.getToUserId());
        UserUIDTO toUserUIDTO = userMapper.userToUserUIDTO(toUser.get());
        
        result.setFromUser(fromUserUIDTO);
        result.setToUser(toUserUIDTO);
		//mailService.sendNotificationMail(result);
		
		String message = result.getDescription();
		String title = result.getSubject();
		//fcmService.sendAndroidNotification(toUser.get().getFcmToken(), message, title);
		return result;
	}
	
	@Override
	public Map<String, String> notificationCount() {
		log.debug("Request to get unread notification count based on status");
		List<Object[]> notificationCountByStatus = trNotificationRepository.findNotificationCount();
		Map<String, String> notificationcounts = new HashMap<>();
		for (Object[] objects : notificationCountByStatus) {
			notificationcounts.put(objects[0].toString(),objects[1].toString());
		}
		return notificationcounts;
	}

}
