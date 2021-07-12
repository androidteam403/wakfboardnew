package com.thresholdsoft.wakfboard.ui.propertycreation.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thresholdsoft.wakfboard.data.utils.DateConverter;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

@Entity(tableName = "property_data")
@TypeConverters({DateConverter.class})
public class PropertyData implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "propertyName")
    private String propertyName;

    @ColumnInfo(name = "propertyType")
    private String propertyType;

    @ColumnInfo(name = "propertyValue")
    private double propertyValue;

    @ColumnInfo(name = "village")
    private String village;

    @ColumnInfo(name = "mandal")
    private String mandal;

    @ColumnInfo(name = "state")
    private String state;

    @ColumnInfo(name = "district")
    private String district;

    @ColumnInfo(name = "measuredunit")
    private String measuredunit;

    @TypeConverters(ImageUploadTypeConverter.class)
    @ColumnInfo(name = "photosList")
    private List<String> photosList;

    @ColumnInfo(name = "mobileNumber")
    private String mobileNumber;

    @ColumnInfo(name = "propertDate")
    private String propertDate;

    public PropertyData(String propertyName, String propertyType, double propertyValue, String village, String mandal, String state, String district, String measuredunit, List<String> photosList, String mobileNumber, String propertDate) {
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.propertyValue = propertyValue;
        this.village = village;
        this.mandal = mandal;
        this.state = state;
        this.district = district;
        this.measuredunit = measuredunit;
        this.photosList = photosList;
        this.mobileNumber = mobileNumber;
        this.propertDate = propertDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public double getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(double propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getMandal() {
        return mandal;
    }

    public void setMandal(String mandal) {
        this.mandal = mandal;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getMeasuredunit() {
        return measuredunit;
    }

    public void setMeasuredunit(String measuredunit) {
        this.measuredunit = measuredunit;
    }

    public List<String> getPhotosList() {
        return photosList;
    }

    public void setPhotosList(List<String> photosList) {
        this.photosList = photosList;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPropertDate() {
        return propertDate;
    }

    public void setPropertDate(String propertDate) {
        this.propertDate = propertDate;
    }

    public static class ImageUploadTypeConverter {
        @TypeConverter
        public List<String> fromString(String value) {
            Type listType = new TypeToken<List<String>>() {
            }.getType();
            return new Gson().fromJson(value, listType);
        }

        @TypeConverter
        public String fromArrayList(List<String> list) {
            Gson gson = new Gson();
            String json = gson.toJson(list);
            return json;
        }
    }
}
