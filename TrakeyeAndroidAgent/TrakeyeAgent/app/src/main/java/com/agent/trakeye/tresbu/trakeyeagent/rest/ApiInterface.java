package com.agent.trakeye.tresbu.trakeyeagent.rest;

import com.agent.trakeye.tresbu.trakeyeagent.model.AgentDashboard;
import com.agent.trakeye.tresbu.trakeyeagent.model.Asset;
import com.agent.trakeye.tresbu.trakeyeagent.model.AssetType;
import com.agent.trakeye.tresbu.trakeyeagent.model.Case;
import com.agent.trakeye.tresbu.trakeyeagent.model.CaseRequest;
import com.agent.trakeye.tresbu.trakeyeagent.model.CaseType;
import com.agent.trakeye.tresbu.trakeyeagent.model.FieldPersonResponse;
import com.agent.trakeye.tresbu.trakeyeagent.model.GpsStatus;
import com.agent.trakeye.tresbu.trakeyeagent.model.LocationRequest;
import com.agent.trakeye.tresbu.trakeyeagent.model.Login;
import com.agent.trakeye.tresbu.trakeyeagent.model.LoginResponse;
import com.agent.trakeye.tresbu.trakeyeagent.model.Notification;
import com.agent.trakeye.tresbu.trakeyeagent.model.Service;
import com.agent.trakeye.tresbu.trakeyeagent.model.UpdateCase;
import com.agent.trakeye.tresbu.trakeyeagent.model.UserInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;


/**
 * Created by Tresbu on 18-Oct-16.
 */

public interface ApiInterface {

    @POST("api/authenticate")
    Call<LoginResponse> doLogin(@Body Login login);

    @GET("api/account")
    Call<UserInfo> getAccountInfo();


    @POST("api/location-logs")
    Call<LocationRequest> postUserLogs(@Body FieldPersonResponse request);

    @PUT("api/location-logs")
    Call<LocationRequest> updateUserLogs(@Body LocationRequest request);

    @GET("api/case-types?page=0&size=20&sort=id,asc")
    Call<ArrayList<CaseType>> getCaseTypes();

    @GET("api/case-types")
    Call<ArrayList<CaseType>> getCaseTypes(@Query("page") int pagestart, @Query("size") int size, @Query("sort") String sort);

    @GET("api/asset-types?page=0&size=20&sort=id,asc")
    Call<ArrayList<AssetType>> getAssetTypes();

    @GET("api/assets")
    Call<ArrayList<Asset>> getAllAssets(@Query("page") int pagestart, @Query("size") int size, @Query("sort") String sort);

    @POST("api/assets")
    Call<Void> postUserAsset(@Body Asset assetRequest);

    @PUT("api/assets")
    Call<Void> updateUserAsset(@Body Asset assetRequest);

    @GET("api/assets/{path}")
    Call<Asset> getAssetDetail(@Path(value = "path") String path);

    @GET
    Call<ArrayList<Asset>> getAllAssetsSearch(@Url String url, @Query("page") int pagestart, @Query("size") int size, @Query("sort") String sort);

    @GET("api/tr-cases")
    Call<ArrayList<Case>> getAllCases(@Query("page") int pagestart, @Query("size") int size, @Query("sort") String sort);

    @GET
    Call<ArrayList<Case>> getAllCasesSearch(@Url String url, @Query("page") int pagestart, @Query("size") int size, @Query("sort") String sort);

    @GET("api/tr-cases/{path}")
    Call<Case> getCaseDetail(@Path(value = "path") String path);

    @GET("api/tr-services/{path}")
    Call<Service> getServiceDetail(@Path(value = "path") String path);

    @GET
    Call<ArrayList<Service>> getAllServicesSearch(@Url String url, @Query("page") int pagestart, @Query("size") int size, @Query("sort") String sort);

    @GET("api/tr-notifications/{path}")
    Call<Notification> getNotificationDetail(@Path(value = "path") String path);

    @PUT("api/tr-notifications")
    Call<Notification> updateNotification(@Body Notification request);

    @GET
    Call<ArrayList<Notification>> getAllNotificationsSearch(@Url String url, @Query("page") int pagestart, @Query("size") int size, @Query("sort") String sort);

    @POST("api/tr-cases")
    Call<Case> postUserCase(@Body CaseRequest caseRequest);

    @PUT("api/tr-cases")
    Call<UpdateCase> updateUserCase(@Body UpdateCase caseRequest);

    @GET("api/tr-notifications")
    Call<ArrayList<Notification>> getAllNotifications(@Query("page") int pagestart, @Query("size") int size, @Query("sort") String sort);

    @GET("api/tr-services")
    Call<ArrayList<Service>> getAllServices(@Query("page") int pagestart, @Query("size") int size, @Query("sort") String sort);

    @POST("api/tr-services")
    Call<Service> postUserServices(@Body Service caseRequest);


    @PUT("api/tr-services")
    Call<Service> updateUserServices(@Body Service caseRequest);

    @GET("api/dashboard/agentdashboard")
    Call<AgentDashboard> getLatestUserDetails();

    @POST("api/location-bulklogs")
    Call<LocationRequest> postOfflineUserDetail(@Body ArrayList<FieldPersonResponse> bulkRequest);

    @POST("api/updateusergpsstatus")
    Call<Void> postGPSStatus(@Body GpsStatus gpsStatus);

    @POST("api/account")
    Call<Void> updateUserProfile(@Body UserInfo userInfo);

    @GET("api/account")
    Call<UserInfo> getUserProfile();
}
