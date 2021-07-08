package com.thresholdsoft.wakfboard.ui.mapdataliastactivity;

import com.thresholdsoft.wakfboard.ui.base.MvpView;
import com.thresholdsoft.wakfboard.ui.propertysurvey.model.MapDataTable;

import java.util.List;

public interface MapDataListActivityMvpView extends MvpView {

    void uncheckableData(int pos, List<MapDataTable> mapDataTable);

}
