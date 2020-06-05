package com.thresholdsoft.praanadhara.ui.surveylistactivity;

import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;

public interface SurveyListMvpPresenter<V extends SurveyListMvpView> extends MvpPresenter<V> {
    void onItemClick(RowsEntity farmerModel);
    void farmersListApiCall();

    void anotherizedTokenClearDate();
}
