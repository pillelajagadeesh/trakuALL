package com.agent.trakeye.tresbu.trakeyeagent.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.model.Service;
import com.agent.trakeye.tresbu.trakeyeagent.model.ServiceImages;
import com.agent.trakeye.tresbu.trakeyeagent.model.ServiceStatus;
import com.agent.trakeye.tresbu.trakeyeagent.model.ServiceTypeAttributeValues;
import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiClient;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiInterface;
import com.agent.trakeye.tresbu.trakeyeagent.utils.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tresbu on 25-Oct-16.
 */

public class EditServiceActivity extends Activity implements View.OnClickListener {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private boolean internetConnected = true;

    String[] spinValue = {"INPROGRESS", "PENDING", "CLOSED", "CANCELLED"};
    Service mService;
    RelativeLayout rlBack;
    TextView tvId, tvDescription, tvServiceDate, tvCase, tvServiceType;
    Spinner spStatus;
    EditText etNotes;
    ImageView ivImageUpload;
    boolean isImageSelected = false;
    String spin_val;
    LinearLayout llServiceTypeLayout;
    Button btSave, btCancel;
    List<EditText> allEds = null;
    AppPreferences app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editservice);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        app = new AppPreferences(this);
        if(!app.getSessionVal()){
            finish();
        }
        llServiceTypeLayout = (LinearLayout) findViewById(R.id.llServiceTypeLayout);
        tvId = (TextView) findViewById(R.id.tvId);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvServiceDate = (TextView) findViewById(R.id.tvServiceDate);
        tvServiceType = (TextView) findViewById(R.id.tvServiceType);
        ivImageUpload = (ImageView) findViewById(R.id.uploadImage);
        ivImageUpload.setOnClickListener(this);
        tvCase = (TextView) findViewById(R.id.tvCase);
        spStatus = (Spinner) findViewById(R.id.spStatus);
        etNotes = (EditText) findViewById(R.id.etNotes);
        btCancel = (Button) findViewById(R.id.btn_clear);
        btSave = (Button) findViewById(R.id.btn_save);
        rlBack = (RelativeLayout) findViewById(R.id.rlBack);

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditServiceActivity.this.finish();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditServiceActivity.this.finish();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnected) {
                    callSaveMethod();
                }else{
                    util.makeAlertdiallog(EditServiceActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });


        mService = (Service) getIntent().getSerializableExtra("service");
        if (mService != null) {
            try {
                tvId.setText(String.valueOf(mService.getId()));
            } catch (Exception e) {
            }

            if (mService.getDescription() != null) {
                tvDescription.setText(mService.getDescription());
            }


            if (mService.getServiceDate() != null) {
                tvServiceDate.setText(util.getDate(Long.parseLong(mService.getServiceDate()), "dd/MM/yyyy HH:mm:ss"));
            }

            if (mService.getNotes() != null) {
                etNotes.setText(mService.getNotes());
            }

            if (mService.getTrCase() != null) {
                tvCase.setText(String.valueOf(mService.getTrCase().getId()));
            }

            if (mService.getServiceType() != null) {
                tvServiceType.setText((mService.getServiceType().getName()));
                if (mService.getServiceTypeAttributeValues() != null && mService.getServiceTypeAttributeValues().size() > 0) {
                    drawAttributeLayout(mService.getServiceTypeAttributeValues());
                }
            }


            //setting array adaptors to spinners
            //ArrayAdapter is a BaseAdapter that is backed by an array of arbitrary objects
//        ArrayAdapter<String> spin_adapter = new ArrayAdapter.createFromResource(CustomerFormActivity.this, android.R.layout.simple_spinner_item, gender);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.servicestatus, R.layout.spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // setting adapters to spinners
            spStatus.setAdapter(adapter);


            if (mService.getStatus() != null) {

                int spinnerPosition = adapter.getPosition(mService.getStatus().name());
                spStatus.setSelection(spinnerPosition);
//                System.out.println("status is:" + mService.getStatus().name() + "........." + adapter.getPosition(mService.getStatus().name()));
                spin_val = spinValue[spinnerPosition];//saving the value selected
            }

            spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override

                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                    // TODO Auto-generated method stub
                    spin_val = spinValue[position];//saving the value selected
                }

                @Override

                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
        } else {
            util.makeAlertdiallog(this, "Unexpected error!!");
            EditServiceActivity.this.finish();
        }
    }


    private void drawAttributeLayout(ArrayList<ServiceTypeAttributeValues> serviceTypeAttributeValues) {
        llServiceTypeLayout.removeAllViews();
        if (serviceTypeAttributeValues != null
                && serviceTypeAttributeValues.size() > 0) {
            allEds = new ArrayList<EditText>();
            allEds.clear();

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            EditText myEditText = null;
            TextView myText = null;
            for (int i = 0; i < serviceTypeAttributeValues.size(); i++) {

                myText = new TextView(this);
                params.setMargins(10, 10, 10, 10);
                myText.setPadding(10, 10, 10, 10);
                myText.setLayoutParams(params);
                myText.setTextColor(getResources().getColor(R.color.menutoolbar));
                myText.setText(serviceTypeAttributeValues.get(i).getServiceTypeAttribute().getName());

                myEditText = new EditText(this);
                params.setMargins(10, 10, 10, 10);
                myEditText.setPadding(10, 10, 10, 10);
                myEditText.setLayoutParams(params);
                myEditText.setTextColor(getResources().getColor(R.color.black));
                myEditText.setHint(serviceTypeAttributeValues.get(i).getAttributeValue());
                myEditText.setHintTextColor(getResources().getColor(R.color.back_menu));
                myEditText.setId(i);
//                myEditText.setBackgroundResource(R.drawable.search_box);
                myEditText.setBackgroundResource(R.color.white);
                allEds.add(myEditText);
                llServiceTypeLayout.addView(myText);
                llServiceTypeLayout.addView(myEditText);
            }

        } else {
            util.makeAlertdiallog(this, "No attribute is available!");
        }
    }

    private void callSaveMethod() {
        if (etNotes.getText().toString().length() > 0) {
            Service caseRequest = new Service();
            caseRequest.setCreatedDate(mService.getCreatedDate());
            caseRequest.setDescription(mService.getDescription());
            caseRequest.setId(mService.getId());
            caseRequest.setModifiedDate(mService.getModifiedDate());
            caseRequest.setReportedBy(mService.getReportedBy());
            caseRequest.setServiceDate(mService.getServiceDate());
            caseRequest.setNotes(etNotes.getText().toString());
            if (spin_val.equalsIgnoreCase(ServiceStatus.CLOSED.name())) {
                caseRequest.setStatus(ServiceStatus.CLOSED);
            } else if (spin_val.equalsIgnoreCase(ServiceStatus.CANCELLED.name())) {
                caseRequest.setStatus(ServiceStatus.CANCELLED);
            } else if (spin_val.equalsIgnoreCase(ServiceStatus.PENDING.name())) {
                caseRequest.setStatus(ServiceStatus.PENDING);
            } else {
                caseRequest.setStatus(ServiceStatus.INPROGRESS);
            }
            caseRequest.setServiceType(mService.getServiceType());
            caseRequest.setTrCase(mService.getTrCase());
            caseRequest.setUpdatedBy(mService.getUpdatedBy());
            caseRequest.setUser(mService.getUser());
            ArrayList<ServiceTypeAttributeValues> list = new ArrayList<ServiceTypeAttributeValues>();
            for (int i = 0; i < mService.getServiceTypeAttributeValues().size(); i++) {
                ServiceTypeAttributeValues data = new ServiceTypeAttributeValues();
                data.setId(mService.getServiceTypeAttributeValues().get(i).getId());
                data.setAttributeValue(allEds.get(i).getText().toString());
                data.setServiceTypeAttribute(mService.getServiceTypeAttributeValues().get(i).getServiceTypeAttribute());
                list.add(data);
            }
            caseRequest.setServiceTypeAttributeValues(list);
            if (isImageSelected) {
                try {
                    Bitmap bitmap = ((BitmapDrawable) ivImageUpload.getDrawable()).getBitmap();
                    if (bitmap != null) {
                        ArrayList<ServiceImages> serviceImage = new ArrayList<>();
                        ServiceImages cas = new ServiceImages();
                        cas.setImage(convertintobase64(bitmap));
                        //cas.setImage(("IMAGEASASASA"));
                        serviceImage.add(cas);
                        caseRequest.setServiceImages(serviceImage);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            util.showProgressDialog(this, "posting your service request!!");
            ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
            Call<Service> call = apiInterface.updateUserServices(caseRequest);
            call.enqueue(new Callback<Service>() {
                @Override
                public void onResponse(Call<Service> call, Response<Service> response) {
                    try {
                        util.hideProgressDialog();
                        if (response.code() == 200 || response.code() == 201) {
                            util.makeAlertdiallog(EditServiceActivity.this, "Service updated successfully!!");
                        } else if (response.code() == 401) {
                            util.callLoginScreen(EditServiceActivity.this);
                            finish();
                        } else if (response.code() == 403)
                        {
                            util.showToast(getApplicationContext(),"* 403 Forbidden");
                        } else
                        {
                            util.showToast(getApplicationContext(),"* Something bad happened");

                        }
                        EditServiceActivity.this.finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                        util.makeAlertdiallog(EditServiceActivity.this, "Access is denied!!");
                        EditServiceActivity.this.finish();
                    }
                }

                @Override
                public void onFailure(Call<Service> call, Throwable t) {
                    util.makeAlertdiallog(EditServiceActivity.this, "Access is denied!!");
                    EditServiceActivity.this.finish();
                }
            });
        } else {
            util.makeAlertdiallog(this, "Note cannot be empty!!");
        }
    }


    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if(internetConnected) {
                    if (options[item].equals("Take Photo")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, 1);
                    } else if (options[item].equals("Choose from Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);

                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }else{
                    util.makeAlertdiallog(EditServiceActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    while (bitmap.getHeight() > 1024 || bitmap.getWidth() > 1024) {
                        bitmap = getResizedBitmap(bitmap);
                    }
                    ivImageUpload.setImageBitmap(bitmap);

                    isImageSelected = true;
                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
//                Log.w("path of image from gallery......******************.........", picturePath+"");
                //
                ivImageUpload.setImageBitmap(thumbnail);
                isImageSelected = true;
            }
        }
    }

    // This method is to resize the Bitmap Image
    public Bitmap getResizedBitmap(Bitmap image) {
        int width = image.getWidth() / 2;
        int height = image.getHeight() / 2;
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private String convertintobase64(Bitmap bitmap2) {
        // TODO Auto-generated method stub
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT | Base64.NO_WRAP);
        return temp;
    }

    public static Bitmap decodeBase64(String input, int i) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);

    }

    @Override
    public void onClick(View view) {
        if(internetConnected){
        switch (view.getId()) {
            case R.id.uploadImage:
                selectImage();
                break;
            default:
                break;
        }
        }else{
            util.makeAlertdiallog(EditServiceActivity.this,getString(R.string.please_check_your_internet_connectivity));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to register runtime broadcast receiver to show snackbar alert for internet connection..
     */
    private void registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, internetFilter);
    }

    /**
     * Runtime Broadcast receiver inner class to capture internet connectivity events
     */
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = getConnectivityStatusString(context);
            setSnackbarMessage(status, false);

        }
    };

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

    private void setSnackbarMessage(String status, boolean showBar) {
        int duration = 0;
        int color = 0;
        boolean isConnect = false, lastConnectionStatus = false;

        String internetStatus = "";
        if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {
            internetStatus = "Internet Connected";
            duration = Snackbar.LENGTH_LONG;
            isConnect = true;
            color = Color.WHITE;
        } else {
            duration = Snackbar.LENGTH_INDEFINITE;
            internetStatus = "Lost Internet Connection";
            isConnect = false;
            color = Color.YELLOW;
        }
        lastConnectionStatus = app.getConnectionStatus();
        app.setConnectionStatus(isConnect);

        if (lastConnectionStatus && isConnect) {

        } else {
            snackbar = Snackbar
                    .make(coordinatorLayout, internetStatus, duration)
                    .setAction("X", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
            // Changing message text color
            snackbar.setActionTextColor(Color.WHITE);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            if (internetStatus.equalsIgnoreCase("Lost Internet Connection")) {
                if (internetConnected) {
                    snackbar.show();
                    internetConnected = false;
                }
            } else {
                if (!internetConnected) {
                    internetConnected = true;
                    snackbar.show();
                }
            }
        }


    }

}
