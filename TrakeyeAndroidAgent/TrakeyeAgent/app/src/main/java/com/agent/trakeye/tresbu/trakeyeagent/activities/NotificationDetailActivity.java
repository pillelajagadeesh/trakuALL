package com.agent.trakeye.tresbu.trakeyeagent.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.model.Notification;
import com.agent.trakeye.tresbu.trakeyeagent.model.NotificationStatus;
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

public class NotificationDetailActivity extends Activity{

    Button btEdit;
    RelativeLayout rlBack;
    TextView tvHeader, tvDescription, tvCreateDate,tvStatus,tvSubject,tvAlertType,tvFromUser, tvToUser, tvCase, tvCaseTitle;
    Notification notification;

    AppPreferences app;

    @Override
    protected void onResume() {
        super.onResume();
        //calltogetDetail();

    }

    private void calltogetDetail() {

        if (Network_Available.hasConnection(this)) {
            if(notification!=null) {


                if (ApiClient.getToken_id() == null) {
                    ApiClient.setToken_id(app.getToken());
                }
                util.showProgressDialog(this, "fetching case details,Please Wait..............");

                ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
                Call<Notification> call = apiInterface.getNotificationDetail(String.valueOf(notification.getId()));
                call.enqueue(new Callback<Notification>() {
                    @Override
                    public void onResponse(Call<Notification> call, Response<Notification> response) {

                        try {
                            if(response.code()==200||response.code()==201) {
                                if (response.body().getSubject() != null) {
                                    setItemonWidget(response.body());
                                } else {
                                    setItemonWidget(notification);
                                }
                            } else if (response.code()==401) {
                                util.callLoginScreen(NotificationDetailActivity.this);
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
                    public void onFailure(Call<Notification> call, Throwable t) {
                        util.hideProgressDialog();

                    }
                });
            }
        }else{
            util.makeAlertdiallog(this,"Please check your internet connectivity");
            if(notification!=null) {
                setItemonWidget(notification);
            }else {
                this.finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificationdetail);
        app = new AppPreferences(this);
        if(!app.getSessionVal()){
            finish();
        }
        btEdit=(Button)findViewById(R.id.btEdit);
        tvHeader=(TextView)findViewById(R.id.tvHeader);
        tvDescription=(TextView)findViewById(R.id.tvDescription);
        tvCreateDate=(TextView)findViewById(R.id.tvCreatedDate);
        tvStatus=(TextView)findViewById(R.id.tvStatus);
        tvSubject=(TextView)findViewById(R.id.tvSubject);
        tvAlertType=(TextView)findViewById(R.id.tvAlertType);
        tvFromUser=(TextView)findViewById(R.id.tvFromUser);
        tvToUser=(TextView)findViewById(R.id.tvToUser);
        tvCase=(TextView)findViewById(R.id.tvCase);
        tvCaseTitle = (TextView)findViewById(R.id.tvCaseTitle);
        rlBack = (RelativeLayout) findViewById(R.id.rlBack);

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationDetailActivity.this.finish();
            }
        });

        notification= (Notification) getIntent().getSerializableExtra("notification");

        setItemonWidget(notification);
        updateNotification();
    }


    private void updateNotification() {
            Notification notificationData = new Notification();
            notificationData = notification;
            notificationData.setStatus(NotificationStatus.RECIEVED);
            ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
            Call<Notification> call = apiInterface.updateNotification(notificationData);
            call.enqueue(new Callback<Notification>() {
                @Override
                public void onResponse(Call<Notification> call, Response<Notification> response) {
                    try {
                        if (response.code() == 401) {
                            util.callLoginScreen(NotificationDetailActivity.this);
                            finish();
                        } else if (response.code() == 403)
                        {
                            util.showToast(getApplicationContext(),"* 403 Forbidden");
                        }
                    }catch (Exception ignored){

                    }
                }

                @Override
                public void onFailure(Call<Notification> call, Throwable t) {
                }
            });
    }

    public void setItemonWidget(Notification notification)
    {
        util.hideProgressDialog();
        if(notification!=null)
        {

            tvHeader.setText(getResources().getString(R.string.notification) + " " + notification.getId());
            if(notification.getDescription()!=null)
            {
                tvDescription.setText(notification.getDescription());
            }

            if(notification.getCreatedDate()!=null)
            {
                tvCreateDate.setText(util.getDate(Long.parseLong(notification.getCreatedDate()),"dd-MM-yyyy HH:mm:ss"));
            }

            if(notification.getStatus()!=null)
            {
                tvStatus.setText(notification.getStatus().name());
            }

            if(notification.getSubject()!=null)
            {
                tvSubject.setText(notification.getSubject());
            }

            if(notification.getAlertType()!=null)
            {
                tvAlertType.setText(notification.getAlertType().name());
            }

            if(notification.getFromUserName()!=null)
            {
                tvFromUser.setText(notification.getFromUserName());
            }

            if(notification.getToUserName()!=null)
            {
                tvToUser.setText(notification.getToUserName());
            }

            try
            {
                tvCase.setText(String.valueOf(notification.getTrCaseId()));
                if(notification.getNotificationType().equals("A")){
                    tvCase.setVisibility(View.GONE);
                    tvCase.setVisibility(View.GONE);
                }else{
                    tvCase.setVisibility(View.VISIBLE);
                    tvCase.setVisibility(View.VISIBLE);
                }

            }catch (Exception e){}
        }
    }
}
