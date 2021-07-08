package com.thresholdsoft.wakfboard.data.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListdataEntity {
    @Expose
    @SerializedName("rows")
    private List<RowsEntity> rows;
    @Expose
    @SerializedName("page")
    private int page;
    @Expose
    @SerializedName("total")
    private int total;
    @Expose
    @SerializedName("select")
    private boolean select;
    @Expose
    @SerializedName("records")
    private String records;

    public List<RowsEntity> getRows() {
        return rows;
    }

    public int getPage() {
        return page;
    }

    public int getTotal() {
        return total;
    }

    public boolean getSelect() {
        return select;
    }

    public String getRecords() {
        return records;
    }
}
