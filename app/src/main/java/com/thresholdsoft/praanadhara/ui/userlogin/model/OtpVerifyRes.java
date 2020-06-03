package com.thresholdsoft.praanadhara.ui.userlogin.model;

import com.google.gson.annotations.SerializedName;

public class OtpVerifyRes {
    @SerializedName("data")
    public Data data;
    @SerializedName("message")
    public String message;
    @SerializedName("success")
    public boolean success;
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

    public class Data {
        @SerializedName("token")
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
        @SerializedName("zcDebugLogs")
        public String zcDebugLogs;

        public String getZcDebugLogs() {
            return zcDebugLogs;
        }
    }
}
