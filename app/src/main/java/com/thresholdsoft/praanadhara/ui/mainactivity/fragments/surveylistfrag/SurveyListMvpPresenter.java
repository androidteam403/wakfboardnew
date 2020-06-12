package com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag;

import androidx.lifecycle.LiveData;

import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.data.db.model.SurveyStatusEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyListModel;

import java.util.List;

public interface SurveyListMvpPresenter<V extends SurveyListMvpView> extends MvpPresenter<V> {
    void onItemClick(FarmerLands farmerModel);

    void farmersListApiCall();

    void anotherizedTokenClearDate();

    void onClickNew();

    void onClickInProgress();

    void onClickCompleted();

    void pullToRefreshApiCall();

    void loadMoreApiCall();

    void onStatusCountApiCall();

    LiveData<List<FarmerLands>> getAllFarmersLands();

    LiveData<SurveyStatusEntity> getSurveyStatusCount();
}
