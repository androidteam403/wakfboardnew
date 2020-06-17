package com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BuildConfig;
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
import com.google.android.material.snackbar.Snackbar;
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.databinding.ActivitySurveyListBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseFragment;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.adapter.SurveyAdapter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusActivity;
import com.thresholdsoft.praanadhara.ui.userlogin.UserLoginActivity;

import java.util.Objects;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

public class SurveyListFrag extends BaseFragment implements SurveyListMvpView, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    @Inject
    SurveyListMvpPresenter<SurveyListMvpView> mpresenter;
    private ActivitySurveyListBinding activitySurveyListBinding;
    private boolean isLoading = false;

    SurveyAdapter surveyAdapter;
    public static final int REQUEST_CODE = 1;
    private static final String TAG = SurveyListFrag.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private double mLatitude;
    private double mLongitude;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activitySurveyListBinding = DataBindingUtil.inflate(inflater, R.layout.activity_survey_list, container, false);
        getActivityComponent().inject(this);
        mpresenter.onAttach(SurveyListFrag.this);
        return activitySurveyListBinding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setUpGClient();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void setUp(View view) {
        mpresenter.onStatusCountApiCall(false);
        activitySurveyListBinding.setCallback(mpresenter);

        surveyAdapter = new SurveyAdapter();
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getContext());
        activitySurveyListBinding.recyclerSurveyList.setLayoutManager(mLayoutManager1);
        activitySurveyListBinding.recyclerSurveyList.setAdapter(surveyAdapter);
        initScrollListener();

        activitySurveyListBinding.simpleSwipeRefreshLayout.setOnRefreshListener(() -> {
            mpresenter.pullToRefreshApiCall();
        });

        mpresenter.getAllFarmersLands().observe(getBaseActivity(), notes -> {
            surveyAdapter.submitList(notes);
        });

        mpresenter.getSurveyStatusCount().observe(getBaseActivity(), surveyStatusEntity -> activitySurveyListBinding.setCount(surveyStatusEntity));

        surveyAdapter.setOnItemClickListener(farmerLands -> {
            Intent intent = new Intent(getContext(), SurveyStatusActivity.class);
            intent.putExtra("surveyData", farmerLands.getUid());
            intent.putExtra("landUid", farmerLands.getFarmerLandUid());
            intent.putExtra("currentLatitude", mLatitude);
            intent.putExtra("currentLongitude", mLongitude);
            startActivityForResult(intent, REQUEST_CODE);
            getBaseActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        });

    }

    private void initScrollListener() {
        activitySurveyListBinding.recyclerSurveyList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == surveyAdapter.getItemCount() - 1) {
                        //bottom of list!
                        //   loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }


    private void loadMore() {
        surveyAdapter.addItemData(null);
        surveyAdapter.notifyItemInserted(surveyAdapter.getItemCount() - 1);
        mpresenter.loadMoreApiCall(surveyAdapter.loadMorePageNumber());
    }

    @Override
    public void statusBaseListFilter(String status) {
        surveyAdapter.statusFilter(status);
    }

    @Override
    public void onItemClick(FarmerLands farmerModel) {
        Intent intent = new Intent(getContext(), SurveyStatusActivity.class);
        intent.putExtra("surveyData", farmerModel.getUid());
        intent.putExtra("landUid", farmerModel.getFarmerLandUid());
        intent.putExtra("currentLatitude", mLatitude);
        intent.putExtra("currentLongitude", mLongitude);
        startActivityForResult(intent, REQUEST_CODE);
        getBaseActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onSuccessPullToRefresh() {
        activitySurveyListBinding.simpleSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onSuccessLoadMore() {
        activitySurveyListBinding.noDataFound.setVisibility(View.GONE);
        isLoading = false;
        surveyAdapter.removeItemData(surveyAdapter.getItemCount() - 1);
        int scrollPosition = surveyAdapter.getItemCount();
        surveyAdapter.notifyItemRemoved(scrollPosition);
//        surveyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessLoadMoreNodData() {
        activitySurveyListBinding.noDataFound.setVisibility(View.GONE);
        surveyAdapter.removeItemData(surveyAdapter.getItemCount() - 1);
        int scrollPosition = surveyAdapter.getItemCount();
        surveyAdapter.notifyItemRemoved(scrollPosition);
        surveyAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayNoData() {
        activitySurveyListBinding.noDataFound.setVisibility(View.VISIBLE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            boolean requiredValue = data.getBooleanExtra("surveySubmit", false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startStep1();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getBaseActivity());
        mGoogleApiClient.disconnect();
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
                googleApiAvailability.getErrorDialog(getActivity(), status, 2404).show();
            }
            return false;
        }
        return true;
    }

    /**
     * Return the current state of the permissions needed.
     */

    private boolean checkPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(getBaseActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        int permissionState2 = ActivityCompat.checkSelfPermission(getBaseActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);

        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;

    }


    /**
     * Start permissions requests.
     */
    private void requestPermissions() {

        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getBaseActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        boolean shouldProvideRationale2 =
                ActivityCompat.shouldShowRequestPermissionRationale(getBaseActivity(),
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
        ActivityCompat.requestPermissions(getBaseActivity(),
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
                Objects.requireNonNull(getView()).getRootView().findViewById(android.R.id.content),
                getString(R.string.permission_denied_explanation),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.settings), listener).show();
    }


    private void getMyLocation() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(getBaseActivity(),
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

                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied.
                                // But could be fixed by showing the user a dialog.
                                try {
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    // Ask to turn on GPS automatically
                                    status.startResolutionForResult(getActivity(),
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
        mGoogleApiClient = new GoogleApiClient.Builder(Objects.requireNonNull(getActivity()))
                .enableAutoManage(getActivity(), 1, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
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
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
    }

    @Override
    public void anotherizedToken() {
        mpresenter.anotherizedTokenClearDate();
        Intent intent = new Intent(getContext(), UserLoginActivity.class);
        startActivity(intent);
        getBaseActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu); // Put your search menu in "menu_search" menu file.
        MenuItem mSearchItem = menu.findItem(R.id.action_search);
        SearchView sv = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        sv.setIconified(true);

        SearchManager searchManager = (SearchManager) getBaseActivity().getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            sv.setSearchableInfo(searchManager.getSearchableInfo(getBaseActivity().getComponentName()));
            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    surveyAdapter.getFilter().filter(query);
                    return true;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
}