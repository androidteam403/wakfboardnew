package com.thresholdsoft.wakfboard.ui.gallery;

import com.thresholdsoft.wakfboard.ui.base.MvpView;

public interface GalleryMvpView extends MvpView {
    void onClickBack();

    void onImageClick(int position);

    void onGalleryDeleteClick(int position);
}
