package com.thresholdsoft.praanadhara.data.network;

import com.thresholdsoft.praanadhara.data.network.pojo.FarmerSurveyList;
import com.thresholdsoft.praanadhara.data.network.pojo.FeedItem;
import com.thresholdsoft.praanadhara.data.network.pojo.LoginRequest;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveySaveReq;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartReq;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartRes;
import com.thresholdsoft.praanadhara.data.network.pojo.UserProfile;
import com.thresholdsoft.praanadhara.data.network.pojo.WrapperResponse;
import com.thresholdsoft.praanadhara.ui.userlogin.model.LoginResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created on : March 30, 2020
 * Author     : JAGADEESH
 */
public interface NetworkService {
    /**
     * @return Observable feed response
     */
    @GET("feed.json")
    Single<WrapperResponse<List<FeedItem>>> getFeedList();


    @POST("api/farmer/list/farmer-survey-list")
    Single<FarmerSurveyList> doFarmerListApiCall(@Body Object o);

    @POST("api/survey/save-update/survey-start")
    Single<WrapperResponse<SurveyStartRes>> SURVEY_START_RES_SINGLE (@Body SurveyStartReq surveyStartReq);

    @POST("api/survey_details/save-update/survey-details-save")
    Single<WrapperResponse<SurveyStartRes>> SURVEY_SAVE_RES_SINGLE (@Body SurveySaveReq surveySaveReq);

    @POST("api/survey/save-update/survey-submit")
    Single<WrapperResponse<SurveyStartRes>> SURVEY_SUBMIT_RES_SINGLE(@Body SurveySaveReq.SurveyEntity landLocationEntity);

}
