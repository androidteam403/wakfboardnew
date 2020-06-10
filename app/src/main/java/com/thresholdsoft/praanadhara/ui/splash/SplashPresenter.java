package com.thresholdsoft.praanadhara.ui.splash;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class SplashPresenter<V extends SplashMvpView> extends BasePresenter<V>
        implements SplashMvpPresenter<V> {

    @Inject
    public SplashPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }


    @Override
    public void onLoginApiCall() {
//        if (getMvpView().isNetworkConnected()) {
//            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//            String request = getDataManager().loginRequest();
//            Gson gson = new Gson();
//            LoginRequest loginRequest = gson.fromJson(request, LoginRequest.class);
//            Call<LoginResponse> call = apiInterface.doPostListLoginResponse(loginRequest);
//            call.enqueue(new Callback<LoginResponse>() {
//                @Override
//                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                    Log.e("TAG", response.code() + "");
//                    if (response.body() != null && response.body().getSuccess()) {
//                        Gson gson = new Gson();
//                        String responseString = gson.toJson(response.body());
//                        getDataManager().isSurveyClick();
//                        getDataManager().loginResponse(responseString);
//                        getMvpView().navigateToSurveyListActivity();
//                    } else {
//                        if (response.body() != null)
//                            getMvpView().showMessage(response.body().getMessage());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<LoginResponse> call, Throwable t) {
//                    handleApiError(t);
//                }
//            });
//        } else {
//            getMvpView().showMessage("Please Connect to Proper internet");
//        }
    }

    @Override
    public void checkUserLogin() {
//        if (getDataManager().isUserLogin() && getDataManager().isSurveyClick()) {
        if (getDataManager().isUserLogin()) {
            getMvpView().navigateToSurveyListActivity();
        } else {
            getMvpView().navigateToUserLgin();
        }
    }
}