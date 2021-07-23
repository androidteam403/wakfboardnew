package com.thresholdsoft.wakfboard.ui.propertysurveystatus;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
import com.thresholdsoft.wakfboard.ui.gallery.GalleryActivity;
import com.thresholdsoft.wakfboard.ui.mapdataliastactivity.MapDataListActivity;
import com.thresholdsoft.wakfboard.ui.propertycreation.PropertyCreation;
import com.thresholdsoft.wakfboard.ui.propertycreation.model.PropertyData;
import com.thresholdsoft.wakfboard.ui.propertysurvey.PropertySurvey;
import com.thresholdsoft.wakfboard.ui.propertysurvey.bottomsheet.PropertySurveyBottomSheet;
import com.thresholdsoft.wakfboard.ui.propertysurvey.model.MapDataTable;
import com.thresholdsoft.wakfboard.ui.propertysurveystatus.adapter.MaptypeListAdapter;
import com.thresholdsoft.wakfboard.utils.CommonUtils;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

@RequiresApi(api = Build.VERSION_CODES.M)
public class PropertyPreview extends BaseActivity implements PropertySurveyStatusMvpView, OnMapReadyCallback, GoogleMap.OnMapClickListener {
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
    private String measurements;
    private PropertyData propertyData;
    private String propertyName;
    private int id;
    private ArrayList<MapTypeModel> mapTypeModelArrayList;
    private MaptypeListAdapter maptypeListAdapter;
    private int colorId=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPropertySurveyStatusBinding = DataBindingUtil.setContentView(this, R.layout.activity_property_survey_status);
        getActivityComponent().inject(this);
        mpresenter.onAttach(PropertyPreview.this);
        closeMapTypeViewList();
        maptypeList();
//        onMapClick();
        setUp();
//        statusCheck();
    }

    private void closeMapTypeViewList() {
        activityPropertySurveyStatusBinding.containerWrapper.setOnTouchListener((v, event) -> {
            if (isMapTypeView) {
                Animation slideUp = AnimationUtils.loadAnimation(PropertyPreview.this, R.anim.bottom_down);
                activityPropertySurveyStatusBinding.maptypeListLayout.setVisibility(View.INVISIBLE);
                activityPropertySurveyStatusBinding.maptypeListLayout.startAnimation(slideUp);
                isMapTypeView = false;
            }
            return false;
        });
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            setUp();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void setUp() {
        activityPropertySurveyStatusBinding.setCallback(mpresenter);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.preview_map);
        mapFragment.getMapAsync(PropertyPreview.this);
        if (getIntent() != null) {
            propertyData = (PropertyData) getIntent().getSerializableExtra(PropertyCreation.PROPERTY_DATA_KEY);
            activityPropertySurveyStatusBinding.headerTittle.setText(propertyData.getPropertyName());
            propertyId = propertyData.getId();
            measurements = (String) getIntent().getStringExtra("measurements");
            propertyName = (String) getIntent().getStringExtra("propertyName");
            id = (int) getIntent().getIntExtra("id", 0);
        }
        mpresenter.getMapTypelist(propertyId);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
        activityPropertySurveyStatusBinding.plusImage.setOnClickListener(v -> openBottomSheet());

        if (mapDataTableList != null && mapDataTableList.size() > 0) {
            activityPropertySurveyStatusBinding.mapViewListIcon.setVisibility(View.VISIBLE);
            activityPropertySurveyStatusBinding.areaCalLay.setVisibility(View.VISIBLE);
        } else {
            activityPropertySurveyStatusBinding.mapViewListIcon.setVisibility(View.GONE);
            activityPropertySurveyStatusBinding.areaCalLay.setVisibility(View.GONE);
        }

        activityPropertySurveyStatusBinding.mapViewListIcon.setOnClickListener(v -> {
            Gson gson = new Gson();
            String myJson = gson.toJson(mapDataTableList);

            startActivityForResult(MapDataListActivity.getStartIntent(PropertyPreview.this, propertyId, myJson), MAP_DATA_LIST);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        });

//        activityPropertySurveyStatusBinding.propertyName.setText(propertyName);

        activityPropertySurveyStatusBinding.currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLocation != null) {
                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                    mMap.addMarker(markerOptions);
                }
            }
        });
        if (mapDataTableList != null && mapDataTableList.size() > 0) {
            activityPropertySurveyStatusBinding.lastUpdateDate.setVisibility(View.VISIBLE);
            activityPropertySurveyStatusBinding.lastUpdateDate.setText("Last updated date: " + mapDataTableList.get(0).getLastUpdatedDate());
        }

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
        startActivityForResult(PropertySurvey.getStartIntent(PropertyPreview.this, mapData, propertyId, true, measurements), PROPERTY_SURVEY);
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

    @Override
    public void onClickBack() {
        onBackPressed();
    }

    @Override
    public void onClickGallery() {
        startActivity(GalleryActivity.getStartIntent(this, propertyId, id));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    }

    @Override
    public void onClickPropertyEdit() {
        startActivityForResult(PropertyCreation.getStartIntent(this, propertyData, true), PropertyCreation.ACTIVITY_ID);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private boolean isMapTypeView = false;

    @Override
    public void onClickMapIcon() {
        if (mapTypeModelArrayList != null && mapTypeModelArrayList.size() > 0) {
            maptypeListAdapter = new MaptypeListAdapter(this, mapTypeModelArrayList, this);
            RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            activityPropertySurveyStatusBinding.maptypeListRecy.setLayoutManager(mLayoutManager2);
            activityPropertySurveyStatusBinding.maptypeListRecy.setItemAnimator(new DefaultItemAnimator());
            activityPropertySurveyStatusBinding.maptypeListRecy.setAdapter(maptypeListAdapter);
            activityPropertySurveyStatusBinding.maptypeListRecy.setNestedScrollingEnabled(false);
            if (!isMapTypeView) {
                Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.bottom_up);
                activityPropertySurveyStatusBinding.maptypeListLayout.setVisibility(View.VISIBLE);
                activityPropertySurveyStatusBinding.maptypeListLayout.startAnimation(slideUp);
                isMapTypeView = true;
            }
        }
    }

    @Override
    public void onItemClick(int pos) {
        mpresenter.saveMapViewType(mapTypeModelArrayList.get(pos).getMapType());
        setMapType(mapTypeModelArrayList.get(pos).getMapType());
        if (mMap != null)
            for (int i = 0; i < mapTypeModelArrayList.size(); i++) {
                mapTypeModelArrayList.get(i).setSelected(i == pos);
            }
        maptypeListAdapter.notifyDataSetChanged();
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.bottom_down);
        activityPropertySurveyStatusBinding.maptypeListLayout.setVisibility(View.INVISIBLE);
        activityPropertySurveyStatusBinding.maptypeListLayout.startAnimation(slideUp);
        isMapTypeView = false;
    }

    private void setMapType(String mapViewType) {
        if (mMap != null) {
            switch (mapViewType) {
                case "Satelite":
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    break;
                case "Hybrid":
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    break;
                case "Terrain":
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    break;
                case "":
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                case "Normal":
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                default:
            }
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
//                    Toast toast = Toast.makeText(PropertyPreview.this, currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT);
//                    toast.getView().setBackground(getResources().getDrawable(R.drawable.toast_bg));
//                    TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        Typeface typeface = Typeface.createFromAsset(getApplication().getAssets(), "font/roboto_bold.ttf");
//                        text.setTypeface(typeface);
//                        text.setTextColor(Color.WHITE);
//                        text.setTextSize(14);
//                    }
//                    toast.show();

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
    Marker polyLineMarker;

    private void getPolyLineList(GoogleMap googleMap) {
        double i1 = 0.0;
        double polygoni1 = 0.0;
        mMap = googleMap;
//        mMap.setOnMapClickListener(this);
        onMapClick();
        if (mpresenter.getMapViewType() != null && !mpresenter.getMapViewType().isEmpty()) {
            setMapType(mpresenter.getMapViewType());
        }
        if (googleMap != null) {
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
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngLine, 18));
                    }
                } else if (mapDataTable.getMapType() == 2 && mapDataTable.isChecked()) {
                    BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.marker_yellow_icon);
                    getPolylineLatlngList.clear();
                    getPolylineLatlngList.addAll(mapDataTable.getLatLngList());
                    for (int i = 0; i < getPolylineLatlngList.size(); i++) {
                        if (i == getPolylineLatlngList.size() - 1) {
                            LatLng latLng1 = new LatLng((getPolylineLatlngList.get(i).latitude + getPolylineLatlngList.get(0).latitude) / 2, (getPolylineLatlngList.get(i).longitude + getPolylineLatlngList.get(0).longitude) / 2);

                        } else {
                            LatLng latLng1 = new LatLng((getPolylineLatlngList.get(i).latitude + getPolylineLatlngList.get(i + 1).latitude) / 2, (getPolylineLatlngList.get(i).longitude + getPolylineLatlngList.get(i + 1).longitude) / 2);

                            LatLng from = new LatLng(((getPolylineLatlngList.get(i).latitude)), ((getPolylineLatlngList.get(i).longitude)));
                            LatLng to = new LatLng(((getPolylineLatlngList.get(i + 1).latitude)), ((getPolylineLatlngList.get(i + 1).longitude)));

                            double amount = Double.parseDouble(mpresenter.getLineLength(from, to));
                            DecimalFormat formatter = new DecimalFormat("#,###");
                            String formatted = formatter.format(amount);

//                            addText(getApplicationContext(), mMap, latLng1, formatted, 3, 16, Color.RED);

                            i1 += Double.parseDouble(mpresenter.getLineLength(from, to));

                            double amount1 = (i1);
                            DecimalFormat formatter1 = new DecimalFormat("#,###.00");
                            String formatted1 = formatter1.format(amount1);

                            activityPropertySurveyStatusBinding.distanceTextView.setText("Length:" + " " + formatted1 + " m");

                        }
//                        latLngLine = new LatLng(getPolylineLatlngList.get(i).latitude, getPolylineLatlngList.get(i).longitude);
//                        MarkerOptions markerOptions = new MarkerOptions().position(latLngLine).icon(icon2);
//                        polyLineMarker = mMap.addMarker(markerOptions);
                    }

                    PolylineOptions polylineOptions = new PolylineOptions().addAll(getPolylineLatlngList).color(getResources().getColor(R.color.colorPrimaryDark)).width(5).clickable(true);
                    polyline = mMap.addPolyline(polylineOptions);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(getPolylineLatlngList.get(0)));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getPolylineLatlngList.get(0), 18));

                } else if (mapDataTable.getMapType() == 3 && mapDataTable.isChecked()) {
                    BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.marker_yellow_icon);
                    getPolygontLatlngList.clear();
                    getPolygontLatlngList.addAll(mapDataTable.getLatLngList());
                    for (int i = 0; i < getPolygontLatlngList.size(); i++) {
                        if (i == getPolygontLatlngList.size() - 1) {
                            LatLng latLng1 = new LatLng((getPolygontLatlngList.get(i).latitude + getPolygontLatlngList.get(0).latitude) / 2, (getPolygontLatlngList.get(i).longitude + getPolygontLatlngList.get(0).longitude) / 2);

                            LatLng from = new LatLng(((getPolygontLatlngList.get(i).latitude)), ((getPolygontLatlngList.get(i).longitude)));
                            LatLng to = new LatLng(((getPolygontLatlngList.get(0).latitude)), ((getPolygontLatlngList.get(0).longitude)));

                            double amount = Double.parseDouble(mpresenter.getLineLength(from, to));
                            DecimalFormat formatter = new DecimalFormat("#,###");
                            String formatted = formatter.format(amount);

//                            addText(getApplicationContext(), mMap, latLng1, formatted, 3, 16, Color.RED);
                        } else {
                            LatLng latLng1 = new LatLng((getPolygontLatlngList.get(i).latitude + getPolygontLatlngList.get(i + 1).latitude) / 2, (getPolygontLatlngList.get(i).longitude + getPolygontLatlngList.get(i + 1).longitude) / 2);

                            LatLng from = new LatLng(((getPolygontLatlngList.get(i).latitude)), ((getPolygontLatlngList.get(i).longitude)));
                            LatLng to = new LatLng(((getPolygontLatlngList.get(i + 1).latitude)), ((getPolygontLatlngList.get(i + 1).longitude)));

                            double amount = Double.parseDouble(mpresenter.getLineLength(from, to));
                            DecimalFormat formatter = new DecimalFormat("#,###");
                            String formatted = formatter.format(amount);

//                            addText(getApplicationContext(), mMap, latLng1, formatted, 3, 16, Color.RED);
                        }
//                        latLngLine = new LatLng(getPolygontLatlngList.get(i).latitude, getPolygontLatlngList.get(i).longitude);
//                        MarkerOptions markerOptions = new MarkerOptions().position(latLngLine).icon(icon1);
//                        polyLineMarker = mMap.addMarker(markerOptions);
                    }

                    if (measurements.equalsIgnoreCase("Square Meters")) {

                        polygoni1 += Double.parseDouble(mpresenter.getPolygonAreainMeters(getPolygontLatlngList));

                        double amount = polygoni1;
                        DecimalFormat formatter = new DecimalFormat("#,###");
                        String formatted = formatter.format(amount);
                        mpresenter.updateAreaByPropertyId(propertyId, formatted);
                        activityPropertySurveyStatusBinding.polygonArea.setText("Area:" + " " + formatted + " m²");
                    } else if (measurements.equalsIgnoreCase("Square Feet")) {

                        polygoni1 += Double.parseDouble(mpresenter.getPolygonAreainSquareFeet(getPolygontLatlngList));

                        double amount = polygoni1;
                        DecimalFormat formatter = new DecimalFormat("#,###");
                        String formatted = formatter.format(amount);
                        mpresenter.updateAreaByPropertyId(propertyId, formatted);

                        activityPropertySurveyStatusBinding.polygonArea.setText("Area:" + " " + formatted + " sq ft²");
                    } else if (measurements.equalsIgnoreCase("Square yards")) {

                        polygoni1 += Double.parseDouble(mpresenter.getPolygonAreainSquareFeet(getPolygontLatlngList));

                        double amount = polygoni1;
                        DecimalFormat formatter = new DecimalFormat("#,###");
                        String formatted = formatter.format(amount);
                        mpresenter.updateAreaByPropertyId(propertyId, formatted);

                        activityPropertySurveyStatusBinding.polygonArea.setText("Area:" + " " + formatted + " sq yd²");
                    } else if (measurements.equalsIgnoreCase("Acres")) {

                        polygoni1 += Double.parseDouble(mpresenter.getPolygonAreainAcers(getPolygontLatlngList));

                        double amount = polygoni1;
                        DecimalFormat formatter = new DecimalFormat("#,###");
                        String formatted = formatter.format(amount);
                        mpresenter.updateAreaByPropertyId(propertyId, formatted);

                        activityPropertySurveyStatusBinding.polygonArea.setText("Area:" + " " + formatted + " acers");
                    }
                    PolygonOptions polygonOptions = null;
                    if (mapDataTable.getId() == 1) {
                        polygonOptions = new PolygonOptions().addAll(getPolygontLatlngList).strokeWidth(5).strokeColor(getResources().getColor(R.color.colorPrimaryDark)).fillColor(getResources().getColor(R.color.yellow_transparent)).clickable(true);
                    } else if (mapDataTable.getId() == 2) {
                        polygonOptions = new PolygonOptions().addAll(getPolygontLatlngList).strokeWidth(5).strokeColor(getResources().getColor(R.color.colorPrimaryDark)).fillColor(getResources().getColor(R.color.red_transparent)).clickable(true);
                    } else if (mapDataTable.getId() == 3) {
                        polygonOptions = new PolygonOptions().addAll(getPolygontLatlngList).strokeWidth(5).strokeColor(getResources().getColor(R.color.colorPrimaryDark)).fillColor(getResources().getColor(R.color.green_transparent)).clickable(true);
                    } else if (mapDataTable.getId() == 4) {
                        polygonOptions = new PolygonOptions().addAll(getPolygontLatlngList).strokeWidth(5).strokeColor(getResources().getColor(R.color.colorPrimaryDark)).fillColor(getResources().getColor(R.color.blue_transparent)).clickable(true);
                    } else if (mapDataTable.getId() == 5) {
                        polygonOptions = new PolygonOptions().addAll(getPolygontLatlngList).strokeWidth(5).strokeColor(getResources().getColor(R.color.colorPrimaryDark)).fillColor(getResources().getColor(R.color.black_transparent)).clickable(true);
                    } else if (mapDataTable.getId() == 6) {
                        polygonOptions = new PolygonOptions().addAll(getPolygontLatlngList).strokeWidth(5).strokeColor(getResources().getColor(R.color.colorPrimaryDark)).fillColor(getResources().getColor(R.color.pink_transparent)).clickable(true);
                    } else {
                        Random rand = new Random();
//                       int randomElement = CommonUtils.getColorList().get(rand.nextInt(CommonUtils.getColorList().size()));
                        polygonOptions = new PolygonOptions().addAll(getPolygontLatlngList).strokeWidth(5).strokeColor(getResources().getColor(R.color.colorPrimaryDark)).fillColor(CommonUtils.getColorList().get(rand.nextInt(CommonUtils.getColorList().size()))).clickable(true);
                    }
                    polygon = mMap.addPolygon(polygonOptions);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(getPolygontLatlngList.get(0)));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getPolygontLatlngList.get(0), 14));
                }
            }
