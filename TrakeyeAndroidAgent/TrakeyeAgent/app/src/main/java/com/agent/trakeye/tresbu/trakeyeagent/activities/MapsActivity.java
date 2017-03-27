package com.agent.trakeye.tresbu.trakeyeagent.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.model.AgentDashboard;
import com.agent.trakeye.tresbu.trakeyeagent.model.FieldPersonResponse;
import com.agent.trakeye.tresbu.trakeyeagent.model.LocationRequest;
import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiClient;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiInterface;
import com.agent.trakeye.tresbu.trakeyeagent.services.ExecuteRequest;
import com.agent.trakeye.tresbu.trakeyeagent.utils.AppConstants;
import com.agent.trakeye.tresbu.trakeyeagent.utils.ConnectivityReceiver;
import com.agent.trakeye.tresbu.trakeyeagent.utils.Network_Available;
import com.agent.trakeye.tresbu.trakeyeagent.utils.util;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Integer.parseInt;

public class MapsActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {


    public static MapsActivity instance = null;

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    GoogleMap googleMap;
    Timer myTimer;
    int level;
    String statusStr = "INACTIVE";
    TextView tvName, tvOperator, tvBattery, tvLocation, tvPhone, tvStatus, tvStartTime, tvNotificationCount, tvCaseCount;
    ImageView menuBell, menuCase;
    TelephonyManager telephonyManager;
    FieldPersonResponse detail;
    LocationRequest userDetails;
    RelativeLayout ivMenu;
    //private Dialog dialog;
    AppPreferences app;
    Location mLocation = null;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private boolean internetConnected = true;


    public static MapsActivity getInstance() {
        if (instance != null) {
            return instance;
        }
        return null;
    }


