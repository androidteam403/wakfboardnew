package com.thresholdsoft.wakfboard.ui.propertysurvey;

import android.annotation.SuppressLint;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.thresholdsoft.wakfboard.data.DataManager;
import com.thresholdsoft.wakfboard.ui.base.BasePresenter;
import com.thresholdsoft.wakfboard.ui.propertysurvey.model.MapDataTable;
import com.thresholdsoft.wakfboard.utils.rx.SchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class PropertySurveyPresenter<V extends PropertySurveyMvpView> extends BasePresenter<V>
        implements PropertySurveyMvpPresenter<V> {

    @Inject
    public PropertySurveyPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void polygonStartClick() {
        getMvpView().polygonStartClick();
    }

    @Override
    public void polygonSaveClick() {
        getMvpView().polygonSaveClick();
    }

    @Override
    public void polygonPauseClick() {
        getMvpView().polygonPauseClick();
    }

    @Override
    public void polygonResumeClick() {
        getMvpView().polygonResumeClick();
    }

    @Override
    public void polygonStopClick() {
        getMvpView().polygonStopClick();
    }

    @Override
    public void polylineUndoClick() {
        getMvpView().polylineUndoClick();
    }

    @Override
    public void polylineClearClick() {
        getMvpView().polylineClearClick();
    }

    @Override
    public void polylineSaveClick() {
        getMvpView().polylineSaveClick();
    }

    @Override
    public void pointSaveClick() {
        getMvpView().pointSaveClick();
    }

    @Override
    public void polygonManualClear() {
        getMvpView().polygonManualClear();
    }

    @Override
    public void polygonManualUndo() {
        getMvpView().polygonManualUndo();
    }

    @Override
    public void polygonManualSave() {
        getMvpView().polygonManualSave();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getPolygonAreainMeters(List<LatLng> polygonPoints) {
        double area = 0;
        if (polygonPoints.size() > 0) {
            area = SphericalUtil.computeArea(polygonPoints);
            area = area * 0.3048 * 0.3048;
        }
        return String.format("%.2f", area);
    }

    @Override
    public String getPolygonAreainSquareFeet(List<LatLng> polygonPoints) {
        double area = 0;
        if (polygonPoints.size() > 0) {
            area = SphericalUtil.computeArea(polygonPoints);
            area = area * 1;
        }
        return String.format("%.2f", area);
    }

    @Override
    public String getPolygonAreainSquareYards(List<LatLng> polygonPoints) {
        double area = 0;
        if (polygonPoints.size() > 0) {
            area = SphericalUtil.computeArea(polygonPoints);
            area = area * 0.111111 * 0.111111;
        }
        return String.format("%.2f", area);
    }

    @Override
    public String getPolygonAreainAcers(List<LatLng> polygonPoints) {
        double area = 0;
        if (polygonPoints.size() > 0) {
            area = SphericalUtil.computeArea(polygonPoints);
            area = area * 0.0000229568;
        }
        return String.format("%.2f", area);
    }

    @Override
    public String getMapViewType() {
        return getDataManager().getMapViewType();
    }

    @Override
    public void insertMapTypeDataTable(MapDataTable mapDataTable) {
        getDataManager().insertMapData(mapDataTable);
    }

    @Override
    public void updateMapDataList(MapDataTable mapDataTable) {
        getDataManager().updateMapEditData(mapDataTable);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getLineLength(LatLng fromPolyLineLatLng, LatLng toPolyLineLatLng) {
        LatLng from = new LatLng(((fromPolyLineLatLng.latitude)), ((fromPolyLineLatLng.longitude)));
        LatLng to = new LatLng(((toPolyLineLatLng.latitude)), ((toPolyLineLatLng.longitude)));
        double length = SphericalUtil.computeDistanceBetween(from, to);
        return String.format("%.2f", length);
    }

    @Override
    public List<MapDataTable> getMapTypelist(int propertyId) {
        return getDataManager().getAllMapDtaListByPropertyId(propertyId);
    }


}
