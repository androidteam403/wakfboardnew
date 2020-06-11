package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thresholdsoft.praanadhara.BuildConfig;
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.data.network.pojo.MapTypeEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyDetailsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartRes;
import com.thresholdsoft.praanadhara.databinding.ActivitySurveyStatusBinding;
import com.thresholdsoft.praanadhara.databinding.CustomActionbarBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.adapter.SurveyDetailsAdapter;
import com.thresholdsoft.praanadhara.ui.surveytrack.SurveyTrackingActivity;
import com.thresholdsoft.praanadhara.ui.surveytrack.model.SurveyModel;
import com.thresholdsoft.praanadhara.ui.userlogin.UserLoginActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class SurveyStatusActivity extends BaseActivity implements SurveyStatusMvpView, OnMapReadyCallback {
    @Inject
    SurveyStatusMvpPresenter<SurveyStatusMvpView> mpresenter;
    private ActivitySurveyStatusBinding activitySurveyStatusBinding;
    private RowsEntity surveyModel;
    private int pos;
    private Context context;
    private ArrayList<RowsEntity> surveyModelArrayList = new ArrayList<>();
    CustomActionbarBinding customActionbarBinding;

    private SurveyStatusMvpView mvpView;

    private GoogleMap map;

    public static final int REQUEST_CODE = 2;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);
    SurveyDetailsAdapter surveyDetailsAdapter;
    SurveyModel survey;

    private int polylineWidth = 10;


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

    @Override
    protected void setUp() {
        Intent intent = getIntent();
        surveyModel = (RowsEntity) intent.getSerializableExtra("surveyData");
        if (surveyModel != null && !TextUtils.isEmpty(surveyModel.getFarmerLand().getSurveyLandLocation().getUid())) {
            surveyModel.getFarmerLand().getSurveyLandLocation().setUid(surveyModel.getFarmerLand().getSurveyLandLocation().getUid());
        }
//        if(surveyModel!= null && surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().size() > 0){
//            ArrayList<SurveyModel> modelArrayList = new ArrayList<>();
//            for(SurveyDetailsEntity surveyDetailsEntity : surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails()){
//                modelArrayList.add(new SurveyModel(surveyDetailsEntity));
//            }
//            surveyModel.setSurveyModelArrayList(modelArrayList);
//        }
        surveyModelArrayList.add(surveyModel);

        surveyDetailsAdapter = new SurveyDetailsAdapter(this, surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails(), mpresenter, this);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        activitySurveyStatusBinding.surveDetailsRecyclerview.setLayoutManager(mLayoutManager1);
        activitySurveyStatusBinding.surveDetailsRecyclerview.setAdapter(surveyDetailsAdapter);

        activitySurveyStatusBinding.setPresenterCallback(mpresenter);
        activitySurveyStatusBinding.setSurvey(surveyModel);
        //  activitySurveyStatusBinding.setCallback(this);

        if (surveyModel.getPic().size() > 0) {
            Glide.with(getApplicationContext()).load(BuildConfig.IMAGE_URL + surveyModel.getPic().get(0).getPath()).placeholder(R.drawable.
                    placeholder).into(activitySurveyStatusBinding.image);
        }
        if (activitySurveyStatusBinding.pointsRadio.isChecked()) {
            activitySurveyStatusBinding.linesRadio.setChecked(false);
            activitySurveyStatusBinding.polygonRadio.setChecked(false);
            surveyModel.setSurveyType(0);
            MapTypeEntity mapTypeEntity = new MapTypeEntity();
            mapTypeEntity.setUid("point");
            mapTypeEntity.setName("point");
            surveyModel.setMapTypeEntity(mapTypeEntity);
        }

        View includedLayout = findViewById(R.id.backArrow);
        ImageView insideTheIncludedLayout = (ImageView) includedLayout.findViewById(R.id.imageButton);
        insideTheIncludedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        activitySurveyStatusBinding.checkBoxHeader.setOnClickListener(view -> {
            if (activitySurveyStatusBinding.checkBoxHeader.isChecked()) {
                for (int i = 0; i < surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().size(); i++) {
                    surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().get(i).setUnChecked(false);
                }
                surveyDetailsAdapter.notifyDataSetChanged();
                previewDisplay();
            } else {
                for (int i = 0; i < surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().size(); i++) {
                    surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().get(i).setUnChecked(true);
                }
                surveyDetailsAdapter.notifyDataSetChanged();
                previewDisplay();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        previewDisplay();
    }

    @Override
    public void startSurvey(RowsEntity surveyModel) {
        mpresenter.startSurvey(surveyModel);
    }

    @Override
    public void startSurveySuccess(RowsEntity rowsEntity, SurveyStartRes data) {
        surveyModel.getFarmerLand().getSurveyLandLocation().setUid(data.getUid());
        surveyModel.getFarmerLand().getSurveyLandLocation().getSubmitted().setUid("No");
    }

    @Override
    public void onpolygonRadioClick() {
        activitySurveyStatusBinding.polygonRadio.setChecked(true);
        activitySurveyStatusBinding.linesRadio.setChecked(false);
        activitySurveyStatusBinding.pointsRadio.setChecked(false);
        surveyModel.setSurveyType(2);
        MapTypeEntity mapTypeEntity = new MapTypeEntity();
        mapTypeEntity.setUid("polygon");
        mapTypeEntity.setName("polygon");
        surveyModel.setMapTypeEntity(mapTypeEntity);
    }

    @Override
    public void onLinesRadioClick() {
        activitySurveyStatusBinding.linesRadio.setChecked(true);
        activitySurveyStatusBinding.polygonRadio.setChecked(false);
        activitySurveyStatusBinding.pointsRadio.setChecked(false);
        surveyModel.setSurveyType(1);
        MapTypeEntity mapTypeEntity = new MapTypeEntity();
        mapTypeEntity.setUid("line");
        mapTypeEntity.setName("line");
        surveyModel.setMapTypeEntity(mapTypeEntity);
    }

    @Override
    public void onPointsRadioClick() {
        activitySurveyStatusBinding.pointsRadio.setChecked(true);
        activitySurveyStatusBinding.linesRadio.setChecked(false);
        activitySurveyStatusBinding.polygonRadio.setChecked(false);
        surveyModel.setSurveyType(0);
        MapTypeEntity mapTypeEntity = new MapTypeEntity();
        mapTypeEntity.setUid("point");
        mapTypeEntity.setName("point");
        surveyModel.setMapTypeEntity(mapTypeEntity);
    }

    @Override
    public void addSurvey(RowsEntity rowsEntity) {
        startActivityForResult(SurveyTrackingActivity.getIntent(this, rowsEntity), REQUEST_CODE);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void submitSurvey(RowsEntity rowsEntity) {
        mpresenter.submitSurvey(rowsEntity);
    }

    @Override
    public void surveySubmitSuccess(SurveyStartRes data) {
        Intent intent = getIntent();
        intent.putExtra("surveySubmit", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onListItemClicked(int position) {
        boolean isCheckedStatus = surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().get(position).isUnChecked();
        if (!isCheckedStatus) {
            surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().get(position).setUnChecked(true);
        } else {
            surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().get(position).setUnChecked(false);
        }
        surveyDetailsAdapter.notifyDataSetChanged();
        previewDisplay();
    }

    @Override
    public void deleteAnItem(int pos) {
        if (surveyModel != null) {
            for (int i = 0; i < surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().size(); i++) {
                surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().remove(pos);
            }
            surveyDetailsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void deleteApiCall() {
    }


    @Override
    public void onDeleteApiSuccess(int pos) {
        if (surveyModel != null) {
            surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().remove(pos);
            surveyDetailsAdapter.notifyDataSetChanged();
            previewDisplay();
        }
    }

    @Override
    public ArrayList<RowsEntity> getUidDetails() {
        return surveyModelArrayList;
    }

    @Override
    public RowsEntity getSurvey() {
        return surveyModel;
    }

    @Override
    public void onSuccessEditSurvey(String description, int postion) {
        surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().get(postion).setDescription(description);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            surveyModel.getFarmerLand().getSurveyLandLocation().setSurveyDetails((ArrayList<SurveyDetailsEntity>) data.getSerializableExtra("surveySubmit"));
            surveyDetailsAdapter.notifyDataSetChanged();
            previewDisplay();
        }
    }

    @Override
    public void anotherizedToken() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private void previewDisplay() {
        map.clear();
        boolean isIncludeLatLong = false;
        if (surveyModel != null && surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (SurveyDetailsEntity detailsEntity : surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails()) {
                if (!detailsEntity.isUnChecked()) {
                    if (detailsEntity.getMapType().getUid() != null) {
                        if (detailsEntity.getMapType().getUid().equalsIgnoreCase("point")) {
                            Gson gson = new Gson();
                            SurveyModel.PointDetails pointDetails = gson.fromJson(detailsEntity.getLatlongs(), SurveyModel.PointDetails.class);
                            LatLng latLng = new LatLng(pointDetails.getLatitude(), pointDetails.getLongitude());
                            builder.include(latLng);
                            map.addMarker(new MarkerOptions().title(detailsEntity.getName())
                                    .position(latLng)
                                    .flat(true)
                                    .anchor(0.5f, 0.5f));
                            isIncludeLatLong = true;
                        } else if (detailsEntity.getMapType().getUid().equalsIgnoreCase("line")) {
                            Gson gson = new Gson();
                            SurveyModel.PolyLineDetails polyLineDetails = gson.fromJson(detailsEntity.getLatlongs(), SurveyModel.PolyLineDetails.class);
                            Polyline runningPathPolyline = map.addPolyline(new PolylineOptions()
                                    .add(new LatLng(polyLineDetails.getFromLatitude(), polyLineDetails.getFromLongitude()), new LatLng(polyLineDetails.getToLatitude(), polyLineDetails.getToLongitude())).width(polylineWidth).color(Color.parseColor("#801B60FE")).geodesic(true));
                            runningPathPolyline.setPattern(null);
                            builder.include(new LatLng(polyLineDetails.getFromLatitude(), polyLineDetails.getFromLongitude()));
                            builder.include(new LatLng(polyLineDetails.getToLatitude(), polyLineDetails.getToLongitude()));
                            isIncludeLatLong = true;
                        } else if (detailsEntity.getMapType().getUid().equalsIgnoreCase("polygon")) {
                            List<LatLng> polygonPoints = new ArrayList<>();
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<SurveyModel>>() {
                            }.getType();
                            List<SurveyModel> posts = gson.fromJson(detailsEntity.getLatlongs(), listType);
                            for (SurveyModel model : posts) {
                                LatLng location = new LatLng(model.getLatitude(), model.getLongitude());
                                builder.include(location);
                                polygonPoints.add(location);
                            }
                            Polygon runningPathPolygon = map.addPolygon(new PolygonOptions()
                                    .addAll(polygonPoints));
                            runningPathPolygon.setStrokeColor(Color.BLUE);
                            runningPathPolygon.setFillColor(Color.argb(20, 0, 255, 0));
                            isIncludeLatLong = true;
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
            LatLng location = new LatLng(surveyModel.getCurrentLatitude(), surveyModel.getCurrentLongitude());
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