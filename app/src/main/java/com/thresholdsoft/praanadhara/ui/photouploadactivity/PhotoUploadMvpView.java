package com.thresholdsoft.praanadhara.ui.photouploadactivity;

import com.thresholdsoft.praanadhara.ui.base.MvpView;

public interface PhotoUploadMvpView extends MvpView {

    void photoUploadButton();

    void onRemovePhoto(int position);

    void onBackClickData();
}
