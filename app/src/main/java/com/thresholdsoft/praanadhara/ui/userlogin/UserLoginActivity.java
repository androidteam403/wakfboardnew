package com.thresholdsoft.praanadhara.ui.userlogin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.mukesh.OnOtpCompletionListener;
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.ActivityUserLoginBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
import com.thresholdsoft.praanadhara.ui.mainactivity.MainActiivty;

import javax.inject.Inject;

public class UserLoginActivity extends BaseActivity implements UserLoginMvpView, OnOtpCompletionListener {
    @Inject
    UserLoginMvpPresenter<UserLoginMvpView> mPresenter;
    private ActivityUserLoginBinding activityLoginBinding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_login);
        getActivityComponent().inject(this);
        mPresenter.onAttach(UserLoginActivity.this);
        setUp();
    }

    @Override
    protected void setUp() {
        activityLoginBinding.setLogonView(1);
        activityLoginBinding.setPresenter(mPresenter);
        activityLoginBinding.phoneNo.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        activityLoginBinding.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        activityLoginBinding.otpView.setOtpCompletionListener(this);
    }

    @Override
    public void onLoginClick() {
        if (validation()) {
            mPresenter.onLiginApiCall();
        }
    }

    @Override
    public void onSucessfullLogin() {
        activityLoginBinding.setLogonView(0);
        activityLoginBinding.setOtpView(1);
        activityLoginBinding.setTimeView(1);
        activityLoginBinding.number.setText(activityLoginBinding.phoneNo.getText().toString());
        backCountTimer();
    }

    @Override
    public String getOtp() {
        return activityLoginBinding.otpView.getText().toString();
    }

    @Override
    public void reseneOtpClick() {
        mPresenter.onLiginApiCall();
        activityLoginBinding.setResendView(0);
        activityLoginBinding.setTimeView(1);
        backCountTimer();
    }

    @Override
    public void navigateToSurveyListActivity() {
        Intent intent = new Intent(this, MainActiivty.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void backCountTimer() {
        new CountDownTimer(120000, 1000) {

            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                activityLoginBinding.resendtime.setText("" + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            @Override
            public void onFinish() {
                activityLoginBinding.setResendView(1);
                activityLoginBinding.setTimeView(0);
            }
        }.start();
    }

    @Override
    public void onCrossClick() {
        activityLoginBinding.setOtpView(0);
        activityLoginBinding.setLogonView(1);
//        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public void onVerifyClick() {
        mPresenter.onOtpApiCall();
    }

    @Override
    public String getPhoneNumber() {
        return activityLoginBinding.phoneNo.getText().toString();
    }

    private boolean validation() {
        String mobile = activityLoginBinding.phoneNo.getText().toString().trim();
        if (mobile.isEmpty()) {
            activityLoginBinding.phoneNo.setError("Mobile number should not be empty");
            activityLoginBinding.phoneNo.requestFocus();
            return false;
        } else if (mobile.length() < 10) {
            activityLoginBinding.phoneNo.setError("Enter min 10 characters");
            activityLoginBinding.phoneNo.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onOtpCompleted(String otp) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public void anotherizedToken() {

    }
}