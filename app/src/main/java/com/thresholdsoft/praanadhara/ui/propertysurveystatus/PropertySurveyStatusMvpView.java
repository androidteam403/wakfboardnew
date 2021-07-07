package com.thresholdsoft.praanadhara.ui.propertysurveystatus;

import com.thresholdsoft.praanadhara.ui.base.MvpView;
import com.thresholdsoft.praanadhara.ui.propertysurvey.model.MapDataTable;

import java.util.List;

public interface PropertySurveyStatusMvpView extends MvpView {
    void mapTypeData(int mapData);

    void getMapDataTable(List<MapDataTable> mapDataTableList);

}
