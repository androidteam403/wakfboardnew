package com.thresholdsoft.wakfboard.data.network;

import com.thresholdsoft.wakfboard.data.network.pojo.FarmerSurveyList;
import com.thresholdsoft.wakfboard.data.network.pojo.FeedItem;
import com.thresholdsoft.wakfboard.data.network.pojo.SurveySaveReq;
import com.thresholdsoft.wakfboard.data.network.pojo.SurveyStartReq;
import com.thresholdsoft.wakfboard.data.network.pojo.SurveyStartRes;
import com.thresholdsoft.wakfboard.data.network.pojo.WrapperResponse;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.model.FarmerLandReq;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.model.SurveyStatusCountModelRequest;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.model.SurveyStatusCountModelResponse;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.model.DeleteReq;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.model.DeleteRes;

import java.util.List;

import io.reactivex.Single;
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
    Single<FarmerSurveyList> doFarmerListApiCall(@Body FarmerLandReq o);

    @POST("api/survey/save-update/survey-start")
    Single<WrapperResponse<SurveyStartRes>> SURVEY_START_RES_SINGLE (@Body SurveyStartReq surveyStartReq);

    @POST("api/survey_details/save-update/survey-details-save")
    Single<WrapperResponse<SurveyStartRes>> SURVEY_SAVE_RES_SINGLE (@Body SurveySaveReq surveySaveReq);

    @POST("api/survey/save-update/survey-submit")
    Single<WrapperResponse<SurveyStartRes>> SURVEY_SUBMIT_RES_SINGLE(@Body SurveySaveReq.SurveyEntity landLocationEntity);

    @POST("api/survey_details/delete/survey-details-delete")
    Single<WrapperResponse<DeleteRes>> DELETE_SURVEY_RES_SINGLE (@Body DeleteReq deleteReq);

    @POST("api/farmer/select/survey-status-count")
    Single<WrapperResponse<SurveyStatusCountModelResponse>> STATUS_COUNT_SURVEY_RES_SINGLE (@Body SurveyStatusCountModelRequest surveyStatusCountModelRequest);

}
