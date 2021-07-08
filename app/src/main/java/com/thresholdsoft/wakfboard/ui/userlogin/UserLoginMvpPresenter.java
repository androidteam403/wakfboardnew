package com.thresholdsoft.wakfboard.ui.userlogin;

import com.thresholdsoft.wakfboard.ui.base.MvpPresenter;

public interface UserLoginMvpPresenter<V extends UserLoginMvpView> extends MvpPresenter<V> {
    void onLoginClick();


    void onCrossClick();

    void onVerifyClick();

    void onLiginApiCall();

    void onOtpApiCall();

    void reseneOtpClick();
}
