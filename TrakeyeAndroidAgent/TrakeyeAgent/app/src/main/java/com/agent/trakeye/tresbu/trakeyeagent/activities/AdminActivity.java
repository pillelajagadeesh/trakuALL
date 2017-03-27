package com.agent.trakeye.tresbu.trakeyeagent.activities;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.utils.PolicyAdminReceiver;

public class AdminActivity extends Activity {

    ComponentName deviceAdmin;
    AppPreferences app;
    private DevicePolicyManager devicePolicyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   setContentView(R.layout.activity_admin);

        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        deviceAdmin = new ComponentName(this, PolicyAdminReceiver.class);

        Intent intent = getIntent();
        app = new AppPreferences(AdminActivity.this);
        boolean activate = intent.getBooleanExtra("activate", false);
        if (activate) {
            if (!devicePolicyManager.isAdminActive(deviceAdmin)) {
                Intent i = new Intent(
                        DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                i.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                        deviceAdmin);
                i.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "");
                startActivityForResult(i, 1);
            } else
                finish();
        } else {
            if (devicePolicyManager.isAdminActive(deviceAdmin))
                devicePolicyManager.removeActiveAdmin(deviceAdmin);
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                app.setAdminVal(true);
                startActivity(new Intent(this, SplashActivty.class));
            } else {
                finish();
            }
        }
        finish();
    }
}
