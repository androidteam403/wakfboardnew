package com.thresholdsoft.wakfboard.ui.surveystatusactivity.dialog.deletedialog;

import com.thresholdsoft.wakfboard.data.DataManager;
import com.thresholdsoft.wakfboard.ui.base.BasePresenter;
import com.thresholdsoft.wakfboard.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class DeletePresenter<V extends DeleteMvpView> extends BasePresenter<V>
        implements DeleteMvpPresenter<V> {

    @Inject
    public DeletePresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onNoClick() {
        getMvpView().onNoClick();
    }

}
