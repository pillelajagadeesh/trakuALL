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
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.utils.util;

/**
 * Created by Tresbu on 15-Oct-16.
 */

public class MenuActivity extends Activity {

    ImageView ivCreateCase, ivMyCase, ivCreateAsset, ivMyAsset, ivNotification, ivService, ivHelp, ivProfile;
    RelativeLayout rlBack;
    AppPreferences app;

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private boolean internetConnected = true;
    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_options_dialog);
        ivCreateCase = (ImageView) findViewById(R.id.iv_create_case);
        ivMyCase = (ImageView) findViewById(R.id.iv_all_issues);
        ivCreateAsset = (ImageView) findViewById(R.id.iv_create_asset);
        ivMyAsset = (ImageView) findViewById(R.id.iv_my_assets);
        ivNotification = (ImageView) findViewById(R.id.iv_notification);
        ivService = (ImageView) findViewById(R.id.iv_service);
        ivHelp = (ImageView) findViewById(R.id.iv_help);
        ivProfile = (ImageView) findViewById(R.id.iv_profile);
        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        app = new AppPreferences(this);

        if(!app.getSessionVal()){
            finish();
        }
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuActivity.this.finish();
            }
        });

        ivMyCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnected) {
                    startActivity(new Intent(getApplicationContext(), CasesActivity.class));
                }else{
                    util.makeAlertdiallog(MenuActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });

        ivService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnected) {
                    startActivity(new Intent(getApplicationContext(), ServiceActivity.class));
                }else{
                    util.makeAlertdiallog(MenuActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });

        ivCreateAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnected){
                    startActivity(new Intent(getApplicationContext(), AssetManagementActivity.class));
                }else{
                    util.makeAlertdiallog(MenuActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });

        ivMyAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnected){
                    startActivity(new Intent(getApplicationContext(), AssetsActivity.class));
                }else{
                    util.makeAlertdiallog(MenuActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });

        ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnected){
                    startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                }else{
                    util.makeAlertdiallog(MenuActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnected){
                    startActivity(new Intent(getApplicationContext(), ProfileDetailActivity.class));
                }else{
                    util.makeAlertdiallog(MenuActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }

/*
                stopService(new Intent(getBaseContext(), ExecuteRequest.class));
                app.setToken("");
                app.setName("");
                app.setPassword("");
                app.setServername("");
                app.setUsername("");
                Intent intent= new Intent(MenuActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
*/


            }
        });


        ivCreateCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internetConnected){
                    startActivity(new Intent(getApplicationContext(), CreateCaseActivity.class));
                }else{
                    util.makeAlertdiallog(MenuActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });


        ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnected){
                    util.makeAlertdiallog(MenuActivity.this, "This feature is not available currently!!");
                }else{
                    util.makeAlertdiallog(MenuActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
                //startActivity(new Intent(getApplicationContext(), SendPicActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();

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
