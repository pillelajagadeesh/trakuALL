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
 * A TrakeyeType.
 */
@Entity
@Table(name = "trakeye_type",uniqueConstraints=
@UniqueConstraint(name="trakeye_type_unique",columnNames={"tenant_id", "name"}))
public class TrakeyeType implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -1642736099842646368L;

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

	@Column(name = "updated_date")
	private long updatedDate;

	@ManyToOne
	private User user;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "trakeye_type_trakeye_attribute", joinColumns = { @JoinColumn(name = "trakeye_type_id") }, inverseJoinColumns = { @JoinColumn(name = "trakeye_type_attribute_id") })
    private Set<TrakeyeTypeAttribute> trakeyeTypeAttribute=new HashSet<TrakeyeTypeAttribute>();
	
	@JsonIgnore
	@ManyToOne
	private Tenant tenant;

	

	public TrakeyeType() {
	}
	
	public TrakeyeType(Long id, String name, String description, long createdDate, long updatedDate, User user,
			Set<TrakeyeTypeAttribute> trakeyeTypeAttribute) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.user = user;
		this.trakeyeTypeAttribute = trakeyeTypeAttribute;
	}

	public TrakeyeType trakeyeTypeAttribute(Set<TrakeyeTypeAttribute> trakeyeTypeAttribute) {
		this.trakeyeTypeAttribute = trakeyeTypeAttribute;
		return this;
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

	public TrakeyeType name(String name) {
		this.name = name;
		return this;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public TrakeyeType description(String description) {
		this.description = description;
		return this;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCreatedDate() {
		return createdDate;
	}

	public TrakeyeType createdDate(long createdDate) {
		this.createdDate = createdDate;
		return this;
	}

	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

	public long getUpdatedDate() {
		return updatedDate;
	}

	public TrakeyeType updatedDate(long updatedDate) {
		this.updatedDate = updatedDate;
		return this;
	}

	public void setUpdatedDate(long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public User getUser() {
		return user;
	}

	public TrakeyeType user(User user) {
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
		TrakeyeType trakeyeType = (TrakeyeType) o;
		if (trakeyeType.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, trakeyeType.id);
	}

	
	public Set<TrakeyeTypeAttribute> getTrakeyeTypeAttribute() {
		return trakeyeTypeAttribute;
	}

	public void setTrakeyeTypeAttribute(Set<TrakeyeTypeAttribute> trakeyeTypeAttribute) {
		this.trakeyeTypeAttribute = trakeyeTypeAttribute;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "TrakeyeType [id=" + id + ", name=" + name + ", description=" + description + ", createdDate=" + createdDate + ", updatedDate="
				+ updatedDate + ", user=" + user + ", attributes=" + trakeyeTypeAttribute + "]";
	}

}
