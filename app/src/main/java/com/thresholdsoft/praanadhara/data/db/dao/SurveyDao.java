package com.thresholdsoft.praanadhara.data.db.dao;

import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.data.db.model.LandEntity;
import com.thresholdsoft.praanadhara.data.db.model.Survey;
import com.thresholdsoft.praanadhara.data.db.model.SurveyEntity;
import com.thresholdsoft.praanadhara.data.db.model.SurveyStatusEntity;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static androidx.room.OnConflictStrategy.REPLACE;

/**
 * Created on : March 30, 2020
 * Author     : JAGADEESH
 */
@Dao
public interface SurveyDao {

    @Query("SELECT * FROM Survey")
    List<Survey> getAll();

    @Insert(onConflict = REPLACE)
    void insertUser(Survey mSurvey);

    @Insert
    void insertAllSurvey(ArrayList<Survey> surveys);

    @Delete
    void deleteUser(Survey mSurvey);

    @Update
    void updateUser(Survey mSurvey);

    @Query("SELECT * FROM Survey WHERE uid = :uId")
    Survey getUserById(int uId);


    @Query("SELECT * FROM Survey WHERE uid IN (:userIds)")
    List<Survey> loadAllByIds(int[] userIds);

//    @Query("SELECT * FROM Survey WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    Survey findByName(String first, String last);

    @Insert
    void insertFarmerLand(FarmerLands farmerLands);

    @Query("SELECT * FROM farmer_land Where uid = :uid and farmer_land_uid = :landUid")
    FarmerLands  getFarmerLand(String uid,String landUid);

    @Query("SELECT * FROM farmer_land")
    LiveData<List<FarmerLands>> getAllFarmerLands();

    @Update
    void updateFarmerLand(FarmerLands farmerLands);

//    @Insert
//    void insetLandEntity(LandEntity landEntity);
//
//    @Query("SELECT * FROM land_details where uid = :landUid")
//    LandEntity getLandEntity(String landUid);
//
//    @Update
//    void updateLandEntity(LandEntity landEntity);

    @Insert
    void insetSurveyEntity(SurveyEntity surveyEntity);

    @Query("SELECT * FROM survey_details where uid = :uid")
    SurveyEntity getSurveyEntity(String uid);

    @Update
    void updateSurveyEntity(SurveyEntity surveyEntity);

    @Query("SELECT * FROM survey_details where land_uid = :landUid")
    List<SurveyEntity> getAllSurveyList(String landUid);

    @Insert(onConflict = REPLACE)
    void insertSurveyCount(SurveyStatusEntity surveyStatusEntity);

    @Update
    void updateSurveyCount(SurveyStatusEntity surveyStatusEntity);

    @Query("SELECT * FROM survey_status_count")
    LiveData<SurveyStatusEntity> getSurveyCount();

}

