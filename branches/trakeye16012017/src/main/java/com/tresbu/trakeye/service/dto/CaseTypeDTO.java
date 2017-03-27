package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import com.tresbu.trakeye.domain.CaseTypeAttribute;

/**
 * A DTO for the CaseType entity.
 */
public class CaseTypeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private String description;

	private long createdDate;

	private long updateDate;

	private Long userId;

	private String createdBy;

	private UserIdDTO user;

	private Set<CaseTypeAttribute> caseTypeAttribute;

	public UserIdDTO getUser() {
		return user;
	}

	public void setUser(UserIdDTO user) {
		this.user = user;
	}

	public Set<CaseTypeAttribute> getCaseTypeAttribute() {
		return caseTypeAttribute;
	}

	public void setCaseTypeAttribute(Set<CaseTypeAttribute> caseTypeAttribute) {
		this.caseTypeAttribute = caseTypeAttribute;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

	public long getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(long updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		CaseTypeDTO caseTypeDTO = (CaseTypeDTO) o;

		if (!Objects.equals(id, caseTypeDTO.id))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "CaseTypeDTO [id=" + id + ", name=" + name + ", description=" + description + ", createdDate="
				+ createdDate + ", updateDate=" + updateDate + ", userId=" + userId + ", createdBy=" + createdBy
				+ ", caseTypeAttribute=" + caseTypeAttribute + "]";
	}

}
