package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;

public interface SurveyStatusMvpPresenter<V extends SurveyStatusMvpView> extends MvpPresenter<V> {

    void startSurvey(RowsEntity surveyModel);

    void onpolygonRadioClick();

    void onLinesRadioClick();

    void onPointsRadioClick();
}
