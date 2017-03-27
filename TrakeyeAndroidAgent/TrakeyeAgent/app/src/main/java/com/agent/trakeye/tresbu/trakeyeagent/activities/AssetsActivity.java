package com.agent.trakeye.tresbu.trakeyeagent.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.adapters.AssetAdapter;
import com.agent.trakeye.tresbu.trakeyeagent.interfaces.OnAssetItemClickListener;
import com.agent.trakeye.tresbu.trakeyeagent.model.Asset;
import com.agent.trakeye.tresbu.trakeyeagent.model.AssetCoordinates;
import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiClient;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiInterface;
import com.agent.trakeye.tresbu.trakeyeagent.utils.AppConstants;
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
import java.util.HashMap;
import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tresbu on 21-Oct-16.
 */

public class AssetsActivity extends FragmentActivity implements OnAssetItemClickListener, OnMapReadyCallback, View.OnClickListener {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    RelativeLayout listLayout;
    LinearLayout noListLayout;
    private boolean internetConnected = true;
    public static int currentPage = 0;
    public int TOTAL_LIST_ITEMS = 0;
    public int NUM_ITEMS_PAGE = 10;
    public static int TOTAL_PAGES = 0;

    RecyclerView rvVerticalList;
    LinearLayout lltabLayout, topHeader, llMap;
    ImageView btList, btMap;
    private ImageButton btnMyLocation;
    AssetAdapter adapter;
    TextView text, tvSelectedOperator;
    TextView tvFirst, tvPrevious, tvCurrent, tvNext, tvLast;
    RelativeLayout rlBack;
    AppPreferences app;
    SearchView svSearch;
    TextView status, name;

