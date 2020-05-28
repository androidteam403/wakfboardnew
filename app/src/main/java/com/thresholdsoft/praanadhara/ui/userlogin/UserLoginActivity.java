package com.thresholdsoft.praanadhara.ui.userlogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.ActivityUserLoginBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.SurveyListActivity;

import javax.inject.Inject;

public class UserLoginActivity extends BaseActivity implements UserLoginMvpView {
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
    }

    @Override
    public void onLoginClick() {
        if (validation()) {
            activityLoginBinding.loginDetails.setVisibility(View.GONE);
            activityLoginBinding.otpDetails.setVisibility(View.VISIBLE);
            activityLoginBinding.number.setText(activityLoginBinding.phoneNo.getText().toString());
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    @Override
    public void onCrossClick() {
        activityLoginBinding.otpDetails.setVisibility(View.GONE);
        activityLoginBinding.loginDetails.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public void onVerifyClick() {
        Toast.makeText(this, "verified", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SurveyListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
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
}