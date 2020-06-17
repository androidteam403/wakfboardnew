package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.data.db.model.SurveyEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyDetailsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartRes;
import com.thresholdsoft.praanadhara.ui.base.MvpView;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyListModel;

import java.util.ArrayList;

public interface SurveyStatusMvpView extends MvpView {

    void startSurveySuccess();

    void onpolygonRadioClick();

    void onLinesRadioClick();

    void onPointsRadioClick();

    void addSurvey(FarmerLands rowsEntity);

    void submitSurvey(FarmerLands rowsEntity);

    void surveySubmitSuccess();

    void onListItemClicked(SurveyEntity surveyEntity);

    SurveyListModel getSurvey();

    void onClickEditSurvey(SurveyEntity surveyEntity);

    void onClickDeleteSurvey(SurveyEntity surveyEntity);

}
