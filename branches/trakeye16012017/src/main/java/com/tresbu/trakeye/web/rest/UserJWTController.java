package com.tresbu.trakeye.web.rest;

import com.tresbu.trakeye.config.TrakEyeProperties;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.repository.UserRepository;
import com.tresbu.trakeye.security.jwt.JWTConfigurer;
import com.tresbu.trakeye.security.jwt.TokenProvider;
import com.tresbu.trakeye.service.UserService;
import com.tresbu.trakeye.service.dto.LocationLogDTO;
import com.tresbu.trakeye.service.util.CollabUtil;
import com.tresbu.trakeye.web.rest.vm.LoginVM;

import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang.StringUtils;


import java.util.Collections;
import java.util.Optional;

import com.codahale.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserJWTController {
	
	private final Logger log = LoggerFactory.getLogger(UserJWTController.class);

    @Inject
    private TokenProvider tokenProvider;

    @Inject
    private AuthenticationManager authenticationManager;
    
    @Inject
    private UserRepository userRepository;
    
    @Inject
    private UserService userService;
    
    @Inject
    private TrakEyeProperties jHipsterProperties;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @Timed
    @ApiOperation(value = "Login ", notes = "User can login by giving username and password. User with role SuperAdmin, User Admin and User is allowed. This API will return auth token.", response = Void.class)
    public ResponseEntity<?> authorize(@Valid @RequestBody LoginVM loginVM, HttpServletResponse response) {
    	log.debug("Rest API to login {}", loginVM.toString());
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        try {
        	
        	//this step compares user with data base details and if matches loads user authentication object to spring security context with user authorities
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
           
            if(!StringUtils.isEmpty(loginVM.getFcmToken())){
            	log.debug("Received user FcmToken {}", loginVM.getFcmToken());
            	Optional<User> user =userRepository.findOneByLogin(loginVM.getUsername());
            	userService.updateFcmToken(user.get().getId(),loginVM.getFcmToken());
            }
            
            if(!StringUtils.isEmpty(loginVM.getImei())){
            	log.debug("Received user IMEI number {}", loginVM.getImei());
            	Optional<User> user =userRepository.findOneByLogin(loginVM.getUsername());
            	log.debug("Received GPS status {}", loginVM.getGpsStatus());
            	userService.updateUserGpsStatus(user.get().getLogin(), loginVM.getGpsStatus());
            	if(user.get().getImei()==null || StringUtils.isEmpty(user.get().getImei())){
            		if(userService.findUserByIMEI(loginVM.getImei())){
                		log.debug("IMEI is already used by another user");
               		    return new ResponseEntity<>(Collections.singletonMap("AuthenticationException","IMEI is already used by another user"), HttpStatus.UNAUTHORIZED);
                	}
            		log.debug("Existing user does not have IMEI no. So updating user with new IMEI");
            		userService.updateIMEI(user.get().getId(),loginVM.getImei() );
            	}	else if(!user.get().getImei().equals(loginVM.getImei())){
            		log.debug("Local DB IMEI and reveived IMEI does not match");
               		    return new ResponseEntity<>(Collections.singletonMap("AuthenticationException","Registered IMEI is not matching"), HttpStatus.UNAUTHORIZED);
               	}
            }else{
            	log.debug("IMEI no and GPS status is not received");
            }
            
            if(!StringUtils.isEmpty(loginVM.getPhoneNo()) || loginVM.getPhoneNo() != null ){
            	log.debug("Received user phone number {}", loginVM.getPhoneNo());
            	Optional<User> user =userRepository.findOneByLogin(loginVM.getUsername());
            	if(!user.get().getPhone().equals(loginVM.getPhoneNo())){
            		log.debug("Local DB Phone number and reveived phone number do not match");
               		return new ResponseEntity<>(Collections.singletonMap("AuthenticationException","Registered phone number is not matching"), HttpStatus.UNAUTHORIZED);
               	}
            }else{
            	log.debug("Phone number is not received");
            }
            if(!StringUtils.isEmpty(loginVM.getApplicationVersion()) || loginVM.getApplicationVersion() != null 
            		|| !StringUtils.isEmpty(loginVM.getOperatingSystem()) || loginVM.getOperatingSystem() != null){
            	Optional<User> loggedInUser =userRepository.findOneByLogin(loginVM.getUsername());
            	userService.updateOsAndAppVerion(loggedInUser.get().getId(), loginVM.getOperatingSystem(), loginVM.getApplicationVersion());
            
            }else{
            	log.debug("Operating system and application version are not received");
            }
            
            
            //generates JWT token using user authorities
            String jwt = tokenProvider.createToken(authentication, rememberMe);
            response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
            // call collab api's only if collabEnabled is true
            if(jHipsterProperties.getCollab().isCollabEnabled()){
            	 // login to collab and save collab jwt token to user
                String collabAdminJwtToken = CollabUtil.collabAdminAuthentication(jHipsterProperties.getCollab().getCollabBaseUrl(), loginVM.getUsername(), loginVM.getPassword());
                Optional<User> loggedInUser =userRepository.findOneByLogin(loginVM.getUsername());
                if(loggedInUser.isPresent()){
                	userService.updateUserWithCollabJwtToken(loggedInUser.get().getId(), collabAdminJwtToken);
                }
            }
           
        	
            return ResponseEntity.ok(new JWTToken(jwt));
        } catch (AuthenticationException exception) {
            return new ResponseEntity<>(Collections.singletonMap("AuthenticationException",exception.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
}
