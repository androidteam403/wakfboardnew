package com.thresholdsoft.praanadhara.data.db.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.thresholdsoft.praanadhara.data.utils.DateConverter;

@Entity(tableName = "land_details")
@TypeConverters({DateConverter.class})
public class LandEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "uid")
    private String mUid;

    @ColumnInfo(name = "status")
    private String mStatus;

    @ColumnInfo(name = "start_date")
    private String mStartDate;

    @ColumnInfo(name = "submitted_date")
    private String mSubmittedDate;

    public LandEntity(String mUid, String mStatus, String mStartDate, String mSubmittedDate) {
        this.mUid = mUid;
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
}
