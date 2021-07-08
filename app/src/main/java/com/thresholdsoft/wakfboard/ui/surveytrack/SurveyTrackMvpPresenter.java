package com.thresholdsoft.wakfboard.ui.surveytrack;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.thresholdsoft.wakfboard.data.network.pojo.SurveySaveReq;
import com.thresholdsoft.wakfboard.ui.base.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

public interface SurveyTrackMvpPresenter<V extends SurveyTrackMvpView> extends MvpPresenter<V> {

    String getTravelledDistance(ArrayList<Location> locations);

    String getLineLength(LatLng fromPolyLineLatLng, LatLng toPolyLineLatLng);

    String getPolygonArea(List<LatLng> polygonPoints);

    void saveSurvey(SurveySaveReq surveySaveReq);

    void submitSurvey(SurveySaveReq.SurveyEntity landLocationEntity);
}
