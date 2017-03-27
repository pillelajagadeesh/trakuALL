package com.tresbu.trakeye.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AssetTypeAttribute.
 */
@Entity
@Table(name = "trakeye_assettype_attribute")
public class AssetTypeAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public AssetTypeAttribute name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AssetTypeAttribute assetTypeAttribute = (AssetTypeAttribute) o;
        if(assetTypeAttribute.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, assetTypeAttribute.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AssetTypeAttribute{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
