package com.thresholdsoft.wakfboard.ui.propertysurvey.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thresholdsoft.wakfboard.data.utils.DateConverter;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

@Entity(tableName = "map_data_table")
@TypeConverters({DateConverter.class})
public class MapDataTable implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "propertyId")
    private int propertyID;

    @ColumnInfo(name = "mapType")
    private int mapType;

    @TypeConverters(LatLngTypeConverter.class)
    @ColumnInfo(name = "latlngList")
    List<LatLng> latLngList;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @TypeConverters(ImageUploadTypeConverter.class)
    @ColumnInfo(name = "pointPhotoData")
    private List<String> pointPhotoData;

    @Ignore
    private boolean isChecked = true;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public MapDataTable(int propertyID, int mapType, List<LatLng> latLngList, String name, String description, List<String> pointPhotoData) {
        this.propertyID = propertyID;
        this.mapType = mapType;
        this.latLngList = latLngList;
        this.name = name;
        this.description = description;
        this.pointPhotoData = pointPhotoData;
    }

    public int getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(int propertyID) {
        this.propertyID = propertyID;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<LatLng> getLatLngList() {
        return latLngList;
    }

    public void setLatLngList(List<LatLng> latLngList) {
        this.latLngList = latLngList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPointPhotoData() {
        return pointPhotoData;
    }

    public void setPointPhotoData(List<String> pointPhotoData) {
        this.pointPhotoData = pointPhotoData;
    }

    public static class LatLngTypeConverter implements Serializable{
        @TypeConverter
        public List<LatLng> fromString(String value) {
            Type listType = new TypeToken<List<LatLng>>() {
            }.getType();
            return new Gson().fromJson(value, listType);
        }

        @TypeConverter
        public String fromArrayList(List<LatLng> list) {
            Gson gson = new Gson();
            String json = gson.toJson(list);
            return json;
        }
    }


    public static class ImageUploadTypeConverter implements Serializable{
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

