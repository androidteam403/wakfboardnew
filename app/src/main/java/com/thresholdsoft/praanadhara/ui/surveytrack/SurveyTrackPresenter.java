package com.thresholdsoft.praanadhara.ui.surveytrack;

import android.location.Location;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.data.db.model.Survey;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.SurveyListMvpPresenter;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.SurveyListMvpView;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class SurveyTrackPresenter<V extends SurveyTrackMvpView> extends BasePresenter<V>
        implements SurveyTrackMvpPresenter<V> {

    @Inject
    public SurveyTrackPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void storeSurveyDetails(Location location, boolean isPoint) {
        getDataManager().insertUser(new Survey(getMvpView().getSurveyId(),getMvpView().getSurveyType(),location.getLatitude(),location.getLongitude(),location.getAccuracy(),isPoint, new Date(),new Date()));
    }

    @Override
    public String getTravelledDistance(ArrayList<Location> locations) {
        double distance = 0;
        if(locations.size()>0) {
            //   distance = locations.get(0).distanceTo(locations.get((locations.size()-1)));   // in meters
            distance = locations.get(0).distanceTo(locations.get((locations.size() - 1))) / 1000;   // in km
            //  distance = locations.get(0).distanceTo(locations.get((locations.size()-1)))/1609.344;   // in miles
        }
        return distance + " KM";
    }
}
