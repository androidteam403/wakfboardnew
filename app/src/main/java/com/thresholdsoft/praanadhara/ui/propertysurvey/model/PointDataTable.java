package com.thresholdsoft.praanadhara.ui.propertysurvey.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thresholdsoft.praanadhara.data.utils.DateConverter;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "point_data_table")
@TypeConverters({DateConverter.class})
public class PointDataTable implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @TypeConverters(ImageUploadTypeConverter.class)
    @ColumnInfo(name = "pointPhotoData")
    private List<String> pointPhotoData;

    public PointDataTable(double latitude, double longitude, String name, String description,List<String> pointPhotoData) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.description = description;
        this.pointPhotoData=pointPhotoData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

