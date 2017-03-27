package com.tresbu.trakeye.service.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.tresbu.trakeye.config.TrakEyeProperties;
import com.tresbu.trakeye.domain.Authority;
import com.tresbu.trakeye.domain.Tenant;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.service.dto.CollabAccountDTO;
import com.tresbu.trakeye.service.dto.CollabManagedUserVM;
import com.tresbu.trakeye.service.dto.CollabTenantDTO;
import com.tresbu.trakeye.service.dto.CollabUserDTO;
import com.tresbu.trakeye.service.dto.ResponseAuthunticationDTO;
import com.tresbu.trakeye.service.dto.TenantDTO;
import com.tresbu.trakeye.service.dto.UserDTO;
import com.tresbu.trakeye.web.rest.vm.KeyAndPasswordVM;
import com.tresbu.trakeye.web.rest.vm.LoginVM;
import com.tresbu.trakeye.web.rest.vm.ManagedUserVM;

public class CollabUtil {
	
	 private static final Logger log = LoggerFactory.getLogger(CollabUtil.class);
	 
	    @Inject
	    private static TrakEyeProperties jHipsterProperties;

	    private CollabUtil() {
	    }
	    
	    public static String collabAdminAuthentication(String baseUrl, String username, String password){
	        log.info("REST request to authenticate collab user: {}, password: {}", username, password);
	    	RestTemplate restTemplate = new RestTemplate();
	    	LoginVM loginVm = new LoginVM();
	    	loginVm.setUsername(username);
	    	loginVm.setPassword(password);
	    	HttpEntity<LoginVM> httpEntity = new HttpEntity<LoginVM>(loginVm);
	    //	String baseUrl = jHipsterProperties.getCollab().getCollabBaseUrl();
	    	ResponseAuthunticationDTO responseAuthunticationDTO = null;
	    	try{
	    		responseAuthunticationDTO = restTemplate.postForObject(baseUrl+"/api/authenticate", httpEntity, ResponseAuthunticationDTO.class);
	        }catch(Exception e){
    		log.info("REST request failed with exception : {} "+e.getMessage());
        	
            }
	    	if(responseAuthunticationDTO != null){
	    		return responseAuthunticationDTO.getIdToken();
	    	}
	    	return null;
	    }
	    
	    public static ResponseEntity<CollabTenantDTO> collabTenantCreate(String baseUrl,String jwtToken, TenantDTO tenant){
	        log.info("REST request to create tenant in collab: {}",tenant);
	    	
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    	headers.set("Authorization", "Bearer "+jwtToken);
	    	
	    	CollabTenantDTO collabTenantDTO = new CollabTenantDTO();
	    	collabTenantDTO.setName(tenant.getLoginName());
	    	collabTenantDTO.setAddress(tenant.getAddress());
	    	collabTenantDTO.setCity(tenant.getCity());
	    	collabTenantDTO.setPhone(tenant.getPhone());
	    	collabTenantDTO.setEmail(tenant.getEmail());
	    	collabTenantDTO.setOrganization(tenant.getOrganization());
	    	collabTenantDTO.setId(null);
	    	collabTenantDTO.setCreatedDate(null);
	    	collabTenantDTO.setUpdatedDate(null);
	    	collabTenantDTO.setTrakeyeTenant(true);
	    	
	    	HttpEntity<CollabTenantDTO> requestEntity = new HttpEntity<CollabTenantDTO>(collabTenantDTO,headers);
	    	//String baseUrl = jHipsterProperties.getCollab().getCollabBaseUrl();
	    	 ResponseEntity<CollabTenantDTO> collabTenant= null;
	    	try{
	    		 collabTenant =restTemplate.exchange(baseUrl+"/api/tenants",HttpMethod.POST, requestEntity, CollabTenantDTO.class);
	        	}catch(Exception e){
	        		log.info("REST request failed with exception : {} "+e.getMessage());
	            	
	            }
	    	
	    	return collabTenant;
	    }
	    
