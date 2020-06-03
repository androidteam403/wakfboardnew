package com.thresholdsoft.praanadhara.data.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MapTypeEntity implements Serializable {
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("uid")
    private String uid;

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }
}
