package com.agent.trakeye.tresbu.trakeyeagent.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.model.UpdateCase;
import com.agent.trakeye.tresbu.trakeyeagent.model.UserInfo;
import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiClient;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiInterface;
import com.agent.trakeye.tresbu.trakeyeagent.utils.util;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by tresbu on 15/03/17.
 */

public class EditProfileActivity extends AppCompatActivity
{
    private AppPreferences app;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private TextView emailTextValue;
    private Button saveButton;
    private Button cancelButton;
    RelativeLayout rlBack;


    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private boolean internetConnected = true;
    
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_layout);
        app= new AppPreferences(this);
        if(!app.getSessionVal()){
            finish();
        }

        firstNameEditText = (EditText)findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText)findViewById(R.id.lastNameEditText);

        emailTextValue = (TextView)findViewById(R.id.emailTextValue);

        saveButton = (Button)findViewById(R.id.saveButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);

        rlBack = (RelativeLayout) findViewById(R.id.rlBack);

        rlBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditProfileActivity.this.finish();
            }
        });


        if (app.getUserInfo() != null)
        {
            if (app.getUserInfo().getFirstName() != null) {
                firstNameEditText.setText(app.getUserInfo().getFirstName());
                firstNameEditText.setSelection(app.getUserInfo().getFirstName().length());
            }
            if (app.getUserInfo().getLastName() != null) {
                lastNameEditText.setText(app.getUserInfo().getLastName());
                lastNameEditText.setSelection(app.getUserInfo().getLastName().length());
            }
            if (app.getUserInfo().getEmail() != null)
            {
                emailTextValue.setText(app.getUserInfo().getEmail());
            }
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) 
            {
                String firstNameString =firstNameEditText.getText().toString();
                String lastNameString =lastNameEditText.getText().toString();

                if(firstNameString.equalsIgnoreCase(""))
                {
                    showError("Your first name is required.");
                }
                else if(lastNameString.equalsIgnoreCase(""))
                {
                    showError("Your last name is required.");
                }
                else
                {
                    if(internetConnected)
                    {
                        hideSoftKeyboard(v);
                        callSaveMethod(firstNameString,lastNameString);
                    }
                    else
                    {
                        util.showToast(EditProfileActivity.this,getString(R.string.please_check_your_internet_connectivity));
                    }
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                hideSoftKeyboard(v);
                finish();
            }
        });
    }

    private void showError(String s) {
        util.hideProgressDialog();
        util.makeAlertdiallog(this, s);

    }


    private void callSaveMethod(final String firstNameString, final String lastNameString)
    {
        if (internetConnected)
        {
            UserInfo userInfo = new UserInfo();
            userInfo.setActivated(app.getUserInfo().isActivated());
            userInfo.setFirstName(firstNameString);
            userInfo.setLastName(lastNameString);
            userInfo.setLangKey(app.getUserInfo().getLangKey());
            userInfo.setLogin(app.getUserInfo().getLogin());
            userInfo.setEmail(app.getUserInfo().getEmail());

            util.showProgressDialog(this, "updating your request!!");

            ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);

            Call<Void> call = apiInterface.updateUserProfile(userInfo);
            call.enqueue(new Callback<Void>()
            {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response)
                {
                    try
                    {
                        util.hideProgressDialog();
                        if (response.code() == 200 || response.code() == 201)
                        {
                            UserInfo userInfo=  app.getUserInfo();
                            userInfo.setFirstName(firstNameString);
                            userInfo.setLastName(lastNameString);
                            app.setUserInfo(userInfo);

                            util.showToast(EditProfileActivity.this, "Profile has been updated successfully!!");

                        }
                        else if (response.code() == 401)
                        {
                            util.callLoginScreen(EditProfileActivity.this);
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
                public void onFailure(Call<Void> call, Throwable t) {
                    util.hideProgressDialog();
                    util.showToast(EditProfileActivity.this, "Something bad happend!!");
                }
            });
        }else{
            util.makeAlertdiallog(EditProfileActivity.this,getString(R.string.please_check_your_internet_connectivity));
        }
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
        super.onDestroy();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    .make(saveButton, internetStatus, duration)
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

    void hideSoftKeyboard(View view)
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
