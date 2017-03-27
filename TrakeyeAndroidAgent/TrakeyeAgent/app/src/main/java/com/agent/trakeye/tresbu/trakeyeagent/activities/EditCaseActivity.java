package com.agent.trakeye.tresbu.trakeyeagent.activities;

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
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.model.Case;
import com.agent.trakeye.tresbu.trakeyeagent.model.UpdateCase;
import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiClient;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiInterface;
import com.agent.trakeye.tresbu.trakeyeagent.utils.Network_Available;
import com.agent.trakeye.tresbu.trakeyeagent.utils.util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tresbu on 25-Oct-16.
 */

public class EditCaseActivity extends FragmentActivity {


    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private boolean internetConnected = true;

    AppPreferences app;

    String spin_val_status;
    String[] statusValues = {"NEW", "INPROGRESS", "PENDING", "ASSIGNED", "RESOLVED", "CANCELLED"};
    Case mCase;

    RelativeLayout rlBack;
    EditText etDescription;
    Spinner spStatus;
    Button btSave, btCancel;

    @Override
    public void onBackPressed() {
        this.finish();
        EditCaseActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editcase);
        app = new AppPreferences(this);
        if(!app.getSessionVal()){
            finish();
        }
        etDescription = (EditText) findViewById(R.id.et_desc);
        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        app = new AppPreferences(this);
        spStatus = (Spinner) findViewById(R.id.spStatus);

        mCase = (Case) getIntent().getSerializableExtra("cases");

        btCancel = (Button) findViewById(R.id.btn_clear);
        btSave = (Button) findViewById(R.id.btn_save);

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCaseActivity.this.finish();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCaseActivity.this.finish();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnected){
                    callSaveMethod();
                }else{
                    util.showToast(EditCaseActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });

        if (mCase != null) {
            if (mCase.getDescription() != null) {
                etDescription.setText(mCase.getDescription());
            }


        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.casestatus, R.layout.spinner_row);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        // setting adapters to spinners
        spStatus.setAdapter(adapter);
        if (mCase.getStatus() != null) {

            int spinnerPosition = adapter.getPosition(mCase.getStatus().name());
            spStatus.setSelection(spinnerPosition);
//                System.out.println("status is:" + mService.getStatus().name() + "........." + adapter.getPosition(mService.getStatus().name()));
            spin_val_status = statusValues[spinnerPosition];//saving the value selected
        }
        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                spin_val_status = statusValues[position];//saving the value selected
            }

            @Override

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

    }

    private void callSaveMethod() {
        if (internetConnected) {
            if (etDescription.getText().toString().length() > 0) {
                if (!spin_val_status.equals("")) {
                    UpdateCase uCase = new UpdateCase();
                    uCase.setAddress(mCase.getAddress());
                    uCase.setAssignedByUser(mCase.getAssignedByUser());
                    uCase.setAssignedToUser(mCase.getAssignedToUser());
                    uCase.setCaseImages(mCase.getCaseImages());
                    uCase.setCaseType(mCase.getCaseType());
                    uCase.setCaseTypeAttributeValues(mCase.getCaseTypeAttributeValues());
                    uCase.setCreateDate(mCase.getCreateDate());
                    uCase.setDescription(etDescription.getText().toString());
                    uCase.setEscalated(mCase.getEscalated());
                    uCase.setId(mCase.getId());
                    uCase.setPinLat(mCase.getPinLat());
                    uCase.setPinLong(mCase.getPinLong());
                    uCase.setPriority(mCase.getPriority());
                    uCase.setReportedByUser(mCase.getReportedByUser());
                    uCase.setStatus(spin_val_status);
                    uCase.setUpdateDate(mCase.getUpdateDate());
                    uCase.setUpdatedByUser(mCase.getUpdatedByUser());

                    util.showProgressDialog(this, "updating your request!!");
                    ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
                    Call<UpdateCase> call = apiInterface.updateUserCase(uCase);
                    call.enqueue(new Callback<UpdateCase>() {
                        @Override
                        public void onResponse(Call<UpdateCase> call, Response<UpdateCase> response) {
                            try {
                                btSave.setEnabled(true);
                                util.hideProgressDialog();
                                if (response.code() == 200 || response.code() == 201) {
                                    util.showToast(EditCaseActivity.this, "Case updated successfully!!");
                                } else if (response.code() == 401) {
                                    util.callLoginScreen(EditCaseActivity.this);
                                    finish();
                                } else if (response.code() == 403) {
                                    util.showToast(getApplicationContext(), "* 403 Forbidden");
                                } else {
                                    util.showToast(getApplicationContext(), "* Something bad happened");

                                }
                                EditCaseActivity.this.finish();
                            } catch (Exception e) {
                                btSave.setEnabled(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateCase> call, Throwable t) {
                            util.hideProgressDialog();
                            util.showToast(EditCaseActivity.this, "Something bad happend!!");
                        }
                    });
                } else {
                    util.makeAlertdiallog(EditCaseActivity.this, "Please select the Status!!");
                }
            } else {
                util.makeAlertdiallog(EditCaseActivity.this, "Description cannot be empty!!");
            }
        }else{
            util.makeAlertdiallog(EditCaseActivity.this,getString(R.string.please_check_your_internet_connectivity));
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


