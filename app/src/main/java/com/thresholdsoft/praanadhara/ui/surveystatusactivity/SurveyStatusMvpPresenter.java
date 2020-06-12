package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import androidx.lifecycle.LiveData;

import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyDetailsEntity;
import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyListModel;
import com.thresholdsoft.praanadhara.ui.surveytrack.model.SurveyModel;

import java.util.ArrayList;
import java.util.List;

public interface SurveyStatusMvpPresenter<V extends SurveyStatusMvpView> extends MvpPresenter<V> {

    FarmerLands getFarmerLand(String uid, String landUid);

    void onpolygonRadioClick();

    void onLinesRadioClick();

    void onPointsRadioClick();

    void startSurvey(SurveyListModel rowsEntity);

    void addSurvey(SurveyListModel rowsEntity);

    void submitSurvey(SurveyListModel rowsEntity);

    void deleteApiCall(SurveyDetailsEntity farmerModel,int position);

    void editApiCal(SurveyDetailsEntity surveyDetailsEntity , int position);

    ArrayList<SurveyDetailsEntity> getAllSurveyList(String landUid);

    void updateFarmerLandStatus(String uid, String landUid);
}
