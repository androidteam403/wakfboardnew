package com.thresholdsoft.wakfboard.ui.mainactivity;

import com.thresholdsoft.wakfboard.ui.base.MvpPresenter;

public interface MainActivityMvpPresenter<V extends MainActivityMvpView> extends MvpPresenter<V> {

    String getUserName();

    void syncData();

    void clearSharedPreference();
}
