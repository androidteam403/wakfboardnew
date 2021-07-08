package com.thresholdsoft.wakfboard.ui.mapdataliastactivity;

import com.thresholdsoft.wakfboard.ui.base.MvpPresenter;
import com.thresholdsoft.wakfboard.ui.propertysurvey.model.MapDataTable;

import java.util.List;

public interface MapDataListActivityMvpPresenter<V extends MapDataListActivityMvpView> extends MvpPresenter<V> {

    List<MapDataTable> getMapTypelist(int name);

}
