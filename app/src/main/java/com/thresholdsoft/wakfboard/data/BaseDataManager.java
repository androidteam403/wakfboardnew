package com.thresholdsoft.wakfboard.data;

import android.content.Context;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;

import com.thresholdsoft.wakfboard.data.db.AppDatabase;
import com.thresholdsoft.wakfboard.data.db.model.FarmerLands;
import com.thresholdsoft.wakfboard.data.db.model.SurveyEntity;
import com.thresholdsoft.wakfboard.data.db.model.SurveyStatusEntity;
import com.thresholdsoft.wakfboard.data.network.RestApiHelper;
import com.thresholdsoft.wakfboard.data.network.pojo.FarmerSurveyList;
import com.thresholdsoft.wakfboard.data.network.pojo.FeedItem;
import com.thresholdsoft.wakfboard.data.network.pojo.SurveySaveReq;
import com.thresholdsoft.wakfboard.data.network.pojo.SurveyStartReq;
import com.thresholdsoft.wakfboard.data.network.pojo.SurveyStartRes;
import com.thresholdsoft.wakfboard.data.network.pojo.WrapperResponse;
import com.thresholdsoft.wakfboard.data.prefs.PreferencesHelper;
import com.thresholdsoft.wakfboard.data.utils.LoggedInMode;
import com.thresholdsoft.wakfboard.di.ApplicationContext;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.model.FarmerLandReq;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.model.SurveyStatusCountModelRequest;
import com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.model.SurveyStatusCountModelResponse;
import com.thresholdsoft.wakfboard.ui.propertycreation.model.PropertyData;
import com.thresholdsoft.wakfboard.ui.propertysurvey.model.MapDataTable;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.model.DeleteReq;
import com.thresholdsoft.wakfboard.ui.surveystatusactivity.model.DeleteRes;
import com.thresholdsoft.wakfboard.ui.userlogin.model.LoginResponse;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class BaseDataManager implements DataManager {


    private final AppDatabase mDatabase;
    private final PreferencesHelper mPreferencesHelper;
    private final RestApiHelper mApiHelper;

    @Inject
    public BaseDataManager(@ApplicationContext Context context,
                           AppDatabase database,
                           PreferencesHelper preferencesHelper,
                           RestApiHelper apiHelper) {
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
    public void updateUserInfo(String accessToken, String userName, String email, String phone) {
        mPreferencesHelper.setAccessToken(accessToken);
        mPreferencesHelper.setUserName(userName);
        mPreferencesHelper.setUserEmail(email);
        mPreferencesHelper.setUserMobile(phone);
    }

    @Override
    public void insertFarmerLand(FarmerLands farmerLands) {
        FarmerLands lands = getFarmerLandDetails(farmerLands.getUid(), farmerLands.getFarmerLandUid());
        if (lands != null) {
            farmerLands.setId(lands.getId());
            farmerLands.setStatus(lands.getStatus());
            farmerLands.setStart(lands.isStart());
            farmerLands.setSubmit(lands.isSubmit());
            farmerLands.setCreatedAt(lands.getCreatedAt());
            farmerLands.setLastUpdate(new Date());
            updateFarmerLand(farmerLands);
        } else {
            farmerLands.setCreatedAt(new Date());
            farmerLands.setLastUpdate(new Date());
            mDatabase.userDao().insertFarmerLand(farmerLands);
        }
    }

    @Override
    public LiveData<FarmerLands> getFarmerLand(String uid, String landUid) {
        return mDatabase.userDao().getFarmerLand(uid, landUid);
    }

    @Override
    public FarmerLands getFarmerLandDetails(String uid, String landUid) {
        return mDatabase.userDao().getFarmerLandDetails(uid, landUid);
    }

    @Override
    public FarmerLands getFarmerLandDetails(String landUid) {
        return mDatabase.userDao().getFarmerLandDetails(landUid);
    }

    @Override
    public LiveData<List<FarmerLands>> getAllFarmerLands() {
        return mDatabase.userDao().getAllFarmerLands();
    }

    @Override
    public void updateFarmerLand(FarmerLands farmerLands) {
        mDatabase.userDao().updateFarmerLand(farmerLands);
    }


    @Override
    public void insetSurveyEntity(SurveyEntity surveyEntity) {
        if (!TextUtils.isEmpty(surveyEntity.getUid())) {
            SurveyEntity lands = getSurveyEntity(surveyEntity.getUid());
            if (lands != null) {
                surveyEntity.setId(lands.getId());
                surveyEntity.setUnchecked(lands.isUnchecked());
                updateSurveyEntity(surveyEntity);
            } else
                mDatabase.userDao().insetSurveyEntity(surveyEntity);
        } else {
            mDatabase.userDao().insetSurveyEntity(surveyEntity);
        }
    }

    @Override
    public SurveyEntity getSurveyEntity(String uid) {
        return mDatabase.userDao().getSurveyEntity(uid);
    }

    @Override
    public SurveyEntity getSurveyLandEntity(String landUid) {
        return mDatabase.userDao().getSurveyLandEntity(landUid);
    }

    @Override
    public void updateSurveyEntity(SurveyEntity surveyEntity) {
        mDatabase.userDao().updateSurveyEntity(surveyEntity);
    }

    @Override
    public LiveData<List<SurveyEntity>> getAllSurveyList(String landUid) {
        return mDatabase.userDao().getAllSurveyList(landUid);
    }

    @Override
    public void deleteSurveyEntity(SurveyEntity surveyEntity) {
        mDatabase.userDao().deleteSurveyEntity(surveyEntity);
    }

    @Override
    public void insertSurveyCount(SurveyStatusEntity surveyStatusEntity) {
        mDatabase.userDao().insertSurveyCount(surveyStatusEntity);
    }

    @Override
    public void updateStatusCount(SurveyStatusEntity surveyStatusEntity) {
        mDatabase.userDao().updateStatusCount(surveyStatusEntity);
    }

    @Override
    public SurveyStatusEntity getSurveyCountData() {
        return mDatabase.userDao().getSurveyCountData();
    }

    @Override
    public LiveData<SurveyStatusEntity> getSurveyCount() {
        return mDatabase.userDao().getSurveyCount();
    }

    @Override
    public List<SurveyEntity> getAllSurveyEditList(boolean isEdit) {
        return mDatabase.userDao().getAllSurveyEditList(isEdit);
    }

    @Override
    public List<SurveyEntity> getAllSurveyDeleteList(boolean isDelete) {
        return mDatabase.userDao().getAllSurveyDeleteList(isDelete);
    }

    @Override
    public List<SurveyEntity> getAllSurveySyncList(boolean isSync) {
        return mDatabase.userDao().getAllSurveySyncList(isSync);
    }

    @Override
    public List<FarmerLands> getAllSurveyStartList(boolean isStart) {
        return mDatabase.userDao().getAllSurveyStartList(isStart);
    }

    @Override
    public List<FarmerLands> getAllSurveySubmitList(boolean isSubmit) {
        return mDatabase.userDao().getAllSurveySubmitList(isSubmit);
    }

    @Override
    public void insertPropertyData(PropertyData propertyData) {
        mDatabase.userDao().insertPropertyData(propertyData);
    }

    @Override
    public List<PropertyData> getAllPropertyCreationDataList() {
        return mDatabase.userDao().getAllPropertyCreationDataList();
    }

    @Override
    public void insertMapData(MapDataTable mapDataTable) {
        mDatabase.userDao().insertMapData(mapDataTable);
    }

//    @Override
//    public void insertPointRelatedData(PointDataTable pointDataTable) {
//        mDatabase.userDao().insertPointRelatedData(pointDataTable);
//    }
//
//    @Override
//    public void insertPolylineData(PolylineDataTable polylineDataTable) {
//        mDatabase.userDao().insertPolylineData(polylineDataTable);
//    }
//
//    @Override
//    public void insertPolygonData(MapDataTable mapDataTable) {
//        mDatabase.userDao().insertPolygonData(mapDataTable);
//    }
//
//    @Override
//    public List<PolylineDataTable> getAllPolylineDataListByName(String name) {
//        return mDatabase.userDao().getAllPolylineDataListByName(name);
//    }
//
//    @Override
//    public List<PointDataTable> getAllPointDataListByName(String name) {
//        return mDatabase.userDao().getAllPointDataListByName(name);
//    }

    @Override
    public List<MapDataTable> getAllMapDtaListByPropertyId(int propertyId) {
        return mDatabase.userDao().getAllMapDtaListByPropertyId(propertyId);
    }

    @Override
    public void deleteMapDataTable() {
        mDatabase.userDao().deleteMapDataTable();
    }

    @Override
    public void deletePropertyDataTable() {
        mDatabase.userDao().deletePropertyDataTable();
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
