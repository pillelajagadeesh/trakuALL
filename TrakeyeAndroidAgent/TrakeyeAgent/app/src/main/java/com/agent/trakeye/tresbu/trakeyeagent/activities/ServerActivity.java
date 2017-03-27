package com.agent.trakeye.tresbu.trakeyeagent.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiClient;

public class ServerActivity extends Activity {

    Button submit;

    EditText server;
    // String loading_url;
    String server_str;
    Dialog dialog;
    AppPreferences appPreferences;

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_screen);
        appPreferences = new AppPreferences(this);
        if(!appPreferences.getSessionVal()){
            finish();
        }
        submit = (Button) findViewById(R.id.submit);
        server = (EditText) findViewById(R.id.server);


        server.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {
                    CallLoginMethod();
                }
                return false;
            }
        });

        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CallLoginMethod();
            }
        });


    }

    protected void CallLoginMethod() {
        // TODO Auto-generated method stub
        server_str = server.getText().toString();
        if (server_str.length() > 0) {
            callIntentMethod(server_str);
        } else {
            showAlertMessage("Error", "Please enter valid url.");
        }
    }


    private void showAlertMessage(String message, String string) {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(ServerActivity.this);
        builder.setTitle(message);
        builder.setMessage(string);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void callIntentMethod(String servername) {
        // TODO Auto-generated method stub

        appPreferences.setServername(servername);
     //   ApiClient.setBaseUrl(servername);
        Intent intent = new Intent(ServerActivity.this, LoginActivity.class);
        startActivity(intent);
        ServerActivity.this.finish();
    }

}
