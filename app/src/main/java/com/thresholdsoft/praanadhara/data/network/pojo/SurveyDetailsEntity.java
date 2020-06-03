package com.thresholdsoft.praanadhara.data.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SurveyDetailsEntity implements Serializable {
    @Expose
    @SerializedName("map_type")
    private MapTypeEntity mapType;
    @Expose
    @SerializedName("latlongs")
    private String latlongs;
    @Expose
    @SerializedName("description")
    private String description;
    @Expose
    @SerializedName("uid")
    private String uid;

    public MapTypeEntity getMapType() {
        return mapType;
    }

    public String getLatlongs() {
        return latlongs;
    }

    public String getDescription() {
        return description;
    }

    public String getUid() {
        return uid;
    }
}
