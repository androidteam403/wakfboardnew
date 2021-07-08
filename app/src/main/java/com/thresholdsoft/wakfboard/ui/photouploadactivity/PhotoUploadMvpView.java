package com.thresholdsoft.wakfboard.ui.photouploadactivity;

import com.thresholdsoft.wakfboard.ui.base.MvpView;

public interface PhotoUploadMvpView extends MvpView {

    void photoUploadButton();

    void onRemovePhoto(int position);

    void onBackClickData();
}
