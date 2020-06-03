package com.thresholdsoft.praanadhara.ui.surveylistactivity;

import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.ui.base.MvpView;

import java.util.List;

public interface SurveyListMvpView extends MvpView {
    void onItemClick(RowsEntity farmerModel);

    void onFarmersRes(List<RowsEntity> rowsEntity);
    void arrayListClear();
}
