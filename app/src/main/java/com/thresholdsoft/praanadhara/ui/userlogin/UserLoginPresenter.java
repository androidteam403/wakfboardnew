package com.thresholdsoft.praanadhara.ui.userlogin;

import android.util.Log;

import com.google.gson.Gson;
import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.ui.ApiClient;
import com.thresholdsoft.praanadhara.ui.ApiInterface;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.ui.userlogin.model.LoginRequest;
import com.thresholdsoft.praanadhara.ui.userlogin.model.LoginResponse;
import com.thresholdsoft.praanadhara.ui.userlogin.model.OtpVerifyReq;
import com.thresholdsoft.praanadhara.ui.userlogin.model.OtpVerifyRes;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginPresenter<V extends UserLoginMvpView> extends BasePresenter<V>
        implements UserLoginMvpPresenter<V> {

    @Inject
    public UserLoginPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onLoginClick() {
        getMvpView().onLoginClick();
    }

    @Override
    public void onCrossClick() {
        getMvpView().onCrossClick();
    }

    @Override
    public void onVerifyClick() {
        getMvpView().onVerifyClick();
    }


    @Override
    public void onLiginApiCall() {
        if (getMvpView().isNetworkConnected()) {
            getMvpView().showLoading();
            getMvpView().hideKeyboard();
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
            final LoginRequest request = new LoginRequest();
            LoginRequest.CheckForExists checkForExists = new LoginRequest.CheckForExists();
            checkForExists.setEnable(true);
            checkForExists.setUser(true);
            request.setCheckForExists(checkForExists);
            request.setData(getMvpView().getPhoneNumber());
            request.setType("sms");
            request.setProvider("default_sms_gateway");
            request.setSender("zerotp");
            Call<LoginResponse> call = api.doPostListLoginResponse(request);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    getMvpView().hideLoading();
                    Log.e("TAG", response.code() + "");
                    if (response.body() != null && response.body().getSuccess()) {
                        Gson gson = new Gson();
                        String requestString = gson.toJson(request);
                        String responseString = gson.toJson(response.body());
                        getDataManager().loginRequest(requestString);
                        getDataManager().loginResponse(responseString);
                        getDataManager().storeUserNum(request.data);
                        getDataManager().storeUserKey(response.body().data.key);
                        getMvpView().onSucessfullLogin();
                        getMvpView().hideKeyboard();
                    } else {
                        getMvpView().showMessage("" + response.body().getMessage());
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
    public void onOtpApiCall() {
        if (getMvpView().isNetworkConnected()) {
            getMvpView().showLoading();
            getMvpView().hideKeyboard();
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
            final OtpVerifyReq request = new OtpVerifyReq();
            request.setAppUserName(getDataManager().storeUserNum());
            request.setKey(getDataManager().storeUserKey());
            request.setOtp(getMvpView().getOtp());
            request.setOtpKey(getDataManager().storeUserKey());
            request.setOtpType("sms");
            request.setOtpBased("true");
            Call<OtpVerifyRes> call = api.doPostListOtpRes(request);
            call.enqueue(new Callback<OtpVerifyRes>() {
                @Override
                public void onResponse(Call<OtpVerifyRes> call, Response<OtpVerifyRes> response) {
                    getMvpView().hideLoading();
                    Log.e("TAG", response.code() + "");
                    if (response.body() != null && response.body().getSuccess()) {
                        getDataManager().storeUserLogin(true);
                        getDataManager().updateUserInfo(response.body().getData().getToken(),response.body().getData().getName(),response.body().getData().getEmail(),response.body().getData().getPhone());
                      //  getDataManager().setAccessToken(response.body().getData().getToken());
                        getMvpView().onSucessfullLogin();
                        getMvpView().navigateToSurveyListActivity();
                        getMvpView().hideKeyboard();
                    } else {
                        getMvpView().showMessage("" + response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<OtpVerifyRes> call, Throwable t) {
                    handleApiError(t);
                }
            });
        } else {
            getMvpView().showMessage("Please Connect to Proper internet");
        }
    }

    @Override
    public void reseneOtpClick() {
        getMvpView().reseneOtpClick();
    }
}