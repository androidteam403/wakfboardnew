package com.thresholdsoft.wakfboard.ui.propertysurveystatus;

import android.annotation.SuppressLint;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.thresholdsoft.wakfboard.data.DataManager;
import com.thresholdsoft.wakfboard.ui.base.BasePresenter;
import com.thresholdsoft.wakfboard.utils.rx.SchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class PropertySurveyStatusPresenter<V extends PropertySurveyStatusMvpView> extends BasePresenter<V>
        implements PropertySurveyStatusMvpPresenter<V> {

    @Inject
    public PropertySurveyStatusPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }


    @Override
    public void getMapTypelist(int propertyId) {
        getMvpView().getMapDataTable(getDataManager().getAllMapDtaListByPropertyId(propertyId));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getPolygonArea(List<LatLng> polygonPoints) {
        double area = 0;
        if (polygonPoints.size() > 0) {
            area = SphericalUtil.computeArea(polygonPoints);
            area = area * 0.3048 * 0.3048;
        }
        return String.format("%.2f", area);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getLineLength(LatLng fromPolyLineLatLng, LatLng toPolyLineLatLng) {
        LatLng from = new LatLng(((fromPolyLineLatLng.latitude)), ((fromPolyLineLatLng.longitude)));
        LatLng to = new LatLng(((toPolyLineLatLng.latitude)), ((toPolyLineLatLng.longitude)));
        double length = SphericalUtil.computeDistanceBetween(from, to);
        return String.format("%.2f", length);
    }
}
