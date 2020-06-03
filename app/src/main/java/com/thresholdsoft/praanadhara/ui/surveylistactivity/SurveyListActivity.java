package com.thresholdsoft.praanadhara.ui.surveylistactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.databinding.ActivitySurveyListBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.adapter.SurveyAdapter;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.FarmersResponse;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SurveyListActivity extends BaseActivity implements SurveyListMvpView {
    @Inject
    SurveyListMvpPresenter<SurveyListMvpView> mpresenter;
    private ActivitySurveyListBinding activitySurveyListBinding;
    private ArrayList<RowsEntity> surveyModelArrayList = new ArrayList<>();
    @Inject
    SurveyAdapter surveyAdapter;

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
        surveyAdapter = new SurveyAdapter(this, surveyModelArrayList, mpresenter);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        activitySurveyListBinding.recyclerSurveyList.setLayoutManager(mLayoutManager1);
        activitySurveyListBinding.recyclerSurveyList.setAdapter(surveyAdapter);

        mpresenter.farmersListApiCall();
    }

    @Override
    public void onItemClick(RowsEntity farmerModel) {
        Intent intent = new Intent(this, SurveyStatusActivity.class);
        intent.putExtra("surveyData", farmerModel);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onFarmersRes(List<RowsEntity> rowsEntity) {
        surveyModelArrayList.clear();
        surveyModelArrayList.addAll(rowsEntity);
        surveyAdapter.notifyDataSetChanged();
    }

    @Override
    public void arrayListClear() {
        surveyModelArrayList.clear();
        surveyAdapter.notifyDataSetChanged();
    }
}