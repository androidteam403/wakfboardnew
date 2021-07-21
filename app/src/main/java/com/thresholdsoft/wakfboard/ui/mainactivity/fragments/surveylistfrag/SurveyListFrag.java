package com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.ActivitySurveyListBinding;
import com.thresholdsoft.wakfboard.ui.base.BaseFragment;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.adapter.SurveyAdapter;
import com.thresholdsoft.wakfboard.ui.propertycreation.PropertyCreation;
import com.thresholdsoft.wakfboard.ui.propertycreation.model.PropertyData;
import com.thresholdsoft.wakfboard.ui.propertysurveystatus.PropertyPreview;
import com.thresholdsoft.wakfboard.utils.CommonUtils;

import java.util.List;

import javax.inject.Inject;

public class SurveyListFrag extends BaseFragment implements SurveyListMvpView {
    @Inject
    SurveyListMvpPresenter<SurveyListMvpView> mpresenter;
    private ActivitySurveyListBinding activitySurveyListBinding;
    private int PROPERTY_CREATION = 1234;
    private SurveyAdapter surveyAdapter;
    private List<PropertyData> propertyDataList;

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
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void setUp(View view) {
        TextView tittle = getActivity().findViewById(R.id.tittle);
        tittle.setText(R.string.label_survey_list);
        activitySurveyListBinding.setCallback(mpresenter);
        this.propertyDataList = mpresenter.getPropertylist();
        activitySurveyListBinding.propertyCreationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(PropertyCreation.getStartIntent(getContext()), PROPERTY_CREATION);
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        if (propertyDataList != null && propertyDataList.size() > 0) {
            surveyAdapter = new SurveyAdapter(getContext(), propertyDataList, this);
            RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            activitySurveyListBinding.recyclerSurveyList.setLayoutManager(mLayoutManager2);
            activitySurveyListBinding.recyclerSurveyList.setItemAnimator(new DefaultItemAnimator());
            activitySurveyListBinding.recyclerSurveyList.setAdapter(surveyAdapter);
            activitySurveyListBinding.recyclerSurveyList.setNestedScrollingEnabled(false);
            activitySurveyListBinding.noDataFound.setVisibility(View.GONE);
            activitySurveyListBinding.recyclerSurveyList.setVisibility(View.VISIBLE);
        } else {
            activitySurveyListBinding.noDataFound.setVisibility(View.VISIBLE);
            activitySurveyListBinding.recyclerSurveyList.setVisibility(View.GONE);
        }
        onSyncClick();
        onClickCreatePropertryIcon();
    }

    private void onClickCreatePropertryIcon() {
        ImageView createProperty = getActivity().findViewById(R.id.property_icon);
        createProperty.setOnClickListener(v -> {
            onClickPropertyCreation();
        });
    }

    @Override
    public void anotherizedToken() {

    }

    @Override
    public void onItemClickTakeSurveyLister(int position) {
        getLocationPermmision(position);
    }

    @Override
    public void onClickPropertyCreation() {
        startActivityForResult(PropertyCreation.getStartIntent(getContext()), PROPERTY_CREATION);
        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private void onSyncClick() {
        ImageView imageView = getActivity().findViewById(R.id.refresh_sync);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sync();
            }
        });
    }

    private void sync() {
        propertyDataList = mpresenter.getPropertylist();
        if (propertyDataList != null && propertyDataList.size() > 0) {
            surveyAdapter = new SurveyAdapter(getContext(), propertyDataList, SurveyListFrag.this);
            RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            activitySurveyListBinding.recyclerSurveyList.setLayoutManager(mLayoutManager2);
            activitySurveyListBinding.recyclerSurveyList.setItemAnimator(new DefaultItemAnimator());
            activitySurveyListBinding.recyclerSurveyList.setAdapter(surveyAdapter);
            activitySurveyListBinding.recyclerSurveyList.setNestedScrollingEnabled(false);
            activitySurveyListBinding.recyclerSurveyList.setVisibility(View.VISIBLE);
            activitySurveyListBinding.noDataFound.setVisibility(View.GONE);
        } else {
            activitySurveyListBinding.recyclerSurveyList.setVisibility(View.GONE);
            activitySurveyListBinding.noDataFound.setVisibility(View.VISIBLE);
        }
    }

    private static final int REQUEST_PERMISSION_LOCATION = 255; // int should be between 0 and 255
    int itemPosition = -1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocationPermmision(int position) {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_LOCATION);
            this.itemPosition = position;
        } else {
            this.itemPosition = position;
//            gpsAccess();
            Intent intent = new Intent(getContext(), PropertyPreview.class);
            intent.putExtra("PROPERTY_DATA_KEY", propertyDataList.get(itemPosition));
            intent.putExtra("propertyId", propertyDataList.get(itemPosition).getId());
            intent.putExtra("propertyName", propertyDataList.get(itemPosition).getPropertyName());
            intent.putExtra("measurements", propertyDataList.get(itemPosition).getMeasuredunit());
            intent.putExtra("id", propertyDataList.get(itemPosition).getId());
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sync();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We now have permission to use the location
//                gpsAccess();
                Intent intent = new Intent(getContext(), PropertyPreview.class);
                intent.putExtra("propertyId", propertyDataList.get(itemPosition).getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        }
    }

    private void gpsAccess() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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

        if (!gps_enabled) {
            // notify user
            new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.gps_network_not_enabled)
                    .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), CommonUtils.LOACTION_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            Intent intent = new Intent(getContext(), PropertyPreview.class);
            intent.putExtra("PROPERTY_DATA_KEY", propertyDataList.get(itemPosition));
            intent.putExtra("propertyId", propertyDataList.get(itemPosition).getId());
            intent.putExtra("propertyName", propertyDataList.get(itemPosition).getPropertyName());
            intent.putExtra("measurements", propertyDataList.get(itemPosition).getMeasuredunit());
            intent.putExtra("id", propertyDataList.get(itemPosition).getId());
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }


        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CommonUtils.LOACTION_REQUEST_CODE) {
//            gpsAccess();
//        }
//    }
}