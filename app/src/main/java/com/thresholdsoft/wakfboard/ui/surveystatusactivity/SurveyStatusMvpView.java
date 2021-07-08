package com.thresholdsoft.wakfboard.ui.surveystatusactivity;

import android.content.Context;

import com.thresholdsoft.wakfboard.data.db.model.FarmerLands;
import com.thresholdsoft.wakfboard.data.db.model.SurveyEntity;
import com.thresholdsoft.wakfboard.ui.base.MvpView;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.model.SurveyListModel;

public interface SurveyStatusMvpView extends MvpView {


    Context getContext();

    void startSurveySuccess(String uid);

    void addSurvey(FarmerLands rowsEntity);

    void submitSurvey(FarmerLands rowsEntity);

    void surveySubmitSuccess();

    void onListItemClicked(SurveyEntity surveyEntity);

    SurveyListModel getSurvey();

    void onClickEditSurvey(SurveyEntity surveyEntity);

    void onClickPolygonMapEditSurvey(SurveyEntity surveyEntity,int position);

    void onClickDeleteSurvey(SurveyEntity surveyEntity);

    void itemDeletedToast();

    void itemUpdatedToast();

}
