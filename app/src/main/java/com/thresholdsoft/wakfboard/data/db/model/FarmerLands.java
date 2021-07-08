package com.thresholdsoft.wakfboard.data.db.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.thresholdsoft.wakfboard.data.utils.DateConverter;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "farmer_land")
@TypeConverters({DateConverter.class})
public class FarmerLands implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "page_no")
    private int pageNo;

    @ColumnInfo(name = "uid")
    private String mUid;

    @ColumnInfo(name = "order_no")
    private int orderNo;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "mobile")
    private long mMobile;

    @ColumnInfo(name = "email")
    private String mEmail;

    @ColumnInfo(name = "pic_path")
    private String mPicPath;

    @ColumnInfo(name = "pincode")
    private String mPincode;

    @ColumnInfo(name = "village")
    private String mVillage;

    @ColumnInfo(name = "farmer_land_uid")
    private String mFarmerLandUid;

    @ColumnInfo(name = "survey_land_uid")
    private String mSurveyLandUid;

    @ColumnInfo(name = "status")
    private String mStatus;

    @ColumnInfo(name = "start_date")
    private String mStartDate;

    @ColumnInfo(name = "submitted_date")
    private String mSubmittedDate;

    @ColumnInfo(name = "is_start")
    private boolean isStart;

    @ColumnInfo(name = "is_submit")
    private boolean isSubmit;

    @ColumnInfo(name = "last_update")
    private Date mLastUpdate;

    @ColumnInfo(name = "created_at")
    private Date mCreatedAt;


    public FarmerLands(int pageNo, int orderNo, String mUid, String mName, long mMobile, String mEmail, String mPicPath, String mPincode, String mVillage, String mFarmerLandUid, String mSurveyLandUid, String mStatus, String mStartDate, String mSubmittedDate) {
        this.pageNo = pageNo;
        this.orderNo = orderNo;
        this.mUid = mUid;
        this.mName = mName;
        this.mMobile = mMobile;
        this.mEmail = mEmail;
        this.mPicPath = mPicPath;
        this.mPincode = mPincode;
        this.mVillage = mVillage;
        this.mFarmerLandUid = mFarmerLandUid;
        this.mSurveyLandUid = mSurveyLandUid;
        this.mStatus = mStatus;
        this.mStartDate = mStartDate;
        this.mSubmittedDate = mSubmittedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String mUid) {
        this.mUid = mUid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public long getMobile() {
        return mMobile;
    }

    public void setMobile(long mMobile) {
        this.mMobile = mMobile;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getPicPath() {
        return mPicPath;
    }

    public void setPicPath(String mPicPath) {
        this.mPicPath = mPicPath;
    }

    public String getFarmerLandUid() {
        return mFarmerLandUid;
    }

    public void setFarmerLandUid(String mFarmerLandUid) {
        this.mFarmerLandUid = mFarmerLandUid;
    }

    public String getPincode() {
        return mPincode;
    }

    public void setPincode(String mPincode) {
        this.mPincode = mPincode;
    }

    public String getVillage() {
        return mVillage;
    }

    public void setVillage(String mVillage) {
        this.mVillage = mVillage;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String mStartDate) {
        this.mStartDate = mStartDate;
    }

    public String getSubmittedDate() {
        return mSubmittedDate;
    }

    public void setSubmittedDate(String mSubmittedDate) {
        this.mSubmittedDate = mSubmittedDate;
    }

    public String getSurveyLandUid() {
        return mSurveyLandUid;
    }

    public void setSurveyLandUid(String mSurveyLandUid) {
        this.mSurveyLandUid = mSurveyLandUid;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public boolean isSubmit() {
        return isSubmit;
    }

    public void setSubmit(boolean submit) {
        isSubmit = submit;
    }

    public Date getLastUpdate() {
        return mLastUpdate;
    }

    public void setLastUpdate(Date mLastUpdate) {
        this.mLastUpdate = mLastUpdate;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }
}
