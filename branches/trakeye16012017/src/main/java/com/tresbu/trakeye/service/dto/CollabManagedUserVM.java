package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.tresbu.trakeye.domain.User;

public class CollabManagedUserVM  extends CollabUserDTO implements Serializable  {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8570563342489447908L;
	public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int PASSWORD_MAX_LENGTH = 100;

    private Long id;

    private ZonedDateTime createdDate;

    private String lastModifiedBy;

    private ZonedDateTime lastModifiedDate;

    @NotNull
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    public CollabManagedUserVM() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "CollabManagedUserVM{" +
            "id=" + id +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            "} " + super.toString();
    }
}

