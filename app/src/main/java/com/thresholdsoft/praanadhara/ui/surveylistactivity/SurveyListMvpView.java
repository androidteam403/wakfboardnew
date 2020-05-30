package com.thresholdsoft.praanadhara.ui.surveylistactivity;

import com.thresholdsoft.praanadhara.ui.base.MvpView;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.SurveyModel;

public interface SurveyListMvpView extends MvpView {
    void onItemClick(SurveyModel surveyModel);
}
