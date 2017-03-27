package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.tresbu.trakeye.domain.ServiceTypeAttributeValue;
import com.tresbu.trakeye.domain.enumeration.ServiceStatus;

/**
 * A DTO for the TrService entity.
 */
public class TrServiceDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 9015565413668568587L;

	private Long id;

    private long createdDate;

    private long modifiedDate;

    private String description;

    private long serviceDate;

    private ServiceStatus status;

    private String notes;
    
    private Long reportedBy;
    
    private Long user;
    
    private Long updatedBy;
    
    private TrCaseDTO trCase;
    
	private Set<ServiceImageDTO> serviceImages = new HashSet<>();
    
    private Set<ServiceTypeAttributeValue> serviceTypeAttributeValues ;

	public Set<ServiceTypeAttributeValue> getServiceTypeAttributeValues() {
		return serviceTypeAttributeValues;
	}

	public void setServiceTypeAttributeValues(Set<ServiceTypeAttributeValue> serviceTypeAttributeValues) {
		this.serviceTypeAttributeValues = serviceTypeAttributeValues;
	}
    
	private ServiceTypeDTO serviceType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }
    public long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public long getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(long serviceDate) {
        this.serviceDate = serviceDate;
    }
    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TrServiceDTO trServiceDTO = (TrServiceDTO) o;

        if ( ! Objects.equals(id, trServiceDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

	

	public Long getReportedBy() {
		return reportedBy;
	}

	public void setReportedBy(Long reportedBy) {
		this.reportedBy = reportedBy;
	}

	public Long getUser() {
		return user;
	}

	public void setUser(Long user) {
		this.user = user;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public TrCaseDTO getTrCase() {
		return trCase;
	}

	public void setTrCase(TrCaseDTO trCase) {
		this.trCase = trCase;
	}

	public ServiceTypeDTO getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceTypeDTO serviceType) {
		this.serviceType = serviceType;
	}

	@Override
	public String toString() {
		return "TrServiceDTO [id=" + id + ", createdDate=" + createdDate + ", modifiedDate=" + modifiedDate
				+ ", description=" + description + ", serviceDate=" + serviceDate + ", status=" + status + ", notes="
				+ notes + ", reportedBy=" + reportedBy + ", user=" + user + ", updatedBy=" + updatedBy + ", trCase="
				+ trCase + ", serviceTypeAttributeValues=" + serviceTypeAttributeValues + ", serviceType=" + serviceType
				+ "]";
	}

	public Set<ServiceImageDTO> getServiceImages() {
		return serviceImages;
	}

	public void setServiceImages(Set<ServiceImageDTO> serviceImages) {
		this.serviceImages = serviceImages;
	}

}
