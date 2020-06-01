package com.thresholdsoft.praanadhara.ui.userlogin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginRequest {
    @SerializedName("checkForExists")
    public CheckForExists checkForExists;
    @SerializedName("data")
    public String data;
    @SerializedName("provider")
    public String provider;
    @SerializedName("sender")
    public String sender;
    @SerializedName("type")
    public String type;


    public CheckForExists getCheckForExists() {
        return checkForExists;
    }

    public void setCheckForExists(CheckForExists checkForExists) {
        this.checkForExists = checkForExists;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class CheckForExists implements Serializable {
        public boolean enable;
        public boolean user;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public boolean isUser() {
            return user;
        }

        public void setUser(boolean user) {
            this.user = user;
        }
    }
}
