package com.tresbu.trakeye.web.rest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.service.TrNotificationService;
import com.tresbu.trakeye.service.dto.ApkDetailsDTO;
import com.tresbu.trakeye.web.rest.util.FileUploadUtil;
import com.tresbu.trakeye.web.rest.util.HeaderUtil;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
public class UploadResource extends BaseResource{
	
	private static final String UNDERSCORE = "_";

	private static final String CATALINA_BASE = "catalina.base";

	private static final String ADMIN = "/admin/";

	private static final String AGENTS = "/agents/";

	private static final String WEBAPPS_CUSTOM = "/webapps/custom/";

	private static final String TRAKEYE_SUFFIX = ".apk";
	
	private static final String ANDROID="android";
	
	private static final String IOS="ios";
	
	private static final String TRAKEYE_IOS_SUFFIX = ".ipa";

	private static final String TRAKEYE_PREFIX = "trakeye-agent_";
	
	private static final String TRAKEYE_IOS_PREFIX = "trakeye-ios-agent_";
	
	private static final String TRAKEYE_ADMIN_PREFIX = "trakeye-admin_";
	
	private static final String TRAKEYE_IOS_ADMIN_PREFIX = "trakeye-ios-admin_";

	private final Logger log = LoggerFactory.getLogger(UploadResource.class);
	
	@Inject
	private TrNotificationService trNotificationService;
	
	 
	@RequestMapping(value = "/upload", method = RequestMethod.POST,consumes="multipart/form-data",
			   produces = {"application/json","application/xml"})
	@Timed
	@Secured({AuthoritiesConstants.USER_ADMIN,AuthoritiesConstants.SUPER_ADMIN})
	@ResponseBody
	@ApiOperation(value = "Upload agent application ", notes = "User can upload agent or admin application. User with role SuperAdmin, User Admin is allowed. "
			+ "User with role Super Admin will upload admin application and user with role User Admin will upload agent application.", response = Void.class)
	public ResponseEntity<?> uploadFileHandler(@RequestParam(value="file", required=true) MultipartFile file, 
			@RequestParam(value="version", required=true) String version, HttpServletRequest request) throws IOException {
		
		log.debug("rest api to upload file");
		
		if (!file.isEmpty()) {
			if(!(getFileExtension(file.getOriginalFilename()).equals("apk")||getFileExtension(file.getOriginalFilename()).equals("ipa"))){
				return ResponseEntity.badRequest()
		                .headers(HeaderUtil.createFailureAlert("upload", "fileisnotapk", "only apk and ipa file allowed"))
		                .body(null);
			}
			try {
				User user=getCurrentUser();
				byte[] bytes = file.getBytes();

				log.debug(System.getProperty(CATALINA_BASE));
				
				File serverDir=null;
				String fileName=null;
				String directory=null;
				for(String authority:getCurrentUserAuthorities()){
					
					if(authority!=null && authority.equals(AuthoritiesConstants.USER_ADMIN)){
					  directory=AGENTS;
					if(getFileExtension(file.getOriginalFilename()).equals("apk")){
					 serverDir=new File(System.getProperty(CATALINA_BASE)+WEBAPPS_CUSTOM+user.getLogin()+directory+ANDROID);
				     fileName= TRAKEYE_PREFIX+version+TRAKEYE_SUFFIX;
					}else{
						serverDir=new File(System.getProperty(CATALINA_BASE)+WEBAPPS_CUSTOM+user.getLogin()+directory+IOS);
						fileName= TRAKEYE_IOS_PREFIX+version+TRAKEYE_IOS_SUFFIX;
					}
					}else if(authority!=null && authority.equals(AuthoritiesConstants.SUPER_ADMIN)){
						directory=ADMIN;
					 if(getFileExtension(file.getOriginalFilename()).equals("apk")){
						 serverDir=new File(System.getProperty(CATALINA_BASE)+WEBAPPS_CUSTOM+user.getLogin()+directory+ANDROID);
					     fileName= TRAKEYE_ADMIN_PREFIX+version+TRAKEYE_SUFFIX;
					 }else{
						 serverDir=new File(System.getProperty(CATALINA_BASE)+WEBAPPS_CUSTOM+user.getLogin()+directory+IOS);
						 fileName= TRAKEYE_IOS_ADMIN_PREFIX+version+TRAKEYE_IOS_SUFFIX;
					 }
					}
				}
				
				
				if(serverDir!=null && !serverDir.exists()){
					serverDir.mkdirs();
				}
				File serverFile=new File(serverDir+File.separator+fileName);
			
				if(serverDir!=null &&!serverFile.exists()){
					serverFile.createNewFile();
				}
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

					String iosFileName=FileUploadUtil.getFileName("ios", user.getLogin(),directory);
					String androidFileName=FileUploadUtil.getFileName("android", user.getLogin(),directory);
				
				log.debug("Server File Location="+ serverFile.getAbsolutePath());
				
				trNotificationService.sendNotification(user, androidFileName,iosFileName, request);
				
				return ResponseEntity.ok()
			            .headers(HeaderUtil.createAlert("uploadsuccess", "File Uploaded successfully"))
			            .body(null);
			} catch (Exception e) {
				return ResponseEntity.badRequest()
		                .headers(HeaderUtil.createFailureAlert("upload", "uploadfailure", "Error while uploading the file"))
		                .body(null);
			}
		} else {
			return ResponseEntity.badRequest()
	                .headers(HeaderUtil.createFailureAlert("upload", "uploadfileempty", "Uploading file is empty"))
	                .body(null);
		}
	
	}
	
