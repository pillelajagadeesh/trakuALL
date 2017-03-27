package com.agent.trakeye.tresbu.trakeyeagent.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.model.Asset;
import com.agent.trakeye.tresbu.trakeyeagent.model.AssetCoordinates;
import com.agent.trakeye.tresbu.trakeyeagent.model.AssetType;
import com.agent.trakeye.tresbu.trakeyeagent.prefernces.AppPreferences;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiClient;
import com.agent.trakeye.tresbu.trakeyeagent.rest.ApiInterface;
import com.agent.trakeye.tresbu.trakeyeagent.utils.Network_Available;
import com.agent.trakeye.tresbu.trakeyeagent.utils.util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tresbu on 25-Oct-16.
 */

public class AssetDetailActivity extends FragmentActivity implements OnMapReadyCallback {
    ImageView btEdit;
    RelativeLayout rlBack;
    LinearLayout llAssetTypeLayout;
    TextView tvHeader, tvName, tvDescription, tvAssetType;
    Asset mAsset;
    AppPreferences app;
    private GoogleMap mMap;
    private ArrayList<AssetCoordinates> assetCoordinates;
    private PolylineOptions polyLineOptions;
    private Bitmap bmp;
    private ImageButton btnMyLocation;

    @Override
    protected void onResume() {
        super.onResume();
        calltogetDetail();
    }