	    public static String collabPasswordReset(String baseUrl,String password, String resetKey){
	        log.info("REST request to reset collab password for user with reset key: {}",resetKey);
	    	
	    	RestTemplate restTemplate = new RestTemplate();
	    	KeyAndPasswordVM keyAndPasswordVM = new KeyAndPasswordVM();
	    	keyAndPasswordVM.setNewPassword(password);
	    	keyAndPasswordVM.setKey(resetKey);
	    	
	    	HttpEntity<KeyAndPasswordVM> requestEntity = new HttpEntity<KeyAndPasswordVM>(keyAndPasswordVM);
	    	//String baseUrl = jHipsterProperties.getCollab().getCollabBaseUrl();
	    	 String collabTenant= null;
	    	try{
	    		 collabTenant =restTemplate.postForObject(baseUrl+"/api/account/reset_password/finish", requestEntity, String.class);
	    		
	        	}catch(Exception e){
	        		log.info("REST request failed with exception : {} "+e.getMessage());
	            	
	            }
	    	
	    	return collabTenant;
	    }
	    
	    public static ResponseEntity<CollabUserDTO> findCollabUserByLogin(String baseUrl,String jwtToken, String login){
	        log.info("REST request to find collab user by login: {}",login);
	    	
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    	headers.set("Authorization", "Bearer "+jwtToken);
	    
	    	HttpEntity<String> requestEntity = new HttpEntity<String>(jwtToken,headers);
	    //	String baseUrl = jHipsterProperties.getCollab().getCollabBaseUrl();
	    	 ResponseEntity<CollabUserDTO> collabUser= null;
	    	try{
	    		 collabUser =restTemplate.exchange(baseUrl+"/api/users/"+login,HttpMethod.GET, requestEntity, CollabUserDTO.class);
	        	}catch(Exception e){
	        		log.info("REST request failed with exception : {} "+e.getMessage());
	            	
	            }
	    	
	    	return collabUser;
	    }
	    
	    public static ResponseEntity<CollabUserDTO> collabUserCreate(String baseUrl,String jwtToken, User user){
	        log.info("REST request to create user in collab: {}",user);
	    	
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    	headers.set("Authorization", "Bearer "+jwtToken);
	    	
	    	CollabUserDTO collabUserDTO = new CollabUserDTO();
	    	collabUserDTO.setId(null);
	    	collabUserDTO.setLogin(user.getLogin());
	    	collabUserDTO.setFirstName(user.getFirstName());
	    	collabUserDTO.setLastName(user.getLastName());
	    	collabUserDTO.setEmail(user.getEmail());
	    	collabUserDTO.setActivated(true);
	    	collabUserDTO.setLangKey(user.getLangKey());
	    	collabUserDTO.setCreatedBy(null);
	    	collabUserDTO.setCreatedDate(null);
	    	collabUserDTO.setLastModifiedBy(null);
	    	collabUserDTO.setLastModifiedDate(null);
	    	collabUserDTO.setResetDate(null);
	    	collabUserDTO.setResetKey(null);
	    	// convert set of authority objects to set of string
	    	Set<Authority> authorities = user.getAuthorities();
			Set<String> collect = authorities.stream().map(Authority::getName).collect(Collectors.toSet());
	    	collabUserDTO.setAuthorities(collect);
	    	
	    	
	    	HttpEntity<CollabUserDTO> requestEntity = new HttpEntity<CollabUserDTO>(collabUserDTO,headers);
	    	//String baseUrl = jHipsterProperties.getCollab().getCollabBaseUrl();
	    	 ResponseEntity<CollabUserDTO> collabUser= null;
	    	try{
	    		collabUser =restTemplate.exchange(baseUrl+"/api/users",HttpMethod.POST, requestEntity, CollabUserDTO.class);
	        	}catch(Exception e){
	        		log.info("REST request failed with exception : {} "+e.getMessage());
	            	
	            }
	    	
	    	return collabUser;
	    }
	    
