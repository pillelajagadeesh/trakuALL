package com.agent.trakeye.tresbu.trakeyeagent.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.adapters.ServiceAdapter;
import com.agent.trakeye.tresbu.trakeyeagent.interfaces.OnServiceItemClickListener;
import com.agent.trakeye.tresbu.trakeyeagent.model.Service;
import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiClient;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiInterface;
import com.agent.trakeye.tresbu.trakeyeagent.utils.AppConstants;
import com.agent.trakeye.tresbu.trakeyeagent.utils.Network_Available;
import com.agent.trakeye.tresbu.trakeyeagent.utils.util;

import java.util.ArrayList;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tresbu on 21-Oct-16.
 */

public class ServiceActivity extends Activity implements OnServiceItemClickListener, View.OnClickListener {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private boolean internetConnected = true;

    RecyclerView rvVerticalList;
    ServiceAdapter adapter;
    TextView text, tvSelectedOperator, tvStatus;
    RelativeLayout rlBack;
    AppPreferences app;
    TextView tvFirst, tvPrevious, tvCurrent, tvNext, tvLast;
    public int TOTAL_LIST_ITEMS = 0;
    public int NUM_ITEMS_PAGE = 20;
    public static int TOTAL_PAGES = 0;
    public static int currentPage = 0;
    private LinearLayout mLinearScroll;
    SearchView svSearch;

    @Override
    public void onBackPressed() {
        this.finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(internetConnected) {
            getAllServices(currentPage);
        }else{
            util.makeAlertdiallog(ServiceActivity.this,getString(R.string.please_check_your_internet_connectivity));
        }
        registerInternetCheckReceiver();
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_issues);
        rvVerticalList = (RecyclerView) findViewById(R.id.rvList);
        mLinearScroll = (LinearLayout) findViewById(R.id.linear_scroll);
        text = (TextView) findViewById(R.id.text);
        tvSelectedOperator = (TextView) findViewById(R.id.tvSelectedOperator);
        tvSelectedOperator.setText(getResources().getString(R.string.services));
        tvFirst = (TextView) findViewById(R.id.tvFirst);
        tvPrevious = (TextView) findViewById(R.id.tvPrev);
        tvCurrent = (TextView) findViewById(R.id.tvCurrent);
        tvNext = (TextView) findViewById(R.id.tvNext);
        tvLast = (TextView) findViewById(R.id.tvLast);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvStatus.setText("Status");
        tvStatus.setVisibility(View.GONE);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        tvFirst.setOnClickListener(this);
        tvPrevious.setOnClickListener(this);
        tvCurrent.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        tvLast.setOnClickListener(this);

