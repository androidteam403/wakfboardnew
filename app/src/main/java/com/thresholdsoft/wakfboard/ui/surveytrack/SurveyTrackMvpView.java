package com.thresholdsoft.wakfboard.ui.surveytrack;

import com.thresholdsoft.wakfboard.data.db.model.FarmerLands;
import com.thresholdsoft.wakfboard.ui.base.MvpView;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.model.SurveyDetailsModel;

public interface SurveyTrackMvpView extends MvpView {

    FarmerLands getFarmerLand();

    String getSurveyId();

    int getSurveyType();

    int getPolygonEditType();

    void onClickPauseResumeBtn();

    void onClickStopBtn();

    void onClickSavePoints();

    void surveySaveSuccess();

    void onPassSurveyTrackEnteredDetails(SurveyDetailsModel surveyDetailsModel);

    void onClickStartPolygon();

    void savePolyGone();
}
