package com.thresholdsoft.praanadhara.ui.propertycreation;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.ui.propertycreation.model.PropertyData;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class PropertyPresenter<V extends PropertyMvpView> extends BasePresenter<V>
        implements PropertyMvpPresenter<V> {

    @Inject
    public PropertyPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void insertPropertyData(PropertyData propertyData) {
        getDataManager().insertPropertyData(propertyData);
//        if (photoUploadedData != null && photoUploadedData.size() > 0) {
//            for (PhotoUploadedData photoUploadedData1 : photoUploadedData) {
//                getDataManager().insertPhotoUploadData(photoUploadedData1);
//            }
//        }
    }
}
