package com.thresholdsoft.praanadhara.data.db.model;

import android.location.Location;

import com.thresholdsoft.praanadhara.data.utils.DateConverter;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


/**
 * Created on : March 30, 2020
 * Author     : JAGADEESH
 */
@Entity(tableName = "survey")
@TypeConverters({DateConverter.class})
public class Survey {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "survey_id")
    private String mSurveyId;

    @ColumnInfo(name = "survey_type")
    private int mSurveyType;

    @ColumnInfo(name = "Lat")
    private double mLatitude;

    @ColumnInfo(name = "Long")
    private double mLongitude;

    @ColumnInfo(name = "accuracy")
    private double mAccuracy;

    @ColumnInfo(name = "is_point")
    private boolean mIsPoint;

    @ColumnInfo(name = "created_at")
    private Date mCreatedAt;

    @ColumnInfo(name = "last_update")
    private Date mLastUpdate;

    public Survey(String mSurveyId, int mSurveyType, double mLatitude, double mLongitude, double mAccuracy, boolean mIsPoint, Date mCreatedAt, Date mLastUpdate) {
        this.mSurveyId = mSurveyId;
        this.mSurveyType = mSurveyType;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mAccuracy = mAccuracy;
        this.mIsPoint = mIsPoint;
        this.mCreatedAt = mCreatedAt;
        this.mLastUpdate = mLastUpdate;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getSurveyId() {
        return mSurveyId;
    }

    public void setSurveyId(String mSurveyId) {
        this.mSurveyId = mSurveyId;
    }

    public int getSurveyType() {
        return mSurveyType;
    }

    public void setSurveyType(int mSurveyType) {
        this.mSurveyType = mSurveyType;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public double getAccuracy() {
        return mAccuracy;
    }

    public void setAccuracy(double mAccuracy) {
        this.mAccuracy = mAccuracy;
    }

    public boolean isIsPoint() {
        return mIsPoint;
    }

    public void setIsPoint(boolean mIsPoint) {
        this.mIsPoint = mIsPoint;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    public Date getLastUpdate() {
        return mLastUpdate;
    }

    public void setLastUpdate(Date mLastUpdate) {
        this.mLastUpdate = mLastUpdate;
    }
}