//             LatLngBounds AUSTRALIA = new LatLngBounds();
//
//// Set the camera to the greatest possible zoom level that includes the
//// bounds
//            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(AUSTRALIA, 0));
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (MapDataTable mapDataTable : mapDataTableList)
                for (LatLng latLng : mapDataTable.getLatLngList())
                    builder.include(new LatLng(latLng.latitude, latLng.longitude));
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 14));
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 14));


        } else {
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.blue_dot);
            if (currentLocation != null) {
                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(icon).title("I am here!");
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                googleMap.addMarker(markerOptions);
            }
        }
    }

    public Marker addText(final Context context, final GoogleMap map,
                          final LatLng location, final String text, final int padding,
                          final int fontSize, int color) {
        Marker marker = null;

        if (context == null || map == null || location == null || text == null
                || fontSize <= 0) {
            return marker;
        }

        final TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(fontSize);
        textView.setTextColor(color);

        final Paint paintText = textView.getPaint();

        final Rect boundsText = new Rect();
        paintText.getTextBounds(text, 0, textView.length(), boundsText);
        paintText.setTextAlign(Paint.Align.CENTER);

        final Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        final Bitmap bmpText = Bitmap.createBitmap(boundsText.width() + 2
                * padding, boundsText.height() + 2 * padding, conf);

        final Canvas canvasText = new Canvas(bmpText);
        paintText.setColor(Color.BLACK);

        canvasText.drawText(text, canvasText.getWidth() / 2,
                canvasText.getHeight() - padding - boundsText.bottom, paintText);

        final MarkerOptions markerOptions = new MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromView(text)))
                .anchor(0.5f, 1);

        marker = map.addMarker(markerOptions);

        return marker;
    }

    private Bitmap getBitmapFromView(String text) {
        View customView = ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.polygon_my_text_layout, null);
        customView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customView.layout(0, 0, customView.getMeasuredWidth(), customView.getMeasuredHeight());
        TextView myView = customView.findViewById(R.id.my_text_layout);
        myView.setText(text);
        customView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customView.getMeasuredWidth(), customView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customView.getBackground();
        if (drawable != null) {
            drawable.draw(canvas);
        }
        customView.draw(canvas);
        return returnedBitmap;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        getPolyLineList(googleMap);
    }

    String name;
    private boolean updatedDat;

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
                            activityPropertySurveyStatusBinding.areaCalLay.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case MAP_DATA_LIST:
                    if (data != null) {

                        Gson gson = new Gson();
                        String json = data.getStringExtra("mapDataTableListUnchecked");
                        Boolean updatedData = data.getBooleanExtra("updatedvalue", false);
                        updatedDat = updatedData;
                        Type type = new TypeToken<List<MapDataTable>>() {
                        }.getType();
                        if (mapDataTableList != null) {
                            mapDataTableList.clear();
                        }
                        mapDataTableList = gson.fromJson(json, type);
                        if (updatedData) {
                            activityPropertySurveyStatusBinding.lastUpdateDate.setVisibility(View.VISIBLE);
                            activityPropertySurveyStatusBinding.lastUpdateDate.setText("Last updated date: " + mapDataTableList.get(0).getLastUpdatedDate());
                        }
//                        mapDataTableList = (List<MapDataTable>) data.getSerializableExtra("mapDataTableListUnchecked");
                        getPolyLineList(mMap);
                    }
                    break;
                case PropertyCreation.ACTIVITY_ID:
                    if (data != null) {
                        propertyData = (PropertyData) data.getSerializableExtra(PropertyCreation.PROPERTY_DATA_KEY);
                        activityPropertySurveyStatusBinding.headerTittle.setText(propertyData.getPropertyName());
                    }
                default:
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

