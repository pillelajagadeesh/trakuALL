package com.tresbu.trakeye.web.rest;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.tresbu.trakeye.config.Constants;
import com.tresbu.trakeye.config.TrakEyeProperties;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.repository.UserRepository;
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.security.SecurityUtils;
import com.tresbu.trakeye.service.MailService;
import com.tresbu.trakeye.service.SMSService;
import com.tresbu.trakeye.service.UserService;
import com.tresbu.trakeye.service.dto.CollabTenantDTO;
import com.tresbu.trakeye.service.dto.CollabUserDTO;
import com.tresbu.trakeye.service.dto.UpdateUserGpsDTO;
import com.tresbu.trakeye.service.dto.UserDTO;
import com.tresbu.trakeye.service.dto.UserIdDTO;
import com.tresbu.trakeye.service.dto.UserUIDTO;
import com.tresbu.trakeye.service.mapper.UserMapper;
import com.tresbu.trakeye.service.util.CollabUtil;
import com.tresbu.trakeye.web.rest.util.HeaderUtil;
import com.tresbu.trakeye.web.rest.util.PaginationUtil;
import com.tresbu.trakeye.web.rest.vm.ManagedUserVM;

import io.swagger.annotations.ApiOperation;

/**
 * REST controller for managing users.
 *
 * <p>This class accesses the User entity, and needs to fetch its collection of authorities.</p>
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * </p>
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>Another option would be to have a specific JPA entity graph to handle this case.</p>
 */
@RestController
@RequestMapping("/api")
public class UserResource extends BaseResource{

    private final Logger log = LoggerFactory.getLogger(UserResource.class);
    
    @Autowired
	MessageSource messageSource;

    @Inject
    private TrakEyeProperties jHipsterProperties;
    
    @Inject
    private UserRepository userRepository;

    @Inject
    private MailService mailService;

    @Inject
    private UserService userService;
    
    @Inject
    private UserMapper userMapper;
    
