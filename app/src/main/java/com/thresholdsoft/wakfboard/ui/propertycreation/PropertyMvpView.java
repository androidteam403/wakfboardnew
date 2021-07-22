package com.thresholdsoft.wakfboard.ui.propertycreation;

import com.thresholdsoft.wakfboard.ui.base.MvpView;

public interface PropertyMvpView extends MvpView {

    void onRemovePhoto(int position);

    void imageFullView(int position, String path);

    void onClickBack();
}
