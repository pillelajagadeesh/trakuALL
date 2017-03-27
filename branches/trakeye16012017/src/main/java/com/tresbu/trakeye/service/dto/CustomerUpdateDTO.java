package com.tresbu.trakeye.service.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.tresbu.trakeye.domain.enumeration.CustomerStatus;

import io.swagger.annotations.ApiModelProperty;

public class CustomerUpdateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotNull
	private CustomerStatus status;

	@ApiModelProperty(required = true, notes="Customer Id is required while updating customer")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ApiModelProperty(required = true, notes="Status is required while updating customer")
	public CustomerStatus getStatus() {
		return status;
	}

	public void setStatus(CustomerStatus status) {
		this.status = status;
	}
		
}
