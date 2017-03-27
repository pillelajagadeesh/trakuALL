package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;

/**
 * Created by Tresbu on 17-Oct-16.
 */

public class NotificationCounts implements Serializable {
    String DELIVERED;
    String FAILED;
    String SENT;
    String RECIEVED;

    public String getRECIEVED() {
        return RECIEVED;
    }

    public void setRECIEVED(String RECIEVED) {
        this.RECIEVED = RECIEVED;
    }

    public String getFAILED() {
        return FAILED;
    }

    public void setFAILED(String FAILED) {
        this.FAILED = FAILED;
    }

    public String getSENT() {
        return SENT;
    }

    public void setSENT(String SENT) {
        this.SENT = SENT;
    }

    public String getDELIVERED() {
        return DELIVERED;
    }

    public void setDELIVERED(String DELIVERED) {
        this.DELIVERED = DELIVERED;
    }

    @Override
    public String toString() {
        return "{" +
                "DELIVERED='" + DELIVERED + '\'' +
                ", RECIEVED='" + RECIEVED + '\'' +
                ", FAILED='" + FAILED + '\'' +
                ", SENT='" + SENT + '\'' +
                '}';
    }
}
