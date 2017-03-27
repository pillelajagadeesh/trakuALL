package com.tresbu.trakeye.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AssetCoordinate.
 */
@Entity
@Table(name = "trakeye_asset_coordinate")
public class AssetCoordinate implements Serializable {

   

    /**
	 * 
	 */
	private static final long serialVersionUID = 8894819579382794121L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "latitude", nullable = false, precision=15, scale=3)
    private Double latitude;

    @NotNull
    @Column(name = "longitude", nullable = false, precision=15, scale=3)
    private Double longitude;
    
    @ManyToOne
    private Asset  asset;

    public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public AssetCoordinate latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public AssetCoordinate longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AssetCoordinate assetCoordinate = (AssetCoordinate) o;
        if(assetCoordinate.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, assetCoordinate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AssetCoordinate{" +
            "id=" + id +
            ", latitude='" + latitude + "'" +
            ", longitude='" + longitude + "'" +
            '}';
    }
}
