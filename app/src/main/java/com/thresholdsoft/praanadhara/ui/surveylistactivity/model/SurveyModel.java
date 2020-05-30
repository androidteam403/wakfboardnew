package com.thresholdsoft.praanadhara.ui.surveylistactivity.model;

import java.io.Serializable;

public class SurveyModel implements Serializable {
    public String farmerName;
    public String farmerAddress;
    public String farmerNumber;
    public String farmerEmail;
    public String date;
    public String status;
    public String statusResult;
    private boolean isChecked;

    public SurveyModel(String farmerName, String farmerAddress, String farmerNumber, String farmerEmail,
                       String date, String status, String statusResult) {
        this.farmerName = farmerName;
        this.farmerAddress = farmerAddress;
        this.farmerNumber = farmerNumber;
        this.farmerEmail = farmerEmail;
        this.date = date;
        this.status = status;
        this.statusResult = statusResult;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getStatusResult() {
        return statusResult;
    }

    public void setStatusResult(String statusResult) {
        this.statusResult = statusResult;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getFarmerAddress() {
        return farmerAddress;
    }

    public void setFarmerAddress(String farmerAddress) {
        this.farmerAddress = farmerAddress;
    }

    public String getFarmerNumber() {
        return farmerNumber;
    }

    public void setFarmerNumber(String farmerNumber) {
        this.farmerNumber = farmerNumber;
    }

    public String getFarmerEmail() {
        return farmerEmail;
    }

    public void setFarmerEmail(String farmerEmail) {
        this.farmerEmail = farmerEmail;
    }
}
