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
 * A CaseType.
 */
@Entity
@Table(name = "trakeye_case_type",uniqueConstraints=
@UniqueConstraint(name="case_type_unique",columnNames={"tenant_id", "name"}))
public class CaseType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date")
    private long createdDate;

    @Column(name = "update_date")
    private long updateDate;

    @ManyToOne
    private User user;
    
    @OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "trakeye_casetype_trakeye_attribute", joinColumns = { @JoinColumn(name = "trakeye_casetype_id") }, inverseJoinColumns = { @JoinColumn(name = "trakeye_casetype_attribute_id") })
	private Set<CaseTypeAttribute> caseTypeAttribute = new HashSet<CaseTypeAttribute>(); 
    
    @JsonIgnore
	@ManyToOne
	private Tenant tenant;
    
    public Set<CaseTypeAttribute> getCaseTypeAttribute() {
		return caseTypeAttribute;
	}

	public void setCaseTypeAttribute(Set<CaseTypeAttribute> caseTypeAttribute) {
		this.caseTypeAttribute = caseTypeAttribute;
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

    public CaseType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public CaseType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public CaseType createdDate(long createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public CaseType updateDate(long updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public User getUser() {
        return user;
    }

    public CaseType user(User user) {
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
        CaseType caseType = (CaseType) o;
        if(caseType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, caseType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CaseType{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", createdDate='" + createdDate + "'" +
            ", updateDate='" + updateDate + "'" +
            '}';
    }
}
