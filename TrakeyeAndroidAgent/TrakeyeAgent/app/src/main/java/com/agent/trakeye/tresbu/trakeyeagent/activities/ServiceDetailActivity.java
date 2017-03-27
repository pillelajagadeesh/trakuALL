package com.agent.trakeye.tresbu.trakeyeagent.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.model.Service;
import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiClient;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiInterface;
import com.agent.trakeye.tresbu.trakeyeagent.utils.Network_Available;
import com.agent.trakeye.tresbu.trakeyeagent.utils.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tresbu on 25-Oct-16.
 */

public class ServiceDetailActivity extends Activity{
    ImageView btEdit;
    RelativeLayout rlBack;
    TextView tvHeader, tvDescription, tvCreateDate,tvStatus,tvModifiedDate,tvServiceDate,tvNotes, tvAssignedTo, tvCaseDesc;
    Service mService;
    LinearLayout linearLayout;
    AppPreferences app;

    @Override
    protected void onResume() {
        super.onResume();
        calltogetDetail();
    }

    private void calltogetDetail() {

        if (Network_Available.hasConnection(this)) {
            if(mService!=null) {


                if (ApiClient.getToken_id() == null) {
                    ApiClient.setToken_id(app.getToken());
                }
                util.showProgressDialog(this, "fetching service details,Please Wait..............");

                ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
                Call<Service> call = apiInterface.getServiceDetail(String.valueOf(mService.getId()));
                call.enqueue(new Callback<Service>() {
                    @Override
                    public void onResponse(Call<Service> call, Response<Service> response) {

                        try {
                            if(response.code()==200||response.code()==201) {
                                if (response.body().getDescription() != null) {
                                    setItemonWidget(response.body());
                                } else {
                                    setItemonWidget(mService);
                                }
                            } else if (response.code()==401) {
                                util.callLoginScreen(ServiceDetailActivity.this);
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
                    public void onFailure(Call<Service> call, Throwable t) {
                        util.hideProgressDialog();

                    }
                });
            }
        }else{
            util.makeAlertdiallog(this,"Please check your internet connectivity");
            if(mService!=null) {
                setItemonWidget(mService);
            }else {
                this.finish();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servicedetail);
        app = new AppPreferences(this);
        if(!app.getSessionVal()){
            finish();
        }
        btEdit=(ImageView)findViewById(R.id.btEdit);
        tvHeader=(TextView)findViewById(R.id.tvHeader);
        tvDescription=(TextView)findViewById(R.id.tvDescription);
        tvCreateDate=(TextView)findViewById(R.id.tvCreateDate);
        tvStatus=(TextView)findViewById(R.id.tvStatus);
        tvModifiedDate=(TextView)findViewById(R.id.tvModifiedDate);
        tvServiceDate=(TextView)findViewById(R.id.tvServiceDate);
        linearLayout = (LinearLayout) findViewById(R.id.llImageLayout);
        tvNotes=(TextView)findViewById(R.id.tvNotes);
        tvAssignedTo =(TextView)findViewById(R.id.tvAssignedTo);
        tvCaseDesc =(TextView)findViewById(R.id.tvCaseDesc);

        rlBack = (RelativeLayout) findViewById(R.id.rlBack);

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceDetailActivity.this.finish();
            }
        });


        mService= (Service) getIntent().getSerializableExtra("service");


            btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!mService.getStatus().name().equalsIgnoreCase("CLOSED")) {
                        Intent intent = new Intent(ServiceDetailActivity.this, EditServiceActivity.class);
                        intent.putExtra("service", mService);
                        startActivity(intent);
                    }else{
                        util.makeAlertdiallog( ServiceDetailActivity.this, "Service is closed. You cannot edit Closed Service!");
                    }
                }
            });

        }


    public void setItemonWidget(Service mService) {
        util.hideProgressDialog();
        if (mService != null) {

            tvHeader.setText(getResources().getString(R.string.service) + " " + mService.getId());
            if (mService.getDescription() != null) {
                tvDescription.setText(mService.getDescription());
            }

            if (mService.getCreatedDate() != null) {
//                tvCreateDate.setText(util.printDateforDetailPage(mService.getCreatedDate()));
                tvCreateDate.setText(getDate(Long.parseLong(mService.getCreatedDate()), "dd/MM/yyyy HH:mm:ss"));
            }

            if (mService.getStatus() != null) {
                tvStatus.setText(mService.getStatus().name());
            }

            if (mService.getModifiedDate() != null) {
                tvModifiedDate.setText(getDate(Long.parseLong(mService.getModifiedDate()), "dd/MM/yyyy HH:mm:ss"));
//                tvModifiedDate.setText(util.printDateforDetailPage(mService.getModifiedDate()));
            }

            if (mService.getServiceDate() != null) {
                tvServiceDate.setText(getDate(Long.parseLong(mService.getServiceDate()), "dd/MM/yyyy HH:mm:ss"));
//                tvServiceDate.setText(util.printDateforDetailPage(mService.getServiceDate()));
            }

            if (mService.getNotes() != null) {
                tvNotes.setText(mService.getNotes());
            }

            try {
                tvAssignedTo.setText(String.valueOf(mService.getTrCase().getAssignedToUser()));
            } catch (Exception e) {
            }

            if (mService.getTrCase() != null) {
                tvCaseDesc.setText(String.valueOf(mService.getTrCase().getDescription()));
            }

            try {
                if (mService.getServiceImages() != null && mService.getServiceImages().size() > 0) {
                    linearLayout.removeAllViews();
                    LinearLayout.LayoutParams mRparams = new LinearLayout.LayoutParams(150, 150);
                    ImageView myImage = null;
                    for (int i = 0; i < mService.getServiceImages().size(); i++) {
                        myImage = new ImageView(this);
                        mRparams.setMargins(10, 10, 10, 10);
                        myImage.setPadding(10, 10, 10, 10);
                        myImage.setLayoutParams(mRparams);
                        Bitmap bm = decodeBase64(mService.getServiceImages().get(i).getImage());
                        myImage.setImageBitmap(getResizedBitmap(bm,150));
                        myImage.setId(i);
                        myImage.setBackgroundResource(R.drawable.search_box);

                        linearLayout.addView(myImage);
                    }
                }
            } catch (Exception e) {

            }

        }else {
            util.makeAlertdiallog(this, "Unexpected error!!");
            ServiceDetailActivity.this.finish();
        }
    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);

    }

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

    /**
     * Return date in specified format.
     *
     * @param milliSeconds Date in milliseconds
     * @param dateFormat   Date format
     * @return String representing date in specified format
     */
    public String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());

    }
}
