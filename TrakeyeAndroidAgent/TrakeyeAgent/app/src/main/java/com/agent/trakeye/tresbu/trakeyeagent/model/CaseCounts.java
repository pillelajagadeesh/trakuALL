package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;

/**
 * Created by Tresbu on 17-Oct-16.
 */

public class CaseCounts implements Serializable {
    String NEW;
    String RESOLVED;
    String INPROGRESS;
    String ASSIBNED;
    String CANCELLED;
    String PENDING;

    public String getCANCELLED() {
        return CANCELLED;
    }

    public void setCANCELLED(String CANCELLED) {
        this.CANCELLED = CANCELLED;
    }

    public String getPENDING() {
        return PENDING;
    }

    public void setPENDING(String PENDING) {
        this.PENDING = PENDING;
    }

    public String getNEW() {
        return NEW;
    }

    public void setNEW(String NEW) {
        this.NEW = NEW;
    }

    public String getRESOLVED() {
        return RESOLVED;
    }

    public void setRESOLVED(String RESOLVED) {
        this.RESOLVED = RESOLVED;
    }

    public String getASSIBNED() {
        return ASSIBNED;
    }

    public void setASSIBNED(String ASSIBNED) {
        this.ASSIBNED = ASSIBNED;
    }

    public String getINPROGRESS() {
        return INPROGRESS;
    }

    public void setINPROGRESS(String INPROGRESS) {
        this.INPROGRESS = INPROGRESS;
    }

    @Override
    public String toString() {
        return "{" +
                "NEW=" + NEW + '\'' +
                ", RESOLVED=" + RESOLVED + '\'' +
                ", ASSIBNED=" + ASSIBNED + '\'' +
                ", INPROGRESS='" + INPROGRESS + '\'' +
                ", CANCELLED='" + CANCELLED + '\'' +
                ", PENDING='" + PENDING + '\'' +
                '}';
    }
}
