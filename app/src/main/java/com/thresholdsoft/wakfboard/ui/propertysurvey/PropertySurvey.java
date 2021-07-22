package com.thresholdsoft.wakfboard.ui.propertysurvey;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.ActivityPropertySurveyBinding;
import com.thresholdsoft.wakfboard.services.LocationMonitoringService;
import com.thresholdsoft.wakfboard.ui.alertdialog.CutomAlertBox;
import com.thresholdsoft.wakfboard.ui.base.BaseActivity;
import com.thresholdsoft.wakfboard.ui.dialog.PropertyCreationDialog;
import com.thresholdsoft.wakfboard.ui.gallery.GalleryActivity;
import com.thresholdsoft.wakfboard.ui.photouploadactivity.PhotoUpload;
import com.thresholdsoft.wakfboard.ui.propertysurvey.model.MapDataTable;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class PropertySurvey extends BaseActivity implements PropertySurveyMvpView,
        OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    @Inject
    PropertySurveyMvpPresenter<PropertySurveyMvpView> mpresenter;
    ActivityPropertySurveyBinding propertySurveyBinding;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    Location currentLocation;
    private GoogleMap mMap;
    private static final int PHOTO_UPLOAD = 141;

    Polyline polyline = null;
    List<Polyline> polylineList = new ArrayList<>();
    List<Polygon> polygonList = new ArrayList<>();
    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();
    Marker marker;
    private int mapTypeData;
    private int propertyId;
    private List<MapDataTable> mapDataTableList;
    private int pos;
    private boolean editMode;
    private boolean editClick;
    boolean individualGallery;
    public static final int GALLERY_ACTIVITY = 202;
    private String measurements;
    private LocationManager mLocationManager;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private Polygon runningPathPolygon;

    public static Intent getStartIntent(Context context, int mapType, int propertyId, boolean edited, String measurements) {
        Intent intent = new Intent(context, PropertySurvey.class);
        intent.putExtra("maptype", mapType);
        intent.putExtra("propertyId", propertyId);
        intent.putExtra("edited", edited);
        intent.putExtra("measurements", measurements);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    public static Intent getStartIntent(Context context, int mapType, int propertyId, String myJson, int pos, boolean isEditMode, String measurements) {
        Intent intent = new Intent(context, PropertySurvey.class);
        intent.putExtra("maptype", mapType);
        intent.putExtra("propertyId", propertyId);
        intent.putExtra("mapDataTableListUnchecked", myJson);
        intent.putExtra("position", pos);
        intent.putExtra("editMode", isEditMode);
        intent.putExtra("measurements", measurements);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        propertySurveyBinding = DataBindingUtil.setContentView(this, R.layout.activity_property_survey);
        getActivityComponent().inject(this);
        mpresenter.onAttach(PropertySurvey.this);


        if (ContextCompat.checkSelfPermission(PropertySurvey.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(PropertySurvey.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(PropertySurvey.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(PropertySurvey.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
//        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, this);
//        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUp();

    }

    @Override
    protected void setUp() {

        propertySurveyBinding.setCallbacks(mpresenter);

        if (getIntent() != null) {
            mapTypeData = (int) getIntent().getIntExtra("maptype", 0);
            propertyId = (int) getIntent().getIntExtra("propertyId", 0);
            editClick = (boolean) getIntent().getBooleanExtra("edited", false);
            measurements = (String) getIntent().getStringExtra("measurements");
            if (editClick) {
                propertySurveyBinding.editIcon.setVisibility(View.GONE);
                propertySurveyBinding.galleryIcon.setVisibility(View.GONE);
            }
            Gson gson = new Gson();
            String json = getIntent().getStringExtra("mapDataTableListUnchecked");
            Type type = new TypeToken<List<MapDataTable>>() {
            }.getType();
            mapDataTableList = gson.fromJson(json, type);

            pos = (int) getIntent().getIntExtra("position", 0);
            editMode = (boolean) getIntent().getBooleanExtra("editMode", false);
        }
        if (!editMode) {
            LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
            }

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
            }

            if (!gps_enabled && !network_enabled) {
                // notify user
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage(R.string.gps_network_not_enabled)
                        .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
//                    .setNegativeButton(R.string.Cancel, null)
                        .show();
            }
        } else {
            propertySurveyBinding.currentLocationIcon.setVisibility(View.GONE);
        }
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        fetchLocation();

        if (mapTypeData == 1) {
            if (editMode) {
                propertySurveyBinding.propertyCreationDate.setVisibility(View.VISIBLE);
                propertySurveyBinding.description.setVisibility(View.VISIBLE);
                propertySurveyBinding.description.setText(mapDataTableList.get(pos).getDescription());
                propertySurveyBinding.tittle.setText(mapDataTableList.get(pos).getName());
                propertySurveyBinding.propertyCreationDate.setText(mapDataTableList.get(pos).getMapDate());
            } else {
                propertySurveyBinding.description.setVisibility(View.GONE);
                propertySurveyBinding.propertyCreationDate.setVisibility(View.GONE);
                propertySurveyBinding.tittle.setText(R.string.label_survey_details);
            }
            if (editClick) {
                propertySurveyBinding.pointSave.setVisibility(View.VISIBLE);
            } else {
                propertySurveyBinding.pointSave.setVisibility(View.GONE);
            }
            propertySurveyBinding.polygonManualLay.setVisibility(View.GONE);
            propertySurveyBinding.polygonStart.setVisibility(View.GONE);
            propertySurveyBinding.polygonSave.setVisibility(View.GONE);
            propertySurveyBinding.polygonStop.setVisibility(View.GONE);
            propertySurveyBinding.polygonLay.setVisibility(View.GONE);
            propertySurveyBinding.polylineLay.setVisibility(View.GONE);
            propertySurveyBinding.pointSave.setVisibility(View.VISIBLE);
            propertySurveyBinding.typeTextview.setBackgroundResource(R.drawable.new_point);
            propertySurveyBinding.polygonArea.setVisibility(View.GONE);
            propertySurveyBinding.distanceTextView.setVisibility(View.GONE);
        } else if (mapTypeData == 2) {
            if (editMode) {
                propertySurveyBinding.propertyCreationDate.setVisibility(View.VISIBLE);
                propertySurveyBinding.description.setVisibility(View.VISIBLE);
                propertySurveyBinding.description.setText(mapDataTableList.get(pos).getDescription());
                propertySurveyBinding.tittle.setText(mapDataTableList.get(pos).getName());
                propertySurveyBinding.propertyCreationDate.setText(mapDataTableList.get(pos).getMapDate());
            } else {
                propertySurveyBinding.propertyCreationDate.setVisibility(View.GONE);
                propertySurveyBinding.description.setVisibility(View.GONE);
                propertySurveyBinding.tittle.setText(R.string.label_survey_details);
            }
            propertySurveyBinding.polygonManualLay.setVisibility(View.GONE);
            propertySurveyBinding.polygonStart.setVisibility(View.GONE);
            propertySurveyBinding.polygonSave.setVisibility(View.GONE);
            propertySurveyBinding.polygonStop.setVisibility(View.GONE);
            propertySurveyBinding.polygonLay.setVisibility(View.GONE);
            propertySurveyBinding.pointSave.setVisibility(View.GONE);
            if (editClick) {
                propertySurveyBinding.polylineLay.setVisibility(View.VISIBLE);
            } else {
                propertySurveyBinding.polylineLay.setVisibility(View.GONE);
            }
            propertySurveyBinding.typeTextview.setBackgroundResource(R.drawable.new_line);
            propertySurveyBinding.polygonArea.setVisibility(View.GONE);
            propertySurveyBinding.distanceTextView.setVisibility(View.VISIBLE);
        } else if (mapTypeData == 3) {
            if (editMode) {
                propertySurveyBinding.propertyCreationDate.setVisibility(View.VISIBLE);
                propertySurveyBinding.description.setVisibility(View.VISIBLE);
                propertySurveyBinding.description.setText(mapDataTableList.get(pos).getDescription());
                propertySurveyBinding.tittle.setText(mapDataTableList.get(pos).getName());
                propertySurveyBinding.propertyCreationDate.setText(mapDataTableList.get(pos).getMapDate());
            } else {
                propertySurveyBinding.propertyCreationDate.setVisibility(View.GONE);
                propertySurveyBinding.description.setVisibility(View.GONE);
                propertySurveyBinding.tittle.setText(R.string.label_survey_details);
            }
            propertySurveyBinding.polygonManualLay.setVisibility(View.VISIBLE);
            if (editClick) {
                propertySurveyBinding.polygonManualLay.setVisibility(View.VISIBLE);
            } else {
                propertySurveyBinding.polygonManualLay.setVisibility(View.GONE);
            }
            propertySurveyBinding.polylineLay.setVisibility(View.GONE);
            propertySurveyBinding.pointSave.setVisibility(View.GONE);
            propertySurveyBinding.typeTextview.setBackgroundResource(R.drawable.new_polygon);
            propertySurveyBinding.polygonArea.setVisibility(View.VISIBLE);
            propertySurveyBinding.distanceTextView.setVisibility(View.GONE);
        } else if (mapTypeData == 4) {
            if (editMode) {
                propertySurveyBinding.propertyCreationDate.setVisibility(View.VISIBLE);
                propertySurveyBinding.description.setVisibility(View.VISIBLE);
                propertySurveyBinding.description.setText(mapDataTableList.get(pos).getDescription());
                propertySurveyBinding.tittle.setText(mapDataTableList.get(pos).getName());
                propertySurveyBinding.propertyCreationDate.setText(mapDataTableList.get(pos).getMapDate());
            } else {
                propertySurveyBinding.propertyCreationDate.setVisibility(View.GONE);
                propertySurveyBinding.description.setVisibility(View.GONE);
                propertySurveyBinding.tittle.setText(R.string.label_survey_details);
            }
            propertySurveyBinding.polygonWalkLay.setVisibility(View.VISIBLE);
            if (editClick) {
                propertySurveyBinding.polygonWalkLay.setVisibility(View.VISIBLE);
            } else {
                propertySurveyBinding.polygonWalkLay.setVisibility(View.GONE);
            }

            propertySurveyBinding.polylineLay.setVisibility(View.GONE);
            propertySurveyBinding.pointSave.setVisibility(View.GONE);
            propertySurveyBinding.polygonLay.setVisibility(View.GONE);
            propertySurveyBinding.typeTextview.setBackgroundResource(R.drawable.new_polygon);
            propertySurveyBinding.polygonArea.setVisibility(View.VISIBLE);
            propertySurveyBinding.distanceTextView.setVisibility(View.GONE);
        }
        propertySurveyBinding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        broadCastReceivers();

        propertySurveyBinding.editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                CutomAlertBox cutomAlertBox = new CutomAlertBox(PropertySurvey.this);

                cutomAlertBox.setTitle("Do you want to edit survey details ?");
                cutomAlertBox.setPositiveListener(view -> {
                    propertySurveyBinding.editIcon.setVisibility(View.GONE);
                    editClick = true;
                    if (mapTypeData == 3)
                        propertySurveyBinding.polygonManualLay.setVisibility(View.VISIBLE);
                    if (mapTypeData == 2)
                        propertySurveyBinding.polylineLay.setVisibility(View.VISIBLE);
                    if (mapTypeData == 1)
                        propertySurveyBinding.pointSave.setVisibility(View.VISIBLE);
                    onMapReady(mMap);
                    cutomAlertBox.dismiss();
                });
                cutomAlertBox.setNegativeListener(v -> cutomAlertBox.dismiss());
                cutomAlertBox.show();
            }
        });
        propertySurveyBinding.currentLocationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLocation != null) {
                    if (currentLocation != null) {
                        BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.blue_dot);
                        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!").icon(icon2);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                        mMap.addMarker(markerOptions);
                    }
                }
            }
        });

        propertySurveyBinding.galleryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                individualGallery = true;
                Gson gson = new Gson();

                String myJson = gson.toJson(mapDataTableList);
                startActivityForResult(GalleryActivity.getStartIntent(PropertySurvey.this, propertyId, pos, individualGallery, myJson), GALLERY_ACTIVITY);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

    }

    private void currentLocation() {
        if (currentLocation != null) {
            BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.blue_dot);
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!").icon(icon2);
//            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            mMap.addMarker(markerOptions);
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
//                    Toast toast = Toast.makeText(PropertySurvey.this, currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT);
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
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(PropertySurvey.this);
                }
            }
        });
    }

    private void broadCastReceivers() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
                        String longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);
                        Location location = intent.getParcelableExtra("location");
                        if (location != null && latitude != null && longitude != null) {


                        }
                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mpresenter.getMapViewType() != null && !mpresenter.getMapViewType().isEmpty()) {
            setMapType(mpresenter.getMapViewType());
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        getPolyLineList(mMap);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.blue_circle);
        BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.marker_yellow_icon);

        if (editClick) {
            double polygoni1 = 0.0;
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    if (mMap != null)
                        mMap.clear();
                    double i1 = 0.0;

                    currentLocation();

                    if (mapTypeData == 0) {
//                    Toast.makeText(PropertySurvey.this, "Please Select MapType", Toast.LENGTH_SHORT).show();
                        getSnackBarView("Please Select MapType");
                    } else if (mapTypeData == 1) {
                        if (markerList.size() < 1) {
                            addPoint(latLng);
                            latLngList.add(latLng);
                            mMap.setOnMarkerClickListener(PropertySurvey.this);
                        }
                    } else if (mapTypeData == 2) {
                        latLngList.add(latLng);

                        if (historyBool) {
                            latLngsdummyhistory.add(latLng);
                        }
                        for (LatLng latLng1 : latLngList) {
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng1).zIndex(latLngList.indexOf(latLng1)).icon(icon2).draggable(true);
                            marker = mMap.addMarker(markerOptions);
                            markerList.add(marker);
                        }
                        if (polyline != null) polyline.remove();
                        PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngList).color(getResources().getColor(R.color.colorPrimaryDark)).width(5).clickable(true);
                        polyline = mMap.addPolyline(polylineOptions);
                        polylineList.add(polyline);

                        for (LatLng latLngPol : latLngList) {

                            int position = latLngList.indexOf(latLngPol);
                            PropertySurvey.MarkerTag yourMarkerTag = new PropertySurvey.MarkerTag();
                            yourMarkerTag.setLatLng(latLngPol);
                            yourMarkerTag.setPosition(position);
                            marker.setTag(yourMarkerTag);

                            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                @Override
                                public void onMarkerDragStart(Marker marker) {

                                }

                                @Override
                                public void onMarkerDrag(Marker marker) {
                                }

                                @Override
                                public void onMarkerDragEnd(Marker marker) {
                                    updateMarkerLocation(marker);
                                    double j = 0.0;
                                    if (latLngList.size() >= 2) {
                                        for (int i = 0; i < latLngList.size(); i++) {
                                            if (i != latLngList.size() - 1) {
                                                LatLng from = new LatLng(((latLngList.get(i).latitude)), ((latLngList.get(i).longitude)));
                                                LatLng to = new LatLng(((latLngList.get(i + 1).latitude)), ((latLngList.get(i + 1).longitude)));

                                                j += Double.parseDouble(mpresenter.getLineLength(from, to));
                                                double amount1 = (j);
                                                DecimalFormat formatter1 = new DecimalFormat("#,###.00");
                                                String formatted1 = formatter1.format(amount1);

                                                propertySurveyBinding.distanceTextView.setText("Length: " + formatted1 + " m");
                                            }
                                        }
                                    }
                                }
                            });
                        }

                        mMap.setOnMarkerClickListener(PropertySurvey.this);

                        if (latLngList.size() >= 2) {
                            for (int i = 0; i < latLngList.size(); i++) {
                                if (i != latLngList.size() - 1) {
                                    LatLng from = new LatLng(((latLngList.get(i).latitude)), ((latLngList.get(i).longitude)));
                                    LatLng to = new LatLng(((latLngList.get(i + 1).latitude)), ((latLngList.get(i + 1).longitude)));

                                    i1 += Double.parseDouble(mpresenter.getLineLength(from, to));
                                    double amount1 = (i1);
                                    DecimalFormat formatter1 = new DecimalFormat("#,###.00");
                                    String formatted1 = formatter1.format(amount1);

                                    propertySurveyBinding.distanceTextView.setText("Length: " + formatted1 + " m");
                                }
                            }
                        }

                    } else if (mapTypeData == 3) {
//                    MarkerOptions markerOptions1 = new MarkerOptions().position(latLng);
//                    marker = mMap.addMarker(markerOptions1);
//
//                    latLngList.add(latLng);
//                    markerList.add(marker);

                        latLngList.add(latLng);
                        if (historyBool) {
                            latLngsdummyhistory.add(latLng);
                        }
                        for (LatLng latLngs : latLngList) {
                            MarkerOptions markerOptions = new MarkerOptions().position(latLngs).zIndex(-2).snippet(latLngList.indexOf(latLngs) + "").draggable(true);
                            marker = mMap.addMarker(markerOptions);
                            markerList.add(marker);
                        }

                        if (latLngList.size() > 1) {
                            for (int i = 0; i < latLngList.size(); i++) {
                                if (i == latLngList.size() - 1) {
                                    LatLng latLng1 = new LatLng((latLngList.get(i).latitude + latLngList.get(0).latitude) / 2, (latLngList.get(i).longitude + latLngList.get(0).longitude) / 2);
                                    MarkerOptions markerOptions1 = new MarkerOptions().position(latLng1).zIndex(-1).icon(icon2).draggable(true);
                                    mMap.addMarker(markerOptions1);
                                } else {
                                    LatLng latLng1 = new LatLng((latLngList.get(i).latitude + latLngList.get(i + 1).latitude) / 2, (latLngList.get(i).longitude + latLngList.get(i + 1).longitude) / 2);
                                    MarkerOptions markerOptions1 = new MarkerOptions().position(latLng1).zIndex(i + 1).icon(icon2).draggable(true);
                                    mMap.addMarker(markerOptions1);

                                }

                            }
                        }
                        if (polygon != null) polygon.remove();
                        PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngList).strokeWidth(5).strokeColor(getResources().getColor(R.color.colorPrimaryDark)).fillColor(getResources().getColor(R.color.yellow_transparent)).clickable(true);
                        polygon = mMap.addPolygon(polygonOptions);
                        polygonList.add(polygon);

                        for (LatLng latLngPol : latLngList) {

                            int position = latLngList.indexOf(latLngPol);
                            PropertySurvey.MarkerTag yourMarkerTag = new PropertySurvey.MarkerTag();
                            yourMarkerTag.setLatLng(latLngPol);
                            yourMarkerTag.setPosition(position);
                            marker.setTag(yourMarkerTag);

                            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                @Override
                                public void onMarkerDragStart(Marker marker) {

                                }

                                @Override
                                public void onMarkerDrag(Marker marker) {
                                }

                                @Override
                                public void onMarkerDragEnd(Marker marker) {
                                    updatePolygonMarkerLocation(marker);
//                                    propertySurveyBinding.polygonArea.setText("Area :" + mpresenter.getPolygonArea(latLngList));
                                    setMeasurements(polygoni1, latLngList);

                                    currentLocation();
                                }
                            });
                        }

                        mMap.setOnMarkerClickListener(PropertySurvey.this);
                        setMeasurements(polygoni1, latLngList);
