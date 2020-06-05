package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import com.google.gson.JsonObject;
import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartReq;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class SurveyStatusPresenter<V extends SurveyStatusMvpView> extends BasePresenter<V>
        implements SurveyStatusMvpPresenter<V> {

    @Inject
    public SurveyStatusPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void startSurvey(RowsEntity rowsEntity) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .startSurvey(new SurveyStartReq(new SurveyStartReq.LandLocationEntity(rowsEntity.getFarmerLand().getUid())))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(blogResponse -> {
                    if (blogResponse != null && blogResponse.getData() != null && blogResponse.getSuccess()) {
                        rowsEntity.setStartSurveyUid(blogResponse.getData().getUid());
                        getMvpView().startSurveySuccess(rowsEntity);
                    }
                    getMvpView().hideLoading();
                }, throwable -> {
                    getMvpView().hideLoading();
                    handleApiError(throwable);
                }));


    }
}
