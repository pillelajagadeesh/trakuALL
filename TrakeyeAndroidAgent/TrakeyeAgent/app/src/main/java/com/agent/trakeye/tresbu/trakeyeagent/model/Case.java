package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Tresbu on 6/23/2016.
 */
public class Case extends CaseRequest implements Serializable {


    long assignedById;
    String assignedByUser;
    long assignedToId;
    String assignedToUser;
    long caseTypeId;
    String caseTypeName;
    String createDate;

    long id;

    long reportedById;
    String reportedByUser;
    CaseStatus status;
    String updateDate;
    long updatedById;
    String updatedByUser;


    public long getAssignedById() {
        return assignedById;
    }

    public void setAssignedById(long assignedById) {
        this.assignedById = assignedById;
    }

    public String getAssignedByUser() {
        return assignedByUser;
    }

    public void setAssignedByUser(String assignedByUser) {
        this.assignedByUser = assignedByUser;
    }

    public long getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(long assignedToId) {
        this.assignedToId = assignedToId;
    }

    public String getAssignedToUser() {
        return assignedToUser;
    }

    public void setAssignedToUser(String assignedToUser) {
        this.assignedToUser = assignedToUser;
    }

    public long getCaseTypeId() {
        return caseTypeId;
    }

    public void setCaseTypeId(long caseTypeId) {
        this.caseTypeId = caseTypeId;
    }

    public String getCaseTypeName() {
        return caseTypeName;
    }

    public void setCaseTypeName(String caseTypeName) {
        this.caseTypeName = caseTypeName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getReportedById() {
        return reportedById;
    }

    public void setReportedById(long reportedById) {
        this.reportedById = reportedById;
    }

    public String getReportedByUser() {
        return reportedByUser;
    }

    public void setReportedByUser(String reportedByUser) {
        this.reportedByUser = reportedByUser;
    }

    public CaseStatus getStatus() {
        return status;
    }

    public void setStatus(CaseStatus status) {
        this.status = status;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public long getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(long updatedById) {
        this.updatedById = updatedById;
    }

    public String getUpdatedByUser() {
        return updatedByUser;
    }

    public void setUpdatedByUser(String updatedByUser) {
        this.updatedByUser = updatedByUser;
    }


    @Override
    public String toString() {
        return "Issue{" +
                "address='" + address + '\'' +
                ", assignedById=" + assignedById +
                ", assignedByUser='" + assignedByUser + '\'' +
                ", assignedToId=" + assignedToId +
                ", assignedToUser='" + assignedToUser + '\'' +
                ", caseTypeId=" + caseTypeId +
                ", caseTypeName='" + caseTypeName + '\'' +
                ", createDate='" + createDate + '\'' +
                ", description='" + description + '\'' +
                ", escalated=" + escalated +
                ", id=" + id +
                ", pinLat=" + pinLat +
                ", pinLong=" + pinLong +
                ", reportedById=" + reportedById +
                ", reportedByUser='" + reportedByUser + '\'' +
                ", status='" + status + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", updatedById=" + updatedById +
                ", updatedByUser='" + updatedByUser + '\'' +
                '}';
    }
}