//                        propertySurveyBinding.polygonArea.setText("Area :" + mpresenter.getPolygonArea(latLngList));
                    }
                }
            });
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private void setMeasurements(double polygoni1, List<LatLng> getPolygontLatlngList) {
        if (measurements != null) {
            if (measurements.equalsIgnoreCase("Square Meters")) {

                polygoni1 += Double.parseDouble(mpresenter.getPolygonAreainMeters(getPolygontLatlngList));

                double amount = polygoni1;
                DecimalFormat formatter = new DecimalFormat("#,###");
                String formatted = formatter.format(amount);
                propertySurveyBinding.polygonArea.setText("Area:" + " " + formatted + " m²");
            } else if (measurements.equalsIgnoreCase("Square Feet")) {

                polygoni1 += Double.parseDouble(mpresenter.getPolygonAreainSquareFeet(getPolygontLatlngList));

                double amount = polygoni1;
                DecimalFormat formatter = new DecimalFormat("#,###");
                String formatted = formatter.format(amount);
                propertySurveyBinding.polygonArea.setText("Area:" + " " + formatted + " sq ft²");
            } else if (measurements.equalsIgnoreCase("Square yards")) {

                polygoni1 += Double.parseDouble(mpresenter.getPolygonAreainSquareFeet(getPolygontLatlngList));

                double amount = polygoni1;
                DecimalFormat formatter = new DecimalFormat("#,###");
                String formatted = formatter.format(amount);
                propertySurveyBinding.polygonArea.setText("Area:" + " " + formatted + " sq yd²");
            } else if (measurements.equalsIgnoreCase("Acres")) {

                polygoni1 += Double.parseDouble(mpresenter.getPolygonAreainAcers(getPolygontLatlngList));

                double amount = polygoni1;
                DecimalFormat formatter = new DecimalFormat("#,###");
                String formatted = formatter.format(amount);
                propertySurveyBinding.polygonArea.setText("Area:" + " " + formatted + " acers");
            }
        }
    }

