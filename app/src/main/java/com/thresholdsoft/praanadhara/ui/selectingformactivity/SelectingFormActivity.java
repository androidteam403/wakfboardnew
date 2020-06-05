package com.thresholdsoft.praanadhara.ui.selectingformactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.ActivityFormBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.SurveyListActivity;
import com.thresholdsoft.praanadhara.ui.userlogin.UserLoginActivity;

import javax.inject.Inject;

public class SelectingFormActivity extends BaseActivity implements SelectingFormMvpView {
    @Inject
    SelectingFormMvpPresenter<SelectingFormMvpView> mpresenter;
    ActivityFormBinding activityFormBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFormBinding = DataBindingUtil.setContentView(this, R.layout.activity_form);
        getActivityComponent().inject(this);
        mpresenter.onAttach(SelectingFormActivity.this);
        setUp();
    }

    @Override
    protected void setUp() {
        activityFormBinding.setPresenter(mpresenter);
    }

    @Override
    public void onEnrollmentClick() {
        Intent intent = new Intent(this, SurveyListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onSurveyClick() {
        Intent intent = new Intent(this, SurveyListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void anotherizedToken() {

    }
}