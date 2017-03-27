package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.tresbu.trakeye.domain.TrakeyeTypeAttribute;

/**
 * A DTO for the TrakeyeType entity.
 */
public class TrakeyeTypeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	@NotNull
	private String name;

	private String description;

	private long createdDate;

	private long updatedDate;

	private Set<TrakeyeTypeAttribute> trakeyeTypeAttribute;

	private Long userId;

	private String createdBy;

	public TrakeyeTypeDTO() {
	}

	// public TrakeyeTypeDTO(TrakeyeType trakeyeType) {
	// this(trakeyeType.getId(), trakeyeType.getName(),
	// trakeyeType.getDescription(), trakeyeType.getCreatedDate(),
	// trakeyeType.getUpdatedDate(), trakeyeType.getTrakeyeTypeAttribute(),
	// trakeyeType.getUser().getId(),
	// trakeyeType.getUser());
	// }

	public TrakeyeTypeDTO(Long id, String name, String description, long createdDate, long updatedDate,
			Set<TrakeyeTypeAttribute> trakeyeTypeAttribute, Long userId, String createdBy) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.trakeyeTypeAttribute = trakeyeTypeAttribute;
		this.userId = userId;
		this.createdBy = createdBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

	public long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Set<TrakeyeTypeAttribute> getTrakeyeTypeAttribute() {
		return trakeyeTypeAttribute;
	}

	public void setTrakeyeTypeAttribute(Set<TrakeyeTypeAttribute> trakeyeTypeAttribute) {
		this.trakeyeTypeAttribute = trakeyeTypeAttribute;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		TrakeyeTypeDTO trakeyeTypeDTO = (TrakeyeTypeDTO) o;

		if (!Objects.equals(id, trakeyeTypeDTO.id))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "TrakeyeTypeDTO [id=" + id + ", name=" + name + ", description=" + description + ", createdDate="
				+ createdDate + ", updatedDate=" + updatedDate + ", trakeyeTypeAttribute=" + trakeyeTypeAttribute
				+ ", userId=" + userId + ", createdBy=" + createdBy + "]";
	}

}
