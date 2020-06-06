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
    private boolean isChecked;

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


    public SurveyModel(double latitude, double longitude, double accuracy, boolean isChecked) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.isChecked = isChecked;
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

    private String polygoneData;

    public String getPolygoneData() {
        return polygoneData;
    }

    public void setPolygoneData(String polygoneData) {
        this.polygoneData = polygoneData;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public SurveyModel(String name, String description, String polygoneData, int mapType, boolean isChecked) {
        this.name = name;
        this.description = description;
        this.polygoneData = polygoneData;
        this.surveyType = mapType;
        this.isChecked = isChecked;
    }

    public static class PolyLineDetails {
        private double fromLatitude;
        private double fromLongitude;
        private double toLatitude;
        private double toLongitude;

        public double getFromLatitude() {
            return fromLatitude;
        }

        public void setFromLatitude(double fromLatitude) {
            this.fromLatitude = fromLatitude;
        }

        public double getFromLongitude() {
            return fromLongitude;
        }

        public void setFromLongitude(double fromLongitude) {
            this.fromLongitude = fromLongitude;
        }

        public double getToLatitude() {
            return toLatitude;
        }

        public void setToLatitude(double toLatitude) {
            this.toLatitude = toLatitude;
        }

        public double getToLongitude() {
            return toLongitude;
        }

        public void setToLongitude(double toLongitude) {
            this.toLongitude = toLongitude;
        }
    }

    public static class PointDetails{
        private double latitude;
        private double longitude;

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

        public PointDetails(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
