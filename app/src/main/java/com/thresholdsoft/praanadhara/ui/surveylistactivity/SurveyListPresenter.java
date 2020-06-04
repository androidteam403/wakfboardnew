package com.thresholdsoft.praanadhara.ui.surveylistactivity;

import com.google.gson.JsonObject;
import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.data.network.pojo.PicEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class SurveyListPresenter<V extends SurveyListMvpView> extends BasePresenter<V>
        implements SurveyListMvpPresenter<V> {
    PicEntity picEntity;

    @Inject
    public SurveyListPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onItemClick(RowsEntity farmerModel) {
        getMvpView().onItemClick(farmerModel);
    }

    @Override
    public void farmersListApiCall() {

        getCompositeDisposable().add(getDataManager()
                .doFarmerListApiCall(new JsonObject())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(blogResponse -> {
                    if (blogResponse != null && blogResponse.getData() != null && blogResponse.getData().getListdata().getRows().size() > 0) {
                        getMvpView().onFarmersRes(blogResponse.getData().getListdata().getRows());
                    }
                    getMvpView().hideLoading();
                }, throwable -> {
                    getMvpView().hideLoading();
                    handleApiError(throwable);
                }));
    }
}