package com.thresholdsoft.praanadhara.ui.selectingformactivity;

import android.util.Log;

import com.google.gson.Gson;
import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.ui.ApiClient;
import com.thresholdsoft.praanadhara.ui.ApiInterface;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.ui.userlogin.model.LoginRequest;
import com.thresholdsoft.praanadhara.ui.userlogin.model.LoginResponse;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        getMvpView().onSurveyClick();
    }

    @Override
    public void onLoginApiCall() {
        if (getMvpView().isNetworkConnected()) {
            //    getMvpView().showLoading();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            String request = getDataManager().loginRequest();
            Gson gson = new Gson();
            LoginRequest loginRequest = gson.fromJson(request, LoginRequest.class);
            Call<LoginResponse> call = apiInterface.doPostListLoginResponse(loginRequest);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    Log.e("TAG", response.code() + "");
                    // getMvpView().hideLoading();
                    if (response.body() != null && response.body().getSuccess()) {
                        Gson gson = new Gson();
                        //  String requestString = gson.toJson(request);
                        String responseString = gson.toJson(response.body());
                        // getDataManager().vendorRequest(requestString);
                        getDataManager().loginResponse(responseString);
                        getMvpView().navigateToSurveyListActivity();
                    } else {
                        if (response.body() != null)
                            getMvpView().showMessage(response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    handleApiError(t);
                }

            });
        } else {
            getMvpView().showMessage("Please Connect to Proper internet");
        }
    }

    @Override
    public void checkUserLogin() {
        if (getDataManager().isUserLogin()) {
            onLoginApiCall();
        } else {
            getMvpView().navigateToUserLgin();
        }
    }

}
