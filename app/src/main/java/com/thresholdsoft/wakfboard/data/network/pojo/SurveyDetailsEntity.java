package com.thresholdsoft.wakfboard.data.network.pojo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thresholdsoft.wakfboard.BR;

import java.io.Serializable;

public class SurveyDetailsEntity extends BaseObservable implements Serializable {
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
    @Expose
    @SerializedName("uid")
    private String uid;

    public boolean swipe = false;

    public boolean isSwipe() {
        return swipe;
    }

    public void setSwipe(boolean swipe) {
        this.swipe = swipe;
    }

    public MapTypeEntity getMapType() {
        return mapType;
    }

    public String getLatlongs() {
        return latlongs;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public String getUid() {
        return uid;
    }


    private int surveyType;
    private String displayMapType;
    private boolean isUnChecked;

    public String getDisplayMapType() {
        if (getSurveyType() == 0) {
            displayMapType = "Points";
        } else if (getSurveyType() == 1) {
            displayMapType = "Line";
        } else if (getSurveyType() == 2) {
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
        notifyPropertyChanged(BR.description);
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
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

    public SurveyDetailsEntity() {
    }

    public SurveyDetailsEntity(String name, String description, String latlongs, MapTypeEntity surveyType, String uid) {
        this.description = description;
        this.name = name;
        this.mapType = surveyType;
        this.latlongs = latlongs;
        this.uid = uid;
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

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }
}
