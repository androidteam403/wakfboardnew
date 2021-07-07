package com.thresholdsoft.praanadhara.ui.propertycreation;

import com.thresholdsoft.praanadhara.ui.base.MvpPresenter;
import com.thresholdsoft.praanadhara.ui.propertycreation.model.PropertyData;

import java.util.List;

public interface PropertyMvpPresenter<V extends PropertyMvpView> extends MvpPresenter<V> {

    void insertPropertyData(PropertyData propertyData);

    int propertyID();

}
