package com.agent.trakeye.tresbu.trakeyeagent.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Tresbu on 24-Oct-16.
 */

public class AgentDashboard implements Serializable {

    CaseCounts caseCounts;
    NotificationCounts notificationCounts;
    ArrayList<LatestUserInfo> liveLogs;

    public NotificationCounts getNotificationCounts() {
        return notificationCounts;
    }

    public void setNotificationCounts(NotificationCounts notificationCounts) {
        this.notificationCounts = notificationCounts;
    }

    public CaseCounts getCaseCounts() {
        return caseCounts;
    }

    public void setCaseCounts(CaseCounts caseCounts) {
        this.caseCounts = caseCounts;
    }

    public ArrayList<LatestUserInfo> getLiveLogs() {
        return liveLogs;
    }

    public void setLiveLogs(ArrayList<LatestUserInfo> liveLogs) {
        this.liveLogs = liveLogs;
    }
}
