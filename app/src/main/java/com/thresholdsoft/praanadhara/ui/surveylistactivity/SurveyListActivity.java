package com.thresholdsoft.praanadhara.ui.surveylistactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.ActivitySurveyListBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.adapter.SurveyAdapter;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.FarmersResponse;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusActivity;

import java.util.ArrayList;

import javax.inject.Inject;

public class SurveyListActivity extends BaseActivity implements SurveyListMvpView {
    @Inject
    SurveyListMvpPresenter<SurveyListMvpView> mpresenter;
    private ActivitySurveyListBinding activitySurveyListBinding;
    private SurveyAdapter surveyAdapter;
    private ArrayList<FarmersResponse.Data.ListData.Rows> surveyModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySurveyListBinding = DataBindingUtil.setContentView(this, R.layout.activity_survey_list);
        getActivityComponent().inject(this);
        mpresenter.onAttach(SurveyListActivity.this);
        setUp();
    }

    @Override
    protected void setUp() {
        mpresenter.farmersListApiCall();
        surveyAdapter = new SurveyAdapter(this, surveyModelArrayList, mpresenter);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        activitySurveyListBinding.recyclerSurveyList.setLayoutManager(mLayoutManager1);
        activitySurveyListBinding.recyclerSurveyList.setAdapter(surveyAdapter);
    }

    @Override
    public void onItemClick(FarmersResponse.Data.ListData.Rows surveyModel) {
        Intent intent = new Intent(this, SurveyStatusActivity.class);
        intent.putExtra("surveyData", surveyModel);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onFarmersRes(FarmersResponse farmersResponse) {
        surveyModelArrayList.clear();
        surveyModelArrayList.addAll(farmersResponse.getData().getListData().getRows());
        surveyAdapter.notifyDataSetChanged();
    }

    @Override
    public void arrayListClear() {
        surveyModelArrayList.clear();
        surveyAdapter.notifyDataSetChanged();
    }
}