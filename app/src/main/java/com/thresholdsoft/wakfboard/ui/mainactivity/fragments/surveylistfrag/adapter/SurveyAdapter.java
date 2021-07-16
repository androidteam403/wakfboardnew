package com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.AdapterSurveyListBinding;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.SurveyListMvpView;
import com.thresholdsoft.wakfboard.ui.propertycreation.model.PropertyData;

import java.util.List;

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.ViewHolder> {
    private Context context;
    private List<PropertyData> propertyDataList;
    private SurveyListMvpView surveyListMvpView;

    public SurveyAdapter(Context context, List<PropertyData> propertyDataList, SurveyListMvpView surveyListMvpView) {
        this.context = context;
        this.propertyDataList = propertyDataList;
        this.surveyListMvpView = surveyListMvpView;
    }

    @NonNull
    @Override
    public SurveyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterSurveyListBinding adapterSurveyListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.adapter_survey_list, parent, false);
        return new SurveyAdapter.ViewHolder(adapterSurveyListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SurveyAdapter.ViewHolder holder, int position) {
        PropertyData propertyData = propertyDataList.get(position);
        holder.adapterSurveyListBinding.setPropertyData(propertyData);
        if (propertyData.getPhotosList() != null && propertyData.getPhotosList().size() > 0)
            Glide.with(context).load(propertyData.getPhotosList().get(0)).error(R.drawable.placeholder).into(holder.adapterSurveyListBinding.image);

        if (propertyData.getMeasuredunit().equalsIgnoreCase("Square Meters")) {
            if (propertyData.getArea() != null) {
                holder.adapterSurveyListBinding.propertyArea.setText(propertyData.getArea() + "m²");
            }
        } else if (propertyData.getMeasuredunit().equalsIgnoreCase("Square Feet")) {
            holder.adapterSurveyListBinding.propertyArea.setText(propertyData.getArea() + "sq ft²");
        } else if (propertyData.getMeasuredunit().equalsIgnoreCase("Square yards")) {
            holder.adapterSurveyListBinding.propertyArea.setText(propertyData.getArea() + "sq yd²");
        } else if (propertyData.getMeasuredunit().equalsIgnoreCase("Acres")) {
            holder.adapterSurveyListBinding.propertyArea.setText(propertyData.getArea() + "acers");
        }

        holder.adapterSurveyListBinding.takeSurveyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (surveyListMvpView != null) {
                    surveyListMvpView.onItemClickTakeSurveyLister(position);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (surveyListMvpView != null) {
                    surveyListMvpView.onItemClickTakeSurveyLister(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return propertyDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterSurveyListBinding adapterSurveyListBinding;

        public ViewHolder(@NonNull AdapterSurveyListBinding adapterSurveyListBinding) {
            super(adapterSurveyListBinding.getRoot());
            this.adapterSurveyListBinding = adapterSurveyListBinding;
        }
    }

}