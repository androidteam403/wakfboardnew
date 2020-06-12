package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyDetailsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartRes;
import com.thresholdsoft.praanadhara.ui.base.MvpView;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyListModel;

import java.util.ArrayList;

public interface SurveyStatusMvpView extends MvpView {

    void startSurvey(SurveyListModel surveyModel);

    void startSurveySuccess(SurveyListModel rowsEntity, SurveyStartRes data);

    void onpolygonRadioClick();

    void onLinesRadioClick();

    void onPointsRadioClick();

    void addSurvey(SurveyListModel rowsEntity);

    void submitSurvey(SurveyListModel rowsEntity);

    void surveySubmitSuccess(SurveyStartRes data);

    void onListItemClicked(int position);

    void deleteAnItem(int pos);

    void deleteApiCall();

    void onDeleteApiSuccess(int position);

    ArrayList<RowsEntity> getUidDetails();

    SurveyListModel getSurvey();

    void onSuccessEditSurvey(String description,int postion);


}
