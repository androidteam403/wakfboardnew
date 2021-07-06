package com.thresholdsoft.praanadhara.ui.propertysurvey;

import com.thresholdsoft.praanadhara.ui.base.MvpView;

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
}
