package com.thresholdsoft.wakfboard.data.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PicEntity implements Serializable {


    @Expose
    @SerializedName("path")
    private String path;
    @Expose
    @SerializedName("contentType")
    private String contenttype;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("saved")
    private boolean saved;
    @Expose
    @SerializedName("size")
    private int size;

    public String getPath() {
        return path;
    }

    public String getContenttype() {
        return contenttype;
    }

    public String getName() {
        return name;
    }

    public boolean getSaved() {
        return saved;
    }

    public int getSize() {
        return size;
    }
}
