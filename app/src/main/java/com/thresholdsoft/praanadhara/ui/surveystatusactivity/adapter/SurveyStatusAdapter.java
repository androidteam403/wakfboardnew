package com.thresholdsoft.praanadhara.ui.surveystatusactivity.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thresholdsoft.praanadhara.BuildConfig;
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.data.network.pojo.MapTypeEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.databinding.AdapterSurveyStatusBinding;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpView;
import com.thresholdsoft.praanadhara.ui.surveytrack.model.SurveyModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SurveyStatusAdapter extends RecyclerView.Adapter<SurveyStatusAdapter.ViewHolder> implements OnMapReadyCallback {

    private ArrayList<RowsEntity> surveyModelArrayList;
    private SurveyStatusMvpView mPresenter;
    private Activity activity;
    private GoogleMap map;
    private RowsEntity surveyModel;

    public SurveyStatusAdapter(Activity activity, ArrayList<RowsEntity> surveyModelArrayList,
                               SurveyStatusMvpView mPresenter) {
        this.activity = activity;
        this.surveyModelArrayList = surveyModelArrayList;
        this.mPresenter = mPresenter;
    }

    @NonNull
    @Override
    public SurveyStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterSurveyStatusBinding adapterSurveyStatusBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.adapter_survey_status, parent, false);
        return new SurveyStatusAdapter.ViewHolder(adapterSurveyStatusBinding);
    }

    @Override
    public void onBindViewHolder(SurveyStatusAdapter.ViewHolder holder, int position) {
        surveyModel = surveyModelArrayList.get(position);
        holder.adapterSurveyStatusBinding.setSurvey(surveyModel);
        holder.adapterSurveyStatusBinding.setCallback(mPresenter);
        if (surveyModel.getPic().size() > 0) {
            Glide.with(activity).load(BuildConfig.IMAGE_URL + surveyModel.getPic().get(0).getPath()).placeholder(R.drawable.
                    placeholder).into(holder.adapterSurveyStatusBinding.image);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) ((FragmentActivity) activity).
                getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        if (holder.adapterSurveyStatusBinding.pointsRadio.isChecked()) {
            holder.adapterSurveyStatusBinding.linesRadio.setChecked(false);
            holder.adapterSurveyStatusBinding.polygonRadio.setChecked(false);
        }
        holder.adapterSurveyStatusBinding.linesRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.adapterSurveyStatusBinding.linesRadio.setChecked(true);
                holder.adapterSurveyStatusBinding.polygonRadio.setChecked(false);
                holder.adapterSurveyStatusBinding.pointsRadio.setChecked(false);
                surveyModel.setSurveyType(1);
                MapTypeEntity mapTypeEntity = new MapTypeEntity();
                mapTypeEntity.setUid("3131041D77AB7EF652D5A4C357B988BB");
                mapTypeEntity.setName("Line");
                surveyModel.setMapTypeEntity(mapTypeEntity);
            }
        });
        holder.adapterSurveyStatusBinding.pointsRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.adapterSurveyStatusBinding.pointsRadio.setChecked(true);
                holder.adapterSurveyStatusBinding.linesRadio.setChecked(false);
                holder.adapterSurveyStatusBinding.polygonRadio.setChecked(false);
                surveyModel.setSurveyType(0);
                MapTypeEntity mapTypeEntity = new MapTypeEntity();
                mapTypeEntity.setUid("63471DD49DCCC9FC18B11014EB119E6E");
                mapTypeEntity.setName("Point");
                surveyModel.setMapTypeEntity(mapTypeEntity);
            }
        });
        holder.adapterSurveyStatusBinding.polygonRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.adapterSurveyStatusBinding.polygonRadio.setChecked(true);
                holder.adapterSurveyStatusBinding.linesRadio.setChecked(false);
                holder.adapterSurveyStatusBinding.pointsRadio.setChecked(false);
                surveyModel.setSurveyType(2);
                MapTypeEntity mapTypeEntity = new MapTypeEntity();
                mapTypeEntity.setUid("555CB32FDE8B6C3CAB1D7E69A9FE1ED0");
                mapTypeEntity.setName("Polygon");
                surveyModel.setMapTypeEntity(mapTypeEntity);
            }
        });
    }
    private Polyline runningPathPolyline;
    private int polylineWidth = 10;
    List<LatLng> polygonPoints = new ArrayList<>();
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(activity);
        //LatLng class is google provided class to get latiude and longitude of location.
        //GpsTracker is helper class to get the details for current location latitude and longitude.
        map = googleMap;
        if(surveyModel != null && !TextUtils.isEmpty(surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().getLatlongs())){
            Gson gson = new Gson();
            Type listType = new TypeToken<List<SurveyModel>>(){}.getType();
            List<SurveyModel> posts = gson.fromJson(surveyModel.getFarmerLand().getSurveyLandLocation().getSurveyDetails().getLatlongs(), listType);
            for(SurveyModel model : posts){
                LatLng location = new LatLng(model.getLatitude(), model.getLongitude());
                polygonPoints.add(location);
            }
            runningPathPolyline = map.addPolyline(new PolylineOptions()
                    .addAll(polygonPoints).width(polylineWidth).color(Color.parseColor("#801B60FE")).geodesic(true));
            runningPathPolyline.setPattern(null);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(polygonPoints.get(0), 21.5f));
        }else{
            LatLng location = new LatLng(surveyModel.getCurrentLatitude(), surveyModel.getCurrentLongitude());
            map.addMarker(new MarkerOptions().position(location));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location,21.5f));
        }

        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public AdapterSurveyStatusBinding adapterSurveyStatusBinding;

        public ViewHolder(@NonNull AdapterSurveyStatusBinding adapterSurveyStatusBinding) {
            super(adapterSurveyStatusBinding.getRoot());
            this.adapterSurveyStatusBinding = adapterSurveyStatusBinding;
        }
    }

    @Override
    public int getItemCount() {
        return surveyModelArrayList.size();
    }

}
