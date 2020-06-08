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


    private String name;
    private int surveyType;
    private String displayMapType;
    private boolean isUnChecked;

    public String getDisplayMapType() {
        if(getSurveyType() == 0){
            displayMapType = "Points";
        }else if(getSurveyType() == 1){
            displayMapType = "Line";
        }else if(getSurveyType() == 2){
            displayMapType = "Polygon";
        }
        return displayMapType;
    }

    public int getSurveyType() {
        return surveyType;
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

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurveyType(int surveyType) {
        this.surveyType = surveyType;
    }

    public void setDisplayMapType(String displayMapType) {
        this.displayMapType = displayMapType;
    }

    public boolean isUnChecked() {
        return isUnChecked;
    }

    public void setUnChecked(boolean checked) {
        isUnChecked = checked;
    }



    public SurveyDetailsEntity(String description, String name, String latlongs, MapTypeEntity surveyType) {
        this.description = description;
        this.name = name;
        this.mapType = surveyType;
        this.latlongs = latlongs;
    }

    private double latitude;
    private double longitude;
    private double accuracy;

    public SurveyDetailsEntity(double latitude, double longitude, double accuracy) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAccuracy() {
        return accuracy;
    }
}
