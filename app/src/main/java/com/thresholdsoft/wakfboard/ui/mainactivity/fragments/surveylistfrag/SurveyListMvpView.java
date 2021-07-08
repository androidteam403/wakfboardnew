package com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag;

import com.thresholdsoft.wakfboard.data.db.model.FarmerLands;
import com.thresholdsoft.wakfboard.ui.base.MvpView;

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

    void onPropertyClick();

}
