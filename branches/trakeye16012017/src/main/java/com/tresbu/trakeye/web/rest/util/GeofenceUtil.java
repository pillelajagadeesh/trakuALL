package com.tresbu.trakeye.web.rest.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.tresbu.trakeye.domain.Geofence;
import com.tresbu.trakeye.service.dto.LocationLogCreateDTO;

public class GeofenceUtil {

	private static boolean isPointInPolygon(List<LocationLogCreateDTO> poly, LocationLogCreateDTO point) {
		int i, j;
		boolean isValidLatLang = false;
		for (i = 0, j = poly.size() - 1; i < poly.size(); j = i++) {
			if ((((poly.get(i).getLatitude() <= point.getLatitude())
					&& (point.getLatitude() < poly.get(j).getLatitude()))
					|| ((poly.get(i).getLatitude() <= point.getLatitude())
							&& (point.getLatitude() < poly.get(i).getLatitude())))
					&& (point.getLatitude() < (poly.get(j).getLongitude() - poly.get(i).getLongitude())
							* (point.getLatitude() - poly.get(i).getLatitude())
							/ (poly.get(j).getLatitude() - poly.get(i).getLatitude()) + poly.get(i).getLongitude()))

				isValidLatLang = !isValidLatLang;
		}
		
		

		return isValidLatLang;
	}

	public static boolean findPointInAPolygon( List<LocationLogCreateDTO> poly,LocationLogCreateDTO point)
	{
	            int sides = poly.size() - 1;
	            int j = sides - 1;
	            boolean pointStatus = false;
	            LocationLogCreateDTO[] myPts = poly.toArray(new LocationLogCreateDTO[poly.size()]);
	            for (int i = 0; i < sides; i++)
	            {
	                if (myPts[i].getLongitude() < point.getLongitude() && myPts[j].getLongitude() >= point.getLongitude() || 
				      myPts[j].getLongitude()< point.getLongitude() && myPts[i].getLongitude() >= point.getLongitude())
	                {
	                    if (myPts[i].getLatitude()+ (point.getLongitude() - myPts[i].getLongitude()) / 
				        (myPts[j].getLongitude()- myPts[i].getLongitude()) * (myPts[j].getLatitude() - myPts[i].getLatitude()) < point.getLatitude())
	                    {
	                        pointStatus = !pointStatus ;                        
	                    }
	                }
	                j = i;
	            }
	            return pointStatus;
	}
	
		
	public static boolean validateGeofence(Set<Geofence> cordinates, LocationLogCreateDTO pLocationLogDTO) {
		boolean isValidGeofence = false;
		double lat = 0;
		double lang = 0;
		if (cordinates != null && !cordinates.isEmpty() && pLocationLogDTO != null) {
			for (Geofence geofence : cordinates) {
				if (geofence.getCoordinates() != null && !geofence.getCoordinates().isEmpty()) {
					String strValue = geofence.getCoordinates().replace("[", "").replace("]", "").replace("{", "")
							.replace("}", "");
					String[] strValues = strValue.split(",");

					List<LocationLogCreateDTO> list = new ArrayList<LocationLogCreateDTO>();
					for (String latlang : strValues) {
						char quotes ='"';
						String[] latlangArray = latlang.replace("/", "").split(":");
						if (latlangArray[0] != null && latlangArray[0].equals(quotes+"lat"+quotes)) {
							lat = new Double(latlangArray[1]);
						} else {
							lang = new Double(latlangArray[1]);
						}

						if (lat != 0 && lang != 0) {
							LocationLogCreateDTO locationLogDTO = new LocationLogCreateDTO();
							locationLogDTO.setLatitude(lat);
							locationLogDTO.setLongitude(lang);
							list.add(locationLogDTO);
							lat = 0;
							lang = 0;
						}
					}
					isValidGeofence = findPointInAPolygon(list, pLocationLogDTO);
					if (isValidGeofence == true) {
						return isValidGeofence;
					}
				}
			}
		}
		return isValidGeofence;
	}
	
	public static Geofence findValidGeofence(List<Geofence> cordinates, LocationLogCreateDTO pLocationLogDTO) {
		boolean isValidGeofence = false;
		double lat = 0;
		double lang = 0;
		if (cordinates != null && !cordinates.isEmpty() && pLocationLogDTO != null) {
			for (Geofence geofence : cordinates) {
				if (geofence.getCoordinates() != null && !geofence.getCoordinates().isEmpty()) {
					String strValue = geofence.getCoordinates().replace("[", "").replace("]", "").replace("{", "")
							.replace("}", "");
					String[] strValues = strValue.split(",");

					List<LocationLogCreateDTO> list = new ArrayList<LocationLogCreateDTO>();
					for (String latlang : strValues) {
						char quotes ='"';
						String[] latlangArray = latlang.replace("/", "").split(":");
						if (latlangArray[0] != null && latlangArray[0].equals(quotes+"lat"+quotes)) {
							lat = new Double(latlangArray[1]);
						} else {
							lang = new Double(latlangArray[1]);
						}

						if (lat != 0 && lang != 0) {
							LocationLogCreateDTO locationLogDTO = new LocationLogCreateDTO();
							locationLogDTO.setLatitude(lat);
							locationLogDTO.setLongitude(lang);
							list.add(locationLogDTO);
							lat = 0;
							lang = 0;
						}
					}
					isValidGeofence = findPointInAPolygon(list, pLocationLogDTO);
					if (isValidGeofence == true) {
						return geofence;
					}
				}
			}
		}
		return null;
	}


}
