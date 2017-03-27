package com.tresbu.trakeye.web.rest;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.tresbu.trakeye.domain.Authority;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.service.UserService;
import com.tresbu.trakeye.service.dto.UserDTO;
import com.tresbu.trakeye.service.mapper.UserMapper;

public class BaseResource extends DefaultDataResource{
	
	 @Inject UserService userService;
	 @Inject UserMapper userMapper;
	
	 public Long getCurrentUserLogin() {
		  Long userId=null;
		  User currentUser = getCurrentUser();
		  if(currentUser!=null){
			  userId =  currentUser.getId();
		  }
	        return userId;
	    }
	 
	 public Set<String> getCurrentUserAuthorities() {
		 Set<String> authorities = new TreeSet<>();
		 User currentUser = getCurrentUser();
		  if(currentUser!=null){
			Set<Authority>   authoritieslist = currentUser.getAuthorities();
			for (Iterator<Authority> iterator = authoritieslist.iterator(); iterator.hasNext();) {
				Authority authority = (Authority) iterator.next();
				authorities.add(authority.getName());
			}
		  }
	        return authorities;
	    }
	 protected UserDTO getCurrentUserDTO(){
		 SecurityContext securityContext = SecurityContextHolder.getContext();
	        Authentication authentication = securityContext.getAuthentication();
	      
	        String userName = null;
	        if (authentication != null) {
	            if (authentication.getPrincipal() instanceof UserDetails) {
	                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
	                userName = springSecurityUser.getUsername();
	            } else if (authentication.getPrincipal() instanceof String) {
	                userName = (String) authentication.getPrincipal();
	            }
	        }
	        
	       return userService.getUserByLogin(userName);
	      
	 }
	 protected  User getCurrentUser(){
		 SecurityContext securityContext = SecurityContextHolder.getContext();
	        Authentication authentication = securityContext.getAuthentication();
	      
	        String userName = null;
	        if (authentication != null) {
	            if (authentication.getPrincipal() instanceof UserDetails) {
	                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
	                userName = springSecurityUser.getUsername();
	            } else if (authentication.getPrincipal() instanceof String) {
	                userName = (String) authentication.getPrincipal();
	            }
	        }
	        
	       Optional<User> user = userService.getUserWithAuthoritiesByLogin(userName);
	       if(user.isPresent()){
	    	   return   user.get();
	    	  
	       }
		return null;
	 }
}
