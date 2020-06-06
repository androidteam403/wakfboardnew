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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
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
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartRes;
import com.thresholdsoft.praanadhara.databinding.ActivitySurveyStatusBinding;
import com.thresholdsoft.praanadhara.databinding.CustomActionbarBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.adapter.SurveyAdapter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.adapter.SurveyDetailsAdapter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.model.SurveyDetailsModel;
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
    private Context context;
    private ArrayList<RowsEntity> surveyModelArrayList = new ArrayList<>();
    CustomActionbarBinding customActionbarBinding;

    private SurveyStatusMvpView presenter;

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
    ArrayList<SurveyModel> surveyModelsList=new ArrayList<>();

    private Polyline runningPathPolyline;
    private Polygon runningPathPolygon;
    private int polylineWidth = 10;
    List<LatLng> polygonPoints = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySurveyStatusBinding = DataBindingUtil.setContentView(this, R.layout.activity_survey_status);
        getActivityComponent().inject(this);
        mpresenter.onAttach(SurveyStatusActivity.this);
        setUp();
    }

    @Override
    protected void setUp() {
        Intent intent = getIntent();
        surveyModel = (RowsEntity) intent.getSerializableExtra("surveyData");
        surveyModelArrayList.add(surveyModel);

        surveyDetailsAdapter= new SurveyDetailsAdapter(this, surveyModelsList, mpresenter);
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
                finish();
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (surveyModel != null && !TextUtils.isEmpty(surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().getLatlongs())) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<SurveyModel>>() {
            }.getType();
            List<SurveyModel> posts = gson.fromJson(surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().getLatlongs(), listType);
            for (SurveyModel model : posts) {
                LatLng location = new LatLng(model.getLatitude(), model.getLongitude());
                polygonPoints.add(location);
                if (model.isPoint()) {
                    map.addMarker(new MarkerOptions().title(model.getName())
                            .position(location)
                            .flat(true)
                            .anchor(0.5f, 0.5f));
                }
            }
            if (surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().getMapType().getName() != null) {
                if (surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().getMapType().getName().equalsIgnoreCase("Point")) {
                    runningPathPolyline = map.addPolyline(new PolylineOptions()
                            .clickable(true)
                            .addAll(polygonPoints).width(polylineWidth).color(Color.parseColor("#801B60FE")).geodesic(true));
                    runningPathPolyline.setPattern(PATTERN_POLYLINE_DOTTED);

                } else if (surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().getMapType().getName().equalsIgnoreCase("Line")) {
                    runningPathPolyline = map.addPolyline(new PolylineOptions()
                            .addAll(polygonPoints).width(polylineWidth).color(Color.parseColor("#801B60FE")).geodesic(true));
                    runningPathPolyline.setPattern(null);
                } else if (surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().getMapType().getName().equalsIgnoreCase("Polygon")) {
                    runningPathPolygon = map.addPolygon(new PolygonOptions()
                            .addAll(polygonPoints));
                    runningPathPolygon.setStrokeColor(Color.BLUE);
                    runningPathPolygon.setFillColor(Color.argb(20, 0, 255, 0));
                }
            }
            if (polygonPoints.size() > 0) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(polygonPoints.get(0), 21.5f));
            }
        } else {
            LatLng location = new LatLng(surveyModel.getCurrentLatitude(), surveyModel.getCurrentLongitude());
            map.addMarker(new MarkerOptions().position(location));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 21.5f));
        }

        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    @Override
    public void startSurvey(RowsEntity surveyModel) {
        mpresenter.startSurvey(surveyModel);
    }

    @Override
    public void startSurveySuccess(RowsEntity rowsEntity, SurveyStartRes data) {
        surveyModel.getFarmerLand().getSurveyLandLocation().setUid(data.getUid());
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            surveyModelsList.addAll((ArrayList<SurveyModel>) data.getSerializableExtra("surveySubmit"));
            surveyDetailsAdapter.notifyDataSetChanged();
//            boolean requiredValue = data.getBooleanExtra("surveySubmit", false);
//            if (requiredValue) {
//                Intent intent = getIntent();
//                intent.putExtra("surveySubmit", requiredValue);
//                setResult(RESULT_OK, intent);
//                finish();
//            }
        }
    }

    @Override
    public void anotherizedToken() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}