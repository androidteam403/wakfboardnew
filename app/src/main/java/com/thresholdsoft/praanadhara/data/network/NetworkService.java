package com.thresholdsoft.praanadhara.data.network;

import com.thresholdsoft.praanadhara.data.network.pojo.FeedItem;
import com.thresholdsoft.praanadhara.data.network.pojo.LoginRequest;
import com.thresholdsoft.praanadhara.data.network.pojo.UserProfile;
import com.thresholdsoft.praanadhara.data.network.pojo.WrapperResponse;

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


    @POST("login")
    Single<WrapperResponse<UserProfile>> doLoginApiCall(@Body LoginRequest mRequest);
}
