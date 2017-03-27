package com.agent.trakeye.tresbu.trakeyeagent.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.adapters.AssetTypeAdapter;
import com.agent.trakeye.tresbu.trakeyeagent.database.UserDBHelper;
import com.agent.trakeye.tresbu.trakeyeagent.model.Asset;
import com.agent.trakeye.tresbu.trakeyeagent.model.AssetCoordinates;
import com.agent.trakeye.tresbu.trakeyeagent.model.AssetType;
import com.agent.trakeye.tresbu.trakeyeagent.model.AssetTypeAttributeValues;
import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiClient;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiInterface;
import com.agent.trakeye.tresbu.trakeyeagent.utils.Network_Available;
import com.agent.trakeye.tresbu.trakeyeagent.utils.util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetManagementActivity extends Activity implements LocationListener {

    int pos = 0;
    long itemId;
    String item;
    String itemDesc;
    double latitude, longitude;

    LinearLayout btStart, btSave, btPin;
    EditText etName, etDescription;
    LinearLayout llAssetTypeLayout;
    LinearLayout llSpread;
    RelativeLayout rlBack, rlButton;

    Spinner spAssetType;
    AppPreferences appPreferences;
    AssetTypeAdapter assetTypeAdapter;

    UserDBHelper dbHelper;

    AssetCoordinates pathDetail;

    // flag for GPS status
    boolean isGPSEnabled = false;
    private boolean isAssetPathSaved = true;
    private LocationManager mLocationManager = null;
    Location mLocation = null;

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private boolean internetConnected = true;

    @Override
    public void onBackPressed() {
        this.finish();
        isAssetPathSaved = true;
        dbHelper.deleteAllAssetData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assets_management_activity);

        appPreferences = new AppPreferences(this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);

        dbHelper = new UserDBHelper(this);
        if(internetConnected) {
            initializeLocationManager();
            getAssetTypeVal();
        }else{
            AssetManagementActivity.this.finish();
        }

        btSave = (LinearLayout) findViewById(R.id.bt_save);
        btStart = (LinearLayout) findViewById(R.id.bt_start);

        etName = (EditText) findViewById(R.id.et_name);
        etDescription = (EditText) findViewById(R.id.et_desc);
        spAssetType = (Spinner) findViewById(R.id.spAssetType);
        llAssetTypeLayout = (LinearLayout) findViewById(R.id.llAssetTypeLayout);

        rlButton = (RelativeLayout) findViewById(R.id.rl_button_layout);
        rlButton.setVisibility(View.GONE);
        llSpread = (LinearLayout) findViewById(R.id.llSpread);
        btPin = (LinearLayout) findViewById(R.id.bt_pin);

        btPin.setVisibility(View.GONE);
        llSpread.setVisibility(View.GONE);

        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAssetPathSaved = true;
                dbHelper.deleteAllAssetData();
                AssetManagementActivity.this.finish();
            }
        });

        btStart.setBackgroundResource(R.color.menutoolbar);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internetConnected) {
                    if (etName.getText().toString().length() == 0) {
                        showError("Please Enter Asset Name");
                    }else {
                        isAssetPathSaved = false;
                        btStart.setBackgroundResource(R.color.lightgray);
                        btStart.setEnabled(false);
                        util.makeAlertdiallog(AssetManagementActivity.this, "Asset Path Started");
                    }
                }else{
                    util.makeAlertdiallog(AssetManagementActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });

        btSave.setBackgroundResource(R.color.menutoolbar);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnected) {
                    isAssetPathSaved = true;
//                Log.d(ExecuteRequest.TAG, "Asset recode number: " + dbHelper.numberOfAsset());
                    if (dbHelper.numberOfAsset() > 0) {
                        postAssetPath(dbHelper.getAllAssetPath());
                    } else {
                        showError("No Asset Path is Available : " + dbHelper.numberOfAsset());
                    }
                }else{
                    util.makeAlertdiallog(AssetManagementActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });


        btPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internetConnected) {
                    saveIssueIntoDataBase();
                }else{
                    util.makeAlertdiallog(AssetManagementActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });

        try {
            // get high accuracy provider
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            LocationProvider high =
                    mLocationManager.getProvider(mLocationManager.getBestProvider(criteria, true));
            isGPSEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (mLocation == null) {
                mLocationManager.requestLocationUpdates(
                        high.getName(), 5000, 0, this);

                if (mLocationManager != null) {
                    mLocation = mLocationManager
                            .getLastKnownLocation(high.getName());
                    if (mLocation != null) {
                        latitude = mLocation.getLatitude();
                        longitude = mLocation.getLongitude();
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    List<EditText> allEds = null;

    private void getAssetTypeVal() {
        if (Network_Available.hasConnection(this)) {
            util.showProgressDialog(this, "fetching asset details,Please Wait..............");
            ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
            Call<ArrayList<AssetType>> call = apiInterface.getAssetTypes();
            call.enqueue(new Callback<ArrayList<AssetType>>() {
                @Override
                public void onResponse(Call<ArrayList<AssetType>> call, Response<ArrayList<AssetType>> response) {
                    try {
                        if (response.code() == 200 || response.code() == 201) {
                            AssetType assetType = new AssetType();
                            assetType.setName("Select Asset Type");
                            response.body().add(0, assetType);

                            appPreferences.setAssetType(response.body());

                            try {
                                if (appPreferences.getAssetType() != null) {
                                    spAssetType.setVisibility(View.VISIBLE);
                                    assetTypeAdapter = new AssetTypeAdapter(AssetManagementActivity.this, response.body());
                                    spAssetType.setAdapter(assetTypeAdapter);
                                    assetTypeAdapter.notifyDataSetChanged();

                                    spAssetType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            pos = position;
                                            isAssetPathSaved = true;
                                            dbHelper.deleteAllAssetData();
                                            btStart.setBackgroundResource(R.color.menutoolbar);
                                            btStart.setEnabled(true);
                                            if (position > 0) {
                                                rlButton.setVisibility(View.VISIBLE);
                                                llAssetTypeLayout.setVisibility(View.VISIBLE);
                                                item = appPreferences.getAssetType().get(position).getName();
                                                itemDesc = appPreferences.getAssetType().get(position).getDescription();
                                                itemId = appPreferences.getAssetType().get(position).getId();
                                                drawAttributeLayout(appPreferences.getAssetType().get(position));
                                            } else {
                                                rlButton.setVisibility(View.GONE);
                                                llAssetTypeLayout.setVisibility(View.GONE);
                                            }

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {
                                        }
                                    });
                                } else {
                                    spAssetType.setVisibility(View.GONE);
                                }
                            } catch (Exception ignored) {
                            }
                        } else if (response.code() == 401) {
                            util.callLoginScreen(AssetManagementActivity.this);
                            finish();
                        } else if (response.code() == 403)
                        {
                            util.showToast(getApplicationContext(),"* 403 Forbidden");
                        } else
                        {
                            util.showToast(getApplicationContext(),"* Something bad happened");

                        }
                        util.hideProgressDialog();
                    } catch (Exception e) {
                        util.hideProgressDialog();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<AssetType>> call, Throwable t) {
                    util.hideProgressDialog();
                }
            });
        }
    }

    @SuppressWarnings("deprecation")
    private void drawAttributeLayout(AssetType assetType) {
        llAssetTypeLayout.removeAllViews();
        if (assetType.getAssetTypeAttributes() != null
                && assetType.getAssetTypeAttributes().size() > 0) {
            allEds = new ArrayList<>();
            allEds.clear();
            LinearLayout.LayoutParams mRparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams mRparams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            EditText myEditText = null;
            TextView myText = null;
            if (assetType.getLayout().equals("SPREAD")) {
                btPin.setVisibility(View.GONE);
                llSpread.setVisibility(View.VISIBLE);
            } else {
                btPin.setVisibility(View.VISIBLE);
                llSpread.setVisibility(View.GONE);

            }
            for (int i = 0; i < assetType.getAssetTypeAttributes().size(); i++) {

                myText = new TextView(this);
                mRparams.setMargins(10, 10, 10, 10);
                myText.setPadding(10, 10, 10, 10);
                myText.setLayoutParams(mRparams);
                myText.setText(assetType.getAssetTypeAttributes().get(i).getName());
                myText.setTextColor(getResources().getColor(R.color.menutoolbar));
                myText.setTypeface(null, Typeface.BOLD);

                myEditText = new EditText(this);
                mRparams1.setMargins(20, 2, 10, 30);
                myEditText.setPadding(30, 2, 15, 10);
                myEditText.setLayoutParams(mRparams1);
                myEditText.setTextSize(12.0f);
                myEditText.setHint(assetType.getAssetTypeAttributes().get(i).getName());
                myEditText.setId(i);
                myEditText.setTextColor(getResources().getColor(R.color.black));
                myEditText.setBackgroundResource(R.color.transparent);
                myEditText.setHintTextColor(getResources().getColor(R.color.back_menu));
                allEds.add(myEditText);
                llAssetTypeLayout.addView(myText);
                llAssetTypeLayout.addView(myEditText);
            }

        } else {
            util.makeAlertdiallog(this, "No attribute is available!");
        }
    }

    private void saveIssueIntoDataBase() {

        if (etName.getText().toString().length() == 0) {
            showError("Please Enter Asset Name");
        } else if (pos == 0) {
            showError("Select Asset Type");
        } else if (etName.getText().toString().length() > 0 && pos > 0) {
            Asset assetRequest = new Asset();
            assetRequest.setAssetType(appPreferences.getAssetType().get(pos));
            assetRequest.setAssetTypeId(appPreferences.getAssetType().get(pos).getId());

            ArrayList<AssetCoordinates> assetCoordinatesList = new ArrayList<>();
            AssetCoordinates assetCoordinates = new AssetCoordinates();
            assetCoordinates.setLatitude(latitude);
            assetCoordinates.setLongitude(longitude);
            if (latitude == 0 || longitude == 0) {
                util.makeAlertdiallog(this, "Unable to fetch location \n please try again later!");
                return;
            }
            assetCoordinatesList.add(assetCoordinates);
            assetRequest.setAssetCoordinates(assetCoordinatesList);

            assetRequest.setName(etName.getText().toString());
            assetRequest.setDescription(etDescription.getText().toString());
            assetRequest.setAssetTypeId(itemId);

            if (appPreferences.getAssetType() != null) {
                assetRequest.setAssetType(appPreferences.getAssetType().get(pos));
                ArrayList<AssetTypeAttributeValues> assetValue = new ArrayList<>();
                for (int i = 0; i < appPreferences.getAssetType().get(pos).getAssetTypeAttributes().size(); i++) {
                    AssetTypeAttributeValues assetTypeAttributeValues = new AssetTypeAttributeValues();
                    assetTypeAttributeValues.setAssetTypeAttribute(appPreferences.getAssetType().get(pos).getAssetTypeAttributes().get(i));
                    assetTypeAttributeValues.setAttributeValue(allEds.get(i).getText().toString());
                    assetValue.add(assetTypeAttributeValues);
                }
                assetRequest.setAssetTypeAttributeValues(assetValue);
            }

            util.showProgressDialog(this, "posting your request!!");
            ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
            Call<Void> call = apiInterface.postUserAsset(assetRequest);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    try {
                        btPin.setEnabled(true);
                        util.hideProgressDialog();
                        if (response.code() == 200 || response.code() == 201) {
                            util.makeAlertdiallog(AssetManagementActivity.this, "Asset created successfully!!");
                            Intent i = new Intent(AssetManagementActivity.this, AssetsActivity.class);
                            startActivity(i);
                        } else if (response.code() == 401) {
                            util.callLoginScreen(AssetManagementActivity.this);
                            finish();
                        } else if (response.code() == 403)
                        {
                            util.showToast(getApplicationContext(),"* 403 Forbidden");
                        } else
                        {
                            util.showToast(getApplicationContext(),"* Something bad happened");

                        }

                        AssetManagementActivity.this.finish();

                    } catch (Exception e) {
                        btPin.setEnabled(true);
                        util.hideProgressDialog();
                        showError("Exception Check : " + e.toString());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    showError("Something bad happened");
                }
            });
        } else {
            showError("Please Enter Asset Name");
        }
    }

    private void showError(String s) {
        util.hideProgressDialog();
        util.makeAlertdiallog(this, s);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        if (location.hasAccuracy()) {
            if (location.getAccuracy() < 15 && location.getSpeed() > 0.0) {
                if (!isAssetPathSaved) {
                    saveAssetPathinDataBase(location);
                }
            }
        }
    }

    private void saveAssetPathinDataBase(Location location) {
        pathDetail = new AssetCoordinates();
        pathDetail.setLatitude((location.getLatitude()));
        pathDetail.setLongitude((location.getLongitude()));
        dbHelper.insertAssetCoordinates(pathDetail);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private void postAssetPath(final ArrayList<AssetCoordinates> assetCoordinatesList) {
        if (etName.getText().toString().length() > 0) {
            Asset assetRequest = new Asset();
            assetRequest.setAssetType(appPreferences.getAssetType().get(pos));
            assetRequest.setAssetTypeId(appPreferences.getAssetType().get(pos).getId());

            assetRequest.setAssetCoordinates(assetCoordinatesList);

            assetRequest.setName(etName.getText().toString());
            assetRequest.setDescription(etDescription.getText().toString());
            assetRequest.setAssetTypeId(itemId);

            if (appPreferences.getAssetType() != null) {
                assetRequest.setAssetType(appPreferences.getAssetType().get(pos));
                ArrayList<AssetTypeAttributeValues> assetValue = new ArrayList<>();
                for (int i = 0; i < appPreferences.getAssetType().get(pos).getAssetTypeAttributes().size(); i++) {
                    AssetTypeAttributeValues assetTypeAttributeValues = new AssetTypeAttributeValues();
                    assetTypeAttributeValues.setAssetTypeAttribute(appPreferences.getAssetType().get(pos).getAssetTypeAttributes().get(i));
                    assetTypeAttributeValues.setAttributeValue(allEds.get(i).getText().toString());
                    assetValue.add(assetTypeAttributeValues);
                }
                assetRequest.setAssetTypeAttributeValues(assetValue);
            }

            util.showProgressDialog(this, "posting your request!!");
            ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
            Call<Void> call = apiInterface.postUserAsset(assetRequest);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    try {
                        btPin.setEnabled(true);
                        util.hideProgressDialog();
                        if (response.code() == 200 || response.code() == 201) {
                            util.makeAlertdiallog(AssetManagementActivity.this, "Asset created successfully!!");
                            dbHelper.deleteAllAssetData();
                            Intent i = new Intent(AssetManagementActivity.this, AssetsActivity.class);
                            startActivity(i);
                        } else if (response.code() == 401) {
                            util.callLoginScreen(AssetManagementActivity.this);
                            finish();
                        } else if (response.code() == 403)
                        {
                            util.showToast(getApplicationContext(),"* 403 Forbidden");
                        } else
                        {
                            util.showToast(getApplicationContext(),"* Something bad happened");

                        }
                        AssetManagementActivity.this.finish();
                    } catch (Exception e) {
                        btPin.setEnabled(true);
                        util.hideProgressDialog();
                        showError("Exception Check : " + e.toString());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    showError("Something bad happened");
                }
            });
        } else {
            showError("Please Enter Asset Name");
        }
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

    private void initializeLocationManager() {
        String bestProvider = null;
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        lastConnectionStatus = appPreferences.getConnectionStatus();
        appPreferences.setConnectionStatus(isConnect);

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
