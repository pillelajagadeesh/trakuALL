package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Tresbu on 21-Oct-16.
 */

public class CaseRequest implements Serializable {
    String address;
    String description;
    boolean escalated;
    double pinLat;
    double pinLong;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    String priority;
    CaseType caseType;

    ArrayList<CaseImages> caseImages;
    ArrayList<CaseTypeAttributeValues> caseTypeAttributeValues;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getEscalated() {
        return escalated;
    }

    public void setEscalated(boolean escalated) {
        this.escalated = escalated;
    }

    public double getPinLat() {
        return pinLat;
    }

    public void setPinLat(double pinLat) {
        this.pinLat = pinLat;
    }

    public double getPinLong() {
        return pinLong;
    }

    public void setPinLong(double pinLong) {
        this.pinLong = pinLong;
    }

    public CaseType getCaseType() {
        return caseType;
    }

    public void setCaseType(CaseType caseType) {
        this.caseType = caseType;
    }

    public ArrayList<CaseImages> getCaseImages() {
        return caseImages;
    }

    public void setCaseImages(ArrayList<CaseImages> caseImages) {
        this.caseImages = caseImages;
    }

    public boolean isEscalated() {
        return escalated;
    }

    public ArrayList<CaseTypeAttributeValues> getCaseTypeAttributeValues() {
        return caseTypeAttributeValues;
    }

    public void setCaseTypeAttributeValues(ArrayList<CaseTypeAttributeValues> caseTypeAttributeValues) {
        this.caseTypeAttributeValues = caseTypeAttributeValues;
    }


    @Override
    public String toString() {
        return "CaseRequest{" +
                "address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", escalated=" + escalated +
                ", pinLat=" + pinLat +
                ", pinLong=" + pinLong +
                ", priority=" + priority +
                ", caseType=" + caseType +
                ", caseImages=" + caseImages +
                ", caseTypeAttributeValues=" + caseTypeAttributeValues +
                '}';
    }
}
