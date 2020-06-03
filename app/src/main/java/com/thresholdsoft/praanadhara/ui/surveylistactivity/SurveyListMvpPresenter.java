package com.thresholdsoft.praanadhara.ui.surveylistactivity;

import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.FarmersResponse;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.SurveyModel;

public interface SurveyListMvpPresenter<V extends SurveyListMvpView> extends MvpPresenter<V> {
    void onItemClick(RowsEntity farmerModel);
    void farmersListApiCall();

}