    //AlertDialog.Builder builder;
    AlertDialog gpsEnableDialog = null;
    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 2);
            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            String technology = intent.getExtras().getString(
                    BatteryManager.EXTRA_TECHNOLOGY);
            int temperature = intent.getIntExtra(
                    BatteryManager.EXTRA_TEMPERATURE, 0);
            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            String version = "ANDROID  " + Build.VERSION.RELEASE;


            String strHealth;
            if (health == BatteryManager.BATTERY_HEALTH_GOOD) {
                strHealth = "Good";
            } else if (health == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
                strHealth = "Over Heat";
            } else if (health == BatteryManager.BATTERY_HEALTH_DEAD) {
                strHealth = "Dead";
            } else if (health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
                strHealth = "Over Voltage";
            } else if (health == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {
                strHealth = "Unspecified Failure";
            } else {
                strHealth = "Unknown";
            }

            String pluggedSrc = plugged == 0 ? "Unplugged"
                    : plugged == 1 ? "AC" : plugged == 2 ? "USB"
                    : plugged == 4 ? "WIRELESS" : "Unknown";
            String statusStr = status == 2 ? "Charging"
                    : status == 3 ? "Discharging" : status == 5 ? "Full"
                    : "Unknown";

            tvBattery.setText("" + Integer.toString(level) + "%");
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onResume() {
        super.onResume();
        registerInternetCheckReceiver();

        try {
            getApplicationContext().registerReceiver(this.batteryInfoReceiver,
                    new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

            getApplicationContext().registerReceiver(this.notificationReceiver,
                    new IntentFilter(AppConstants.NOTIFICATIONCOUNTACTION));

            getApplicationContext().registerReceiver(this.caseCountReceiver,
                    new IntentFilter(AppConstants.CASECOUNTACTION));

        } catch (Exception e) {
        }

        try {
            ApiClient.setToken_id(app.getToken());
        } catch (Exception e) {
        }
    }


    BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String notificationCount = intent.getStringExtra("NotificationCount");
            tvNotificationCount.setText(notificationCount+"");
        }
    };

    BroadcastReceiver caseCountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String notificationCount = intent.getStringExtra("CaseCount");
            tvCaseCount.setText(notificationCount+"");
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.home, menu);
        RelativeLayout notificationLayout = (RelativeLayout) menu.findItem(R.id.action_notification).getActionView();
        tvNotificationCount = (TextView) notificationLayout.findViewById(R.id.notification_count);

        menuBell = (ImageView) notificationLayout.findViewById(R.id.bell_icon);
        menuBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnected) {
                    Intent caseIntent = new Intent(getApplicationContext(), NotificationActivity.class);
                    startActivity(caseIntent);
                }else{
                    util.makeAlertdiallog(MapsActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });


        RelativeLayout caseLayout = (RelativeLayout) menu.findItem(R.id.action_case).getActionView();
        tvCaseCount = (TextView) caseLayout.findViewById(R.id.case_count);
        menuCase = (ImageView) caseLayout.findViewById(R.id.case_icon);
        menuCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnected){
                    Intent notificationIntent = new Intent(getApplicationContext(), CasesActivity.class);
                    startActivity(notificationIntent);
                }else{
                    util.makeAlertdiallog(MapsActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        setContentView(R.layout.activity_maps);

        instance = MapsActivity.this;

        userDetails = new LocationRequest();
        app = new AppPreferences(this);
        if (app.getUsername().equalsIgnoreCase("")) {
            app.setSessionVal(false);
            finish();
        }else{
            app.setSessionVal(true);
        }

        userDetails = app.getUserDetail();
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_1);
        setSupportActionBar(toolbar);
        setupActionBarTitleWithFont(toolbar, "My Location");
        Intent intent1 = new Intent(MapsActivity.this, ExecuteRequest.class);
        startService(intent1);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);

        tvBattery = (TextView) findViewById(R.id.tvBattery);
        tvName = (TextView) findViewById(R.id.tvName);
        tvOperator = (TextView) findViewById(R.id.tvOperator);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvStartTime = (TextView) findViewById(R.id.startTime);
        tvStatus = (TextView) findViewById(R.id.tvStatus);

        if (app.getUserInfo().getFirstName() != null) {
            if (app.getUserInfo().getLastName() != null) {
                tvName.setText(app.getUserInfo().getFirstName().toUpperCase() + " " + app.getUserInfo().getLastName().toUpperCase());
            } else {
                tvName.setText(app.getUserInfo().getFirstName().toUpperCase());
            }
        } else {
            tvName.setText(app.getUserInfo().getLogin().toUpperCase());
        }

        getLatest();

        if (userDetails != null) {

            tvLocation.setText("" + userDetails.getAddress());
        }

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


        myTimer = new Timer();
        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        supportMapFragment.getMapAsync(this);

        myTimer.schedule(new TimerTask()
        {
            @Override
            public void run() {
                getLatest();
            }
        }, 30000, 120000);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

