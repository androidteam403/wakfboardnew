package com.thresholdsoft.praanadhara.data.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SurveyStartRes {


        @Expose
        @SerializedName("isUpdate")
        private boolean isupdate;
        @Expose
        @SerializedName("uid")
        private String uid;

        public boolean getIsupdate() {
            return isupdate;
        }

        public String getUid() {
            return uid;
        }

}
