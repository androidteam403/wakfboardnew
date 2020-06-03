package com.thresholdsoft.praanadhara.data.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SurveyLandLocationEntity implements Serializable {
    @Expose
    @SerializedName("start_date")
    private String startDate;
    @Expose
    @SerializedName("submitted_date")
    private String submittedDate;
    @Expose
    @SerializedName("survey_details")
    private SurveyDetailsEntity surveyDetails;
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

    public SurveyDetailsEntity getSurveyDetails() {
        return surveyDetails;
    }

    public SubmittedEntity getSubmitted() {
        return submitted;
    }

    public String getUid() {
        return uid;
    }
}
