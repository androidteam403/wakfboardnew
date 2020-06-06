package com.thresholdsoft.praanadhara.ui.surveytrack;

import android.location.Location;

import com.thresholdsoft.praanadhara.data.network.pojo.SurveySaveReq;
import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;

import java.util.ArrayList;

public interface SurveyTrackMvpPresenter<V extends SurveyTrackMvpView> extends MvpPresenter<V> {

    void storeSurveyDetails(Location location, boolean isPoint);

    String getTravelledDistance(ArrayList<Location> locations);

    void saveSurvey(SurveySaveReq surveySaveReq);

    void submitSurvey(SurveySaveReq.SurveyEntity landLocationEntity);
}
