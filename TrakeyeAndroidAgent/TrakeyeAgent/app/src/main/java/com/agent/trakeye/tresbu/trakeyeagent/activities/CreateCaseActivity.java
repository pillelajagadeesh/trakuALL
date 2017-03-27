package com.agent.trakeye.tresbu.trakeyeagent.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.adapters.CaseTypeAdpter;
import com.agent.trakeye.tresbu.trakeyeagent.adapters.CustomAlertAdapter;
import com.agent.trakeye.tresbu.trakeyeagent.model.Case;
import com.agent.trakeye.tresbu.trakeyeagent.model.CaseImages;
import com.agent.trakeye.tresbu.trakeyeagent.model.CaseRequest;
import com.agent.trakeye.tresbu.trakeyeagent.model.CaseType;
import com.agent.trakeye.tresbu.trakeyeagent.model.CaseTypeAttributeValues;
import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiClient;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiInterface;
import com.agent.trakeye.tresbu.trakeyeagent.utils.AppConstants;
import com.agent.trakeye.tresbu.trakeyeagent.utils.Network_Available;
import com.agent.trakeye.tresbu.trakeyeagent.utils.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class CreateCaseActivity extends Activity implements LocationListener, View.OnClickListener {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private boolean internetConnected = true;
    AppPreferences app;
    EditText etDescription;
    Button btSave, btCancel;
    ImageView ivImageUpload;
    TextView tvDesc, tvNote, tvPriority, tvUpload, tvCaseType, spCaseTypeEmpty;
    double latitude, longitude;
    String address;
    Spinner spPriority;
    CheckBox cbEscalated;
    AppPreferences appPreferences;
    private ArrayList<String> array_case_type;
    private ArrayList<CaseType> CaseTypeVal = new ArrayList<>();
    HashSet<String> caseTypeId = new HashSet<>();
    RelativeLayout rlBack;
    String[] priorityValues = {"Select Priority", "LOW", "MEDIUM", "HIGH", "CRITICAL"};
    String spin_val_priority;
    String item;
    String audioStream = "";
    int pos = 0;
    boolean isImageSelected = false;
    CaseType selectedCaseType = null;
    CaseTypeAdpter caseTypeAdpter;
    LinearLayout llCaseTypeLayout;
    private int textlength = 0;
    private int selected = -1;

    private int pageVal = 0;
    CustomAlertAdapter arrayAdapter = null;
    ListView listview = null;

    private boolean isCaseTypeSelected = false, isPrioritySelected = false;
    private boolean loadingMore=false;
    private ArrayList<String>  arraylist;
    private Dialog dialogProject=null;
    private EditText editText;
    Button btnCancel;

    @Override
    public void onBackPressed() {
        this.finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);

        /*getCaseType(pageVal);*/

        app = new AppPreferences(this);
        if(!app.getSessionVal()){
            finish();
        }
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        tvNote = (TextView) findViewById(R.id.tv_notes);
        tvPriority = (TextView) findViewById(R.id.tv_priority);
        tvUpload = (TextView) findViewById(R.id.tv_upload);
        tvCaseType = (TextView) findViewById(R.id.tvCaseType);

        btCancel = (Button) findViewById(R.id.btn_clear);
        btSave = (Button) findViewById(R.id.btn_save);
        etDescription = (EditText) findViewById(R.id.et_desc);
        cbEscalated = (CheckBox) findViewById(R.id.cbEscalated);
        spPriority = (Spinner) findViewById(R.id.spPriority);
        llCaseTypeLayout = (LinearLayout) findViewById(R.id.llCaseTypeLayout);
        spCaseTypeEmpty = (TextView) findViewById(R.id.spCaseTypeEmpty);
        llCaseTypeLayout.setVisibility(View.GONE);
        appPreferences = new AppPreferences(this);
        ivImageUpload = (ImageView) findViewById(R.id.uploadImage);
        ivImageUpload.setOnClickListener(this);
        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateCaseActivity.this.finish();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateCaseActivity.this.finish();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internetConnected) {
                    saveIssueIntoDataBase();
                }else{
                    util.makeAlertdiallog(CreateCaseActivity.this,getString(R.string.please_check_your_internet_connectivity));
                }
            }
        });

        spPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                if (position > 0) {
                    isPrioritySelected = true;
                    spin_val_priority = priorityValues[position];//saving the value selected
                } else {
                    isPrioritySelected = false;
                }
            }

            @Override

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.priorityVal, R.layout.spinner_row);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);
        // setting adapters to spinners
        spPriority.setAdapter(adapter1);


        spCaseTypeEmpty.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(appPreferences.getCaseType() == null)
                {
                    getCaseType(0);
                }
                else
                {
                    updateDialogListView();
                }
            }
        });

        try {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(bestProvider, 30000, 10f, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void callCaseTypeDialog()
    {


        try {
            if (appPreferences.getCaseType() != null)
            {

                dialogProject = new Dialog(this, android.R.style.DeviceDefault_Light_ButtonBar);
                dialogProject.setContentView(R.layout.builder_select_project_layout);
                listview = (ListView) dialogProject.findViewById(R.id.projectListView);
                editText = (EditText) dialogProject.findViewById(R.id.searchEditText);
                btnCancel = (Button) dialogProject.findViewById(R.id.btnCancel);
                editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search_icon, 0, 0, 0);
                dialogProject.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialogProject.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                dialogProject.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialogProject.show();

                if(array_case_type.size()>0)
                {
                    if(arrayAdapter==null)
                    {
                        arrayAdapter = new CustomAlertAdapter(CreateCaseActivity.this, array_case_type);
                        listview.setAdapter(arrayAdapter);
                    }
                    else
                    {
                        arrayAdapter.notifyDataSetChanged();
                    }

                }


                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        String temp = array_case_type.get(position);
                        int index = -1;

                        for (int i = 0; i < CaseTypeVal.size(); i++)
                        {
                            if (CaseTypeVal.get(i).getName().equals(temp))
                            {
                                index = i;

                            }
                            if(i==CaseTypeVal.size()-1)
                            {
                                isCaseTypeSelected = true;
                                selected = index;
                                spCaseTypeEmpty.setText(temp);
                                pos = selected;
                                item = temp;


                                selectedCaseType = CaseTypeVal.get(selected);
                                llCaseTypeLayout.setVisibility(View.VISIBLE);
                                drawAttributeLayout(CaseTypeVal.get(selected));

                                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                                arrayAdapter =null;
                                caseTypeId.clear();
                                array_case_type.clear();
                                CaseTypeVal.clear();

                                dialogProject.dismiss();

                            }

                        }


                    }
                });

                listview.setOnScrollListener(new AbsListView.OnScrollListener()
                {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
                    {
                        String search = editText.getText().toString();
                        if(search.length()==0)
                        {
                            int lastInScreen = firstVisibleItem + visibleItemCount;
                            if((lastInScreen == totalItemCount) && !(loadingMore))
                            {
                                util.showProgressDialog(CreateCaseActivity.this,"Loading...");
                                getCaseType(pageVal);
                            }
                        }

                    }
                });

                editText.addTextChangedListener(new TextWatcher()
                {
                    public void afterTextChanged(Editable s)
                    {
                        String text = s.toString().toLowerCase(Locale.getDefault());
                        filter(text);
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {

                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        editText.setText("");
                        arrayAdapter =null;
                        caseTypeId.clear();
                        array_case_type.clear();
                        CaseTypeVal.clear();

                        dialogProject.dismiss();
                    }
                });


            }
        } catch (Exception e) {
        }
    }


    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        array_case_type.clear();


        if (charText.length() == 0)
        {
            array_case_type.addAll(arraylist);
        }
        else
        {
            for (String caseString : arraylist)
            {
                if (caseString.toLowerCase(Locale.getDefault())
                        .contains(charText))
                {
                    array_case_type.add(caseString);
                }
            }
        }

        arrayAdapter.notifyDataSetChanged();
    }

    private void updateDialogListView()
    {
        loadingMore = false;
        CaseTypeVal = appPreferences.getCaseType();

        if(array_case_type==null)
        {
            array_case_type = new ArrayList<>();
        }

        for (CaseType ctVal : CaseTypeVal)
        {
            if(!caseTypeId.contains(ctVal.getId()+""))
            {
                array_case_type.add(ctVal.getName());
                caseTypeId.add(ctVal.getId()+"");
            }
        }

        arraylist = new ArrayList<>();
        arraylist.addAll(array_case_type);

        Log.d("","Size info ===> "+array_case_type.size()+" == "+CaseTypeVal.size());

        if(arrayAdapter == null)
        {
            callCaseTypeDialog();
        }
        else
        {
            arrayAdapter.notifyDataSetChanged();
        }




    }


    List<EditText> allEds = null;

    private void drawAttributeLayout(CaseType caseType) {
        llCaseTypeLayout.removeAllViews();
        if (caseType.getCaseTypeAttribute() != null
                && caseType.getCaseTypeAttribute().size() > 0) {
            allEds = new ArrayList<EditText>();
            allEds.clear();
            LinearLayout.LayoutParams mRparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            EditText myEditText = null;
            TextView myText = null;
            for (int i = 0; i < caseType.getCaseTypeAttribute().size(); i++) {

                myText = new TextView(this);
                mRparams.setMargins(10, 10, 10, 10);
                myText.setPadding(10, 10, 10, 10);
                myText.setLayoutParams(mRparams);
                myText.setText(caseType.getCaseTypeAttribute().get(i).getName());
                myText.setTextColor(getResources().getColor(R.color.menutoolbar));
                myText.setTypeface(null, Typeface.BOLD);

                myEditText = new EditText(this);
                mRparams.setMargins(20, 10, 10, 10);
                myEditText.setPadding(30, 10, 15, 10);
                myEditText.setLayoutParams(mRparams);
                myEditText.setTextSize(12.0f);
                myEditText.setHint(caseType.getCaseTypeAttribute().get(i).getName());
                myEditText.setId(i);
                myEditText.setTextColor(getResources().getColor(R.color.black));
                myEditText.setBackgroundResource(R.color.transparent);
                myEditText.setHintTextColor(getResources().getColor(R.color.back_menu));
                allEds.add(myEditText);
                llCaseTypeLayout.addView(myText);
                llCaseTypeLayout.addView(myEditText);
            }

        } else {
            util.makeAlertdiallog(this, "No attribute is available!");
        }
    }

    private void saveIssueIntoDataBase() {
        if (etDescription.getText().toString().length() > 0) {
            if ((latitude > 0.0) && (longitude > 0.0)) {
                if (isPrioritySelected) {
                    if (isCaseTypeSelected) {
                        btSave.setEnabled(false);
                        CaseRequest caseRequest = new CaseRequest();
                        if (address != null) {
                            caseRequest.setAddress(address);
                        } else {
                            caseRequest.setAddress("");
                        }
                        caseRequest.setPinLat(latitude);
                        caseRequest.setPinLong(longitude);

                        if (cbEscalated.isChecked()) {
                            caseRequest.setEscalated(true);
                        } else {
                            caseRequest.setEscalated(false);
                        }

                        if (spin_val_priority.equalsIgnoreCase("LOW")) {
                            caseRequest.setPriority("LOW");
                        } else if (spin_val_priority.equalsIgnoreCase("MEDIUM")) {
                            caseRequest.setPriority("MEDIUM");
                        } else if (spin_val_priority.equalsIgnoreCase("HIGH")) {
                            caseRequest.setPriority("HIGH");
                        } else if (spin_val_priority.equalsIgnoreCase("CRITICAL")) {
                            caseRequest.setPriority("CRITICAL");
                        }

                        caseRequest.setDescription(etDescription.getText().toString());
                        if (selectedCaseType != null) {
                            caseRequest.setCaseType(selectedCaseType);
                            ArrayList<CaseTypeAttributeValues> caseValue = new ArrayList<>();
                            for (int i = 0; i < selectedCaseType.getCaseTypeAttribute().size(); i++) {
                                CaseTypeAttributeValues caseTypeAttributeValues = new CaseTypeAttributeValues();
                                caseTypeAttributeValues.setCaseTypeAttribute(selectedCaseType.getCaseTypeAttribute().get(i));
                                caseTypeAttributeValues.setAttributeValue(allEds.get(i).getText().toString());
                                caseValue.add(caseTypeAttributeValues);

                            }

                            // All CaseTypeAttribute is Added to caseRequest
                            caseRequest.setCaseTypeAttributeValues(caseValue);
                        }

                        if (isImageSelected) {
                            try {
                                Bitmap bitmap = ((BitmapDrawable) ivImageUpload.getDrawable()).getBitmap();
                                if (bitmap != null) {
                                    ArrayList<CaseImages> caseImage = new ArrayList<>();
                                    CaseImages cas = new CaseImages();
                                    cas.setImage(convertintobase64(bitmap));
                                    caseImage.add(cas);
                                    caseRequest.setCaseImages(caseImage);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        util.showProgressDialog(this, "posting your request!!");
                        ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
                        Call<Case> call = apiInterface.postUserCase(caseRequest);
                        call.enqueue(new Callback<Case>() {
                            @Override
                            public void onResponse(Call<Case> call, Response<Case> response) {
                                try {
                                    btSave.setEnabled(true);
                                    util.hideProgressDialog();
                                    if (response.code() == 200 || response.code() == 201) {
                                        util.showToast(CreateCaseActivity.this, "Case created successfully!!");
                                    } else if (response.code() == 401) {
                                        util.callLoginScreen(CreateCaseActivity.this);
                                        finish();
                                    } else if (response.code() == 403)
                                    {
                                        util.showToast(getApplicationContext(),"* 403 Forbidden");
                                    } else
                                    {
                                        util.showToast(getApplicationContext(),"* Something bad happened");

                                    }
                                    startActivity(new Intent(getApplicationContext(), CasesActivity.class));
                                    CreateCaseActivity.this.finish();
                                } catch (Exception e) {
                                    btSave.setEnabled(true);
                                }
                            }

                            @Override
                            public void onFailure(Call<Case> call, Throwable t) {
                                showError("Something bad happened");

                            }
                        });
                    } else {
                        showError("Please Select Case Type");
                    }
                } else {
                    showError("Please Select Priority");
                }
            } else {
                showError("Location should not be empty");
            }
        } else {
            showError("Please enter description");
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

    private void showError(String s) {
        util.hideProgressDialog();
        btSave.setEnabled(true);
        util.makeAlertdiallog(this, s);

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        address = getAddress(latitude, longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            if (obj.getCountryName() != null) {
                add = add + ", " + obj.getCountryName();
            }
            if (obj.getCountryCode() != null) {
                add = add + ", " + obj.getCountryCode();
            }
            if (obj.getAdminArea() != null) {
                add = add + ", " + obj.getAdminArea();
            }
            if (obj.getSubAdminArea() != null) {
                add = add + ", " + obj.getSubAdminArea();
            }
            if (obj.getLocality() != null) {
                add = add + ", " + obj.getLocality();
            }

            return add;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.uploadImage:
                selectImage();
                break;
            default:
                break;
        }
    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
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
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                try {
                    File f = new File(Environment.getExternalStorageDirectory().toString());
                    for (File temp : f.listFiles()) {
                        if (temp.getName().equals("temp.jpg")) {
                            f = temp;
                            break;
                        }
                    }

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
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
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
                    util.makeAlertdiallog(CreateCaseActivity.this, "not able to fetch image. Please try again!!");
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
                while (thumbnail.getHeight() > 1024 || thumbnail.getWidth() > 1024) {
                    thumbnail = getResizedBitmap(thumbnail);
                }
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


    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        String temp = null;
        try {
            int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            scaled.compress(Bitmap.CompressFormat.PNG, 90, stream);
            byte[] imageByte = stream.toByteArray();
            temp = Base64.encodeToString(imageByte, Base64.NO_WRAP);
            return temp;

        } catch (Exception c) {
            return temp;
        }


    }


    public static Bitmap decodeBase64(String input, int i) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);

    }

    private void getCaseType(final int currentPage)
    {
        if (Network_Available.hasConnection(this))
        {

            loadingMore = true;
            ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
            Call<ArrayList<CaseType>> call = apiInterface.getCaseTypes(currentPage, 20, AppConstants.queryParameterDesc);
            call.enqueue(new Callback<ArrayList<CaseType>>() {
                @Override
                public void onResponse(Call<ArrayList<CaseType>> call, Response<ArrayList<CaseType>> response)
                {

                    try {
                        if (response.code() == 200 || response.code() == 201)
                        {
                            ArrayList<CaseType> resList = new ArrayList<>();

                            resList =  response.body();

                            if(resList.size()>0)
                            {
                                if(appPreferences.getCaseType()!=null)
                                {
                                    CaseTypeVal = appPreferences.getCaseType();
                                }

                                CaseTypeVal.addAll(response.body());
                                appPreferences.setCaseType(CaseTypeVal);
                                pageVal = currentPage + 1;
                                updateDialogListView();
                            }


                        } else if (response.code() == 401) {
                            util.callLoginScreen(getApplicationContext());
                            finish();
                        } else if (response.code() == 403)
                        {
                            util.showToast(getApplicationContext(),"* 403 Forbidden");
                        } else
                        {
                            util.showToast(getApplicationContext(),"* Something bad happened");

                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<CaseType>> call, Throwable t) {

                }
            });
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
