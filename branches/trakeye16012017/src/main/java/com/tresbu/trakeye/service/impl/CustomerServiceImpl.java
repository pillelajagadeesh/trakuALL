package com.tresbu.trakeye.service.impl;

import java.time.Instant;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tresbu.trakeye.domain.Customer;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.domain.enumeration.CustomerStatus;
import com.tresbu.trakeye.repository.CustomerRepository;
import com.tresbu.trakeye.repository.UserRepository;
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.service.CustomerService;
import com.tresbu.trakeye.service.MailService;
import com.tresbu.trakeye.service.dto.CustomerCreateDTO;
import com.tresbu.trakeye.service.dto.CustomerDTO;
import com.tresbu.trakeye.service.dto.UserUIDTO;
import com.tresbu.trakeye.service.mapper.CustomerMapper;
import com.tresbu.trakeye.service.mapper.UserMapper;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);
    
    @Inject
    private CustomerRepository customerTypeRepository;

    @Inject
    private UserRepository userRepository;
    
    @Inject
    private CustomerMapper customerTypeMapper;
    
    @Inject
    private UserMapper userMapper;
    
    @Inject
    private MailService mailService;
    
	@Override
	public CustomerDTO save(CustomerCreateDTO customerCreateDTO) {
        log.debug("Request to save Customer : {}", customerCreateDTO);
        Customer customerType = customerTypeMapper.customerCreateDTOToCustomer(customerCreateDTO);
        
        if (customerCreateDTO.getLangKey() == null) {
            customerType.setLangKey("en"); // default language
        } else {
            customerType.setLangKey(customerCreateDTO.getLangKey());
        }
       
        customerType.setCreatedDate(Instant.now().toEpochMilli());
        customerType.setStatus(CustomerStatus.CREATED);
        customerType = customerTypeRepository.save(customerType);
        CustomerDTO result = customerTypeMapper.customerTocustomerDTO(customerType);
        
    	mailService.sendCustomerCreationEmail(result, getUserWithAuthoritySuperAdmin());
    	
    	return result;
	}

	@Override
	public List<CustomerDTO> findAll() {
		// TODO Auto-generated method stub
        log.debug("Request to get Customer's");
        List<Customer> customers = customerTypeRepository.findAll();
		return customerTypeMapper.customersToCustomerDTOs(customers);
	}

	@Override
	public CustomerDTO findOne(Long id) {
		// TODO Auto-generated method stub
        log.debug("Request to get Customer with id : {}", id);
        Customer customer = customerTypeRepository.findOne(id);
        CustomerDTO customerDTO = customerTypeMapper.customerTocustomerDTO(customer);
		return customerDTO;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
        log.debug("Request to delete Customer : {}", id);
        customerTypeRepository.delete(id);
	}

	@Override
	public CustomerDTO update(CustomerDTO customerDTO) {
		// TODO Auto-generated method stub
        log.debug("Request to update Customer : {}", customerDTO);
   	 Optional<Customer> customer = customerTypeRepository.findOneById(customerDTO.getId());
   	 if(customer.isPresent()){
        Customer customerObj = customer.get();
        customerObj = customerTypeMapper.customerDTOToCustomer(customerDTO);
        customerTypeRepository.save(customerObj);
   	 }
		return null;
	}

    @Override
    public UserUIDTO getUserWithAuthoritySuperAdmin(){
    	User user = userRepository.findUserWithAuthoritySuperAdmin(AuthoritiesConstants.SUPER_ADMIN);    	    	    	
    	return userMapper.userToUserUIDTO(user);
    }
}
