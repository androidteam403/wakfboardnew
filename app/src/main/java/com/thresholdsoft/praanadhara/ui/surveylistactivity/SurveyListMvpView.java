package com.thresholdsoft.praanadhara.ui.surveylistactivity;

import com.thresholdsoft.praanadhara.ui.base.MvpView;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.FarmersResponse;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.SurveyModel;

public interface SurveyListMvpView extends MvpView {
    void onItemClick(FarmersResponse.Data.ListData.Rows surveyModel);

    void onFarmersRes(FarmersResponse farmersResponse);
    void arrayListClear();
}
