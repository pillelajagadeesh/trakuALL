package com.tresbu.trakeye.service.util;

import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneOffset;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tresbu.trakeye.domain.enumeration.LogSource;
import com.tresbu.trakeye.service.dto.LocationLogDTO;

/**
 * Utility class for generating random Strings.
 */
public final class RandomUtil {

    private static final int DEF_COUNT = 20;
    private static final Logger log = LoggerFactory.getLogger(RandomUtil.class);

    private RandomUtil() {
    }

    /**
     * Generates a password.
     *
     * @return the generated password
     */
    public static String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
    }

    /**
     * Generates an activation key.
     *
     * @return the generated activation key
     */
    public static String generateActivationKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }

    /**
    * Generates a reset key.
    *
    * @return the generated reset key
    */
    public static String generateResetKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }
    
    
   
    
 
	public static boolean checkFromAndToTime(int fromTime, int toTime, LogSource logSource, long createDateTime)
			throws ParseException {
		log.info("Calling  checkFromAndToTime to check time ---- LogSource in util {}", logSource);
			int	hour =0;
		if (logSource.toString().equals(LogSource.NP.toString()) || logSource.toString().equals(LogSource.NETWORK.toString()) ) {
			hour = Instant.ofEpochMilli(createDateTime).atZone(ZoneOffset.UTC).getHour();
			log.info("current hour {},fromTime {}, Totime {} with  NP ", hour,fromTime,toTime);
			
		} else if (logSource.toString().equals(LogSource.GPS.toString())) {

			/*
			 * location log creation time hour should be in between user From
			 * and To Times
			 */
			 hour = Instant.now().atZone(ZoneOffset.UTC).getHour();
			log.info("current hour {} ,from Time {} , Totime {} with GPS ", hour,fromTime,toTime);
			
		}
		if ((hour >= fromTime && hour <= toTime && fromTime <=toTime)||(!(hour < fromTime && hour > toTime) && fromTime>=toTime)) {
			return true;
		}
		return false;
		
	}
    
    
    public static String convertStringToHex(String str){

  	  char[] chars = str.toCharArray();

  	  StringBuffer hex = new StringBuffer();
  	  for(int i = 0; i < chars.length; i++){
  	    hex.append(Integer.toHexString((int)chars[i]));
  	  }

  	  return hex.toString();
    }
    
    public static String convertHexToString(String hex){

  	  StringBuilder sb = new StringBuilder();
  	  StringBuilder temp = new StringBuilder();

  	  //49204c6f7665204a617661 split into two characters 49, 20, 4c...
  	  for( int i=0; i<hex.length()-1; i+=2 ){

  	      //grab the hex in pairs
  	      String output = hex.substring(i, (i + 2));
  	      //convert hex to decimal
  	      int decimal = Integer.parseInt(output, 16);
  	      //convert the decimal to character
  	      sb.append((char)decimal);

  	      temp.append(decimal);
  	  }
  	 

  	  return sb.toString();
    }
    
    public static double distance(double lat1, double lon1, double lat2, double lon2, String sr) {

		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (sr.equals("K")) {
			dist = dist * 1.609344;
		} else if (sr.equals("N")) {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	public static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	public static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}
}
