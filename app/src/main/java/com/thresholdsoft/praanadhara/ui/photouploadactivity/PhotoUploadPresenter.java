package com.thresholdsoft.praanadhara.ui.photouploadactivity;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.ui.propertycreation.PropertyMvpPresenter;
import com.thresholdsoft.praanadhara.ui.propertycreation.PropertyMvpView;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class PhotoUploadPresenter <V extends PhotoUploadMvpView> extends BasePresenter<V>
        implements PhotoUploadMvpPresenter<V> {

    @Inject
    public PhotoUploadPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void photoUploadbutton() {
        getMvpView().photoUploadButton();
    }

    @Override
    public void onBackClickData() {
        getMvpView().onBackClickData();
    }
}
