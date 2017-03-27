package com.tresbu.trakeye.web.rest.vm;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.tresbu.trakeye.domain.TrakeyeType;
import com.tresbu.trakeye.domain.TrakeyeTypeAttributeValue;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.service.dto.UserDTO;

import io.swagger.annotations.ApiModelProperty;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserVM extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int PASSWORD_MAX_LENGTH = 100;

    private Long id;

    private long createdDate;

    private String lastModifiedBy;

    private long lastModifiedDate;

    @NotNull
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    public ManagedUserVM() {
    }

    public ManagedUserVM(User user) {
        super(user);
        this.id = user.getId();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.password = null;
    }

    public ManagedUserVM(Long id, String login, String password, String firstName, String lastName,
                          String email, boolean activated, String langKey, Set<String> authorities , long createdDate, String lastModifiedBy, long lastModifiedDate,Set<String> geofences,
                          TrakeyeType trakeyeType,Set<TrakeyeTypeAttributeValue> trakeyeTypeAttributeValues,int fromTime, int toTime, String phone, String imei,
                          String operatingSystem, String applicationVersion, byte[] userImage) {
        super(login, firstName, lastName, email, activated, langKey, authorities,geofences,trakeyeType,trakeyeTypeAttributeValues,fromTime,toTime,phone, imei,
        		operatingSystem,applicationVersion, userImage);
        this.id = id;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.password = password;
        
    }

   
    @ApiModelProperty(notes="Not requiredto give id while creating customer/agent. And id is mandatory while updating customer/agent")
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ApiModelProperty(notes="Not requiredto give created date time")
    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    @ApiModelProperty(notes="Not requiredto give last modified by")
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @ApiModelProperty(notes="Not requiredto give last modified date")
    public long getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(long lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @ApiModelProperty(notes="Not requiredto give last modified date")
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "ManagedUserVM{" +
            "id=" + id +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            "} " + super.toString();
    }
}
