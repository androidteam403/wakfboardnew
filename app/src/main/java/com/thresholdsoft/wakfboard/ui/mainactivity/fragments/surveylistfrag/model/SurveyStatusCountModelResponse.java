package com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.model;

import com.google.gson.annotations.SerializedName;


public class SurveyStatusCountModelResponse  {


    @SerializedName("Completed")
    public int Completed;
    @SerializedName("InProgress")
    public int InProgress;
    @SerializedName("New")
    public int New;
    @SerializedName("Total")
    public int Total;


    public int getCompleted() {
        return Completed;
    }

    public void setCompleted(int completed) {
        Completed = completed;
    }

    public int getInProgress() {
        return InProgress;
    }

    public void setInProgress(int inProgress) {
        InProgress = inProgress;
    }

    public int getNew() {
        return New;
    }

    public void setNew(int aNew) {
        New = aNew;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }


}
