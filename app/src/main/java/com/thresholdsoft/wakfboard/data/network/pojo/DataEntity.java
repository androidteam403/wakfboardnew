package com.thresholdsoft.wakfboard.data.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataEntity {
    @Expose
    @SerializedName("listData")
    private ListdataEntity listdata;

    public ListdataEntity getListdata() {
        return listdata;
    }
}
