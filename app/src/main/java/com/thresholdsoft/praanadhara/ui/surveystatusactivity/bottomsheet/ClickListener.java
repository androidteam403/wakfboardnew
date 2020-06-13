package com.thresholdsoft.praanadhara.ui.surveystatusactivity.bottomsheet;

import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;

public interface ClickListener {
    void onpolygonRadioClick();

    void onLinesRadioClick();

    void onPointsRadioClick();

    void addSurvey(RowsEntity rowsEntity);

    void submitSurvey(RowsEntity rowsEntity);

    void startSurvey(RowsEntity surveyModel);

}
