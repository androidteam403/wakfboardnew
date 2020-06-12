package com.thresholdsoft.praanadhara.data.db.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.thresholdsoft.praanadhara.data.utils.DateConverter;

@Entity(tableName = "survey_status_count")
@TypeConverters({DateConverter.class})
public class SurveyStatusEntity {

    @PrimaryKey(autoGenerate = false)
    private int id;

    @ColumnInfo(name = "inProgress")
    private int mInProgress;

    @ColumnInfo(name = "completed")
    private int mCompleted;

    @ColumnInfo(name =  "new")
    private int mNew;

    @ColumnInfo(name = "total")
    private int mTotal;

    public SurveyStatusEntity(int mInProgress, int mCompleted, int mNew, int mTotal) {
        this.mInProgress = mInProgress;
        this.mCompleted = mCompleted;
        this.mNew = mNew;
        this.mTotal = mTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInProgress() {
        return mInProgress;
    }

    public void setInProgress(int mInProgress) {
        this.mInProgress = mInProgress;
    }

    public int getCompleted() {
        return mCompleted;
    }

    public void setCompleted(int mCompleted) {
        this.mCompleted = mCompleted;
    }

    public int getNew() {
        return mNew;
    }

    public void setNew(int mNew) {
        this.mNew = mNew;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int mTotal) {
        this.mTotal = mTotal;
    }
}
