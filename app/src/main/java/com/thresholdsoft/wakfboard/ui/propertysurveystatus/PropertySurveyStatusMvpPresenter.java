package com.thresholdsoft.wakfboard.ui.propertysurveystatus;

import com.google.android.gms.maps.model.LatLng;
import com.thresholdsoft.wakfboard.ui.base.MvpPresenter;

import java.util.List;

public interface PropertySurveyStatusMvpPresenter<V extends PropertySurveyStatusMvpView> extends MvpPresenter<V> {

    void getMapTypelist(int name);

    String getPolygonArea(List<LatLng> polygonPoints);

    String getLineLength(LatLng fromPolyLineLatLng, LatLng toPolyLineLatLng);

    void onClickBack();

}
