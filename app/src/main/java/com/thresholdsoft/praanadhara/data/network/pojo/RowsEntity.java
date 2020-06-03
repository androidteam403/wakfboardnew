package com.thresholdsoft.praanadhara.data.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RowsEntity implements Serializable {


    @Expose
    @SerializedName("pic")
    private List<PicEntity> pic;
    @Expose
    @SerializedName("farmer_land")
    private FarmerLandEntity farmerLand;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("gender")
    private GenderEntity gender;
    @Expose
    @SerializedName("father_name")
    private String fatherName;
    @Expose
    @SerializedName("farmer_type")
    private FarmerTypeEntity farmerType;
    @Expose
    @SerializedName("mobile")
    private long mobile;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("uid")
    private String uid;


    public List<PicEntity> getPic() {
        return pic;
    }

    public FarmerLandEntity getFarmerLand() {
        return farmerLand;
    }

    public String getName() {
        return name;
    }

    public GenderEntity getGender() {
        return gender;
    }

    public String getFatherName() {
        return fatherName;
    }

    public FarmerTypeEntity getFarmerType() {
        return farmerType;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public long getMobile() {
        return mobile;
    }

    private boolean isChecked;
    private int surveyType;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getSurveyType() {
        return surveyType;
    }

    public void setSurveyType(int surveyType) {
        this.surveyType = surveyType;
    }
}