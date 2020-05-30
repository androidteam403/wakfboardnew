package com.thresholdsoft.praanadhara.data.db.dao;

import com.thresholdsoft.praanadhara.data.db.model.Survey;

import java.util.ArrayList;
import java.util.List;

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
}

