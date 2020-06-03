package com.thresholdsoft.praanadhara.data.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmerSurveyList {

    @Expose
    @SerializedName("data")
    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

}
