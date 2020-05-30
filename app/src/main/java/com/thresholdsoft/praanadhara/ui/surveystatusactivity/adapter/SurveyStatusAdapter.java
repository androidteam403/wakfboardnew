package com.thresholdsoft.praanadhara.ui.surveystatusactivity.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.AdapterSurveyStatusBinding;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.SurveyModel;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpPresenter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpView;

import java.util.ArrayList;

public class SurveyStatusAdapter extends RecyclerView.Adapter<SurveyStatusAdapter.ViewHolder> {

    private ArrayList<SurveyModel> surveyModelArrayList;
    private SurveyStatusMvpPresenter<SurveyStatusMvpView> mPresenter;
    private Activity activity;

    public SurveyStatusAdapter(Activity activity, ArrayList<SurveyModel> surveyModelArrayList,
                               SurveyStatusMvpPresenter<SurveyStatusMvpView> mPresenter) {
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
            }
        });
        holder.adapterSurveyStatusBinding.pointsRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.adapterSurveyStatusBinding.pointsRadio.setChecked(true);
                holder.adapterSurveyStatusBinding.linesRadio.setChecked(false);
                holder.adapterSurveyStatusBinding.polygonRadio.setChecked(false);
            }
        });
        holder.adapterSurveyStatusBinding.polygonRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.adapterSurveyStatusBinding.polygonRadio.setChecked(true);
                holder.adapterSurveyStatusBinding.linesRadio.setChecked(false);
                holder.adapterSurveyStatusBinding.pointsRadio.setChecked(false);
            }
        });
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
