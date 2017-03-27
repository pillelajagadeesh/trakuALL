package com.tresbu.trakeye.service.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;



public class ReportDTO implements Serializable{
	
	

	public ReportDTO(long id,  String login,String name, double distance,long cases, 
			long services) {
		super();
		this.id = id;
		this.name = name;
		this.login = login;
		this.distance=distance;
		this.cases = cases;
		this.services = services;
	}
	public ReportDTO(long id,  String name, long cases, long services) {
		super();
		this.id = id;
		this.name = name;
		this.cases = cases;
		this.services = services;
	}
	
	
	
	public ReportDTO(long id, String name,String coordinates,
			long casesCriticalNew, long casesCriticalInprogress , long casesCriticalPending, long casesCriticalAssigned,long casesCriticalResolved, long casesCriticalCancelled,
			long casesHighNew, long casesHighInprogress , long casesHighPending, long casesHighAssigned,long casesHighResolved, long casesHighCancelled,
			long casesMediumNew, long casesMediumInprogress , long casesMediumPending, long casesMediumAssigned,long casesMediumResolved, long casesMediumCancelled,
			long casesLowNew, long casesLowInprogress , long casesLowPending, long casesLowAssigned,long casesLowResolved, long casesLowCancelled,
			long servicesCriticalInprogress, long servicesCriticalPending, long servicesCriticalClosed, long servicesCriticalCancelled,
			long servicesHighInprogress, long servicesHighPending, long servicesHighClosed, long servicesHighCancelled,
			long servicesMediumInprogress, long servicesMediumPending, long servicesMediumClosed, long servicesMediumCancelled,
			long servicesLowInprogress, long servicesLowPending, long servicesLowClosed, long servicesLowCancelled
			) {
		super();
		this.id = id;
		this.name = name;
		this.coordinates = coordinates;
		HashMap< String, Long> counts= new HashMap<>();
		counts.put("casesCriticalNew", casesCriticalNew);
		counts.put("casesCriticalInprogress", casesCriticalInprogress);
		counts.put("casesCriticalPending", casesCriticalPending);
		counts.put("casesCriticalAssigned", casesCriticalAssigned);
		counts.put("casesCriticalResolved", casesCriticalResolved);
		counts.put("casesCriticalCancelled", casesCriticalCancelled);
		
		counts.put("casesHighNew", casesHighNew);
		counts.put("casesHighInprogress", casesHighInprogress);
		counts.put("casesHighPending", casesHighPending);
		counts.put("casesHighAssigned", casesHighAssigned);
		counts.put("casesHighResolved", casesHighResolved);
		counts.put("casesHighCancelled", casesHighCancelled);
		
		counts.put("casesMediumNew", casesMediumNew);
		counts.put("casesMediumInprogress", casesMediumInprogress);
		counts.put("casesMediumPending", casesMediumPending);
		counts.put("casesMediumAssigned", casesMediumAssigned);
		counts.put("casesMediumResolved", casesMediumResolved);
		counts.put("casesMediumCancelled", casesMediumCancelled);
		
		counts.put("casesLowNew", casesLowNew);
		counts.put("casesLowInprogress", casesLowInprogress);
		counts.put("casesLowPending", casesLowPending);
		counts.put("casesLowAssigned", casesLowAssigned);
		counts.put("casesLowResolved", casesLowResolved);
		counts.put("casesLowCancelled", casesLowCancelled);
		
		
		counts.put("servicesCriticalInprogress", servicesCriticalInprogress);
		counts.put("servicesCriticalPending", servicesCriticalPending);
		counts.put("servicesCriticalClosed", servicesCriticalClosed);
		counts.put("servicesCriticalCancelled", servicesCriticalCancelled);
		
		counts.put("servicesHighInprogress", servicesHighInprogress);
		counts.put("servicesHighPending", servicesHighPending);
		counts.put("servicesHighClosed", servicesHighClosed);
		counts.put("servicesHighCancelled", servicesHighCancelled);
		
		counts.put("servicesMediumInprogress", servicesMediumInprogress);
		counts.put("servicesMediumPending", servicesMediumPending);
		counts.put("servicesMediumClosed", servicesMediumClosed);
		counts.put("servicesMediumCancelled", servicesMediumCancelled);
		
		counts.put("servicesLowInprogress", servicesLowInprogress);
		counts.put("servicesLowPending", servicesLowPending);
		counts.put("servicesLowClosed", servicesLowClosed);
		counts.put("servicesLowCancelled", servicesLowCancelled);
		this.counts=counts;
	}
	
