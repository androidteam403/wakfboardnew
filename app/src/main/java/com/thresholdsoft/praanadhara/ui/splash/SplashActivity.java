package com.thresholdsoft.praanadhara.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.ActivitySplashBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
import com.thresholdsoft.praanadhara.ui.selectingformactivity.SelectingFormActivity;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity implements SplashMvpView {

    @Inject
    SplashMvpPresenter<SplashMvpView> mPresenter;
    private ActivitySplashBinding activitySplashBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        getActivityComponent().inject(this);
        mPresenter.onAttach(SplashActivity.this);
        setUp();
    }

    @Override
    protected void setUp() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        activitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(SplashActivity.this, SelectingFormActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        }, 2000);
    }

}