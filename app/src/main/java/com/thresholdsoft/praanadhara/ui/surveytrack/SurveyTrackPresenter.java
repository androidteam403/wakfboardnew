package com.thresholdsoft.praanadhara.ui.surveytrack;

import android.annotation.SuppressLint;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.data.db.model.SurveyEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveySaveReq;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class SurveyTrackPresenter<V extends SurveyTrackMvpView> extends BasePresenter<V>
        implements SurveyTrackMvpPresenter<V> {


    @Inject
    public SurveyTrackPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }


    @SuppressLint("DefaultLocale")
    @Override
    public String getTravelledDistance(ArrayList<Location> locations) {
        double distance = 0;
        if (locations.size() > 0) {
            //   distance = locations.get(0).distanceTo(locations.get((locations.size()-1)));   // in meters
            distance = locations.get(0).distanceTo(locations.get((locations.size() - 1))) / 1000;   // in km
            //  distance = locations.get(0).distanceTo(locations.get((locations.size()-1)))/1609.344;   // in miles
        }
        return String.format("%.2f", distance) + " KM";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getLineLength(LatLng fromPolyLineLatLng, LatLng toPolyLineLatLng) {
        LatLng from = new LatLng(((fromPolyLineLatLng.latitude)), ((fromPolyLineLatLng.longitude)));
        LatLng to = new LatLng(((toPolyLineLatLng.latitude)), ((toPolyLineLatLng.longitude)));
        double length = SphericalUtil.computeDistanceBetween(from, to);
        return String.format("%.2f", length) + "m";
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
    public void saveSurvey(SurveySaveReq surveySaveReq) {
        if (getMvpView().isNetworkConnected()) {
            getMvpView().showLoading();
            getCompositeDisposable().add(getDataManager()
                    .saveSurvey(surveySaveReq)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(blogResponse -> {
                        if (blogResponse != null && blogResponse.getData() != null && blogResponse.getSuccess()) {
                            getDataManager().insetSurveyEntity(new SurveyEntity(getMvpView().getFarmerLand().getFarmerLandUid(), blogResponse.getData().getUid(), getMvpView().getFarmerLand().getSurveyLandUid(), surveySaveReq.getName(), surveySaveReq.getDescription(), surveySaveReq.getLatlongs(), surveySaveReq.getMapType().getUid(), true));
                            getMvpView().surveySaveSuccess();
                        }
                        getMvpView().hideLoading();
                    }, throwable -> {
                        getMvpView().hideLoading();
                        handleApiError(throwable);
                    }));
        } else {
            getDataManager().insetSurveyEntity(new SurveyEntity(getMvpView().getFarmerLand().getFarmerLandUid(), "", getMvpView().getFarmerLand().getSurveyLandUid(), surveySaveReq.getName(), surveySaveReq.getDescription(), surveySaveReq.getLatlongs(), surveySaveReq.getMapType().getUid(), false));
            getMvpView().surveySaveSuccess();
        }
    }

    @Override
    public void submitSurvey(SurveySaveReq.SurveyEntity landLocationEntity) {

    }
}
