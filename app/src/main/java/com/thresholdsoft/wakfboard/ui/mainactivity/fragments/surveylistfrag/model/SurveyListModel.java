package com.thresholdsoft.wakfboard.ui.mainactivity.fragments.surveylistfrag.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.thresholdsoft.wakfboard.BR;
import com.thresholdsoft.wakfboard.data.network.pojo.MapTypeEntity;
import com.thresholdsoft.wakfboard.data.network.pojo.SurveyDetailsEntity;
import com.thresholdsoft.wakfboard.ui.surveytrack.model.SurveyModel;
import com.thresholdsoft.wakfboard.utils.CommonUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class SurveyListModel extends BaseObservable implements Serializable {

    private String uid;
    private String name;
    private String address;
    private Long mobile;
    private String email;
    private String pic;
    private String landUid;
    private String status;
    private String startDate;
    private String submitDate;

    public SurveyListModel() {
    }

    public SurveyListModel(String uid, String name, String address, Long mobile, String email, String pic, String landUid, String status, String startDate, String submitDate) {
        this.uid = uid;
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.email = email;
        this.pic = pic;
        this.landUid = landUid;
        this.status = status;
        this.startDate = startDate;
        this.submitDate = submitDate;
    }

    @Bindable
    public String getStartDate() {
        return CommonUtils.dateConversion(startDate);
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
        notifyPropertyChanged(BR.startDate);
    }

    @Bindable
    public String getSubmitDate() {
        return CommonUtils.dateConversion(submitDate);
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
        notifyPropertyChanged(BR.submitDate);
    }

    public String getLandUid() {
        return landUid;
    }

    public void setLandUid(String landUid) {
        this.landUid = landUid;
    }

    public String getUid() {
        return uid;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }


    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }


    private double currentLatitude;
    private double currentLongitude;

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    private ArrayList<SurveyModel> surveyModelArrayList = new ArrayList<>();

    private ArrayList<SurveyDetailsEntity> surveyDetails = new ArrayList<>();

    @Bindable
    public ArrayList<SurveyDetailsEntity> getSurveyDetails() {
        return surveyDetails;
    }

    public void setSurveyDetails(ArrayList<SurveyDetailsEntity> surveyDetails) {
        this.surveyDetails.addAll(surveyDetails);
        notifyPropertyChanged(BR.surveyDetails);
    }

    private MapTypeEntity mapTypeEntity;

    public MapTypeEntity getMapTypeEntity() {
        return mapTypeEntity;
    }

    public void setMapTypeEntity(MapTypeEntity mapTypeEntity) {
        this.mapTypeEntity = mapTypeEntity;
    }

    private int surveyType;

    public int getSurveyType() {
        return surveyType;
    }

    public void setSurveyType(int surveyType) {
        this.surveyType = surveyType;
    }

    private String startUid;

    public String getStartUid() {
        return startUid;
    }

    public void setStartUid(String startUid) {
        this.startUid = startUid;
    }
}