	@RequestMapping(value = "/listfiles", method = RequestMethod.GET)
	@Timed
	@Secured({AuthoritiesConstants.USER_ADMIN,AuthoritiesConstants.SUPER_ADMIN})
	@ResponseBody
	@ApiOperation(value = "EGt list of uploaded applications ", notes = "User can get list of agent applications or admin applications. User with role SuperAdmin, User Admin is allowed. "
			+ "User with role Super Admin will get list of admin applications and user with role User Admin will get list of agent applications.", response = Void.class)
	public ResponseEntity<List<ApkDetailsDTO>> findAllFiles(HttpServletRequest request) throws IOException {
	  User user=getCurrentUser();
	  File dir=null;
	  for(String authority:getCurrentUserAuthorities()){
			if(authority!=null && authority.equals(AuthoritiesConstants.USER_ADMIN)){
		     dir=new File(System.getProperty(CATALINA_BASE)+WEBAPPS_CUSTOM+user.getLogin()+AGENTS+ANDROID);
			}else if(authority!=null && authority.equals(AuthoritiesConstants.SUPER_ADMIN)){
			 dir=new File(System.getProperty(CATALINA_BASE)+WEBAPPS_CUSTOM+user.getLogin()+ADMIN+ANDROID);
			}
		}
	 
   	  List<ApkDetailsDTO> apkDetailsDTOs=new ArrayList<>();
   	  File[] files = dir.listFiles();
   	  
   	  if(files!=null && files.length!=0){
   	
   		Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
   	
   	   for (int i = 0; i < files.length; i++) {
   		   File lastModifiedFile = files[i];
 	      ApkDetailsDTO apkDetailsDTO=new ApkDetailsDTO();
 	      String file=lastModifiedFile.getName();
 	   		apkDetailsDTO.setFileName(file);
 	   		if(file!=null){
 	   		   String [] fileArray=file.split(UNDERSCORE);
 	   		   String[]array2=fileArray[1].split(TRAKEYE_SUFFIX);
 	   		   apkDetailsDTO.setVersion(array2[0]);
 	   		}
 	     	apkDetailsDTOs.add(apkDetailsDTO);
 	       
 	    }
   	   
   	   
//     	String baseUrl = request.getScheme() + // "http"
//               "://" +                                // "://"
//               request.getServerName() +              // "myhost"
//               "/custom/"+user.getLogin()+"/agents/"+lastModifiedFile.getName();
   	  }
		return new ResponseEntity<>(apkDetailsDTOs, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/listfiles-ios", method = RequestMethod.GET)
	@Timed
	@Secured({AuthoritiesConstants.USER_ADMIN,AuthoritiesConstants.SUPER_ADMIN})
	@ResponseBody
	public ResponseEntity<List<ApkDetailsDTO>> findAllIosFiles(HttpServletRequest request) throws IOException {
	  User user=getCurrentUser();
	  File dir=null;
	  for(String authority:getCurrentUserAuthorities()){
			if(authority!=null && authority.equals(AuthoritiesConstants.USER_ADMIN)){
		     dir=new File(System.getProperty(CATALINA_BASE)+WEBAPPS_CUSTOM+user.getLogin()+AGENTS+IOS);
			}else if(authority!=null && authority.equals(AuthoritiesConstants.SUPER_ADMIN)){
			 dir=new File(System.getProperty(CATALINA_BASE)+WEBAPPS_CUSTOM+user.getLogin()+ADMIN+IOS);
			}
		}
	 
   	  List<ApkDetailsDTO> apkDetailsDTOs=new ArrayList<>();
   	  File[] files = dir.listFiles();
   	  
   	  if(files!=null && files.length!=0){
   	
   		Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
   	
   	   for (int i = 0; i < files.length; i++) {
   		   File lastModifiedFile = files[i];
 	      ApkDetailsDTO apkDetailsDTO=new ApkDetailsDTO();
 	      String file=lastModifiedFile.getName();
 	   		apkDetailsDTO.setFileName(file);
 	   		if(file!=null){
 	   		   String [] fileArray=file.split(UNDERSCORE);
 	   		   String[]array2=fileArray[1].split(TRAKEYE_IOS_SUFFIX);
 	   		   apkDetailsDTO.setVersion(array2[0]);
 	   		}
 	     	apkDetailsDTOs.add(apkDetailsDTO);
 	       
 	    }
   	   
   	   
//     	String baseUrl = request.getScheme() + // "http"
//               "://" +                                // "://"
//               request.getServerName() +              // "myhost"
//               "/custom/"+user.getLogin()+"/agents/"+lastModifiedFile.getName();
   	  }
		return new ResponseEntity<>(apkDetailsDTOs, HttpStatus.OK);
	}
	private static String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
	
//	@RequestMapping(value = "/listfiles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	@Timed
//	@Secured({  AuthoritiesConstants.USER_ADMIN })
//	public ResponseEntity<List<ApkDetailsDTO>> geofences(@PageableDefault(page = 0, size = 20)Pageable pageable) throws URISyntaxException {
//    	 Page<ApkDetailsDTO> page = apkDetailService.findAll(pageable);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/listfiles");
//        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//	}
}
