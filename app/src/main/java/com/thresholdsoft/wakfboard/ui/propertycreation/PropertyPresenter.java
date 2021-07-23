package com.thresholdsoft.wakfboard.ui.propertycreation;

import com.thresholdsoft.wakfboard.data.DataManager;
import com.thresholdsoft.wakfboard.ui.base.BasePresenter;
import com.thresholdsoft.wakfboard.ui.propertycreation.model.PropertyData;
import com.thresholdsoft.wakfboard.utils.rx.SchedulerProvider;

import java.util.List;

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

    @Override
    public int propertyID() {
        List<PropertyData> propertyDataList = getDataManager().getAllPropertyCreationDataList();
        return propertyDataList.get(propertyDataList.size()-1).getId();
    }

    @Override
    public void onClickBack() {
        getMvpView().onClickBack();
    }
}
