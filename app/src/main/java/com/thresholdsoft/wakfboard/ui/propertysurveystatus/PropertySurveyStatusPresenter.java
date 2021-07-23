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

    @SuppressLint("DefaultLocale")
    @Override
    public String getLineLength(LatLng fromPolyLineLatLng, LatLng toPolyLineLatLng) {
        LatLng from = new LatLng(((fromPolyLineLatLng.latitude)), ((fromPolyLineLatLng.longitude)));
        LatLng to = new LatLng(((toPolyLineLatLng.latitude)), ((toPolyLineLatLng.longitude)));
        double length = SphericalUtil.computeDistanceBetween(from, to);
        return String.format("%.2f", length);
    }

    @Override
    public void onClickBack() {
        getMvpView().onClickBack();
    }

    @Override
    public void onClickGallery() {
        getMvpView().onClickGallery();
    }

    @Override
    public void onClickPropertyEdit() {
        getMvpView().onClickPropertyEdit();
    }

    @Override
    public void updateAreaByPropertyId(int id, String area) {
        getDataManager().updateAreaByPropertyId(id, area);
    }

    @Override
    public void onClickMapIcon() {
        getMvpView().onClickMapIcon();
    }

    @Override
    public void saveMapViewType(String mapViewType) {
        getDataManager().setMapViewType(mapViewType);
    }

    @Override
    public String getMapViewType() {
        return getDataManager().getMapViewType();
    }
}
