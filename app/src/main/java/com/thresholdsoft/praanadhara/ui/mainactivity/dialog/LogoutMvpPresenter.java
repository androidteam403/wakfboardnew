package com.thresholdsoft.praanadhara.ui.mainactivity.dialog;

import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;

public interface LogoutMvpPresenter<V extends LogoutMvpView> extends MvpPresenter<V> {
    void onNoClick();

    void onYesClick();

    void clearSharedPreference();
}
