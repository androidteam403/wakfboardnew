package com.thresholdsoft.praanadhara.ui.propertysurveystatus;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.ActivityPropertySurveyStatusBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
import com.thresholdsoft.praanadhara.ui.propertysurvey.PropertySurvey;
import com.thresholdsoft.praanadhara.ui.propertysurvey.bottomsheet.PropertySurveyBottomSheet;
import com.thresholdsoft.praanadhara.ui.propertysurvey.model.PolylineDataTable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PropertySurveyStatus extends BaseActivity implements PropertySurveyStatusMvpView, OnMapReadyCallback {
    @Inject
    PropertySurveyStatusMvpPresenter<PropertySurveyStatusMvpView> mpresenter;
    ActivityPropertySurveyStatusBinding activityPropertySurveyStatusBinding;
    private PropertySurveyBottomSheet bottomSheet;
    FusedLocationProviderClient fusedLocationProviderClient;
    private int mapTypeData;
    private static final int PROPERTY_SURVEY = 545;
    private static final int REQUEST_CODE = 121;
    Location currentLocation;
    private GoogleMap mMap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPropertySurveyStatusBinding = DataBindingUtil.setContentView(this, R.layout.activity_property_survey_status);
        getActivityComponent().inject(this);
        mpresenter.onAttach(PropertySurveyStatus.this);
        setUp();
    }

    @Override
    protected void setUp() {

        activityPropertySurveyStatusBinding.expandView.setVisibility(View.VISIBLE);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

        activityPropertySurveyStatusBinding.plusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet();
            }
        });

        fullMap();
    }

    public void fullMap() {
//        ImageView collapse = includedLayout.findViewById(R.id.collapseView);
        LinearLayout mapFrameLayout = (LinearLayout) findViewById(R.id.mapFrameLayout);
        activityPropertySurveyStatusBinding.expandView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams fullMapParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                activityPropertySurveyStatusBinding.farmerdata.setVisibility(View.GONE);
                mapFrameLayout.setLayoutParams(fullMapParams);
                activityPropertySurveyStatusBinding.expandView.setVisibility(View.GONE);
                activityPropertySurveyStatusBinding.collapseView.setVisibility(View.VISIBLE);
                //  previewDisplay(surveyDetailsAdapter.getListData());
            }
        });
        activityPropertySurveyStatusBinding.collapseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams fullMapParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                activityPropertySurveyStatusBinding.farmerdata.setVisibility(View.VISIBLE);
                fullMapParams.height = 900;
                mapFrameLayout.setLayoutParams(fullMapParams);

                activityPropertySurveyStatusBinding.expandView.setVisibility(View.VISIBLE);
                activityPropertySurveyStatusBinding.collapseView.setVisibility(View.GONE);
                //  previewDisplay(surveyDetailsAdapter.getListData());
            }
        });
    }

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
        startActivityForResult(PropertySurvey.getStartIntent(PropertySurveyStatus.this, mapData), PROPERTY_SURVEY);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
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

                    Toast toast = Toast.makeText(PropertySurveyStatus.this, currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT);
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
                    mapFragment.getMapAsync(PropertySurveyStatus.this);
                }
            }
        });
    }

    private List<LatLng> getPolyLineLatlangList = new ArrayList<>();
    private LatLng latLngLine;
    Polyline polyline = null;
    List<Polyline> polylineList = new ArrayList<>();
    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();
    Marker polyLineMarker;
    private void getPolyLineList(GoogleMap googleMap) {
        mMap = googleMap;
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.blue_circle);

        if (mpresenter.getPolylinelist() != null && mpresenter.getPolylinelist().size() > 0) {
            for (PolylineDataTable polylineDataTable : mpresenter.getPolylinelist()) {
                latLngLine = new LatLng(polylineDataTable.getLatitude(), polylineDataTable.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(latLngLine).draggable(true).icon(icon);
                polyLineMarker = mMap.addMarker(markerOptions);
                latLngList.add(latLngLine);
                markerList.add(polyLineMarker);
                getPolyLineLatlangList.add(latLngLine);
            }

            PolylineOptions polylineOptions = new PolylineOptions().addAll(getPolyLineLatlangList).color(Color.BLUE).width(5).clickable(true);
            polyline = mMap.addPolyline(polylineOptions);
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(getPolyLineLatlangList.get(0)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getPolyLineLatlangList.get(0), 7));

        } else {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!").icon(icon);
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
            googleMap.addMarker(markerOptions);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        getPolyLineList(googleMap);
    }
}
