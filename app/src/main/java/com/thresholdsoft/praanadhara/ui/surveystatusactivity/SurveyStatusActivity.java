package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.ActivitySurveyStatusBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.SurveyModel;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.adapter.SurveyStatusAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

public class SurveyStatusActivity extends BaseActivity implements SurveyStatusMvpView {
    @Inject
    SurveyStatusMvpPresenter<SurveyStatusMvpView> mpresenter;
    private ActivitySurveyStatusBinding activitySurveyStatusBinding;
    private SurveyStatusAdapter surveyStatusAdapter;
    private SurveyModel surveyModel;
    private ArrayList<SurveyModel> surveyModelArrayList = new ArrayList<>();

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
        surveyModel = (SurveyModel) intent.getSerializableExtra("surveyData");
        surveyModelArrayList.add(surveyModel);
        surveyStatusAdapter = new SurveyStatusAdapter(this, surveyModelArrayList, mpresenter);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        activitySurveyStatusBinding.recyclerSurveyStatus.setLayoutManager(mLayoutManager1);
        activitySurveyStatusBinding.recyclerSurveyStatus.setAdapter(surveyStatusAdapter);
        surveyStatusAdapter.notifyDataSetChanged();
    }

}
