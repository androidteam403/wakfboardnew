package com.thresholdsoft.wakfboard.ui.propertysurveystatus;

import com.thresholdsoft.wakfboard.ui.base.MvpView;
import com.thresholdsoft.wakfboard.ui.propertysurvey.model.MapDataTable;

import java.util.List;

public interface PropertySurveyStatusMvpView extends MvpView {
    void mapTypeData(int mapData);

    void getMapDataTable(List<MapDataTable> mapDataTableList);

    void onClickBack();
}
