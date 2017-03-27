package com.tresbu.trakeye.service;

import java.io.UnsupportedEncodingException;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tresbu.trakeye.domain.Authority;
import com.tresbu.trakeye.domain.Geofence;
import com.tresbu.trakeye.domain.Tenant;
import com.tresbu.trakeye.domain.TrakeyeType;
import com.tresbu.trakeye.domain.TrakeyeTypeAttributeValue;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.repository.AuthorityRepository;
import com.tresbu.trakeye.repository.GeofenceRepository;
import com.tresbu.trakeye.repository.TrakeyeTypeAttributeValueRepository;
import com.tresbu.trakeye.repository.UserRepository;
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.security.SecurityUtils;
import com.tresbu.trakeye.service.dto.UserDTO;
import com.tresbu.trakeye.service.dto.UserIdDTO;
import com.tresbu.trakeye.service.dto.UserUIDTO;
import com.tresbu.trakeye.service.mapper.UserMapper;
import com.tresbu.trakeye.service.util.RandomUtil;
import com.tresbu.trakeye.web.rest.vm.ManagedUserVM;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;
    
    @Inject
    private UserMapper userMapper;

    @Inject
    private AuthorityRepository authorityRepository;
    
    @Inject
    private TrakeyeTypeAttributeValueRepository trakeyeTypeAttributeValueRepository;
    @Inject
    GeofenceRepository geofenceRepository;
    
    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public UserUIDTO completePasswordReset(String newPassword, String key) {
       log.debug("Reset user password for reset key {}", key);

       Optional<User> user = userRepository.findOneByResetKey(key);
       if(user.isPresent()){
    	   Long millisec= Instant.now().toEpochMilli() - user.get().getResetDate();
           int hours = (int) (millisec / 3600000);
           if(hours >= 24){
        	   return null;
           }else{
        	   user.get().setPassword(passwordEncoder.encode(newPassword));
               user.get().setResetKey(null);
               user.get().setResetDate(Instant.now().toEpochMilli());
               userRepository.save(user.get());
               UserUIDTO userUIDTO= userMapper.userToUserUIDTO(user.get());
               return userUIDTO;
           }
    	   
       }
       return null;
       
	
    }

    @Transactional
    public UserUIDTO requestPasswordReset(String login, String keyValue) {
    	log.info("reset password ");
    	
    	Optional<User> user = userRepository.findOneByLogin(login);
        if(user.isPresent()){
        	log.info("User is present with the given login ");
        	user.get().setResetKey(keyValue);
            user.get().setResetDate(Instant.now().toEpochMilli());
            userRepository.save(user.get());  
                UserUIDTO userUIDTO= userMapper.userToUserUIDTO(user.get());
                return userUIDTO;
            
     	   
        }
        return null;
    }


    public User createUser(ManagedUserVM managedUserVM) throws  UnsupportedEncodingException {
    	
    	User user = new User();
        user.setLogin(managedUserVM.getLogin());
        user.setFirstName(managedUserVM.getFirstName());
        user.setLastName(managedUserVM.getLastName());
        user.setEmail(managedUserVM.getEmail());
       
        user.setFromTime(managedUserVM.getFromTime());
        user.setToTime(managedUserVM.getToTime());
        if (managedUserVM.getLangKey() == null) {
            user.setLangKey("en"); // default language
        } else {
            user.setLangKey(managedUserVM.getLangKey());
        }
        if (managedUserVM.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            managedUserVM.getAuthorities().stream().forEach(
                authority -> authorities.add(authorityRepository.findOne(authority))
            );
            user.setAuthorities(authorities);
        }
        
        if (managedUserVM.getGeofences() != null) {
        	Set<Geofence> geofences = new HashSet<>();
            managedUserVM.getGeofences().stream().forEach(
               geofence -> geofences.add((geofenceRepository.findGeofenceByName(geofence)).get())		
            );
            user.setGeofences(geofences);
        }
        
        if (managedUserVM.getTrakeyeTypeAttributeValues() != null) {
            user.setTrakeyeTypeAttributeValues(managedUserVM.getTrakeyeTypeAttributeValues());
        }
        if (managedUserVM.getLastModifiedBy() != null){
        	user.setCreatedBy(managedUserVM.getLastModifiedBy());
        	user.setLastModifiedBy(managedUserVM.getLastModifiedBy());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date curdate = new Date();
		String date = sdf.format(curdate);
        String keyValue = user.getLogin()+"^0^"+Instant.now().toEpochMilli();
        keyValue = RandomUtil.convertStringToHex(keyValue);
        //keyValue= RandomUtil.getEncrypt(keyValue);
        //keyValue = URLEncoder.encode(keyValue, "UTF-8");
        user.setResetKey(keyValue);
        user.setResetDate(Instant.now().toEpochMilli());
        user.setActivated(true);
        user.setTrakeyeType(managedUserVM.getTrakeyeType());
        user.setPhone(managedUserVM.getPhone());
        user.setImei(managedUserVM.getImei());
        user.setUserImage(managedUserVM.getUserImage());
        user.setTenant(SecurityUtils.getCurrentUserTenant());
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }
    
    
    
public User createUser(Tenant tenant) {
    	
    	User user = new User();
        user.setLogin(tenant.getLoginName());
        user.setFirstName(tenant.getOrganization());
        user.setLastName(tenant.getOrganization());
        user.setEmail(tenant.getEmail());
       
        user.setFromTime(0);
        user.setToTime(24);
        user.setLangKey("en");
       /* if (managedUserVM.getLangKey() == null) {
            user.setLangKey("en"); // default language
        } else {
            user.setLangKey(managedUserVM.getLangKey());
        }
        if (managedUserVM.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            managedUserVM.getAuthorities().stream().forEach(
                authority -> authorities.add(authorityRepository.findOne(authority))
            );
            user.setAuthorities(authorities);
        }*/
        
        Set<Authority> authorities = new HashSet<>();
        Authority adminAuthority = authorityRepository.findOne(AuthoritiesConstants.USER_ADMIN);
          authorities.add(adminAuthority);
        Authority userAuthority =  authorityRepository.findOne(AuthoritiesConstants.USER);
          authorities.add(userAuthority);
        user.setAuthorities(authorities);
       /* if (managedUserVM.getGeofences() != null) {
            Set<Geofence> geofences = new HashSet<>();
            managedUserVM.getGeofences().stream().forEach(
                geofence -> geofences.add(geofenceRepository.findOneByName(geofence).get())
            );
            user.setGeofences(geofences);
        }*/
        
      /*  if (managedUserVM.getTrakeyeTypeAttributeValues() != null) {
            user.setTrakeyeTypeAttributeValues(managedUserVM.getTrakeyeTypeAttributeValues());
        }
        if (managedUserVM.getLastModifiedBy() != null){
        	user.setCreatedBy(managedUserVM.getLastModifiedBy());
        	user.setLastModifiedBy(managedUserVM.getLastModifiedBy());
        }*/
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date curdate = new Date();
		String date = sdf.format(curdate);
        String keyValue = user.getLogin()+"^0^"+Instant.now().toEpochMilli();
        keyValue = RandomUtil.convertStringToHex(keyValue);
        //keyValue= RandomUtil.getEncrypt(keyValue);
        //keyValue = URLEncoder.encode(keyValue, "UTF-8");
        user.setResetKey(keyValue);
        user.setResetDate(Instant.now().toEpochMilli());
        user.setActivated(true);
       // user.setTrakeyeType(managedUserVM.getTrakeyeType());
        user.setPhone(tenant.getPhone());
       // user.setImei(managedUserVM.getImei());
        //user.setUserImage(managedUserVM.getUserImage());
        user.setTenant(tenant);
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }
public void updateTenantUser(Tenant tenant) {
    Optional<User> user =userRepository.findOneByLogin(tenant.getLoginName());
    if(user.isPresent()){
    	user.get().setPhone(tenant.getPhone());
        User user1 = userRepository.save(user.get());
        log.debug("Changed Information for User: {}", user1);
    }  
   
}

    public void updateUser(String firstName, String lastName, String email, String langKey, int fromTime,int toTime, String phone, String imei, byte[] userImage) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setEmail(email);
            u.setLangKey(langKey);
            u.setFromTime(fromTime);
            u.setToTime(toTime);
            u.setPhone(phone);
            u.setImei(imei);
            u.setUserImage(userImage);
            userRepository.save(u);
            log.debug("Changed Information for User: {}", u);
        });
    }
    
    @Transactional
    public void updateUserAccount(String firstName, String lastName, String email) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setEmail(email);
            userRepository.save(u);
            log.debug("Changed Information for User: {}", u);
        });
    }
    
    @Transactional
    public void updateUser(Long id, String login, String firstName, String lastName, String email,boolean activated, String langKey, Set<String> authorities, Set<String> geofences, int fromTime, int toTime,TrakeyeType trakeyeType,Set<TrakeyeTypeAttributeValue> trakeyeTypeAttributeValues,
    		String phone, String imei, byte[] userImage) {

    	
    	 Optional<User> user = userRepository.findUserByLogin(login);
    	 if(user.isPresent()){
    		 User u = user.get();
    		 u.setFirstName(firstName);
             u.setLastName(lastName);
             u.setActivated(activated);
             u.setLangKey(langKey);
             u.setFromTime(fromTime);
             u.setToTime(toTime);
             u.setTrakeyeType(trakeyeType);
             u.setPhone(phone);
             u.setImei(imei);
             u.setUserImage(userImage);
             Set<TrakeyeTypeAttributeValue> trakeyeTypeAttributeValues2 = u.getTrakeyeTypeAttributeValues();
             trakeyeTypeAttributeValues2.clear();
             for (TrakeyeTypeAttributeValue trakeyeTypeAttributeValue : trakeyeTypeAttributeValues) {
            	 TrakeyeTypeAttributeValue save = trakeyeTypeAttributeValueRepository.save(trakeyeTypeAttributeValue);
            	 trakeyeTypeAttributeValues2.add(save);
			}
             u.setTrakeyeTypeAttributeValues(trakeyeTypeAttributeValues2);
             Set<Authority> managedAuthorities = u.getAuthorities();
             managedAuthorities.clear();
             authorities.stream().forEach(
                 authority -> managedAuthorities.add(authorityRepository.findOne(authority)));
             Set<Geofence> ugeofences = u.getGeofences();
             ugeofences.clear();
             
             for (String geofence : geofences) {
            	/* Optional<User> loggedinuser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
             	Optional<User> loggedinusersadmin = userRepository.findOneByLogin(loggedinuser.get().getCreatedBy());
				Optional<Geofence> fence = geofenceRepository.findOneByNameUserAndTenant(geofence,loggedinusersadmin.get().getId(), SecurityUtils.getCurrentUserTenant().getId());*/
            	 //geofenceRepository.findOne(Long.parseLong(geofence))
            	 Optional<Geofence> fence = geofenceRepository.findGeofenceByName(geofence);
            	 if(fence.isPresent()){
					ugeofences.add(fence.get());
				}
			}
             u.setGeofences(ugeofences);
             userRepository.save(u);
    	 }
     
    }
    
    @Transactional
    public void updateIMEI(Long id,String imei) {
        Optional<User> user = userRepository.findOneById(id);
    	 if(user.isPresent()){
    		 User u = user.get();
    		 u.setImei(imei);
            userRepository.save(u);
    	 }
    }
    
    @Transactional
    public void updateFcmToken(Long id,String fcmToken) {
        Optional<User> user = userRepository.findOneById(id);
    	 if(user.isPresent()){
    		 User u = user.get();
    		 u.setFcmToken(fcmToken);
            userRepository.save(u);
    	 }
    }
    
    @Transactional
    public void updateOsAndAppVerion(Long id,String operatingSystem, String applicationVersion) {
        Optional<User> user = userRepository.findOneById(id);
    	 if(user.isPresent()){
    		 User u = user.get();
    		 u.setOperatingSystem(operatingSystem);
    		 u.setApplicationVersion(applicationVersion);
            userRepository.save(u);
    	 }
    }
    
    @Transactional
    public User updateUserGpsStatus(String login, boolean gpsStatus) {
        Optional<User> user = userRepository.findOneByLogin(login);
    	 if(user.isPresent()){
    		 User u = user.get();
    		 u.setGpsStatus(gpsStatus);
            userRepository.save(u);
            log.debug("Changed GPS information for User: {}", u);
            return u;
    	 }
    	 return null;
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(u -> {
            userRepository.delete(u);
            log.debug("Deleted User: {}", u);
        });
    }

    public void changePassword(String password) {
    	userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
            String encryptedPassword = passwordEncoder.encode(password);
            u.setPassword(encryptedPassword);
            userRepository.save(u);
            log.debug("Changed password for User: {}", u);
        });
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneByLogin(login).map(u -> {
            u.getAuthorities().size();
            return u;
        });
    }
    
    @Transactional(readOnly = true)
    public UserUIDTO getUserUIDTOWithAuthoritiesByLogin(String login) {
          Optional<User> map = userRepository.findUserByLogin(login).map(u -> {
            u.getAuthorities().size();
            return u;
        });
          if(map.isPresent()){
      		return  userMapper.userToUserUIDTO(map.get());
      	}else{
      		return null;
      	}
    }
    
    @Transactional(readOnly = true)
    public List<UserUIDTO> getActivatedUsersByLogin() {
    	 List<User> listUsers = userRepository.findActivatedUsersByLogin();
         List<UserUIDTO> userUIDTOs=new ArrayList<UserUIDTO>();
         for(User user:listUsers){
       	  UserUIDTO userUIDTO=userMapper.userToUserUIDTO(user);
       	  log.debug(userUIDTO.toString());
       	  userUIDTOs.add(userUIDTO);
         }
         return userUIDTOs;
    }
    
    
    @Transactional(readOnly = true)
	public List<UserIdDTO> findAllUsersByName(String login) {
		List<User> listUsers = userRepository.findAllUsersByName(login);
		return userMapper.usersToUserSearchDTOs(listUsers);

	}
    @Transactional(readOnly = true)
   	public List<UserIdDTO> findAllUsersListByLogin() {
   		List<User> listUsers = userRepository.findAllByCreatedBy();
   		return userMapper.usersToUserSearchDTOs(listUsers);

   	}
    
    public Page<UserUIDTO> getActiveUsersByStatus(String status,Pageable pageable) {
    	Page<User> result = userRepository.getUsersByStatus(Instant.now().minusMillis(TimeUnit.MINUTES.toMillis(5)).toEpochMilli(),Instant.now().atZone(ZoneOffset.UTC).getHour(),status,pageable);
 		return result.map(user -> userMapper.userToUserUIDTO(user));
	}
    
    
   	public Page<UserUIDTO> getAllUsersByStatusAndSearchValue(String status, String searchText, Pageable pageable) {
   		log.debug("Api to find all users by status {} and search value {}", status, searchText);
   		Page<User> result = userRepository.getUsersForStausSearch(Instant.now().minusMillis(TimeUnit.MINUTES.toMillis(5)).toEpochMilli(),Instant.now().atZone(ZoneOffset.UTC).getHour(), status,searchText,pageable);
   		return result.map(user -> userMapper.userToUserUIDTO(user));
   		
   	}
    
    @Transactional(readOnly = true)
    public List<UserUIDTO> findUsersByLogin() {
          List<User> listUsers = userRepository.findUsersByLogin();
          List<UserUIDTO> userUIDTOs=new ArrayList<UserUIDTO>();
          for(User user:listUsers){
        	  UserUIDTO userUIDTO=userMapper.userToUserUIDTO(user);
        	  log.debug(userUIDTO.toString());
        	  userUIDTOs.add(userUIDTO);
          }
           //List<UserUIDTO> userDTOs=userMapper.usersToUserUIDTOs(listUsers);
            return userUIDTOs;
          
    }
    
    @Transactional(readOnly = true)
    public List<User> findAdminUsersByLogin() {
          List<User> listUsers = userRepository.findUsersByLogin();
            return listUsers;
    }
    
    @Transactional(readOnly = true)
    public UserDTO getUserByLogin(String login) {
        Optional<User> user = userRepository.findOneByLogin(login);
        	if(user.isPresent()){
        		return userMapper.userToUserDTO(user.get());
        	}else{
        		return null;
        	}
    }

    @Transactional(readOnly = true)
    public UserDTO getUserWithAuthorities(Long id) {
        User user = userRepository.findOne(id);
        user.getAuthorities().size(); // eagerly load the association
        return userMapper.userToUserDTO(user);
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        user.getAuthorities().size(); // eagerly load the association
        return user;
    }
    @Transactional(readOnly = true)
    public UserUIDTO getUserAccountWithAuthorities() {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        user.getAuthorities().size(); // eagerly load the association
        return userMapper.userToUserUIDTO(user);
    }


    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
//    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        long now = Instant.now().toEpochMilli();
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now-TimeUnit.DAYS.toMillis(3));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
        }
    }
    
    @Transactional(readOnly=true)
    public User findOne(Long id){
    	 log.debug("Request to get User : {}", id);
         User user = userRepository.findOne(id);
         return user;
    }
    
    @Transactional(readOnly=true)
    public boolean findUserByIMEI(String imei){
    	 log.debug("Request to get User : {}", imei);
         Optional<User> user = userRepository.findOneByImei(imei);
         if(user.isPresent()){
     		return true;
     	}else{
     		return false;
     	}
    }


    @Transactional
    public void updateUserWithCollabJwtToken(Long id,String collabJwtToken) {
        Optional<User> user = userRepository.findOneById(id);
    	 if(user.isPresent()){
    		 User u = user.get();
    		 u.setCollabJwtToken(collabJwtToken);
            userRepository.save(u);
    	 }
    }
    
   

}
