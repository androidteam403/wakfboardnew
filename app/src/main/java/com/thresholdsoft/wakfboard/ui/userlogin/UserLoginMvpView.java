package com.thresholdsoft.wakfboard.ui.userlogin;

import com.thresholdsoft.wakfboard.ui.base.MvpView;
import com.thresholdsoft.wakfboard.ui.userlogin.model.LoginResponse;
import com.thresholdsoft.wakfboard.ui.userlogin.model.OtpVerifyRes;

public interface UserLoginMvpView extends MvpView {
    void onLoginClick();

    void onCrossClick();

    void onVerifyClick();

    String getPhoneNumber();

    void onSucessfullLogin();

    String getOtp();

    void reseneOtpClick();

    void navigateToSurveyListActivity();

    void backCountTimer();

    void snackBarView();

    void numberDetailsNotFount(LoginResponse response);

    void otpDetailsNotFound(OtpVerifyRes res);
}