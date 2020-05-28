package com.thresholdsoft.praanadhara.ui.userlogin;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class UserLoginPresenter<V extends UserLoginMvpView> extends BasePresenter<V>
        implements UserLoginMvpPresenter<V> {

    @Inject
    public UserLoginPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onLoginClick() {
        getMvpView().onLoginClick();
    }

    @Override
    public void onCrossClick() {
        getMvpView().onCrossClick();
    }

    @Override
    public void onVerifyClick() {
        getMvpView().onVerifyClick();
    }
}