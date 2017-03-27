package com.tresbu.trakeye.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Tenant entity.
 */
public class TenantDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String organization;

    @NotNull
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String loginName;

    private String email;

    private String phone;

    private String city;

    private String address;

    private Long createdDate;

    private Long updatedDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TenantDTO tenantDTO = (TenantDTO) o;

        if ( ! Objects.equals(id, tenantDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TenantDTO{" +
            "id=" + id +
            ", organization='" + organization + "'" +
            ", loginName='" + loginName + "'" +
            ", email='" + email + "'" +
            ", phone='" + phone + "'" +
            ", city='" + city + "'" +
            ", address='" + address + "'" +
            ", createdDate='" + createdDate + "'" +
            ", updatedDate='" + updatedDate + "'" +
            '}';
    }
}
