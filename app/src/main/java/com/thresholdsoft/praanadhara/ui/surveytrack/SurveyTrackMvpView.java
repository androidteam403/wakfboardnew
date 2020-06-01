package com.thresholdsoft.praanadhara.ui.surveytrack;

import com.thresholdsoft.praanadhara.ui.base.MvpView;

public interface SurveyTrackMvpView extends MvpView {

    String getSurveyId();

    int getSurveyType();

    void onClickPauseResumeBtn();

    void onClickStopBtn();

    void onClickPointBtn();
}
