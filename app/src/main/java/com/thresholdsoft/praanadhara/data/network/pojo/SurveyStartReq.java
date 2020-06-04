package com.thresholdsoft.praanadhara.data.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class SurveyStartReq {


    @Expose
    @SerializedName("land_location")
    private LandLocationEntity landLocation;

    public void setLandLocation(LandLocationEntity landLocation) {
        this.landLocation = landLocation;
    }

     public SurveyStartReq(LandLocationEntity landLocation) {
         this.landLocation = landLocation;
     }

     public static class LandLocationEntity {
        @Expose
        @SerializedName("uid")
        private String uid;

        public void setUid(String uid) {
            this.uid = uid;
        }

         public LandLocationEntity(String uid) {
             this.uid = uid;
         }
     }
}
