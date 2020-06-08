package com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.editdialog;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class EditDialogPresenter<V extends EditDialogMvpView> extends BasePresenter<V>
        implements EditDialogMvpPresenter<V> {

    @Inject
    public EditDialogPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
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


}
