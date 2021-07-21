package com.thresholdsoft.wakfboard.ui.propertysurveystatus;

import com.google.android.gms.maps.model.LatLng;
import com.thresholdsoft.wakfboard.ui.base.MvpPresenter;

import java.util.List;

public interface PropertySurveyStatusMvpPresenter<V extends PropertySurveyStatusMvpView> extends MvpPresenter<V> {

    void getMapTypelist(int name);

    String getPolygonAreainMeters(List<LatLng> polygonPoints);

    String getPolygonAreainSquareFeet(List<LatLng> polygonPoints);

    String getPolygonAreainSquareYards(List<LatLng> polygonPoints);

    String getPolygonAreainAcers(List<LatLng> polygonPoints);

    String getLineLength(LatLng fromPolyLineLatLng, LatLng toPolyLineLatLng);

    void onClickBack();

    void onClickGallery();

    void onClickPropertyEdit();

    void updateAreaByPropertyId(int id, String area);

    void onClickMapIcon();

    void saveMapViewType(String mapViewType);

    String getMapViewType();
}
