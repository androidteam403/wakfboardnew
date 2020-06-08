package com.thresholdsoft.praanadhara.ui.mainactivity;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivityPresenter<V extends MainActivityMvpView> extends BasePresenter<V>
        implements MainActivityMvpPresenter<V> {

    @Inject
    public MainActivityPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public String getUserName() {
        return getDataManager().getUserName();
    }
}