//    private List<LatLng> getPointLatlngList = new ArrayList<>();
//    private List<LatLng> getPolylineLatlngList = new ArrayList<>();
//    private List<LatLng> getPolygontLatlngList = new ArrayList<>();

    private LatLng latLngLine;

    private void getPolyLineList(GoogleMap googleMap) {
        double i1 = 0.0;
        double polygoni1 = 0.0;
        mMap = googleMap;
        if (googleMap != null) {
            googleMap.clear();
        }
        if (mapDataTableList != null && mapDataTableList.size() > 0) {
            for (MapDataTable mapDataTable : mapDataTableList) {
                if (mapDataTable.getMapType() == 1 && mapDataTable.isEditable()) {
                    latLngList.clear();
                    latLngList.addAll(mapDataTable.getLatLngList());
                    for (int i = 0; i < latLngList.size(); i++) {
                        latLngLine = new LatLng(latLngList.get(i).latitude, latLngList.get(i).longitude);
                        MarkerOptions markerOptions = new MarkerOptions().position(latLngLine);
                        marker = mMap.addMarker(markerOptions);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLngLine));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngLine, 18));
                    }
                } else if (mapDataTable.getMapType() == 2 && mapDataTable.isEditable()) {
                    BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.marker_yellow_icon);
                    latLngList.clear();
                    latLngList.addAll(mapDataTable.getLatLngList());
                    for (int i = 0; i < latLngList.size(); i++) {
                        latLngLine = new LatLng(latLngList.get(i).latitude, latLngList.get(i).longitude);

                        if (!editClick) {
                            if (i == latLngList.size() - 1) {
                                LatLng latLng1 = new LatLng((latLngList.get(i).latitude + latLngList.get(0).latitude) / 2, (latLngList.get(i).longitude + latLngList.get(0).longitude) / 2);

                            } else {
                                LatLng latLng1 = new LatLng((latLngList.get(i).latitude + latLngList.get(i + 1).latitude) / 2, (latLngList.get(i).longitude + latLngList.get(i + 1).longitude) / 2);

                                LatLng from = new LatLng(((latLngList.get(i).latitude)), ((latLngList.get(i).longitude)));
                                LatLng to = new LatLng(((latLngList.get(i + 1).latitude)), ((latLngList.get(i + 1).longitude)));

                                double amount = Double.parseDouble(mpresenter.getLineLength(from, to));
                                DecimalFormat formatter = new DecimalFormat("#,###");
                                String formatted = formatter.format(amount);
                                if (amount != 0) {
                                    addText(getApplicationContext(), mMap, latLng1, formatted, 3, 16, Color.RED);
                                }
                            }
                        }


                    }
