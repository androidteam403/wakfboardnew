package com.thresholdsoft.wakfboard.data.network.pojo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thresholdsoft.wakfboard.BR;

import java.io.Serializable;

public class FarmerLandEntity extends BaseObservable implements Serializable {
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
    private double extent;
    @Expose
    @SerializedName("uid")
    private String uid;

    @Bindable
    public SurveyLandLocationEntity getSurveyLandLocation() {
        return surveyLandLocation;
    }

    public void setSurveyLandLocation(SurveyLandLocationEntity surveyLandLocation) {
        this.surveyLandLocation = surveyLandLocation;
        notifyPropertyChanged(BR.surveyLandLocation);
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

    public double getExtent() {
        return extent;
    }

    public String getUid() {
        return uid;
    }
}
