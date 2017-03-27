package com.tresbu.trakeye.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Tenant entity.
 */
public class CollabTenantDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 818720937437120462L;

	private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String name;

    private String address;

    private String city;

    private String phone;

    private String email;

    @NotNull
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String organization;

    private Long createdDate;

    private Long updatedDate;


    private Long createdById;
    

    private String createdByLogin;
    
    private Boolean trakeyeTenant;

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
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }
    public Long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long userId) {
        this.createdById = userId;
    }


    public String getCreatedByLogin() {
        return createdByLogin;
    }

    public void setCreatedByLogin(String userLogin) {
        this.createdByLogin = userLogin;
    }
    

    public Boolean getTrakeyeTenant() {
		return trakeyeTenant;
	}

	public void setTrakeyeTenant(Boolean trakeyeTenant) {
		this.trakeyeTenant = trakeyeTenant;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CollabTenantDTO collabTenantDTO = (CollabTenantDTO) o;

        if ( ! Objects.equals(id, collabTenantDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CollabTenantDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", address='" + address + "'" +
            ", city='" + city + "'" +
            ", phone='" + phone + "'" +
            ", email='" + email + "'" +
            ", organization='" + organization + "'" +
            ", createdDate='" + createdDate + "'" +
            ", updatedDate='" + updatedDate + "'" +
            '}';
    }
}