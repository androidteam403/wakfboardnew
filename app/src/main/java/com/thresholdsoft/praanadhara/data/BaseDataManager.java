package com.thresholdsoft.praanadhara.data;

import android.content.Context;

import com.thresholdsoft.praanadhara.data.db.AppDatabase;
import com.thresholdsoft.praanadhara.data.db.model.Survey;
import com.thresholdsoft.praanadhara.data.network.RestApiHelper;
import com.thresholdsoft.praanadhara.data.network.pojo.FeedItem;
import com.thresholdsoft.praanadhara.data.network.pojo.LoginRequest;
import com.thresholdsoft.praanadhara.data.network.pojo.UserProfile;
import com.thresholdsoft.praanadhara.data.network.pojo.WrapperResponse;
import com.thresholdsoft.praanadhara.data.prefs.PreferencesHelper;
import com.thresholdsoft.praanadhara.data.utils.LoggedInMode;
import com.thresholdsoft.praanadhara.di.ApplicationContext;
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
    public void updateUserInfo(String accessToken, Long userId, LoggedInMode loggedInMode, String userName, String email, String profilePicPath) {
        mPreferencesHelper.setAccessToken(accessToken);
        mPreferencesHelper.setUserId(userId);
        mPreferencesHelper.setUserLoggedIn(loggedInMode);
        mPreferencesHelper.setUserName(userName);
        mPreferencesHelper.setUserEmail(email);
        mPreferencesHelper.setUserProfilePicUrl(profilePicPath);
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

//    @Override
//    public Survey findByName(String first, String last) {
//        return mDatabase.userDao().findByName(first, last);
//    }

    @Override
    public Single<WrapperResponse<UserProfile>> doLoginApiCall(LoginRequest request) {
        return mApiHelper.doLoginApiCall(request);
    }

    @Override
    public Single<WrapperResponse<List<FeedItem>>> getFeedList() {
        return mApiHelper.getFeedList();
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
        mPreferencesHelper.getAccessToken();
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
