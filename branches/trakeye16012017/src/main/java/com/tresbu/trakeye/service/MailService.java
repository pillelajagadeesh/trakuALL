package com.tresbu.trakeye.service;

import com.tresbu.trakeye.config.TrakEyeProperties;
import com.tresbu.trakeye.domain.TrNotification;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.service.dto.CustomerDTO;
import com.tresbu.trakeye.service.dto.TrNotificationDTO;
import com.tresbu.trakeye.service.dto.UserUIDTO;

import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;


import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Optional;

/**
 * Service for sending e-mails.
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";
    private static final String BASE_URL = "baseUrl";
    private static final String BASE_URL_IOS="baseUrlIos";
    private static final String FROMUSERNAME = "fromusername";
    private static final String TOUSERNAME = "tousername";
    private static final String DESCRIPTION = "description";
    private static final String CASEIDLINK = "caseidlink";
    private static final String CUSTOMER = "customer";

    @Inject
    private TrakEyeProperties jHipsterProperties;

    @Inject
    private JavaMailSenderImpl javaMailSender;

    @Inject
    private MessageSource messageSource;

    @Inject
    private SpringTemplateEngine templateEngine;

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendActivationEmail(UserUIDTO user, String baseUrl) {
        log.debug("Sending activation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process("activationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

  /*  @Async
    public void sendCreationEmail(UserUIDTO user, String baseUrl) {
        log.debug("Sending creation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process("creationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }*/
    
    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process("creationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendPasswordResetMail(UserUIDTO user, String baseUrl) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process("passwordResetEmail", context);
        String subject = messageSource.getMessage("email.reset.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }
    
    @Async
    public void sendDownloadLinkMail(UserUIDTO user, String baseUrl,String baseIosUrl) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        context.setVariable(BASE_URL_IOS, baseIosUrl);
        String content = templateEngine.process("downloadLinkEmail", context);
        String subject = messageSource.getMessage("email.apk.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }
    
    
    @Async
    public void sendNotificationMail(TrNotificationDTO trNotificationDTO) {
    	
        log.debug("Sending notification e-mail to '{}' from '{}'", trNotificationDTO.getToUser().getEmail(),trNotificationDTO.getFromUser().getEmail());
        Locale locale = Locale.forLanguageTag(trNotificationDTO.getToUser().getLangKey());
        Context context = new Context(locale);
        context.setVariable(FROMUSERNAME, trNotificationDTO.getFromUser().getLogin());
        context.setVariable(TOUSERNAME, trNotificationDTO.getToUser().getLogin());
        context.setVariable(DESCRIPTION, trNotificationDTO.getDescription());
        String content = null;
        if( trNotificationDTO.getCaseIdLink() != null){
        	context.setVariable(CASEIDLINK, trNotificationDTO.getCaseIdLink());
            context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
             content = templateEngine.process("caseNotificationEmail", context);
        }else{
        	  content = templateEngine.process("notificationEmail", context);
        }
        
       
        String subject = trNotificationDTO.getSubject();
        sendEmail2(trNotificationDTO.getFromUser().getEmail(), trNotificationDTO.getToUser().getEmail(), subject, content, false, true);
    }
    
    @Async
    public void sendAdminNotificationMail(TrNotificationDTO trNotificationDTO) {
    	
        log.debug("Sending notification e-mail to '{}' from '{}'", trNotificationDTO.getToUser().getEmail(),trNotificationDTO.getFromUser().getEmail());
        Locale locale = Locale.forLanguageTag(trNotificationDTO.getToUser().getLangKey());
        Context context = new Context(locale);
        context.setVariable(FROMUSERNAME, trNotificationDTO.getFromUser().getLogin());
        context.setVariable(TOUSERNAME, trNotificationDTO.getToUser().getLogin());
        context.setVariable(DESCRIPTION, trNotificationDTO.getDescription());
        String content = templateEngine.process("notificationAdminEmail", context);
        String subject = trNotificationDTO.getSubject();
        sendEmail2(trNotificationDTO.getFromUser().getEmail(), trNotificationDTO.getToUser().getEmail(), subject, content, false, true);
    }
    
    @Async
    public void sendEmail2(String from, String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }
    
    @Async
    public void sendCustomerCreationEmail(CustomerDTO customerDTO, UserUIDTO adminUser) {
        log.debug("Sending creation e-mail to '{}'", customerDTO.getEmail());
        Locale locale = Locale.forLanguageTag(customerDTO.getLangKey());
        Context context = new Context(locale);
        context.setVariable(CUSTOMER, customerDTO);
        String content = templateEngine.process("customerCreationEmail", context);
        String subject = messageSource.getMessage("email.customer.creation.title", null, locale);
        sendEmail(customerDTO.getEmail(), subject, content, false, true);
        
        //Sending an confirmation email to administrator
        sendCustomerCreationEmailToAdministrator(customerDTO, adminUser);
    }

    @Async
    public void sendCustomerCreationEmailToAdministrator(CustomerDTO customerDTO, UserUIDTO adminUser) {
        log.debug("Sending creation e-mail from '{}'", jHipsterProperties.getMail().getFrom(), "to {} ", adminUser.getEmail());
        Locale locale = Locale.forLanguageTag(customerDTO.getLangKey());
        Context context = new Context(locale);
        context.setVariable(CUSTOMER, customerDTO);
        context.setVariable(USER, adminUser);
        String content = templateEngine.process("customerConfirmationEmailToAdmin", context);
        String subject = messageSource.getMessage("email.customer.admin.title", null, locale);
        
        sendEmail(adminUser.getEmail(), subject, content, false, true);
    }
}
