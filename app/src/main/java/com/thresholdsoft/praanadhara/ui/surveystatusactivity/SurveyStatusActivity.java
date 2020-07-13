package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BuildConfig;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.data.db.model.SurveyEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyDetailsEntity;
import com.thresholdsoft.praanadhara.databinding.ActivitySurveyStatusBinding;
import com.thresholdsoft.praanadhara.root.WaveApp;
import com.thresholdsoft.praanadhara.services.ConnectivityReceiver;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.SurveyListFrag;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyListModel;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.adapter.SurveyDetailsAdapter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.bottomsheet.SurveyStatusBottomSheet;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.CustomEditDialog;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.deletedialog.DeleteDialog;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.swipe.ItemTouchHelperCallback;
import com.thresholdsoft.praanadhara.ui.surveytrack.SurveyTrackingActivity;
import com.thresholdsoft.praanadhara.ui.surveytrack.model.SurveyModel;
import com.thresholdsoft.praanadhara.ui.userlogin.UserLoginActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SurveyStatusActivity extends BaseActivity implements SurveyStatusMvpView, OnMapReadyCallback, ConnectivityReceiver.ConnectivityReceiverListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, android.location.LocationListener {
    @Inject
    SurveyStatusMvpPresenter<SurveyStatusMvpView> mpresenter;
    private ActivitySurveyStatusBinding activitySurveyStatusBinding;
    private GoogleMap map;
    //    public static final int REQUEST_CODE = 2;
    private SurveyDetailsAdapter surveyDetailsAdapter;
    private SurveyStatusBottomSheet bottomSheet;
    boolean isExpanded = true;
    private ImageView fullScreenView;
    private View includedLayout;
    FarmerLands farmerLands;
    private BroadcastReceiver MyReceiver = null;
    public static final int REQUEST_CODE = 1;
    private static final String TAG = SurveyListFrag.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    boolean editMode=true;
    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private double mLatitude;
    private double mLongitude;
    private boolean isNewSurveyCurrentLocation = false;
    private ArrayList<SurveyDetailsEntity> surveyModelArrayList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySurveyStatusBinding = DataBindingUtil.setContentView(this, R.layout.activity_survey_status);
        getActivityComponent().inject(this);
        mpresenter.onAttach(SurveyStatusActivity.this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.preview_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        setUpGClient();
        MyReceiver = new ConnectivityReceiver();
        setUp();
    }

    String uid;
    String landUid;
    int mapType;

    @Override
    protected void setUp() {
        Intent intent = getIntent();
        uid = intent.getStringExtra("surveyData");
        landUid = intent.getStringExtra("landUid");
        mpresenter.getFarmerLand(uid, landUid).observe(this, farmerLands -> {
            activitySurveyStatusBinding.setFarmerLand(farmerLands);
            activitySurveyStatusBinding.backArrow.setFarmerLand(farmerLands);
            this.farmerLands = farmerLands;
        });

        surveyDetailsAdapter = new SurveyDetailsAdapter(this);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        activitySurveyStatusBinding.surveDetailsRecyclerview.setLayoutManager(mLayoutManager1);
        activitySurveyStatusBinding.surveDetailsRecyclerview.setItemAnimator(new DefaultItemAnimator());
        activitySurveyStatusBinding.surveDetailsRecyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        activitySurveyStatusBinding.surveDetailsRecyclerview.setAdapter(surveyDetailsAdapter);

        ItemTouchHelperCallback mCallback = new ItemTouchHelperCallback();
        ItemTouchHelperExtension mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
        mItemTouchHelper.attachToRecyclerView(activitySurveyStatusBinding.surveDetailsRecyclerview);
        surveyDetailsAdapter.setItemTouchHelperExtension(mItemTouchHelper);

        activitySurveyStatusBinding.setPresenterCallback(mpresenter);
        activitySurveyStatusBinding.setExpandView(1);

//        if (activitySurveyStatusBinding.pointsRadio.isChecked()) {
//            activitySurveyStatusBinding.linesRadio.setChecked(false);
//            activitySurveyStatusBinding.polygonRadio.setChecked(false);
//            mapType = 0;
//        }

        includedLayout = findViewById(R.id.backArrow);
        ImageView insideTheIncludedLayout = includedLayout.findViewById(R.id.imageButton);
        insideTheIncludedLayout.setOnClickListener(v -> onBackPressed());

//        fullScreenView = includedLayout.findViewById(R.id.fullScreenview);
//        fullScreenView.setVisibility(View.VISIBLE);

        ImageView imageInsideLayout = includedLayout.findViewById(R.id.plusImage);
//        imageInsideLayout.setVisibility(View.VISIBLE);
//        mpresenter.getFarmerLand(uid, landUid).observe(this, farmerLands -> activitySurveyStatusBinding.backArrow.setFarmerLand(farmerLands));
        imageInsideLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet();
            }
        });

        activitySurveyStatusBinding.checkBoxHeader.setOnClickListener(view -> {
            if (activitySurveyStatusBinding.checkBoxHeader.isChecked()) {
                for (SurveyEntity surveyEntity : surveyDetailsAdapter.getListData()) {
                    surveyEntity.setUnchecked(false);
                }
            } else {
                for (SurveyEntity surveyEntity : surveyDetailsAdapter.getListData()) {
                    surveyEntity.setUnchecked(true);
                }
            }
            surveyDetailsAdapter.notifyDataSetChanged();
            previewDisplay(surveyDetailsAdapter.getListData());
        });

        mpresenter.getAllSurveyList(landUid).observe(this, surveyEntities -> {
            surveyDetailsAdapter.addItems(surveyEntities);
//            surveyDetailsAdapter.notifyDataSetChanged();
            activitySurveyStatusBinding.setSurvey(surveyEntities.size() > 0);
            previewDisplay(surveyEntities);
            if (farmerLands != null && farmerLands.getStatus().equalsIgnoreCase("No") && surveyEntities.size() == 0) {
                openBottomSheet();
            }
        });
    }

    private void openBottomSheet() {
        bottomSheet = new SurveyStatusBottomSheet();
        bottomSheet.setPresenterData(mpresenter);
        Bundle bundle = new Bundle();
        bundle.putString("surveyDataSheet", uid);
        bundle.putString("landUidSheet", landUid);
        bottomSheet.setArguments(bundle);
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        fullMap();
    }

    public void fullMap() {
//        ImageView collapse = includedLayout.findViewById(R.id.collapseView);
        LinearLayout mapFrameLayout = (LinearLayout) findViewById(R.id.mapFrameLayout);
        activitySurveyStatusBinding.expandView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams fullMapParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                activitySurveyStatusBinding.farmerdata.setVisibility(View.GONE);
                mapFrameLayout.setLayoutParams(fullMapParams);
                activitySurveyStatusBinding.setExpandView(0);
                activitySurveyStatusBinding.setCollapseView(1);
                //  previewDisplay(surveyDetailsAdapter.getListData());
            }
        });
        activitySurveyStatusBinding.collapseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams fullMapParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                activitySurveyStatusBinding.farmerdata.setVisibility(View.VISIBLE);
                fullMapParams.height = 900;
                mapFrameLayout.setLayoutParams(fullMapParams);
                activitySurveyStatusBinding.setExpandView(1);
                activitySurveyStatusBinding.setCollapseView(0);
                //  previewDisplay(surveyDetailsAdapter.getListData());
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void startSurveySuccess(String uid) {
        mpresenter.updateFarmerLandStatus(this.uid, landUid, uid);
        openBottomSheet();
    }

    @Override
    public void addSurvey(FarmerLands rowsEntity) {
//        startActivityForResult(SurveyTrackingActivity.getIntent(this, rowsEntity, mapType), REQUEST_CODE);
//        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void submitSurvey(FarmerLands rowsEntity) {
        mpresenter.submitSurvey(rowsEntity);
    }

    @Override
    public void surveySubmitSuccess() {
        mpresenter.updateLandSurveySubmit(uid, landUid);
        Intent intent = getIntent();
        intent.putExtra("surveySubmit", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onListItemClicked(SurveyEntity surveyEntity) {
        boolean isCheckedStatus = surveyEntity.isUnchecked();
        if (!isCheckedStatus) {
            surveyEntity.setUnchecked(true);

        } else {
            surveyEntity.setUnchecked(false);
        }
        mpresenter.updateSurveyCheck(surveyEntity);
//        surveyDetailsAdapter.notifyDataSetChanged();
//        previewDisplay(surveyDetailsAdapter.getListData());
    }

    @Override
    public SurveyListModel getSurvey() {
        return null;
    }


    @Override
    public void onClickEditSurvey(SurveyEntity surveyEntity) {
        CustomEditDialog customEditDialog = new CustomEditDialog(this);
        customEditDialog.setEditTextData(surveyEntity.getName());
        customEditDialog.setEditTextDescriptionData(surveyEntity.getDescription());
        customEditDialog.setTitle("Edit ");
        customEditDialog.setPositiveUpdateLabel("Update");
        customEditDialog.setPositiveUpdateListener(v -> {
            if (customEditDialog.validations()) {
                surveyEntity.setName(customEditDialog.getPointName());
                surveyEntity.setDescription(customEditDialog.getPointDescription());
                customEditDialog.dismiss();
                mpresenter.editApiCal(surveyEntity);
            }
        });
        customEditDialog.setNegativeUpdateLabel("Cancel");
        customEditDialog.setNegativeUpdateListener(v -> {
            customEditDialog.dismiss();
        });
        customEditDialog.show();
    }

    @Override
    public void onClickPolygonMapEditSurvey(SurveyEntity surveyEntity,int position) {
        if (surveyEntity.getMapType().equalsIgnoreCase("point")) {
            mapType= 0;
        } else if (surveyEntity.getMapType().equalsIgnoreCase("line")) {
            mapType= 1;
        } else if (surveyEntity.getMapType().equalsIgnoreCase("polygon")) {
            mapType= 2;
        }
        startActivityForResult(SurveyTrackingActivity.getIntent(getContext(), farmerLands, mapType,true,surveyEntity), REQUEST_CODE);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onClickDeleteSurvey(SurveyEntity surveyEntity) {
        DeleteDialog deleteDialog = new DeleteDialog(this);
        deleteDialog.setTitle("Are You Sure!");
        deleteDialog.setPositiveLabel("Ok");
        deleteDialog.setPositiveListener(v -> {
            deleteDialog.dismiss();
            mpresenter.deleteApiCall(surveyEntity);
        });
        deleteDialog.setNegativeLabel("Cancel");
        deleteDialog.setNegativeListener(v -> {
            deleteDialog.dismiss();
        });
        deleteDialog.show();
    }

    @Override
    public void itemDeletedToast() {
        Toast.makeText(this, "Item Deleted successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemUpdatedToast() {
        Toast.makeText(this, "The details are updated successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void anotherizedToken() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private void previewDisplay(List<SurveyEntity> surveyEntities) {
        map.clear();
        boolean isIncludeLatLong = false;
        if (surveyEntities.size() > 0) {
            isNewSurveyCurrentLocation = false;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (SurveyEntity detailsEntity : surveyEntities) {
                if (!detailsEntity.isUnchecked()) {
                    if (detailsEntity.getMapType() != null) {
                        if (detailsEntity.getMapType().equalsIgnoreCase("point")) {
                            Gson gson = new Gson();
                            try {
                                SurveyModel.PointDetails pointDetails = gson.fromJson(detailsEntity.getLatLongs(), SurveyModel.PointDetails.class);
                                if (pointDetails != null) {
                                    LatLng latLng = new LatLng(pointDetails.getLatitude(), pointDetails.getLongitude());
                                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.new_point);
                                    builder.include(latLng);
                                    map.addMarker(new MarkerOptions().title(detailsEntity.getName())
                                            .position(latLng)
                                            .flat(true).icon(icon)
                                            .anchor(0.5f, 0.5f));
                                    isIncludeLatLong = true;
                                }
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        } else if (detailsEntity.getMapType().equalsIgnoreCase("line")) {
                            Gson gson = new Gson();
                            SurveyModel.PolyLineDetails polyLineDetails = gson.fromJson(detailsEntity.getLatLongs(), SurveyModel.PolyLineDetails.class);
                            int polylineWidth = 10;
                            Polyline runningPathPolyline = map.addPolyline(new PolylineOptions()
                                    .add(new LatLng(polyLineDetails.getFromLatitude(), polyLineDetails.getFromLongitude()), new LatLng(polyLineDetails.getToLatitude(), polyLineDetails.getToLongitude())).width(polylineWidth).color(Color.parseColor("#009919")).geodesic(true));
                            runningPathPolyline.setPattern(null);
                            builder.include(new LatLng(polyLineDetails.getFromLatitude(), polyLineDetails.getFromLongitude()));
                            builder.include(new LatLng(polyLineDetails.getToLatitude(), polyLineDetails.getToLongitude()));
                            isIncludeLatLong = true;
                        } else if (detailsEntity.getMapType().equalsIgnoreCase("polygon")) {
                            List<LatLng> polygonPoints = new ArrayList<>();
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<SurveyModel>>() {
                            }.getType();
                            List<SurveyModel> posts = gson.fromJson(detailsEntity.getLatLongs(), listType);
                            for (SurveyModel model : posts) {
                                LatLng location = new LatLng(model.getLatitude(), model.getLongitude());
                                builder.include(location);
                                polygonPoints.add(location);
                            }
                            if (polygonPoints.size() > 0) {
                                Polygon runningPathPolygon = map.addPolygon(new PolygonOptions()
                                        .addAll(polygonPoints));
                                runningPathPolygon.setStrokeColor(Color.parseColor("#009919"));
                                runningPathPolygon.setFillColor(Color.argb(20, 0, 255, 0));
                                isIncludeLatLong = true;
                            }
                        }
                    } // Due to Select all is not checking, code is commenting by Raghava
//                else {
//                    activitySurveyStatusBinding.checkBoxHeader.setChecked(false);
//                }
                }
            }
            if (isIncludeLatLong) {
                LatLngBounds bounds = builder.build();
                int padding = 30; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                map.animateCamera(cu);
            }
        } else {
            isNewSurveyCurrentLocation = true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }

    @Override
    public void onResume() {
        super.onResume();
        startStep1();
        // register connection status listener
        WaveApp.getInstance().setConnectivityListener(this);
        registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
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
                    .make(activitySurveyStatusBinding.surveDetailsRecyclerview, message, Snackbar.LENGTH_SHORT);

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
                    .make(activitySurveyStatusBinding.surveDetailsRecyclerview, message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
            isOffline = true;
        }
    }


    /**
     * Step 1: Check Google Play services
     */
    private void startStep1() {
        //Check whether this user has installed Google play service which is being used by Location updates.
        if (isGooglePlayServicesAvailable()) {
            if (checkPermissions()) {
                getMyLocation();
            } else {
                requestPermissions();

            }
        } else {
            Toast.makeText(getContext(), R.string.no_google_playservice_available, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Return the availability of GooglePlayServices
     */
    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(getContext());
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
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the img_user denied the permission
            // previously and checked "Never ask again".
        }
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
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
                int permissionLocation = ContextCompat.checkSelfPermission(this,
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
                                getLocation();
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied.
                                // But could be fixed by showing the user a dialog.
                                try {
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    // Ask to turn on GPS automatically
                                    status.startResolutionForResult(this,
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

    private synchronized void setUpGClient() {
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, 1, this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
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
    public void onLocationChanged(Location location) {
        if (isNewSurveyCurrentLocation) {
            LatLng loc = new LatLng(mLatitude, mLongitude);
            map.addMarker(new MarkerOptions().position(loc));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 21.0f));
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private boolean canGetLocation = false;
    private Location loc;

    private void getLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        // getting GPS status
        boolean checkGPS = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean checkNetwork = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!checkGPS && !checkNetwork) {
            Toast.makeText(this, "No Service Provider Available", Toast.LENGTH_SHORT).show();
        } else {
            this.canGetLocation = true;
            // First get location from Network Provider
            if (checkNetwork) {
                try {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                    if (locationManager != null) {
                        loc = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    }

                    if (loc != null) {
                        mLatitude = loc.getLatitude();
                        mLongitude = loc.getLongitude();
                    }
                } catch (SecurityException e) {

                }
            }
        }
        // if GPS Enabled get lat/long using GPS Services
        if (checkGPS) {
            if (loc == null) {
                try {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            1000,
                            0, this);
                    if (locationManager != null) {
                        loc = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null) {
                            mLatitude = loc.getLatitude();
                            mLongitude = loc.getLongitude();
                        }
                    }
                } catch (SecurityException e) {

                }
            }
        }
    }
}