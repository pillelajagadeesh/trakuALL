package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Tresbu on 24-Oct-16.
 */

public class Service implements Serializable {
    long id;
    String createdDate;
    String modifiedDate;
    String description;
    String serviceDate;
    ServiceStatus status;
    String notes;
    long reportedBy;
    long user;
    long updatedBy;
    Case trCase;
    ArrayList<ServiceImages> serviceImages;
    ArrayList<ServiceTypeAttributeValues> serviceTypeAttributeValues;
    ServiceType serviceType;

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

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(long reportedBy) {
        this.reportedBy = reportedBy;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Case getTrCase() {
        return trCase;
    }

    public void setTrCase(Case trCase) {
        this.trCase = trCase;
    }

    public ArrayList<ServiceTypeAttributeValues> getServiceTypeAttributeValues() {
        return serviceTypeAttributeValues;
    }

    public void setServiceTypeAttributeValues(ArrayList<ServiceTypeAttributeValues> serviceTypeAttributeValues) {
        this.serviceTypeAttributeValues = serviceTypeAttributeValues;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public ArrayList<ServiceImages> getServiceImages() {
        return serviceImages;
    }

    public void setServiceImages(ArrayList<ServiceImages> serviceImages) {
        this.serviceImages = serviceImages;
    }
}
