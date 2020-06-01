package com.thresholdsoft.praanadhara.ui.userlogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
        activityLoginBinding.otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (activityLoginBinding.otp1.length() == 1) {
                    activityLoginBinding.otp2.requestFocus();
                }
            }
        });
        activityLoginBinding.otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (activityLoginBinding.otp2.length() == 1) {
                    activityLoginBinding.otp3.requestFocus();
                }
                if (activityLoginBinding.otp2.length()==0){
                    activityLoginBinding.otp1.requestFocus();
                }

            }
        });
        activityLoginBinding.otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (activityLoginBinding.otp3.length() == 1) {
                    activityLoginBinding.otp4.requestFocus();
                }
                if (activityLoginBinding.otp3.length()==0){
                    activityLoginBinding.otp2.requestFocus();
                }
            }
        });
        activityLoginBinding.otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (activityLoginBinding.otp4.length() == 1) {
                    activityLoginBinding.otp5.requestFocus();
                }
                if (activityLoginBinding.otp4.length()==0){
                    activityLoginBinding.otp3.requestFocus();
                }
            }
        });
        activityLoginBinding.otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (activityLoginBinding.otp5.length() == 1) {
                    activityLoginBinding.otp6.requestFocus();
                }
                if (activityLoginBinding.otp5.length()==0) {
                    activityLoginBinding.otp4.requestFocus();
                }
            }
        });
        activityLoginBinding.otp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (activityLoginBinding.otp6.length() == 1) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                if (activityLoginBinding.otp6.length()==0){
                    activityLoginBinding.otp5.requestFocus();
                }
            }
        });
    }

    @Override
    public void onLoginClick() {
        if (validation()) {
            mPresenter.onLiginApiCall();
        }
    }

    @Override
    public void onSucessfullLogin() {
        activityLoginBinding.loginDetails.setVisibility(View.GONE);
        activityLoginBinding.otpDetails.setVisibility(View.VISIBLE);
        activityLoginBinding.number.setText(activityLoginBinding.phoneNo.getText().toString());
    }

    @Override
    public String getOtp() {
        return activityLoginBinding.otp1.getText().toString() + activityLoginBinding.otp2.getText().toString() +
                activityLoginBinding.otp3.getText().toString() + activityLoginBinding.otp4.getText().toString()
                + activityLoginBinding.otp5.getText().toString() + activityLoginBinding.otp6.getText().toString();
    }

    @Override
    public void reseneOtpClick() {
        mPresenter.onLiginApiCall();
    }

    @Override
    public void navigateToSurveyListActivity() {
        Intent intent = new Intent(this, SurveyListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onCrossClick() {
        activityLoginBinding.otpDetails.setVisibility(View.GONE);
        activityLoginBinding.loginDetails.setVisibility(View.VISIBLE);
//        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public void onVerifyClick() {
        if (otpValidations()) {
            mPresenter.onOtpApiCall();
        }
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

    private boolean otpValidations() {
        String o1 = activityLoginBinding.otp1.getText().toString();
        String o2 = activityLoginBinding.otp2.getText().toString();
        String o3 = activityLoginBinding.otp3.getText().toString();
        String o4 = activityLoginBinding.otp4.getText().toString();
        String o5 = activityLoginBinding.otp5.getText().toString();
        String o6 = activityLoginBinding.otp6.getText().toString();

        if (o1.isEmpty()) {
            Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
            activityLoginBinding.otp1.requestFocus();
            return false;
        } else if (o2.isEmpty()) {
            Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
            activityLoginBinding.otp2.requestFocus();
            return false;
        } else if (o3.isEmpty()) {
            Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
            activityLoginBinding.otp3.requestFocus();
            return false;
        } else if (o4.isEmpty()) {
            Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
            activityLoginBinding.otp4.requestFocus();
            return false;
        } else if (o5.isEmpty()) {
            Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
            activityLoginBinding.otp5.requestFocus();
            return false;
        } else if (o6.isEmpty()) {
            Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
            activityLoginBinding.otp6.requestFocus();
            return false;
        }
        return true;
    }
}