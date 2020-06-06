package com.thresholdsoft.praanadhara.ui.mainactivity.dialog;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class LogoutPresenter<V extends LogoutMvpView> extends BasePresenter<V>
        implements LogoutMvpPresenter<V> {

    @Inject
    public LogoutPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onNoClick() {
        getMvpView().onNoClick();
    }

    @Override
    public void onYesClick() {
        getMvpView().onYesClick();
    }

    @Override
    public void clearSharedPreference() {
        getDataManager().storeUserLogin(false);
    }
}
