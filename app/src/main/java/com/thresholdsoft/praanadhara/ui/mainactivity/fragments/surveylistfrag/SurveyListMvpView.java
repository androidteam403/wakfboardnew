package com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag;

import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.ui.base.MvpView;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyListModel;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyStatusCountModelResponse;

import java.util.ArrayList;
import java.util.List;

public interface SurveyListMvpView extends MvpView {
    void onItemClick(SurveyListModel farmerModel);

    void onFarmersRes(List<SurveyListModel> rowsEntity);

    void onSuccessLoadMore(List<RowsEntity> rowsEntities);

    void onSuccessLoadMoreNodData();

    void displayNoData();

    void arrayListClear();

    void updateFilteredList(ArrayList<RowsEntity> surveyList);

    void onStatuCountApiSucess(SurveyStatusCountModelResponse response);
}
