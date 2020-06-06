package com.thresholdsoft.praanadhara.ui.surveytrack;

import com.thresholdsoft.praanadhara.ui.base.MvpView;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.model.SurveyDetailsModel;

public interface SurveyTrackMvpView extends MvpView {

    String getSurveyId();

    int getSurveyType();

    void onClickPauseResumeBtn();

    void onClickStopBtn();

    void onClickPointBtn();

    void onClickSavePoints();

    void surveySaveSuccess();

    void onPassSurveyTrackEnteredDetails(SurveyDetailsModel surveyDetailsModel);

    void onClickStartPolygon();

    void savePolyGone();
}
