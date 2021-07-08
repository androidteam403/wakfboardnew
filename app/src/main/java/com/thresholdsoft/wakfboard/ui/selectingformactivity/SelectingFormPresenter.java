package com.thresholdsoft.wakfboard.ui.selectingformactivity;

import com.thresholdsoft.wakfboard.data.DataManager;
import com.thresholdsoft.wakfboard.ui.base.BasePresenter;
import com.thresholdsoft.wakfboard.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class SelectingFormPresenter<V extends SelectingFormMvpView> extends BasePresenter<V>
        implements SelectingFormMvpPresenter<V> {

    @Inject
    public SelectingFormPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onEnrollmentClick() {
        getMvpView().onEnrollmentClick();
    }

    @Override
    public void onSurveyClick() {
        getDataManager().storeSurveyClick(true);
        getMvpView().onSurveyClick();
    }

}
