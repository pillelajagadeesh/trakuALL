package com.agent.trakeye.tresbu.trakeyeagent.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.agent.trakeye.tresbu.trakeyeagent.R;

public class SendPicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_pic);
        getSupportActionBar().setTitle("Send Pic");
    }
}
