package com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag;

import com.thresholdsoft.wakfboard.ui.base.MvpView;

public interface SurveyListMvpView extends MvpView {

    void onItemClickTakeSurveyLister(int position);

    void onClickPropertyCreation();
}
