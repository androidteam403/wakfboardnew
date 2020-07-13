package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import androidx.lifecycle.LiveData;

import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.data.db.model.SurveyEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyDetailsEntity;
import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyListModel;

import java.util.ArrayList;
import java.util.List;

public interface SurveyStatusMvpPresenter<V extends SurveyStatusMvpView> extends MvpPresenter<V> {

    LiveData<FarmerLands> getFarmerLand(String uid, String landUid);

    void startSurvey(FarmerLands rowsEntity);

    void addSurvey(FarmerLands rowsEntity);

    void submitSurvey(FarmerLands rowsEntity);

    void deleteApiCall(SurveyEntity surveyEntity);

    void editApiCal(SurveyEntity surveyEntity);

    LiveData<List<SurveyEntity>> getAllSurveyList(String landUid);

    void updateFarmerLandStatus(String uid, String landUid,String surveyLandUid);

    void updateLandSurveySubmit(String uid, String landUid);

    void updateSurveyCheck(SurveyEntity surveyEntity);
}
