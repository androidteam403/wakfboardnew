package com.thresholdsoft.wakfboard.ui.mainactivity.dialog;

import com.thresholdsoft.wakfboard.ui.base.MvpPresenter;

public interface LogoutMvpPresenter<V extends LogoutMvpView> extends MvpPresenter<V> {
    void onNoClick();

    void onYesClick();

    void clearSharedPreference();

    void clearLocalDb();
}