	    public static ResponseEntity<CollabUserDTO> collabUserUpdate(String baseUrl,String jwtToken, User user, Long collabUserId){
	        log.info("REST request to update  user in collab with id {}",collabUserId);
	    	
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    	headers.set("Authorization", "Bearer "+jwtToken);
	    	
	    	CollabUserDTO collabUserDTO = new CollabUserDTO();
	    	collabUserDTO.setId(collabUserId);
	    	collabUserDTO.setLogin(user.getLogin());
	    	collabUserDTO.setFirstName(user.getFirstName());
	    	collabUserDTO.setLastName(user.getLastName());
	    	collabUserDTO.setEmail(user.getEmail());
	    	collabUserDTO.setActivated(true);
	    	collabUserDTO.setLangKey(user.getLangKey());
	    	collabUserDTO.setCreatedBy(user.getCreatedBy());
	    	collabUserDTO.setResetKey(user.getResetKey());
	    	// convert set of authority objects to set of string
	    	Set<Authority> authorities = user.getAuthorities();
			Set<String> collect = authorities.stream().map(Authority::getName).collect(Collectors.toSet());
	    	collabUserDTO.setAuthorities(collect);
	    	
	    	
	    	HttpEntity<CollabUserDTO> requestEntity = new HttpEntity<CollabUserDTO>(collabUserDTO,headers);
	    	//String baseUrl = jHipsterProperties.getCollab().getCollabBaseUrl();
	    	 ResponseEntity<CollabUserDTO> collabUser= null;
	    	try{
	    		collabUser =restTemplate.exchange(baseUrl+"/api/users",HttpMethod.PUT, requestEntity, CollabUserDTO.class);
	        	}catch(Exception e){
	        		log.info("REST request failed with exception : {} "+e.getMessage());
	            	
	            }
	    	
	    	return collabUser;
	    }
	    
	    public static ResponseEntity<CollabTenantDTO> collabTenantUpdate(String baseUrl,String jwtToken, TenantDTO tenant, CollabTenantDTO collabTenant){
	        log.info("REST request to update  tenant in collab with id: {}",collabTenant.getId());
	    	
	        RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    	headers.set("Authorization", "Bearer "+jwtToken);
	    	
	    	CollabTenantDTO collabTenantDTO = new CollabTenantDTO();
	    	collabTenantDTO.setId(collabTenant.getId());
	    	collabTenantDTO.setName(tenant.getLoginName());
	    	collabTenantDTO.setAddress(tenant.getAddress());
	    	collabTenantDTO.setCity(tenant.getCity());
	    	collabTenantDTO.setPhone(tenant.getPhone());
	    	collabTenantDTO.setEmail(tenant.getEmail());
	    	collabTenantDTO.setOrganization(tenant.getOrganization());
	    	collabTenantDTO.setCreatedDate(collabTenant.getCreatedDate());
	    	collabTenantDTO.setUpdatedDate(null);
	    	collabTenantDTO.setCreatedById(collabTenant.getCreatedById());
	    	collabTenantDTO.setCreatedByLogin(collabTenant.getCreatedByLogin());
	    	collabTenantDTO.setTrakeyeTenant(true);
	    	
	    	HttpEntity<CollabTenantDTO> requestEntity = new HttpEntity<CollabTenantDTO>(collabTenantDTO,headers);
	    	//String baseUrl = jHipsterProperties.getCollab().getCollabBaseUrl();
	    	 ResponseEntity<CollabTenantDTO> collabTenant1= null;
	    	try{
	    		 collabTenant1 =restTemplate.exchange(baseUrl+"/api/tenants",HttpMethod.PUT, requestEntity, CollabTenantDTO.class);
	        	}catch(Exception e){
	        		log.info("REST request failed with exception : {} "+e.getMessage());
	            	
	            }
	    	
	    	return collabTenant1;
	    }
	    
	    public static ResponseEntity<CollabTenantDTO> findCollabTenantByName(String baseUrl,String jwtToken, String name){
	        log.info("REST request to find collab tenant by name: {}",name);
	    	
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    	headers.set("Authorization", "Bearer "+jwtToken);
	    
	    	HttpEntity<String> requestEntity = new HttpEntity<String>(jwtToken,headers);
	    
	    	 ResponseEntity<CollabTenantDTO> collabTenant= null;
	    	try{
	    		collabTenant =restTemplate.exchange(baseUrl+"/api/tenantbyname/"+name,HttpMethod.GET, requestEntity, CollabTenantDTO.class);
	        	}catch(Exception e){
	        		log.info("REST request failed with exception : {} "+e.getMessage());
	            	
	            }
	    	
	    	return collabTenant;
	    }
	    
