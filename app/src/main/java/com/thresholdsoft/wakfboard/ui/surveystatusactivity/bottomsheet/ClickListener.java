package com.thresholdsoft.wakfboard.ui.surveystatusactivity.bottomsheet;

import com.thresholdsoft.wakfboard.data.db.model.FarmerLands;

public interface ClickListener {
    void onpolygonRadioClick();

    void onLinesRadioClick();

    void onPointsRadioClick();


    void addSurvey(FarmerLands rowsEntity);

    void onCrossClick();


}
