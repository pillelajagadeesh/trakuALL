package com.tresbu.trakeye.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Properties specific to JHipster.
 *
 * <p>
 * Properties are configured in the application.yml file.
 * </p>
 */
@ConfigurationProperties(prefix = "trakeye", ignoreUnknownFields = false)
public class TrakEyeProperties {

	private final Async async = new Async();

	private final Http http = new Http();

	private final Cache cache = new Cache();

	private final Mail mail = new Mail();

	private final Sms sms = new Sms();

	private final Fcm fcm = new Fcm();
	
	private final Collab collab = new Collab();

	private final Security security = new Security();

	private final Swagger swagger = new Swagger();

	private final Metrics metrics = new Metrics();

	private final CorsConfiguration cors = new CorsConfiguration();

	private final Ribbon ribbon = new Ribbon();

	private final CustomerAdmin customerAdmin = new CustomerAdmin();
	
	private final SuperAdmin superAdmin = new SuperAdmin();

	private final GpsTracker gpsTracker = new GpsTracker();
	
	public Googleapi getGoogleapi() {
		return googleapi;
	}

	private final Googleapi googleapi = new Googleapi();

	public Async getAsync() {
		return async;
	}

	public Http getHttp() {
		return http;
	}

	public Cache getCache() {
		return cache;
	}

	public Mail getMail() {
		return mail;
	}

	public Sms getSms() {
		return sms;
	}

	public Fcm getFcm() {
		return fcm;
	}
	
	public Collab getCollab() {
		return collab;
	}

	public Security getSecurity() {
		return security;
	}

	public Swagger getSwagger() {
		return swagger;
	}

	public Metrics getMetrics() {
		return metrics;
	}

	public CorsConfiguration getCors() {
		return cors;
	}

	public Ribbon getRibbon() {
		return ribbon;
	}

	public CustomerAdmin getCustomerAdmin() {
		return customerAdmin;
	}
	
	public SuperAdmin getSuperAdmin() {
		return superAdmin;
	}

	
	public GpsTracker getGpsTracker() {
		return gpsTracker;
	}

	public static class Async {

		private int corePoolSize = 2;

		private int maxPoolSize = 50;

		private int queueCapacity = 10000;

		public int getCorePoolSize() {
			return corePoolSize;
		}

		public void setCorePoolSize(int corePoolSize) {
			this.corePoolSize = corePoolSize;
		}

		public int getMaxPoolSize() {
			return maxPoolSize;
		}

		public void setMaxPoolSize(int maxPoolSize) {
			this.maxPoolSize = maxPoolSize;
		}

		public int getQueueCapacity() {
			return queueCapacity;
		}

		public void setQueueCapacity(int queueCapacity) {
			this.queueCapacity = queueCapacity;
		}
	}

	public static class Http {

		private final Cache cache = new Cache();

		public Cache getCache() {
			return cache;
		}

		public static class Cache {

			private int timeToLiveInDays = 1461;

			public int getTimeToLiveInDays() {
				return timeToLiveInDays;
			}

			public void setTimeToLiveInDays(int timeToLiveInDays) {
				this.timeToLiveInDays = timeToLiveInDays;
			}
		}
	}

	public static class Cache {

		private int timeToLiveSeconds = 3600;

		public int getTimeToLiveSeconds() {
			return timeToLiveSeconds;
		}

		public void setTimeToLiveSeconds(int timeToLiveSeconds) {
			this.timeToLiveSeconds = timeToLiveSeconds;
		}
	}

	public static class Mail {

		private String from = "trakeye@localhost";
		
		private String baseUrl="";

		public String getBaseUrl() {
			return baseUrl;
		}

