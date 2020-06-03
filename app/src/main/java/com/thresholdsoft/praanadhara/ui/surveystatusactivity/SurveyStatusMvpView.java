package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.ui.base.MvpView;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.FarmersResponse;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.SurveyModel;

public interface SurveyStatusMvpView extends MvpView {

    void startSurvey(RowsEntity surveyModel);
}
