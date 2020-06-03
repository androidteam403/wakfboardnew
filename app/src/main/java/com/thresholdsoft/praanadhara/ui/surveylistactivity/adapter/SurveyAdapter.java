package com.thresholdsoft.praanadhara.ui.surveylistactivity.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.databinding.AdapterSurveyListBinding;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.SurveyListMvpPresenter;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.SurveyListMvpView;

import java.util.ArrayList;
import java.util.List;

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.ViewHolder> {

    private ArrayList<RowsEntity> surveyModelArrayList;
    private SurveyListMvpPresenter<SurveyListMvpView> mPresenter;
    private Activity activity;

    public SurveyAdapter(ArrayList<RowsEntity> rowsEntities) {
        this.surveyModelArrayList = rowsEntities;
    }

    public SurveyAdapter(Activity activity, ArrayList<RowsEntity> surveyModelArrayList,
                         SurveyListMvpPresenter<SurveyListMvpView> mPresenter) {
        this.activity = activity;
        this.surveyModelArrayList = surveyModelArrayList;
        this.mPresenter = mPresenter;
    }

    @NonNull
    @Override
    public SurveyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterSurveyListBinding adapterSurveyListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.adapter_survey_list, parent, false);
        return new SurveyAdapter.ViewHolder(adapterSurveyListBinding);
    }

    @Override
    public void onBindViewHolder(SurveyAdapter.ViewHolder holder, int position) {
        final RowsEntity farmerModel = surveyModelArrayList.get(position);
        holder.adapterSurveyListBinding.setSurvey(farmerModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onItemClick(farmerModel);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public AdapterSurveyListBinding adapterSurveyListBinding;

        public ViewHolder(@NonNull AdapterSurveyListBinding adapterSurveyListBinding) {
            super(adapterSurveyListBinding.getRoot());
            this.adapterSurveyListBinding = adapterSurveyListBinding;
        }
    }

    @Override
    public int getItemCount() {
        return surveyModelArrayList.size();
    }

    public void addItems(List<RowsEntity> blogList) {
        surveyModelArrayList.addAll(blogList);
        notifyDataSetChanged();
    }

    public void clearItems() {
        surveyModelArrayList.clear();
    }
}
