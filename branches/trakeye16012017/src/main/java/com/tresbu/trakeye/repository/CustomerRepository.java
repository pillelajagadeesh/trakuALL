package com.tresbu.trakeye.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tresbu.trakeye.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	Optional<Customer> findOneByEmail(String email);
	
	Optional<Customer> findOneByFirstName(String firstName);

	Optional<Customer> findOneById(Long customerId);

	@Query(value = "select distinct customer from Customer customer order by customer.id desc", 
			countQuery = "select count(customer) from Customer customer")
	Page<Customer> findAllCustomers(Pageable pageable);
	
	@Query(value = "select distinct customer from Customer customer where (customer.firstName like :searchText% or customer.lastName like :searchText% "
			+ " or customer.mobilePhone like :searchText% or customer.email like :searchText% or customer.organization like :searchText%)  order by customer.id desc", countQuery = "select count(customer) from Customer customer where "
					+ "(customer.firstName like :searchText% or customer.email like :searchText% "
			+ " or customer.mobilePhone like :searchText% or customer.organization like :searchText%)")
	Page<Customer> findAllCustomersBySearchText(@Param("searchText")String searchText, Pageable pageable);

}
