package com.thresholdsoft.praanadhara.ui.propertysurvey;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.ui.propertysurvey.model.PointDataTable;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class PropertySurveyPresenter<V extends PropertySurveyMvpView> extends BasePresenter<V>
        implements PropertySurveyMvpPresenter<V> {

    @Inject
    public PropertySurveyPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void insertPointDataTable(PointDataTable pointDataTable) {
        getDataManager().insertPointRelatedData(pointDataTable);
    }
}
