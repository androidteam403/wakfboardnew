package com.thresholdsoft.praanadhara.ui.login;

import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;

public interface LoginMvpPresenter<V extends LoginMvpView> extends MvpPresenter<V> {
    void onLoginClick();
}