//                    latLngList.add(latLngLine);
                    if (editClick) {
                        for (LatLng latLng1 : latLngList) {
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng1).zIndex(latLngList.indexOf(latLng1)).icon(icon2).draggable(true);
                            marker = mMap.addMarker(markerOptions);
                            markerList.add(marker);
                        }
                    }
                    if (polyline != null) polyline.remove();
                    PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngList).color(getResources().getColor(R.color.colorPrimaryDark)).width(5).clickable(true);
                    polyline = mMap.addPolyline(polylineOptions);
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (LatLng latLng : latLngList)
                        builder.include(new LatLng(latLng.latitude, latLng.longitude));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 14));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 14));
//                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLngList.get(0)));
//                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngList.get(0), 18));
                    polylineList.add(polyline);

                    for (LatLng latLngPol : latLngList) {

                        int position = latLngList.indexOf(latLngPol);
                        MarkerTag yourMarkerTag = new MarkerTag();
                        yourMarkerTag.setLatLng(latLngPol);
                        yourMarkerTag.setPosition(position);
//                        marker.setTag(yourMarkerTag);

                        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                            @Override
                            public void onMarkerDragStart(Marker marker) {

                            }

                            @Override
                            public void onMarkerDrag(Marker marker) {
                            }

                            @Override
                            public void onMarkerDragEnd(Marker marker) {
                                double j = 0.0;
                                updateMarkerLocation(marker);
                                if (latLngList.size() >= 2) {
                                    for (int i = 0; i < latLngList.size(); i++) {
                                        if (i != latLngList.size() - 1) {
                                            LatLng from = new LatLng(((latLngList.get(i).latitude)), ((latLngList.get(i).longitude)));
                                            LatLng to = new LatLng(((latLngList.get(i + 1).latitude)), ((latLngList.get(i + 1).longitude)));

                                            j += Double.parseDouble(mpresenter.getLineLength(from, to));
                                            double amount1 = (j);
                                            DecimalFormat formatter1 = new DecimalFormat("#,###.00");
                                            String formatted1 = formatter1.format(amount1);

                                            propertySurveyBinding.distanceTextView.setText("Length: " + formatted1 + " m");
                                        }
                                    }
                                }

                            }
                        });
                    }
                    if (latLngList.size() >= 2) {
                        for (int i = 0; i < latLngList.size(); i++) {
                            if (i != latLngList.size() - 1) {
                                LatLng from = new LatLng(((latLngList.get(i).latitude)), ((latLngList.get(i).longitude)));
                                LatLng to = new LatLng(((latLngList.get(i + 1).latitude)), ((latLngList.get(i + 1).longitude)));

                                i1 += Double.parseDouble(mpresenter.getLineLength(from, to));
                                double amount1 = (i1);
                                DecimalFormat formatter1 = new DecimalFormat("#,###.00");
                                String formatted1 = formatter1.format(amount1);

                                propertySurveyBinding.distanceTextView.setText("Length: " + formatted1 + " m");
                            }
                        }
                    }


                } else if (mapDataTable.getMapType() == 3 && mapDataTable.isEditable()) {
                    BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.marker_yellow_icon);
                    latLngList.clear();
                    latLngList.addAll(mapDataTable.getLatLngList());
                    for (int i = 0; i < latLngList.size(); i++) {
                        latLngLine = new LatLng(latLngList.get(i).latitude, latLngList.get(i).longitude);
                    }
//                    latLngList.add(latLngLine);
                    if (editClick) {
                        for (LatLng latLngs : latLngList) {
                            MarkerOptions markerOptions = new MarkerOptions().position(latLngs).zIndex(-2).snippet(latLngList.indexOf(latLngs) + "").draggable(true);
                            marker = mMap.addMarker(markerOptions);
                            markerList.add(marker);
                        }
                    }

                    if (latLngList.size() > 1) {
                        for (int i = 0; i < latLngList.size(); i++) {
                            if (i == latLngList.size() - 1) {
                                LatLng latLng1 = new LatLng((latLngList.get(i).latitude + latLngList.get(0).latitude) / 2, (latLngList.get(i).longitude + latLngList.get(0).longitude) / 2);
                                if (!editClick) {
                                    LatLng from = new LatLng(((latLngList.get(i).latitude)), ((latLngList.get(i).longitude)));
                                    LatLng to = new LatLng(((latLngList.get(0).latitude)), ((latLngList.get(0).longitude)));

                                    double amount = Double.parseDouble(mpresenter.getLineLength(from, to));
                                    DecimalFormat formatter = new DecimalFormat("#,###");
                                    String formatted = formatter.format(amount);
                                    if (amount != 0) {
                                        addText(getApplicationContext(), mMap, latLng1, formatted, 3, 16, Color.RED);
                                    }
                                }

                                if (editClick) {
                                    MarkerOptions markerOptions1 = new MarkerOptions().position(latLng1).zIndex(-1).icon(icon2).draggable(true);
                                    mMap.addMarker(markerOptions1);
                                }
                            } else {
                                LatLng latLng1 = new LatLng((latLngList.get(i).latitude + latLngList.get(i + 1).latitude) / 2, (latLngList.get(i).longitude + latLngList.get(i + 1).longitude) / 2);
                                if (!editClick) {
                                    LatLng from = new LatLng(((latLngList.get(i).latitude)), ((latLngList.get(i).longitude)));
                                    LatLng to = new LatLng(((latLngList.get(i + 1).latitude)), ((latLngList.get(i + 1).longitude)));

                                    double amount = Double.parseDouble(mpresenter.getLineLength(from, to));
                                    DecimalFormat formatter = new DecimalFormat("#,###");
                                    String formatted = formatter.format(amount);
                                    if (amount != 0) {
                                        addText(getApplicationContext(), mMap, latLng1, formatted, 3, 16, Color.RED);
                                    }
                                }
                                if (editClick) {
                                    MarkerOptions markerOptions1 = new MarkerOptions().position(latLng1).zIndex(i + 1).icon(icon2).draggable(true);
                                    mMap.addMarker(markerOptions1);
                                }
                            }
                        }
                    }
                    if (polygon != null) polygon.remove();
                    PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngList).strokeWidth(5).strokeColor(getResources().getColor(R.color.colorPrimaryDark)).fillColor(getResources().getColor(R.color.yellow_transparent)).clickable(true);
                    polygon = mMap.addPolygon(polygonOptions);
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (LatLng latLng : latLngList)
                        builder.include(new LatLng(latLng.latitude, latLng.longitude));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 14));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 14));
//                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLngList.get(0)));
//                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngList.get(0), 18));
                    polygonList.add(polygon);

                    for (LatLng latLngPol : latLngList) {

                        int position = latLngList.indexOf(latLngPol);
                        MarkerTag yourMarkerTag = new MarkerTag();
                        yourMarkerTag.setLatLng(latLngPol);
                        yourMarkerTag.setPosition(position);
//                        marker.setTag(yourMarkerTag);

                        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                            @Override
                            public void onMarkerDragStart(Marker marker) {

                            }

                            @Override
                            public void onMarkerDrag(Marker marker) {
                            }

                            @Override
                            public void onMarkerDragEnd(Marker marker) {
                                updatePolygonMarkerLocation(marker);
//                                propertySurveyBinding.polygonArea.setText("Area :" + mpresenter.getPolygonArea(latLngList));
                                setMeasurements(polygoni1, latLngList);

                                currentLocation();
                            }
                        });
                    }

                    mMap.setOnMarkerClickListener(PropertySurvey.this);

