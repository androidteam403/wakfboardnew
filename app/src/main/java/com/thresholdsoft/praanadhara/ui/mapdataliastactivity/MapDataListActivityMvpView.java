package com.thresholdsoft.praanadhara.ui.mapdataliastactivity;

import com.thresholdsoft.praanadhara.ui.base.MvpView;
import com.thresholdsoft.praanadhara.ui.propertysurvey.model.MapDataTable;

import java.util.List;

public interface MapDataListActivityMvpView extends MvpView {

    void uncheckableData(int pos, List<MapDataTable> mapDataTable);

}
