package com.tresbu.trakeye.web.rest;

import java.io.UnsupportedEncodingException;

import java.net.URI;

import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.codahale.metrics.annotation.Timed;
import com.tresbu.trakeye.config.TrakEyeProperties;
import com.tresbu.trakeye.domain.AbstractAuditingEntity;
import com.tresbu.trakeye.domain.Authority;
import com.tresbu.trakeye.domain.Customer;
import com.tresbu.trakeye.domain.Tenant;
import com.tresbu.trakeye.repository.AuthorityRepository;
import com.tresbu.trakeye.repository.CustomerRepository;
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.service.CustomerService;
import com.tresbu.trakeye.service.dto.CustomerCreateDTO;
import com.tresbu.trakeye.service.dto.CustomerDTO;
import com.tresbu.trakeye.service.dto.CustomerUpdateDTO;
import com.tresbu.trakeye.service.dto.ResponseAuthunticationDTO;
import com.tresbu.trakeye.service.dto.TenantDTO;
import com.tresbu.trakeye.service.dto.UserDTO;
import com.tresbu.trakeye.service.dto.UserUIDTO;
import com.tresbu.trakeye.service.mapper.CustomerMapper;
import com.tresbu.trakeye.web.rest.util.HeaderUtil;
import com.tresbu.trakeye.web.rest.util.PaginationUtil;
import com.tresbu.trakeye.service.UserService;
import com.tresbu.trakeye.web.rest.vm.LoginVM;
import com.tresbu.trakeye.web.rest.vm.ManagedUserVM;
import com.tresbu.trakeye.domain.User;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api")
public class CustomerResource {

    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);
    
    @Inject
    private CustomerService customerService;

    @Inject
    private CustomerRepository customerRepository;
    
    @Inject
    private AuthorityRepository authorityRepository;
    
    @Inject
    private CustomerMapper customerMapper;

    @Inject
    private UserService userService;
    
    @Inject
    private TrakEyeProperties jHipsterProperties;
    
    /**
     * POST  /customer registration : It's an open api which doesn't require authentication.
     *
     * @param cutomerDTO the customerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerDTO, or with status 400 (Bad Request) if the customer has already an ID and
     * with status 404 (Not Found) if the api is not found
     * @throws URISyntaxException 
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value = "/customer",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @ApiResponses(value = {
    		@ApiResponse(code = 201, message = "Created"),
    		@ApiResponse(code = 404, message = "Not Found"),
    		@ApiResponse(code = 400, message = "Bad Request")
    })
    @ApiOperation(value = "Create a Customer", notes = "Registers new customer request to the portal.", response = CustomerDTO.class)
    public ResponseEntity<?> registerACustomer(@Valid @RequestBody CustomerCreateDTO customerCreateDTO, HttpServletRequest request) throws URISyntaxException, UnsupportedEncodingException {
        log.debug("REST request to save customer : {}", customerCreateDTO);
                        
        //Validating user with role user_admin. Creating a user with role user_admin, if user doesn't exists        
       // UserDTO userDTO = userService.getUserByLogin(jHipsterProperties.getCustomerAdmin().getLogin());        
        
    	String url = request.getRequestURL().toString();
    	String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() ;

       // if(userDTO == null){
        //	createUserAdmin(baseURL, customerCreateDTO, getSuperAdminLogin());        	 
        //}else{
        //	String userAdminToken = userAdminAuthuntication(baseURL, jHipsterProperties.getCustomerAdmin().getLogin(), jHipsterProperties.getCustomerAdmin().getPassword());
        	//createCustomerAsUserEntity(userAdminToken, customerCreateDTO, baseURL);
        //}
    	CustomerDTO customerDTO = customerService.save(customerCreateDTO);    
    	if(customerDTO != null){
	    	String superAdminToken = userAdminAuthuntication(baseURL, jHipsterProperties.getSuperAdmin().getSuperAdminLogin(), jHipsterProperties.getSuperAdmin().getSuperAdminPassword());
	    	createTenant(superAdminToken, customerCreateDTO, baseURL);
    	}	    	
            return ResponseEntity.created(new URI("/api/customer/" +customerDTO.getId()))
                    .headers(HeaderUtil.createAlert( "customerManagement.created", customerDTO.getFirstName())).build();
    }
        
    /**
     * GET  /customers : This operation is allowed only for super admin.
     * 
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all customers
     * @throws URISyntaxException if the pagination headers couldn't be generated
     */
    @RequestMapping(value = {"/customers", "/customers/searchvalue/{searchtext}"},
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found"),
		@ApiResponse(code = 401, message = "Unauthorized")
    })
    @ApiOperation(value = "Get all Customers", notes = "This operation is allowed only for super admin", response = CustomerDTO.class)
    @Timed
    @Secured({AuthoritiesConstants.SUPER_ADMIN})
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(@PathVariable("searchtext") Optional<String> searchtext, Pageable pageable)
        throws URISyntaxException {
    	log.debug("Rest API to get customerslist");
    	if(searchtext.isPresent()){
        	return searchAllCustomers(searchtext.get(),pageable);
        }
        //List<Customer> customers = customerRepository.findAll();
        //Page<Customer> page = customerRepository.findAll(pageable);
    	Page<Customer> page = customerRepository.findAllCustomers(pageable);
    	
        List<Customer> managedCustomers = page.getContent();
        List<CustomerDTO> managedCustomerVMs = customerMapper.customersToCustomerDTOs(managedCustomers);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customers");
            return new ResponseEntity<>(managedCustomerVMs, headers, HttpStatus.OK);
    }
    
    private ResponseEntity<List<CustomerDTO>> searchAllCustomers(String searchText,Pageable pageable)
            throws URISyntaxException {
        	log.debug("Rest API to get customerslist based on search value");
        	Page<Customer> page = customerRepository.findAllCustomersBySearchText(searchText, pageable);
        	
            List<Customer> managedCustomers = page.getContent();
            List<CustomerDTO> managedCustomerVMs = customerMapper.customersToCustomerDTOs(managedCustomers);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customers/searchvalue/{searchtext}");
            
            return new ResponseEntity<>(managedCustomerVMs, headers, HttpStatus.OK);
        }
    
    /**
     * GET  /customer : This operation is allowed only for super admin.
     * 
     * @return the ResponseEntity with status 200 (OK) and with body of a customer
     */
    @RequestMapping(value = {"/customer/{customerId}"},
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found"),
		@ApiResponse(code = 401, message = "Unauthorized")
    })
    @ApiOperation(value = "Get a customer", notes = "This operation is allowed only for super admin", response = CustomerDTO.class)
    @Timed
    @Secured({AuthoritiesConstants.SUPER_ADMIN})    
    public ResponseEntity<CustomerDTO> getACustomer(@PathVariable("customerId") Long customerId)
        throws URISyntaxException {
        log.debug("REST request to get Customer by Id : {}", customerId);
        
        CustomerDTO customerDTO =customerService.findOne(customerId);
         if(customerDTO!=null){
        	    return new ResponseEntity<>(customerDTO, HttpStatus.OK);
         }            
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    /**
     * UPDATE  /customer : This operation is allowed for only super admin.
     * 
     * @return the ResponseEntity with status 200 (OK) and with body of a customer
     */
    @RequestMapping(value = {"/customer"},
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 201, message = "Created"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found")
    })
    @ApiOperation(value = "Update a customer", notes = "This operation is allowed for only super admin", response = CustomerDTO.class)
    @Timed
    @Secured({AuthoritiesConstants.SUPER_ADMIN})
    public ResponseEntity<?> updateACustomers(@Valid @RequestBody CustomerUpdateDTO customerUpdateDTO, HttpServletRequest request)
        throws URISyntaxException {
        log.debug("REST request to update Customer : {}", customerUpdateDTO);
        
        Optional<Customer> existingCustomer =customerRepository.findOneById(customerUpdateDTO.getId());
         if(!existingCustomer.isPresent() && (!existingCustomer.get().getId().equals(customerUpdateDTO.getId()))){
             return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("customerManagement", "emailnotexists", "Customer doesn't exists")).body(null);
         }     
         
         Customer customer = existingCustomer.get();
         customer.setStatus(customerUpdateDTO.getStatus());
         customerService.update(customerMapper.customerTocustomerDTO(customer));
         
         return ResponseEntity.ok()
                 .headers(HeaderUtil.createAlert("customerManagement.updated", customer.getFirstName())).build();
    }
    
   /* private void createCustomerAsUserEntity(String userAdminToken, CustomerCreateDTO customerCreateDTO, String baseUrl) throws UnsupportedEncodingException{
    	log.debug("REST request to create a user with Customer : {}", customerCreateDTO);
    	//Building authorities to set role for user as user_admin
    	Set<Authority> authorities = new HashSet<>();
    	Authority authority = new Authority();
    	authority.setName(AuthoritiesConstants.USER);
    	authorities.add(authority);
    	
    	Set<Authority> authorities = new HashSet<>();
        Authority adminAuthority = authorityRepository.findOne(AuthoritiesConstants.USER_ADMIN);
          authorities.add(adminAuthority);
        Authority userAuthority =  authorityRepository.findOne(AuthoritiesConstants.USER);
          authorities.add(userAuthority);

    	//Building a user_admin object    	
    	User user = new User();
    	user.setLogin(customerCreateDTO.getFirstName());
    	user.setFirstName(customerCreateDTO.getFirstName());
    	user.setLastName(customerCreateDTO.getLastName());
    	user.setEmail(customerCreateDTO.getEmail());
    	user.setLangKey(customerCreateDTO.getLangKey());
    	user.setAuthorities(authorities);
    	
    	ManagedUserVM managedrUserVm = new ManagedUserVM(user);    	
    	
    	RestTemplate restTemplate = new RestTemplate();
    	HttpHeaders headers = new HttpHeaders();
    	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    	headers.set("Authorization", "Bearer "+userAdminToken);

    	HttpEntity<ManagedUserVM> requestEntity = new HttpEntity<ManagedUserVM>(managedrUserVm,headers);
    	try{
    	restTemplate.exchange(baseUrl+"/api/users",HttpMethod.POST, requestEntity, User.class);
    	}catch(Exception e){
    		log.debug("REST request failed with exception : {} "+e.getMessage());
        	user.setLogin(customerCreateDTO.getFirstName()+customerCreateDTO.getLastName());    		
        	managedrUserVm = new ManagedUserVM(user);
        	try{
        		requestEntity = new HttpEntity<ManagedUserVM>(managedrUserVm,headers);
        	restTemplate.exchange(baseUrl+"/api/users",HttpMethod.POST, requestEntity, User.class);
        	}catch(Exception ex){
        	  user.setLogin(user.getLogin()+(int)(Math.random()*9000)+1000);	
          	managedrUserVm = new ManagedUserVM(user);
      		requestEntity = new HttpEntity<ManagedUserVM>(managedrUserVm,headers);
        	restTemplate.exchange(baseUrl+"/api/users",HttpMethod.POST, requestEntity, User.class);
        	}
    	}
    }*/
     
    /*private void createUserAdmin(String baseUrl, CustomerCreateDTO customerCreateDTO, String adminLogin) throws UnsupportedEncodingException{
        log.debug("REST request to create a trail user admin");
        
    	//Building authorities to set role for user as user_admin
    	Set<Authority> authorities = new HashSet<>();
    	Authority authority = new Authority();
    	authority.setName(AuthoritiesConstants.USER_ADMIN);
    	authorities.add(authority);
    	
    	//Building a user_admin object    	    	
    	User user = new User();
    	user.setLogin(jHipsterProperties.getCustomerAdmin().getLogin());
    	user.setFirstName(jHipsterProperties.getCustomerAdmin().getFirstName());
    	user.setLastName(jHipsterProperties.getCustomerAdmin().getLastName());
    	user.setEmail(jHipsterProperties.getCustomerAdmin().getEmail());
    	user.setLangKey(customerCreateDTO.getLangKey());
    	user.setAuthorities(authorities);
    	user.setLastModifiedBy(adminLogin);
    	
    	ManagedUserVM managedrUserVm = new ManagedUserVM(user);

    	//Create a trail admin
    	user = userService.createUser(managedrUserVm);    	
    	
    	//reseting a user_admin passsword
    	userService.completePasswordReset(jHipsterProperties.getCustomerAdmin().getPassword(), user.getResetKey());
    	 
    	String userAdminJwtToken = userAdminAuthuntication(baseUrl, jHipsterProperties.getCustomerAdmin().getLogin(), jHipsterProperties.getCustomerAdmin().getPassword());
                
    	createCustomerAsUserEntity(userAdminJwtToken,customerCreateDTO, baseUrl);
    	//return userMapper.userToUserDTO(user);
    }*/
    
    private String userAdminAuthuntication(String baseUrl, String username, String password){
        log.debug("REST request to authenticate user admin");
    	RestTemplate restTemplate = new RestTemplate();
    	LoginVM loginVm = new LoginVM();
    	loginVm.setUsername(username);
    	loginVm.setPassword(password);
    	HttpEntity<LoginVM> httpEntity = new HttpEntity<LoginVM>(loginVm);
/*    	return ResponseEntity.created(new URI("/api/authenticate")
                .body(loginVm);*/
    	ResponseAuthunticationDTO responseAuthunticationDTO = restTemplate.postForObject(baseUrl+"/api/authenticate", httpEntity, ResponseAuthunticationDTO.class);
    	return responseAuthunticationDTO.getIdToken();
    }

     
        
        private void createTenant(String superAdminToken, CustomerCreateDTO customerCreateDTO, String baseUrl) throws UnsupportedEncodingException{
        	log.debug("Method to create tenant {}", customerCreateDTO);
        	RestTemplate restTemplate = new RestTemplate();
        	HttpHeaders headers = new HttpHeaders();
        	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        	headers.set("Authorization", "Bearer "+superAdminToken);
        	
        	TenantDTO tenantDTO = new TenantDTO();
        	tenantDTO.setOrganization(customerCreateDTO.getOrganization());
        	tenantDTO.setLoginName(customerCreateDTO.getOrganization());
        	tenantDTO.setEmail(customerCreateDTO.getEmail());
        	tenantDTO.setPhone(customerCreateDTO.getMobilePhone());
        	tenantDTO.setCity(customerCreateDTO.getCity());
        	tenantDTO.setAddress("");
        	tenantDTO.setId(null);
        	tenantDTO.setCreatedDate(null);
        	tenantDTO.setUpdatedDate(null);
        	
        	HttpEntity<TenantDTO> requestEntity = new HttpEntity<TenantDTO>(tenantDTO,headers);
        	try{
        	restTemplate.exchange(baseUrl+"/api/tenants",HttpMethod.POST, requestEntity, Tenant.class);
        	}catch(Exception e){
        		log.debug("REST request failed with exception : {} "+e.getMessage());
        		tenantDTO.setLoginName(customerCreateDTO.getOrganization()+customerCreateDTO.getFirstName());    		
            	
            	try{
            		requestEntity = new HttpEntity<TenantDTO>(tenantDTO,headers);
            	restTemplate.exchange(baseUrl+"/api/tenants",HttpMethod.POST, requestEntity, Tenant.class);
            	}catch(Exception ex){
            		log.debug("REST request failed with exception : {} "+ex.getMessage());
            	tenantDTO.setLoginName(tenantDTO.getLoginName()+(int)(Math.random()*9000)+1000);	
              	
          		requestEntity = new HttpEntity<TenantDTO>(tenantDTO,headers);
            	restTemplate.exchange(baseUrl+"/api/tenants",HttpMethod.POST, requestEntity, Tenant.class);
            	}
        	}
        }	
        
        
}
