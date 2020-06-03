package com.thresholdsoft.praanadhara.ui.splash;

import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;

public interface SplashMvpPresenter<V extends SplashMvpView> extends MvpPresenter<V> {
    void onLoginApiCall();

    void checkUserLogin();

}
