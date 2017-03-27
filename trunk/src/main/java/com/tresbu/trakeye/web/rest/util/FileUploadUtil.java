package com.tresbu.trakeye.web.rest.util;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUploadUtil {

	 private static final Logger log = LoggerFactory.getLogger(FileUploadUtil.class);
	 
	public static String getFileName(String directory,String login,String pAgent){
		File dir=new File(System.getProperty("catalina.base")+"/webapps/custom/"+login+"/"+pAgent+"/"+directory);
  	  
  	  File[] files = dir.listFiles();
  	
  	  if (files == null || files.length == 0) {
  		log.info("APK is not available.");
  		 // return new ResponseEntity<>("password set successfully",HttpStatus.OK);
  		  return null;
	    }
  	  
  	  File lastModifiedFile = files[0];

  	  for (int i = 1; i < files.length; i++) {
	       if (lastModifiedFile.lastModified() < files[i].lastModified()) {
	           lastModifiedFile = files[i];
	       }
	    }
	return lastModifiedFile.getName();
	}
}
