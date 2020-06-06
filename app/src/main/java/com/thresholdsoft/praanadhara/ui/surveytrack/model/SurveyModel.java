package com.thresholdsoft.praanadhara.ui.surveytrack.model;

import java.io.Serializable;

public class SurveyModel implements Serializable {

    private double latitude;
    private double longitude;
    private double accuracy;
    private boolean isPoint;
    private String name;
    private String description;
    private int surveyType;
    private String displayMapType;

    public String getDisplayMapType() {
        if(surveyType == 0){
            displayMapType = "Points";
        }else if(surveyType == 1){
            displayMapType = "Lines";
        }else if(surveyType == 2){
            displayMapType = "Plygone";
        }
        return displayMapType;
    }


    public SurveyModel(double latitude, double longitude, double accuracy) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
    }

    public SurveyModel(double latitude, double longitude, double accuracy, boolean isPoint, String name, String description,int mapType) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.isPoint = isPoint;
        this.name = name;
        this.description = description;
        this.surveyType = mapType;
    }

    public int getSurveyType() {
        return surveyType;
    }

    public void setSurveyType(int surveyType) {
        this.surveyType = surveyType;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public boolean isPoint() {
        return isPoint;
    }

    public void setPoint(boolean point) {
        isPoint = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
