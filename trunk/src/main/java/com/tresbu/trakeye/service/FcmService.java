package com.tresbu.trakeye.service;

import javax.inject.Inject;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tresbu.trakeye.config.TrakEyeProperties;

@Service
public class FcmService {

	private static final String AUTHORIZATION = "authorization";

	private static final String NOTIFICATION = "notification";

	private static final String TO = "to";

	private static final String TITLE2 = "title";

	private static final String BODY = "body";

	private final Logger log = LoggerFactory.getLogger(FcmService.class);
	
	@Inject
    private TrakEyeProperties jHipsterProperties;
	
    @Async
	public void sendAndroidNotification(String deviceToken,String message,String title) {
		try {
			log.debug("Sending notification FCM to '{}' from '{}'", deviceToken);
			if(deviceToken==null || deviceToken.isEmpty()){
				log.debug("device Token is empty");
				return;
			}
			
		    JSONObject obj = new JSONObject();
		    JSONObject msgObject = new JSONObject();
		    msgObject.put(BODY, message);
		    msgObject.put(TITLE2, title);
		 // msgObject.put("icon", ANDROID_NOTIFICATION_ICON);
		 // msgObject.put("color", ANDROID_NOTIFICATION_COLOR);
		    
		    obj.put(TO, deviceToken);
		    obj.put(NOTIFICATION,msgObject);
			
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.add(AUTHORIZATION, "key="+jHipsterProperties.getFcm().getFcmKey());
		    HttpEntity<String> request = new HttpEntity<String>(obj.toString(),headers);
		    
			RestTemplate rt = new RestTemplate();
	        rt.getMessageConverters().add(new StringHttpMessageConverter());
	        rt.postForObject(jHipsterProperties.getFcm().getFcmUrl(),request,String.class);
		    
		}catch (Exception ex) {
         log.error(ex.getMessage());
		} finally {
			
		}
    }
	
}
