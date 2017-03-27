package com.tresbu.trakeye.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Tenant.
 */
@Entity
@Table(name = "trakeye_tenant")
public class Tenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    @Column(name = "organization", length = 50, nullable = false)
    private String organization;

    @NotNull
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    @Column(name = "login_name", length = 50, nullable = false)
    private String loginName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "created_date")
    private Long createdDate;

    @Column(name = "updated_date")
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

    public Tenant organization(String organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getLoginName() {
        return loginName;
    }

    public Tenant loginName(String loginName) {
        this.loginName = loginName;
        return this;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getEmail() {
        return email;
    }

    public Tenant email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public Tenant phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public Tenant city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public Tenant address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public Tenant createdDate(Long createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public Tenant updatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
        return this;
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
        Tenant tenant = (Tenant) o;
        if(tenant.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tenant.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tenant{" +
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
