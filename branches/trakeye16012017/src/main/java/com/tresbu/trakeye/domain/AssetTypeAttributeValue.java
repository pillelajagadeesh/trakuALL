package com.tresbu.trakeye.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AssetTypeAttributeValue.
 */
@Entity
@Table(name = "trakeye_assettype_attribute_value")
public class AssetTypeAttributeValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "attribute_value")
    private String attributeValue;

    @ManyToOne
    private User user;

    @ManyToOne
    private AssetTypeAttribute assetTypeAttribute;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public AssetTypeAttributeValue attributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
        return this;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public User getUser() {
        return user;
    }

    public AssetTypeAttributeValue user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AssetTypeAttribute getAssetTypeAttribute() {
        return assetTypeAttribute;
    }

    public AssetTypeAttributeValue assetTypeAttribute(AssetTypeAttribute assetTypeAttribute) {
        this.assetTypeAttribute = assetTypeAttribute;
        return this;
    }

    public void setAssetTypeAttribute(AssetTypeAttribute assetTypeAttribute) {
        this.assetTypeAttribute = assetTypeAttribute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AssetTypeAttributeValue assetTypeAttributeValue = (AssetTypeAttributeValue) o;
        if(assetTypeAttributeValue.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, assetTypeAttributeValue.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AssetTypeAttributeValue{" +
            "id=" + id +
            ", attributeValue='" + attributeValue + "'" +
            '}';
    }
}
