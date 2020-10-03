package com.yoeki.kalpnay.frdinventry.InventoryCountingNew.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduleDetail {
    @SerializedName("SID")
    @Expose
    private String sid;
     @SerializedName("ScheduleActivityNo")
    @Expose
    private String scheduleActivityNo;
    @SerializedName("WareHouseNo")
    @Expose
    private String wareHouseNo;
    @SerializedName("FromTime")
    @Expose
    private String fromTime;
    @SerializedName("ToTime")
    @Expose
    private String toTime;

    public ScheduleDetail(String sid, String scheduleActivityNo, String wareHouseNo, String fromTime, String toTime) {
        this.sid = sid;
        this.scheduleActivityNo = scheduleActivityNo;
        this.wareHouseNo = wareHouseNo;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getScheduleActivityNo() {
        return scheduleActivityNo;
    }

    public void setScheduleActivityNo(String scheduleActivityNo) {
        this.scheduleActivityNo = scheduleActivityNo;
    }

    public String getWareHouseNo() {
        return wareHouseNo;
    }

    public void setWareHouseNo(String wareHouseNo) {
        this.wareHouseNo = wareHouseNo;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }
}
