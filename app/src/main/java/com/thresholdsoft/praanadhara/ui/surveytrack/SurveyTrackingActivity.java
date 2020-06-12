package com.thresholdsoft.praanadhara.ui.surveytrack;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.thresholdsoft.praanadhara.BuildConfig;
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.data.db.model.Survey;
import com.thresholdsoft.praanadhara.data.db.model.SurveyEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyDetailsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveySaveReq;
import com.thresholdsoft.praanadhara.databinding.ActivitySurveyTrackingBinding;
import com.thresholdsoft.praanadhara.services.LocationMonitoringService;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
import com.thresholdsoft.praanadhara.ui.dialog.SurveyPointDialog;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusActivity;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.model.SurveyDetailsModel;
import com.thresholdsoft.praanadhara.ui.surveytrack.model.SurveyModel;
import com.thresholdsoft.praanadhara.ui.userlogin.UserLoginActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class SurveyTrackingActivity extends BaseActivity implements SurveyTrackMvpView, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {

    private static final String TAG = SurveyTrackingActivity.class.getSimpleName();

    @Inject
    SurveyTrackMvpPresenter<SurveyTrackMvpView> mpresenter;
    private ActivitySurveyTrackingBinding surveyTrackingBinding;
    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private Marker pointMarker;
    private boolean mAlreadyStartedService = false;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Marker userPositionMarker;
    private Polyline runningPathPolyline;
    private Polygon runningPathPolygon;
    private int polylineWidth = 10;
    private ArrayList<Location> locationList = new ArrayList<>();
    private ArrayList<SurveyDetailsEntity> surveyModelArrayList = new ArrayList<>();

    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dot.
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);
    private boolean isStartLogging = false;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private List<LatLng> polygonPoints = new ArrayList<>();
    boolean zoomable = true;
    boolean didInitialZoom;
    private Location currentLocation;
    private RowsEntity surveyModel;
    private List<Marker> markerList = new ArrayList<>();

    public static Intent getIntent(Context context, FarmerLands surveyEntity, int mapType) {
        Intent intent = new Intent(context, SurveyTrackingActivity.class);
        intent.putExtra("surveyEntity", surveyEntity);
        intent.putExtra("map_type",mapType);
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
            mapFragment.getMapAsync(this);
        }

        setUp();
    }

    private boolean firstTimeZoom = false;

    @Override
    protected void setUp() {
        surveyTrackingBinding.setView(this);
        surveyModel =  (RowsEntity)getIntent().getSerializableExtra("surveyEntity");
        surveyTrackingBinding.setType(getIntent().getIntExtra("map_type",0));
        surveyTrackingBinding.setSurvey(surveyModel);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
                        String longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);
                        Location location = intent.getParcelableExtra("location");
                        if (location != null && latitude != null && longitude != null) {
                            surveyTrackingBinding.progressBar.setVisibility(View.GONE);
                            if (!firstTimeZoom) {
                                firstTimeZoom = true;
                                zoomMapTo(location);
                            }
                            currentLocation = location;
                            surveyTrackingBinding.accuracyTextView.setText("Accuracy\n" + location.getAccuracy());
                            surveyTrackingBinding.distanceTextView.setText("Distance\n " + mpresenter.getTravelledDistance(locationList));
                            if (isStartLogging) {
                                filterLocationLatLong(location);
                            }
                        }
                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );
        setUpGClient();
        isStartLogging = true;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        startStep1();
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
        if (shouldProvideRationale || shouldProvideRationale2) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            ActivityCompat.requestPermissions(SurveyTrackingActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the img_user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(SurveyTrackingActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
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
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
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
        SurveyPointDialog dialogView = new SurveyPointDialog(this);
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                if (getSurveyType() == 0) {
                    pointMarker = mMap.addMarker(new MarkerOptions()
                            .position(point)
                            .flat(true)
                            .anchor(0.5f, 0.5f));
                    showPointDialog(point);
                } else if (getSurveyType() == 1) {
                    if (markerList.size() < 2) {
                        Marker myMarker = mMap.addMarker(new MarkerOptions()
                                .position(point)
                                .flat(true)
                                .anchor(0.5f, 0.5f));
                        mMap.setOnMarkerClickListener(SurveyTrackingActivity.this);
                        markerList.add(myMarker);
                        drawLine();

                    }
                }
                Log.d("DEBUG", "Map clicked [" + point.latitude + " / " + point.longitude + "]");
                //Do your stuff with LatLng here
                //Then pass LatLng to other activity
            }
        });
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

        //setAccuracy(newLocation.getAccuracy());
        float horizontalAccuracy = location.getAccuracy();
        Log.e("Accuracy", String.valueOf(horizontalAccuracy));
