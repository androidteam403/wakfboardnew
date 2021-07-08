package com.thresholdsoft.wakfboard.ui.selectingformactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.ActivityFormBinding;
import com.thresholdsoft.wakfboard.ui.base.BaseActivity;
import com.thresholdsoft.wakfboard.ui.mainactivity.MainActiivty;

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
        Intent intent = new Intent(this, MainActiivty.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onSurveyClick() {
        Intent intent = new Intent(this, MainActiivty.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void anotherizedToken() {

    }
}