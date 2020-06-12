package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyDetailsEntity;
import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyListModel;

import java.util.ArrayList;

public interface SurveyStatusMvpPresenter<V extends SurveyStatusMvpView> extends MvpPresenter<V> {

    FarmerLands getFarmerLand(String uid, String landUid);

    void onpolygonRadioClick();

    void onLinesRadioClick();

    void onPointsRadioClick();

    void startSurvey(SurveyListModel rowsEntity);

    void addSurvey(SurveyListModel rowsEntity);

    void submitSurvey(SurveyListModel rowsEntity);

    void deleteApiCall(SurveyDetailsEntity farmerModel, int position);

    void onItemClick(int position);

    void editApiCal(SurveyDetailsEntity surveyDetailsEntity, int position);

    ArrayList<SurveyDetailsEntity> getAllSurveyList(String landUid);

    void updateFarmerLandStatus(String uid, String landUid);
}
