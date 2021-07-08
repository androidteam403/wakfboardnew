package com.thresholdsoft.wakfboard.ui.mainactivity.fragments.newenrollmentfrag;

import com.thresholdsoft.wakfboard.ui.base.MvpPresenter;

public interface NewEnrollmentFragMvpPresenter <V extends NewEnrollmentFragMvpView> extends MvpPresenter<V> {

    String getAccessToken();
}
