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

    void polygonWalkClear();

    void polygonWalkStart();

    void polygonWalkStop();

    void polygonWalkSave();

    void insertMapTypeDataTable(MapDataTable mapDataTable);

    void updateMapDataList(MapDataTable mapDataTable);

    String getLineLength(LatLng fromPolyLineLatLng, LatLng toPolyLineLatLng);

    List<MapDataTable> getMapTypelist(int propertyId);

    String getPolygonAreainMeters(List<LatLng> polygonPoints);

    String getPolygonAreainSquareFeet(List<LatLng> polygonPoints);

    String getPolygonAreainSquareYards(List<LatLng> polygonPoints);

    String getPolygonAreainAcers(List<LatLng> polygonPoints);

    String getMapViewType();
}
