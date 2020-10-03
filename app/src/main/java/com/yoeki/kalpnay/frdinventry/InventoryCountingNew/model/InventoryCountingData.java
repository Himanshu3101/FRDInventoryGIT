package com.yoeki.kalpnay.frdinventry.InventoryCountingNew.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InventoryCountingData {
    @SerializedName("dataList")
    @Expose
    private ArrayList<ScheduleDetail> scheduleList;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Message")
    @Expose
    private String message;

    public InventoryCountingData(ArrayList<ScheduleDetail> scheduleList, String status, String message) {
        this.scheduleList = scheduleList;
        this.status = status;
        this.message = message;
    }

    public ArrayList<ScheduleDetail> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(ArrayList<ScheduleDetail> scheduleList) {
        this.scheduleList = scheduleList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
