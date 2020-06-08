package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import com.thresholdsoft.praanadhara.data.db.model.Survey;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartRes;
import com.thresholdsoft.praanadhara.ui.base.MvpView;
import com.thresholdsoft.praanadhara.ui.surveytrack.model.SurveyModel;

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

}