		public void setBaseUrl(String baseUrl) {
			this.baseUrl = baseUrl;
		}

		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from;
		}
	}

	public static class Sms {

		private boolean sendSms;

		private String twilioSid;

		private String twilioAuthToken;

		private String twilioSenderNo;

		public String getTwilioSid() {
			return twilioSid;
		}

		public void setTwilioSid(String twilioSid) {
			this.twilioSid = twilioSid;
		}

		public String getTwilioAuthToken() {
			return twilioAuthToken;
		}

		public void setTwilioAuthToken(String twilioAuthToken) {
			this.twilioAuthToken = twilioAuthToken;
		}

		public String getTwilioSenderNo() {
			return twilioSenderNo;
		}

		public void setTwilioSenderNo(String twilioSenderNo) {
			this.twilioSenderNo = twilioSenderNo;
		}

		public boolean isSendSms() {
			return sendSms;
		}

		public void setSendSms(boolean sendSms) {
			this.sendSms = sendSms;
		}

	}

	public static class Fcm {

		private boolean sendFcm;

		private String fcmKey;

		private String fcmUrl;

		public boolean isSendFcm() {
			return sendFcm;
		}

		public void setSendFcm(boolean sendFcm) {
			this.sendFcm = sendFcm;
		}

		public String getFcmKey() {
			return fcmKey;
		}

		public void setFcmKey(String fcmKey) {
			this.fcmKey = fcmKey;
		}

		public String getFcmUrl() {
			return fcmUrl;
		}

		public void setFcmUrl(String fcmUrl) {
			this.fcmUrl = fcmUrl;
		}

	}
	
	public static class Collab {

		private String collabBaseUrl;

		private String collabSuperAdminLogin;

		private String collabSuperAdminPassword;
		
		private boolean collabEnabled;
		
		

		public String getCollabBaseUrl() {
			return collabBaseUrl;
		}

		public void setCollabBaseUrl(String collabBaseUrl) {
			this.collabBaseUrl = collabBaseUrl;
		}

		public String getCollabSuperAdminLogin() {
			return collabSuperAdminLogin;
		}

		public void setCollabSuperAdminLogin(String collabSuperAdminLogin) {
			this.collabSuperAdminLogin = collabSuperAdminLogin;
		}

		public String getCollabSuperAdminPassword() {
			return collabSuperAdminPassword;
		}

		public void setCollabSuperAdminPassword(String collabSuperAdminPassword) {
			this.collabSuperAdminPassword = collabSuperAdminPassword;
		}

		public boolean isCollabEnabled() {
			return collabEnabled;
		}

		public void setCollabEnabled(boolean collabEnabled) {
			this.collabEnabled = collabEnabled;
		}

		
		

		
	}

	public static class Security {

		private final Authentication authentication = new Authentication();

		public Authentication getAuthentication() {
			return authentication;
		}

		public static class Authentication {

			private final Jwt jwt = new Jwt();

			public Jwt getJwt() {
				return jwt;
			}

			public static class Jwt {

				private String secret;

				private long tokenValidityInSeconds = 1800;
				private long tokenValidityInSecondsForRememberMe = 2592000;

				public String getSecret() {
					return secret;
				}

				public void setSecret(String secret) {
					this.secret = secret;
				}

				public long getTokenValidityInSeconds() {
					return tokenValidityInSeconds;
				}

				public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
					this.tokenValidityInSeconds = tokenValidityInSeconds;
				}

				public long getTokenValidityInSecondsForRememberMe() {
					return tokenValidityInSecondsForRememberMe;
				}

				public void setTokenValidityInSecondsForRememberMe(long tokenValidityInSecondsForRememberMe) {
					this.tokenValidityInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
				}
			}
		}
	}

	public static class Swagger {

		private String title = "trakeye API";

		private String description = "trakeye API documentation";

		private String version = "0.0.1";

		private String termsOfServiceUrl;

		private String contactName;

		private String contactUrl;

		private String contactEmail;

		private String license;

		private String licenseUrl;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getTermsOfServiceUrl() {
			return termsOfServiceUrl;
		}

		public void setTermsOfServiceUrl(String termsOfServiceUrl) {
			this.termsOfServiceUrl = termsOfServiceUrl;
		}

		public String getContactName() {
			return contactName;
		}

		public void setContactName(String contactName) {
			this.contactName = contactName;
		}

		public String getContactUrl() {
			return contactUrl;
		}

		public void setContactUrl(String contactUrl) {
			this.contactUrl = contactUrl;
		}

		public String getContactEmail() {
			return contactEmail;
		}

		public void setContactEmail(String contactEmail) {
			this.contactEmail = contactEmail;
		}

		public String getLicense() {
			return license;
		}

		public void setLicense(String license) {
			this.license = license;
		}

		public String getLicenseUrl() {
			return licenseUrl;
		}

		public void setLicenseUrl(String licenseUrl) {
			this.licenseUrl = licenseUrl;
		}
	}

	public static class Metrics {

		private final Jmx jmx = new Jmx();

		private final Spark spark = new Spark();

		private final Graphite graphite = new Graphite();

		private final Logs logs = new Logs();

		public Jmx getJmx() {
			return jmx;
		}

		public Spark getSpark() {
			return spark;
		}

		public Graphite getGraphite() {
			return graphite;
		}

		public Logs getLogs() {
			return logs;
		}

		public static class Jmx {

			private boolean enabled = true;

			public boolean isEnabled() {
				return enabled;
			}

			public void setEnabled(boolean enabled) {
				this.enabled = enabled;
			}
		}

		public static class Spark {

			private boolean enabled = false;

			private String host = "localhost";

			private int port = 9999;

			public boolean isEnabled() {
				return enabled;
			}

			public void setEnabled(boolean enabled) {
				this.enabled = enabled;
			}

			public String getHost() {
				return host;
			}

			public void setHost(String host) {
				this.host = host;
			}

			public int getPort() {
				return port;
			}

			public void setPort(int port) {
				this.port = port;
			}
		}

		public static class Graphite {

			private boolean enabled = false;

			private String host = "localhost";

			private int port = 2003;

			private String prefix = "trakeye";

			public boolean isEnabled() {
				return enabled;
			}

			public void setEnabled(boolean enabled) {
				this.enabled = enabled;
			}

			public String getHost() {
				return host;
			}

			public void setHost(String host) {
				this.host = host;
			}

			public int getPort() {
				return port;
			}

			public void setPort(int port) {
				this.port = port;
			}

			public String getPrefix() {
				return prefix;
			}

			public void setPrefix(String prefix) {
				this.prefix = prefix;
			}
		}

		public static class Logs {

			private boolean enabled = false;

			private long reportFrequency = 60;

			public long getReportFrequency() {
				return reportFrequency;
			}

			public void setReportFrequency(int reportFrequency) {
				this.reportFrequency = reportFrequency;
			}

			public boolean isEnabled() {
				return enabled;
			}

			public void setEnabled(boolean enabled) {
				this.enabled = enabled;
			}
		}
	}

	private final Logging logging = new Logging();

	public Logging getLogging() {
		return logging;
	}

	public static class Logging {

		private final Logstash logstash = new Logstash();

		public Logstash getLogstash() {
			return logstash;
		}

		public static class Logstash {

			private boolean enabled = false;

			private String host = "localhost";

			private int port = 5000;

			private int queueSize = 512;

			public boolean isEnabled() {
				return enabled;
			}

			public void setEnabled(boolean enabled) {
				this.enabled = enabled;
			}

			public String getHost() {
				return host;
			}

			public void setHost(String host) {
				this.host = host;
			}

			public int getPort() {
				return port;
			}

			public void setPort(int port) {
				this.port = port;
			}

			public int getQueueSize() {
				return queueSize;
			}

			public void setQueueSize(int queueSize) {
				this.queueSize = queueSize;
			}
		}

	}

	public static class Ribbon {

		private String[] displayOnActiveProfiles;

		public String[] getDisplayOnActiveProfiles() {
			return displayOnActiveProfiles;
		}

		public void setDisplayOnActiveProfiles(String[] displayOnActiveProfiles) {
			this.displayOnActiveProfiles = displayOnActiveProfiles;
		}
	}

	public static class Googleapi {
		private String key = "AIzaSyDLVWFeNy9E2WZLGCHZHvFyvl1ZtRUble0";

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
	}

	public static class CustomerAdmin {

		private String login = "trailadmin";
		private String firstName = "admin";
		private String lastName = "trail";
		private String password = "admin";
		private String email = "mohan@tresbu.com";

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

	}

	public static class GpsTracker {
		private String gpsServerUrl = "http://gpstracker.net.in/gs/api/api.php?api=user&ver=1.0&key=";
		
		private String trakEyeType="";
		private String param1 = "cmd";
		private long delayTime;
		
		public String getParam1() {
			return param1;
		}
		public void setParam1(String param1) {
			this.param1 = param1;
		}
		public String getParam2() {
			return param2;
		}
		public void setParam2(String param2) {
			this.param2 = param2;
		}
		private String param2="key";
        private boolean isEnabled = false;
        
		public String getTrakEyeType() {
			return trakEyeType;
		}
		public void setTrakEyeType(String trakEyeType) {
			this.trakEyeType = trakEyeType;
		}
		                
		public String getGpsServerUrl() {
			return gpsServerUrl;
		}
		public void setGpsServerUrl(String gpsServerUrl) {
			this.gpsServerUrl = gpsServerUrl;
		}
		
		public boolean isEnabled() {
			return isEnabled;
		}
		public void setEnabled(boolean isEnabled) {
			this.isEnabled = isEnabled;
		}
		public long getDelayTime() {
			return delayTime;
		}
		public void setDelayTime(long delayTime) {
			this.delayTime = delayTime;
		}
		
		
	}
	
	public static class SuperAdmin {

		private String superAdminLogin;
		private String superAdminPassword;
		public String getSuperAdminLogin() {
			return superAdminLogin;
		}
		public void setSuperAdminLogin(String superAdminLogin) {
			this.superAdminLogin = superAdminLogin;
		}
		public String getSuperAdminPassword() {
			return superAdminPassword;
		}
		public void setSuperAdminPassword(String superAdminPassword) {
			this.superAdminPassword = superAdminPassword;
		}
		
		
		
	}		
	
}
