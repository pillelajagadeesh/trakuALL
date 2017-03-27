package com.agent.trakeye.tresbu.trakeyeagent.application;

/**
 * Created by Tresbu on 18-Nov-16.
 */

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.agent.trakeye.tresbu.trakeyeagent.utils.ConnectivityReceiver;



public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate()
    {
     /*   ACRA.init(this);
        YourOwnSender yourSender = new YourOwnSender(whatever, parameters, needed);
        ACRA.getErrorReporter().setReportSender(yourSender);*/
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}