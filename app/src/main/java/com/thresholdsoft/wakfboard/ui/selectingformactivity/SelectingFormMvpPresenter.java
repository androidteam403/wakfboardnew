package com.thresholdsoft.wakfboard.ui.selectingformactivity;

import com.thresholdsoft.wakfboard.ui.base.MvpPresenter;

public interface SelectingFormMvpPresenter<V extends SelectingFormMvpView> extends MvpPresenter<V> {

    void onEnrollmentClick();

    void onSurveyClick();



}
