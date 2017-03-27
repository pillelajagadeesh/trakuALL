package com.tresbu.trakeye.domain;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A ServiceType.
 */
@Entity
@Table(name = "trakeye_service_type",uniqueConstraints=
@UniqueConstraint(name="service_type_unique",columnNames={"tenant_id", "name"}))
public class ServiceType implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public ServiceType(){
    	
    }
  
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date")
    private long createdDate;

    @Column(name = "updated_date")
    private long updatedDate;

    @ManyToOne
    private User user;
    
    @JsonIgnore
	@ManyToOne
	private Tenant tenant;
   
    @OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "trakeye_servicetype_trakeye_attribute", joinColumns = { @JoinColumn(name = "trakeye_servicetype_id") }, inverseJoinColumns = { @JoinColumn(name = "trakeye_servicetype_attribute_id") })
	public Set<ServiceTypeAttribute> serviceTypeAttribute = new HashSet<ServiceTypeAttribute>(); 
    
    
    public ServiceType(Long id, String name, String description, long createdDate, long updatedDate,
			User user, Set<ServiceTypeAttribute> serviceTypeAttribute) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.user = user;
		this.serviceTypeAttribute = serviceTypeAttribute;
	}
    
    
    public Set<ServiceTypeAttribute> getServiceTypeAttribute() {
		return serviceTypeAttribute;
	}


	public void setServiceTypeAttribute(Set<ServiceTypeAttribute> serviceTypeAttribute) {
		this.serviceTypeAttribute = serviceTypeAttribute;
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

    public ServiceType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public ServiceType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public ServiceType createdDate(long createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getUpdatedDate() {
        return updatedDate;
    }

    public ServiceType updatedDate(long updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public void setUpdatedDate(long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public User getUser() {
        return user;
    }

    public ServiceType user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    

    public Tenant getTenant() {
		return tenant;
	}


	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}


	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceType serviceType = (ServiceType) o;
        if(serviceType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, serviceType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceType{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", createdDate='" + createdDate + "'" +
            ", updatedDate='" + updatedDate + "'" +
            '}';
    }
}
