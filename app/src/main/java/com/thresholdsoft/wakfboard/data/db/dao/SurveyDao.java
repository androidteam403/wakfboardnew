package com.thresholdsoft.wakfboard.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.thresholdsoft.wakfboard.data.db.model.FarmerLands;
import com.thresholdsoft.wakfboard.data.db.model.SurveyEntity;
import com.thresholdsoft.wakfboard.data.db.model.SurveyStatusEntity;
import com.thresholdsoft.wakfboard.ui.propertycreation.model.PropertyData;
import com.thresholdsoft.wakfboard.ui.propertysurvey.model.MapDataTable;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

/**
 * Created on : March 30, 2020
 * Author     : JAGADEESH
 */
@Dao
public interface SurveyDao {


    @Insert
    void insertFarmerLand(FarmerLands farmerLands);

    @Query("SELECT * FROM farmer_land WHERE uid = :uid AND farmer_land_uid = :landUid")
    LiveData<FarmerLands> getFarmerLand(String uid, String landUid);

    @Query("SELECT * FROM farmer_land WHERE uid = :uid AND farmer_land_uid = :landUid")
    FarmerLands getFarmerLandDetails(String uid, String landUid);

    @Query("SELECT * FROM farmer_land WHERE farmer_land_uid = :landUid")
    FarmerLands getFarmerLandDetails(String landUid);

    @Query("SELECT * FROM farmer_land ORDER BY created_at DESC")
    LiveData<List<FarmerLands>> getAllFarmerLands();

    @Update
    void updateFarmerLand(FarmerLands farmerLands);

    @Insert
    void insetSurveyEntity(SurveyEntity surveyEntity);

    @Query("SELECT * FROM survey_details where uid = :uid")
    SurveyEntity getSurveyEntity(String uid);

    @Query("SELECT * FROM survey_details where land_uid = :landUid")
    SurveyEntity getSurveyLandEntity(String landUid);


    @Update
    void updateSurveyEntity(SurveyEntity surveyEntity);

    @Query("SELECT * FROM survey_details where land_uid = :landUid")
    LiveData<List<SurveyEntity>> getAllSurveyList(String landUid);

    @Delete
    void deleteSurveyEntity(SurveyEntity surveyEntity);

    @Insert(onConflict = REPLACE)
    void insertSurveyCount(SurveyStatusEntity surveyStatusEntity);

    @Update
    void updateStatusCount(SurveyStatusEntity surveyStatusEntity);

    @Query("SELECT * FROM survey_status_count")
    SurveyStatusEntity getSurveyCountData();

    @Query("SELECT * FROM survey_status_count")
    LiveData<SurveyStatusEntity> getSurveyCount();

    @Query("SELECT * FROM survey_details where isEdit = :isEdit")
    List<SurveyEntity> getAllSurveyEditList(boolean isEdit);

    @Query("SELECT * FROM survey_details where isDelete = :isDelete")
    List<SurveyEntity> getAllSurveyDeleteList(boolean isDelete);

    @Query("SELECT * FROM survey_details where isSync = :isSync")
    List<SurveyEntity> getAllSurveySyncList(boolean isSync);

    @Query("SELECT * FROM farmer_land where is_start = :isStart")
    List<FarmerLands> getAllSurveyStartList(boolean isStart);

    @Query("SELECT * FROM farmer_land where is_submit = :isSubmit")
    List<FarmerLands> getAllSurveySubmitList(boolean isSubmit);

    @Insert
    void insertPropertyData(PropertyData propertyData);

    @Update
    void updatePropertyData(PropertyData propertyData);

    @Query("SELECT * FROM PROPERTY_DATA")
    List<PropertyData> getAllPropertyCreationDataList();

    @Insert
    void insertMapData(MapDataTable mapDataTable);

    @Query("SELECT * FROM MAP_DATA_TABLE WHERE propertyId=:propertyId")
    List<MapDataTable> getAllMapDtaListByPropertyId(int propertyId);

    @Query("SELECT * FROM PROPERTY_DATA WHERE id=:id")
    List<PropertyData> getAllPropertyListByPropertyId(int id);

    @Query("DELETE FROM MAP_DATA_TABLE")
    void deleteMapDataTable();

    @Query("DELETE FROM PROPERTY_DATA")
    void deletePropertyDataTable();

    @Query("UPDATE PROPERTY_DATA SET area=:area WHERE id=:id")
    void updateAreaByPropertyId(int id, String area);

    @Update
    void updateMapEditData(MapDataTable mapDataTable);
}

