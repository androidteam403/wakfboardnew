package com.thresholdsoft.wakfboard.ui.mainactivity.fragments.userprofilefrag;

import com.thresholdsoft.wakfboard.data.DataManager;
import com.thresholdsoft.wakfboard.ui.base.BasePresenter;
import com.thresholdsoft.wakfboard.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class UserProfilePresenter<V extends UserProfileMvpView> extends BasePresenter<V>
        implements UserProfileMvpPresenter<V> {

    @Inject
    public UserProfilePresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public String getUserName() {
        return getDataManager().getUserName();
    }

    @Override
    public String getUserContactNum() {
        return getDataManager().getUserMobile();
    }

    @Override
    public String getUserEmail() {
        return getDataManager().getUserEmail();
    }

    @Override
    public void onUpdateClick() {
        getMvpView().onUpdateClick();
    }

    @Override
    public void onCancelClick() {
        getMvpView().onCancelClick();
    }
}
