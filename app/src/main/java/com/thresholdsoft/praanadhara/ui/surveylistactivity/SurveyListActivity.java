package com.thresholdsoft.praanadhara.ui.surveylistactivity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.ActivitySurveyListBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.adapter.SurveyAdapter;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.SurveyModel;

import java.util.ArrayList;

import javax.inject.Inject;

public class SurveyListActivity extends BaseActivity implements SurveyListMvpView {
    @Inject
    SurveyListMvpPresenter<SurveyListMvpView> mpresenter;
    private ActivitySurveyListBinding activitySurveyListBinding;
    private SurveyAdapter surveyAdapter;
    private ArrayList<SurveyModel> surveyModelArrayList = new ArrayList<>();

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
        surveyDetails();
    }

    private void surveyDetails() {
        SurveyModel surveyModel = new SurveyModel("Narayana Swamy.P", "Sannakunta Village,VuyyuruMandal,Chitoor District", "6755656545",
                "narayana@gmail.com", "01-10-2020", "New", "TAKE SURVEY");
        surveyModelArrayList.add(surveyModel);
        surveyModel = new SurveyModel("Veera Swamy.P", "Sannakunta Village,VuyyuruMandal,Chitoor District", "6755656545",
                "narayana@gmail.com", "01-10-2020", "In Progress", "CONTINUE");
        surveyModelArrayList.add(surveyModel);
        surveyModel = new SurveyModel("Narayana Swamy.P", "Sannakunta Village,VuyyuruMandal,Chitoor District", "6755656545",
                "narayana@gmail.com", "01-10-2020", "Completed", "DONE");
        surveyModelArrayList.add(surveyModel);
        surveyModel = new SurveyModel("Govinda Swamy.P", "Sannakunta Village,VuyyuruMandal,Chitoor District", "6755656545",
                "narayana@gmail.com", "01-10-2020", "New", "TAKE SURVEY");
        surveyModelArrayList.add(surveyModel);
        surveyModel = new SurveyModel("Siva Swamy.P", "Sannakunta Village,VuyyuruMandal,Chitoor District", "6755656545",
                "narayana@gmail.com", "01-10-2020", "In Progress", "CONTINUE");
        surveyModelArrayList.add(surveyModel);
        surveyModel = new SurveyModel("Mani Swamy.P", "Sannakunta Village,VuyyuruMandal,Chitoor District", "6755656545",
                "narayana@gmail.com", "01-10-2020", "Completed", "DONE");
        surveyModelArrayList.add(surveyModel);
        surveyModel = new SurveyModel("Narayana Swamy.P", "Sannakunta Village,VuyyuruMandal,Chitoor District", "6755656545",
                "narayana@gmail.com", "01-10-2020", "New", "TAKE SURVEY");
        surveyModelArrayList.add(surveyModel);
        surveyAdapter.notifyDataSetChanged();


    }
}