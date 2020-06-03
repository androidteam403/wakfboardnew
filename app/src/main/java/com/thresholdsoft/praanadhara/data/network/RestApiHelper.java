package com.thresholdsoft.praanadhara.data.network;

import com.thresholdsoft.praanadhara.data.network.pojo.FarmerSurveyList;
import com.thresholdsoft.praanadhara.data.network.pojo.FeedItem;
import com.thresholdsoft.praanadhara.data.network.pojo.LoginRequest;
import com.thresholdsoft.praanadhara.data.network.pojo.UserProfile;
import com.thresholdsoft.praanadhara.data.network.pojo.WrapperResponse;

import java.util.List;

import io.reactivex.Single;

public interface RestApiHelper {

    Single<FarmerSurveyList> doFarmerListApiCall(Object request);

    Single<WrapperResponse<List<FeedItem>>> getFeedList();
}
