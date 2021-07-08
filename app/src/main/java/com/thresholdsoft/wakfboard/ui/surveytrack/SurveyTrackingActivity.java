package com.thresholdsoft.wakfboard.ui.surveytrack;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.thresholdsoft.wakfboard.BuildConfig;
import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.data.db.model.FarmerLands;
import com.thresholdsoft.wakfboard.data.db.model.SurveyEntity;
import com.thresholdsoft.wakfboard.data.network.pojo.MapTypeEntity;
import com.thresholdsoft.wakfboard.data.network.pojo.SurveyDetailsEntity;
import com.thresholdsoft.wakfboard.data.network.pojo.SurveySaveReq;
import com.thresholdsoft.wakfboard.databinding.ActivitySurveyTrackingBinding;
import com.thresholdsoft.wakfboard.root.WaveApp;
import com.thresholdsoft.wakfboard.services.ConnectivityReceiver;
import com.thresholdsoft.wakfboard.services.LocationMonitoringService;
import com.thresholdsoft.wakfboard.ui.base.BaseActivity;
import com.thresholdsoft.wakfboard.ui.dialog.SurveyPointDialog;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.SurveyStatusActivity;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.dialog.deletedialog.DeleteDialog;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.model.SurveyDetailsModel;
import com.thresholdsoft.wakfboard.ui.surveytrack.model.MarkerTag;
import com.thresholdsoft.wakfboard.ui.surveytrack.model.SurveyModel;
import com.thresholdsoft.wakfboard.ui.userlogin.UserLoginActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SurveyTrackingActivity extends BaseActivity implements SurveyTrackMvpView, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG = SurveyTrackingActivity.class.getSimpleName();

    @Inject
    SurveyTrackMvpPresenter<SurveyTrackMvpView> mpresenter;
    private ActivitySurveyTrackingBinding surveyTrackingBinding;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private Marker pointMarker;
    private Marker myMarker;
    private boolean mAlreadyStartedService = false;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Polyline runningPathPolyline;
    private Polygon runningPathPolygon;
    private ArrayList<Location> locationList = new ArrayList<>();
    private ArrayList<SurveyDetailsEntity> surveyModelArrayList = new ArrayList<>();
    private boolean isStartLogging = false;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private List<LatLng> polygonPoints = new ArrayList<>();
    boolean zoomable = true;
    boolean didInitialZoom;
    private FarmerLands surveyModel;
    private SurveyEntity surveyEntity;
    private List<Marker> markerList = new ArrayList<>();
    private BroadcastReceiver MyReceiver = null;
    View mapView;

    public static Intent getIntent(Context context, FarmerLands surveyEntity, int mapType) {
        Intent intent = new Intent(context, SurveyTrackingActivity.class);
        intent.putExtra("surveyEntity", surveyEntity);
        intent.putExtra("map_type", mapType);
        return intent;
    }

    public static Intent getIntent(Context context, FarmerLands surveyEntity, int mapType, boolean isEdit, SurveyEntity surveyDetails) {
        Intent intent = new Intent(context, SurveyTrackingActivity.class);
        intent.putExtra("surveyEntity", surveyEntity);
        intent.putExtra("map_type", mapType);
        intent.putExtra("polygonArrayData", surveyDetails);
        intent.putExtra("my_boolean_key", isEdit);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surveyTrackingBinding = DataBindingUtil.setContentView(this, R.layout.activity_survey_tracking);
        getActivityComponent().inject(this);
        mpresenter.onAttach(SurveyTrackingActivity.this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapView = mapFragment.getView();
            mapFragment.getMapAsync(this);
        }
        MyReceiver = new ConnectivityReceiver();
        setUp();
    }

    private boolean firstTimeZoom = false;

    @Override
    protected void setUp() {
        surveyTrackingBinding.setView(this);
        surveyModel = (FarmerLands) getIntent().getSerializableExtra("surveyEntity");
        surveyTrackingBinding.setType(getIntent().getIntExtra("map_type", 0));
        surveyTrackingBinding.setSurvey(surveyModel);
        if (getSurveyType() == 0) {
            surveyTrackingBinding.typeTextview.setBackgroundResource(R.drawable.new_point);
        } else if (getSurveyType() == 1) {
            surveyTrackingBinding.typeTextview.setBackgroundResource(R.drawable.new_line);
        } else if (getSurveyType() == 2) {
            surveyTrackingBinding.typeTextview.setBackgroundResource(R.drawable.new_polygon);
        }
        broadCastReceivers();
        setUpGClient();
        View includedLayout = findViewById(R.id.backArrow);
        ImageView insideTheIncludedLayout = (ImageView) includedLayout.findViewById(R.id.imageButton);
        insideTheIncludedLayout.setOnClickListener(v -> onBackPressed());
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
                            surveyTrackingBinding.progressBar.setVisibility(View.GONE);
                            boolean myeditMode = getIntent().getBooleanExtra("my_boolean_key", false);
                            if (!myeditMode) {
                                if (!firstTimeZoom) {
                                    firstTimeZoom = true;
                                    zoomMapTo(location);
                                }
                            }
                            surveyTrackingBinding.accuracyTextView.setText("Accuracy\n" + location.getAccuracy());
                            if (getSurveyType() == 0) {
                                surveyTrackingBinding.distanceTextView.setText("Length\n" + "0.00 LG");
                            } else if (getSurveyType() == 2) {
                                surveyTrackingBinding.distanceTextView.setText("Length\n" + "0.00 LG");
                            }
//                            surveyTrackingBinding.distanceTextView.setText("Distance\n " + mpresenter.getTravelledDistance(locationList));
                            surveyTrackingBinding.polygonArea.setText("Area\n" + mpresenter.getPolygonArea(polygonPoints));
                            if (isStartLogging) {
                                filterLocationLatLong(location);
                            }
                        }
                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        startStep1();

        // register connection status listener
        WaveApp.getInstance().setConnectivityListener(this);
        registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


    /**
     * Step 1: Check Google Play services
     */
    private void startStep1() {
        //Check whether this user has installed Google play service which is being used by Location updates.
        if (isGooglePlayServicesAvailable()) {
            if (checkPermissions()) { //Yes permissions are granted by the user. Go to the next step.
                startStep2();
            } else {  //No user has not granted the permissions yet. Request now.
                requestPermissions();
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.no_google_playservice_available, Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Step 2: Start the Location Monitor Service
     */
    private void startStep2() {
        //And it will be keep running until you close the entire application from task manager.
        //This method will executed only once.
        if (!mAlreadyStartedService) {
            //Start location sharing service to app server.........
            Intent intent = new Intent(this, LocationMonitoringService.class);
            startService(intent);
            mAlreadyStartedService = true;
            //Ends................................................
        }
    }

    /**
     * Return the availability of GooglePlayServices
     */
    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }


    /**
     * Return the current state of the permissions needed.
     */

    private boolean checkPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        int permissionState2 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;

    }

    /**
     * Start permissions requests.
     */
    private void requestPermissions() {

        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        boolean shouldProvideRationale2 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);


        // Provide an additional rationale to the img_user. This would happen if the img_user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            ActivityCompat.requestPermissions(SurveyTrackingActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else if (shouldProvideRationale2) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            ActivityCompat.requestPermissions(SurveyTrackingActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the img_user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(SurveyTrackingActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    /**
     * Shows a {@link Snackbar}.
     *
     * @param listener The listener associated with the Snackbar action.
     */
    private void showSnackbar(View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(R.string.permission_denied_explanation),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.settings), listener).show();
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If img_user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.i(TAG, "Permission granted, updates requested, starting location updates");

                getMyLocation();
            } else {
                // Permission denied.

                // Notify the img_user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the img_user for permission (device policy or "Never ask
                // again" prompts). Therefore, a img_user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(
                        view -> {
                            // Build intent that displays the App settings screen.
                            Intent intent = new Intent();
                            intent.setAction(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        });
            }
        }
    }


    @Override
    public void onDestroy() {
        //Stop location sharing service to app server.........
        stopService(new Intent(this, LocationMonitoringService.class));
        mAlreadyStartedService = false;
        //Ends................................................
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
// position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.setMargins(0, 100, 180, 0);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setOnMapClickListener(point -> {
            if (getSurveyType() == 0) {
                if (markerList.size() < 1) {
                    addPoint(point);
                }
            } else if (getSurveyType() == 1) {
                if (markerList.size() < 2) {
                    BitmapDescriptor blueDot = BitmapDescriptorFactory.fromResource(R.mipmap.blue_circle);
                    myMarker = mMap.addMarker(new MarkerOptions()
                            .position(point)
                            .flat(true).icon(blueDot)
                            .anchor(0.5f, 0.5f));
                    mMap.setOnMarkerClickListener(SurveyTrackingActivity.this);
                    markerList.add(myMarker);
                    drawLine();
                }
            }
            Log.d("DEBUG", "Map clicked [" + point.latitude + " / " + point.longitude + "]");
        });
        Bundle bundle = getIntent().getExtras();
        ///Polygon edit
        assert bundle != null;
        boolean myeditMode = bundle.getBoolean("my_boolean_key");
        if (myeditMode) {
            surveyEntity = (SurveyEntity) getIntent().getSerializableExtra("polygonArrayData");
            if (getSurveyType() == 2) {
                surveyTrackingBinding.setSave(true);
            }
            boolean isIncludeLatLong = false;
            if (surveyEntity != null) {
                Gson gson = new Gson();
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                if (surveyEntity.getMapType() != null) {
                    if (getSurveyType() == 0) {
                        try {
                            SurveyModel.PointDetails pointDetails = gson.fromJson(surveyEntity.getLatLongs(), SurveyModel.PointDetails.class);
                            if (pointDetails != null) {
                                LatLng latLng = new LatLng(pointDetails.getLatitude(), pointDetails.getLongitude());
                                builder.include(latLng);
                                addPoint(latLng);
                                isIncludeLatLong = true;
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    } else if (getSurveyType() == 1) {
                        SurveyModel.PolyLineDetails polyLineDetails = gson.fromJson(surveyEntity.getLatLongs(), SurveyModel.PolyLineDetails.class);
                        LatLng from = new LatLng(((polyLineDetails.getFromLatitude())), ((polyLineDetails.getFromLongitude())));
                        LatLng to = new LatLng(((polyLineDetails.getToLatitude())), ((polyLineDetails.getToLongitude())));
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.blue_circle);
                        Marker marker1 = mMap.addMarker(new MarkerOptions()
                                .position(from)
                                .flat(true).icon(icon)
                                .anchor(0.5f, 0.5f).draggable(true));
                        markerList.add(marker1);
                        Marker marker2 = mMap.addMarker(new MarkerOptions()
                                .position(to)
                                .flat(true).icon(icon)
                                .anchor(0.5f, 0.5f).draggable(true));
                        markerList.add(marker2);
                        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                            @Override
                            public void onMarkerDragStart(Marker marker) {

                            }

                            @Override
                            public void onMarkerDrag(Marker marker) {
                            }

                            @Override
                            public void onMarkerDragEnd(Marker marker) {
                                markerList.clear();
                                markerList.add(marker1);
                                markerList.add(marker2);
                                drawLine();
                            }
                        });
                        planePolyline(from, to);
                        builder.include(new LatLng(polyLineDetails.getFromLatitude(), polyLineDetails.getFromLongitude()));
                        builder.include(new LatLng(polyLineDetails.getToLatitude(), polyLineDetails.getToLongitude()));
                        isIncludeLatLong = true;
                    } else if (getSurveyType() == 2) {
                        Type listType = new TypeToken<List<SurveyModel>>() {
                        }.getType();
                        List<SurveyModel> posts = gson.fromJson(surveyEntity.getLatLongs(), listType);
                        for (SurveyModel model : posts) {
                            isIncludeLatLong = true;
                            LatLng location = new LatLng(model.getLatitude(), model.getLongitude());
                            builder.include(location);
                            polygonPoints.add(location);
                            surveyModelArrayList.add(new SurveyDetailsEntity(model.getLatitude(), model.getLongitude(), model.getAccuracy()));
                        }

                        polygonPolyline();
                    }

                    if (isIncludeLatLong) {
                        LatLngBounds bounds = builder.build();
                        int padding = 30; // offset from edges of the map in pixels
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        mMap.animateCamera(cu);
                    }
                }
            }
        }


    }

    private void filterLocationLatLong(Location location) {
        long age = getLocationAge(location);
        if (age > 5 * 1000) { //more than 5 seconds
            Log.d(TAG, "Location is old");
            // oldLocationList.add(location);
            return;
        }

        if (location.getAccuracy() <= 0) {
            Log.d(TAG, "Latitidue and longitude values are invalid.");
            //  noAccuracyLocationList.add(location);
            return;
        }

        float horizontalAccuracy = location.getAccuracy();
        Log.e("Accuracy", String.valueOf(horizontalAccuracy));

        if (getSurveyType() == 0) {
            //  dottedPolyline();
        } else if (getSurveyType() == 1) {
            //  planePolyline();
        } else if (getSurveyType() == 2) {
            boolean updateLocation = true;
            if (locationList.size() == 0) {
                locationList.add(location);
            }
            if (locationList.size() > 1) {
                Location prevLocation = locationList.get(locationList.size() - 1);
                if (prevLocation != null) {
                    updateLocation = location.distanceTo(prevLocation) >= 1;
                }
            }
            if (updateLocation) {
                locationList.add(location);
                polygonPoints.add(new LatLng(location.getLatitude(), location.getLongitude()));
                surveyModelArrayList.add(new SurveyDetailsEntity(location.getLatitude(), location.getLongitude(), location.getAccuracy()));
                polygonPolyline();
            }
        }

    }

    private void zoomMapTo(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (!this.didInitialZoom) {
            try {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.5f));
                this.didInitialZoom = true;
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (zoomable) {
            try {
                zoomable = false;
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng),
                        new GoogleMap.CancelableCallback() {
                            @Override
                            public void onFinish() {
                                zoomable = true;
                            }

                            @Override
                            public void onCancel() {
                                zoomable = true;
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void planePolyline(LatLng fromPolyLineLatLng, LatLng toPolyLineLatLng) {
        LatLng from = new LatLng(((fromPolyLineLatLng.latitude)), ((fromPolyLineLatLng.longitude)));
        LatLng to = new LatLng(((toPolyLineLatLng.latitude)), ((toPolyLineLatLng.longitude)));
        PolylineOptions options = new PolylineOptions();
        clearPolyline();
        int polylineWidth = 10;
        this.runningPathPolyline = mMap.addPolyline(options
                .add(from, to)
                .width(polylineWidth).color(Color.parseColor("#009919")).geodesic(true));
        surveyTrackingBinding.distanceTextView.setText("Length\n " + mpresenter.getLineLength(from, to));
//        Toast.makeText(this, "Length"+mpresenter.getLineLength(from,to), Toast.LENGTH_SHORT).show();

    }

    private void polygonPolyline() {
        if (polygonPoints.size() > 1) {
            PolygonOptions options = new PolygonOptions();//.strokePattern(pattern)
            if (runningPathPolygon != null) {
                runningPathPolygon.remove();
                mMap.clear();
            }

            this.runningPathPolygon = mMap.addPolygon(options
                    .addAll(polygonPoints));
            runningPathPolygon.setFillColor(Color.argb(20, 0, 255, 0));
            for (LatLng latLng : polygonPoints) {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.blue_circle);
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .flat(true).icon(icon)
                        .anchor(0.5f, 0.5f).draggable(true));

                int position = polygonPoints.indexOf(latLng);
                MarkerTag yourMarkerTag = new MarkerTag();
                yourMarkerTag.setLatLng(latLng);
                yourMarkerTag.setPosition(position);
                marker.setTag(yourMarkerTag);
//                Toast.makeText(SurveyTrackingActivity.this, "computeArea" +mpresenter.getPolygonArea(polygonPoints)+"sq ft", Toast.LENGTH_LONG).show();
                mMap.setOnMarkerClickListener(SurveyTrackingActivity.this);
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

    private void updateMarkerLocation(Marker marker) {
        MarkerTag tag = (MarkerTag) marker.getTag();
//        LatLng latLng=yourMarkerTag.getLatLng();
        assert tag != null;
        int position = polygonPoints.indexOf(tag.getLatLng());
        if (position != -1) {
            polygonPoints.set(position, marker.getPosition());
            surveyModelArrayList.get(position).setLatitude(marker.getPosition().latitude);
            surveyModelArrayList.get(position).setLongitude(marker.getPosition().longitude);
            MarkerTag markerTag = new MarkerTag();
            markerTag.setPosition(position);
            markerTag.setLatLng(marker.getPosition());
            marker.setTag(markerTag);
        } else {
            marker.remove();
        }

        polygonPolyline();
    }

    @SuppressLint("NewApi")
    private long getLocationAge(Location newLocation) {
        long locationAge;
        long currentTimeInMilli = SystemClock.elapsedRealtimeNanos() / 1000000;
        long locationTimeInMilli = newLocation.getElapsedRealtimeNanos() / 1000000;
        locationAge = currentTimeInMilli - locationTimeInMilli;
        return locationAge;
    }

    private void clearPolyline() {
        if (runningPathPolyline != null) {
            runningPathPolyline.remove();
            runningPathPolyline = null;
        }
    }

    private synchronized void setUpGClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 1, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void getMyLocation() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(SurveyTrackingActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(mGoogleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(mGoogleApiClient, builder.build());
                    result.setResultCallback(result1 -> {
                        final Status status = result1.getStatus();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                // All location settings are satisfied.
                                // You can initialize location requests here.
                                startStep2();
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied.
                                // But could be fixed by showing the user a dialog.
                                try {
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    // Ask to turn on GPS automatically
                                    status.startResolutionForResult(SurveyTrackingActivity.this,
                                            REQUEST_CHECK_SETTINGS_GPS);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied.
                                // However, we have no way
                                // to fix the
                                // settings so we won't show the dialog.
                                // finish();
                                break;
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getMyLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS_GPS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    getMyLocation();
                    break;
                case Activity.RESULT_CANCELED:
                    finish();
                    break;
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public FarmerLands getFarmerLand() {
        return surveyModel;
    }

    @Override
    public String getSurveyId() {
        return "asdfg";
    }

    @Override
    public int getSurveyType() {
        return getIntent().getIntExtra("map_type", 0);
    }

    @Override
    public int getPolygonEditType() {
        return getIntent().getIntExtra("intmapType", 0);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClickPauseResumeBtn() {
        if (isStartLogging) {
            isStartLogging = false;
            surveyTrackingBinding.pauseResumeBtn.setText("Resume");
        } else {
            isStartLogging = true;
            surveyTrackingBinding.pauseResumeBtn.setText("Pause");
        }
    }

    @Override
    public void onClickStopBtn() {
        isStartLogging = false;
        surveyTrackingBinding.setStart(false);
        surveyTrackingBinding.setSave(true);
    }

    @Override
    public void onClickSavePoints() {
        if (getSurveyType() == 0) {
            LatLng latLng = new LatLng(markerList.get(0).getPosition().latitude, markerList.get(0).getPosition().longitude);
            showPointDialog(latLng);

        } else if (getSurveyType() == 1) {
            if (markerList.size() == 2) {
                SurveyPointDialog dialogView = new SurveyPointDialog(this);
                dialogView.setTitle("Line Details");
                boolean myeditMode = getIntent().getBooleanExtra("my_boolean_key", false);
                if (myeditMode) {
                    dialogView.setEditTextData(surveyEntity.getName());
                    dialogView.setEditTextDescriptionData(surveyEntity.getDescription());
                }
                dialogView.setPositiveLabel("Ok");
                dialogView.setPositiveListener(view -> {
                    if (dialogView.validations()) {
                        dialogView.dismiss();
                        SurveyModel.PolyLineDetails polyLineDetails = new SurveyModel.PolyLineDetails();
                        polyLineDetails.setFromLatitude(markerList.get(0).getPosition().latitude);
                        polyLineDetails.setFromLongitude(markerList.get(0).getPosition().longitude);
                        polyLineDetails.setToLatitude(markerList.get(1).getPosition().latitude);
                        polyLineDetails.setToLongitude(markerList.get(1).getPosition().longitude);
                        Gson gson = new Gson();
                        String json = gson.toJson(polyLineDetails);
                        SurveySaveReq surveySaveReq = new SurveySaveReq();
                        surveySaveReq.setSurvey(new SurveySaveReq.SurveyEntity(surveyModel.getSurveyLandUid()));
                        MapTypeEntity mapTypeEntity = new MapTypeEntity();
                        boolean myeditMode1 = getIntent().getBooleanExtra("my_boolean_key", false);
                        if (myeditMode1) {
                            surveySaveReq.setUid(surveyEntity.getUid());
                        }
                        mapTypeEntity.setUid("line");
                        mapTypeEntity.setName("line");
                        surveySaveReq.setMapType(mapTypeEntity);
                        surveySaveReq.setLatlongs(json);
                        surveySaveReq.setDescription(dialogView.getPointDescription());
                        surveySaveReq.setName(dialogView.getPointName());
                        mpresenter.saveSurvey(surveySaveReq);
                        Toast.makeText(SurveyTrackingActivity.this, "PolyLine Details are saved successfully", Toast.LENGTH_LONG).show();
                    }
                });
                dialogView.setNegativeLabel("Cancel");
                dialogView.setNegativeListener(v -> dialogView.dismiss());
                dialogView.show();
            } else {
                showMessage("please add Line");
            }
        }
    }

    @Override
    public void surveySaveSuccess() {
        Intent intent = getIntent();
        intent.putExtra("surveySubmit", surveyModelArrayList);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onPassSurveyTrackEnteredDetails(SurveyDetailsModel surveyDetailsModel) {
        surveyTrackingBinding.saveBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SurveyStatusActivity.class);
            intent.putExtra("dialogedittextdata", surveyModel);
            startActivity(intent);
        });
    }

    @Override
    public void onClickStartPolygon() {
        isStartLogging = true;
        surveyTrackingBinding.setStart(true);
    }

    @Override
    public void savePolyGone() {
        SurveyPointDialog dialogView = new SurveyPointDialog(this);
        dialogView.setTitle("Polygon Details");
        boolean myeditMode = getIntent().getBooleanExtra("my_boolean_key", false);
        if (myeditMode) {
            dialogView.setEditTextData(surveyEntity.getName());
            dialogView.setEditTextDescriptionData(surveyEntity.getDescription());
        }
        dialogView.setPositiveLabel("Ok");
        dialogView.setPositiveListener(view -> {
            if (dialogView.validations()) {
                dialogView.dismiss();
                Gson gson = new Gson();
                String json = gson.toJson(surveyModelArrayList);
                SurveySaveReq surveySaveReq = new SurveySaveReq();
                boolean myeditMode1 = getIntent().getBooleanExtra("my_boolean_key", false);
                if (myeditMode1) {
                    surveySaveReq.setUid(surveyEntity.getUid());
                }
                surveySaveReq.setSurvey(new SurveySaveReq.SurveyEntity(surveyModel.getSurveyLandUid()));
                MapTypeEntity mapTypeEntity = new MapTypeEntity();
                mapTypeEntity.setUid("polygon");
                mapTypeEntity.setName("polygon");
                surveySaveReq.setMapType(mapTypeEntity);
                surveySaveReq.setLatlongs(json);
                surveySaveReq.setDescription(dialogView.getPointDescription());
                surveySaveReq.setName(dialogView.getPointName());
                mpresenter.saveSurvey(surveySaveReq);

                Toast.makeText(SurveyTrackingActivity.this, "PolyGone Details are saved successfully", Toast.LENGTH_LONG).show();
            }
        });
        dialogView.setNegativeLabel("Cancel");
        dialogView.setNegativeListener(v -> dialogView.dismiss());
        dialogView.show();
    }

    private void addPoint(LatLng latLng) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.new_point);
        pointMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .flat(true).icon(icon)
                .anchor(0.5f, 0.5f).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.5f));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                pointMarker = marker;
                markerList.clear();
                markerList.add(pointMarker);
            }
        });
        surveyTrackingBinding.saveBtn.setVisibility(View.VISIBLE);
        markerList.add(pointMarker);

    }

    private void drawLine() {
        if (markerList.size() == 2) {
            planePolyline(markerList.get(0).getPosition(), markerList.get(1).getPosition());
        }
    }

    @Override
    public void anotherizedToken() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private void showPointDialog(LatLng latLng) {
        SurveyPointDialog dialogView = new SurveyPointDialog(this);
        dialogView.setTitle("Point Details");
        boolean myeditMode = getIntent().getBooleanExtra("my_boolean_key", false);
        if (myeditMode) {
            dialogView.setEditTextData(surveyEntity.getName());
            dialogView.setEditTextDescriptionData(surveyEntity.getDescription());
        }
        dialogView.setPositiveLabel("Ok");
        dialogView.setPositiveListener(view -> {
            if (dialogView.validations()) {
                dialogView.dismiss();
                SurveySaveReq surveySaveReq = new SurveySaveReq();
                surveySaveReq.setSurvey(new SurveySaveReq.SurveyEntity(surveyModel.getSurveyLandUid()));
                MapTypeEntity mapTypeEntity = new MapTypeEntity();
                boolean myeditMode1 = getIntent().getBooleanExtra("my_boolean_key", false);
                if (myeditMode1) {
                    surveySaveReq.setUid(surveyEntity.getUid());
                }
                mapTypeEntity.setUid("point");
                mapTypeEntity.setName("point");
                surveySaveReq.setMapType(mapTypeEntity);
                SurveyModel.PointDetails pointDetails = new SurveyModel.PointDetails(latLng.latitude, latLng.longitude);
                Gson gson = new Gson();
                String json = gson.toJson(pointDetails);
                surveySaveReq.setLatlongs(json);
                surveySaveReq.setDescription(dialogView.getPointDescription());
                surveySaveReq.setName(dialogView.getPointName());
                mpresenter.saveSurvey(surveySaveReq);
                Toast.makeText(SurveyTrackingActivity.this, "PointDetails are saved successfully", Toast.LENGTH_LONG).show();
//                    Intent intent = getIntent();
//                    intent.putExtra("surveySubmit", surveyModelArrayList);
//                    setResult(RESULT_OK, intent);
//                    finish();

            }
        });
        dialogView.setNegativeLabel("Cancel");
        dialogView.setNegativeListener(v -> {
            dialogView.dismiss();
            markerList.remove(pointMarker);
            pointMarker.remove();
            surveyTrackingBinding.saveBtn.setVisibility(View.GONE);
            Toast.makeText(SurveyTrackingActivity.this, "PointDetails are not saved", Toast.LENGTH_LONG).show();
        });
        dialogView.show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        DeleteDialog deleteDialog = new DeleteDialog(this);
        if (getSurveyType() == 2) {
            deleteDialog.setTitle("Plygon Edit Details");
            deleteDialog.setPositiveLabel("Add");
            deleteDialog.setEditTextDialogDetails(this);
            deleteDialog.setPositiveListener(view -> {
                deleteDialog.dismiss();
                mMap.setOnMapClickListener(latLng -> {
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.blue_circle);
                    Marker polygonMarker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .flat(true).icon(icon)
                            .anchor(0.5f, 0.5f).draggable(true));
                    if (marker.getTag() != null && marker.getTag() instanceof MarkerTag) {
                        MarkerTag markerTag = (MarkerTag) marker.getTag();
                        polygonPoints.add(markerTag.getPosition() + 1, polygonMarker.getPosition());
                        surveyModelArrayList.add(markerTag.getPosition() + 1, new SurveyDetailsEntity(latLng.latitude, latLng.longitude, 0.0));
                        MarkerTag yourMarkerTag = new MarkerTag();
                        yourMarkerTag.setLatLng(latLng);
                        yourMarkerTag.setPosition(markerTag.getPosition() + 1);
                        polygonMarker.setTag(yourMarkerTag);
//                                Log.i(TAG, "computeArea " + SphericalUtil.computeArea(polygonPoints));
                    }
                    mMap.setOnMapClickListener(null);
                    polygonPolyline();
                });
            });
            deleteDialog.setNegativeLabel("Delete");
            deleteDialog.setNegativeListener(v -> {
                deleteDialog.dismiss();
                if (marker != null) {
                    if (polygonPoints.size() > 2) {
                        polygonPoints.remove(marker.getPosition());
                        if (marker.getTag() != null && marker.getTag() instanceof MarkerTag) {
                            surveyModelArrayList.remove(((MarkerTag) marker.getTag()).getPosition());
                        }
                        polygonPolyline();
                        marker.remove();
                    }
                }
            });
            deleteDialog.show();
        } else {
            if (marker != null) {
                marker.remove();
                markerList.remove(marker);
                clearPolyline();
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }


    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(MyReceiver);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private boolean isOffline = false;

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Back to Online Mode";
            color = Color.WHITE;
            Snackbar snackbar = Snackbar
                    .make(surveyTrackingBinding.fullMap, message, Snackbar.LENGTH_SHORT);

            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.thickGreem));
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(color);
            if (isOffline) {
                snackbar.show();
                isOffline = false;
            }
        } else {
            message = "Your in Offline Mode";
            color = Color.WHITE;
            Snackbar snackbar = Snackbar
                    .make(surveyTrackingBinding.fullMap, message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
            isOffline = true;
        }
    }
}