//       getCaseCount();
//       getNotificationCount();
    }


    /**
     * Method to set actionbar title.
     * call after setSupportActionBar.
     *
     * @param toolbar Toolbar
     * @param title   Title to set.
     */
    protected void setupActionBarTitleWithFont(Toolbar toolbar, String title) {
        if (toolbar == null) {
            return;
        }


        TextView actionbarTitle = (TextView) toolbar.findViewById(R.id.custom_toolbar_title);
        actionbarTitle.setText(title);

        actionbarTitle.setEllipsize(null);
        ivMenu = (RelativeLayout) toolbar.findViewById(R.id.ivMenu);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnected) {
                    showptionsDialog();
                }else{
                    util.makeAlertdiallog(MapsActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_geo_fence:
//                return true;
           /* case R.id.action_profile:
                Intent profileIntent = new Intent(this, ProfileDetailActivity.class);
                startActivity(profileIntent);
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showptionsDialog() {
        if(internetConnected) {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        }else{
            util.makeAlertdiallog(MapsActivity.this,getString(R.string.please_check_your_internet_connectivity));
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
//            Log.d("locationchanged:", tvStatus.getText().toString());
            googleMap.clear();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            mLocation = location;
            //googleMap.addMarker(new MarkerOptions().position(latLng).title("")).showInfoWindow();
            if (tvStatus.getText().toString().equalsIgnoreCase("IDLE")) {
                googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))).showInfoWindow();
            } else if (tvStatus.getText().toString().equalsIgnoreCase("ACTIVE")) {

                googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).showInfoWindow();
            } else {
                googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).showInfoWindow();
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(14f));
            if (ConnectivityReceiver.isConnected()) {
                if (getAddress(latitude, longitude) != null) {
                    tvLocation.setText("" + getAddress(latitude, longitude));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            String currentAddress = obj.getSubAdminArea() + ", "
                    + obj.getAdminArea();
            double latitude = obj.getLatitude();
            double longitude = obj.getLongitude();
            String currentCity = obj.getSubAdminArea();
            String currentState = obj.getAdminArea();
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

            tvLocation.setText("" + add);
            return add;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        try {
            getApplicationContext().unregisterReceiver(batteryInfoReceiver);
        } catch (Exception e) {
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        try {
            getApplicationContext().unregisterReceiver(batteryInfoReceiver);
            getApplicationContext().unregisterReceiver(notificationReceiver);
            getApplicationContext().unregisterReceiver(caseCountReceiver);
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        unregisterReceiver(broadcastReceiver);
        // TODO Auto-generated method stub
        super.onPause();
        try {
            getApplicationContext().unregisterReceiver(batteryInfoReceiver);
        } catch (Exception e) {
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsStatus) {
            if (!gpsStatus) {
                if (gpsEnableDialog == null) {
                    final AlertDialog.Builder networkAlertDialogBuilder = new AlertDialog.Builder(this);
                    networkAlertDialogBuilder
                            .setTitle("Enable GPS")
                            .setMessage("You need to enable GPS for best results.  Please enable GPS " +
                                    "in next screen")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    gpsEnableDialog = null;
                                }
                            });
                    gpsEnableDialog = networkAlertDialogBuilder.create();
                    gpsEnableDialog.show();
                }
            }
        }
        if (location != null) {
            mLocation = location;
            onLocationChanged(location);
        }
        try {
            locationManager.requestLocationUpdates(bestProvider, 5 * 1000, 0f, this);
            if (userDetails != null) {
                double latitude = (userDetails.getLatitude());
                double longitude = (userDetails.getLongitude());
                LatLng latLng = new LatLng(latitude, longitude);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(14f));
                tvLocation.setText("" + getAddress(latitude, longitude));
            }
        } catch (Exception e) {
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Maps Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        /*if (ConnectivityReceiver.isConnected() == true && userDetails != null) {
            getLatest();
        }*/
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }


    private void getLatest() {
        if (Network_Available.hasConnection(this)) {
            if (ApiClient.getToken_id() == null) {
                ApiClient.setToken_id(app.getToken());
            }


            ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
            Call<AgentDashboard> call = apiInterface.getLatestUserDetails();
            call.enqueue(new Callback<AgentDashboard>() {
                @Override
                public void onResponse(Call<AgentDashboard> call, Response<AgentDashboard> response) {

                    try {
                        if (response.code() == 200 || response.code() == 201) {
                            showOnMap(response.body());
                        } else if (response.code() == 401) {
                            util.callLoginScreen(MapsActivity.this);
                            finish();
                        } else if (response.code() == 403)
                        {
                            util.showToast(getApplicationContext(),"* 403 Forbidden");
                        } else
                        {
                            util.showToast(getApplicationContext(),"* Something bad happened");

                        }
                    } catch (Exception e) {
                        tvStatus.setText("INACTIVE");
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<AgentDashboard> call, Throwable t) {
                    try {
                        tvStatus.setText("INACTIVE");
                    } catch (Exception e) {
                    }

                }
            });
        } else {
            try {
                tvStatus.setText("INACTIVE");
            } catch (Exception e) {
            }
        }
    }

    private void showOnMap(AgentDashboard body) {

        if (body != null)
        {
            //  Collections.reverse(body);
            if (body.getNotificationCounts().getSENT() != null)
            {
                setNotificationCountValue(body.getNotificationCounts().getSENT());

            }
            else
            {
                setNotificationCountValue("0");
            }
//            NEW, ASSIGNED,
//                    INPROGRESS, PENDING "
            if (body.getCaseCounts() != null) {
                int caseCountVal = 0;

                if (body.getCaseCounts().getNEW() != null) {
                    caseCountVal = parseInt("" + (caseCountVal + parseInt(body.getCaseCounts().getNEW())));
                }
                if (body.getCaseCounts().getASSIBNED() != null) {
                    caseCountVal = parseInt("" + (caseCountVal + parseInt(body.getCaseCounts().getASSIBNED())));
                }
                if (body.getCaseCounts().getINPROGRESS() != null) {
                    caseCountVal = parseInt("" + (caseCountVal + parseInt(body.getCaseCounts().getINPROGRESS())));
                }
                if (body.getCaseCounts().getPENDING() != null) {
                    caseCountVal = parseInt("" + (caseCountVal + parseInt(body.getCaseCounts().getPENDING())));
                }

                if (caseCountVal > 0)
                {
                 setCaseCountValue(new StringBuilder().append(caseCountVal).append("").toString());
                } else {
                    setCaseCountValue("0");
                }
            }
        }
        if (body.getNotificationCounts().getSENT() != null)
        {
            try {
                tvNotificationCount.setText(body.getNotificationCounts().getSENT());
            } catch (NullPointerException ignored) {}
        }

        if (body.getLiveLogs().size() > 0)
        {
            statusStr = body.getLiveLogs().get(0).getStatus().name();
            tvLocation.setText("" + body.getLiveLogs().get(0).getAddress());
            tvStatus.setText("" + body.getLiveLogs().get(0).getStatus().name());
            if (mLocation != null) {
                onLocationChanged(mLocation);
            }

        } else {
            tvStatus.setText("INACTIVE");
        }

    }



    public void setCaseCountValue(String total_list_items)
    {
        Intent nIntent = new Intent(AppConstants.CASECOUNTACTION);
        nIntent.putExtra("CaseCount",total_list_items);
        sendBroadcast(nIntent);
    }



    public void setNotificationCountValue(String total_list_items)
    {
        app.setNotificationCount(total_list_items+"");

        Intent nIntent = new Intent(AppConstants.NOTIFICATIONCOUNTACTION);
        nIntent.putExtra("NotificationCount",total_list_items);
        sendBroadcast(nIntent);
    }

    /**
     * Method to register runtime broadcast receiver to show snackbar alert for internet connection..
     */
    private void registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, internetFilter);
    }

    /**
     * Runtime Broadcast receiver inner class to capture internet connectivity events
     */
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = getConnectivityStatusString(context);
            setSnackbarMessage(status, false);

        }
    };

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

    private void setSnackbarMessage(String status, boolean showBar) {
        int duration = 0;
        int color = 0;
        boolean isConnect = false, lastConnectionStatus = false;

        String internetStatus = "";
        if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {
            internetStatus = "Internet Connected";
            duration = Snackbar.LENGTH_LONG;
            isConnect = true;
            color = Color.WHITE;
        } else {
            duration = Snackbar.LENGTH_INDEFINITE;
            internetStatus = "Lost Internet Connection";
            isConnect = false;
            color = Color.YELLOW;
        }
        lastConnectionStatus = app.getConnectionStatus();
        app.setConnectionStatus(isConnect);

        if (lastConnectionStatus && isConnect) {

        } else {
            snackbar = Snackbar
                    .make(coordinatorLayout, internetStatus, duration)
                    .setAction("X", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
            // Changing message text color
            snackbar.setActionTextColor(Color.WHITE);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            if (internetStatus.equalsIgnoreCase("Lost Internet Connection")) {
                if (internetConnected) {
                    snackbar.show();
                    internetConnected = false;
                }
            } else {
                if (!internetConnected) {
                    internetConnected = true;
                    snackbar.show();
                }
            }
        }


    }
}