    private ArrayList<AssetCoordinates> assetCoordinates;
    private PolylineOptions polyLineOptions;
    private Bitmap bmp;
    private GoogleMap mMap;
    private HashMap hm;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllAssets(currentPage);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_assets);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        status = (TextView) findViewById(R.id.tvStatus);
        status.setText("Layout");
        name = (TextView) findViewById(R.id.tvDescription);
        name.setText("Name");
        rvVerticalList = (RecyclerView) findViewById(R.id.rvList);
        text = (TextView) findViewById(R.id.text);
        lltabLayout = (LinearLayout) findViewById(R.id.tabLayout);
        topHeader = (LinearLayout) findViewById(R.id.topheader);
        topHeader.setVisibility(View.VISIBLE);
        llMap = (LinearLayout) findViewById(R.id.llmap);
        llMap.setVisibility(View.GONE);
        btList = (ImageView) findViewById(R.id.btList);
        btMap = (ImageView) findViewById(R.id.btMap);
        btMap.setImageResource(R.drawable.map_button_uncheck);
        tvFirst = (TextView) findViewById(R.id.tvFirst);
        tvPrevious = (TextView) findViewById(R.id.tvPrev);
        tvCurrent = (TextView) findViewById(R.id.tvCurrent);
        tvNext = (TextView) findViewById(R.id.tvNext);
        tvLast = (TextView) findViewById(R.id.tvLast);
        btnMyLocation = (ImageButton) findViewById(R.id.btnMyLocation);
        noListLayout = (LinearLayout) findViewById(R.id.no_list_layout);
        listLayout = (RelativeLayout) findViewById(R.id.list_layout);
        noListLayout.setVisibility(View.GONE);
        listLayout.setVisibility(View.VISIBLE);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        tvFirst.setOnClickListener(this);
        tvPrevious.setOnClickListener(this);
        tvCurrent.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        tvLast.setOnClickListener(this);
        btList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnected) {
                    topHeader.setVisibility(View.VISIBLE);
                    rvVerticalList.setVisibility(View.VISIBLE);
                    llMap.setVisibility(View.GONE);
                    btList.setImageResource(R.drawable.list_button);
                    btMap.setImageResource(R.drawable.map_button_uncheck);
                }else{
                    util.makeAlertdiallog(AssetsActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });
        btMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnected) {
                    topHeader.setVisibility(View.GONE);
                    rvVerticalList.setVisibility(View.GONE);
                    llMap.setVisibility(View.VISIBLE);
                    btMap.setImageResource(R.drawable.map_button);
                    btList.setImageResource(R.drawable.list_button_uncheck);
                }else{
                    util.makeAlertdiallog(AssetsActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });

        svSearch = (SearchView) findViewById(R.id.svSearchCases);
        svSearch.setQueryHint("Search Here");
        svSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnected){
                    svSearch.onActionViewExpanded();
                }else{
                    util.makeAlertdiallog(AssetsActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (util.isShowingProgressDialog(getApplicationContext())) {
                    util.hideProgressDialog();
                }
                if (newText.length() > 2) {
                    try {
                        adapter.clearData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    currentPage = 0;
                    getAllAssetsSearch(newText, currentPage);
                } else if (newText.length() == 0) {
                    currentPage = 0;
                    try {
                        adapter.clearData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    getAllAssets(currentPage);
                }
                return false;
            }
        });

        tvSelectedOperator = (TextView) findViewById(R.id.tvSelectedOperator);
        tvSelectedOperator.setText(getResources().getString(R.string.assets));
        app = new AppPreferences(this);
        if(!app.getSessionVal()){
            finish();
        }
        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AssetsActivity.this.finish();
            }
        });
        LinearLayoutManager verticalLayoutmanager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvVerticalList.setLayoutManager(verticalLayoutmanager);
    }

    private void getAllAssets(int currentPage) {
        if (internetConnected) {
            util.showProgressDialog(this, "fetching cases,Please Wait..............");
            if (ApiClient.getToken_id() == null) {
                ApiClient.setToken_id(app.getToken());
            }
            tvCurrent.setText("" + (currentPage + 1));
            ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
            Call<ArrayList<Asset>> call = apiInterface.getAllAssets(currentPage, NUM_ITEMS_PAGE, AppConstants.queryParameterDesc);
            call.enqueue(new Callback<ArrayList<Asset>>() {
                @Override
                public void onResponse(Call<ArrayList<Asset>> call, Response<ArrayList<Asset>> response) {

                    try {

                        if (response.code() == 200 || response.code() == 201) {
                            try {
                                mMap.clear();
                                // get header value
                                Headers headers = response.headers();
                                for (int i = 0; i < headers.size(); i++) {
                                    String fieldName = headers.name(i);
                                    String fieldValue = headers.value(i);
//                                    System.out.println("header response fieldname is:" + fieldName + " and fieldvalue is: " + fieldValue);
                                    if (fieldName.equalsIgnoreCase("X-Total-Count")) {
                                        TOTAL_LIST_ITEMS = Integer.parseInt(fieldValue);
                                        int val = TOTAL_LIST_ITEMS % NUM_ITEMS_PAGE;
                                        val = val == 0 ? 0 : 1;
                                        TOTAL_PAGES = TOTAL_LIST_ITEMS / NUM_ITEMS_PAGE + val;
//                                        Log.d(ExecuteRequest.TAG, String.valueOf(TOTAL_PAGES));
//                                        drawButtons(TOTAL_PAGES);
                                    }
                                }
                            } catch (Exception e) {
                            }
                            setItemInAdapter(response.body());
                        } else if (response.code() == 401) {
                            util.callLoginScreen(AssetsActivity.this);
                            finish();
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
                public void onFailure(Call<ArrayList<Asset>> call, Throwable t) {
                    util.hideProgressDialog();
                    util.makeAlertdiallog(AssetsActivity.this, "Something went wrong.....");
                    AssetsActivity.this.finish();
                }
            });
        } else {
            util.makeAlertdiallog(this, "Please check your internet connectivity");
            this.finish();
        }
    }

    private void getAllAssetsSearch(String searchVal, int currentPage) {
        if (Network_Available.hasConnection(this)) {
            util.showProgressDialog(this, "fetching cases,Please Wait..............");
            if (ApiClient.getToken_id() == null) {
                ApiClient.setToken_id(app.getToken());
            }
            tvCurrent.setText("" + (currentPage + 1));
            String urlVal = "http://dev.trakeye.com/api/assets/searchvalue/" + searchVal;
            ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
            Call<ArrayList<Asset>> call = apiInterface.getAllAssetsSearch(urlVal, currentPage, NUM_ITEMS_PAGE, AppConstants.queryParameterDesc);
            call.enqueue(new Callback<ArrayList<Asset>>() {
                @Override
                public void onResponse(Call<ArrayList<Asset>> call, Response<ArrayList<Asset>> response) {

                    try {

                        if (response.code() == 200 || response.code() == 201) {
                            try {
                                mMap.clear();
                                // get header value
                                Headers headers = response.headers();
                                for (int i = 0; i < headers.size(); i++) {
                                    String fieldName = headers.name(i);
                                    String fieldValue = headers.value(i);
//                                    System.out.println("header response fieldname is:" + fieldName + " and fieldvalue is: " + fieldValue);
                                    if (fieldName.equalsIgnoreCase("X-Total-Count")) {
                                        TOTAL_LIST_ITEMS = Integer.parseInt(fieldValue);
                                        int val = TOTAL_LIST_ITEMS % NUM_ITEMS_PAGE;
                                        val = val == 0 ? 0 : 1;
                                        TOTAL_PAGES = TOTAL_LIST_ITEMS / NUM_ITEMS_PAGE + val;
//                                        Log.d(ExecuteRequest.TAG, String.valueOf(TOTAL_PAGES));
//                                        drawButtons(TOTAL_PAGES);
                                    }
                                }
                            } catch (Exception e) {
                            }
                            setItemInAdapter(response.body());
                        } else if (response.code() == 401) {
                            util.callLoginScreen(AssetsActivity.this);
                            finish();
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
                public void onFailure(Call<ArrayList<Asset>> call, Throwable t) {
                    util.hideProgressDialog();
                    util.makeAlertdiallog(AssetsActivity.this, "Something went wrong.....");
                    AssetsActivity.this.finish();
                }
            });
        } else {
            util.makeAlertdiallog(this, "Please check your internet connectivity");
            this.finish();
        }
    }

    private void setItemInAdapter(ArrayList<Asset> body)
    {
        if (body.size() > 0) {
            listLayout.setVisibility(View.VISIBLE);
            noListLayout.setVisibility(View.GONE);
            adapter = new AssetAdapter(AssetsActivity.this, body, this);
            rvVerticalList.setAdapter(adapter);
            adapter.setAllData(body);
            for (Asset i : body) {
                setItemonWidget(i);
            }
        } else {
            listLayout.setVisibility(View.GONE);
            noListLayout.setVisibility(View.VISIBLE);
            adapter = new AssetAdapter(AssetsActivity.this, body, this);
            rvVerticalList.setAdapter(adapter);
            adapter.setAllData(body);
        }
        if (util.isShowingProgressDialog(getApplicationContext())) {
            util.hideProgressDialog();
        }
    }

    @Override
    public void onAssetItemClick(Asset item) {
        if(internetConnected) {
            Intent intent = new Intent(this, AssetDetailActivity.class);
            intent.putExtra("assets", item);
            startActivity(intent);
        }else{
            util.makeAlertdiallog(AssetsActivity.this,getString(R.string.please_check_your_internet_connectivity));
        }
    }

    // Method to get Assets using Asset Id
    private void calltoGetDetail(String mAssetId) {

        if (Network_Available.hasConnection(this)) {
            if (mAssetId != null) {


                if (ApiClient.getToken_id() == null) {
                    ApiClient.setToken_id(app.getToken());
                }

                ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
                Call<Asset> call = apiInterface.getAssetDetail(mAssetId);
                call.enqueue(new Callback<Asset>() {
                    @Override
                    public void onResponse(Call<Asset> call, Response<Asset> response) {

                        try {
                            if (response.code() == 200 || response.code() == 201) {
                                if (response.body() != null) {
                                    setItemonWidget(response.body());
                                }
                                util.hideProgressDialog();
                            } else if (response.code() == 401) {
                                util.callLoginScreen(AssetsActivity.this);
                                finish();
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
                    public void onFailure(Call<Asset> call, Throwable t) {
                        util.hideProgressDialog();

                    }
                });
            }
        } else {
            util.makeAlertdiallog(this, "Please check your internet connectivity");
        }
    }

    @SuppressWarnings("deprecation")
    public void setItemonWidget(Asset mAsset) {
        util.hideProgressDialog();
        if (mAsset != null) {
            if (mAsset.getAssetCoordinates() != null) {
                assetCoordinates = mAsset.getAssetCoordinates();
                List<LatLng> latlng = new ArrayList<>();
                if (assetCoordinates != null) {
                    if (assetCoordinates.size() > 0) {
                        if (mAsset.getAssetType().getLayout().equals("SPREAD")) {
                            for (int i = 0; i < assetCoordinates.size(); i++) {
                                latlng.add(i, new LatLng(assetCoordinates.get(i).getLatitude(), assetCoordinates.get(i).getLongitude()));
                            }
                            polyLineOptions = new PolylineOptions();
                            polyLineOptions.addAll(latlng);
                            polyLineOptions.width(6);

                            switch (mAsset.getAssetType().getColorcode()) {
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

                            // Zoom Map Camera to first LatLng
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(assetCoordinates.get(0).getLatitude(), assetCoordinates.get(0).getLongitude())));
                            mMap.getUiSettings().setZoomControlsEnabled(true);
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(17f));
                        } else {
                            if (mAsset.getAssetType().getImage() != null) {
                                // To place image on all coordinates of "Fixed" layout
                                for (int i = 0; i < assetCoordinates.size(); i++) {
                                    mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(assetCoordinates.get(i).getLatitude(), assetCoordinates.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(getResizedBitmap(decodeBase64(mAsset.getAssetType().getImage()),150))));

                                }
                                // Zoom Map Camera to first LatLng
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(assetCoordinates.get(0).getLatitude(), assetCoordinates.get(0).getLongitude())));
                                mMap.getUiSettings().setZoomControlsEnabled(true);
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(11.0f));
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
                                        mMap.animateCamera(CameraUpdateFactory.zoomTo(11.0f));
                                    }
                                }else{
                                    util.makeAlertdiallog(AssetsActivity.this,getString(R.string.please_check_your_internet_connectivity));
                                }
                            }
                        });
                    } else {
//                        util.showToast(this, "No Point Are Available");
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(internetConnected) {
            // Perform action on click
            switch (v.getId()) {
                case R.id.tvFirst:
                    if (svSearch.getQuery().toString().equals("")) {
                        currentPage = 0;
                        getAllAssets(0);
                    } else if (svSearch.getQuery().length() > 2) {
                        currentPage = 0;
                        getAllAssetsSearch(svSearch.getQuery().toString(), 0);
                    }
                    break;
                case R.id.tvPrev:
                    if (svSearch.getQuery().toString().equals("")) {
                        if (currentPage > 0) {
                            currentPage--;
                            getAllAssets(currentPage);
                        }
                    } else if (svSearch.getQuery().length() > 2) {
                        if (currentPage > 0) {
                            currentPage--;
                            getAllAssetsSearch(svSearch.getQuery().toString(), currentPage);
                        }
                    }
                    break;
                case R.id.tvNext:
                    if (svSearch.getQuery().toString().equals("")) {
                        if (currentPage < (TOTAL_PAGES - 1)) {
                            currentPage++;
                            getAllAssets(currentPage);
                        }
                    } else if (svSearch.getQuery().length() > 2) {
                        if (currentPage < (TOTAL_PAGES - 1)) {
                            currentPage++;
                            getAllAssetsSearch(svSearch.getQuery().toString(), currentPage);
                        }
                    }
                    break;
                case R.id.tvLast:
                    if (svSearch.getQuery().toString().equals("")) {
                        currentPage = (TOTAL_PAGES - 1);
                        getAllAssets(TOTAL_PAGES - 1);
                    } else if (svSearch.getQuery().length() > 2) {
                        currentPage = (TOTAL_PAGES - 1);
                        getAllAssetsSearch(svSearch.getQuery().toString(), TOTAL_PAGES - 1);
                    }
                    break;
            }
        }else{
            util.makeAlertdiallog(AssetsActivity.this,getString(R.string.please_check_your_internet_connectivity));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
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