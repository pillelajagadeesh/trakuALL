package com.tresbu.trakeye.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tresbu.trakeye.domain.enumeration.AlertType;
import com.tresbu.trakeye.domain.enumeration.NotificationStatus;
import com.tresbu.trakeye.domain.enumeration.NotificationType;

/**
 * A TrNotification.
 */
@Entity
@Table(name = "trakeye_notification")
public class TrNotification implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "created_date")
	private long createdDate;

	@Column(name = "download_link", nullable = true)
	private String downloadLink;

	@Column(name = "description")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private NotificationStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "notification_type")
	private NotificationType notificationType;

	@NotNull
	@Column(name = "subject", nullable = false)
	private String subject;

	@Enumerated(EnumType.STRING)
	@Column(name = "alert_type")
	private AlertType alertType;
	
	@JsonIgnore
	@ManyToOne
	private Tenant tenant;

	public TrNotification() {
	}
	
	public TrNotification(long createdDate,User fromUser, User toUser,TrCase trCase,NotificationStatus notificationStatus,NotificationType notificationType,
			String subject,String description,AlertType alertType, Tenant tenant){
		this.createdDate=createdDate;
		this.fromUser=fromUser;
		this.toUser=toUser;
		this.trCase=trCase;
		this.status=notificationStatus;
		this.notificationType=notificationType;
		this.subject=subject;
		this.description=description;
		this.alertType=alertType;
		this.tenant = tenant;
	}
	@ManyToOne
	private User fromUser;

	@ManyToOne
	private User toUser;

	@ManyToOne
	private TrCase trCase;
	
	public String getDownloadLink() {
		return downloadLink;
	}

	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getCreatedDate() {
		return createdDate;
	}

	public TrNotification createdDate(long createdDate) {
		this.createdDate = createdDate;
		return this;
	}

	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

	public String getDescription() {
		return description;
	}

	public TrNotification description(String description) {
		this.description = description;
		return this;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public NotificationStatus getStatus() {
		return status;
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public TrNotification status(NotificationStatus status) {
		this.status = status;
		return this;
	}

	public void setStatus(NotificationStatus status) {
		this.status = status;
	}

	public String getSubject() {
		return subject;
	}

	public TrNotification subject(String subject) {
		this.subject = subject;
		return this;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public AlertType getAlertType() {
		return alertType;
	}

	public TrNotification alertType(AlertType alertType) {
		this.alertType = alertType;
		return this;
	}

	public void setAlertType(AlertType alertType) {
		this.alertType = alertType;
	}

	public User getFromUser() {
		return fromUser;
	}

	public TrNotification fromUser(User user) {
		this.fromUser = user;
		return this;
	}

	public void setFromUser(User user) {
		this.fromUser = user;
	}

	public User getToUser() {
		return toUser;
	}

	public TrNotification toUser(User user) {
		this.toUser = user;
		return this;
	}

	public void setToUser(User user) {
		this.toUser = user;
	}

	public TrCase getTrCase() {
		return trCase;
	}

	public TrNotification trCase(TrCase trCase) {
		this.trCase = trCase;
		return this;
	}

	public void setTrCase(TrCase trCase) {
		this.trCase = trCase;
	}	
	
	
	
	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		TrNotification trNotification = (TrNotification) o;
		if (trNotification.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, trNotification.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "TrNotification [id=" + id + ", createdDate=" + createdDate + ", downloadLink=" + downloadLink
				+ ", description=" + description + ", status=" + status + ", subject=" + subject + ", alertType="
				+ alertType + ", fromUser=" + fromUser + ", toUser=" + toUser + ", trCase=" + trCase + "]";
	}

}
