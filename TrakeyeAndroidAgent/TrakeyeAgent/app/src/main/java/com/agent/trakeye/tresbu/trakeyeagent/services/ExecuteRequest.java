package com.agent.trakeye.tresbu.trakeyeagent.services;

/**
 * Created by sharmaan on 09-05-2016.
 */

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.agent.trakeye.tresbu.trakeyeagent.application.MyApplication;
import com.agent.trakeye.tresbu.trakeyeagent.database.UserDBHelper;
import com.agent.trakeye.tresbu.trakeyeagent.model.FieldPersonResponse;
import com.agent.trakeye.tresbu.trakeyeagent.model.GpsStatus;
import com.agent.trakeye.tresbu.trakeyeagent.model.LocationRequest;
import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiClient;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiInterface;
import com.agent.trakeye.tresbu.trakeyeagent.utils.ConnectivityReceiver;
import com.agent.trakeye.tresbu.trakeyeagent.utils.Network_Available;
import com.agent.trakeye.tresbu.trakeyeagent.utils.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExecuteRequest extends Service implements LocationListener, ConnectivityReceiver.ConnectivityReceiverListener {


    public static final String TAG = "Servicefor√fieldapp";
    private static final long LOCATION_INTERVAL = 5 * 1000; // 15 seconds
    private static final long THREAD_INTERVAL = 60 * 1000; // 2 minutes
    private static final long LOCATION_DISTANCE = 0;

    private final Handler threadHandler = new Handler();

    double lati = 0.0, longit = 0.0;
    String addr = null;
    boolean isCreated = false;
    boolean isOfflineCreated = false;
    long createId = 0;

    AppPreferences app;
    UserDBHelper dbHelper;

    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for Network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;
    Location mLocation = null;
    private boolean isRunning = false;
    private LocationManager mLocationManager = null;
    Context mContext = null;
    int level = 0;// battery level info
    private static final String FILENAME = "LocationsLog.txt";

    private Location prevLocation = null;
    private String LocSource = "";

    private Logger log;
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        app = new AppPreferences(this);
        dbHelper = new UserDBHelper(this);
        ApiClient.setToken_id(app.getToken());
        try {
            getApplicationContext().registerReceiver(this.batteryInfoReceiver,
                    new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        } catch (Exception e) {
        }
//        handler.removeCallbacks(sendUpdatesToUI);
//        handler.postDelayed(sendUpdatesToUI, 60000); // 5 mins

        threadHandler.removeCallbacks(postDatainThread);
        threadHandler.postDelayed(postDatainThread, THREAD_INTERVAL); // 5 mins
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        initializeLocationManager();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
        registerReceiver(gpsReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        mContext = this;
        initializeLocationManager();
        isRunning = true;
        log = LoggerFactory.getLogger(ExecuteRequest.class);
//        log.info("hello world");
        try {
            // get high accuracy provider
            LocationProvider high = mLocationManager.getProvider(mLocationManager.getBestProvider(createFineCriteria(), true));
            isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            this.canGetLocation = true;
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled)
            {
                    //                Log.d(TAG, "GPS Enabled");
                    mLocationManager.requestLocationUpdates(
                            high.getName(),
                            LOCATION_INTERVAL,
                            LOCATION_DISTANCE, this);
                    LocSource = "GPS";
                    if (mLocationManager != null) {
                        mLocation = mLocationManager.getLastKnownLocation(high.getName());
                        if (mLocation != null) {
                            lati = mLocation.getLatitude();
                            longit = mLocation.getLongitude();
                        }
                    }
            }
            if (isNetworkEnabled) {
                if(mLocation == null) {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            LOCATION_INTERVAL,
                            LOCATION_DISTANCE, this);
//                Log.d("Network", "Network");
                    LocSource = "NP";
                    if (mLocationManager != null) {
                        mLocation = mLocationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (mLocation != null) {
                            lati = mLocation.getLatitude();
                            longit = mLocation.getLongitude();
                        }
                    }
                }
            }


        } catch (Exception ex) {
//            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * this criteria needs high accuracy, high power, and cost
     */
    public static Criteria createFineCriteria() {
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        c.setAltitudeRequired(true);
        c.setBearingRequired(false);
        c.setSpeedRequired(true);
        c.setCostAllowed(true);
        c.setPowerRequirement(Criteria.POWER_HIGH);
        return c;
    }


    /*
            * Stop using GPS listener
    * Calling this function will stop using GPS in your app
    * */
    public void stopUsingGPS() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (mLocation != null) {
            lati = mLocation.getLatitude();
        }

        // return latitude
        return lati;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (mLocation != null) {
            longit = mLocation.getLongitude();
        }

        // return longitude
        return longit;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        try {
            getApplicationContext().unregisterReceiver(batteryInfoReceiver);
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    private void initializeLocationManager() {
//        Log.e(TAG, "initializeLocationManager");
        String bestProvider = null;
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        }
    }

    @Override
    public void onLocationChanged(Location location) {
//        Log.e(TAG, "okhttp onLocationChanged: " + location);
//        Log.e(TAG, "okhttp onLocationChanged: " + location.getSpeed());
//        util.showToast(mContext, "Accuracy : " + location.getAccuracy() + "\n Speed : " + location.getSpeed());
        Double dis = 0.0;
        try {
            dis = util.DirectDistance(location.getLatitude(),location.getLongitude(),prevLocation.getLatitude(),prevLocation.getLongitude());
            log.info("Recorded Logs --> "+LocSource+" A = "+location.getAccuracy() + " S = "+location.getSpeed() + " Post = "+ location.getLatitude() + "," + location.getLongitude() + " dis = "+dis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (level != 0 ) {
            if (location.getSpeed() > 0.0 || dis > 1) {
                if (getAddress(location) != null) {
                    onLocationLogCallAPI(getAddress(location), location);
                } else {
                    onLocationLogCallAPI("Not able to fetch address", location);
                }
            }
        }



        if (!app.getFirstRunningStatus()) {
            app.setFirstRunningStatus(true);
            if (getAddress(location) != null) {
                onLocationLogCallAPI(getAddress(location), location);
            } else {
                onLocationLogCallAPI("Not able to fetch address", location);
            }
        }
        prevLocation = location;
    }


    @Override
    public void onStatusChanged(String provider, int i, Bundle bundle) {
//        Log.e(TAG, "onStatusChanged: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
//        Log.e(TAG, "onProviderEnabled: " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
//        Log.e(TAG, "onProviderDisabled: " + provider);
    }

    private Runnable postDatainThread = new Runnable() {
        public void run() {
            checkforUpdateCall();
            threadHandler.postDelayed(this, THREAD_INTERVAL); // 60 seconds here you can give your time
        }
    };

    private void onLocationLogCallAPI(String address, Location location) {
        try {
            if (Network_Available.hasConnection(this)) {

                onLocationPushLogCallAPI(address, location);
            } else {

                saveDatainDataBase(location);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveDatainDataBase(Location location) {

        // util.showToast(this, "Offline logs for");
        addr = "in Offline mode, Address will not be available";
        FieldPersonResponse detail = new FieldPersonResponse();
        detail.setLogTimeAndZone(location.getTime());
        detail.setLatitude((location.getLatitude()));
        detail.setLongitude((location.getLongitude()));
        detail.setAddress("in Offline mode, Address will not be available");
        detail.setLogSource(LocSource);
        detail.setBatteryPercentage(level);
        detail.setCreatedDateTime(location.getTime());
        boolean flag = dbHelper.insertCustomerDetails(detail);
        log.info(LocSource+" A = "+location.getAccuracy() + " S = "+location.getSpeed() + " Save = "+ location.getLatitude() + "," + location.getLongitude() + " dis = "+util.DirectDistance(location.getLatitude(),location.getLongitude(),prevLocation.getLatitude(),prevLocation.getLongitude()));
    }

    private void onLocationPushLogCallAPI(String address, final Location location) {
        FieldPersonResponse detail = new FieldPersonResponse();
        detail.setLogTimeAndZone(System.currentTimeMillis());
        detail.setLatitude(location.getLatitude());
        detail.setLongitude(location.getLongitude());
        detail.setCreatedDateTime(location.getTime());
        detail.setBatteryPercentage(level);
        try {
            if (address == null && address.equalsIgnoreCase("null")) {
                detail.setAddress("Not able to fetch address");
            } else {
                detail.setAddress(address);
            }
        } catch (Exception e) {
            detail.setAddress("Not able to fetch address");
        }

        detail.setLogSource(LocSource);

        ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
        Call<LocationRequest> call = apiInterface.postUserLogs(detail);
        call.enqueue(new Callback<LocationRequest>() {
            @Override
            public void onResponse(Call<LocationRequest> call, Response<LocationRequest> response) {

                try {
                    if (response.code() == 200 || response.code() == 201) {
//                        Log.d(TAG, "responcse for create log" + response.body().toString());
                        createId = response.body().getId();
                        isCreated = true;
                        app.setUserDetail(response.body());
                        log.info(LocSource+" A = "+location.getAccuracy() + " S = "+location.getSpeed() + " Post = "+ location.getLatitude() + "," + location.getLongitude() + " dis = "+util.DirectDistance(location.getLatitude(),location.getLongitude(),prevLocation.getLatitude(),prevLocation.getLongitude()));
                    } else if (response.code() == 401) {
                        util.callLoginScreen(mContext);
                        log.info("logs 401");
                        stopSelf();
                    } else if (response.code() == 403)
                    {
                        log.info("logs 403");
                        util.showToast(getApplicationContext(),"* 403 Forbidden");
                    } else
                    {
                        util.showToast(getApplicationContext(),"* Something bad happened");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LocationRequest> call, Throwable t) {
                log.info("logs Failure" + t.toString());
                //   boolean flag = dbHelper.insertCustomerDetails(detail);
                // we need to handle for different status code:::: FUTURE
            }
        });
    }

    private void checkforUpdateCall() {
//        Log.i(TAG, "check for update");
        if (isCreated) {
            isCreated = false;
//            Log.i(TAG, "not updating");
        } else {
//            Log.i(TAG, "check for update else part");
            try {
                if (app.getUserDetail() != null) {
//                    Log.d(TAG, String.valueOf(app.getUserDetail().getId()));
                    if (app.getUserDetail().getLogSource().equalsIgnoreCase("NP")) {
                        // for future we will change the logic
                        LocationRequest detail = app.getUserDetail();
//                        detail.setLogSource("NP");
                        detail.setBatteryPercentage(level);
                        callUpdateRequest(detail);
                        log.info("updateNP : B = "+level+" Lat = "+detail.getLongitude()+" Lng = "+detail.getLongitude());
                    } else {
                        LocationRequest detail = app.getUserDetail();
//                        detail.setLogSource("GPS");
                        detail.setBatteryPercentage(level);
                        callUpdateRequest(detail);

                        log.info("updateGPS : B = "+level+" Lat = "+detail.getLongitude()+" Lng = "+detail.getLongitude());
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void callUpdateRequest(LocationRequest detail) {

        try {
            ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
            Call<LocationRequest> call = apiInterface.updateUserLogs(detail);
            call.enqueue(new Callback<LocationRequest>() {
                @Override
                public void onResponse(Call<LocationRequest> call, Response<LocationRequest> response) {

                    try {
                        if (response.code() == 200 || response.code() == 201) {
                            if (response.body() != null) {
                                Log.d(TAG, "==>" + response.body().toString());
                                app.setUserDetail(response.body());
//                                Log.d(TAG, "update the location log :" + response.body().getId());
//                                util.showToast(mContext, "update the location log :" + response.body().getId());
                            }
                        } else if (response.code() == 401) {
                            util.callLoginScreen(mContext);
                            stopSelf();
                        } else if (response.code() == 403)
                        {
                            util.showToast(getApplicationContext(),"* 403 Forbidden");
                        } else
                        {
                            util.showToast(getApplicationContext(),"* Something bad happened");

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<LocationRequest> call, Throwable t) {

                }
            });
        } catch (Exception ignored) {
        }
    }

    public String getAddress(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);

            if (obj.getCountryName() != null) {
                add = add + ", " + obj.getCountryName();
            }
            if (obj.getCountryCode() != null) {
                add = add + ", " + obj.getCountryCode();
            }
            if (obj.getAdminArea() != null) {
                add = add + ", " + obj.getAdminArea();
            }
            if (obj.getSubAdminArea() != null) {
                add = add + ", " + obj.getSubAdminArea();
            }
            if (obj.getLocality() != null) {
                add = add + ", " + obj.getLocality();
            }
            add.replace("/", "-");
            return add;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            util.showToast(this, e.getMessage());
            return null;
        }
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
//        Log.d(TAG, "offline network connected status" + isConnected);
        if (isConnected) {
//            Log.d(TAG, "offline recode number: " + dbHelper.numberOfRows());
            if (dbHelper.numberOfRows() > 0) {
                onOfflineDataLocationLogAPI(dbHelper.getAllCustomer());
            }

            // check for gps status
            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                callGpsStatus(true);
            } else {
                callGpsStatus(false);
            }
        }
    }


    private void onOfflineDataLocationLogAPI(final ArrayList<FieldPersonResponse> list) {
        try {
            if (Network_Available.hasConnection(this)) {
                if (list.size() > 0) {
                    int subArraylistSize = list.size() / 10;

                    int subItemSize = list.size() % 10;

                    // logic to create sublist
                    ArrayList<FieldPersonResponse> subList;
                    for (int i = 0; i < subArraylistSize; i++) {

                        subList = new ArrayList<>(list.subList(0, 10));
                        list.removeAll(subList);
                        if (subList.size() > 0) {
                            ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
                            Call<LocationRequest> call = apiInterface.postOfflineUserDetail(subList);
                            call.enqueue(new Callback<LocationRequest>() {
                                @Override
                                public void onResponse(Call<LocationRequest> call, Response<LocationRequest> response) {
                                    try {
                                        if (response.code() == 200 || response.code() == 201) {
//                                            Log.d(TAG, "offline data created:" + response.body().toString());
                                            createId = response.body().getId();
                                            isOfflineCreated = true;
                                            app.setUserDetail(response.body());
                                        } else if (response.code() == 401) {
                                            isOfflineCreated = false;
                                            util.callLoginScreen(mContext);
                                            stopSelf();
                                        } else if (response.code() == 403)
                                        {
                                            util.showToast(getApplicationContext(),"* 403 Forbidden");
                                        } else
                                        {
                                            util.showToast(getApplicationContext(),"* Something bad happened");

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        isOfflineCreated = false;
                                    }
                                }

                                @Override
                                public void onFailure(Call<LocationRequest> call, Throwable t) {
                                    try {
                                        isOfflineCreated = false;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }

                    if (subItemSize > 0) {
                        subList = new ArrayList<>(list.subList(0, subItemSize));
                        list.removeAll(subList);
                        if (subList.size() > 0) {
                            ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
                            Call<LocationRequest> call = apiInterface.postOfflineUserDetail(subList);
                            call.enqueue(new Callback<LocationRequest>() {
                                @Override
                                public void onResponse(Call<LocationRequest> call, Response<LocationRequest> response) {
                                    try {
                                        if (response.code() == 200 || response.code() == 201) {
//                                            Log.d(TAG, "offline data created:" + response.body().toString());
                                            createId = response.body().getId();
                                            isOfflineCreated = true;
                                            app.setUserDetail(response.body());
                                        } else if (response.code() == 401) {
                                            isOfflineCreated = false;
                                            util.callLoginScreen(mContext);
                                            stopSelf();
                                        } else if (response.code() == 403)
                                        {
                                            util.showToast(getApplicationContext(),"* 403 Forbidden");
                                        } else
                                        {
                                            util.showToast(getApplicationContext(),"* Something bad happened");

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        isOfflineCreated = false;
                                    }
                                }

                                @Override
                                public void onFailure(Call<LocationRequest> call, Throwable t) {
                                    try {
                                        isOfflineCreated = false;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }

                    if (isOfflineCreated) {
                        dbHelper.deleteAllData();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        }
    };


    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                //Do your stuff on GPS status change
                if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    callGpsStatus(true);
                } else {
                    callGpsStatus(false);
                }
            }
        }
    };

    private void callGpsStatus(boolean status) {
        try {
            if (Network_Available.hasConnection(mContext)) {
                if (ApiClient.getToken_id() == null) {
                    ApiClient.setToken_id(app.getToken());
                }
                GpsStatus gpsStatus = new GpsStatus();
                gpsStatus.setGpsStatus(status);
                gpsStatus.setLogin(app.getUsername());
                ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
                Call<Void> call = apiInterface.postGPSStatus(gpsStatus);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        try {
                            if (response.code() == 200 || response.code() == 201) {
//                                Log.d(TAG, "gps status:" + response.body().toString());
                            } else if (response.code() == 401) {
                                util.callLoginScreen(mContext);
                                stopSelf();
                            } else if (response.code() == 403)
                            {
                                util.showToast(getApplicationContext(),"* 403 Forbidden");
                            } else
                            {
                                util.showToast(getApplicationContext(),"* Something bad happened");

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



