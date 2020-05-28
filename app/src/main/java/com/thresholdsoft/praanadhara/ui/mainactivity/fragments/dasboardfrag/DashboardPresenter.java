package com.thresholdsoft.praanadhara.ui.mainactivity.fragments.dasboardfrag;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class DashboardPresenter<V extends DashboardMvpView> extends BasePresenter<V>
        implements DashboardMvpPresenter<V> {

    @Inject
    public DashboardPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }
}
