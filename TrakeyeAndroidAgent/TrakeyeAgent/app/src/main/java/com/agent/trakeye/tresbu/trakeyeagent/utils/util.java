package com.agent.trakeye.tresbu.trakeyeagent.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.activities.LoginActivity;
import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.services.ExecuteRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * Created by Tresbu on 21-Sep-16.
 */
public class util {

    public static void makeAlertdiallog(Context context,
                                        String msg) {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(titlte);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static Dialog dialog;

    public static void showProgressDialog(final Context ctx, String message)
    {

        try {
            // hideProgressDialog();
            dialog = new Dialog(ctx);
            dialog.setContentView(R.layout.dialoglayout);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            TextView text = (TextView) dialog.findViewById(R.id.progressText);
//            text.setText(message);
            text.setText("Loading...");

            ProgressBar  mprogressBar = (ProgressBar)dialog.findViewById(R.id.progressBar);
            mprogressBar.getIndeterminateDrawable().setColorFilter(
                    ctx.getResources().getColor(R.color.loginbtn),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            dialog.show();
        } catch (Exception e) {
        }

    }

    public static void hideProgressDialog() {
        try {

            if (dialog != null) {
                dialog.cancel();
                dialog = null;
            }
        } catch (Exception e) {
        }
    }

    public static boolean isShowingProgressDialog(Context ctx) {
        boolean f = false;
        try {

            if (dialog != null) {
                f = dialog.isShowing();
            }
        } catch (Exception e) {
        }
        return f;
    }

    public static String printDate(String str) {
        try {
            SimpleDateFormat frm = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = formatter.parse(str);
            return frm.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return str;

        }
    }

    public static String printDateforDetailPage(String str) {
        try {
            SimpleDateFormat frm = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = formatter.parse(str);
            return frm.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return str;

        }
    }

    /**
     * Return date in specified format.
     *
     * @param milliSeconds Date in milliseconds
     * @param dateFormat   Date format
     * @return String representing date in specified format
     */
    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());

    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    private static double LOCAL_PI = 3.1415926535897932385;

    private static double ToRadians(double degrees) {
        double radians = degrees * LOCAL_PI / 180;
        return radians;
    }

    public static double DirectDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = ToRadians(lat2 - lat1);
        double dLng = ToRadians(lng2 - lng1);
        double a = sin(dLat / 2) * sin(dLat / 2) +
                cos(ToRadians(lat1)) * cos(ToRadians(lat2)) *
                        sin(dLng / 2) * sin(dLng / 2);
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));
        double dist = earthRadius * c;
        double meterConversion = 1609.00;
        return dist * meterConversion;
    }

    public static void callLoginScreen(Context context) {
        showToast(context, "Session Expired, Redirecting to Login Page!!");
        AppPreferences app = new AppPreferences(context);
        app.setName("");
        app.setPassword("");
        app.setServername("");
        app.setToken("");
        app.setUsername("");
        app.setSessionVal(false);

        context.stopService(new Intent(context, ExecuteRequest.class));

        if(!context.getClass().equals(LoginActivity.class)) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }
    }
}
