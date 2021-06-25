package com.thresholdsoft.praanadhara.ui.propertycreation;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.ui.selectingformactivity.SelectingFormMvpPresenter;
import com.thresholdsoft.praanadhara.ui.selectingformactivity.SelectingFormMvpView;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class PropertyPresenter <V extends PropertyMvpView> extends BasePresenter<V>
        implements PropertyMvpPresenter<V>{

    @Inject
    public PropertyPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }
}
