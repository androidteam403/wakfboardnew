package com.thresholdsoft.wakfboard.ui.photouploadactivity;

import com.thresholdsoft.wakfboard.data.DataManager;
import com.thresholdsoft.wakfboard.ui.base.BasePresenter;
import com.thresholdsoft.wakfboard.utils.rx.SchedulerProvider;

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
