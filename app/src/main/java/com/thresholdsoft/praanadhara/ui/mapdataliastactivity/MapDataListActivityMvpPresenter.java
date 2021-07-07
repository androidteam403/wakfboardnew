package com.thresholdsoft.praanadhara.ui.mapdataliastactivity;

import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;
import com.thresholdsoft.praanadhara.ui.propertysurvey.model.MapDataTable;

import java.util.List;

public interface MapDataListActivityMvpPresenter<V extends MapDataListActivityMvpView> extends MvpPresenter<V> {

    List<MapDataTable> getMapTypelist(int name);

}
