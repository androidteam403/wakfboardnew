package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.ActivitySurveyStatusBinding;
import com.thresholdsoft.praanadhara.databinding.CustomActionbarBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.FarmersResponse;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.adapter.SurveyStatusAdapter;
import com.thresholdsoft.praanadhara.ui.surveytrack.SurveyTrackingActivity;

import java.util.ArrayList;

import javax.inject.Inject;

public class SurveyStatusActivity extends BaseActivity implements SurveyStatusMvpView {
    @Inject
    SurveyStatusMvpPresenter<SurveyStatusMvpView> mpresenter;
    private ActivitySurveyStatusBinding activitySurveyStatusBinding;
    private SurveyStatusAdapter surveyStatusAdapter;
    private FarmersResponse.Data.ListData.Rows surveyModel;
    private ArrayList<FarmersResponse.Data.ListData.Rows> surveyModelArrayList = new ArrayList<>();
    CustomActionbarBinding customActionbarBinding;

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
        surveyModel = (FarmersResponse.Data.ListData.Rows) intent.getSerializableExtra("surveyData");
        surveyModelArrayList.add(surveyModel);
        surveyStatusAdapter = new SurveyStatusAdapter(this, surveyModelArrayList, this);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        activitySurveyStatusBinding.recyclerSurveyStatus.setLayoutManager(mLayoutManager1);
        activitySurveyStatusBinding.recyclerSurveyStatus.setAdapter(surveyStatusAdapter);
        surveyStatusAdapter.notifyDataSetChanged();
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
    public void startSurvey(FarmersResponse.Data.ListData.Rows surveyModel) {
        startActivity(SurveyTrackingActivity.getIntent(this, surveyModel));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
