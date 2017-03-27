package com.tresbu.trakeye.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Asset.
 */
@Entity
@Table(name = "trakeye_asset")
public class Asset implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2561513141557391445L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Size(min = 3, max = 50)
	@Column(name = "name", length = 50, nullable = false)
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "create_date")
	private Long createDate;

	@Column(name = "update_date")
	private Long updateDate;

	@ManyToOne
	private User user;

	@ManyToOne
	private AssetType assetType;
	
	@JsonIgnore
	@ManyToOne
	private Tenant tenant;
	
	/*
	 * @OneToMany(mappedBy = "asset")
	 * 
	 * @JsonIgnore
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "trakeye_assettype_trakeye_attributevalue", joinColumns = {
			@JoinColumn(name = "trakeye_asset_id") }, inverseJoinColumns = {
					@JoinColumn(name = "trakeye_assettype_attribute_value_id") })
	private Set<AssetTypeAttributeValue> assetTypeAttributeValues = new HashSet<AssetTypeAttributeValue>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "asset")
	private List<AssetCoordinate> assetCoordinates = new LinkedList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Asset name(String name) {
		this.name = name;
		return this;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public Asset description(String description) {
		this.description = description;
		return this;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public Asset createDate(Long createDate) {
		this.createDate = createDate;
		return this;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public Long getUpdateDate() {
		return updateDate;
	}

	public Asset updateDate(Long updateDate) {
		this.updateDate = updateDate;
		return this;
	}

	public void setUpdateDate(Long updateDate) {
		this.updateDate = updateDate;
	}

	public User getUser() {
		return user;
	}

	public Asset user(User user) {
		this.user = user;
		return this;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public Asset assetType(AssetType assetType) {
		this.assetType = assetType;
		return this;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}

	public Set<AssetTypeAttributeValue> getAssetTypeAttributeValues() {
		return assetTypeAttributeValues;
	}

	
	/*
	 * public Asset assetTypeAttributeValues(Set<AssetTypeAttributeValue>
	 * assetTypeAttributeValues) { this.assetTypeAttributeValues =
	 * assetTypeAttributeValues; return this; }
	 */

	/*
	 * public Asset addAssetTypeAttributeValue(AssetTypeAttributeValue
	 * assetTypeAttributeValue) {
	 * assetTypeAttributeValues.add(assetTypeAttributeValue);
	 * assetTypeAttributeValue.setAsset(this); return this; }
	 * 
	 * public Asset removeAssetTypeAttributeValue(AssetTypeAttributeValue
	 * assetTypeAttributeValue) {
	 * assetTypeAttributeValues.remove(assetTypeAttributeValue);
	 * assetTypeAttributeValue.setAsset(null); return this; }
	 */

	public void setAssetTypeAttributeValues(Set<AssetTypeAttributeValue> assetTypeAttributeValues) {
		this.assetTypeAttributeValues = assetTypeAttributeValues;
	}

	/*
	 * public Asset assetCoordinates(Set<AssetCoordinate> assetCoordinates) {
	 * this.assetCoordinates = assetCoordinates; return this; }
	 */

	/*
	 * public Asset addAssetCoordinate(AssetCoordinate assetCoordinate) {
	 * assetCoordinates.add(assetCoordinate); assetCoordinate.setAsset(this);
	 * return this; }
	 * 
	 * public Asset removeAssetCoordinate(AssetCoordinate assetCoordinate) {
	 * assetCoordinates.remove(assetCoordinate); assetCoordinate.setAsset(null);
	 * return this; }
	 */
	
	

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Asset asset = (Asset) o;
		if (asset.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, asset.id);
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public List<AssetCoordinate> getAssetCoordinates() {
		return assetCoordinates;
	}

	public void setAssetCoordinates(List<AssetCoordinate> assetCoordinates) {
		this.assetCoordinates = assetCoordinates;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Asset{" + "id=" + id + ", name='" + name + "'" + ", description='" + description + "'"
				+ ", createDate='" + createDate + "'" + ", updateDate='" + updateDate + "'" + '}';
	}
}
