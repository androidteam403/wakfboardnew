package com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag;

import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.ui.base.MvpView;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyListModel;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyStatusCountModelResponse;

import java.util.ArrayList;
import java.util.List;

public interface SurveyListMvpView extends MvpView {

    void statusBaseListFilter(String status);

    void onItemClick(FarmerLands farmerModel);

    void onSuccessPullToRefresh();

    void onSuccessLoadMore();

    void onSuccessLoadMoreNodData();

    void displayNoData();

    void onClickNew();

    void onClickInProgress();

    void onClickCompleted();
}
