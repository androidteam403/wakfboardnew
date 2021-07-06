package com.thresholdsoft.praanadhara.ui.propertysurvey;

import com.thresholdsoft.praanadhara.ui.base.MvpView;

public interface PropertySurveyMvpView extends MvpView {

    void mapTypeData(int mapData);

    void getSnackBarView(String msg);
}
