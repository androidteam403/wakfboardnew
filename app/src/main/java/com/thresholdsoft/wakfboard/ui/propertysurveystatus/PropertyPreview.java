package com.thresholdsoft.wakfboard.ui.propertysurveystatus;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.ActivityPropertySurveyStatusBinding;
import com.thresholdsoft.wakfboard.ui.base.BaseActivity;
import com.thresholdsoft.wakfboard.ui.mapdataliastactivity.MapDataListActivity;
import com.thresholdsoft.wakfboard.ui.propertysurvey.PropertySurvey;
import com.thresholdsoft.wakfboard.ui.propertysurvey.bottomsheet.PropertySurveyBottomSheet;
import com.thresholdsoft.wakfboard.ui.propertysurvey.model.MapDataTable;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PropertyPreview extends BaseActivity implements PropertySurveyStatusMvpView, OnMapReadyCallback {
    @Inject
    PropertySurveyStatusMvpPresenter<PropertySurveyStatusMvpView> mpresenter;
    ActivityPropertySurveyStatusBinding activityPropertySurveyStatusBinding;
    private PropertySurveyBottomSheet bottomSheet;
    FusedLocationProviderClient fusedLocationProviderClient;
    private int mapTypeData;
    private static final int PROPERTY_SURVEY = 545;
    private static final int MAP_DATA_LIST = 123;
    private static final int REQUEST_CODE = 121;
    Location currentLocation;
    private GoogleMap mMap;
    private int propertyId;
    List<MapDataTable> mapDataTableList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPropertySurveyStatusBinding = DataBindingUtil.setContentView(this, R.layout.activity_property_survey_status);
        getActivityComponent().inject(this);
        mpresenter.onAttach(PropertyPreview.this);
        setUp();
    }

    @Override
    protected void setUp() {
        if (getIntent() != null) {
            propertyId = (Integer) getIntent().getIntExtra("propertyId", 0);
        }

        mpresenter.getMapTypelist(propertyId);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

        activityPropertySurveyStatusBinding.plusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet();
            }
        });

        if (mapDataTableList != null && mapDataTableList.size() > 0) {
            activityPropertySurveyStatusBinding.mapViewListIcon.setVisibility(View.VISIBLE);
            activityPropertySurveyStatusBinding.areaCalLay.setVisibility(View.VISIBLE);
        } else {
            activityPropertySurveyStatusBinding.mapViewListIcon.setVisibility(View.GONE);
            activityPropertySurveyStatusBinding.areaCalLay.setVisibility(View.GONE);
        }

        activityPropertySurveyStatusBinding.mapViewListIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                String myJson = gson.toJson(mapDataTableList);

                startActivityForResult(MapDataListActivity.getStartIntent(PropertyPreview.this, propertyId, myJson), MAP_DATA_LIST);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

    }