	public ReportDTO(long id, String login,String name,double distance,
			long casesCreatedCriticalNew, long casesCreatedCriticalInprogress , long casesCreatedCriticalPending, long casesCreatedCriticalAssigned,long casesCreatedCriticalResolved, long casesCreatedCriticalCancelled,
			long casesCreatedHighNew, long casesCreatedHighInprogress , long casesCreatedHighPending, long casesCreatedHighAssigned,long casesCreatedHighResolved, long casesCreatedHighCancelled,
			long casesCreatedMediumNew, long casesCreatedMediumInprogress , long casesCreatedMediumPending, long casesCreatedMediumAssigned,long casesCreatedMediumResolved, long casesCreatedMediumCancelled,
			long casesCreatedLowNew, long casesCreatedLowInprogress , long casesCreatedLowPending, long casesCreatedLowAssigned,long casesCreatedLowResolved, long casesCreatedLowCancelled,
			long casesAssignedCriticalNew, long casesAssignedCriticalInprogress , long casesAssignedCriticalPending, long casesAssignedCriticalAssigned,long casesAssignedCriticalResolved, long casesAssignedCriticalCancelled,
			long casesAssignedHighNew, long casesAssignedHighInprogress , long casesAssignedHighPending, long casesAssignedHighAssigned,long casesAssignedHighResolved, long casesAssignedHighCancelled,
			long casesAssignedMediumNew, long casesAssignedMediumInprogress , long casesAssignedMediumPending, long casesAssignedMediumAssigned,long casesAssignedMediumResolved, long casesAssignedMediumCancelled,
			long casesAssignedLowNew, long casesAssignedLowInprogress , long casesAssignedLowPending, long casesAssignedLowAssigned,long casesAssignedLowResolved, long casesAssignedLowCancelled,
			long servicesCriticalInprogress, long servicesCriticalPending, long servicesCriticalClosed, long servicesCriticalCancelled,
			long servicesHighInprogress, long servicesHighPending, long servicesHighClosed, long servicesHighCancelled,
			long servicesMediumInprogress, long servicesMediumPending, long servicesMediumClosed, long servicesMediumCancelled,
			long servicesLowInprogress, long servicesLowPending, long servicesLowClosed, long servicesLowCancelled
			) {
		super();
		this.id = id;
		this.name = name;
		this.login=login;
		this.distance = distance;
		HashMap< String, Long> counts= new HashMap<>();
		counts.put("casesCreatedCriticalNew", casesCreatedCriticalNew);
		counts.put("casesCreatedCriticalInprogress", casesCreatedCriticalInprogress);
		counts.put("casesCreatedCriticalPending", casesCreatedCriticalPending);
		counts.put("casesCreatedCriticalAssigned", casesCreatedCriticalAssigned);
		counts.put("casesCreatedCriticalResolved", casesCreatedCriticalResolved);
		counts.put("casesCreatedCriticalCancelled", casesCreatedCriticalCancelled);
		
		counts.put("casesCreatedHighNew", casesCreatedHighNew);
		counts.put("casesCreatedHighInprogress", casesCreatedHighInprogress);
		counts.put("casesCreatedHighPending", casesCreatedHighPending);
		counts.put("casesCreatedHighAssigned", casesCreatedHighAssigned);
		counts.put("casesCreatedHighResolved", casesCreatedHighResolved);
		counts.put("casesCreatedHighCancelled", casesCreatedHighCancelled);
		
		counts.put("casesCreatedMediumNew", casesCreatedMediumNew);
		counts.put("casesCreatedMediumInprogress", casesCreatedMediumInprogress);
		counts.put("casesCreatedMediumPending", casesCreatedMediumPending);
		counts.put("casesCreatedMediumAssigned", casesCreatedMediumAssigned);
		counts.put("casesCreatedMediumResolved", casesCreatedMediumResolved);
		counts.put("casesCreatedMediumCancelled", casesCreatedMediumCancelled);
		
		counts.put("casesCreatedLowNew", casesCreatedLowNew);
		counts.put("casesCreatedLowInprogress", casesCreatedLowInprogress);
		counts.put("casesCreatedLowPending", casesCreatedLowPending);
		counts.put("casesCreatedLowAssigned", casesCreatedLowAssigned);
		counts.put("casesCreatedLowResolved", casesCreatedLowResolved);
		counts.put("casesCreatedLowCancelled", casesCreatedLowCancelled);
		
		
		counts.put("casesAssignedCriticalNew", casesAssignedCriticalNew);
		counts.put("casesAssignedCriticalInprogress", casesAssignedCriticalInprogress);
		counts.put("casesAssignedCriticalPending", casesAssignedCriticalPending);
		counts.put("casesAssignedCriticalAssigned", casesAssignedCriticalAssigned);
		counts.put("casesAssignedCriticalResolved", casesAssignedCriticalResolved);
		counts.put("casesAssignedCriticalCancelled", casesAssignedCriticalCancelled);
		
		counts.put("casesAssignedHighNew", casesAssignedHighNew);
		counts.put("casesAssignedHighInprogress", casesAssignedHighInprogress);
		counts.put("casesAssignedHighPending", casesAssignedHighPending);
		counts.put("casesAssignedHighAssigned", casesAssignedHighAssigned);
		counts.put("casesAssignedHighResolved", casesAssignedHighResolved);
		counts.put("casesAssignedHighCancelled", casesAssignedHighCancelled);
		
		counts.put("casesAssignedMediumNew", casesAssignedMediumNew);
		counts.put("casesAssignedMediumInprogress", casesAssignedMediumInprogress);
		counts.put("casesAssignedMediumPending", casesAssignedMediumPending);
		counts.put("casesAssignedMediumAssigned", casesAssignedMediumAssigned);
		counts.put("casesAssignedMediumResolved", casesAssignedMediumResolved);
		counts.put("casesAssignedMediumCancelled", casesAssignedMediumCancelled);
		
		counts.put("casesAssignedLowNew", casesAssignedLowNew);
		counts.put("casesAssignedLowInprogress", casesAssignedLowInprogress);
		counts.put("casesAssignedLowPending", casesAssignedLowPending);
		counts.put("casesAssignedLowAssigned", casesAssignedLowAssigned);
		counts.put("casesAssignedLowResolved", casesAssignedLowResolved);
		counts.put("casesAssignedLowCancelled", casesAssignedLowCancelled);
		
		
		counts.put("servicesCriticalInprogress", servicesCriticalInprogress);
		counts.put("servicesCriticalPending", servicesCriticalPending);
		counts.put("servicesCriticalClosed", servicesCriticalClosed);
		counts.put("servicesCriticalCancelled", servicesCriticalCancelled);
		
		counts.put("servicesHighInprogress", servicesHighInprogress);
		counts.put("servicesHighPending", servicesHighPending);
		counts.put("servicesHighClosed", servicesHighClosed);
		counts.put("servicesHighCancelled", servicesHighCancelled);
		
		counts.put("servicesMediumInprogress", servicesMediumInprogress);
		counts.put("servicesMediumPending", servicesMediumPending);
		counts.put("servicesMediumClosed", servicesMediumClosed);
		counts.put("servicesMediumCancelled", servicesMediumCancelled);
		
		counts.put("servicesLowInprogress", servicesLowInprogress);
		counts.put("servicesLowPending", servicesLowPending);
		counts.put("servicesLowClosed", servicesLowClosed);
		counts.put("servicesLowCancelled", servicesLowCancelled);
		this.counts=counts;
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String name;
	
	private String login;
	
	private int fromTime;
	
	private int toTime;
	
	private long cases;
	
	private long services;
	
	private String coordinates;
	
	private double distance;
	
	private List<LatLongDTO> locations; 
	
	private HashMap<String, Long> counts;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFromTime() {
		return fromTime;
	}

	public void setFromTime(int fromTime) {
		this.fromTime = fromTime;
	}

	public int getToTime() {
		return toTime;
	}

	public void setToTime(int toTime) {
		this.toTime = toTime;
	}
	public long getCases() {
		return cases;
	}
	public void setCases(long cases) {
		this.cases = cases;
	}
	public long getServices() {
		return services;
	}
	public void setServices(long services) {
		this.services = services;
	}
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance2) {
		this.distance = distance2;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public HashMap<String, Long> getCounts() {
		return counts;
	}
	public void setCounts(HashMap<String, Long> counts) {
		this.counts = counts;
	}
	public List<LatLongDTO> getLocations() {
		return locations;
	}
	public void setLocations(List<LatLongDTO> locations) {
		this.locations = locations;
	}
	public String getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(String coordicates) {
		this.coordinates = coordicates;
	}
	
	
		
	
}
