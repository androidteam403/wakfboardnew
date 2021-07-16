package com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag;

import com.thresholdsoft.wakfboard.ui.base.MvpPresenter;
import com.thresholdsoft.wakfboard.ui.propertycreation.model.PropertyData;

import java.util.List;

public interface SurveyListMvpPresenter<V extends SurveyListMvpView> extends MvpPresenter<V> {
    List<PropertyData> getPropertylist();

    void onClickPropertyCreation();
}