        app = new AppPreferences(this);
        if(!app.getSessionVal()){
            finish();
        }
        svSearch = (SearchView) findViewById(R.id.svSearchCases);
        svSearch.setQueryHint("Search Here");
        svSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnected) {
                    svSearch.onActionViewExpanded();
                }else{
                    util.makeAlertdiallog(ServiceActivity.this,getString(R.string.please_check_your_internet_connectivity));
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
                    getAllServiceSearch(newText, currentPage);
                } else if (newText.length() == 0) {
                    currentPage = 0;
                    try {
                        adapter.clearData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    getAllServices(currentPage);
                }
                return false;
            }
        });

        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceActivity.this.finish();
            }
        });
        LinearLayoutManager verticalLayoutmanager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvVerticalList.setLayoutManager(verticalLayoutmanager);


    }

    private void setItemInAdapter(ArrayList<Service> body)
    {
        if (body.size() > 0) {
            adapter = new ServiceAdapter(ServiceActivity.this, body, this);
            text.setVisibility(View.GONE);
            rvVerticalList.setAdapter(adapter);
            adapter.setAllData(body);
        } else {
            text.setVisibility(View.VISIBLE);
            text.setText("No Services are available");
            adapter = new ServiceAdapter(ServiceActivity.this, body, this);
            rvVerticalList.setAdapter(adapter);
            adapter.setAllData(body);
        }
        util.hideProgressDialog();
    }


    @Override
    public void onServiceItemClick(Service item) {
        if(internetConnected){
        Intent intent = new Intent(this, ServiceDetailActivity.class);
        intent.putExtra("service", item);
        startActivity(intent);
        }else{
            util.makeAlertdiallog(ServiceActivity.this,getString(R.string.please_check_your_internet_connectivity));
        }
    }

    @Override
    public void onClick(View v) {
        if(internetConnected){
        // Perform action on click
        switch (v.getId()) {
            case R.id.tvFirst:
                if (svSearch.getQuery().toString().equals("")) {
                    currentPage = 0;
                    getAllServices(0);
                } else if (svSearch.getQuery().length() > 2) {
                    currentPage = 0;
                    getAllServiceSearch(svSearch.getQuery().toString(), 0);
                }
                break;
            case R.id.tvPrev:
                if (svSearch.getQuery().toString().equals("")) {
                    if (currentPage > 0) {
                        currentPage--;
                        getAllServices(currentPage);
                    }
                } else if (svSearch.getQuery().length() > 2) {
                    if (currentPage > 0) {
                        currentPage--;
                        getAllServiceSearch(svSearch.getQuery().toString(), currentPage);
                    }
                }
                break;
            case R.id.tvNext:
                if (svSearch.getQuery().toString().equals("")) {
                    if (currentPage < (TOTAL_PAGES - 1)) {
                        currentPage++;
                        getAllServices(currentPage);
                    }
                } else if (svSearch.getQuery().length() > 2) {
                    if (currentPage < (TOTAL_PAGES - 1)) {
                        currentPage++;
                        getAllServiceSearch(svSearch.getQuery().toString(), currentPage);
                    }
                }
                break;
            case R.id.tvLast:
                if (svSearch.getQuery().toString().equals("")) {
                    currentPage = (TOTAL_PAGES - 1);
                    getAllServices(TOTAL_PAGES - 1);
                } else if (svSearch.getQuery().length() > 2) {
                    currentPage = (TOTAL_PAGES - 1);
                    getAllServiceSearch(svSearch.getQuery().toString(), TOTAL_PAGES - 1);
                }
                break;
        }
        }else{
            util.makeAlertdiallog(ServiceActivity.this,getString(R.string.please_check_your_internet_connectivity));
        }
    }

    private void getAllServices(int currentPage) {
        if (internetConnected) {
            if (ApiClient.getToken_id() == null) {
                ApiClient.setToken_id(app.getToken());
            }
            tvCurrent.setText("" + (currentPage + 1));
            util.showProgressDialog(this, "fetching services,Please Wait..............");
            ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
            Call<ArrayList<Service>> call = apiInterface.getAllServices(currentPage, NUM_ITEMS_PAGE, AppConstants.queryParameterDesc);
            call.enqueue(new Callback<ArrayList<Service>>() {
                @Override
                public void onResponse(Call<ArrayList<Service>> call, Response<ArrayList<Service>> response) {

                    try {
                        try {
                            if (response.code() == 200 || response.code() == 201) {
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
                                    setItemInAdapter(response.body());
                                    util.hideProgressDialog();
                                }
                            } else if (response.code() == 401) {
                                util.callLoginScreen(ServiceActivity.this);
                                finish();
                            } else if (response.code() == 403)
                            {
                                util.showToast(getApplicationContext(),"* 403 Forbidden");
                            } else
                            {
                                util.showToast(getApplicationContext(),"* Something bad happened");

                            }
                        } catch (Exception e) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<Service>> call, Throwable t) {
                    util.hideProgressDialog();
                    util.makeAlertdiallog(ServiceActivity.this, "Something went wrong.....");
                    ServiceActivity.this.finish();
                }
            });
        } else {
            util.makeAlertdiallog(this, "Please check your internet connectivity");
            this.finish();
        }
    }

    private void getAllServiceSearch(String searchVal, int currentPage) {
        if (Network_Available.hasConnection(this)) {
            util.showProgressDialog(this, "fetching cases,Please Wait..............");
            if (ApiClient.getToken_id() == null) {
                ApiClient.setToken_id(app.getToken());
            }
            tvCurrent.setText("" + (currentPage + 1));
            String urlVal = "http://dev.trakeye.com/api/tr-services/searchvalue/" + searchVal;
            ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
            Call<ArrayList<Service>> call = apiInterface.getAllServicesSearch(urlVal, currentPage, NUM_ITEMS_PAGE, AppConstants.queryParameterDesc);
            call.enqueue(new Callback<ArrayList<Service>>() {
                @Override
                public void onResponse(Call<ArrayList<Service>> call, Response<ArrayList<Service>> response) {

                    try {

                        if (response.code() == 200 || response.code() == 201) {
                            try {
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
                            util.callLoginScreen(ServiceActivity.this);
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
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<Service>> call, Throwable t) {
                    util.hideProgressDialog();
                    util.makeAlertdiallog(ServiceActivity.this, "Something went wrong.....");
                    ServiceActivity.this.finish();
                }
            });
        } else {
            util.makeAlertdiallog(this, "Please check your internet connectivity");
            this.finish();
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