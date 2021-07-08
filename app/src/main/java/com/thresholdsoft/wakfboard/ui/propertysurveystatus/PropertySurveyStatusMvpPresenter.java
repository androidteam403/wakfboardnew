package com.thresholdsoft.wakfboard.ui.propertysurveystatus;

import com.thresholdsoft.wakfboard.ui.base.MvpPresenter;

public interface PropertySurveyStatusMvpPresenter<V extends PropertySurveyStatusMvpView> extends MvpPresenter<V> {

    void getMapTypelist(int name);
}
