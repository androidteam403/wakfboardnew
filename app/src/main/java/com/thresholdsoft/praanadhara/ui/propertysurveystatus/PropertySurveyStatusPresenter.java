package com.thresholdsoft.praanadhara.ui.propertysurveystatus;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class PropertySurveyStatusPresenter<V extends PropertySurveyStatusMvpView> extends BasePresenter<V>
        implements PropertySurveyStatusMvpPresenter<V> {

    @Inject
    public PropertySurveyStatusPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }


    @Override
    public void getMapTypelist(int propertyId) {
        getMvpView().getMapDataTable(getDataManager().getAllMapDtaListByPropertyId(propertyId));
    }
}
