package com.agent.trakeye.tresbu.trakeyeagent.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.model.Case;
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

public class CaseDetailActivity extends Activity {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private boolean internetConnected = true;

    RelativeLayout rlBack;
    ImageView btEdit;
    TextView tvHeader, tvDescription, tvCreateDate, tvUpdateDate,
            tvStatus, tvPinLat, tvPinLong,
            tvEscalated, tvAddress, tvReportedBy,
            tvAssignedBy, tvAssignedTo, tvUpdatedBy,
            tvCaseTYpe;
    Case mCases;
    LinearLayout linearLayout;
    AppPreferences app;

    @Override
    protected void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
        calltogetDetail();
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

    private void calltogetDetail() {

        if (internetConnected) {
            if (mCases != null) {

                if (ApiClient.getToken_id() == null) {
                    ApiClient.setToken_id(app.getToken());
                }

                util.showProgressDialog(this, "fetching case details,Please Wait..............");

                ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
                Call<Case> call = apiInterface.getCaseDetail(String.valueOf(mCases.getId()));
                call.enqueue(new Callback<Case>() {
                    @Override
                    public void onResponse(Call<Case> call, Response<Case> response) {

                        try {
                            if (response.code()==200 || response.code()==201) {
                                if(response.body()!=null) {
                                    setItemonWidget(response.body());
                                    mCases = response.body();
                                }else{
                                    setItemonWidget(mCases);
                                }
                            } else if (response.code()==401) {
                                util.callLoginScreen(CaseDetailActivity.this);
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
                    public void onFailure(Call<Case> call, Throwable t) {
                        util.hideProgressDialog();

                    }
                });
            }
        } else {
            util.makeAlertdiallog(this,"Please check your internet connectivity");
            if (mCases != null) {
                setItemonWidget(mCases);
            } else {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        CaseDetailActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.casedetail);
        tvHeader = (TextView) findViewById(R.id.tvHeader);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvCreateDate = (TextView) findViewById(R.id.tvCreateDate);
        tvUpdateDate = (TextView) findViewById(R.id.tvUpdateDate);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        btEdit=(ImageView)findViewById(R.id.btEdit);
        app = new AppPreferences(this);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
//        tvPinLat = (TextView) findViewById(R.id.tvPinLat);
//        tvPinLong = (TextView) findViewById(R.id.tvPinLong);
        tvEscalated = (TextView) findViewById(R.id.tvEscalated);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvReportedBy = (TextView) findViewById(R.id.tvReportedBy);
        tvAssignedBy = (TextView) findViewById(R.id.tvAssignedBy);
        tvAssignedTo = (TextView) findViewById(R.id.tvAssignedTo);
        tvUpdatedBy = (TextView) findViewById(R.id.tvUpdateBy);
        tvCaseTYpe = (TextView) findViewById(R.id.tvCaseType);
        linearLayout = (LinearLayout) findViewById(R.id.llImageLayout);
        rlBack = (RelativeLayout) findViewById(R.id.rlBack);

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaseDetailActivity.this.finish();
            }
        });

        mCases = (Case) getIntent().getSerializableExtra("cases");


        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internetConnected) {
                    Intent intent = new Intent(CaseDetailActivity.this, EditCaseActivity.class);
                    intent.putExtra("cases", mCases);
                    startActivity(intent);
                }else{
                    util.makeAlertdiallog(CaseDetailActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });

    }

    public void clickToMap(View view){
                try {
                    //using google maps you will create a map setting lat & long.
                    String urlAddress = "http://maps.google.com/maps?q="+ mCases.getPinLat() +"," + mCases.getPinLong() +"("+ mCases.getAssignedToUser() + ")";
                    Intent maps = new Intent(Intent.ACTION_VIEW, Uri.parse(urlAddress));
                    startActivity(maps);
                } catch (Exception e) {
//                    Log.e(TAG, e.toString());
                }
    }
    public void setItemonWidget(Case mCases) {
        util.hideProgressDialog();
        if (mCases != null) {
            tvHeader.setText(getResources().getString(R.string._case) + " " + mCases.getId());
            if (mCases.getDescription() != null) {
                tvDescription.setText(mCases.getDescription());
            }
            if (mCases.getCreateDate() != null) {
//                tvCreateDate.setText(util.printDateforDetailPage(mCases.getCreateDate()));
                tvCreateDate.setText(util.getDate(Long.parseLong(mCases.getCreateDate()), "dd/MM/yyyy HH:mm:ss"));
            }
            if (mCases.getUpdateDate() != null) {
//                tvUpdateDate.setText(util.printDateforDetailPage(mCases.getUpdateDate()));
                tvUpdateDate.setText(util.getDate(Long.parseLong(mCases.getUpdateDate()), "dd/MM/yyyy HH:mm:ss"));
            }

            if (mCases.getStatus() != null) {
                tvStatus.setText(mCases.getStatus().name());
            }
            try {
                if (mCases.getEscalated()) {
                    tvEscalated.setText("True");
                } else {
                    tvEscalated.setText("False");
                }
            } catch (Exception e) {
            }

            if (mCases.getCaseType() != null) {
                tvCaseTYpe.setText(mCases.getCaseType().getName());
            }

            if (mCases.getAssignedByUser() != null) {
                tvAssignedBy.setText(mCases.getAssignedByUser());
            }
            if (mCases.getAssignedToUser() != null) {
                tvAssignedTo.setText(mCases.getAssignedToUser());
            }
            if (mCases.getReportedByUser() != null) {
                tvReportedBy.setText(mCases.getReportedByUser());
            }

            if (mCases.getUpdatedByUser() != null) {
                tvUpdatedBy.setText(mCases.getUpdatedByUser());
            }

//            try {
//                tvPinLat.setText(String.valueOf(mCases.getPinLat()));
//            } catch (Exception e) {
//            }
//            try {
//                tvPinLong.setText(String.valueOf(mCases.getPinLong()));
//            } catch (Exception e) {
//            }
            if (mCases.getAddress() != null) {
                tvAddress.setText(mCases.getAddress());
            }

            try {
                if (mCases.getCaseImages() != null && mCases.getCaseImages().size() > 0) {
                    linearLayout.removeAllViews();
                    LinearLayout.LayoutParams mRparams = new LinearLayout.LayoutParams(150, 150);
                    ImageView myImage = null;
                    for (int i = 0; i < mCases.getCaseImages().size(); i++) {
                        myImage = new ImageView(this);
                        mRparams.setMargins(10, 10, 10, 10);
                        myImage.setPadding(25, 10, 10, 10);
                        myImage.setLayoutParams(mRparams);
                        Bitmap bm = decodeBase64(mCases.getCaseImages().get(i).getImage());
                        myImage.setImageBitmap(getResizedBitmap(bm,150));
                        myImage.setId(i);
//                        myImage.setBackgroundResource(R.drawable.search_box);

                        linearLayout.addView(myImage);
                    }
                }
            } catch (Exception e) {

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
