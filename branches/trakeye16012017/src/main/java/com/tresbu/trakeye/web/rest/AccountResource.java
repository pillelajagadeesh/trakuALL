package com.tresbu.trakeye.web.rest;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.tresbu.trakeye.config.TrakEyeProperties;
import com.tresbu.trakeye.domain.Authority;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.repository.UserRepository;
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.security.SecurityUtils;
import com.tresbu.trakeye.service.MailService;
import com.tresbu.trakeye.service.SMSService;
import com.tresbu.trakeye.service.UserService;
import com.tresbu.trakeye.service.dto.CollabUserDTO;
import com.tresbu.trakeye.service.dto.UserAccountUpdateDTO;
import com.tresbu.trakeye.service.dto.UserDTO;
import com.tresbu.trakeye.service.dto.UserUIDTO;
import com.tresbu.trakeye.service.mapper.UserMapper;
import com.tresbu.trakeye.service.util.CollabUtil;
import com.tresbu.trakeye.service.util.RandomUtil;
import com.tresbu.trakeye.web.rest.util.FileUploadUtil;
import com.tresbu.trakeye.web.rest.util.HeaderUtil;
import com.tresbu.trakeye.web.rest.vm.KeyAndPasswordVM;
import com.tresbu.trakeye.web.rest.vm.ManagedUserVM;

