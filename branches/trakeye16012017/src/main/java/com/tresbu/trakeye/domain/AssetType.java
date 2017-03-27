package com.tresbu.trakeye.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.tresbu.trakeye.domain.enumeration.Layout;

/**
 * A AssetType.
 */
@Entity
@Table(name = "trakeye_assettype",uniqueConstraints=
@UniqueConstraint(name="asset_type_unique",columnNames={"tenant_id", "name"}))
public class AssetType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "layout")
    private Layout layout;

    @Column(name = "colorcode")
    private String colorcode;

    @Column(name = "create_date")
    private Long createDate;

    @Column(name = "update_date")
    private Long updateDate;

    @ManyToOne
    private User user;
    
    @Type(type="org.hibernate.type.BinaryType")  
	@Column(name = "asset_image",columnDefinition = "LONGBLOB")
	private byte[] image;
    
    @JsonIgnore
	@ManyToOne
	private Tenant tenant;

   /* @OneToMany(mappedBy = "assetType")
    @JsonIgnore*/
    @OneToMany(cascade = CascadeType.ALL)
   	@JoinTable(name = "trakeye_assettype_trakeye_attribute", joinColumns = { @JoinColumn(name = "trakeye_assetyype_id") }, inverseJoinColumns = { @JoinColumn(name = "trakeye_assettype_attribute_id") })
   	private Set<AssetTypeAttribute> assetTypeAttributes = new HashSet<AssetTypeAttribute>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public AssetType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public AssetType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Layout getLayout() {
        return layout;
    }

    public AssetType layout(Layout layout) {
        this.layout = layout;
        return this;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public String getColorcode() {
        return colorcode;
    }

    public AssetType colorcode(String colorcode) {
        this.colorcode = colorcode;
        return this;
    }

    public void setColorcode(String colorcode) {
        this.colorcode = colorcode;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public AssetType createDate(Long createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public AssetType updateDate(Long updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public User getUser() {
        return user;
    }

    public AssetType user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<AssetTypeAttribute> getAssetTypeAttributes() {
        return assetTypeAttributes;
    }

    public AssetType assetTypeAttributes(Set<AssetTypeAttribute> assetTypeAttributes) {
        this.assetTypeAttributes = assetTypeAttributes;
        return this;
    }
    
    

  /*  public AssetType addAssetTypeAttribute(AssetTypeAttribute assetTypeAttribute) {
        assetTypeAttributes.add(assetTypeAttribute);
        assetTypeAttribute.setAssetType(this);
        return this;
    }

    public AssetType removeAssetTypeAttribute(AssetTypeAttribute assetTypeAttribute) {
        assetTypeAttributes.remove(assetTypeAttribute);
        assetTypeAttribute.setAssetType(null);
        return this;
    }*/

   

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public void setAssetTypeAttributes(Set<AssetTypeAttribute> assetTypeAttributes) {
        this.assetTypeAttributes = assetTypeAttributes;
    }

    public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AssetType assetType = (AssetType) o;
        if(assetType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, assetType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AssetType{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", layout='" + layout + "'" +
            ", colorcode='" + colorcode + "'" +
            ", createDate='" + createDate + "'" +
            ", updateDate='" + updateDate + "'" +
            '}';
    }
}
