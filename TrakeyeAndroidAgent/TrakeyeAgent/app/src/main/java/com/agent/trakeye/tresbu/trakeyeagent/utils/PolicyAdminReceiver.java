package com.agent.trakeye.tresbu.trakeyeagent.utils;

import android.annotation.TargetApi;
import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;

public class PolicyAdminReceiver extends DeviceAdminReceiver {
    public PolicyAdminReceiver() {
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {

//        Toast.makeText(context, "Admin disable request",Toast.LENGTH_LONG).show();
        return super.onDisableRequested(context, intent);

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        //getManager(context).setCameraDisabled(getWho(context),false);
       // Toast.makeText(context, "Admin enabled",Toast.LENGTH_LONG).show();
       // devicePolicyManager.setCameraDisabled(deviceAdmin, true);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        AppPreferences app = new AppPreferences(context);
        app.setAdminVal(false);
//        Toast.makeText(context, "Admin disabled",Toast.LENGTH_LONG).show();
    }


}
