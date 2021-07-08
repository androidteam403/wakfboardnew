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
    public String getPolygonArea(List<LatLng> polygonPoints) {
        double area = 0;
        if (polygonPoints.size() > 0) {
            area = SphericalUtil.computeArea(polygonPoints);
            area = area * 0.3048 * 0.3048;
        }
        return String.format("%.2f", area) + "m²";
    }

    @Override
    public void insertMapTypeDataTable(MapDataTable mapDataTable) {
        getDataManager().insertMapData(mapDataTable);
    }
}