//        if(horizontalAccuracy > 10){ //10meter filter
//            Log.d(TAG, "Accuracy is too low.");
//         //   kalmanNGLocationList.add(location);
//            return false;
//        }

        locationList.add(location);
        // surveyModelArrayList.add(new SurveyModel(location.getLatitude(),location.getLongitude(),location.getAccuracy()));
        polygonPoints.add(new LatLng(location.getLatitude(), location.getLongitude()));
        mpresenter.storeSurveyDetails(location, false);

        if (getSurveyType() == 0) {
            //  dottedPolyline();
        } else if (getSurveyType() == 1) {
            //  planePolyline();
        } else if (getSurveyType() == 2) {
            surveyModelArrayList.add(new SurveyDetailsEntity(location.getLatitude(), location.getLongitude(), location.getAccuracy()));
            drawUserPositionMarker(location);
            polygonPolyline();
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

    private void dottedPolyline() {
        if (runningPathPolyline == null) {
            if (locationList.size() > 1) {
                Location fromLocation = locationList.get(locationList.size() - 2);
                Location toLocation = locationList.get(locationList.size() - 1);

                LatLng from = new LatLng(((fromLocation.getLatitude())),
                        ((fromLocation.getLongitude())));

                LatLng to = new LatLng(((toLocation.getLatitude())),
                        ((toLocation.getLongitude())));
                List<PatternItem> pattern = Arrays.asList(new Dot(), new Gap(20));
                PolylineOptions options = new PolylineOptions().pattern(pattern);
                this.runningPathPolyline = mMap.addPolyline(options
                        .add(from, to)
                        .width(polylineWidth).color(Color.parseColor("#801B60FE")).geodesic(true));
            }
        } else {
            Location toLocation = locationList.get(locationList.size() - 1);
            LatLng to = new LatLng(((toLocation.getLatitude())),
                    ((toLocation.getLongitude())));

            List<LatLng> points = runningPathPolyline.getPoints();
            points.add(to);

            runningPathPolyline.setPoints(points);
        }
    }

    private void planePolyline(LatLng fromPolyLineLatLng, LatLng toPolyLineLatLng) {
        LatLng from = new LatLng(((fromPolyLineLatLng.latitude)), ((fromPolyLineLatLng.longitude)));
        LatLng to = new LatLng(((toPolyLineLatLng.latitude)), ((toPolyLineLatLng.longitude)));
        PolylineOptions options = new PolylineOptions();
        this.runningPathPolyline = mMap.addPolyline(options
                .add(from, to)
                .width(polylineWidth).color(Color.parseColor("#801B60FE")).geodesic(true));
//        if (runningPathPolyline == null) {
//            if (locationList.size() > 1) {
//                Location fromLocation = locationList.get(locationList.size() - 2);
//                Location toLocation = locationList.get(locationList.size() - 1);
//
//                LatLng from = new LatLng(((fromLocation.getLatitude())),
//                        ((fromLocation.getLongitude())));
//
//                LatLng to = new LatLng(((toLocation.getLatitude())),
//                        ((toLocation.getLongitude())));
//                PolylineOptions options = new PolylineOptions();
//                this.runningPathPolyline = mMap.addPolyline(options
//                        .add(from, to)
//                        .width(polylineWidth).color(Color.parseColor("#801B60FE")).geodesic(true));
//            }
//        } else {
//            Location toLocation = locationList.get(locationList.size() - 1);
//            LatLng to = new LatLng(((toLocation.getLatitude())),
//                    ((toLocation.getLongitude())));
//
//            List<LatLng> points = runningPathPolyline.getPoints();
//            points.add(to);
//
//            runningPathPolyline.setPoints(points);
//        }
    }

    private void polygonPolyline() {
        if (polygonPoints.size() > 1) {
            Location fromLocation = locationList.get(locationList.size() - 2);
            Location toLocation = locationList.get(locationList.size() - 1);

            LatLng from = new LatLng(((fromLocation.getLatitude())),
                    ((fromLocation.getLongitude())));

            LatLng to = new LatLng(((toLocation.getLatitude())),
                    ((toLocation.getLongitude())));
            PolygonOptions options = new PolygonOptions();
            if (runningPathPolygon != null)
                runningPathPolygon.remove();

            this.runningPathPolygon = mMap.addPolygon(options
                    .addAll(polygonPoints));
            runningPathPolygon.setStrokeColor(Color.BLUE);
            runningPathPolygon.setFillColor(Color.argb(20, 0, 255, 0));
        }
    }

    private void drawUserPositionMarker(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        surveyTrackingBinding.accuracyTextView.setText("Accuracy:\n" + location.getAccuracy());

        if (userPositionMarker == null) {
            userPositionMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .flat(true)
                    .anchor(0.5f, 0.5f)
            );
        } else {
            userPositionMarker.setPosition(latLng);
        }
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

    private void clearPolygon() {
        if (runningPathPolygon != null) {
            runningPathPolygon.remove();
            runningPathPolygon = null;
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
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(@NonNull LocationSettingsResult result) {
                            final Status status = result.getStatus();
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
//
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 16));

        //        mMap.clear();
//
//        MarkerOptions mp = new MarkerOptions();
//
//        mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
//
//        mp.title("my position");
//
//        mMap.addMarker(mp);
//
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                new LatLng(location.getLatitude(), location.getLongitude()), 16));
    }

    @Override
    public String getSurveyId() {
        return "asdfg";
    }

    @Override
    public int getSurveyType() {
        return getIntent().getIntExtra("map_type",0);
    }

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
    public void onClickPointBtn() {
        SurveyPointDialog dialogView = new SurveyPointDialog(this);
        dialogView.setTitle("Point Details");
        dialogView.setPositiveLabel("Ok");
        dialogView.setEditTextDialogDetails(this);
        dialogView.setPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogView.dismiss();
                if (currentLocation != null) {
                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    addPoint(latLng, dialogView.getPointName(), dialogView.getPointDescription());
                }
            }
        });
        dialogView.setNegativeLabel("Cancel");
        dialogView.setNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();
            }
        });
        dialogView.show();
    }

    @Override
    public void onClickSavePoints() {
        if (getSurveyType() == 0) {
            Intent intent = getIntent();
            intent.putExtra("surveySubmit", surveyModelArrayList);
            setResult(RESULT_OK, intent);
            finish();
        } else if (getSurveyType() == 1) {
            if (markerList.size() == 2) {
                SurveyPointDialog dialogView = new SurveyPointDialog(this);
                dialogView.setTitle("Line Details");
                dialogView.setPositiveLabel("Ok");
                dialogView.setPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialogView.validations()) {
                            dialogView.dismiss();
                            SurveyModel.PolyLineDetails polyLineDetails = new SurveyModel.PolyLineDetails();
                            polyLineDetails.setFromLatitude(markerList.get(0).getPosition().latitude);
                            polyLineDetails.setFromLongitude(markerList.get(0).getPosition().longitude);
                            polyLineDetails.setToLatitude(markerList.get(1).getPosition().latitude);
                            polyLineDetails.setToLongitude(markerList.get(1).getPosition().longitude);
                            Gson gson = new Gson();
                            String json = gson.toJson(polyLineDetails);
                            surveyModelArrayList.clear();
                            surveyModelArrayList.add(new SurveyDetailsEntity(dialogView.getPointName(), dialogView.getPointDescription(), json, surveyModel.getMapTypeEntity(),
                                    surveyModel.getUid()));
                            SurveySaveReq surveySaveReq = new SurveySaveReq();
                            surveySaveReq.setSurvey(new SurveySaveReq.SurveyEntity(surveyModel.getFarmerLand().getSurveyLandLocation().getUid()));
                            surveySaveReq.setMapType(surveyModel.getMapTypeEntity());
                            surveySaveReq.setLatlongs(json);
                            surveySaveReq.setDescription(dialogView.getPointDescription());
                            surveySaveReq.setName(dialogView.getPointName());
                            mpresenter.saveSurvey(surveySaveReq);
//                        Intent intent = getIntent();
//                        intent.putExtra("surveySubmit", surveyModelArrayList);
//                        setResult(RESULT_OK, intent);
//                        finish();
                        }
                    }
                });
                dialogView.setNegativeLabel("Cancel");
                dialogView.setNegativeListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogView.dismiss();
                    }
                });
                dialogView.show();
            } else {
                showMessage("please add Line");
            }
        }
    }

    @Override
    public void surveySaveSuccess() {
        if (getSurveyType() == 1 || getSurveyType() == 2) {
            Intent intent = getIntent();
            intent.putExtra("surveySubmit", surveyModelArrayList);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onPassSurveyTrackEnteredDetails(SurveyDetailsModel surveyDetailsModel) {
        surveyTrackingBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SurveyStatusActivity.class);
                intent.putExtra("dialogedittextdata", surveyModel);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClickStartPolygon() {
        surveyTrackingBinding.setStart(true);
    }

    @Override
    public void savePolyGone() {
        SurveyPointDialog dialogView = new SurveyPointDialog(this);
        dialogView.setTitle("Polygon Details");
        dialogView.setPositiveLabel("Ok");
        dialogView.setPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogView.validations()) {
                    dialogView.dismiss();
                    Gson gson = new Gson();
                    String json = gson.toJson(surveyModelArrayList);
                    surveyModelArrayList.clear();
                    surveyModelArrayList.add(new SurveyDetailsEntity(dialogView.getPointName(), dialogView.getPointDescription(), json, surveyModel.getMapTypeEntity(),
                            surveyModel.getUid()));
                    SurveySaveReq surveySaveReq = new SurveySaveReq();
                    surveySaveReq.setSurvey(new SurveySaveReq.SurveyEntity(surveyModel.getFarmerLand().getSurveyLandLocation().getUid()));
                    surveySaveReq.setMapType(surveyModel.getMapTypeEntity());
                    surveySaveReq.setLatlongs(json);
                    surveySaveReq.setDescription(dialogView.getPointDescription());
                    surveySaveReq.setName(dialogView.getPointName());
                    mpresenter.saveSurvey(surveySaveReq);
//                Intent intent = getIntent();
//                intent.putExtra("surveySubmit", surveyModelArrayList);
//                setResult(RESULT_OK, intent);
//                finish();
//        SurveySaveReq surveySaveReq = new SurveySaveReq();
//        surveySaveReq.setDescription("");
//        Gson gson = new Gson();
//        String json = gson.toJson(surveyModelArrayList);
//        surveySaveReq.setLatlongs(json);
//        surveySaveReq.setMapType(surveyModel.getMapTypeEntity());
//        surveySaveReq.setSurvey(new SurveySaveReq.SurveyEntity(surveyModel.getStartSurveyUid()));
//        mpresenter.saveSurvey(surveySaveReq);
                }
            }
        });
        dialogView.setNegativeLabel("Cancel");
        dialogView.setNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();
            }
        });
        dialogView.show();
    }

    private void addPoint(LatLng latLng, String name, String description) {
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .flat(true)
                .anchor(0.5f, 0.5f));
        SurveyModel.PointDetails pointDetails = new SurveyModel.PointDetails(latLng.latitude, latLng.longitude);
        Gson gson = new Gson();
        String json = gson.toJson(pointDetails);
        surveyModelArrayList.add(new SurveyDetailsEntity(name, description, json, surveyModel.getMapTypeEntity(), surveyModel.getUid()));
        mMap.setOnMarkerClickListener(this);
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
        dialogView.setPositiveLabel("Ok");
        dialogView.setPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogView.validations()) {
                    dialogView.dismiss();
                    SurveySaveReq surveySaveReq = new SurveySaveReq();
                    surveySaveReq.setSurvey(new SurveySaveReq.SurveyEntity(surveyModel.getFarmerLand().getSurveyLandLocation().getUid()));
                    surveySaveReq.setMapType(surveyModel.getMapTypeEntity());
                    SurveyModel.PointDetails pointDetails = new SurveyModel.PointDetails(latLng.latitude, latLng.longitude);
                    Gson gson = new Gson();
                    String json = gson.toJson(pointDetails);
                    surveySaveReq.setLatlongs(json);
                    surveySaveReq.setDescription(dialogView.getPointDescription());
                    surveySaveReq.setName(dialogView.getPointName());
                    mpresenter.saveSurvey(surveySaveReq);
                    addPoint(latLng, dialogView.getPointName(), dialogView.getPointDescription());
                }
            }
        });
        dialogView.setNegativeLabel("Cancel");
        dialogView.setNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();
                pointMarker.remove();
            }
        });
        dialogView.show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker != null) {
            marker.remove();
            markerList.remove(marker);
            clearPolyline();
        }
        return true;
    }
}