//                    propertySurveyBinding.polygonArea.setText("Area :" + mpresenter.getPolygonArea(latLngList));
                    setMeasurements(polygoni1, latLngList);
                }

            }

        } else {
            currentLocation();
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

    private void updatePolygonMarkerLocation(Marker marker) {
//        MarkerTag tag = (MarkerTag) marker.getTag();
        if (marker.getZIndex() == -2) {
//            LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
            int position = Integer.parseInt(marker.getSnippet());
            //latLngList.indexOf(latLng);
            if (position != -1) {
                latLngList.set(position, marker.getPosition());
                MarkerTag markerTag = new MarkerTag();
                markerTag.setPosition(position);
                latLngsdummyhistory.add(latLngList.get(position));
                historyBool = true;
                historyPos = position;
                markerTag.setLatLng(marker.getPosition());
                marker.setTag(markerTag);
            } else {
                marker.remove();
            }
        } else {
            if (marker.getZIndex() != -1) {
                String position = String.valueOf(marker.getZIndex());
                String pos = position.substring(0, position.indexOf("."));
                latLngList.add(Integer.parseInt(pos), marker.getPosition());
                MarkerTag markerTag = new MarkerTag();
                markerTag.setPosition(Integer.parseInt(pos));
                latLngsdummyhistory.add(latLngList.get(Integer.parseInt(pos)));
                historyBool = true;
                historyPos = Integer.parseInt(pos);
                markerTag.setLatLng(marker.getPosition());
                marker.setTag(markerTag);
            } else {
                latLngList.add(marker.getPosition());
                latLngsdummyhistory.add(marker.getPosition());
                historyBool = true;
            }
        }
        if (mMap != null) {
            mMap.clear();
        }
        for (LatLng latLngs : latLngList) {
            MarkerOptions markerOptions = new MarkerOptions().position(latLngs).zIndex(-2).snippet(latLngList.indexOf(latLngs) + "").draggable(true);
            marker = mMap.addMarker(markerOptions);
            markerList.add(marker);
        }
        BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.marker_yellow_icon);
        if (latLngList.size() > 1) {
            for (int i = 0; i < latLngList.size(); i++) {
                if (i == latLngList.size() - 1) {
                    LatLng latLng1 = new LatLng((latLngList.get(i).latitude + latLngList.get(0).latitude) / 2, (latLngList.get(i).longitude + latLngList.get(0).longitude) / 2);
                    MarkerOptions markerOptions1 = new MarkerOptions().position(latLng1).zIndex(-1).icon(icon2).draggable(true);
                    mMap.addMarker(markerOptions1);
                } else {
                    LatLng latLng1 = new LatLng((latLngList.get(i).latitude + latLngList.get(i + 1).latitude) / 2, (latLngList.get(i).longitude + latLngList.get(i + 1).longitude) / 2);
                    MarkerOptions markerOptions1 = new MarkerOptions().position(latLng1).zIndex(i + 1).icon(icon2).draggable(true);
                    mMap.addMarker(markerOptions1);
                }
            }
        }
        if (polygon != null) polygon.remove();
        PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngList).strokeWidth(5).strokeColor(getResources().getColor(R.color.colorPrimaryDark)).fillColor(getResources().getColor(R.color.yellow_transparent)).clickable(true);
        polygon = mMap.addPolygon(polygonOptions);
        polygonList.add(polygon);
    }

    private List<LatLng> latLngsdummyhistory = new ArrayList<>();
    private boolean historyBool;
    int historyPos;

    private void updateMarkerLocation(Marker marker) {
//        MarkerTag tag = (MarkerTag) marker.getTag();
//        assert tag != null;
        int position = Integer.parseInt(String.valueOf(marker.getZIndex()).substring(0, String.valueOf(marker.getZIndex()).indexOf(".")));
//        int position = latLngList.indexOf(tag.getLatLng());
        if (position != -1) {
            latLngList.set(position, marker.getPosition());
            latLngsdummyhistory.add(latLngList.get(position));
            historyBool = true;
            historyPos = position;
            MarkerTag markerTag = new MarkerTag();
            markerTag.setPosition(position);
            markerTag.setLatLng(marker.getPosition());
            marker.setTag(markerTag);
        } else {
            marker.remove();
        }

        if (polyline != null) polyline.remove();
        PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngList).color(getResources().getColor(R.color.colorPrimaryDark)).width(5).clickable(true);
        polyline = mMap.addPolyline(polylineOptions);
        polylineList.add(polyline);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker != null) {
            if (mapTypeData == 1) {
                marker.remove();
                markerList.remove(marker);
            } else if (mapTypeData == 2) {
                PropertyCreationDialog dialogView = new PropertyCreationDialog(this);
                dialogView.setEditTextVisible(false);
                dialogView.setTitle("Delete Marker?");
                dialogView.setPositiveLabel("Ok");
                dialogView.setPositiveListener(view -> {
                    if (latLngList.size() > 0) {
                        latLngList.remove(marker.getPosition());
                        if (latLngList != null && latLngList.size() > 0) {
                            setPolylineView();
                        } else {
                            if (mMap != null)
                                mMap.clear();
                        }
                    }
                    dialogView.dismiss();
                });
                dialogView.setNegativeLabel("Cancel");
                dialogView.setNegativeListener(v -> {
                    dialogView.dismiss();
                });
                dialogView.show();
            } else if (mapTypeData == 3) {
                if (marker.getZIndex() == -2) {
                    PropertyCreationDialog dialogView = new PropertyCreationDialog(this);
                    dialogView.setEditTextVisible(false);
                    dialogView.setTitle("Delete Marker?");
                    dialogView.setPositiveLabel("Ok");
                    dialogView.setPositiveListener(view -> {
                        if (latLngList.size() > 0) {
                            latLngList.remove(marker.getPosition());
                            if (latLngList != null && latLngList.size() > 0) {
                                setPolygoneView();
                            } else {
                                if (mMap != null)
                                    mMap.clear();
                            }
                        }
                        dialogView.dismiss();
                    });
                    dialogView.setNegativeLabel("Cancel");
                    dialogView.setNegativeListener(v -> {
                        dialogView.dismiss();
                    });
                    dialogView.show();
                }
            }
        }
        return true;
    }

    Marker marker1;

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

    @Override
    public void onLocationChanged(Location location) {
//        this.currentLocation = location;
//        if (mMap != null) {
//            BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.blue_dot);
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!").icon(icon2);
//            mMap.addMarker(markerOptions);
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
////        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 21));
//        }
//        hideLoading();
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.blue_dot);
        currentLocation = location;
        if (marker1 != null) {
            marker1.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(icon);
        marker1 = mMap.addMarker(markerOptions);

        //move map camera
        if (mapDataTableList == null || mapDataTableList.size() < 1) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        }

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, PropertySurvey.this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private static class MarkerTag implements Serializable {
        private LatLng latLng;
        private int position;

        public LatLng getLatLng() {
            return latLng;
        }

        public void setLatLng(LatLng latLng) {
            this.latLng = latLng;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    private void addPoint(LatLng latLng) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.new_point);
        marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .flat(true).icon(icon)
                .anchor(0.5f, 0.5f).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                marker = marker;
                markerList.clear();
                markerList.add(marker);
            }
        });
        markerList.add(marker);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(PropertySurvey.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    @Override
    public void anotherizedToken() {

    }

    @Override
    public void getSnackBarView(String msg) {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        Snackbar snackbar = Snackbar
                .make(relativeLayout, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        Typeface font = Typeface.createFromAsset(getApplication().getAssets(), "font/roboto_bold.ttf");
        TextView textView = (TextView) snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTypeface(font);
        snackBarView.setBackgroundColor(this.getResources().getColor(R.color.red));
        snackbar.show();
    }

    Polygon polygon = null;

    @Override
    public void polygonStartClick() {
        propertySurveyBinding.polygonLay.setVisibility(View.VISIBLE);
    }

    @Override
    public void polygonSaveClick() {

    }

    @Override
    public void polygonPauseClick() {
        propertySurveyBinding.polygonResume.setVisibility(View.VISIBLE);
        propertySurveyBinding.polygonPause.setVisibility(View.GONE);
    }

    @Override
    public void polygonResumeClick() {
        propertySurveyBinding.polygonResume.setVisibility(View.GONE);
        propertySurveyBinding.polygonPause.setVisibility(View.VISIBLE);
    }

    @Override
    public void polygonStopClick() {
        propertySurveyBinding.polygonLay.setVisibility(View.GONE);
        propertySurveyBinding.polygonSave.setVisibility(View.VISIBLE);
    }

    @Override
    public void polylineUndoClick() {
        double i1 = 0.0;
        if (mMap != null) {
            mMap.clear();
        }
        if (latLngList != null && latLngList.size() > 0) {
            if (latLngsdummyhistory != null && latLngsdummyhistory.size() > 0) {
                latLngList.remove(latLngsdummyhistory.get(latLngsdummyhistory.size() - 1));
                latLngsdummyhistory.remove(latLngsdummyhistory.size() - 1);
            } else {
                latLngList.remove(latLngList.size() - 1);
            }
            if (latLngList != null && latLngList.size() > 0) {
                setPolylineView();
            }

            currentLocation();

            if (latLngList.size() >= 2) {
                for (int i = 0; i < latLngList.size(); i++) {
                    if (i != latLngList.size() - 1) {
                        LatLng from = new LatLng(((latLngList.get(i).latitude)), ((latLngList.get(i).longitude)));
                        LatLng to = new LatLng(((latLngList.get(i + 1).latitude)), ((latLngList.get(i + 1).longitude)));

                        i1 += Double.parseDouble(mpresenter.getLineLength(from, to));
                        double amount1 = (i1);
                        DecimalFormat formatter1 = new DecimalFormat("#,###.00");
                        String formatted1 = formatter1.format(amount1);

                        propertySurveyBinding.distanceTextView.setText("Length: " + formatted1 + " m");
                    }
                }
            } else {
                propertySurveyBinding.distanceTextView.setText("Length: " + 0.0 + " m");
            }
        }


//        if (markerList.size() > 0) {
//
//            latLngList.remove(latLngList.size() - 1);
//            Marker marker1 = markerList.get(markerList.size() - 1);
//            marker1.remove();
//            markerList.remove(markerList.size() - 1);
//            for (int i = 0; i < polylineList.size(); i++) {
//                if (i == polylineList.size() - 1) {
//                    polylineList.remove(polylineList.size() - 1);
//                    polylineList.remove(polyline);
//                    if (polyline != null) polyline.remove();
//                    PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngList).color(Color.BLUE).width(10).clickable(true);
//                    polyline = mMap.addPolyline(polylineOptions);
//                    i--;
//                }
//            }
//        }
    }

    @Override
    public void polylineClearClick() {

        CutomAlertBox cutomAlertBox = new CutomAlertBox(PropertySurvey.this);

        cutomAlertBox.setTitle("Do you want to clear survey ?");
        cutomAlertBox.setPositiveListener(view -> {

            if (polyline != null) polyline.remove();
            for (Marker marker : markerList)
                marker.remove();
            latLngList.clear();
            markerList.clear();

            currentLocation();
            cutomAlertBox.dismiss();
            Toast.makeText(this, "Survey removed successfully", Toast.LENGTH_SHORT).show();

            propertySurveyBinding.distanceTextView.setText("Length :" + 0.0 + "m");

        });
        cutomAlertBox.setNegativeListener(v -> cutomAlertBox.dismiss());
        cutomAlertBox.show();


    }

    @Override
    public void polylineSaveClick() {
        if (polylineList.size() < 2) {

//            Toast toast = Toast.makeText(PropertySurvey.this, "Please select a polyline on map", Toast.LENGTH_SHORT);
//            toast.getView().setBackground(getResources().getDrawable(R.drawable.toast_bg));
//            TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                Typeface typeface = Typeface.createFromAsset(getApplication().getAssets(), "font/roboto_bold.ttf");
//                text.setTypeface(typeface);
//                text.setTextColor(Color.WHITE);
//                text.setTextSize(14);
//            }
//            toast.show();

            Toast.makeText(this, "Please select a polyline on map", Toast.LENGTH_SHORT).show();

        } else {
            showPolylineDialog();
        }
    }

    @Override
    public void pointSaveClick() {
        if (markerList.size() < 1) {
//                    Toast.makeText(PropertySurvey.this, "Please select a point on map", Toast.LENGTH_SHORT).show();

//            Toast toast = Toast.makeText(PropertySurvey.this, "Please select a point on map", Toast.LENGTH_SHORT);
//            toast.getView().setBackground(getResources().getDrawable(R.drawable.toast_bg));
//            TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                Typeface typeface = Typeface.createFromAsset(getApplication().getAssets(), "font/roboto_bold.ttf");
//                text.setTypeface(typeface);
//                text.setTextColor(Color.WHITE);
//                text.setTextSize(14);
//            }
//            toast.show();

            Toast.makeText(this, "Please select a point on map", Toast.LENGTH_SHORT).show();

        } else {
            LatLng latLng = new LatLng(markerList.get(0).getPosition().latitude, markerList.get(0).getPosition().longitude);
            showPointDialog(latLng);
        }
    }

    @Override
    public void polygonManualClear() {
        CutomAlertBox cutomAlertBox = new CutomAlertBox(PropertySurvey.this);

        cutomAlertBox.setTitle("Do you want to delete survey ?");
        cutomAlertBox.setPositiveListener(view -> {


            if (polygon != null) polygon.remove();
            for (Marker marker : markerList)
                marker.remove();
            latLngList.clear();
            markerList.clear();
            if (mMap != null) {
                mMap.clear();
            }
//        propertySurveyBinding.polygonArea.setText("Area :" + mpresenter.getPolygonArea(latLngList));
            setMeasurements(0.0, latLngList);

            currentLocation();

            cutomAlertBox.dismiss();
            Toast.makeText(this, "Survey removed successfully", Toast.LENGTH_SHORT).show();
        });
        cutomAlertBox.setNegativeListener(v -> cutomAlertBox.dismiss());
        cutomAlertBox.show();

    }

    @Override
    public void polygonManualUndo() {
        double polygoni1 = 0.0;
        if (latLngList != null && latLngList.size() > 0) {
            if (latLngsdummyhistory != null && latLngsdummyhistory.size() > 0) {
                latLngList.remove(latLngsdummyhistory.get(latLngsdummyhistory.size() - 1));
                latLngsdummyhistory.remove(latLngsdummyhistory.size() - 1);
            } else {
                latLngList.remove(latLngList.size() - 1);
            }
        } else {
            if (mMap != null)
                mMap.clear();
        }
        if (latLngList.size() > 0) {
            setPolygoneView();
        }

        currentLocation();

        setMeasurements(polygoni1, latLngList);

//        if (markerList.size() > 0) {
//
//            latLngList.remove(latLngList.size() - 1);
//
//            Marker marker1 = markerList.get(markerList.size() - 1);
//            marker1.remove();
//            markerList.remove(markerList.size() - 1);
//
//            for (int i = 0; i < polygonList.size(); i++) {
//                if (polygonList.size() == 1 || markerList.size() == 0) {
//                    polygonManualClear();
//                    polygonList.clear();
//                }
//                if (i == polygonList.size() - 1) {
//                    polygonList.remove(polygonList.size() - 1);
//                    polygonList.remove(polygon);
//                    if (polygon != null) polygon.remove();
//                    PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngList).strokeWidth(5).strokeColor(Color.RED).fillColor(getResources().getColor(R.color.alpha_ripple_effect_btn_color)).clickable(true);
//                    polygon = mMap.addPolygon(polygonOptions);
//                    i--;
//                }
//            }
//
//            propertySurveyBinding.polygonArea.setText("Area :" + mpresenter.getPolygonArea(latLngList));
//
//        }
    }

    private void setPolygoneView() {
        if (mMap != null) {
            mMap.clear();
        }
        for (LatLng latLngs : latLngList) {
            MarkerOptions markerOptions = new MarkerOptions().position(latLngs).zIndex(-2).snippet(latLngList.indexOf(latLngs) + "").draggable(true);
            marker = mMap.addMarker(markerOptions);
            markerList.add(marker);
        }
        BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.marker_yellow_icon);
        if (latLngList.size() > 1) {
            for (int i = 0; i < latLngList.size(); i++) {
                if (i == latLngList.size() - 1) {
                    LatLng latLng1 = new LatLng((latLngList.get(i).latitude + latLngList.get(0).latitude) / 2, (latLngList.get(i).longitude + latLngList.get(0).longitude) / 2);
                    MarkerOptions markerOptions1 = new MarkerOptions().position(latLng1).zIndex(-1).icon(icon2).draggable(true);
                    mMap.addMarker(markerOptions1);
                } else {
                    LatLng latLng1 = new LatLng((latLngList.get(i).latitude + latLngList.get(i + 1).latitude) / 2, (latLngList.get(i).longitude + latLngList.get(i + 1).longitude) / 2);
                    MarkerOptions markerOptions1 = new MarkerOptions().position(latLng1).zIndex(i + 1).icon(icon2).draggable(true);
                    mMap.addMarker(markerOptions1);
                }
            }
        }
        if (polygon != null) polygon.remove();
        PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngList).strokeWidth(5).strokeColor(getResources().getColor(R.color.colorPrimaryDark)).fillColor(getResources().getColor(R.color.yellow_transparent)).clickable(true);
        polygon = mMap.addPolygon(polygonOptions);
        polygonList.add(polygon);
    }

    private void setPolylineView() {
        if (mMap != null)
            mMap.clear();
        BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.marker_yellow_icon);
        for (LatLng latLng1 : latLngList) {
            MarkerOptions markerOptions = new MarkerOptions().position(latLng1).icon(icon2).zIndex(latLngList.indexOf(latLng1)).draggable(true);
            marker = mMap.addMarker(markerOptions);
            markerList.add(marker);
        }
        if (polyline != null) polyline.remove();
        PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngList).color(getResources().getColor(R.color.colorPrimaryDark)).width(5).clickable(true);
        polyline = mMap.addPolyline(polylineOptions);
        polylineList.add(polyline);

        for (LatLng latLngPol : latLngList) {

            int position = latLngList.indexOf(latLngPol);
            MarkerTag yourMarkerTag = new MarkerTag();
            yourMarkerTag.setLatLng(latLngPol);
            yourMarkerTag.setPosition(position);
            marker.setTag(yourMarkerTag);

            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {
                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    updateMarkerLocation(marker);
                    double j = 0.0;
                    if (latLngList.size() > 2) {
                        for (int i = 0; i < latLngList.size(); i++) {
                            if (i != latLngList.size() - 1) {
                                LatLng from = new LatLng(((latLngList.get(i).latitude)), ((latLngList.get(i).longitude)));
                                LatLng to = new LatLng(((latLngList.get(i + 1).latitude)), ((latLngList.get(i + 1).longitude)));

                                j += Double.parseDouble(mpresenter.getLineLength(from, to));
                                double amount1 = (j);
                                DecimalFormat formatter1 = new DecimalFormat("#,###.00");
                                String formatted1 = formatter1.format(amount1);

                                propertySurveyBinding.distanceTextView.setText("Length: " + formatted1 + " m");
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void polygonManualSave() {
        showPolygonDialog();
    }

    @Override
    public void polygonWalkClear() {

    }

    @Override
    public void polygonWalkStart() {

    }

    private void polygonPolyline() {
        if (latLngList.size() > 1) {
            PolygonOptions options = new PolygonOptions();//.strokePattern(pattern)
            if (runningPathPolygon != null) {
                runningPathPolygon.remove();
                mMap.clear();
            }

            this.runningPathPolygon = mMap.addPolygon(options
                    .addAll(latLngList));
            runningPathPolygon.setFillColor(Color.argb(20, 0, 255, 0));
            for (LatLng latLng : latLngList) {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.blue_circle);
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .flat(true).icon(icon)
                        .anchor(0.5f, 0.5f).draggable(true));

                int position = latLngList.indexOf(latLng);
                MarkerTag yourMarkerTag = new MarkerTag();
                yourMarkerTag.setLatLng(latLng);
                yourMarkerTag.setPosition(position);
                marker.setTag(yourMarkerTag);
//                Toast.makeText(SurveyTrackingActivity.this, "computeArea" +mpresenter.getPolygonArea(polygonPoints)+"sq ft", Toast.LENGTH_LONG).show();
                mMap.setOnMarkerClickListener(PropertySurvey.this);
                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {
                        //  updateMarkerLocation(marker);
                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        updateMarkerLocation(marker);
                    }
                });
            }
        }
    }

    @Override
    public void polygonWalkStop() {

    }

    @Override
    public void polygonWalkSave() {

    }

    private boolean updatedvalue;

    private void showPolygonDialog() {
        if (latLngList.size() > 2) {
            PropertyCreationDialog dialogView = new PropertyCreationDialog(this);
            dialogView.setTitle("Survey Details");
            dialogView.setPositiveLabel("Save");
            if (mapDataTableList != null && mapDataTableList.size() > 0) {
                mapDataTableList.get(pos).setLatLngList(latLngList);
                dialogView.setEditTextData(mapDataTableList.get(pos).getName());
                dialogView.setEditTextDescriptionData(mapDataTableList.get(pos).getDescription());
            }
            dialogView.setPositiveListener(view -> {
                if (dialogView.validations()) {
                    dialogView.dismiss();
                    if (latLngList != null && latLngList.size() > 0) {
//                    if (latLngList.size() == markerList.size()) {
//                        for (int i = 0; i < latLngList.size(); i++) {
                        if (mapDataTableList != null && mapDataTableList.size() > 0) {
                            updatedvalue = true;
//                        MapDataTable mapDataTable = new MapDataTable();
//                        mapDataTable.setId(mapDataTableList.get(pos).getId());
//                        mapDataTable.setPropertyID(mapDataTableList.get(pos).getPropertyID());
//                        mapDataTable.setMapType(mapDataTableList.get(pos).getMapType());
//                        mapDataTable.setLatLngList(mapDataTableList.get(pos).getLatLngList());
//                        mapDataTable.setName(mapDataTableList.get(pos).getName());
//                        mapDataTable.setDescription(mapDataTableList.get(pos).getDescription());
//                        mapDataTable.setPointPhotoData(mapDataTableList.get(pos).getPointPhotoData());
                            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                            Date todayDate = new Date();
                            String thisDate = currentDate.format(todayDate);
                            mapDataTableList.get(pos).setLastUpdatedDate(thisDate);
                            mapDataTableList.get(pos).setPointPhotoData(imagesUploadedList);
                            mapDataTableList.get(pos).setAreaDistance(propertySurveyBinding.polygonArea.getText().toString());
                            mpresenter.updateMapDataList(mapDataTableList.get(pos));
                            Toast.makeText(PropertySurvey.this, "Survey Details are updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            updatedvalue = false;
                            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                            Date todayDate = new Date();
                            String thisDate = currentDate.format(todayDate);

                            MapDataTable mapDataTable = new MapDataTable(propertyId, mapTypeData, latLngList, dialogView.getPointName(), dialogView.getPointDescription(), imagesUploadedList, thisDate, propertySurveyBinding.polygonArea.getText().toString(), measurements, thisDate);
                            mpresenter.insertMapTypeDataTable(mapDataTable);
                            Toast.makeText(PropertySurvey.this, "Survey Details are saved successfully", Toast.LENGTH_SHORT).show();
                        }
//                        }
//                    }
                    }
//                Toast toast =
//                toast.getView().setBackground(getResources().getDrawable(R.drawable.toast_bg));
//                TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    Typeface typeface = Typeface.createFromAsset(getApplication().getAssets(), "font/roboto_bold.ttf");
//                    text.setTypeface(typeface);
//                    text.setTextColor(Color.WHITE);
//                    text.setTextSize(14);
//                }
//                toast.show();
                    Intent intent = new Intent();
                    intent.putExtra("dialogName", dialogView.getPointName());
                    if (mapDataTableList != null && mapDataTableList.size() > 0) {
                        Gson gson = new Gson();
                        String myJson = gson.toJson(mapDataTableList);
                        intent.putExtra("mapDataTableListUnchecked", myJson);
                        intent.putExtra("updatedvalue", updatedvalue);
                    }
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            dialogView.setPositiveUploadImageListener(view -> {
                startActivityForResult(PhotoUpload.getStartIntent(PropertySurvey.this), PHOTO_UPLOAD);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            });
            dialogView.setNegativeLabel("Cancel");
            dialogView.setNegativeListener(v -> {
                dialogView.dismiss();
//            Toast.makeText(PropertySurvey.this, "PointDetails are not saved", Toast.LENGTH_LONG).show();
//            Toast toast = Toast.makeText(PropertySurvey.this, "Polygon Details are not saved", Toast.LENGTH_SHORT);
//            toast.getView().setBackground(getResources().getDrawable(R.drawable.toast_bg));
//            TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                Typeface typeface = Typeface.createFromAsset(getApplication().getAssets(), "font/roboto_bold.ttf");
//                text.setTypeface(typeface);
//                text.setTextColor(Color.WHITE);
//                text.setTextSize(14);
//            }
//            toast.show();
            });
            dialogView.show();
        } else {
            Toast.makeText(this, "Please draw a polygon area", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPolylineDialog() {
        if (latLngList.size() > 1) {
            PropertyCreationDialog dialogView = new PropertyCreationDialog(this);
            dialogView.setTitle("Survey Details");
            dialogView.setPositiveLabel("Save");
            if (mapDataTableList != null && mapDataTableList.size() > 0) {
                mapDataTableList.get(pos).setLatLngList(latLngList);
                dialogView.setEditTextData(mapDataTableList.get(pos).getName());
                dialogView.setEditTextDescriptionData(mapDataTableList.get(pos).getDescription());
            }
            dialogView.setPositiveListener(view -> {
                if (dialogView.validations()) {
                    dialogView.dismiss();
                    if (latLngList != null && latLngList.size() > 0) {
//                    if (latLngList.size() == markerList.size()) {
//                        for (int i = 0; i < latLngList.size(); i++) {
                        if (mapDataTableList != null && mapDataTableList.size() > 0) {
                            updatedvalue = true;
//                        MapDataTable mapDataTable = new MapDataTable();
//                        mapDataTable.setPropertyID(mapDataTableList.get(pos).getPropertyID());
//                        mapDataTable.setMapType(mapDataTableList.get(pos).getMapType());
//                        mapDataTable.setLatLngList(mapDataTableList.get(pos).getLatLngList());
//                        mapDataTable.setName(mapDataTableList.get(pos).getName());
//                        mapDataTable.setDescription(mapDataTableList.get(pos).getDescription());
//                        mapDataTable.setPointPhotoData(mapDataTableList.get(pos).getPointPhotoData());
                            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                            Date todayDate = new Date();
                            String thisDate = currentDate.format(todayDate);
                            mapDataTableList.get(pos).setLastUpdatedDate(thisDate);
                            mapDataTableList.get(pos).setPointPhotoData(imagesUploadedList);
                            mapDataTableList.get(pos).setAreaDistance(propertySurveyBinding.distanceTextView.getText().toString());
                            mpresenter.updateMapDataList(mapDataTableList.get(pos));
                            Toast.makeText(PropertySurvey.this, "Survey Details are updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            updatedvalue = false;
                            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                            Date todayDate = new Date();
                            String thisDate = currentDate.format(todayDate);
                            MapDataTable polylineDataTable = new MapDataTable(propertyId, mapTypeData, latLngList, dialogView.getPointName(), dialogView.getPointDescription(), imagesUploadedList, thisDate, propertySurveyBinding.distanceTextView.getText().toString(), "m", thisDate);
                            mpresenter.insertMapTypeDataTable(polylineDataTable);
                            Toast.makeText(PropertySurvey.this, "Survey Details are saved successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                    Intent intent = new Intent();
                    intent.putExtra("dialogName", dialogView.getPointName());
                    if (mapDataTableList != null && mapDataTableList.size() > 0) {
                        Gson gson = new Gson();
                        String myJson = gson.toJson(mapDataTableList);
                        intent.putExtra("mapDataTableListUnchecked", myJson);
                        intent.putExtra("updatedvalue", updatedvalue);
                    }
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            dialogView.setPositiveUploadImageListener(view -> {
                startActivityForResult(PhotoUpload.getStartIntent(PropertySurvey.this), PHOTO_UPLOAD);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            });
            dialogView.setNegativeLabel("Cancel");
            dialogView.setNegativeListener(v -> {
                dialogView.dismiss();
            });
            dialogView.show();
        } else {
            Toast.makeText(this, "Please draw a polyline", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPointDialog(LatLng latLng) {
        PropertyCreationDialog dialogView = new PropertyCreationDialog(this);
        dialogView.setTitle("Survey Details");
        dialogView.setPositiveLabel("Save");

        if (mapDataTableList != null && mapDataTableList.size() > 0) {
            mapDataTableList.get(pos).setLatLngList(latLngList);
            dialogView.setEditTextData(mapDataTableList.get(pos).getName());
            dialogView.setEditTextDescriptionData(mapDataTableList.get(pos).getDescription());
        }

        dialogView.setPositiveListener(view -> {
            if (dialogView.validations()) {
                dialogView.dismiss();
                if (latLngList != null && latLngList.size() > 0) {
                    if (mapDataTableList != null && mapDataTableList.size() > 0) {
                        updatedvalue = true;
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                        Date todayDate = new Date();
                        String thisDate = currentDate.format(todayDate);
                        mapDataTableList.get(pos).setLastUpdatedDate(thisDate);
                        mapDataTableList.get(pos).setPointPhotoData(imagesUploadedList);
                        mapDataTableList.get(pos).setAreaDistance(propertySurveyBinding.distanceTextView.getText().toString());
                        mpresenter.updateMapDataList(mapDataTableList.get(pos));
                        Toast.makeText(PropertySurvey.this, "Survey Details are updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        updatedvalue = false;
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                        Date todayDate = new Date();
                        String thisDate = currentDate.format(todayDate);
                        MapDataTable pointDataTable = new MapDataTable(propertyId, mapTypeData, latLngList, dialogView.getPointName(), dialogView.getPointDescription(), imagesUploadedList, thisDate, "", "", thisDate);
                        mpresenter.insertMapTypeDataTable(pointDataTable);
                        Toast.makeText(PropertySurvey.this, "Survey Details are saved successfully", Toast.LENGTH_SHORT).show();
                    }
                }

                Intent intent = new Intent();
                intent.putExtra("dialogName", dialogView.getPointName());
                if (mapDataTableList != null && mapDataTableList.size() > 0) {
                    Gson gson = new Gson();
                    String myJson = gson.toJson(mapDataTableList);
                    intent.putExtra("mapDataTableListUnchecked", myJson);
                    intent.putExtra("updatedvalue", updatedvalue);
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        dialogView.setPositiveUploadImageListener(view -> {
            startActivityForResult(PhotoUpload.getStartIntent(PropertySurvey.this), PHOTO_UPLOAD);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        });
        dialogView.setNegativeLabel("Cancel");
        dialogView.setNegativeListener(v -> {
            dialogView.dismiss();
            Toast.makeText(PropertySurvey.this, "PointDetails are not saved", Toast.LENGTH_LONG).show();
        });
        dialogView.show();
    }

    List<String> imagesUploadedList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_UPLOAD:
                    if (data != null) {
                        List<String> imageStringListPaths = (List<String>) data.getSerializableExtra("mpaths");
                        imagesUploadedList = imageStringListPaths;
                    }
                    break;
                default:
            }
        }

    }


}
