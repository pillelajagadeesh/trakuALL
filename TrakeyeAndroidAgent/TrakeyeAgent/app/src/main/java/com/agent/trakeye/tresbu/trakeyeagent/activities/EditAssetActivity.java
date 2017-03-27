package com.agent.trakeye.tresbu.trakeyeagent.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.database.UserDBHelper;
import com.agent.trakeye.tresbu.trakeyeagent.model.Asset;
import com.agent.trakeye.tresbu.trakeyeagent.model.AssetCoordinates;
import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiClient;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiInterface;
import com.agent.trakeye.tresbu.trakeyeagent.utils.Network_Available;
import com.agent.trakeye.tresbu.trakeyeagent.utils.util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tresbu on 25-Oct-16.
 */

public class EditAssetActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback {


    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private boolean internetConnected = true;
    Asset mAssets;
    RelativeLayout rlBack;
    LinearLayout llAssetTypeLayout;
    EditText etName, etDescription;
    Button btStart, btSave, btPin;

    // flag for GPS status
    boolean isGPSEnabled = false;
    Location mLocation = null;
    private LocationManager mLocationManager = null;
    private boolean isAssetPathSaved = true;
    double latitude, longitude;
    LinearLayout llFixed, llSpread;
    private ImageButton btnMyLocation;

    UserDBHelper dbHelper;
    AppPreferences appPreferences;
    private GoogleMap mMap;
    AssetCoordinates pathDetail;
    private ArrayList<AssetCoordinates> assetCoordinates;
    private PolylineOptions polyLineOptions;
    private Bitmap bmp;

