package com.thresholdsoft.wakfboard.ui.photouploadactivity;

import com.thresholdsoft.wakfboard.ui.base.MvpPresenter;

public interface PhotoUploadMvpPresenter<V extends PhotoUploadMvpView> extends MvpPresenter<V> {

    void photoUploadbutton();

    void onBackClickData();

}
