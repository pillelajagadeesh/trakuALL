package com.tresbu.trakeye.web.rest;

import static com.jayway.restassured.RestAssured.given;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.web.rest.vm.LoginVM;


/*@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrakeyeApp.class)*/
public class LocationLogIntTest {
	 private static Logger log = LoggerFactory.getLogger(LocationLogIntTest.class);
	
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final boolean ADMIN_REMEMBERME = true;
    
	
	
   
   // private static final String BASE_URI = "http://localhost:8080";
    private static RequestSpecification spec= new RequestSpecBuilder()
    .setContentType(ContentType.JSON)
    .setBaseUri("http://localhost:8080")
    .build();
    public static Response response;
    public static String jsonAsString;
    
    
	public static void main(String[] args) throws JSONException, SQLException {
		createLogs();
		//deleteLogs();
	}
	
	
	private static void createLogs() throws JSONException{
		// super admin login
				
				
				LoginVM loginVM = new LoginVM();
		    	loginVM.setUsername(ADMIN_USERNAME);
		    	loginVM.setPassword(ADMIN_PASSWORD);
				loginVM.setRememberMe(ADMIN_REMEMBERME);
		    	
		    	response =   (Response) given().spec(spec)
				.body(loginVM)
			    .and().header("Content-Type", "application/json")
				.when()
		    .post("/api/authenticate")
		    .then().extract();
			
		    	String superAdminauthtoken = response.getHeader("Authorization");
		    	
		    	log.info("login response {}",superAdminauthtoken);
		    	  	
		    	
		    	
		    	// create user with role admin and user
		    	Set<String> admin_role = new HashSet<String>();
		    	admin_role.add("ROLE_USER");
		    	admin_role.add("ROLE_ADMIN");
		    	JSONObject jo = new JSONObject();
		    	jo.put("id",null);
		    	jo.put("login","logadmin");
		    	jo.put("firstName","logadmin");
		    	jo.put("lastName","logadmin");
		    	jo.put("email","logadmin@yopmail.com");
		    	jo.put("phone","8888888888");
		    	jo.put("activated",true);
		    	jo.put("langKey",null);
		    	jo.put("createdBy",null);
		    	jo.put("createdDate",null);
		    	jo.put("lastModifiedBy",null);
		    	jo.put("lastModifiedDate",null);
		    	jo.put("resetDate",null);
		    	jo.put("resetKey",null);
		    	jo.put("authorities",admin_role);
		    	jo.put("geofences",null);
		    	
		    	response =   (Response) given().spec(spec)
		    			.body(jo)
		    			.header("Authorization", superAdminauthtoken)
		    		    .and().header("Content-Type", "application/json")
		    			.when()
		    	    .post("/api/users")
		    	    .then().extract();
		    	String body = response.getBody().asString();
		    	org.json.JSONObject jsonObject = new org.json.JSONObject(body);
		    	String stringKey = jsonObject.getString("resetKey");
		    	
		    	log.info("response {}",response);
		    	
		    	//set password
		    	
		    	JSONObject setPwd = new JSONObject();
		    	setPwd.put("key", stringKey);
		    	setPwd.put("newPassword", "admin");
		    	
		    	response =   (Response) given().spec(spec)
		    			.body(setPwd)
		    			.and().header("Content-Type", "application/json")
		    			.when()
		    	    .post("/api/account/reset_password/finish")
		    	    .then().extract();
		    	
		    	// login useradmin
		    	
				
				 loginVM = new LoginVM();
		  	loginVM.setUsername("logadmin");
		  	loginVM.setPassword("admin");
				loginVM.setRememberMe(ADMIN_REMEMBERME);
		  	
		  	response =   (Response) given().spec(spec)
				.body(loginVM)
			    .and().header("Content-Type", "application/json")
				.when()
		  .post("/api/authenticate")
		  .then().extract();
			
		  	String logAdminAuthKey = response.getHeader("Authorization");
		    	
		    	// create users with role role_user
		  	Set<String> user_role = new HashSet<String>();
			admin_role.add("ROLE_USER");
			
			
		    BufferedReader br = null;
		    String line = "";
		    String cvsSplitBy = ",";

		    try {

		      
		       br=  new BufferedReader(new FileReader(System.getProperty("user.dir")+"/src/test/resources/customers_table.csv"));
		        while ((line = br.readLine()) != null) {
		           String[] users = line.split(cvsSplitBy);
		           
		           jo = new JSONObject();
		       	jo.put("id",null);
		       	jo.put("login",users[0]);
		       	jo.put("firstName",users[1]);
		       	jo.put("lastName",users[2]);
		       	jo.put("email",users[3]);
		       	jo.put("phone",users[4]);
		       	jo.put("activated",true);
		       	jo.put("langKey",null);
		       	jo.put("createdBy",null);
		       	jo.put("createdDate",null);
		       	jo.put("lastModifiedBy",null);
		       	jo.put("lastModifiedDate",null);
		       	jo.put("resetDate",null);
		       	jo.put("resetKey",null);
		       	jo.put("authorities",admin_role);
		       	jo.put("geofences",null);
		       	response =   (Response) given().spec(spec)
		    			.body(jo)
		    			.header("Authorization", logAdminAuthKey)
		    		    .and().header("Content-Type", "application/json")
		    			.when()
		    	    .post("/api/users")
		    	    .then().extract();
		       	String body1 = response.getBody().asString();
		     	org.json.JSONObject jsonObject1 = new org.json.JSONObject(body1);
		     	String stringKey1 = jsonObject1.getString("resetKey");
		    	 
		    	
		    	log.info("response {}",response);
		    	  
		    	// set password for all users
		    	 setPwd = new JSONObject();
		    	setPwd.put("key", stringKey1);
		    	setPwd.put("newPassword", "admin");
		    	
		    	response =   (Response) given().spec(spec)
		    			.body(setPwd)
		    			.and().header("Content-Type", "application/json")
		    			.when()
		    	    .post("/api/account/reset_password/finish")
		    	    .then().extract();
		    	
		    	// login for all the users
		    			
				 loginVM = new LoginVM();
		  	  loginVM.setUsername(users[0]);
		  	  loginVM.setPassword("admin");
			  loginVM.setRememberMe(ADMIN_REMEMBERME);
		  	
		  	response =   (Response) given().spec(spec)
				.body(loginVM)
			    .and().header("Content-Type", "application/json")
				.when()
		  .post("/api/authenticate")
		  .then().extract();
			
		  	String userAuthToken = response.getHeader("Authorization");
		  	
		  	log.info("login response {}",userAuthToken);
		  	
		  	// creare location logs for all users
		  	
		    BufferedReader br1 = null;
		    String line1 = "";
		    String cvsSplitBy1 = ",";

		    try {

		        
		    	br1=  new BufferedReader(new FileReader(System.getProperty("user.dir")+"/src/test/resources/customerslog_table.csv"));
		        while ((line1 = br1.readLine()) != null) {
		           String[] locationlogs = line1.split(cvsSplitBy1);
		           
		           JSONObject locaionLog = new JSONObject();
		       	locaionLog.put("latitude",locationlogs[6]);
		       	locaionLog.put("longitude",locationlogs[7]);
		       	locaionLog.put("address",locationlogs[5]);
		       	locaionLog.put("logSource","GPS");
		       	locaionLog.put("createdDateTime",null);
		       	locaionLog.put("updatedDateTime",null);
		       	locaionLog.put("id",null);
		       	response =   (Response) given().spec(spec)
		       			.body(locaionLog)
		       			.header("Authorization", userAuthToken)
		       		    .and().header("Content-Type", "application/json")
		       			.when()
		       	    .post("/api/location-logs")
		       	    .then().extract();

		        }

		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    } finally {
		        if (br1 != null) {
		            try {
		                br1.close();
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		        }
		    }
		    	

		        }

		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    } finally {
		        if (br != null) {
		            try {
		                br.close();
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		        }
		    }
		    	
		    	
		    	          	

		
	}
	
	private static void deleteLogs() throws JSONException, SQLException{
		Connection con=DriverManager.getConnection(  
				"jdbc:mysql://localhost:3306/trakeye","root","root");  
		Statement stmt=con.createStatement();  
		int rs=stmt.executeUpdate("delete from trakeye.trakeye_location_log where user_id in (select id from trakeye.trakeye_user where created_by = 'logadmin' )"); 
		int rs1=stmt.executeUpdate("delete from trakeye.trakeye_user_authority where user_id in (select id from trakeye.trakeye_user where created_by = 'logadmin' or login='logadmin' )"); 
		int rs2=stmt.executeUpdate("delete from trakeye.trakeye_user where created_by = 'logadmin' or login='logadmin'"); 
		con.close();  
		/*delete from trakeye.trakeye_location_log where user_id in (select id from trakeye.trakeye_user where created_by = 'logadmin' );
		delete from trakeye.trakeye_user_authority where user_id in (select id from trakeye.trakeye_user where created_by = 'logadmin' or login='logadmin' );
		delete from trakeye.trakeye_user where created_by = 'logadmin' or login='logadmin';*/
			
	}

}
