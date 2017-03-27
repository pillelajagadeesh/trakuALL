package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Tresbu on 09-Nov-16.
 */

public class UpdateCase implements Serializable {

    String caseTypeName;
    long reportedById;
    String reportedByUser;
    String updateDate;
    long updatedById;
    String updatedByUser;
    String status;
    String description;

    long id;
    long assignedById;
    String assignedByUser;
    long assignedToId;
    String assignedToUser;
    long caseTypeId;

    String createDate;
    String priority;
    ArrayList<CaseImages> caseImages;
    CaseType caseType;
    ArrayList<CaseTypeAttributeValues> caseTypeAttributeValues;
    boolean escalated;
    String geofenceName;
    double pinLat;
    double pinLong;
    String address;

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setCaseImages(ArrayList<CaseImages> caseImages) {
        this.caseImages = caseImages;
    }

    public ArrayList<CaseTypeAttributeValues> getCaseTypeAttributeValues() {
        return caseTypeAttributeValues;
    }

    public void setCaseTypeAttributeValues(ArrayList<CaseTypeAttributeValues> caseTypeAttributeValues) {
        this.caseTypeAttributeValues = caseTypeAttributeValues;
    }

    public void setEscalated(boolean escalated) {
        this.escalated = escalated;
    }

    public void setPinLat(double pinLat) {
        this.pinLat = pinLat;
    }

    public void setPinLong(double pinLong) {
        this.pinLong = pinLong;
    }

    public void setCaseType(CaseType caseType) {
        this.caseType = caseType;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAssignedByUser(String assignedByUser) {
        this.assignedByUser = assignedByUser;
    }

    public void setAssignedToUser(String assignedToUser) {
        this.assignedToUser = assignedToUser;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setReportedByUser(String reportedByUser) {
        this.reportedByUser = reportedByUser;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setUpdatedByUser(String updatedByUser) {
        this.updatedByUser = updatedByUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
