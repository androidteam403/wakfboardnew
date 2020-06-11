package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyDetailsEntity;
import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;
import com.thresholdsoft.praanadhara.ui.surveytrack.model.SurveyModel;

import java.util.ArrayList;
import java.util.List;

public interface SurveyStatusMvpPresenter<V extends SurveyStatusMvpView> extends MvpPresenter<V> {


    void onpolygonRadioClick();

    void onLinesRadioClick();

    void onPointsRadioClick();

    void startSurvey(RowsEntity rowsEntity);

    void addSurvey(RowsEntity rowsEntity);

    void submitSurvey(RowsEntity rowsEntity);

    void deleteApiCall(SurveyDetailsEntity farmerModel,int position);

    void editApiCal(SurveyDetailsEntity surveyDetailsEntity , int position);

    ArrayList<SurveyDetailsEntity> getAllSurveyList(String landUid);
}
