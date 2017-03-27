package com.agent.trakeye.tresbu.trakeyeagent.prefernces;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.agent.trakeye.tresbu.trakeyeagent.model.AssetType;
import com.agent.trakeye.tresbu.trakeyeagent.model.CaseType;
import com.agent.trakeye.tresbu.trakeyeagent.model.LocationRequest;
import com.agent.trakeye.tresbu.trakeyeagent.model.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sharmaan on 13-04-2016.
 */
public class AppPreferences {

    public static final String KEY_USER_DETAIL = "detail";
    public static final String KEY_SESSION_EXPIRE = "expire";
    public static final String KEY_USER_NAME = "username";
    public static final String KEY_AGENT_NAME = "name";
    public static final String KEY_USER_PASSWORD = "password";
    public static final String KEY_USER_ADMIN = "admin";
    public static final String KEY_SERVER_NAME = "server";
    public static final String KEY_USER_INFO = "user_info";
    public static final String KEY_USER_TOKEN = "id_token";
    public static final String KEY_CASETYPE = "caseType";
    public static final String KEY_ASSETTYPE = "assetType";
    public static final String KEY_FIRSTTIMERUNNING = "firsttym";
    public static final String KEY_FCM_TOKEN = "fcmtoken";
    private static final String KEY_NOTIFICATION_COUNT = "notification_count";
    private static final String KEY_CONNECTIONSTATUS = "network_status";


    private static final String APP_SHARED_PREFS = AppPreferences.class
            .getSimpleName();


    public static String authKey = null;
    private SharedPreferences _sharedPrefs;
    private SharedPreferences.Editor _prefsEditor;
    private boolean connectionStatus;

    public AppPreferences(Context context) {
        try {
            this._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS,
                    Activity.MODE_PRIVATE);
            this._prefsEditor = _sharedPrefs.edit();
        } catch (Exception e) {
        }
    }

    public LocationRequest getUserDetail() {
        LocationRequest userhHealthRange;
        try {
            Gson gson = new Gson();
            String userHealthRangesString = _sharedPrefs.getString(
                    KEY_USER_DETAIL, null);
            userhHealthRange = gson.fromJson(userHealthRangesString,
                    LocationRequest.class);
            return userhHealthRange;
        } catch (Exception ex) {

            return null;
        }
    }

    public void setUserDetail(LocationRequest detail) {
        Gson gson = new Gson();
        String json = gson.toJson(detail);
        _prefsEditor.putString(KEY_USER_DETAIL, json).commit();
    }

    public UserInfo getUserInfo() {
        UserInfo userInfo;
        try {
            Gson gson = new Gson();
            String userHealthRangesString = _sharedPrefs.getString(
                    KEY_USER_INFO, null);
            userInfo = gson.fromJson(userHealthRangesString,
                    UserInfo.class);
            return userInfo;
        } catch (Exception ex) {

            return null;
        }
    }

    public void setUserInfo(UserInfo detail) {
        Gson gson = new Gson();
        String json = gson.toJson(detail);
        _prefsEditor.putString(KEY_USER_INFO, json).commit();
    }

    public ArrayList<CaseType> getCaseType() {
        ArrayList<CaseType> userInfo;
        try {
            Gson gson = new Gson();
            String userHealthRangesString = _sharedPrefs.getString(
                    KEY_CASETYPE, null);
            userInfo = gson.fromJson(userHealthRangesString,
                    new TypeToken<List<CaseType>>() {
                    }.getType());
            return userInfo;
        } catch (Exception ex) {

            return null;
        }
    }

    public void setCaseType(ArrayList<CaseType> detail) {
        Gson gson = new Gson();
        String json = gson.toJson(detail);
        _prefsEditor.putString(KEY_CASETYPE, json).commit();

    }

    public ArrayList<AssetType> getAssetType() {
        ArrayList<AssetType> userInfo;
        try {
            Gson gson = new Gson();
            String userHealthRangesString = _sharedPrefs.getString(
                    KEY_ASSETTYPE, null);
            userInfo = gson.fromJson(userHealthRangesString,
                    new TypeToken<List<AssetType>>() {
                    }.getType());
            return userInfo;
        } catch (Exception ex) {
            return null;
        }
    }

    public void setAssetType(ArrayList<AssetType> detail) {
        Gson gson = new Gson();
        String json = gson.toJson(detail);
        _prefsEditor.putString(KEY_ASSETTYPE, json).commit();

    }

    public String getUsername() {
        return _sharedPrefs.getString(KEY_USER_NAME, "");
    }

    public void setUsername(String username) {
        _prefsEditor.putString(KEY_USER_NAME, username);
        _prefsEditor.commit();
    }

    public String getServerName() {
        return _sharedPrefs.getString(KEY_SERVER_NAME, "");
    }

    public void setServername(String servername) {
        _prefsEditor.putString(KEY_SERVER_NAME, servername);
        _prefsEditor.commit();
    }
    public String getFCMToken() {
        return _sharedPrefs.getString(KEY_FCM_TOKEN, "");
    }

    public void setFCMToken(String fcmtoken) {
        authKey = fcmtoken;
        _prefsEditor.putString(KEY_FCM_TOKEN, fcmtoken);
        _prefsEditor.commit();
    }

    public String getToken() {
        return _sharedPrefs.getString(KEY_USER_TOKEN, "");
    }

    public void setToken(String token) {
        authKey = token;
        _prefsEditor.putString(KEY_USER_TOKEN, token);
        _prefsEditor.commit();
    }

    public String getName() {
        return _sharedPrefs.getString(KEY_AGENT_NAME, "");
    }

    public void setName(String username) {
        _prefsEditor.putString(KEY_AGENT_NAME, username);
        _prefsEditor.commit();
    }


    public String getPassword() {
        return _sharedPrefs.getString(KEY_USER_PASSWORD, "");
    }

    public void setPassword(String password) {
        _prefsEditor.putString(KEY_USER_PASSWORD, password);
        _prefsEditor.commit();
    }


    public Boolean getAdminVal() {
        return _sharedPrefs.getBoolean(KEY_USER_ADMIN, false);
    }

    public void setAdminVal(Boolean AdminVal) {
        _prefsEditor.putBoolean(KEY_USER_ADMIN, AdminVal);
        _prefsEditor.commit();
    }

    public Boolean getSessionVal() {
        return _sharedPrefs.getBoolean(KEY_SESSION_EXPIRE, false);
    }

    public void setSessionVal(Boolean SessionVal) {
        _prefsEditor.putBoolean(KEY_SESSION_EXPIRE, SessionVal);
        _prefsEditor.commit();
    }

    public Boolean getFirstRunningStatus() {
        return _sharedPrefs.getBoolean(KEY_FIRSTTIMERUNNING, false);
    }

    public void setFirstRunningStatus(Boolean AdminVal) {
        _prefsEditor.putBoolean(KEY_FIRSTTIMERUNNING, AdminVal);
        _prefsEditor.commit();
    }

    public void setNotificationCount(String notificationCount) {
        _prefsEditor.putString(KEY_NOTIFICATION_COUNT, notificationCount);
        _prefsEditor.commit();
    }

    public String getNotificationCount() {
        return _sharedPrefs.getString(KEY_NOTIFICATION_COUNT, "0");
    }


    public void setConnectionStatus(boolean isConnected) {
        _prefsEditor.putBoolean(KEY_CONNECTIONSTATUS, isConnected);
        _prefsEditor.commit();
    }

    public Boolean getConnectionStatus() {
        return _sharedPrefs.getBoolean(KEY_CONNECTIONSTATUS, false);
    }
}
