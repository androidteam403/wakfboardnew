package com.thresholdsoft.praanadhara.ui.mainactivity.fragments.newenrollmentfrag;

import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;

public interface NewEnrollmentFragMvpPresenter <V extends NewEnrollmentFragMvpView> extends MvpPresenter<V> {

    String getAccessToken();
}
