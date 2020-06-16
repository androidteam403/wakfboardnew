package com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FarmerLandReq {

    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("dependents")
    private DependentsEntity dependents;
    @Expose
    @SerializedName("sort")
    private List<String> sort;
    @Expose
    @SerializedName("rows")
    private int rows = 100;
    @Expose
    @SerializedName("page")
    private int page;

    public String getStatus() {
        return status;
    }

    public DependentsEntity getDependents() {
        return dependents;
    }

    public List<String> getSort() {
        return sort;
    }

    public int getRows() {
        return rows;
    }

    public int getPage() {
        return page;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDependents(DependentsEntity dependents) {
        this.dependents = dependents;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public static class DependentsEntity {
    }
}
