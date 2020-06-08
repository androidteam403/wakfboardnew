package com.thresholdsoft.praanadhara.ui.mainactivity.fragments.newenrollmentfrag;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class NewEnrollmentFragPresenter<V extends NewEnrollmentFragMvpView> extends BasePresenter<V>
        implements NewEnrollmentFragMvpPresenter<V> {

    @Inject
    public NewEnrollmentFragPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public String getAccessToken() {
        return getDataManager().getAccessToken();
    }
}
