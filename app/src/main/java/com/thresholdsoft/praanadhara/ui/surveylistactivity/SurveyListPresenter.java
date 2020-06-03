package com.thresholdsoft.praanadhara.ui.surveylistactivity;

import android.util.Log;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.ui.ApiClient;
import com.thresholdsoft.praanadhara.ui.ApiInterface;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.FarmersRequest;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.FarmersResponse;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.model.SurveyModel;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

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
    public void onItemClick(FarmersResponse.Data.ListData.Rows surveyModel) {
        getMvpView().onItemClick(surveyModel);
    }

    @Override
    public void farmersListApiCall() {
        if (getMvpView().isNetworkConnected()) {
            getMvpView().showLoading();
            getMvpView().hideKeyboard();
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
            final FarmersRequest request = new FarmersRequest();
            String authKey = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1OTEwNzIxNjEsImQiOiIvVnlTSjJSWjhHND0ifQ.ORdg2kg1dHxeYOUCv7NMV7GuhcsNiR3uOPUOcJ0D9No";
            Call<FarmersResponse> call = api.doPostListFarmersResponse(authKey,request);
            call.enqueue(new Callback<FarmersResponse>() {
                @Override
                public void onResponse(Call<FarmersResponse> call, Response<FarmersResponse> response) {
                    getMvpView().hideLoading();
                    Log.e("TAG", response.code() + "");
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getData().getListData().getRows() != null &&
                                response.body().getData().getListData().getRows().size() > 0) {
                            getMvpView().onFarmersRes(response.body());
                        } else {
                            getMvpView().arrayListClear();
//                            getMvpView().hideKeyboard();
                        }
                    }
                }

                @Override
                public void onFailure(Call<FarmersResponse> call, Throwable t) {
                    handleApiError(t);
                }
            });
        } else {
            getMvpView().showMessage("Please Connect to Proper internet");
        }
    }
}
