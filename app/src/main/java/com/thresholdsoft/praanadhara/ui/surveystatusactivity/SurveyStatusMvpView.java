package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyDetailsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartRes;
import com.thresholdsoft.praanadhara.ui.base.MvpView;

import java.util.ArrayList;

public interface SurveyStatusMvpView extends MvpView {

    void startSurvey(RowsEntity surveyModel);

    void startSurveySuccess(RowsEntity rowsEntity, SurveyStartRes data);

    void onpolygonRadioClick();

    void onLinesRadioClick();

    void onPointsRadioClick();

    void addSurvey(RowsEntity rowsEntity);

    void submitSurvey(RowsEntity rowsEntity);

    void surveySubmitSuccess(SurveyStartRes data);

    void onListItemClicked(int position);

    void deleteAnItem(int pos);

    void deleteApiCall();

    void onDeleteApiSuccess(int position);

    ArrayList<RowsEntity> getUidDetails();

    RowsEntity getSurvey();

    void onSuccessEditSurvey(String description,int postion);


}
