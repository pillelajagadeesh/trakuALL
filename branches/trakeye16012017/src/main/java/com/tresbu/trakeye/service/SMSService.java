package com.tresbu.trakeye.service;

import com.tresbu.trakeye.config.TrakEyeProperties;
import com.tresbu.trakeye.domain.TrNotification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * import com.twilio.sdk.Twilio;
import com.twilio.sdk.client.TwilioRestClient;
import com.twilio.sdk.creator.api.v2010.account.MessageCreator;
import com.twilio.sdk.resource.api.v2010.account.Message;
import com.twilio.sdk.type.PhoneNumber;
 */

/**
 * Service for sending SMS notification.
 * <p>
 * We use the @Async annotation to send SMS's asynchronously.
 * </p>
 */
@Service
public class SMSService {

    private final Logger log = LoggerFactory.getLogger(SMSService.class);
    
    @Inject
    private TrakEyeProperties jHipsterProperties;


    @Async
    public void sendSMS(String contactNumber, String message) {
    	// log.debug("Sending SMS to {}", contactNumber);
     	
     	if(jHipsterProperties.getSms().isSendSms()){
     	/*
     	 // sending sms with twilio service provider
     	   try{
     	  
   	  Twilio.init(jHipsterProperties.getSms().getTwilioSid(), jHipsterProperties.getSms().getTwilioAuthToken());
   	  MessageCreator message= Message.create(jHipsterProperties.getSms().getTwilioSid(), new PhoneNumber("+91"+contactNumber),
   	  new PhoneNumber(jHipsterProperties.getSms().getTwilioSenderNo()), message);
   	  log.debug("SMS sent sucessfully);
   	  }catch(Exception e){
   	     log.debug("Error in sending SMS",e.getMessage());
      }*/
    
     }
    }

    @Async
    public void sendNotificationSMS(TrNotification notification) { 	
        log.debug("Sending notification SMS to '{}' from '{}'", notification.getToUser().getEmail(),notification.getToUser().getEmail());
       // sendSMS(notification.getFromUser().getPhoneNumber(), notification.getToUser().getEmailPhoneNumber(), notification.getSubject(), notification.getDescription());
        //sendSMS(notification.getFromUser().getEmail(), notification.getToUser().getEmail(), notification.getSubject(), notification.getDescription());
    }
    
    
    public void registerSMSGateway(){
    	
    }
}
