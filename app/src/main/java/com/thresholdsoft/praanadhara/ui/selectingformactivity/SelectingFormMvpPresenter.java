package com.thresholdsoft.praanadhara.ui.selectingformactivity;

import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;

public interface SelectingFormMvpPresenter<V extends SelectingFormMvpView> extends MvpPresenter<V> {

    void onEnrollmentClick();

    void onSurveyClick();



}
