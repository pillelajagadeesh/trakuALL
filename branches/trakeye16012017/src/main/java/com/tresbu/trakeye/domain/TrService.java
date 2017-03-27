package com.tresbu.trakeye.domain;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tresbu.trakeye.domain.enumeration.ServiceStatus;

/**
 * A TrService.
 */
@Entity
@Table(name = "trakeye_trservice")
public class TrService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "created_date")
    private long createdDate;

    @Column(name = "modified_date")
    private long modifiedDate;

    @Column(name = "description")
    private String description;

    @Column(name = "service_date")
    private long serviceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ServiceStatus status;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    
    @ManyToOne(fetch = FetchType.EAGER) 
    private User reportedBy;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private User updatedBy;
    
    
    @JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
    private ServiceType serviceType;
    
    
    @OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "trakeye_service_trakeye_attributevalue", joinColumns = { @JoinColumn(name = "trakeye_service_id") }, inverseJoinColumns = { @JoinColumn(name = "trakeye_servicetype_attribute_value_id") })
    private Set<ServiceTypeAttributeValue> serviceTypeAttributeValues = new HashSet<>();
    
    @OneToMany(cascade = CascadeType.ALL,mappedBy="trService")
   	private Set<ServiceImage> serviceImages = new HashSet<>();
    
    @JsonIgnore
	@ManyToOne
	private Tenant tenant;
    
	public Set<ServiceTypeAttributeValue> getServiceTypeAttributeValues() {
		return serviceTypeAttributeValues;
	}

	public void setServiceTypeAttributeValues(Set<ServiceTypeAttributeValue> serviceTypeAttributeValues) {
		this.serviceTypeAttributeValues = serviceTypeAttributeValues;
	}

    public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	@ManyToOne
    private TrCase trCase;
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public TrService createdDate(long createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public TrService modifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public void setModifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public TrService description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getServiceDate() {
        return serviceDate;
    }

    public TrService serviceDate(long serviceDate) {
        this.serviceDate = serviceDate;
        return this;
    }

    public void setServiceDate(long serviceDate) {
        this.serviceDate = serviceDate;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public TrService status(ServiceStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public TrService notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getUser() {
        return user;
    }

    public TrService user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TrCase getTrCase() {
        return trCase;
    }

    public TrService trCase(TrCase trCase) {
        this.trCase = trCase;
        return this;
    }

    public void setTrCase(TrCase trCase) {
        this.trCase = trCase;
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
        TrService trService = (TrService) o;
        if(trService.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, trService.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


	@Override
	public String toString() {
		return "TrService [id=" + id + ", createdDate=" + createdDate + ", modifiedDate=" + modifiedDate
				+ ", description=" + description + ", serviceDate=" + serviceDate + ", status=" + status + ", notes="
				+ notes + ", user=" + user + ", reportedBy=" + reportedBy + ", updatedBy=" + updatedBy
				+ ", serviceType=" + serviceType + ", serviceTypeAttributeValues=" + serviceTypeAttributeValues
				+ ", trCase=" + trCase + "]";
	}

	public User getReportedBy() {
		return reportedBy;
	}

	public void setReportedBy(User reportedBy) {
		this.reportedBy = reportedBy;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Set<ServiceImage> getServiceImages() {
		return serviceImages;
	}

	public void setServiceImages(Set<ServiceImage> serviceImages) {
		this.serviceImages = serviceImages;
	}
    
}
