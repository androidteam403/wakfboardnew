package com.thresholdsoft.praanadhara.data;

import android.content.Context;

import com.thresholdsoft.praanadhara.data.db.AppDatabase;
import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.data.db.model.LandEntity;
import com.thresholdsoft.praanadhara.data.db.model.Survey;
import com.thresholdsoft.praanadhara.data.db.model.SurveyEntity;
import com.thresholdsoft.praanadhara.data.network.RestApiHelper;
import com.thresholdsoft.praanadhara.data.network.pojo.FarmerSurveyList;
import com.thresholdsoft.praanadhara.data.network.pojo.FeedItem;
import com.thresholdsoft.praanadhara.data.network.pojo.LoginRequest;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveySaveReq;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartReq;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartRes;
import com.thresholdsoft.praanadhara.data.network.pojo.UserProfile;
import com.thresholdsoft.praanadhara.data.network.pojo.PicEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.WrapperResponse;
import com.thresholdsoft.praanadhara.data.prefs.PreferencesHelper;
import com.thresholdsoft.praanadhara.data.utils.LoggedInMode;
import com.thresholdsoft.praanadhara.di.ApplicationContext;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.FarmerLandReq;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyStatusCountModelRequest;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyStatusCountModelResponse;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.model.DeleteReq;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.model.DeleteRes;
import com.thresholdsoft.praanadhara.ui.userlogin.model.LoginResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class BaseDataManager implements DataManager {
    private static final String TAG = "BaseDataManager";


    private final Context mContext;
    private final AppDatabase mDatabase;
    private final PreferencesHelper mPreferencesHelper;
    private final RestApiHelper mApiHelper;

    @Inject
    public BaseDataManager(@ApplicationContext Context context,
                           AppDatabase database,
                           PreferencesHelper preferencesHelper,
                           RestApiHelper apiHelper) {
        mContext = context;
        mDatabase = database;
        mPreferencesHelper = preferencesHelper;
        mApiHelper = apiHelper;
    }

    @Override
    public void updateApiHeader(Long userId, String accessToken) {

    }

    @Override
    public void setUserLoggedOut() {
        logoutUser();
    }

    @Override
    public void updateUserInfo(String accessToken,  String userName, String email, String phone) {
        mPreferencesHelper.setAccessToken(accessToken);
        mPreferencesHelper.setUserName(userName);
        mPreferencesHelper.setUserEmail(email);
        mPreferencesHelper.setUserMobile(phone);
    }

    @Override
    public List<Survey> getAll() {
        return mDatabase.userDao().getAll();
    }

    @Override
    public void insertUser(Survey mSurvey) {
        mDatabase.userDao().insertUser(mSurvey);
    }

    @Override
    public void insertAllSurvey(ArrayList<Survey> surveys) {
        mDatabase.userDao().insertAllSurvey(surveys);
    }

    @Override
    public void deleteUser(Survey mSurvey) {
        mDatabase.userDao().deleteUser(mSurvey);
    }

    @Override
    public void updateUser(Survey mSurvey) {

    }


    @Override
    public Survey getUserById(int uId) {
        return mDatabase.userDao().getUserById(uId);
    }

    @Override
    public List<Survey> loadAllByIds(int[] userIds) {
        return mDatabase.userDao().loadAllByIds(userIds);
    }

    @Override
    public void insertFarmerLand(FarmerLands farmerLands) {
        FarmerLands lands = getFarmerLand(farmerLands.getUid(),farmerLands.getFarmerLandUid());
       if( lands != null){
           farmerLands.setId(lands.getId());
           updateFarmerLand(farmerLands);
       }else
           mDatabase.userDao().insertFarmerLand(farmerLands);
    }

    @Override
    public FarmerLands getFarmerLand(String uid, String landUid) {
        return mDatabase.userDao().getFarmerLand(uid,landUid);
    }

    @Override
    public List<FarmerLands> getAllFarmerLands() {
        return mDatabase.userDao().getAllFarmerLands();
    }

    @Override
    public void updateFarmerLand(FarmerLands farmerLands) {
        mDatabase.userDao().updateFarmerLand(farmerLands);
    }

    @Override
    public void insetLandEntity(LandEntity landEntity) {
        LandEntity lands = getLandEntity(landEntity.getUid());
        if( lands != null){
            landEntity.setId(lands.getId());
            updateLandEntity(landEntity);
        }else
            mDatabase.userDao().insetLandEntity(landEntity);
    }

    @Override
    public LandEntity getLandEntity(String landUid) {
        return mDatabase.userDao().getLandEntity(landUid);
    }

    @Override
    public void updateLandEntity(LandEntity landEntity) {
        mDatabase.userDao().updateLandEntity(landEntity);
    }

    @Override
    public void insetSurveyEntity(SurveyEntity surveyEntity) {
        SurveyEntity lands = getSurveyEntity(surveyEntity.getUid());
        if( lands != null){
            surveyEntity.setId(lands.getId());
            updateSurveyEntity(surveyEntity);
        }else
            mDatabase.userDao().insetSurveyEntity(surveyEntity);
    }

    @Override
    public SurveyEntity getSurveyEntity(String uid) {
        return mDatabase.userDao().getSurveyEntity(uid);
    }

    @Override
    public void updateSurveyEntity(SurveyEntity surveyEntity) {
        mDatabase.userDao().updateSurveyEntity(surveyEntity);
    }

    @Override
    public List<SurveyEntity> getAllSurveyList(String landUid) {
        return mDatabase.userDao().getAllSurveyList(landUid);
    }

//    @Override
//    public Survey findByName(String first, String last) {
//        return mDatabase.userDao().findByName(first, last);
//    }

    @Override
    public Single<FarmerSurveyList> doFarmerListApiCall(FarmerLandReq request) {
        return mApiHelper.doFarmerListApiCall(request);
    }

    @Override
    public Single<WrapperResponse<SurveyStartRes>> startSurvey(SurveyStartReq surveyStartReq) {
        return mApiHelper.startSurvey(surveyStartReq);
    }

    @Override
    public Single<WrapperResponse<SurveyStartRes>> saveSurvey(SurveySaveReq surveySaveReq) {
        return mApiHelper.saveSurvey(surveySaveReq);
    }

    @Override
    public Single<WrapperResponse<SurveyStartRes>> submitSurvey(SurveySaveReq.SurveyEntity locationEntity) {
        return mApiHelper.submitSurvey(locationEntity);
    }

    @Override
    public Single<WrapperResponse<List<FeedItem>>> getFeedList() {
        return mApiHelper.getFeedList();
    }

    @Override
    public Single<WrapperResponse<DeleteRes>> deleteSurvey(DeleteReq deleteReq) {
        return mApiHelper.deleteSurvey(deleteReq);
    }

    @Override
    public Single<WrapperResponse<SurveyStatusCountModelResponse>> statusCount(SurveyStatusCountModelRequest surveyStatusCountModelRequest) {
        return mApiHelper.statusCount(surveyStatusCountModelRequest);
    }

    @Override
    public void storeSurveyClick(boolean value) {
        mPreferencesHelper.storeSurveyClick(value);
    }

    @Override
    public boolean isSurveyClick() {
        return mPreferencesHelper.isSurveyClick();
    }

    @Override
    public void loginRequest(String req) {
        mPreferencesHelper.loginRequest(req);
    }

    @Override
    public String loginRequest() {
        return mPreferencesHelper.loginRequest();
    }

    @Override
    public void loginResponse(String response) {
        mPreferencesHelper.loginResponse(response);
    }

    @Override
    public LoginResponse loginResponse() {
        return mPreferencesHelper.loginResponse();
    }

    @Override
    public void storeUserLogin(boolean value) {
        mPreferencesHelper.storeUserLogin(value);
    }

    @Override
    public boolean isUserLogin() {
        return mPreferencesHelper.isUserLogin();
    }

    @Override
    public void storeUserNum(String key) {
        mPreferencesHelper.storeUserNum(key);
    }

    @Override
    public String storeUserNum() {
        return mPreferencesHelper.storeUserNum();
    }

    @Override
    public void storeUserKey(String num) {
        mPreferencesHelper.storeUserKey(num);
    }

    @Override
    public String storeUserKey() {
        return mPreferencesHelper.storeUserKey();
    }


    @Override
    public int getUserLoggedInMode() {
        return mPreferencesHelper.getUserLoggedInMode();
    }

    @Override
    public void setUserLoggedIn(LoggedInMode mode) {
        mPreferencesHelper.setUserLoggedIn(mode);
    }

    @Override
    public Long getUserId() {
        return mPreferencesHelper.getUserId();
    }

    @Override
    public void setUserId(Long userId) {
        mPreferencesHelper.setUserId(userId);
    }

    @Override
    public String getUserName() {
        return mPreferencesHelper.getUserName();
    }

    @Override
    public void setUserName(String userName) {
        mPreferencesHelper.setUserName(userName);
    }

    @Override
    public String getUserEmail() {
        return mPreferencesHelper.getUserEmail();
    }

    @Override
    public void setUserEmail(String email) {
        mPreferencesHelper.setUserEmail(email);
    }

    @Override
    public String getUserProfilePicUrl() {
        return mPreferencesHelper.getUserProfilePicUrl();
    }

    @Override
    public void setUserProfilePicUrl(String profilePicUrl) {
        mPreferencesHelper.setUserProfilePicUrl(profilePicUrl);
    }

    @Override
    public String getAccessToken() {
        return mPreferencesHelper.getAccessToken();
    }

    @Override
    public void setAccessToken(String accessToken) {
        mPreferencesHelper.setAccessToken(accessToken);
    }

    @Override
    public String getUserMobile() {
        return mPreferencesHelper.getUserMobile();
    }

    @Override
    public void setUserMobile(String mobileNumber) {
        mPreferencesHelper.setUserMobile(mobileNumber);
    }

    @Override
    public boolean isCoachMarkView() {
        return mPreferencesHelper.isCoachMarkView();
    }

    @Override
    public void setCoachMarkView(boolean isShowCoachMark) {
        mPreferencesHelper.setCoachMarkView(isShowCoachMark);
    }

    @Override
    public boolean isFirstTime() {
        return mPreferencesHelper.isFirstTime();
    }

    @Override
    public void setFirstTime(boolean firstTime) {
        mPreferencesHelper.setCoachMarkView(firstTime);
    }

    @Override
    public void logoutUser() {
        mPreferencesHelper.logoutUser();
    }
}
