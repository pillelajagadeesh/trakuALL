package com.agent.trakeye.tresbu.trakeyeagent.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.model.Login;
import com.agent.trakeye.tresbu.trakeyeagent.model.LoginResponse;
import com.agent.trakeye.tresbu.trakeyeagent.model.UserInfo;
import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiClient;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiInterface;
import com.agent.trakeye.tresbu.trakeyeagent.utils.Network_Available;
import com.agent.trakeye.tresbu.trakeyeagent.utils.util;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sharmaan on 13-04-2016.
 */

public class LoginActivity extends Activity implements View.OnClickListener {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private boolean internetConnected = true;

    EditText etEmailId, etPassword;
    Button btSignIn, btSignUp;
    AppPreferences app;
    TextView tvForgotPassword, tvErrorMsg;
    String serverUrl;
    String id_token = null;
    boolean gpsStatus = false;
    private LocationManager mLocationManager = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    TelephonyManager telephonyManager;
    private ProgressBar mprogressBar=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        etEmailId = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        btSignIn = (Button) findViewById(R.id.btLogin);
        btSignUp = (Button) findViewById(R.id.btSignUp);
        tvErrorMsg = (TextView) findViewById(R.id.tv_error_msg);
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        }

        etEmailId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tvErrorMsg.setVisibility(View.GONE);
                }
            }
        });
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tvErrorMsg.setVisibility(View.GONE);
                }
            }
        });
        app = new AppPreferences(this);
        btSignUp.setOnClickListener(this);
        btSignIn.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        serverUrl = getResources().getString(R.string.server_url);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // serverUrl = app.getServerName();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        mprogressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN);

    }

    @Override
    public void onClick(View v) {
        if(internetConnected){
        switch (v.getId()) {
            case R.id.btLogin:
                /*util.showProgressDialog(this, "Login....");*/
                mprogressBar.setVisibility(View.VISIBLE);
                callLoginMethod();
                break;

            case R.id.btSignUp:
                openRegistrationPage();
                break;

            case R.id.tvForgotPassword:
                openForgotPasswordPage();
                break;

        }
        }else{
            util.makeAlertdiallog(LoginActivity.this,getString(R.string.please_check_your_internet_connectivity));
        }
    }

    private void openRegistrationPage() {
        if(internetConnected){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        }else{
            util.makeAlertdiallog(LoginActivity.this,getString(R.string.please_check_your_internet_connectivity));
        }
    }

    private void openForgotPasswordPage() {
        if(internetConnected){
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
        }else{
            util.makeAlertdiallog(LoginActivity.this,getString(R.string.please_check_your_internet_connectivity));
        }
    }

    private void callLoginMethod() {
        if(internetConnected){
        if (etEmailId.getText().length() > 0 && etPassword.getText().length() > 0) {
            if (Network_Available.hasConnection(this)) {
                try {
                    if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        gpsStatus = true;
                    } else {
                        gpsStatus = false;
                    }
                } catch (Exception e) {
                }
                // new fetchDetailinBackground(etEmailId.getText().toString(), etPassword.getText().toString()).execute();
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Login login = new Login();
                login.setUsername(etEmailId.getText().toString());
                login.setPassword(etPassword.getText().toString());
                login.setIMEI(telephonyManager.getDeviceId());
                login.setGpsStatus(gpsStatus);
                login.setApplicationVersion(getResources().getString(R.string.version));
                login.setOperatingSystem(getResources().getString(R.string.operating_system));
             /*   if (telephonyManager.getLine1Number() != null) {
                    //login.setPhoneNo(telephonyManager.getLine1Number());
                }*/
                if(app.getFCMToken() != null) {
                    login.setFcmToken(app.getFCMToken());
                }
                login.setRememberMe(true);
//                Log.d("LoginActivity", login.toString());
                Call<LoginResponse> result = apiInterface.doLogin(login);
                result.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        try {

                            mprogressBar.setVisibility(View.GONE);

                            if (response.code() == 200 || response.code() == 201)
                            {
                                tvErrorMsg.setVisibility(View.GONE);
                                id_token = response.body().getId_token();
                                app.setUsername(etEmailId.getText().toString());
                                app.setPassword(etPassword.getText().toString());
                                app.setSessionVal(true);
                                saveInPreferences(id_token);

                            } else if (response.code() == 401)
                            {
                                                               try {
                                    if (response.errorBody().toString() != null) 
                                    {
                                        JSONObject jsonObj = new JSONObject(response.errorBody().string());
                                        String str = jsonObj.optString("AuthenticationException");
                                        tvErrorMsg.setVisibility(View.VISIBLE);
                                        tvErrorMsg.setText("*" + str);
//                                        util.showToast(LoginActivity.this, "" + str);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (response.code() == 403)
                            {
                                tvErrorMsg.setText("* 403 Forbidden");
                            } else
                            {
                                tvErrorMsg.setText("* Something bad happened");

                            }
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            mprogressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        mprogressBar.setVisibility(View.GONE);
                    }
                });
            } else {
                mprogressBar.setVisibility(View.GONE);
                util.makeAlertdiallog(this, "No internet Connection available!");
            }
        } else {
            mprogressBar.setVisibility(View.GONE);
            util.makeAlertdiallog(this, "Please enter your credentials!");
        }
        }else{
            util.makeAlertdiallog(LoginActivity.this,getString(R.string.please_check_your_internet_connectivity));
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
        registerReceiver(gpsReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
        unregisterReceiver(gpsReceiver);
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

    private void saveInPreferences(String result) {

        mprogressBar.setVisibility(View.VISIBLE);
        app.setToken(result);
        ApiClient.setToken_id(result);
        //new fetchUserDetails().execute();
        ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
        Call<UserInfo> call = apiInterface.getAccountInfo();
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                try {
                    if (response.code() == 200 || response.code() == 201) {
                        mprogressBar.setVisibility(View.GONE);
                        app.setUserInfo(response.body());
                        callnewIntent();
                    } else if (response.code() == 401) {
                        util.callLoginScreen(LoginActivity.this);
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
            public void onFailure(Call<UserInfo> call, Throwable t) {
                mprogressBar.setVisibility(View.GONE);
            }
        });

    }


    private void callnewIntent() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        this.finish();
    }

    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                //Do your stuff on GPS status change
                if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    gpsStatus = true;
                } else {
                    gpsStatus = false;
                }
            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
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
