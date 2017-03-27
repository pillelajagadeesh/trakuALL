package com.agent.trakeye.tresbu.trakeyeagent.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.agent.trakeye.tresbu.trakeyeagent.R;

public class PinAChamberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_achamber);
        getSupportActionBar().setTitle("Pin A Chamber");
    }
}
