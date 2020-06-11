package com.thresholdsoft.praanadhara.data.network;

import com.thresholdsoft.praanadhara.data.network.pojo.FarmerSurveyList;
import com.thresholdsoft.praanadhara.data.network.pojo.FeedItem;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveySaveReq;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartReq;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartRes;
import com.thresholdsoft.praanadhara.data.network.pojo.WrapperResponse;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.FarmerLandReq;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyStatusCountModelRequest;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyStatusCountModelResponse;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.model.DeleteReq;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.model.DeleteRes;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class RestApiManager implements RestApiHelper {

    NetworkService mService;

    @Inject
    public RestApiManager(NetworkService apiService) {
        mService = apiService;
    }

    @Override
    public Single<FarmerSurveyList> doFarmerListApiCall(FarmerLandReq request) {
        return mService.doFarmerListApiCall(request);
    }

    @Override
    public Single<WrapperResponse<SurveyStartRes>> startSurvey(SurveyStartReq surveyStartReq) {
        return mService.SURVEY_START_RES_SINGLE(surveyStartReq);
    }

    @Override
    public Single<WrapperResponse<SurveyStartRes>> saveSurvey(SurveySaveReq surveySaveReq) {
        return mService.SURVEY_SAVE_RES_SINGLE(surveySaveReq);
    }

    @Override
    public Single<WrapperResponse<SurveyStartRes>> submitSurvey(SurveySaveReq.SurveyEntity locationEntity) {
        return mService.SURVEY_SUBMIT_RES_SINGLE(locationEntity);
    }

    @Override
    public Single<WrapperResponse<List<FeedItem>>> getFeedList() {
        return mService.getFeedList();
    }

    @Override
    public Single<WrapperResponse<DeleteRes>> deleteSurvey(DeleteReq deleteReq) {
        return mService.DELETE_SURVEY_RES_SINGLE(deleteReq);
    }

    @Override
    public Single<WrapperResponse<SurveyStatusCountModelResponse>> statusCount(SurveyStatusCountModelRequest surveyStatusCountModelRequest) {
        return mService.STATUS_COUNT_SURVEY_RES_SINGLE(surveyStatusCountModelRequest);
    }
}