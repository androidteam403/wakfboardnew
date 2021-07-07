package com.thresholdsoft.praanadhara.ui.propertysurveystatus;

import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;

public interface PropertySurveyStatusMvpPresenter<V extends PropertySurveyStatusMvpView> extends MvpPresenter<V> {

    void getMapTypelist(int name);
}
