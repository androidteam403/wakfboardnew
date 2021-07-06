package com.thresholdsoft.praanadhara.ui.propertysurvey;

import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;
import com.thresholdsoft.praanadhara.ui.propertysurvey.model.PointDataTable;

public interface PropertySurveyMvpPresenter<V extends PropertySurveyMvpView> extends MvpPresenter<V> {
    void insertPointDataTable(PointDataTable pointDataTable);

    void polygonStartClick();

    void polygonSaveClick();

    void polygonPauseClick();

    void polygonResumeClick();

    void polygonStopClick();

    void polylineUndoClick();

    void polylineClearClick();

    void polylineSaveClick();

    void pointSaveClick();

}