	    public static String collabForgotPassword(String baseUrl,String email){
	        log.info("REST request for forgot password in  collab with email: {}",email);
	    	
	    	RestTemplate restTemplate = new RestTemplate();
	    	
	    	HttpEntity<String> requestEntity = new HttpEntity<String>(email);
	    	//String baseUrl = jHipsterProperties.getCollab().getCollabBaseUrl();
	    	 String collabTenant= null;
	    	try{
	    		 collabTenant =restTemplate.postForObject(baseUrl+"/api/account/reset_password/init", requestEntity, String.class);
	    		
	        	}catch(Exception e){
	        		log.info("REST request failed with exception : {} "+e.getMessage());
	            	
	            }
	    	
	    	return collabTenant;
	    }
	    
	    public static ResponseEntity<CollabUserDTO> collabChangePassword(String baseUrl,String jwtToken, String password){
	        log.info("REST request to change password in  collab for logged in user");
	    	
	        RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));
	    	headers.set("Authorization", "Bearer "+jwtToken);
	    	//StringBuilder sb=new StringBuilder(password);  
	    	String p = "\"" + password + "\"";
	    	HttpEntity<String> requestEntity = new HttpEntity<String>(password,headers);
	    	//String baseUrl = jHipsterProperties.getCollab().getCollabBaseUrl();
	    	ResponseEntity<CollabUserDTO> response= null;
	    	try{
	    		response= restTemplate.exchange(baseUrl+"/api/account/change_password",HttpMethod.POST, requestEntity, CollabUserDTO.class);
	    		
	        	}catch(Exception e){
	        		log.info("REST request failed with exception : {} "+e.getMessage());
	            	
	            }
	    	
	    	return response;
	    }
	    
	    public static ResponseEntity<CollabUserDTO> collabAccountUpdate(String baseUrl,String jwtToken, String firstName, String LastName, String email, String login){
	        log.info("REST request to update  user account  in collab for logged in user. This is settings link API");
	    	
	        RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    	headers.set("Authorization", "Bearer "+jwtToken);
	    	
	    	CollabAccountDTO collabAccountDTO = new CollabAccountDTO();
	    	collabAccountDTO.setFirstName(firstName);
	    	collabAccountDTO.setLastName(LastName);
	    	collabAccountDTO.setEmail(email);
	    	collabAccountDTO.setLogin(login);
	    	
	    	HttpEntity<CollabAccountDTO> requestEntity = new HttpEntity<CollabAccountDTO>(collabAccountDTO,headers);
	    	//String baseUrl = jHipsterProperties.getCollab().getCollabBaseUrl();
	    	 ResponseEntity<CollabUserDTO> collabUser= null;
	    	try{
	    		collabUser =restTemplate.exchange(baseUrl+"/api/account",HttpMethod.POST, requestEntity, CollabUserDTO.class);
	        	}catch(Exception e){
	        		log.info("REST request failed with exception : {} "+e.getMessage());
	            	
	            }
	    	
	    	return collabUser;
	    }
	    
	    
	    
	    /*public static ResponseEntity<CollabUserDTO> findCollabUserAccount(String baseUrl,String jwtToken){
	        log.info("REST request to fetch loggedin  collab user before update");
	    	
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    	headers.set("Authorization", "Bearer "+jwtToken);
	    
	    	HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
	    //	String baseUrl = jHipsterProperties.getCollab().getCollabBaseUrl();
	    	 ResponseEntity<CollabUserDTO> collabUserAccount= null;
	    	try{
	    		collabUserAccount =restTemplate.exchange(baseUrl+"/api/account",HttpMethod.GET, requestEntity, CollabUserDTO.class);
	        	}catch(Exception e){
	        		log.info("REST request failed with exception : {} "+e.getMessage());
	            	
	            }
	    	
	    	return collabUserAccount;
	    }*/


}
