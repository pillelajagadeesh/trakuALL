package com.agent.trakeye.tresbu.trakeyeagent.utils;

/**
 * Created by sharmaan on 23-05-2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.agent.trakeye.tresbu.trakeyeagent.model.GpsStatus;
import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiClient;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiInterface;
import com.agent.trakeye.tresbu.trakeyeagent.services.ExecuteRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OnStartUp extends BroadcastReceiver {

    LocationManager mLocationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) {
            if (info.isConnected() ||intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                //start service
                startService(context);
            }
            else {
                //stop service
            }
        }

        if (mLocationManager == null) {
            mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        }
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            try {
                //Do your stuff on GPS status change
                if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    callGpsStatus(context, true);
                } else {
                    callGpsStatus(context, false);
                }
            }catch (Exception e){}
        }
    }

    private void startService(Context context) {
        Intent i = new Intent(context, ExecuteRequest.class);
        context.startService(i);
    }


    private void callGpsStatus(final Context mContext,boolean status) {
        AppPreferences app= new AppPreferences(mContext);
        if(Network_Available.hasConnection(mContext))
        {
            if (ApiClient.getToken_id() == null) {
                ApiClient.setToken_id(app.getToken());
            }
            GpsStatus gpsStatus= new GpsStatus();
            gpsStatus.setGpsStatus(status);
            gpsStatus.setLogin(app.getUserInfo().getLogin());
            ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
            Call<Void> call = apiInterface.postGPSStatus(gpsStatus);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    try {
                        if (response.code() == 200 || response.code() == 201) {
//                            Log.d("Reboot", "gps status:" + response.body().toString());

                        } else if (response.code() == 401) {
                            util.callLoginScreen(mContext);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

}