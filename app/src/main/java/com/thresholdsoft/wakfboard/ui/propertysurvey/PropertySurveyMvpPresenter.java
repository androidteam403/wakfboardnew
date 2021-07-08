package com.thresholdsoft.wakfboard.ui.propertysurvey;

import com.google.android.gms.maps.model.LatLng;
import com.thresholdsoft.wakfboard.ui.base.MvpPresenter;
import com.thresholdsoft.wakfboard.ui.propertysurvey.model.MapDataTable;

import java.util.List;

public interface PropertySurveyMvpPresenter<V extends PropertySurveyMvpView> extends MvpPresenter<V> {

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

    void insertMapTypeDataTable(MapDataTable mapDataTable);
}
