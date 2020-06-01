package com.thresholdsoft.praanadhara.ui.userlogin;

import com.thresholdsoft.praanadhara.ui.base.MvpView;

public interface UserLoginMvpView extends MvpView {
    void onLoginClick();

    void onCrossClick();

    void onVerifyClick();

    String getPhoneNumber();

    void onSucessfullLogin();

    String getOtp();

    void reseneOtpClick();

    void navigateToSurveyListActivity();
}