    @Override
    public void onBackPressed() {
        this.finish();
        isAssetPathSaved = true;
        dbHelper.deleteAllAssetData();
        EditAssetActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editasset);
        initializeLocationManager();
        appPreferences = new AppPreferences(this);
        if(!appPreferences.getSessionVal()){
            finish();
        }
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        dbHelper = new UserDBHelper(this);
        etName = (EditText) findViewById(R.id.et_name);
        etDescription = (EditText) findViewById(R.id.et_desc);
        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        btSave = (Button) findViewById(R.id.bt_save);
        btStart = (Button) findViewById(R.id.bt_start);
        btPin = (Button) findViewById(R.id.bt_pin);
        llFixed = (LinearLayout) findViewById(R.id.llFixed);
        llSpread = (LinearLayout) findViewById(R.id.llSpread);
        llAssetTypeLayout = (LinearLayout) findViewById(R.id.llAssetTypeLayout);
        btnMyLocation = (ImageButton) findViewById(R.id.btnMyLocation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditAssetActivity.this.finish();
            }
        });
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnected){
                isAssetPathSaved = true;
//                Log.d(ExecuteRequest.TAG, "Asset recode number: " + dbHelper.numberOfAsset());
                if (dbHelper.numberOfAsset() > 0) {
                    updateAssetPath(dbHelper.getAllAssetPath());
                } else {
                    showError("No Asset Path is Available : " + dbHelper.numberOfAsset());
                }
                }else{
                    util.makeAlertdiallog(EditAssetActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internetConnected) {
                    isAssetPathSaved = false;
                    btStart.setBackgroundResource(R.color.lightgray);
                    btStart.setEnabled(false);
                    util.makeAlertdiallog(EditAssetActivity.this, "Asset Path Started");
                }else{
                    util.makeAlertdiallog(EditAssetActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });
        btPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internetConnected) {
                    saveIssueIntoDataBase();
                }else{
                    util.makeAlertdiallog(EditAssetActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });

        mAssets = (Asset) getIntent().getSerializableExtra("assets");
        if (mAssets != null) {

            if (mAssets.getName() != null) {
                etName.setText(mAssets.getName());
            }
            if (mAssets.getDescription() != null) {
                etDescription.setText(mAssets.getDescription());
            }


        }

        if (mAssets.getAssetType() != null) {
            if (mAssets.getAssetType().getLayout().equals("SPREAD")) {
                llFixed.setVisibility(View.GONE);
                llSpread.setVisibility(View.VISIBLE);
            } else {
                llFixed.setVisibility(View.VISIBLE);
                llSpread.setVisibility(View.GONE);

            }
        } else {
            llFixed.setVisibility(View.GONE);
            llSpread.setVisibility(View.GONE);
        }


        try {
            // Get high accuracy provider
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            LocationProvider high =
                    mLocationManager.getProvider(mLocationManager.getBestProvider(criteria, true));
            isGPSEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (mLocation == null) {
                mLocationManager.requestLocationUpdates(
                        high.getName(), 5000, 5, this);
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

    private void updateAssetPath(final ArrayList<AssetCoordinates> assetCoordinatesList) {
        if (etName.getText().toString().length() > 0) {
            btStart.setEnabled(false);

            Asset assetRequest = new Asset();
            assetRequest.setId(mAssets.getId() + "");
            assetRequest.setAssetCoordinates(assetCoordinatesList);
            assetRequest.setAssetType(mAssets.getAssetType());
            assetRequest.setAssetTypeAttributeValues(mAssets.getAssetTypeAttributeValues());
            assetRequest.setName(etName.getText().toString());
            assetRequest.setDescription(etDescription.getText().toString());

//            if (appPreferences.getAssetType() != null) {
//                assetRequest.setAssetType(appPreferences.getAssetType().get(pos));
//                ArrayList<AssetTypeAttributeValues> assetValue = new ArrayList<>();
//                for (int i = 0; i < appPreferences.getAssetType().get(pos).getAssetTypeAttributes().size(); i++) {
//                    AssetTypeAttributeValues assetTypeAttributeValues = new AssetTypeAttributeValues();
//                    assetTypeAttributeValues.setAssetTypeAttribute(appPreferences.getAssetType().get(pos).getAssetTypeAttributes().get(i));
//                    assetTypeAttributeValues.setAttributeValue(allEds.get(i).getText().toString());
//                    assetValue.add(assetTypeAttributeValues);
//
//                }
//                assetRequest.setAssetTypeAttributeValues(assetValue);
//            }

            util.showProgressDialog(this, "posting your request!!");
            ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
            Call<Void> call = apiInterface.updateUserAsset(assetRequest);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    try {
                        btPin.setEnabled(true);
                        util.hideProgressDialog();
                        if (response.code() == 200 || response.code() == 201) {
                            util.makeAlertdiallog(EditAssetActivity.this, "Asset created successfully!!");
                            dbHelper.deleteAllAssetData();
                            EditAssetActivity.this.finish();
                        } else if (response.code() == 401) {
                            util.callLoginScreen(EditAssetActivity.this);
                            finish();
                        } else if (response.code() == 403)
                        {
                            util.showToast(getApplicationContext(),"* 403 Forbidden");
                        } else
                        {
                            util.showToast(getApplicationContext(),"* Something bad happened");

                        }
                        EditAssetActivity.this.finish();
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
            showError("Name cannot be empty!!");
        }
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

    private void initializeLocationManager() {
        String bestProvider = null;
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        }
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

    private void saveAssetPathinDataBase(Location location) {
        pathDetail = new AssetCoordinates();
        pathDetail.setLatitude((location.getLatitude()));
        pathDetail.setLongitude((location.getLongitude()));
        dbHelper.insertAssetCoordinates(pathDetail);
    }

    private void saveIssueIntoDataBase() {
        if (etName.getText().toString().length() > 0) {
            btStart.setEnabled(false);
            ArrayList<AssetCoordinates> assetCoordinatesList = new ArrayList<>();
            AssetCoordinates assetCoordinates = new AssetCoordinates();
            assetCoordinates.setLatitude(latitude);
            assetCoordinates.setLongitude(longitude);
            if(latitude == 0 || longitude == 0){
                Toast.makeText(this, "Unable to fetch location \n please try again later!", Toast.LENGTH_SHORT).show();
                return;
            }
            assetCoordinatesList.add(assetCoordinates);
            Asset assetRequest = new Asset();
            assetRequest.setId(mAssets.getId() + "");
            assetRequest.setAssetCoordinates(assetCoordinatesList);
            assetRequest.setAssetType(mAssets.getAssetType());
            assetRequest.setAssetTypeAttributeValues(mAssets.getAssetTypeAttributeValues());
            assetRequest.setName(etName.getText().toString());
            assetRequest.setDescription(etDescription.getText().toString());
            util.showProgressDialog(this, "posting your request!!");
            ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
            Call<Void> call = apiInterface.updateUserAsset(assetRequest);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    try {
                        btPin.setEnabled(true);
                        util.hideProgressDialog();
                        if (response.code() == 200 || response.code() == 201) {
                            util.makeAlertdiallog(EditAssetActivity.this, "Asset created successfully!!");
                            EditAssetActivity.this.finish();
                        } else if (response.code() == 401) {
                            util.callLoginScreen(EditAssetActivity.this);
                            finish();
                        } else if (response.code() == 403)
                        {
                            util.showToast(getApplicationContext(),"* 403 Forbidden");
                        } else
                        {
                            util.showToast(getApplicationContext(),"* Something bad happened");

                        }

                        EditAssetActivity.this.finish();

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
            showError("Name cannot be empty!!");
        }
    }

    private void showError(String s) {
        util.hideProgressDialog();
        btStart.setEnabled(true);
        util.makeAlertdiallog(this, s);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mAssets.getAssetCoordinates() != null) {
            assetCoordinates = mAssets.getAssetCoordinates();
            List<LatLng> latlng = new ArrayList<>();
            if (assetCoordinates != null) {
                if (assetCoordinates.size() > 0) {
                    if (mAssets.getAssetType().getLayout().equals("SPREAD")) {
                        for (int i = 0; i < assetCoordinates.size(); i++) {
                            latlng.add(i, new LatLng(assetCoordinates.get(i).getLatitude(), assetCoordinates.get(i).getLongitude()));
                        }
                        polyLineOptions = new PolylineOptions();
                        polyLineOptions.addAll(latlng);
                        polyLineOptions.width(6);

                        switch (mAssets.getAssetType().getColorcode()) {
                            case "CYAN":
                                polyLineOptions.color(getResources().getColor(R.color.CYAN));
                                break;
                            case "BLACK":
                                polyLineOptions.color(getResources().getColor(R.color.BLACK));
                                break;
                            case "BLUE":
                                polyLineOptions.color(getResources().getColor(R.color.BLUE));
                                break;
                            case "BLUEVIOLET":
                                polyLineOptions.color(getResources().getColor(R.color.BLUEVIOLET));
                                break;
                            case "BROWN":
                                polyLineOptions.color(getResources().getColor(R.color.BROWN));
                                break;
                            case "CHARTREUSE":
                                polyLineOptions.color(getResources().getColor(R.color.CHARTREUSE));
                                break;
                            case "CRIMSON":
                                polyLineOptions.color(getResources().getColor(R.color.CRIMSON));
                                break;
                            case "YELLOW":
                                polyLineOptions.color(getResources().getColor(R.color.YELLOW));
                                break;
                            case "MAGENTA":
                                polyLineOptions.color(getResources().getColor(R.color.MAGENTA));
                                break;
                            case "DEEPPINK":
                                polyLineOptions.color(getResources().getColor(R.color.DEEPPINK));
                                break;
                            case "LIGHTCORAL":
                                polyLineOptions.color(getResources().getColor(R.color.LIGHTCORAL));
                                break;
                            default:
                                polyLineOptions.color(Color.BLUE);
                                break;
                        }
                        mMap.addPolyline(polyLineOptions);

                        // Add a marker in Sydney and move the camera
                        LatLng latLngVal = new LatLng(assetCoordinates.get(0).getLatitude(), assetCoordinates.get(0).getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngVal));
                        mMap.getUiSettings().setZoomControlsEnabled(true);
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
                    } else {
                        if (mAssets.getAssetType().getImage() != null) {
                            // To place image on all coordinates of "Fixed" layout
                            for (int i = 0; i < assetCoordinates.size(); i++) {
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(assetCoordinates.get(i).getLatitude(), assetCoordinates.get(i).getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(getResizedBitmap(decodeBase64(mAssets.getAssetType().getImage()), 150))));
                            }
                            // Zoom Map Camera to first LatLng
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(assetCoordinates.get(0).getLatitude(), assetCoordinates.get(0).getLongitude())));
                            mMap.getUiSettings().setZoomControlsEnabled(true);
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
                        }
                    }
                    btnMyLocation.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if(internetConnected) {
                                if (assetCoordinates.get(0).getLatitude() > 0.0) {
                                    // Zoom Map Camera to first LatLng
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(assetCoordinates.get(0).getLatitude(), assetCoordinates.get(0).getLongitude())));
                                    mMap.getUiSettings().setZoomControlsEnabled(true);
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
                                }
                            }else{
                                util.makeAlertdiallog(EditAssetActivity.this,getString(R.string.please_check_your_internet_connectivity));
                            }
                        }
                    });
                } else {
                    util.makeAlertdiallog(this, "No Point Are Available");
                }
            }
        }
    }

    // This method is to resize the Bitmap Image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    // This method is to decode Base64Image to BitmapImage
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
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


