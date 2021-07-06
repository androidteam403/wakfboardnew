package com.thresholdsoft.praanadhara.ui.propertysurveystatus;

import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;
import com.thresholdsoft.praanadhara.ui.propertysurvey.model.PolylineDataTable;

import java.util.List;

public interface PropertySurveyStatusMvpPresenter<V extends PropertySurveyStatusMvpView> extends MvpPresenter<V> {
    List<PolylineDataTable> getPolylinelist(String name);
}