//    LinearLayout mapFrameLayout;
//    public void fullMap() {
////        ImageView collapse = includedLayout.findViewById(R.id.collapseView);
//         mapFrameLayout = (LinearLayout) findViewById(R.id.mapFrameLayout);
//        activityPropertySurveyStatusBinding.expandView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LinearLayout.LayoutParams fullMapParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//                activityPropertySurveyStatusBinding.farmerdata.setVisibility(View.GONE);
//                mapFrameLayout.setLayoutParams(fullMapParams);
//                activityPropertySurveyStatusBinding.expandView.setVisibility(View.GONE);
//                activityPropertySurveyStatusBinding.collapseView.setVisibility(View.VISIBLE);
//                //  previewDisplay(surveyDetailsAdapter.getListData());
//            }
//        });
//        activityPropertySurveyStatusBinding.collapseView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LinearLayout.LayoutParams fullMapParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//                activityPropertySurveyStatusBinding.farmerdata.setVisibility(View.VISIBLE);
//                fullMapParams.height = 900;
//                mapFrameLayout.setLayoutParams(fullMapParams);
//
//                activityPropertySurveyStatusBinding.expandView.setVisibility(View.VISIBLE);
//                activityPropertySurveyStatusBinding.collapseView.setVisibility(View.GONE);
//                //  previewDisplay(surveyDetailsAdapter.getListData());
//            }
//        });
//    }

    private void openBottomSheet() {
        bottomSheet = new PropertySurveyBottomSheet();
        bottomSheet.setPresenterData(this);
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }

    @Override
    public void anotherizedToken() {

    }

    @Override
    public void mapTypeData(int mapData) {
        mapTypeData = mapData;
        startActivityForResult(PropertySurvey.getStartIntent(PropertyPreview.this, mapData, propertyId), PROPERTY_SURVEY);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void getMapDataTable(List<MapDataTable> mapDataTableList1) {
        if (mapDataTableList != null && mapDataTableList.size() > 0) {
            for (int i = 0; i < mapDataTableList.size(); i++) {
                mapDataTableList1.get(i).setChecked(mapDataTableList.get(i).isChecked());
            }
            this.mapDataTableList = mapDataTableList1;
        } else {
            this.mapDataTableList = mapDataTableList1;
        }
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
//                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();

                    Toast toast = Toast.makeText(PropertyPreview.this, currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT);
                    toast.getView().setBackground(getResources().getDrawable(R.drawable.toast_bg));
                    TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Typeface typeface = Typeface.createFromAsset(getApplication().getAssets(), "font/roboto_bold.ttf");
                        text.setTypeface(typeface);
                        text.setTextColor(Color.WHITE);
                        text.setTextSize(14);
                    }
                    toast.show();

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.preview_map);
                    mapFragment.getMapAsync(PropertyPreview.this);
                }
            }
        });
    }

    private List<LatLng> getPointLatlngList = new ArrayList<>();
    private List<LatLng> getPolylineLatlngList = new ArrayList<>();
    private List<LatLng> getPolygontLatlngList = new ArrayList<>();

    private LatLng latLngLine;
    Polyline polyline = null;
    Polygon polygon = null;
    List<Polyline> polylineList = new ArrayList<>();
    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();
    Marker polyLineMarker;

    private void getPolyLineList(GoogleMap googleMap) {
        mMap = googleMap;
        if (googleMap!=null) {
            googleMap.clear();
        }
        if (mapDataTableList != null && mapDataTableList.size() > 0) {
            for (MapDataTable mapDataTable : mapDataTableList) {
                if (mapDataTable.getMapType() == 1 && mapDataTable.isChecked()) {
                    getPointLatlngList.clear();
                    getPointLatlngList.addAll(mapDataTable.getLatLngList());
                    for (int i = 0; i < getPointLatlngList.size(); i++) {
                        latLngLine = new LatLng(getPointLatlngList.get(i).latitude, getPointLatlngList.get(i).longitude);
                        MarkerOptions markerOptions = new MarkerOptions().position(latLngLine).title(name);
                        polyLineMarker = mMap.addMarker(markerOptions);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLngLine));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngLine, 7));
                    }
                } else if (mapDataTable.getMapType() == 2 && mapDataTable.isChecked()) {
                    BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.marker_yellow_icon);
                    getPolylineLatlngList.clear();
                    getPolylineLatlngList.addAll(mapDataTable.getLatLngList());
                    for (int i = 0; i < getPolylineLatlngList.size(); i++) {
                        latLngLine = new LatLng(getPolylineLatlngList.get(i).latitude, getPolylineLatlngList.get(i).longitude);
                        MarkerOptions markerOptions = new MarkerOptions().position(latLngLine).icon(icon2);
                        polyLineMarker = mMap.addMarker(markerOptions);
                    }
                    LatLng from = new LatLng(((getPolylineLatlngList.get(0).latitude)), ((getPolylineLatlngList.get(0).longitude)));
                    LatLng to = new LatLng(((getPolylineLatlngList.get(getPolylineLatlngList.size() - 1).latitude)), ((getPolylineLatlngList.get(getPolylineLatlngList.size() - 1).longitude)));

                    double amount = Double.parseDouble(mpresenter.getLineLength(from, to));
                    DecimalFormat formatter = new DecimalFormat("#,###");
                    String formatted = formatter.format(amount);

                    activityPropertySurveyStatusBinding.distanceTextView.setText("Length :" + formatted+"m");

                    PolylineOptions polylineOptions = new PolylineOptions().addAll(getPolylineLatlngList).color(Color.BLUE).width(5).clickable(true);
                    polyline = mMap.addPolyline(polylineOptions);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(getPolylineLatlngList.get(0)));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getPolylineLatlngList.get(0), 7));

                } else if (mapDataTable.getMapType() == 3 && mapDataTable.isChecked()) {
                    BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.marker_yellow_icon);
                    getPolygontLatlngList.clear();
                    getPolygontLatlngList.addAll(mapDataTable.getLatLngList());
                    for (int i = 0; i < getPolygontLatlngList.size(); i++) {
                        latLngLine = new LatLng(getPolygontLatlngList.get(i).latitude, getPolygontLatlngList.get(i).longitude);
                        MarkerOptions markerOptions = new MarkerOptions().position(latLngLine).icon(icon1);
                        polyLineMarker = mMap.addMarker(markerOptions);
                    }

                    double amount = Double.parseDouble(mpresenter.getPolygonArea(getPolygontLatlngList));
                    DecimalFormat formatter = new DecimalFormat("#,###");
                    String formatted = formatter.format(amount);

                    activityPropertySurveyStatusBinding.polygonArea.setText("Area :" + formatted + "m²");
                    PolygonOptions polygonOptions = new PolygonOptions().addAll(getPolygontLatlngList).strokeWidth(5).fillColor(getResources().getColor(R.color.alpha_ripple_effect_btn_color)).strokeColor(Color.RED).clickable(true);
                    polygon = mMap.addPolygon(polygonOptions);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(getPolygontLatlngList.get(0)));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getPolygontLatlngList.get(0), 7));
                }

            }

        } else {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
            googleMap.addMarker(markerOptions);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        getPolyLineList(googleMap);
    }

    String name;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PROPERTY_SURVEY:
                    if (data != null) {
                        String dialogName = (String) data.getSerializableExtra("dialogName");
                        name = dialogName;
                        mpresenter.getMapTypelist(propertyId);
                        getPolyLineList(mMap);
                        if (mapDataTableList != null && mapDataTableList.size() > 0) {
                            activityPropertySurveyStatusBinding.mapViewListIcon.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case MAP_DATA_LIST:
                    if (data != null) {

                        Gson gson = new Gson();
                        String json = data.getStringExtra("mapDataTableListUnchecked");
                        Type type = new TypeToken<List<MapDataTable>>() {
                        }.getType();
                        if (mapDataTableList != null) {
                            mapDataTableList.clear();
                        }
                        mapDataTableList = gson.fromJson(json, type);

//                        mapDataTableList = (List<MapDataTable>) data.getSerializableExtra("mapDataTableListUnchecked");
                        getPolyLineList(mMap);
                    }
                    break;
                default:
            }
        }

    }
}
