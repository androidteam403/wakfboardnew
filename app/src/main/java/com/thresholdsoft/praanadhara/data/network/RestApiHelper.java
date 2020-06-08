package com.thresholdsoft.praanadhara.data.network;

import com.thresholdsoft.praanadhara.data.network.pojo.FarmerSurveyList;
import com.thresholdsoft.praanadhara.data.network.pojo.FeedItem;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveySaveReq;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartRes;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartReq;
import com.thresholdsoft.praanadhara.data.network.pojo.WrapperResponse;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.model.DeleteReq;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.model.DeleteRes;

import java.util.List;

import io.reactivex.Single;

public interface RestApiHelper {

    Single<FarmerSurveyList> doFarmerListApiCall(Object request);

    Single<WrapperResponse<SurveyStartRes>> startSurvey(SurveyStartReq surveyStartReq);

    Single<WrapperResponse<SurveyStartRes>> saveSurvey(SurveySaveReq surveySaveReq);

    Single<WrapperResponse<SurveyStartRes>> submitSurvey(SurveySaveReq.SurveyEntity locationEntity);

    Single<WrapperResponse<List<FeedItem>>> getFeedList();

    Single<WrapperResponse<DeleteRes>> deleteSurvey(DeleteReq deleteReq);
}
