package com.thresholdsoft.praanadhara.ui.propertysurvey;

import com.google.android.gms.maps.model.LatLng;
import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;
import com.thresholdsoft.praanadhara.ui.propertysurvey.model.PointDataTable;

import java.util.List;

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

    void polygonManualClear();

    void polygonManualUndo();

    void polygonManualSave();

    String getPolygonArea(List<LatLng> polygonPoints);
}
