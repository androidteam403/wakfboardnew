package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyDetailsEntity;
import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;

public interface SurveyStatusMvpPresenter<V extends SurveyStatusMvpView> extends MvpPresenter<V> {


    void onpolygonRadioClick();

    void onLinesRadioClick();

    void onPointsRadioClick();

    void startSurvey(RowsEntity rowsEntity);

    void addSurvey(RowsEntity rowsEntity);

    void submitSurvey(RowsEntity rowsEntity);

    void deleteApiCall(SurveyDetailsEntity farmerModel, int position);

    void editApiCal(SurveyDetailsEntity surveyDetailsEntity, int position);

    void onItemClick(int position);
}
