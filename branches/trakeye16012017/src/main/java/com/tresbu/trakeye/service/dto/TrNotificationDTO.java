package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.domain.enumeration.AlertType;
import com.tresbu.trakeye.domain.enumeration.NotificationStatus;
import com.tresbu.trakeye.domain.enumeration.NotificationType;

import io.swagger.annotations.ApiModelProperty;

/**
 * A DTO for the TrNotification entity.
 */
public class TrNotificationDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private long createdDate;

	private String description;

	private NotificationStatus status;

	private NotificationType notificationType;

	private String downloadLink;
	
	private String caseIdLink;

	private UserUIDTO fromUser;

	private UserUIDTO toUser;

	@NotNull
	private String subject;

	private AlertType alertType;

	private Long fromUserId;

	@ApiModelProperty(notes="Not required to give from user value")
	public UserUIDTO getFromUser() {
		return fromUser;
	}

	public void setFromUser(UserUIDTO fromUser) {
		this.fromUser = fromUser;
	}

	@ApiModelProperty(required= true, notes="Notification to user mandatory and useruidto values should be given. This is user to whom notification has to be sent")
	public UserUIDTO getToUser() {
		return toUser;
	}

	public void setToUser(UserUIDTO toUser) {
		this.toUser = toUser;
	}

	@ApiModelProperty( notes="Notification download link is not mandatory. ")
	public String getDownloadLink() {
		return downloadLink;
	}

	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}

	@ApiModelProperty(notes="Not required to give from user name")
	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	@ApiModelProperty(required = true, notes="Notification to username is mandatory and string value should be given. This is username to whom notification has to be sent")
	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	private String fromUserName;
	private Long toUserId;
	private String toUserName;
	private Long trCaseId;

	@ApiModelProperty(notes="Not required to give notification id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ApiModelProperty(notes="Not required to give notification created date")
	public long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
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

	@ApiModelProperty(notes="Notification type is not mandatory and should be a string value. ex: A or C or S")
	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	@ApiModelProperty(required=true, notes="Notification subject is mandatory and should be a string value. ")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@ApiModelProperty( notes="Notification alert type is not mandatory. Should be string value. ex: EMAIL or SMS ")
	public AlertType getAlertType() {
		return alertType;
	}

	public void setAlertType(AlertType alertType) {
		this.alertType = alertType;
	}

	@ApiModelProperty(notes="Not required to give this value")
	public Long getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(Long userId) {
		this.fromUserId = userId;
	}
	
	@ApiModelProperty(required=true, notes="Notification to useris is mandatory and string value should be given. This is userid to whom notification has to be sent")
	public Long getToUserId() {
		return toUserId;
	}

	public void setToUserId(Long userId) {
		this.toUserId = userId;
	}

	@ApiModelProperty(notes="Notification case id is not mandatory and long value should be given. This is id of the case which should be sent in notification. ex: 1022")
	public Long getTrCaseId() {
		return trCaseId;
	}

	public void setTrCaseId(Long trCaseId) {
		this.trCaseId = trCaseId;
	}
	
	

	public String getCaseIdLink() {
		return caseIdLink;
	}

	public void setCaseIdLink(String caseIdLink) {
		this.caseIdLink = caseIdLink;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		TrNotificationDTO trNotificationDTO = (TrNotificationDTO) o;

		if (!Objects.equals(id, trNotificationDTO.id))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "TrNotificationDTO [id=" + id + ", createdDate=" + createdDate + ", description=" + description
				+ ", status=" + status + ", downloadLink=" + downloadLink + ", subject=" + subject + ", alertType="
				+ alertType + ", fromUserId=" + fromUserId + ", fromUserName=" + fromUserName + ", toUserId=" + toUserId
				+ ", toUserName=" + toUserName + ", trCaseId=" + trCaseId + "]";
	}

}
