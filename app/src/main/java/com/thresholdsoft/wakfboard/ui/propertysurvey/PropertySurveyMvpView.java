package com.thresholdsoft.wakfboard.ui.propertysurvey;

import com.thresholdsoft.wakfboard.ui.base.MvpView;

public interface PropertySurveyMvpView extends MvpView {

    void getSnackBarView(String msg);

    void polygonStartClick();

    void polygonSaveClick();

    void polygonPauseClick();

    void polygonResumeClick();

    void polygonStopClick();

    void polylineUndoClick();

    void polylineClearClick();

    void polylineSaveClick();

    void pointSaveClick();

    void polygonManualClear();

    void polygonManualUndo();

    void polygonManualSave();


    void polygonWalkClear();

    void polygonWalkStart();

    void polygonWalkStop();

    void polygonWalkSave();
}
