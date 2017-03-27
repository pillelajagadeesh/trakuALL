package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.tresbu.trakeye.domain.LiveLogs;
import com.tresbu.trakeye.service.dto.TrCaseDTO;

public class AgentDashBoardDTO implements  Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6021449897958832460L;
	
	
	private List<LiveLogs> liveLogs;
	
	private Map<String,String> caseCounts;
	
	private Map<String,String> notificationCounts;
	
	private List<TrCaseDTO> trCases;
	
	
	
	public List<LiveLogs> getLiveLogs() {
		return liveLogs;
	}
	public void setLiveLogs(List<LiveLogs> liveLogs) {
		this.liveLogs = liveLogs;
	}
	public Map<String, String> getCaseCounts() {
		return caseCounts;
	}
	public void setCaseCounts(Map<String, String> caseCounts) {
		this.caseCounts = caseCounts;
	}
	public Map<String, String> getNotificationCounts() {
		return notificationCounts;
	}
	public void setNotificationCounts(Map<String, String> notificationCounts) {
		this.notificationCounts = notificationCounts;
	}
	public List<TrCaseDTO> getTrCases() {
		return trCases;
	}
	public void setTrCases(List<TrCaseDTO> trCases) {
		this.trCases = trCases;
	}
	
	
	
	
	
	

}
