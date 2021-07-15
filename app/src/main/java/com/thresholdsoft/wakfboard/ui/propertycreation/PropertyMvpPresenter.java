package com.thresholdsoft.wakfboard.ui.propertycreation;

import com.thresholdsoft.wakfboard.ui.base.MvpPresenter;
import com.thresholdsoft.wakfboard.ui.propertycreation.model.PropertyData;

public interface PropertyMvpPresenter<V extends PropertyMvpView> extends MvpPresenter<V> {

    void insertPropertyData(PropertyData propertyData);

    int propertyID();

    void onClickBack();

}
