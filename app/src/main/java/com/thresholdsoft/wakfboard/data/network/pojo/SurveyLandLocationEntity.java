package com.thresholdsoft.wakfboard.data.network.pojo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thresholdsoft.wakfboard.BR;

import java.io.Serializable;
import java.util.ArrayList;

public class SurveyLandLocationEntity  extends BaseObservable implements Serializable{
    @Expose
    @SerializedName("start_date")
    private String startDate;
    @Expose
    @SerializedName("submitted_date")
    private String submittedDate;
    @Expose
    @SerializedName("survey_details")
    private ArrayList<SurveyDetailsEntity> surveyDetails;
    @Expose
    @SerializedName("submitted")
    private SubmittedEntity submitted;
    @Expose
    @SerializedName("uid")
    private String uid;

    public String getStartDate() {
        return startDate;
    }

    public String getSubmittedDate() {
        return submittedDate;
    }

    @Bindable
    public ArrayList<SurveyDetailsEntity> getSurveyDetails() {
        return surveyDetails;
    }

    public SubmittedEntity getSubmitted() {
        return submitted;
    }

    @Bindable
    public String getUid() {
        return uid;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setSubmittedDate(String submittedDate) {
        this.submittedDate = submittedDate;
    }

    public void setSurveyDetails(ArrayList<SurveyDetailsEntity> surveyDetails) {
        this.surveyDetails.addAll(surveyDetails);
        notifyPropertyChanged(BR.surveyDetails);
    }

    public void setSubmitted(SubmittedEntity submitted) {
        this.submitted = submitted;
    }

    public void setUid(String uid) {
        this.uid = uid;
        notifyPropertyChanged(BR.uid);
    }
}
