package com.thresholdsoft.praanadhara.ui.mainactivity;

import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;

public interface MainActivityMvpPresenter<V extends MainActivityMvpView> extends MvpPresenter<V> {

    String getUserName();

    void syncData();
}
