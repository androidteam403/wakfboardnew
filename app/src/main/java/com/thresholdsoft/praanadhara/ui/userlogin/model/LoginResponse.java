package com.thresholdsoft.praanadhara.ui.userlogin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginResponse implements Serializable {
    @SerializedName("data")
    public Data data ;
    @SerializedName("message")
    public String message;
    @SerializedName("success")
    private boolean success;
    @SerializedName("zcServerDateTime")
    public String zcServerDateTime;
    @SerializedName("zcServerHost")
    public String zcServerHost;
    @SerializedName("zcServerIp")
    public String zcServerIp;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getZcServerDateTime() {
        return zcServerDateTime;
    }

    public void setZcServerDateTime(String zcServerDateTime) {
        this.zcServerDateTime = zcServerDateTime;
    }

    public String getZcServerHost() {
        return zcServerHost;
    }

    public void setZcServerHost(String zcServerHost) {
        this.zcServerHost = zcServerHost;
    }

    public String getZcServerIp() {
        return zcServerIp;
    }

    public void setZcServerIp(String zcServerIp) {
        this.zcServerIp = zcServerIp;
    }

    public static class Data implements Serializable {
        @SerializedName("key")
        public String key;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
