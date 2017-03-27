package com.tresbu.trakeye.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.tresbu.trakeye.domain.Customer;
import com.tresbu.trakeye.service.dto.CustomerCreateDTO;
import com.tresbu.trakeye.service.dto.CustomerDTO;
import com.tresbu.trakeye.service.dto.CustomerUpdateDTO;

@Mapper(componentModel = "spring", uses = {TrakeyeTypeMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    CustomerDTO customerTocustomerDTO(Customer customer);

    List<CustomerDTO> customersToCustomerDTOs(List<Customer> customer);

    Customer customerDTOToCustomer(CustomerDTO customerDTO);
    
    //Customer customerCreateDTOToCustomer(CustomerDTO customerDTO);
    
    Customer customerCreateDTOToCustomer(CustomerCreateDTO customerCreateDTO);
    
    Customer customerUpdateDTOToCustomer(CustomerUpdateDTO customerUpdateDTO);
/*    List<Customer> customerDTOsToCustomers(List<CustomerDTO> customerDTO);*/
}
