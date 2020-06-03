package com.thresholdsoft.praanadhara.data.network.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created on : March 30, 2020
 * Author     : JAGADEESH
 */
public class WrapperResponse<T> {
    @SerializedName("data")
    private T mData;
    @SerializedName("success")
    private Boolean mSuccess;
    @SerializedName("message")
    private String mMessage;

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        mData = data;
    }

    public Boolean getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Boolean mSuccess) {
        this.mSuccess = mSuccess;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

}
