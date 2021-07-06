package com.thresholdsoft.praanadhara.ui.photouploadactivity;

import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;

public interface PhotoUploadMvpPresenter<V extends PhotoUploadMvpView> extends MvpPresenter<V> {

    void photoUploadbutton();

    void onBackClickData();

}