import io.swagger.annotations.ApiOperation;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource extends BaseResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);
    
    @Autowired
	MessageSource messageSource;

    @Inject
    private UserRepository userRepository;
    
    @Inject
    private TrakEyeProperties jHipsterProperties;

    @Inject
    private UserService userService;
    
    @Inject
    private UserMapper userMapper;

    @Inject
    private MailService mailService;
    
    @Inject
    private SMSService smsService;

    

    /**
     * GET  /activate : activate the registered user.
     *
     * @param key the activation key
     * @return the ResponseEntity with status 200 (OK) and the activated user in body, or status 500 (Internal Server Error) if the user couldn't be activated
     */
    @RequestMapping(value = "/activate",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @ApiOperation(value = "Activate user account", notes = "User can activate his account based on key. User with role SuperAdmin, User Admin and User is allowed. ", response = String.class)
    public ResponseEntity<String> activateAccount(@RequestParam(value = "key") String key) {
        return userService.activateRegistration(key)
            .map(user -> new ResponseEntity<String>(HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the login if the user is authenticated
     */
    @RequestMapping(value = "/authenticate",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @ApiOperation(value = "Authenticate user account", notes = "User can authenticate his account. User with role SuperAdmin, User Admin and User is allowed. ", response = String.class)
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account : get the current user.
     *
     * @return the ResponseEntity with status 200 (OK) and the current user in body, or status 500 (Internal Server Error) if the user couldn't be returned
     */
    @RequestMapping(value = "/account",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @ApiOperation(value = "Get user account", notes = "User can get user account based on login name. User with role SuperAdmin, User Admin and User is allowed. ", response = UserUIDTO.class)
    public ResponseEntity<?> getAccount() {
    	
        return Optional.ofNullable(userService.getUserAccountWithAuthorities())
            .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /account : update the current user information.
     *
     * @param userDTO the current user information
     * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request) or 500 (Internal Server Error) if the user couldn't be updated
     */
    @RequestMapping(value = "/account",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @ApiOperation(value = "Update user account", notes = "User can update his own user account details based on login name. User with role SuperAdmin, User Admin and User is allowed. ", response = String.class)
    public ResponseEntity<String> saveAccount(@Valid @RequestBody UserAccountUpdateDTO userUIDTO) {
        User existingUser = getCurrentUser();
        if (existingUser == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("user-management", "emailexists", "Email already in use")).body(null);
        }
        return userRepository
            .findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {
                userService.updateUserAccount(userUIDTO.getFirstName(), userUIDTO.getLastName(), userUIDTO.getEmail());
                
             // call collab api's only if collabEnabled is true
                if(jHipsterProperties.getCollab().isCollabEnabled()){
                Optional<User> loggedInUserForCollab =userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
                if(loggedInUserForCollab.isPresent()){
                	String collabJwtToken = loggedInUserForCollab.get().getCollabJwtToken();
                	if(collabJwtToken != null){
                     	// update collab user account
                            ResponseEntity<CollabUserDTO> collabUser= CollabUtil.collabAccountUpdate(jHipsterProperties.getCollab().getCollabBaseUrl(), collabJwtToken, 
                            		userUIDTO.getFirstName(), userUIDTO.getLastName(), userUIDTO.getEmail(), userUIDTO.getLogin());
                        }
                }
                }
                
                
                return new ResponseEntity<String>(HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /account/change_password : changes the current user's password
     *
     * @param password the new password
     * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request) if the new password is not strong enough
     */
    @RequestMapping(value = "/account/change_password",
        method = RequestMethod.POST,
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    @ApiOperation(value = "Update user password", notes = "User can update his own password based on login name. User with role SuperAdmin, User Admin and User is allowed. ", response = String.class)
    public ResponseEntity<?> changePassword(@RequestBody String password) {
        if (!checkPasswordLength(password)) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        userService.changePassword(password);
        
     // call collab api's only if collabEnabled is true
        User loggedInuser=getCurrentUser();
        if(loggedInuser != null){
        	Set<Authority> authorities = loggedInuser.getAuthorities();
    		Set<String> collect = authorities.stream().map(Authority::getName).collect(Collectors.toSet());
    		//User admin = user;
    		if (!collect.contains(AuthoritiesConstants.SUPER_ADMIN)) {
    			 if(jHipsterProperties.getCollab().isCollabEnabled()){
    			        Optional<User> loggedInUserForCollab =userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
    			        if(loggedInUserForCollab.isPresent()){
    			        	String collabJwtToken = loggedInUserForCollab.get().getCollabJwtToken();
    			        	if(collabJwtToken != null){
    			             	// update collab user account
    			                    ResponseEntity<CollabUserDTO> collabResponse= CollabUtil.collabChangePassword(jHipsterProperties.getCollab().getCollabBaseUrl(), collabJwtToken,password);
    			                }
    			        }
    			        }
    		}
        }
        
       
      
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST   /account/reset_password/init : Send an e-mail to reset the password of the user
     *
     * @param mail the mail of the user
     * @param request the HTTP request
     * @return the ResponseEntity with status 200 (OK) if the e-mail was sent, or status 400 (Bad Request) if the e-mail address is not registered
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    @RequestMapping(value = "/account/reset_password/init",
        method = RequestMethod.POST,
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    @ApiOperation(value = "Requestto reset password", notes = "User can request to reset his own password based on login name. User with role SuperAdmin, User Admin and User is allowed. ", response = String.class)
    public ResponseEntity<?> requestPasswordReset(@RequestBody String login, HttpServletRequest request) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
    	Optional<User> userObj = userRepository.findOneByLogin(login);
    	if(!userRepository.findOneByLogin(login).isPresent()){
    		log.info("User name is not registered");
    		//return new ResponseEntity<>("username not registered", HttpStatus.BAD_REQUEST);
    		return ResponseEntity.badRequest()
		            .headers(HeaderUtil.createFailureAlert("reset","usernamenotregistered", ""))
		            .body(null);
    	}
    		if(userObj.get().getActivated() == false){
    			log.info("User is deactivated");
    			//return new ResponseEntity<>("user is deactivated", HttpStatus.BAD_REQUEST);
    			return ResponseEntity.badRequest()
    		            .headers(HeaderUtil.createFailureAlert("reset","userdeactivated", ""))
    		            .body(null);
    		}
    		if(StringUtils.isEmpty(userObj.get().getResetKey()) || userObj.get().getResetKey() == null){
    			log.info("Reset key is null. So starting count from 1");
    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        		Date curdate = new Date();
        		String date = sdf.format(curdate);
    			String keyValue=null;
    			keyValue = userObj.get().getLogin()+"^1^"+Instant.now().toEpochMilli();
    			keyValue = RandomUtil.convertStringToHex(keyValue);
    			//keyValue= RandomUtil.getEncrypt(keyValue);
    			UserUIDTO userUIDTO= userService.requestPasswordReset(login, keyValue);
    			if(userUIDTO == null){
    				return ResponseEntity.badRequest()
    			            .headers(HeaderUtil.createFailureAlert("reset","usernamenotregistered", ""))
    			            .body(null);
    			}
    			
    			// call collab api's only if collabEnabled is true
                if(jHipsterProperties.getCollab().isCollabEnabled()){
    		     //collab admin login
    		        String collabAdminJwtToken = CollabUtil.collabAdminAuthentication(jHipsterProperties.getCollab().getCollabBaseUrl(), jHipsterProperties.getCollab().getCollabSuperAdminLogin(), jHipsterProperties.getCollab().getCollabSuperAdminPassword());
    		        if(collabAdminJwtToken != null){
    		        	// find collab user by login
        		        ResponseEntity<CollabUserDTO> collabUser= CollabUtil.findCollabUserByLogin(jHipsterProperties.getCollab().getCollabBaseUrl(), collabAdminJwtToken, login);
        		        if(collabUser != null){
        		        	 if(collabUser.getBody() != null){
        		        	// update collab user
            		        String collabForgotPwd= CollabUtil.collabForgotPassword(jHipsterProperties.getCollab().getCollabBaseUrl(), collabUser.getBody().getEmail());
        		        	 }
        		        }
    		        }
                }
    		        
    			String baseUrl = request.getScheme() +
                        "://" +
                        request.getServerName() +
                        ":" +
                        request.getServerPort() +
                        request.getContextPath();
    			   // UserUIDTO userUIDTO=userMapper.userToUserUIDTO(user.get());
                    mailService.sendPasswordResetMail(userUIDTO, baseUrl);// here
                    String message=messageSource.getMessage("sms.resetpassword.text1", new String[]{""+userUIDTO.getLogin(),""+baseUrl,""+userUIDTO.getResetKey()}, new Locale("en", "US"));
                    smsService.sendSMS(userObj.get().getPhone(), message);
                   // return new ResponseEntity<>("e-mail was sent", HttpStatus.OK);
                    return ResponseEntity.ok()
        		            .headers(HeaderUtil.createAlert("resetlinksent",""))
        		            .body(null);
    			
    		} else{
    			log.info("Reset key is not null");
    		//String[] decryptedKey = RandomUtil.getDecrypt(userObj.get().getResetKey());
    		String hex= RandomUtil.convertHexToString(userObj.get().getResetKey());
    		String[] keyArray= hex.split("\\^");
    		//String[] keyArray= userObj.get().getResetKey().split("\\^");
    		
    		String dateInKey = keyArray[2];
    		
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		Date curdate = new Date();
    		String date = sdf.format(curdate);
    		int emailCount = Integer.parseInt(keyArray[1]);
    		if(emailCount == 0){
    			log.info("Sending creation email again, as user has not yet set the password for the first time");
    			UserUIDTO userUIDTO=userService.requestPasswordReset(login, userObj.get().getResetKey());
    			if(userUIDTO == null){
    		     return ResponseEntity.badRequest()
  		            .headers(HeaderUtil.createFailureAlert("reset","usernamenotregistered", ""))
  		            .body(null);
    			}
    			
    			
    			// call collab api's only if collabEnabled is true
                if(jHipsterProperties.getCollab().isCollabEnabled()){
    			 //collab admin login
		        String collabAdminJwtToken = CollabUtil.collabAdminAuthentication(jHipsterProperties.getCollab().getCollabBaseUrl(), jHipsterProperties.getCollab().getCollabSuperAdminLogin(), jHipsterProperties.getCollab().getCollabSuperAdminPassword());
		        if(collabAdminJwtToken != null){
		        	// find collab user by login
			        ResponseEntity<CollabUserDTO> collabUser= CollabUtil.findCollabUserByLogin(jHipsterProperties.getCollab().getCollabBaseUrl(), collabAdminJwtToken, login);
			        if(collabUser != null){
			        	if(collabUser.getBody() != null){
			        	// update collab user
				        String collabForgotPwd= CollabUtil.collabForgotPassword(jHipsterProperties.getCollab().getCollabBaseUrl(), collabUser.getBody().getEmail());
			        	}
			        }
		        }
                }
		     
		     
    			String baseUrl = request.getScheme() + 
    		            "://" +                     
    		            request.getServerName() +            
    		            ":" +                            
    		            request.getServerPort() +             
    		            request.getContextPath();
    			//UserUIDTO userUIObj=userMapper.userToUserUIDTO(newUser.get()); // here
    		           // mailService.sendCreationEmail(userUIDTO, baseUrl);
    			
    			 mailService.sendCreationEmail(userObj.get());
    		            String message=messageSource.getMessage("sms.usercreation.text1", new String[]{""+userUIDTO.getLogin(),""+baseUrl,""+userUIDTO.getResetKey()}, new Locale("en", "US"));
    		            smsService.sendSMS(userObj.get().getPhone(), message);
    		           // return new ResponseEntity<>("e-mail was sent", HttpStatus.OK);
    		            return ResponseEntity.ok()
            		            .headers(HeaderUtil.createAlert("setlinksent",""))
            		            .body(null);
    		}
    		
    		Long millisec= Instant.now().toEpochMilli() - Long.parseLong(dateInKey);
            int hours = (int) (millisec / 3600000);
            if(hours < 24){
            	log.info("Requesting password withing 24 hours, So checking the count");
    			int count = Integer.parseInt(keyArray[1]);
            	if(count >=5){
            		log.info("Count is equals to 5 or more than 5");
            		//return new ResponseEntity<>("Reset password count exceeded for the day", HttpStatus.BAD_REQUEST);
            		Date keydate=new Date(Long.parseLong(dateInKey)+(3600000*24));// to get time after 24 hours
                    SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                    String dateText = df2.format(keydate);
                    
            		 return ResponseEntity.badRequest()
            		            .headers(HeaderUtil.createFailureAlert("reset","countExceeded", dateText))
            		            .body(null);
            	} else {
            		log.info("Count is less than 5, so adding 1 to exsting count. and the current count is {}",count);
            		String keyValue=null;
            		count = Integer.parseInt(keyArray[1])+1;
        			keyValue = userObj.get().getLogin()+"^"+count+"^"+dateInKey;
        			// whenever any other encryption type is added here, add same thing in below else part, in above if for old users and also in createUser method in userservice.
        			keyValue = RandomUtil.convertStringToHex(keyValue);
        			//keyValue= RandomUtil.getEncrypt(keyValue);
        			UserUIDTO userUIDTO= userService.requestPasswordReset(login, keyValue);
        			if(userUIDTO == null){
           		     return ResponseEntity.badRequest()
         		            .headers(HeaderUtil.createFailureAlert("reset","usernamenotregistered", ""))
         		            .body(null);
           			}
        			
        			// call collab api's only if collabEnabled is true
                    if(jHipsterProperties.getCollab().isCollabEnabled()){
        			 //collab admin login
    		        String collabAdminJwtToken = CollabUtil.collabAdminAuthentication(jHipsterProperties.getCollab().getCollabBaseUrl(), jHipsterProperties.getCollab().getCollabSuperAdminLogin(), jHipsterProperties.getCollab().getCollabSuperAdminPassword());
    		        if(collabAdminJwtToken !=null){
    		        	// find collab user by login
        		        ResponseEntity<CollabUserDTO> collabUser= CollabUtil.findCollabUserByLogin(jHipsterProperties.getCollab().getCollabBaseUrl(), collabAdminJwtToken, login);
        		        if(collabUser != null){
        		        	 if(collabUser.getBody() != null){
        		        	 // update collab user
            		        String collabForgotPwd= CollabUtil.collabForgotPassword(jHipsterProperties.getCollab().getCollabBaseUrl(), collabUser.getBody().getEmail());
        		        	 }
        		        }
    		        }
                    }
    		     
    		        String baseUrl = request.getScheme() +
                            "://" +
                            request.getServerName() +
                            ":" +
                            request.getServerPort() +
                            request.getContextPath();
        			    //UserUIDTO userUIDTO=userMapper.userToUserUIDTO(user.get()); // here
                        mailService.sendPasswordResetMail(userUIDTO, baseUrl);
                        String message=messageSource.getMessage("sms.resetpassword.text1", new String[]{""+userUIDTO.getLogin(),""+baseUrl,""+userUIDTO.getResetKey()}, new Locale("en", "US"));
                        smsService.sendSMS(userObj.get().getPhone(), message);
                        //return new ResponseEntity<>("e-mail was sent", HttpStatus.OK);
                        return ResponseEntity.ok()
            		            .headers(HeaderUtil.createAlert("resetlinksent",""))
            		            .body(null);
            	}
    		} else{
    			log.info("Requesting forgot password after 24 hours. So again starting the count from 1");
    			String keyValue=null;
    			keyValue = userObj.get().getLogin()+"^1^"+Instant.now().toEpochMilli();
    			keyValue = RandomUtil.convertStringToHex(keyValue);
    			//keyValue= RandomUtil.getEncrypt(keyValue);
    			UserUIDTO userUIDTO= userService.requestPasswordReset(login, keyValue);
    			if(userUIDTO == null){
          		     return ResponseEntity.badRequest()
        		            .headers(HeaderUtil.createFailureAlert("reset","usernamenotregistered", ""))
        		            .body(null);
          			}
    			
    			// call collab api's only if collabEnabled is true
                if(jHipsterProperties.getCollab().isCollabEnabled()){
    			 //collab admin login
		        String collabAdminJwtToken = CollabUtil.collabAdminAuthentication(jHipsterProperties.getCollab().getCollabBaseUrl(), jHipsterProperties.getCollab().getCollabSuperAdminLogin(), jHipsterProperties.getCollab().getCollabSuperAdminPassword());
		        if(collabAdminJwtToken != null){
		        	// find collab user by login
			        ResponseEntity<CollabUserDTO> collabUser= CollabUtil.findCollabUserByLogin(jHipsterProperties.getCollab().getCollabBaseUrl(), collabAdminJwtToken, login);
			        if(collabUser !=null){
			        	if(collabUser.getBody() !=null){
			        	 // update collab user
				        String collabForgotPwd= CollabUtil.collabForgotPassword(jHipsterProperties.getCollab().getCollabBaseUrl(), collabUser.getBody().getEmail());
			        	}
			        }
		        }
                }
		     
		        String baseUrl = request.getScheme() +
                        "://" +
                        request.getServerName() +
                        ":" +
                        request.getServerPort() +
                        request.getContextPath();
    			//UserUIDTO userUIDTO=userMapper.userToUserUIDTO(user.get());// here
                    mailService.sendPasswordResetMail(userUIDTO, baseUrl);
                    String message=messageSource.getMessage("sms.resetpassword.text1", new String[]{""+userUIDTO.getLogin(),""+baseUrl,""+userUIDTO.getResetKey()}, new Locale("en", "US"));
                    smsService.sendSMS(userObj.get().getPhone(), message);
                    return ResponseEntity.ok()
        		            .headers(HeaderUtil.createAlert("resetlinksent",""))
        		            .body(null);
    		}
    	}	
    }

    /**
     * POST   /account/reset_password/finish : Finish to reset the password of the user
     *
     * @param keyAndPassword the generated key and the new password
     * @return the ResponseEntity with status 200 (OK) if the password has been reset,
     * or status 400 (Bad Request) or 500 (Internal Server Error) if the password could not be reset
     * @throws UnsupportedEncodingException 
     * @throws URISyntaxException 
     * @throws DecoderException 
     */
    @RequestMapping(value = "/account/reset_password/finish",
        method = RequestMethod.POST,
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    @ApiOperation(value = "Reset password", notes = "User can reset his own password based on rest key that has been sentin the email. User with role SuperAdmin, User Admin and User is allowed. ", response = String.class)
    public ResponseEntity<String> finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword,HttpServletRequest request) throws UnsupportedEncodingException {
    	log.info("Rest API to reset the password");
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
           // return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
            return ResponseEntity.badRequest()
		            .headers(HeaderUtil.createFailureAlert("reset","incorrectpwd", ""))
		            .body(null);
        }
        //String result = URLDecoder.decode(keyAndPassword.getKey(), "UTF-8");
        Optional<User> userObj = userRepository.findOneByResetKey(keyAndPassword.getKey());
        if(!userObj.isPresent() || StringUtils.isEmpty(keyAndPassword.getKey()) || keyAndPassword.getKey() == null){
        	log.info("Email link invalid. No user found with this reset  key");
        	//return new ResponseEntity<>("email link invalid", HttpStatus.BAD_REQUEST);
        	return ResponseEntity.badRequest()
		            .headers(HeaderUtil.createFailureAlert("reset","emaillinkinvalid", ""))
		            .body(null);
        }
        
        String hex= RandomUtil.convertHexToString(keyAndPassword.getKey());
		String[] keyArray= hex.split("\\^");
		int count = Integer.parseInt(keyArray[1]);
		
       if(count == 0){
    	   log.info("Count is 0. Setting the password for the first time");
    	   UserUIDTO user= userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());
    	   
    	  
    	   if(user == null){
    		   log.info("User is null. Password link is older than 24 hours");
    		   //new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    		   return ResponseEntity.badRequest()
   		            .headers(HeaderUtil.createFailureAlert("reset","pwdsetlinkvalid", ""))
   		            .body(null);
    	   }
    	   
    	// call collab api's only if collabEnabled is true
           if(jHipsterProperties.getCollab().isCollabEnabled()){
    	//collab admin login
           String collabAdminJwtToken = CollabUtil.collabAdminAuthentication(jHipsterProperties.getCollab().getCollabBaseUrl(), jHipsterProperties.getCollab().getCollabSuperAdminLogin(), jHipsterProperties.getCollab().getCollabSuperAdminPassword());
           if(collabAdminJwtToken != null){
        	   // find collab user by login
               ResponseEntity<CollabUserDTO> collabUser= CollabUtil.findCollabUserByLogin(jHipsterProperties.getCollab().getCollabBaseUrl(),collabAdminJwtToken,user.getLogin());
               if(collabUser != null){
            	   if(collabUser.getBody() != null){
            	// reset password for collab tenant
                   String collabPwdReset= CollabUtil.collabPasswordReset(jHipsterProperties.getCollab().getCollabBaseUrl(),keyAndPassword.getNewPassword(), collabUser.getBody().getResetKey());
            	   }
               }
           }
           }
    	  
        	//return userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey())
                  //  .map(user -> {
			Optional<User> adminopt = userRepository.findOneByLogin(userObj.get().getCreatedBy());
			User admin = adminopt.get();

			String directory = null;
			String userCreatedBy = user.getCreatedBy();
			if (user.getAuthorities().contains(AuthoritiesConstants.USER)) {
				directory = "agents";
			} else if (user.getAuthorities().contains(AuthoritiesConstants.USER_ADMIN)) {
				directory = "admin";
			}
			String androidFileName = FileUploadUtil.getFileName("android", admin.getLogin(), directory);

			String iosFileName = FileUploadUtil.getFileName("ios", admin.getLogin(), directory);
			 
			if (androidFileName == null && iosFileName==null) {
				log.debug("APK is not available.");
				// return new ResponseEntity<>("password set successfully",HttpStatus.OK);
				return ResponseEntity.ok()
						.headers(HeaderUtil.createAlert("pwdsetsuccess",""))
						.body(null);
			}
			
			String baseUrl =request.getScheme() + // "http"
    	                    "://" +                                // "://"
    	                    request.getServerName() +              // "myhost"
    	                    "/custom/"+userCreatedBy+"/agents/android/"+androidFileName; 
                	
                
            String baseIosUrl = request.getScheme() + // "http"
  	                    "://" +                                // "://"
  	                    request.getServerName() +              // "myhost"
  	                    "/custom/"+userCreatedBy+"/agents/ios/"+iosFileName; 
            mailService.sendDownloadLinkMail(user, baseUrl,baseIosUrl);
            String message=messageSource.getMessage("sms.apklink.text1", new String[]{""+user.getLogin(),""+baseUrl,""+baseIosUrl}, new Locale("en", "US"));
            smsService.sendSMS(user.getPhone(), message);
                  	
                  	
                  	  //return new ResponseEntity<>("e-mail was sent",HttpStatus.OK);
                  	 return ResponseEntity.ok()
           		            .headers(HeaderUtil.createAlert("pwdsetsuccess",""))
           		            .body(null);
                // })
                    //.orElse(
                    		
        }else{
        	log.info("Resetting password after doing forgot password");
        	UserUIDTO user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());
        	if(user == null){
        		log.info("User is null.Password link is older than 24 hours");
        		//return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        		return ResponseEntity.badRequest()
       		            .headers(HeaderUtil.createFailureAlert("reset","pwdresetlinkvalid", ""))
       		            .body(null);
        	}
        	
        	// call collab api's only if collabEnabled is true
            if(jHipsterProperties.getCollab().isCollabEnabled()){
        	 //collab admin login
            String collabAdminJwtToken = CollabUtil.collabAdminAuthentication(jHipsterProperties.getCollab().getCollabBaseUrl(), jHipsterProperties.getCollab().getCollabSuperAdminLogin(), jHipsterProperties.getCollab().getCollabSuperAdminPassword());
            if(collabAdminJwtToken !=null){
            	// find collab user by login
                ResponseEntity<CollabUserDTO> collabUser= CollabUtil.findCollabUserByLogin(jHipsterProperties.getCollab().getCollabBaseUrl(),collabAdminJwtToken,user.getLogin());
                if(collabUser != null){
                	if(collabUser.getBody() != null){
                	// reset password for collab tenant
                    String collabPwdReset= CollabUtil.collabPasswordReset(jHipsterProperties.getCollab().getCollabBaseUrl(),keyAndPassword.getNewPassword(), collabUser.getBody().getResetKey());
                	}
                }
            }
            }
     	   
           
        	//return new ResponseEntity<>("password reset successfully",HttpStatus.OK);
        	 return ResponseEntity.ok()
   		            .headers(HeaderUtil.createAlert("pwdresetsuccess",""))
   		            .body(null);
        	/*return userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey())
                    .map(user -> {
                  	 
                  	  return new ResponseEntity<>("password reset successfully",HttpStatus.OK);
                 }).orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));*/
        }
       
        
    }
    
    /*
     * Verify user account as soon as email link is clicked
     */
    @RequestMapping(value = "/account/verify",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
        @Timed
        @ApiOperation(value = "Verify user account", notes = "User can verify his own user account details based on reset key. User with role SuperAdmin, User Admin and User is allowed. ", response = boolean.class)
        public ResponseEntity<?> verifyAcount(@RequestBody String resetKey) {
    	log.info("Rest API to verify account");
    	
            if (resetKey == null || resetKey=="" || StringUtils.isEmpty(resetKey)) {
            	log.info("Reset key is null");
                //return new ResponseEntity<>("Reset key is null", HttpStatus.BAD_REQUEST);
            	 return ResponseEntity.badRequest()
        		            .headers(HeaderUtil.createFailureAlert("reset","resetkeynull", ""))
        		            .body(null);
            }
            Optional<User> userObj = userRepository.findOneByResetKey(resetKey);
            if(!userObj.isPresent()){
            	log.info("User not found with this key");
            	 return ResponseEntity.ok()
                         .headers(HeaderUtil.createAlert("user not found with the key", resetKey))
                         .body(null);
            }
           
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createAlert("user found with the key", resetKey))
                    .body("true");
        }

    private boolean checkPasswordLength(String password) {
        return (!StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH);
    }
}
