package com.thresholdsoft.praanadhara.ui.surveylistactivity;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.ui.ApiClient;
import com.thresholdsoft.praanadhara.ui.ApiInterface;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.FarmersRequest;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.FarmersResponse;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.SurveyModel;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyListPresenter<V extends SurveyListMvpView> extends BasePresenter<V>
        implements SurveyListMvpPresenter<V> {
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
