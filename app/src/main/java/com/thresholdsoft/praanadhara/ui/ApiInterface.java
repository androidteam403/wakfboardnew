package com.thresholdsoft.praanadhara.ui;

import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.FarmersRequest;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.FarmersResponse;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.model.DeleteReq;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.model.DeleteRes;
import com.thresholdsoft.praanadhara.ui.userlogin.model.LoginRequest;
import com.thresholdsoft.praanadhara.ui.userlogin.model.LoginResponse;
import com.thresholdsoft.praanadhara.ui.userlogin.model.OtpVerifyReq;
import com.thresholdsoft.praanadhara.ui.userlogin.model.OtpVerifyRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("https://fc-test.zeroco.de/zc-v3.1-user-svc/2.0/praanadhara/sendOtp")
    Call<LoginResponse> doPostListLoginResponse(@Body LoginRequest loginRequest);

    @POST("https://fc-test.zeroco.de/zc-v3.1-user-svc/2.0/praanadhara/login")
    Call<OtpVerifyRes> doPostListOtpRes(@Body OtpVerifyReq otpVerifyReq);

    @POST("https://fc-test.zeroco.de/zc-v3.1-user-svc/2.0/praanadhara/api/farmer/list/demographic-list")
    Call<FarmersResponse> doPostListFarmersResponse(@Header("Authorization") String anupama, @Body FarmersRequest farmersRequest);

    @POST("https://fc-test.zeroco.de/zc-v3.1-user-svc/2.0/praanadhara/api/survey_details/delete/survey-details-delete")
    Call<DeleteRes> doPostListDeleteResponse(@Header("Authorization") String anupama, @Body DeleteReq deleteReq);

}