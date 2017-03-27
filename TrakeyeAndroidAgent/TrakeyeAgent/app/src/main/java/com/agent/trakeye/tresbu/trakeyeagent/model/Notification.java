package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;

/**
 * Created by Tresbu on 24-Oct-16.
 */

public class Notification implements Serializable{
    long id;
    String createdDate;
    String description;
    NotificationStatus status;

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    String notificationType;
    String downloadLink;
    String subject;
    AlertType alertType;
    long fromUserId;
    String fromUserName;
    long toUserId;
    String toUserName;
    long trCaseId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public long getTrCaseId() {
        return trCaseId;
    }

    public void setTrCaseId(long trCaseId) {
        this.trCaseId = trCaseId;
    }


    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", createdDate='" + createdDate + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", downloadLink=" + downloadLink +
                ", notificationType=" + notificationType +
                ", subject='" + subject + '\'' +
                ", alertType=" + alertType +
                ", fromUserId=" + fromUserId +
                ", fromUserName='" + fromUserName + '\'' +
                ", toUserId=" + toUserId +
                ", toUserName='" + toUserName + '\'' +
                ", trCaseId=" + trCaseId +
                '}';
    }
}
