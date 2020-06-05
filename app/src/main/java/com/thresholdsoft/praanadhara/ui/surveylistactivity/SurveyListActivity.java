package com.thresholdsoft.praanadhara.ui.surveylistactivity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.snackbar.Snackbar;
import com.thresholdsoft.praanadhara.BuildConfig;
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.databinding.ActivitySurveyListBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.adapter.SurveyAdapter;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.SurveyCountModel;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusActivity;
import com.thresholdsoft.praanadhara.ui.userlogin.UserLoginActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SurveyListActivity extends BaseActivity implements SurveyListMvpView, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    @Inject
    SurveyListMvpPresenter<SurveyListMvpView> mpresenter;
    private ActivitySurveyListBinding activitySurveyListBinding;
    private ArrayList<RowsEntity> surveyModelArrayList = new ArrayList<>();

    SurveyAdapter surveyAdapter;
    public static final int REQUEST_CODE = 1;
    private static final String TAG = SurveyListActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySurveyListBinding = DataBindingUtil.setContentView(this, R.layout.activity_survey_list);
        getActivityComponent().inject(this);
        mpresenter.onAttach(SurveyListActivity.this);
        setUp();
    }

    @Override
    protected void setUp() {
        activitySurveyListBinding.setSurvey(new SurveyCountModel());
        setUpGClient();
        surveyAdapter = new SurveyAdapter(this, surveyModelArrayList, mpresenter);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        activitySurveyListBinding.recyclerSurveyList.setLayoutManager(mLayoutManager1);
        activitySurveyListBinding.recyclerSurveyList.setAdapter(surveyAdapter);

        mpresenter.farmersListApiCall();
    }

    @Override
    public void onItemClick(RowsEntity farmerModel) {
        farmerModel.setCurrentLatitude(latitude);
        farmerModel.setCurrentLongitude(longitude);
        Intent intent = new Intent(this, SurveyStatusActivity.class);
        intent.putExtra("surveyData", farmerModel);
        startActivityForResult(intent, REQUEST_CODE);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onFarmersRes(List<RowsEntity> rowsEntity) {
        surveyModelArrayList.clear();
        surveyModelArrayList.addAll(rowsEntity);
        surveyAdapter.notifyDataSetChanged();
        for(RowsEntity entity : rowsEntity){
            if(entity.getFarmerLand().getSurveyLandLocation().getSubmitted().getUid()== null){
                activitySurveyListBinding.getSurvey().setNewCount(activitySurveyListBinding.getSurvey().getNewCount()+1);
            }else if(entity.getFarmerLand().getSurveyLandLocation().getSubmitted().getUid().equalsIgnoreCase("Yes")){
                activitySurveyListBinding.getSurvey().setCompletedCount(activitySurveyListBinding.getSurvey().getCompletedCount()+1);
            }else if(entity.getFarmerLand().getSurveyLandLocation().getSubmitted().getUid().equalsIgnoreCase("No")){
                activitySurveyListBinding.getSurvey().setInProgressCount(activitySurveyListBinding.getSurvey().getInProgressCount()+1);
            }
        }
//        if (surveyModelArrayList.size() > 0) {
//            if (surveyModelArrayList.get(0).getFarmerLand().getSurveyLandLocation().getSubmitted().getUid() != null) {
//                if (surveyModelArrayList.get(0).getFarmerLand().getSurveyLandLocation().getSubmitted().getUid().equalsIgnoreCase("yes")) {
//                    activitySurveyListBinding.itemGreenCount.setText(String.valueOf(surveyModelArrayList.size()));
//                }
//            }
//        }
    }

    @Override
    public void arrayListClear() {
        surveyModelArrayList.clear();
        surveyAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            boolean requiredValue = data.getBooleanExtra("surveySubmit", false);
            if (requiredValue) {
                mpresenter.farmersListApiCall();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startStep1();
    }

    /**
     * Step 1: Check Google Play services
     */
    private void startStep1() {
        //Check whether this user has installed Google play service which is being used by Location updates.
        if (isGooglePlayServicesAvailable()) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.no_google_playservice_available, Toast.LENGTH_LONG).show();
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
            ActivityCompat.requestPermissions(SurveyListActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the img_user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(SurveyListActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
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


    private void getMyLocation() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(SurveyListActivity.this,
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

                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(SurveyListActivity.this,
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

    private synchronized void setUpGClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 1, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void anotherizedToken() {
        mpresenter.anotherizedTokenClearDate();
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}