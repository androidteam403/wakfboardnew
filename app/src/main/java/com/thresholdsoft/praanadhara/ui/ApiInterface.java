package com.thresholdsoft.praanadhara.ui;

import com.thresholdsoft.praanadhara.ui.userlogin.model.LoginRequest;
import com.thresholdsoft.praanadhara.ui.userlogin.model.LoginResponse;
import com.thresholdsoft.praanadhara.ui.userlogin.model.OtpVerifyReq;
import com.thresholdsoft.praanadhara.ui.userlogin.model.OtpVerifyRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("https://fc-test.zeroco.de/zc-v3.1-user-svc/2.0/praanadhara/sendOtp")
    Call<LoginResponse> doPostListLoginResponse(@Body LoginRequest loginRequest);

    @POST("https://fc-test.zeroco.de/zc-v3.1-user-svc/2.0/praanadhara/login")
    Call<OtpVerifyRes> doPostListOtpRes(@Body OtpVerifyReq otpVerifyReq);
}