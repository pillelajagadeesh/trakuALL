package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.tresbu.trakeye.domain.ServiceTypeAttribute;

import io.swagger.annotations.ApiModelProperty;

/**
 * A DTO for the ServiceType entity.
 */
public class ServiceTypeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	@NotNull
	@ApiModelProperty(required = true, value = "Mandatory")
	private String name;

	private String description;

	private long createdDate;

	private long updatedDate;

	@NotNull
	@ApiModelProperty(required = true, value = "Mandatory")
	private Set<ServiceTypeAttribute> serviceTypeAttribute;

	public Set<ServiceTypeAttribute> getServiceTypeAttribute() {
		return serviceTypeAttribute;
	}

	public void setServiceTypeAttribute(Set<ServiceTypeAttribute> serviceTypeAttribute) {
		this.serviceTypeAttribute = serviceTypeAttribute;
	}
	private Long userId;
	private String createdBy;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

	public long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ServiceTypeDTO serviceTypeDTO = (ServiceTypeDTO) o;

		if (!Objects.equals(id, serviceTypeDTO.id))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "ServiceTypeDTO{" + "id=" + id + ", name='" + name + "'" + ", description='" + description + "'"
				+ ", createdDate='" + createdDate + "'" + ", updatedDate='" + updatedDate + "'" + '}';
	}
}
