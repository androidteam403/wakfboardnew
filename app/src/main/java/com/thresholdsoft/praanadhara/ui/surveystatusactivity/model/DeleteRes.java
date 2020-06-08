package com.thresholdsoft.praanadhara.ui.surveystatusactivity.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class DeleteRes implements Serializable {
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

    public class Data {
        @SerializedName("message")
        public String message;
        @SerializedName("success")
        public boolean success;
        @SerializedName("uids")
        ArrayList<Uids> uids = new ArrayList<Uids>();
        @SerializedName("zcDebugLogs")
        public ZcDebugLogs zcDebugLogs;

        public class Uids {
            @SerializedName("0")
            public String zero;
        }

        public class ZcDebugLogs {
            @SerializedName("1")
            public ArrayList<One> one = new ArrayList<One>();

            public class One {
                @SerializedName("context")
                public String context;
                @SerializedName("log")
                public String log;
                @SerializedName("place")
                public String place;
                @SerializedName("time")
                public String time;
            }
        }
    }

}
