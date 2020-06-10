package com.thresholdsoft.praanadhara.data.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SurveySaveReq {


    @Expose
    @SerializedName("uid")
    private String uid;
    @Expose
    @SerializedName("survey")
    private SurveyEntity survey;
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
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurvey(SurveyEntity survey) {
        this.survey = survey;
    }

    public void setMapType(MapTypeEntity mapType) {
        this.mapType = mapType;
    }

    public void setLatlongs(String latlongs) {
        this.latlongs = latlongs;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SurveyEntity getSurvey() {
        return survey;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public static class SurveyEntity {
        @Expose
        @SerializedName("uid")
        private String uid;

        public void setUid(String uid) {
            this.uid = uid;
        }

        public SurveyEntity(String uid) {
            this.uid = uid;
        }
    }


}
