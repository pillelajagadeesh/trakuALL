package com.tresbu.trakeye.service.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.tresbu.trakeye.domain.enumeration.AlertType;
import com.tresbu.trakeye.domain.enumeration.NotificationStatus;

import io.swagger.annotations.ApiModelProperty;

public class TrNotificationUpdateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	private Long id;

	private String description;

	private NotificationStatus status;

	private AlertType alertType;

	private Long trCaseId;
	
	private Long toUserId;

	@NotNull
	private String subject;

	private UserIdDTO toUser;

	@ApiModelProperty(required=true, notes="Notification id is mandatory and should be long ex: 1022")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ApiModelProperty(notes="Notification description is not mandatory and should be a string value. ex: Case is registered,Case ticket number is: 1005")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ApiModelProperty(notes="Notification description is not mandatory and should be a string value. ex: SENT or RECEIVED or DELIVERED or FAILED")
	public NotificationStatus getStatus() {
		return status;
	}

	public void setStatus(NotificationStatus status) {
		this.status = status;
	}

	@ApiModelProperty( notes="Notification alert type is not mandatory. Should be string value. ex: EMAIL or SMS ")
	public AlertType getAlertType() {
		return alertType;
	}

	public void setAlertType(AlertType alertType) {
		this.alertType = alertType;
	}

	@ApiModelProperty(notes="Notification case id is not mandatory and long value should be given. This is id of the case which should be sent in notification. ex: 1022")
	public Long getTrCaseId() {
		return trCaseId;
	}

	public void setTrCaseId(Long trCaseId) {
		this.trCaseId = trCaseId;
	}

	@ApiModelProperty(required=true, notes="Notification subject is mandatory and should be a string value. ")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@ApiModelProperty(required= true, notes="Notification to user mandatory and useruidto values should be given. This is user to whom notification has to be sent")
	public UserIdDTO getToUser() {
		return toUser;
	}

	public void setToUser(UserIdDTO toUser) {
		this.toUser = toUser;
	}

	public Long getToUserId() {
		return toUserId;
	}

	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}
	
	

}
