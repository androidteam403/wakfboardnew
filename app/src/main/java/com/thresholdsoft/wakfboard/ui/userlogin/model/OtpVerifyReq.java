package com.thresholdsoft.wakfboard.ui.userlogin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OtpVerifyReq implements Serializable {
    @SerializedName("appUserName")
    public String appUserName;
    @SerializedName("key")
    public String key;
    @SerializedName("otp")
    public String otp;
    @SerializedName("otpBased")
    public String otpBased;
    @SerializedName("otpKey")
    public String otpKey;
    @SerializedName("otpType")
    public String otpType;

    public String getAppUserName() {
        return appUserName;
    }

    public void setAppUserName(String appUserName) {
        this.appUserName = appUserName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getOtpBased() {
        return otpBased;
    }

    public void setOtpBased(String otpBased) {
        this.otpBased = otpBased;
    }

    public String getOtpKey() {
        return otpKey;
    }

    public void setOtpKey(String otpKey) {
        this.otpKey = otpKey;
    }

    public String getOtpType() {
        return otpType;
    }

    public void setOtpType(String otpType) {
        this.otpType = otpType;
    }
}
