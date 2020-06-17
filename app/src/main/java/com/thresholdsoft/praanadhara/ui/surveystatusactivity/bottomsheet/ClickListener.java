package com.thresholdsoft.praanadhara.ui.surveystatusactivity.bottomsheet;

import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;

public interface ClickListener {
    void onpolygonRadioClick();

    void onLinesRadioClick();

    void onPointsRadioClick();


    void addSurvey(FarmerLands rowsEntity);

    void onCrossClick();


}