    private void calltogetDetail() {

        if (Network_Available.hasConnection(this)) {
            if (mAsset != null) {


                if (ApiClient.getToken_id() == null) {
                    ApiClient.setToken_id(app.getToken());
                }
                util.showProgressDialog(this, "fetching asset details,Please Wait..............");

                ApiInterface apiInterface = ApiClient.getClientWithKey().create(ApiInterface.class);
                Call<Asset> call = apiInterface.getAssetDetail(String.valueOf(mAsset.getId()));
                call.enqueue(new Callback<Asset>() {
                    @Override
                    public void onResponse(Call<Asset> call, Response<Asset> response) {

                        try {
                            if (response.code() == 200 || response.code() == 201) {
                                if (response.body() != null) {
                                    setItemonWidget(response.body());
                                    mAsset = response.body();
                                }
                            } else if (response.code() == 401) {
                                util.callLoginScreen(AssetDetailActivity.this);
                                finish();
                            } else if (response.code() == 403)
                            {
                                util.showToast(getApplicationContext(),"* 403 Forbidden");
                            } else
                            {
                                util.showToast(getApplicationContext(),"* Something bad happened");

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<Asset> call, Throwable t) {
                        util.hideProgressDialog();

                    }
                });
            }
        } else {
            util.makeAlertdiallog(this, "Please check your internet connectivity");
            if (mAsset != null) {
                setItemonWidget(mAsset);
            } else {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        AssetDetailActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assetdetail);
        btEdit = (ImageView) findViewById(R.id.btEdit);
        tvHeader = (TextView) findViewById(R.id.tvHeader);
        tvName = (TextView) findViewById(R.id.tvName);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvAssetType = (TextView) findViewById(R.id.tvAssetType);
        llAssetTypeLayout = (LinearLayout) findViewById(R.id.llAssetTypeLayout);
        btnMyLocation = (ImageButton) findViewById(R.id.btnMyLocation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Add this line
        app = new AppPreferences(this);
        if(!app.getSessionVal()){
            finish();
        }
        rlBack = (RelativeLayout) findViewById(R.id.rlBack);

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AssetDetailActivity.this.finish();

            }
        });

        mAsset = (Asset) getIntent().getSerializableExtra("assets");

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssetDetailActivity.this, EditAssetActivity.class);
                intent.putExtra("assets", mAsset);
                startActivity(intent);
            }
        });

    }

    List<TextView> allEds = null;

    @SuppressWarnings("deprecation")
    private void drawAttributeLayout(AssetType assetType) {
        llAssetTypeLayout.removeAllViews();
        if (assetType.getAssetTypeAttributes() != null
                && assetType.getAssetTypeAttributes().size() > 0) {
            allEds = new ArrayList<>();
            allEds.clear();
            LinearLayout.LayoutParams mRparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams mRparams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView myEditText = null;
            TextView myText = null;

            for (int i = 0; i < assetType.getAssetTypeAttributes().size(); i++) {

                myText = new TextView(this);
                mRparams.setMargins(10, 10, 10, 10);
                myText.setPadding(10, 10, 10, 10);
                myText.setLayoutParams(mRparams);
                myText.setText(assetType.getAssetTypeAttributes().get(i).getName());
                myText.setTextColor(getResources().getColor(R.color.menutoolbar));
                myText.setTypeface(null, Typeface.BOLD);

                myEditText = new TextView(this);
                mRparams1.setMargins(20, 2, 10, 30);
                myEditText.setPadding(30, 2, 15, 10);
                myEditText.setLayoutParams(mRparams1);
                myEditText.setTextSize(12.0f);
                myEditText.setId(i);
                myEditText.setTextColor(getResources().getColor(R.color.black));
                myEditText.setBackgroundResource(R.color.transparent);
                myEditText.setHintTextColor(getResources().getColor(R.color.back_menu));
                myEditText.setText(mAsset.getAssetTypeAttributeValues().get(i).getAttributeValue());
                allEds.add(myEditText);
                llAssetTypeLayout.addView(myText);
                llAssetTypeLayout.addView(myEditText);
            }

        } else {
            util.makeAlertdiallog(this, "No attribute is available!");
        }
    }

    @SuppressWarnings("deprecation")
    public void setItemonWidget(Asset mAsset) {
        util.hideProgressDialog();
        if (mAsset != null) {
            tvHeader.setText(getResources().getString(R.string._asset) + " " + mAsset.getId());
            if (mAsset.getName() != null) {
                tvName.setText(mAsset.getName());
            }
            if (mAsset.getDescription() != null) {
                tvDescription.setText(mAsset.getDescription());
            }

            drawAttributeLayout(mAsset.getAssetType());
            if (mAsset.getAssetType() != null) {
                tvAssetType.setText(mAsset.getAssetType().getName());
            }
            if (mAsset.getAssetTypeAttributeValues() != null) {
                String temp = mAsset.getAssetTypeAttributeValues().get(0).getAttributeValue();
            }

            if (mAsset.getAssetCoordinates() != null) {
                assetCoordinates = mAsset.getAssetCoordinates();
                List<LatLng> latlng = new ArrayList<>();
                if (assetCoordinates != null) {
                    if (assetCoordinates.size() > 0) {
                        if (mAsset.getAssetType().getLayout().equals("SPREAD")) {
                            for (int i = 0; i < assetCoordinates.size(); i++) {
                                latlng.add(i, new LatLng(assetCoordinates.get(i).getLatitude(), assetCoordinates.get(i).getLongitude()));
                            }
                            polyLineOptions = new PolylineOptions();
                            polyLineOptions.addAll(latlng);
                            polyLineOptions.width(6);

                            switch (mAsset.getAssetType().getColorcode()) {
                                case "CYAN":
                                    polyLineOptions.color(getResources().getColor(R.color.CYAN));
                                    break;
                                case "BLACK":
                                    polyLineOptions.color(getResources().getColor(R.color.BLACK));
                                    break;
                                case "BLUE":
                                    polyLineOptions.color(getResources().getColor(R.color.BLUE));
                                    break;
                                case "BLUEVIOLET":
                                    polyLineOptions.color(getResources().getColor(R.color.BLUEVIOLET));
                                    break;
                                case "BROWN":
                                    polyLineOptions.color(getResources().getColor(R.color.BROWN));
                                    break;
                                case "CHARTREUSE":
                                    polyLineOptions.color(getResources().getColor(R.color.CHARTREUSE));
                                    break;
                                case "CRIMSON":
                                    polyLineOptions.color(getResources().getColor(R.color.CRIMSON));
                                    break;
                                case "YELLOW":
                                    polyLineOptions.color(getResources().getColor(R.color.YELLOW));
                                    break;
                                case "MAGENTA":
                                    polyLineOptions.color(getResources().getColor(R.color.MAGENTA));
                                    break;
                                case "DEEPPINK":
                                    polyLineOptions.color(getResources().getColor(R.color.DEEPPINK));
                                    break;
                                case "LIGHTCORAL":
                                    polyLineOptions.color(getResources().getColor(R.color.LIGHTCORAL));
                                    break;
                                default:
                                    polyLineOptions.color(Color.BLUE);
                                    break;
                            }
                            mMap.addPolyline(polyLineOptions);

                            // Add a marker in Sydney and move the camera
                            LatLng latLngVal = new LatLng(assetCoordinates.get(0).getLatitude(), assetCoordinates.get(0).getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngVal));
                            mMap.getUiSettings().setZoomControlsEnabled(true);
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
                        } else {
                            if (mAsset.getAssetType().getImage() != null) {
                                // To place image on all coordinates of "Fixed" layout
                                for (int i = 0; i < assetCoordinates.size(); i++) {
                                    mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(assetCoordinates.get(i).getLatitude(), assetCoordinates.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(getResizedBitmap(decodeBase64(mAsset.getAssetType().getImage()), 150))));
                                }
                                // Zoom Map Camera to first LatLng
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(assetCoordinates.get(0).getLatitude(), assetCoordinates.get(0).getLongitude())));
                                mMap.getUiSettings().setZoomControlsEnabled(true);
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
                            }
                        }
                        btnMyLocation.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (assetCoordinates.get(0).getLatitude() > 0.0) {
                                    // Zoom Map Camera to first LatLng
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(assetCoordinates.get(0).getLatitude(), assetCoordinates.get(0).getLongitude())));
                                    mMap.getUiSettings().setZoomControlsEnabled(true);
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
                                }
                            }
                        });
                    } else {
                        util.makeAlertdiallog(this, "No Point Are Available");
                    }
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    // This method is to resize the Bitmap Image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    // This method is to decode Base64Image to BitmapImage
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
