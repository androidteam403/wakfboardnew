package com.thresholdsoft.praanadhara.ui.surveytrack;

import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.ui.base.MvpView;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.model.SurveyDetailsModel;

public interface SurveyTrackMvpView extends MvpView {

    FarmerLands getFarmerLand();

    String getSurveyId();

    int getSurveyType();

    void onClickPauseResumeBtn();

    void onClickStopBtn();

    void onClickSavePoints();

    void surveySaveSuccess();

    void onPassSurveyTrackEnteredDetails(SurveyDetailsModel surveyDetailsModel);

    void onClickStartPolygon();

    void savePolyGone();
}
