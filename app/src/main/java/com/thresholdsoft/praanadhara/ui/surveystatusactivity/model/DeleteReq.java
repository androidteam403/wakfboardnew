package com.thresholdsoft.praanadhara.ui.surveystatusactivity.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class DeleteReq implements Serializable {
    @SerializedName("uids")
    public ArrayList<String> uids = new ArrayList<>();

    public ArrayList<String> getUids() {
        return uids;
    }

    public void setUids(ArrayList<String> uids) {
        this.uids = uids;
    }

}
