package com.agent.trakeye.tresbu.trakeyeagent.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.application.MyApplication;
import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.utils.ConnectivityReceiver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class SplashActivty extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    String username, password, servername,notificationCount;
    Boolean userAdmin;
    TextView tvVersion;
    private Snackbar snackbar=null;
    private StringBuilder log=null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // setContentView method will set the layout file for the screen.
        // If we don't use this method then screen will be black.
        setContentView(R.layout.activity_splash_activty);
        tvVersion = (TextView) findViewById(R.id.tvVersion);
        tvVersion.setText("TEV_"+ getResources().getString(R.string.version));

        createLogs();

        // create a instance of Thread class with the name timer.
        Thread timer = new Thread() {

            public void run() {
                try {
                    // wait for 2000 milliseconds
                    // it will keep the screen for 2 seconds
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // fetching the login details from shared preference
                    AppPreferences app = new AppPreferences(SplashActivty.this);
                    username = app.getUsername();
                    password = app.getPassword();
                    servername = app.getServerName();
                    userAdmin = app.getAdminVal();
//                    notificationCount = app.getNotificationCount();

                    if (username.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
                      //  ApiClient.setBaseUrl(app.getServerName());
                        Intent intent = new Intent(SplashActivty.this,
                                LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashActivty.this,
                                MapsActivity.class);
                        startActivity(intent);
                    }
                    overridePendingTransition(0, 0);
                    SplashActivty.this.finish();

                }
            }
        };

        // to start the thread
        timer.start();


    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected)
    {
        String message;
        int color;

        AppPreferences appPreferences = new AppPreferences(SplashActivty.this);
        appPreferences.setConnectionStatus(isConnected);
        if (isConnected)
        {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        }
        else
        {
            message = "Sorry! Not connected to internet";
            color = Color.YELLOW;

            snackbar = Snackbar.make(findViewById(R.id.imageView2), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected)
    {
        Log.d("","Connection Status==> "+isConnected);
        showSnack(isConnected);
    }

    void createLogs()
    {
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            log=new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }

        } catch (IOException e) {
        }


        //convert log to string
        final String logString = new String(log.toString());

        //create text file in SDCard
        File file = new File(getApplicationContext().getExternalFilesDir(null), "logcat.txt");


        try {
            //to write logcat in text file
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            // Write the string to the file
            osw.write(logString);
            osw.flush();
            osw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
