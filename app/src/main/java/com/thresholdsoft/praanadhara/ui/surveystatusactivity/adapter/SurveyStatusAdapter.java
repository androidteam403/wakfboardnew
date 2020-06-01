package com.thresholdsoft.praanadhara.ui.surveystatusactivity.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.AdapterSurveyStatusBinding;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.SurveyModel;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpPresenter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpView;

import java.util.ArrayList;

public class SurveyStatusAdapter extends RecyclerView.Adapter<SurveyStatusAdapter.ViewHolder> implements OnMapReadyCallback {

    private ArrayList<SurveyModel> surveyModelArrayList;
    private SurveyStatusMvpView mPresenter;
    private Activity activity;
    private GoogleMap map;
    public SurveyStatusAdapter(Activity activity, ArrayList<SurveyModel> surveyModelArrayList,
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
        final SurveyModel surveyModel = surveyModelArrayList.get(position);
        holder.adapterSurveyStatusBinding.setSurvey(surveyModel);
        holder.adapterSurveyStatusBinding.setCallback(mPresenter);
        SupportMapFragment mapFragment = (SupportMapFragment) ((FragmentActivity)activity).getSupportFragmentManager().findFragmentById(R.id.map);
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
            }
        });
        holder.adapterSurveyStatusBinding.pointsRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.adapterSurveyStatusBinding.pointsRadio.setChecked(true);
                holder.adapterSurveyStatusBinding.linesRadio.setChecked(false);
                holder.adapterSurveyStatusBinding.polygonRadio.setChecked(false);
                surveyModel.setSurveyType(0);
            }
        });
        holder.adapterSurveyStatusBinding.polygonRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.adapterSurveyStatusBinding.polygonRadio.setChecked(true);
                holder.adapterSurveyStatusBinding.linesRadio.setChecked(false);
                holder.adapterSurveyStatusBinding.pointsRadio.setChecked(false);
                surveyModel.setSurveyType(2);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(activity);
        //LatLng class is google provided class to get latiude and longitude of location.
        //GpsTracker is helper class to get the details for current location latitude and longitude.
        LatLng location = new LatLng(17.2942063, 78.5675261);
        map = googleMap;
        map.addMarker(new MarkerOptions().position(location).title("Marker position"));
        map.moveCamera(CameraUpdateFactory.newLatLng(location));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
