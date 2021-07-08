package com.thresholdsoft.wakfboard.ui.splash;

import com.thresholdsoft.wakfboard.ui.base.MvpPresenter;

public interface SplashMvpPresenter<V extends SplashMvpView> extends MvpPresenter<V> {
    void onLoginApiCall();

    void checkUserLogin();

}
