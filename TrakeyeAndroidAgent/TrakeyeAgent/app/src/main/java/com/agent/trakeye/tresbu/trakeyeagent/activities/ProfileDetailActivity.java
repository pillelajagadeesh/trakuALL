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
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.model.UserInfo;
import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiClient;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiInterface;
import com.agent.trakeye.tresbu.trakeyeagent.utils.util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tresbu on 25-Oct-16.
 */

public class ProfileDetailActivity extends Activity {
    Button btEdit;
    RelativeLayout rlBack,editLay;
    TextView tvHeader, tvFirstName, tvLastName, tvEmail;
    AppPreferences app;


    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private boolean internetConnected = true;
    private LinearLayout linearLay;
    private Object agentProfileMethod;


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        ProfileDetailActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profiledetail);
        app = new AppPreferences(this);
        if (!app.getSessionVal()) {
            finish();
        }
        tvHeader = (TextView) findViewById(R.id.tvHeader);
        tvFirstName = (TextView) findViewById(R.id.tvFirstName);
        tvLastName = (TextView) findViewById(R.id.tvLastName);


        linearLay = (LinearLayout) findViewById(R.id.linearLay);

        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvHeader = (TextView) findViewById(R.id.tvHeader);
        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        editLay = (RelativeLayout) findViewById(R.id.editLay);

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDetailActivity.this.finish();
            }
        });

        editLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileDetailActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        if (internetConnected)
        {
            getAgentProfileMethod();
        }
        else
        {
            util.showToast(ProfileDetailActivity.this, getString(R.string.please_check_your_internet_connectivity));
        }

    }

    private void getAgentProfileMethod()
    {
        if (internetConnected)
        {

        util.showProgressDialog(this, "updating your request!!");

            ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);

            Call<UserInfo> call = apiInterface.getUserProfile();
            call.enqueue(new Callback<UserInfo>()
            {
                @Override
                public void onResponse(Call<UserInfo> call, Response<UserInfo> response)
                {
                    try
                    {
                        util.hideProgressDialog();
                        if (response.code() == 200 || response.code() == 201)
                        {
                            app.setUserInfo(response.body());
                            updateUi();
                        }
                        else if (response.code() == 401)
                        {
                            util.callLoginScreen(ProfileDetailActivity.this);
                            finish();
                        } else if (response.code() == 403) {
                            util.showToast(getApplicationContext(), "* 403 Forbidden");
                        } else {
                            util.showToast(getApplicationContext(), "* Something bad happened");

                        }

                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<UserInfo> call, Throwable t) {
                    util.hideProgressDialog();
                    util.showToast(ProfileDetailActivity.this, "Something bad happend!!");
                }
            });
        }else{
            util.makeAlertdiallog(ProfileDetailActivity.this,getString(R.string.please_check_your_internet_connectivity));
        }
    }

    private void updateUi() {

        if (app.getUserInfo() != null)
        {
            tvHeader.setText("My Profile");
            if (app.getUserInfo().getFirstName() != null) {
                tvFirstName.setText(app.getUserInfo().getFirstName());
            }
            if (app.getUserInfo().getLastName() != null) {
                tvLastName.setText(app.getUserInfo().getLastName());
            }
            if (app.getUserInfo().getEmail() != null) {
                tvEmail.setText(app.getUserInfo().getEmail());
            }
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        updateUi();
    }

    /**
     * Method to register runtime broadcast receiver to show snackbar alert for internet connection..
     */

    @Override
    protected void onResume() {
        super.onResume();
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
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

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

    private void setSnackbarMessage(String status, boolean showBar)
    {
        int duration = 0;
        int color = 0;
        boolean isConnect = false, lastConnectionStatus = false;

        String internetStatus = "";
        if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {
            internetStatus = "Internet Connected";
            duration = Snackbar.LENGTH_LONG;
            isConnect = true;
            color = Color.WHITE;
            internetConnected = true;
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
                    .make(linearLay, internetStatus, duration)
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

            if (internetStatus.equalsIgnoreCase("Lost Internet Connection"))
            {
                if (internetConnected)
                {
                    snackbar.show();
                    internetConnected = false;
                }
            }
            else
            {
                if (!internetConnected)
                {
                    internetConnected = true;
                    snackbar.show();
                }
            }
        }


    }


}
