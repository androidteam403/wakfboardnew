package com.thresholdsoft.praanadhara.data.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FarmerLandEntity implements Serializable {
    @Expose
    @SerializedName("survey_land_location")
    private SurveyLandLocationEntity surveyLandLocation;
    @Expose
    @SerializedName("pincode")
    private PincodeEntity pincode;
    @Expose
    @SerializedName("survey_no")
    private String surveyNo;
    @Expose
    @SerializedName("passbook_no")
    private String passbookNo;
    @Expose
    @SerializedName("katha_no")
    private String kathaNo;
    @Expose
    @SerializedName("extent")
    private int extent;
    @Expose
    @SerializedName("uid")
    private String uid;

    public SurveyLandLocationEntity getSurveyLandLocation() {
        return surveyLandLocation;
    }

    public PincodeEntity getPincode() {
        return pincode;
    }

    public String getSurveyNo() {
        return surveyNo;
    }

    public String getPassbookNo() {
        return passbookNo;
    }

    public String getKathaNo() {
        return kathaNo;
    }

    public int getExtent() {
        return extent;
    }

    public String getUid() {
        return uid;
    }
}
