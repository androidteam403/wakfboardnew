package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import com.thresholdsoft.praanadhara.ui.base.MvpView;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.SurveyModel;

public interface SurveyStatusMvpView extends MvpView {

    void startSurvey(SurveyModel surveyModel);
}