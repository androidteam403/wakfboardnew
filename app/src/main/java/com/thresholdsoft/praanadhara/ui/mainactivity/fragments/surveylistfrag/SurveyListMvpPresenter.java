package com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag;

import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyListModel;

public interface SurveyListMvpPresenter<V extends SurveyListMvpView> extends MvpPresenter<V> {
    void onItemClick(SurveyListModel farmerModel);

    void farmersListApiCall();

    void anotherizedTokenClearDate();

    void onClickNew();

    void onClickInProgress();

    void onClickCompleted();

    void pullToRefreshApiCall();

    void loadMoreApiCall();

    void onStatusCountApiCall();
}