//    public void getMapTypeSpinner() {
//        activityPropertySurveyStatusBinding.maptypeIcon.getEditText().setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "font/roboto_regular.ttf"));
//        activityPropertySurveyStatusBinding.maptypeIcon.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "font/roboto_regular.ttf"));
//        ArrayAdapter<MapTypeModel> addresModelArrayAdapter = new ArrayAdapter<MapTypeModel>(getApplicationContext(), android.R.layout.simple_spinner_item, getMaptypeList()) {
//            @NotNull
//            public View getView(int position, View convertView, @NotNull ViewGroup parent) {
//                View v = super.getView(position, convertView, parent);
//                Typeface externalFont = Typeface.createFromAsset(getContext().getAssets(), "font/roboto_regular.ttf");
//                ((TextView) v).setTypeface(externalFont);
//                return v;
//            }
//
//            public View getDropDownView(int position, View convertView, @NotNull ViewGroup parent) {
//                View v = super.getDropDownView(position, convertView, parent);
//                Typeface externalFont = Typeface.createFromAsset(getContext().getAssets(), "font/roboto_regular.ttf");
//                ((TextView) v).setTypeface(externalFont);
//                return v;
//            }
//        };
//        addresModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        activityPropertySurveyStatusBinding.maptypeIcon.setAdapter(addresModelArrayAdapter);
////        propertyCreationBinding.state.setSelection(0);
//    }

    private ArrayList<MapTypeModel> getMaptypeList() {
        ArrayList<MapTypeModel> mapTypeModelArrayList = new ArrayList<>();
        MapTypeModel mapTypeModel = new MapTypeModel();
        mapTypeModel.setMapType("Satelite");
        mapTypeModelArrayList.add(mapTypeModel);
        mapTypeModel = new MapTypeModel();
        mapTypeModel.setMapType("Hybrid");
        mapTypeModelArrayList.add(mapTypeModel);
        mapTypeModel = new MapTypeModel();
        mapTypeModel.setMapType("Terrain");
        mapTypeModelArrayList.add(mapTypeModel);
        return mapTypeModelArrayList;
    }

    private void maptypeList() {
        mapTypeModelArrayList = new ArrayList<>();
        MapTypeModel mapTypeModel = new MapTypeModel();
        mapTypeModel.setMapType("Satelite");
        mapTypeModel.setSelected(mpresenter.getMapViewType() != null && mpresenter.getMapViewType().equals("Satelite"));
        mapTypeModelArrayList.add(mapTypeModel);
        mapTypeModel = new MapTypeModel();
        mapTypeModel.setMapType("Hybrid");
        mapTypeModel.setSelected(mpresenter.getMapViewType() != null && mpresenter.getMapViewType().equals("Hybrid"));
        mapTypeModelArrayList.add(mapTypeModel);
        mapTypeModel = new MapTypeModel();
        mapTypeModel.setMapType("Terrain");
        mapTypeModel.setSelected(mpresenter.getMapViewType() != null && mpresenter.getMapViewType().equals("Terrain"));
        mapTypeModelArrayList.add(mapTypeModel);
        mapTypeModel = new MapTypeModel();
        mapTypeModel.setMapType("Normal");
        mapTypeModel.setSelected(mpresenter.getMapViewType() != null && mpresenter.getMapViewType().equals("Normal"));
        mapTypeModelArrayList.add(mapTypeModel);
    }

    public void onMapClick() {
        if (mMap != null) {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (isMapTypeView) {
                        isMapTypeView = false;
                        Animation slideUp = AnimationUtils.loadAnimation(PropertyPreview.this, R.anim.bottom_down);
                        activityPropertySurveyStatusBinding.maptypeListLayout.setVisibility(View.INVISIBLE);
                        activityPropertySurveyStatusBinding.maptypeListLayout.startAnimation(slideUp);
                    }
                }
            });
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
//        if (isMapTypeView) {
//            Animation slideUp = AnimationUtils.loadAnimation(PropertyPreview.this, R.anim.bottom_up);
//            activityPropertySurveyStatusBinding.maptypeListLayout.setVisibility(View.VISIBLE);
//            activityPropertySurveyStatusBinding.maptypeListLayout.startAnimation(slideUp);
//            isMapTypeView = false;
//        }
    }

    public class MapTypeModel {
        public String mapType;
        private boolean isSelected;

        public String getMapType() {
            return mapType;
        }

        public void setMapType(String mapType) {
            this.mapType = mapType;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
