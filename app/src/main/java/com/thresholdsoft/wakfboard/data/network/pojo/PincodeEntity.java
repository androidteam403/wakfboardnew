package com.thresholdsoft.wakfboard.data.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PincodeEntity implements Serializable {
    @Expose
    @SerializedName("village")
    private VillageEntity village;
    @Expose
    @SerializedName("pincode")
    private String pincode;
    @Expose
    @SerializedName("uid")
    private String uid;

    public VillageEntity getVillage() {
        return village;
    }

    public String getPincode() {
        return pincode;
    }

    public String getUid() {
        return uid;
    }
}
