package com.thresholdsoft.wakfboard.ui.gallery;


import com.thresholdsoft.wakfboard.ui.base.MvpPresenter;
import com.thresholdsoft.wakfboard.ui.propertycreation.model.PropertyData;
import com.thresholdsoft.wakfboard.ui.propertysurvey.model.MapDataTable;

import java.util.List;

public interface GalleryMvpPresenter<V extends GalleryMvpView> extends MvpPresenter<V> {
    List<MapDataTable> getAllMapDataTableListByPropertyid(int propertyId);

    List<PropertyData> getAllPropertyList(int id);

    void onClickBack();

    void updateMapDataList(MapDataTable mapDataTable);
}
