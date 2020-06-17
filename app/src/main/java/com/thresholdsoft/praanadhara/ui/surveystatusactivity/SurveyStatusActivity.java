package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.data.db.model.SurveyEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartRes;
import com.thresholdsoft.praanadhara.databinding.ActivitySurveyStatusBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyListModel;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.adapter.SurveyDetailsAdapter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.bottomsheet.SurveyStatusBottomSheet;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.CustomEditDialog;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.deletedialog.DeleteDialog;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.swipe.ItemTouchHelperCallback;
import com.thresholdsoft.praanadhara.ui.surveytrack.model.SurveyModel;
import com.thresholdsoft.praanadhara.ui.userlogin.UserLoginActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SurveyStatusActivity extends BaseActivity implements SurveyStatusMvpView, OnMapReadyCallback {
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
        mpresenter.getFarmerLand(uid, landUid).observe(this, farmerLands -> activitySurveyStatusBinding.setFarmerLand(farmerLands));

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
        mpresenter.getFarmerLand(uid, landUid).observe(this, farmerLands -> activitySurveyStatusBinding.backArrow.setFarmerLand(farmerLands));
        imageInsideLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet = new SurveyStatusBottomSheet();
                bottomSheet.setPresenterData(mpresenter);
                Bundle bundle = new Bundle();
                bundle.putString("surveyDataSheet", uid);
                bundle.putString("landUidSheet", landUid);
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
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
            activitySurveyStatusBinding.setSurvey(surveyEntities.size() > 0);
            previewDisplay(surveyEntities);
        });
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
                fullMapParams.height = 800;
                mapFrameLayout.setLayoutParams(fullMapParams);
                activitySurveyStatusBinding.setExpandView(1);
                activitySurveyStatusBinding.setCollapseView(0);
              //  previewDisplay(surveyDetailsAdapter.getListData());
            }
        });
    }

    @Override
    public void startSurveySuccess() {
        mpresenter.updateFarmerLandStatus(uid, landUid);
    }

    @Override
    public void onpolygonRadioClick() {
//        activitySurveyStatusBinding.polygonRadio.setChecked(true);
//        activitySurveyStatusBinding.linesRadio.setChecked(false);
//        activitySurveyStatusBinding.pointsRadio.setChecked(false);
//        mapType = 2;
    }

    @Override
    public void onLinesRadioClick() {
//        activitySurveyStatusBinding.linesRadio.setChecked(true);
//        activitySurveyStatusBinding.polygonRadio.setChecked(false);
//        activitySurveyStatusBinding.pointsRadio.setChecked(false);
//        mapType = 1;
    }

    @Override
    public void onPointsRadioClick() {
//        activitySurveyStatusBinding.pointsRadio.setChecked(true);
//        activitySurveyStatusBinding.linesRadio.setChecked(false);
//        activitySurveyStatusBinding.polygonRadio.setChecked(false);
//        mapType = 0;
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
        mpresenter.updateLandSurveySubmit(uid,landUid);
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
        surveyDetailsAdapter.notifyDataSetChanged();
        previewDisplay(surveyDetailsAdapter.getListData());
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
        customEditDialog.setNegativeUpdateListener(v -> customEditDialog.dismiss());
        customEditDialog.show();
    }

    @Override
    public void onClickDeleteSurvey(SurveyEntity surveyEntity) {
        DeleteDialog deleteDialog = new DeleteDialog(this);
        deleteDialog.setTitle("Are You Sure!");
        deleteDialog.setPositiveLabel("Ok");
        deleteDialog.setPositiveListener(v -> {
            mpresenter.deleteApiCall(surveyEntity);
            deleteDialog.dismiss();
        });
        deleteDialog.setNegativeLabel("Cancel");
        deleteDialog.setNegativeListener(v -> deleteDialog.dismiss());
        deleteDialog.show();
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
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (SurveyEntity detailsEntity : surveyEntities) {
                if (!detailsEntity.isUnchecked()) {
                    if (detailsEntity.getMapType() != null) {
                        if (detailsEntity.getMapType().equalsIgnoreCase("point")) {
                            Gson gson = new Gson();
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
            LatLng location = new LatLng(getIntent().getDoubleExtra("currentLatitude",0), getIntent().getDoubleExtra("currentLongitude",0));
            map.addMarker(new MarkerOptions().position(location));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 21.0f));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}