    @Inject
    private SMSService smsService;
    
   

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     * </p>
     *
     * @param managedUserVM the user to create
     * @param request the HTTP request
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     * @throws IOException 
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create new customer or user", notes = "User can create a new customer or agent. User with role Super Admin and User Admin is allowed."
   		+ "User with role Super Admin can create customers and user with role User Admin can create agents", response = User.class)
	@Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    public ResponseEntity<?> createUser(@RequestBody ManagedUserVM managedUserVM, HttpServletRequest request) throws URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {

        log.debug("REST request to save User : {}", managedUserVM);
/*        ResponseEntity<UserDTO> validateCreateAuthority = validateCreateAuthority(managedUserVM);
        if(validateCreateAuthority!=null){
        	return validateCreateAuthority;
        }*/
      
        //Lowercase the user login before comparing with database
        if (userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "Login already in use"))
                .body(null);
        }
        if(managedUserVM.getImei() != null && managedUserVM.getImei() != "" ){
        if (userRepository.findOneByImei(managedUserVM.getImei()).isPresent()) {
        	//Optional<User> dbUser= userRepository.findOneByImei(managedUserVM.getImei());
        	//if(dbUser.get().getActivated()){
        		return ResponseEntity.badRequest()
                        .headers(HeaderUtil.createFailureAlert("userManagement", "imeiexists", "IMEI no already used"))
                        .body(null);
        	//}
            
        }
        }
        BufferedImage img = null;
        if(managedUserVM.getUserImage()!=null){
        img = ImageIO.read(new ByteArrayInputStream(managedUserVM.getUserImage()));                
        }
        
        if(managedUserVM.getUserImage()!=null){
        	if(managedUserVM.getUserImage().length > 40000 || img.getWidth() > 192 || img.getHeight() > 192){
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("userManagement", "imagesizeexides", "Image size should be lessthan 4KB and should be of 192 x 192"))
                    .body(null);
        	}

        }/* else if (userRepository.findOneByEmail(managedUserVM.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "Email already in use"))
                .body(null);
        }*/ 
            User newUser = userService.createUser(managedUserVM);
           
         // call collab api's only if collabEnabled is true
            if(jHipsterProperties.getCollab().isCollabEnabled()){
            Optional<User> loggedInUserForCollab =userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
            if(loggedInUserForCollab.isPresent()){
            	String collabJwtToken = loggedInUserForCollab.get().getCollabJwtToken();
            	 if(collabJwtToken!= null){
                 	// create collab user
                     ResponseEntity<CollabUserDTO> collabUser= CollabUtil.collabUserCreate(jHipsterProperties.getCollab().getCollabBaseUrl(), collabJwtToken, newUser);
                 }
            }
            }
          
           
         /* Set<String> current= getCurrentUserAuthorities();
            
            if(current.contains(AuthoritiesConstants.SUPER_ADMIN))
            {
            	createGeofence(newUser.getId());
            	createCaseType(newUser.getId());
            	createTrakeyeType(newUser.getId());
            	createServiceType(newUser.getId());
            
            }

            String baseUrl = request.getScheme() + // "http"
            "://" +                                // "://"
            request.getServerName() +              // "myhost"
            ":" +                                  // ":"
            request.getServerPort() +              // "80"
            request.getContextPath();              // "/myContextPath" or "" if deployed in root context
            UserUIDTO userUIDTO= userMapper.userToUserUIDTO(newUser);
            mailService.sendCreationEmail(userUIDTO, baseUrl);*/
            mailService.sendCreationEmail(newUser);
           // String message=messageSource.getMessage("sms.usercreation.text1", new String[]{""+userUIDTO.getLogin(),""+baseUrl,""+userUIDTO.getResetKey()}, new Locale("en", "US"));
           // smsService.sendSMS(newUser.getPhone(), message);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert( "userManagement.created", newUser.getLogin()))
                .body(newUser);
    }

    /**
     * PUT  /users : Updates an existing User.
     *
     * @param managedUserVM the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the login or email is already in use,
     * or with status 500 (Internal Server Error) if the user couldn't be updated
     * @throws IOException 
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update existing customer or user", notes = "User can update existing customer or agent. User with role Super Admin and User Admin is allowed."
       		+ "User with role Super Admin can update customers and user with role User Admin can update agents", response = UserDTO.class)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    public ResponseEntity<UserDTO> updateUser(@RequestBody ManagedUserVM managedUserVM) throws IOException {
        log.debug("REST request to update User : {}", managedUserVM);
       /* ResponseEntity<UserDTO> validateCreateAuthority = validateCreateAuthority(managedUserVM);
        if(validateCreateAuthority!=null){
        	return validateCreateAuthority;
        }*/
    /*    Optional<User> existingUser = userRepository.findOneByEmail(managedUserVM.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "E-mail already in use")).body(null);
        }
        existingUser = userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "Login already in use")).body(null);
        }*/
        //if(existingUser.get().getActivated() != managedUserVM.isActivated())
      //  {
        	 if(managedUserVM.getImei() != null && managedUserVM.getImei() != ""){
        	Optional<User> usersByImei= userRepository.findOneByImei(managedUserVM.getImei());
        
        	if (usersByImei.isPresent()) {
        		if(!usersByImei.get().getLogin().equals(managedUserVM.getLogin())){
        			return ResponseEntity.badRequest()
                            .headers(HeaderUtil.createFailureAlert("userManagement", "imeiexists", "IMEI no already used"))
                            .body(null);
        		}
                
            }
        	 }
       // }
        
        BufferedImage img = null;
        if(managedUserVM.getUserImage()!=null){
        img = ImageIO.read(new ByteArrayInputStream(managedUserVM.getUserImage()));                
        }
        
        if(managedUserVM.getUserImage()!=null){
        	if(managedUserVM.getUserImage().length > 40000 || img.getWidth() > 192 || img.getHeight() > 192){
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("userManagement", "imagesizeexides", "Image size should be lessthan 4KB and should be of 192 x 192"))
                    .body(null);
        	}

        }
       userService.updateUser(managedUserVM.getId(), managedUserVM.getLogin(), managedUserVM.getFirstName(),
            managedUserVM.getLastName(), managedUserVM.getEmail(), managedUserVM.isActivated(),
            managedUserVM.getLangKey(), managedUserVM.getAuthorities(),managedUserVM.getGeofences(),managedUserVM.getFromTime(), 
            managedUserVM.getToTime(),managedUserVM.getTrakeyeType(),managedUserVM.getTrakeyeTypeAttributeValues(),managedUserVM.getPhone(),managedUserVM.getImei(),
            managedUserVM.getUserImage());
       
       Optional<User> user = userRepository.findOneById(managedUserVM.getId());
       
    // call collab api's only if collabEnabled is true
       if(jHipsterProperties.getCollab().isCollabEnabled()){
       Optional<User> loggedInUserForCollab =userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
       if(loggedInUserForCollab != null){
    	   String collabJwtToken = loggedInUserForCollab.get().getCollabJwtToken();
    	   if(collabJwtToken != null){
    	    	// find collab user by login
    	           ResponseEntity<CollabUserDTO> collabUser= CollabUtil.findCollabUserByLogin(jHipsterProperties.getCollab().getCollabBaseUrl(), collabJwtToken, managedUserVM.getLogin());
    	           if(collabUser != null){
    	        	if(collabUser.getBody() != null){
    	        	// update collab user
    	               ResponseEntity<CollabUserDTO> collabUpdatedUser= CollabUtil.collabUserUpdate(jHipsterProperties.getCollab().getCollabBaseUrl(), collabJwtToken, user.get(), collabUser.getBody().getId());
    	        	}
    	           }
    	       }
       }
       }
    
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("userManagement.updated", managedUserVM.getLogin()))
            .body(userService.getUserWithAuthorities(managedUserVM.getId()));
    }

    /**
     * GET  /users : get all users.
     * 
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     * @throws URISyntaxException if the pagination headers couldn't be generated
     */
    @RequestMapping(value = {"/users", "/users/searchvalue/{searchtext}"},
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get list of users", notes = "User will get list of customers or agents created  by him. User with role Super Admin and User Admin is allowed."
       		+ "User with role Super Admin will get list of customers and user with role User Admin will get list of agents", response = UserUIDTO.class)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    public ResponseEntity<List<UserUIDTO>> getAllUsers(@PathVariable("searchtext") Optional<String> searchtext, Pageable pageable)
        throws URISyntaxException {
    	log.debug("Rest API to get userslist");
    	
    	if(searchtext.isPresent()){
        	return searchAllUsers(searchtext.get(),pageable);
        }
        Page<User> page = userRepository.findAllUsersWithAuthorities(pageable);
        List<UserUIDTO> managedUserVMs = page.getContent().stream()
            .map(UserUIDTO::new)
            .collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(managedUserVMs, headers, HttpStatus.OK);
    }
    
    
    private ResponseEntity<List<UserUIDTO>> searchAllUsers(String searchText,Pageable pageable)
        throws URISyntaxException {
    	log.debug("Rest API to get userslist based on search value");
        Page<User> page = userRepository.findAllUsersBySearchText(searchText,pageable);
        List<UserUIDTO> managedUserVMs = page.getContent().stream()
            .map(UserUIDTO::new)
            .collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users/searchvalue/{searchtext}");
        return new ResponseEntity<>(managedUserVMs, headers, HttpStatus.OK);
    }

    /**
     * GET  /activateduserlist to get activated users created by logged in user to show  in case assigned to list , notification recepient list etc
     *
     * @return the ResponseEntity with status 200 (OK) and with body the "userslist" user, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/activateduserlist",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get activated users by given login name", notes = "User can get activated agents created by him by given login name to show  in case assigned to list , notification recepients list etc. User with role User Admin is allowed."
       		, response = UserUIDTO.class)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    public ResponseEntity<List<UserUIDTO>> getActivatedUsers() {
    	log.debug("REST request to get Activated Users");
        List<UserUIDTO> managedUserVMs = userService.getActivatedUsersByLogin();
            return  new ResponseEntity<>(managedUserVMs, HttpStatus.OK);
    }
    
    /**
     * GET  /users/:login : get the "login" user.  
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/users/{login:" + Constants.LOGIN_REGEX + "}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get user by given login name", notes = "User can get customer or agent created by him by given login name. User with role Super Admin and User Admin is allowed."
       		, response = UserUIDTO.class)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    public ResponseEntity<UserUIDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        
        UserUIDTO managedUserVM =userService.getUserUIDTOWithAuthoritiesByLogin(login);
         if(managedUserVM!=null){
        	    return  new ResponseEntity<>(managedUserVM, HttpStatus.OK);
         }
            
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * GET  /users/:name : get the "login" user.
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/users-details/{name}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search users based on login", notes = "User can search customer or agent creted by him based on given login name. User with role Super Admin and User Admin is allowed."
       		, response = UserIdDTO.class)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    public ResponseEntity<List<UserIdDTO>> getUserList(@PathVariable String name) {
        log.debug("REST request to get User : {}", name);
        List<UserIdDTO> managedUserVM=userService.findAllUsersByName(name);
         if(managedUserVM!=null){
        	    return  new ResponseEntity<>(managedUserVM, HttpStatus.OK);
          }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    /**
     * DELETE /users/:login : delete the "login" User.
     *
     * @param login the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/users/{login:" + Constants.LOGIN_REGEX + "}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete user by login", notes = "User can delete customer or agent created by him. User with role Super Admin and User Admin is allowed."
       		, response = Void.class)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "userManagement.deleted", login)).build();
    }
    
   /* private ResponseEntity<UserDTO> validateCreateAuthority(ManagedUserVM managedUserVM){
    	Set<String> currentUserAuthorities = getCurrentUserAuthorities();
        Set<String> authorities = managedUserVM.getAuthorities();
        if(authorities==null || authorities.size()==0){
        			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", "profilerequired", "Profile not selected for user creation")).body(null);
        }else{
        	if(authorities.contains(AuthoritiesConstants.USER_ADMIN ) ){
        		if(!currentUserAuthorities.contains(AuthoritiesConstants.SUPER_ADMIN)){
        			return ResponseEntity.badRequest()
                            .headers(HeaderUtil.createFailureAlert("userManagement", "notallowedtocreatewithrole", "User not authorized create user with the selected authority"))
                            .body(null);
        		}
        	}else if(authorities.contains(AuthoritiesConstants.USER) ){
				if(!currentUserAuthorities.contains(AuthoritiesConstants.USER_ADMIN)){
					return ResponseEntity.badRequest()
		                    .headers(HeaderUtil.createFailureAlert("userManagement", "notallowedtocreatewithrole", "User not authorized create user with the selected authority"))
		                    .body(null);	
        		}
        	}
        }
		return null;
    }*/
       
    @RequestMapping(value = "/userdetails/{status}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	@ApiOperation(value = "Get list of users based on status (active, inactive, idle)", notes = "User can get agents created by him based on status of users. User with role User Admin is allowed."
	   		, response = UserUIDTO.class)
	@Secured({  AuthoritiesConstants.USER_ADMIN })
	public ResponseEntity<List<UserUIDTO>> listActiveUsersByStatus(@PathVariable String status,@PageableDefault(page = 0, size = 20)Pageable pageable) throws URISyntaxException {
    	
    	 Page<UserUIDTO> page = userService.getActiveUsersByStatus(status,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/userdetails/{status}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
    
    /**
     * GET  /userdetails/statussearch/{status}/{searchtext} : get all the user based on status and searchtext
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of trCases in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/userdetails/statussearch/{status}/{searchtext}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER_ADMIN})
    @ApiOperation(value = "Get list of all user", notes = "User can get list of all user created by him. User with role User Admin is allowed.", response = UserUIDTO.class)
    public ResponseEntity<List<UserUIDTO>> getAllUsersByStatusSearch(@PathVariable("status") String status,@PathVariable("searchtext") String searchtext,Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Users by status {} and searchtext {}", status,searchtext);
        
        Page<UserUIDTO> page = userService.getAllUsersByStatusAndSearchValue(status,searchtext,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userdetails/statussearch/{status}/{searchtext}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
   
    
    
    /**
     * GET  /users/:name : get the "list of users to display a list" user.
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/userslist",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @ApiOperation(value = "Get list of users", notes = "User can list of  agents created by him. User with role Super Admin and User Admin is allowed."
		, response = UserIdDTO.class)
    @Secured({AuthoritiesConstants.SUPER_ADMIN,AuthoritiesConstants.USER_ADMIN})
    public ResponseEntity<List<UserIdDTO>> getUserListforUI() {
        log.debug("REST request to get Users list");
        List<UserIdDTO> managedUserVM=userService.findAllUsersListByLogin();
         if(managedUserVM!=null){
        	    return  new ResponseEntity<>(managedUserVM, HttpStatus.OK);
          }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } 
    
    
    /**
     * POST  /updateusergpsstatus : Updates an existing User.
     *
     * @param UpdateUserGPSDTO the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the login or email is already in use,
     * or with status 500 (Internal Server Error) if the user couldn't be updated
     */
    @RequestMapping(value = "/updateusergpsstatus",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update existing user's GPS staus", notes = "User can update existing user's GPS staus. User with role User is allowed.", response = Void.class)
    @Timed
    @Secured({AuthoritiesConstants.USER})
    public ResponseEntity<Void> updateUserGpsStatus(@RequestBody UpdateUserGpsDTO updateUserGPSDTO) {
        log.debug("REST request to update User GPS status : {}", updateUserGPSDTO);
       
       
        User result =  userService.updateUserGpsStatus(updateUserGPSDTO.getLogin(), updateUserGPSDTO.getGpsStatus());
        if(result != null){
        return ResponseEntity.ok().headers(HeaderUtil
  	    		.createAlert("usergpsupdate",  "User GPS status has been updated successfully")).body(null);
        } else{
        	 return ResponseEntity.ok().headers(HeaderUtil
       	    		.createAlert("usergpsupdate",  "User not found with login "+updateUserGPSDTO.getLogin())).body(null);
        }
    }
    
    @RequestMapping(value = {"/users/searchactivatedusers/{searchtext}"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @ApiOperation(value = "Get list of activated users", notes = "User will get list of customers or agents created  by him. User with role Super Admin and User Admin is allowed."
           		+ "User with role Super Admin will get list of customers and user with role User Admin will get list of agents", response = UserUIDTO.class)
        @Timed
        @Secured({AuthoritiesConstants.USER_ADMIN})
        public ResponseEntity<List<UserUIDTO>> getAllUsers(@PathVariable("searchtext") String searchtext, Pageable pageable)
            throws URISyntaxException {
        	log.debug("Rest API to get userslist");
        	
        	Page<User> page = userRepository.searchactivatedusers(searchtext,pageable);
            List<UserUIDTO> managedUserVMs = page.getContent().stream()
                .map(UserUIDTO::new)
                .collect(Collectors.toList());
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
            return new ResponseEntity<>(managedUserVMs, headers, HttpStatus.OK);
        }
    
}
