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

public interface RestApiHelper {

    Single<FarmerSurveyList> doFarmerListApiCall(FarmerLandReq request);

    Single<WrapperResponse<SurveyStartRes>>  startSurvey(SurveyStartReq surveyStartReq);

    Single<WrapperResponse<SurveyStartRes>> saveSurvey(SurveySaveReq surveySaveReq);

    Single<WrapperResponse<SurveyStartRes>> submitSurvey(SurveySaveReq.SurveyEntity locationEntity);

    Single<WrapperResponse<List<FeedItem>>> getFeedList();

    Single<WrapperResponse<DeleteRes>> deleteSurvey(DeleteReq deleteReq);

    Single<WrapperResponse<SurveyStatusCountModelResponse>> statusCount(SurveyStatusCountModelRequest surveyStatusCountModelRequest);
}
